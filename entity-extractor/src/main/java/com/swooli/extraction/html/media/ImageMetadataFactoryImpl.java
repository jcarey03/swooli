package com.swooli.extraction.html.media;

import com.swooli.bo.video.ImageReference;
import com.swooli.extraction.html.entity.ImageEntity;
import com.swooli.extraction.html.entity.MetaEntity;
import com.swooli.extraction.html.util.HtmlUtil;
import com.swooli.extraction.html.util.UrlParser;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageMetadataFactoryImpl implements ImageMetadataFactory<ImageMetadata> {

    public static final int DEFAULT_CACHE_SIZE = 1000;

    private static final Logger logger = LoggerFactory.getLogger(ImageMetadataFactoryImpl.class);

    private int minPixels = 50;

    private int maxPixels = 1024;

    private double maxAspectRatio = 3;

    private int readTimeout = 1000;

    private int connectionTimeout = 1000;

    private int cacheSize;

    private LinkedHashMap<String, ImageInfo> lruImageInfoCache;

    private Map<String, List<String>> acceptedContentTypes = new HashMap<>();

    public ImageMetadataFactoryImpl() {
        this(DEFAULT_CACHE_SIZE);
    }

    public ImageMetadataFactoryImpl(final int cacheSize) {

        if(cacheSize > 0) {
            lruImageInfoCache = new LinkedHashMap<String, ImageInfo>(cacheSize + 1, 1.0F, true) {
                @Override
                protected boolean removeEldestEntry(final Map.Entry<String, ImageInfo> eldest) {
                    return lruImageInfoCache.size() > cacheSize;
                }
            };
        }
        this.cacheSize = cacheSize;

        acceptedContentTypes.put("image/jpeg", Arrays.asList(".jpg", ".jpeg", ".jpe", ".jif", ".jfif", ".jfi"));
        acceptedContentTypes.put("image/gif", Arrays.asList(".gif"));
        acceptedContentTypes.put("image/png", Arrays.asList(".png"));
    }

    @Override
    public List<ImageMetadata> create(final List<MetaEntity> entities) {
        final List<ImageMetadata> imageMetadataList = new ArrayList<>();
        for(final MetaEntity entity : entities) {
            final String propertyNoNamespace = HtmlUtil.stripOpenGraphNamespacePrefix(entity.getProperty());
            if(propertyNoNamespace.equals("image")) {

                final ImageEntity imageEntity = new ImageEntity(entity.getOrigin());
                imageEntity.setSrc(entity.getContent());

                final ImageInfo imageInfo = getImageInfo(imageEntity);
                if(accept(imageInfo)) {
                    final ImageMetadata imageMetadata = new ImageMetadata();

                    final ImageReference imageReference = new ImageReference();
                    imageMetadata.setImageReference(imageReference);
                    imageReference.setOriginUri(imageInfo.originUri);
                    imageReference.setImageUri(imageInfo.srcUri);

                    imageMetadata.setOpenGraph(true);

                    imageMetadataList.add(imageMetadata);
                }
            }
        }
        return imageMetadataList;
    }

    @Override
    public ImageMetadata create(final ImageEntity entity) {
        final ImageInfo imageInfo = getImageInfo(entity);
        if(accept(imageInfo)) {

            final ImageMetadata imageMetadata = new ImageMetadata();

            final ImageReference imageReference = new ImageReference();
            imageMetadata.setImageReference(imageReference);
            imageReference.setOriginUri(imageInfo.originUri);
            imageReference.setImageUri(imageInfo.srcUri);

            return imageMetadata;
        }
        return null;
    }

    public int getMinPixels() {
        return minPixels;
    }

    public void setMinPixels(final int minPixels) {
        this.minPixels = minPixels;
    }

    public int getMaxPixels() {
        return maxPixels;
    }

    public void setMaxPixels(final int maxPixels) {
        this.maxPixels = maxPixels;
    }

    public double getMaxAspectRatio() {
        return maxAspectRatio;
    }

    public void setMaxAspectRatio(final double maxAspectRatio) {
        this.maxAspectRatio = maxAspectRatio;
    }

    public Map<String, List<String>> getAcceptedContentTypes() {
        return acceptedContentTypes;
    }

    public void setAcceptedContentTypes(final Map<String, List<String>> acceptedContentTypes) {
        this.acceptedContentTypes = acceptedContentTypes;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(final int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(final int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    private ImageInfo getImageInfo(final ImageEntity entity) {

        final ImageInfo imageInfo = new ImageInfo();

        final UrlParser parser = new UrlParser();

        try {
            imageInfo.originUri = parser.parseUrl(entity.getOrigin()).toUri();
        } catch(final URISyntaxException use) {
            logger.warn("Invalid URI: {}", entity.getOrigin());
            return null;
        }

        final String absoluteSrcUri = HtmlUtil.toAbsolute(entity.getOrigin(), entity.getSrc());

        // check cache
        final ImageInfo cachedImageInfo = getFromCache(absoluteSrcUri);
        if(isFailure(cachedImageInfo)) {
            logger.info("'Failure' cache hit for {}", ((FailedImageInfo) cachedImageInfo).url);
            return null;
        } else if(cachedImageInfo != null) {
            logger.info("Cache hit for {}", cachedImageInfo.srcUri);
            return cachedImageInfo;
        }

        try {
            imageInfo.srcUri = parser.parseUrl(absoluteSrcUri).toUri();
        } catch(final URISyntaxException use) {
            logger.warn("Invalid URI: {}", entity.getSrc());
            addToCache(absoluteSrcUri, new FailedImageInfo(absoluteSrcUri));
            return null;
        }

        // try to infer image info from entity
        if(entity.getWidth() > 0 && entity.getHeight() > 0) {
            imageInfo.width = entity.getWidth();
            imageInfo.height = entity.getHeight();
            imageInfo.contentType = getContentType(imageInfo.srcUri.getPath());
            addToCache(absoluteSrcUri, imageInfo);
            return imageInfo;
        }

        // could not infer image info, so try downloading it

        final HttpClient client = createHttpClient();

        final HttpGet httpGet;
        try {
            httpGet = new HttpGet(imageInfo.srcUri);
        } catch(final IllegalArgumentException iae) {
            logger.warn("Failed to get image info for URL {} due to {}", imageInfo.srcUri, iae);
            addToCache(absoluteSrcUri, new FailedImageInfo(absoluteSrcUri));
            return null;
        }

        final ResponseHandler<ImageInfo> handler = new ResponseHandler<ImageInfo>() {
            @Override
            public ImageInfo handleResponse(final HttpResponse response) {

                if(response.getStatusLine().getStatusCode() != 200) {
                    logger.warn("Failed downloading image for {}.  Status line: '{}'", imageInfo.srcUri, response.getStatusLine());
                    return null;
                }

                try {
                    final Header contentType = response.getFirstHeader("content-type");
                    if(contentType != null && StringUtils.isNotBlank(contentType.getValue())) {
                        final BufferedImage bufferedImg = ImageIO.read(response.getEntity().getContent());
                        if(bufferedImg != null) {
                            imageInfo.width = bufferedImg.getWidth();
                            imageInfo.height = bufferedImg.getHeight();
                            imageInfo.contentType = contentType.getValue();
                            return imageInfo;
                        }
                    }
                } catch(final IOException ioe) {
                    logger.warn("Failed to decode image for {} due to {}", imageInfo.srcUri, ioe);
                }

                return null;

            }
        };

        try {
            if(client.execute(httpGet, handler) == null) {
                addToCache(absoluteSrcUri, new FailedImageInfo(absoluteSrcUri));
                return null;
            } else {
                addToCache(absoluteSrcUri, imageInfo);
                return imageInfo;
            }
        } catch(final IOException ioe) {
            logger.warn("Failed creating image info for {} due to {}", imageInfo.srcUri, ioe);
            addToCache(absoluteSrcUri, new FailedImageInfo(absoluteSrcUri));
            return null;
        } finally {
            client.getConnectionManager().shutdown();
        }

    }

    private boolean accept(final ImageInfo imageInfo) {

        if(imageInfo == null) {
            return false;
        }

        final boolean acceptWidth = imageInfo.width >= minPixels && imageInfo.width <= maxPixels;
        final boolean acceptHeight = imageInfo.height >= minPixels && imageInfo.height <= maxPixels;
        final boolean acceptAspectRatio = imageInfo.getAspectRatio() > 0 && imageInfo.getAspectRatio() <= maxAspectRatio;
        final boolean acceptContentType;
        if(imageInfo.contentType == null) {
            acceptContentType = false;
        } else {
            acceptContentType = acceptedContentTypes.containsKey(imageInfo.contentType);
        }

        return (acceptWidth && acceptHeight && acceptAspectRatio && acceptContentType);
    }

    private String getContentType(final String value) {
        for(final Map.Entry<String, List<String>> entry : acceptedContentTypes.entrySet()) {
            for(final String extension : entry.getValue()) {
                if(value.endsWith(extension)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    private HttpClient createHttpClient() {

        final HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, getConnectionTimeout());
        HttpConnectionParams.setSoTimeout(params, getReadTimeout());

        return new DefaultHttpClient(params);
    }

    private static class ImageInfo {

        private int width;

        private int height;

        private URI originUri;

        private URI srcUri;

        private String contentType;

        public double getAspectRatio() {
            if(width == 0 || height == 0) {
                return 0;
            } else if(width >= height) {
                return (double) width / height;
            } else {
                return (double) height / width;
            }
        }

    }

    private void addToCache(final String imageSrc, final ImageInfo imageInfo) {
        if(lruImageInfoCache != null) {
            lruImageInfoCache.put(imageSrc, imageInfo);
        }
    }

    private ImageInfo getFromCache(final String imageSrc) {
        if(lruImageInfoCache == null) {
            return null;
        } else {
            return lruImageInfoCache.get(imageSrc);
        }
    }

    private boolean isFailure(final ImageInfo imageInfo) {
        return (imageInfo instanceof FailedImageInfo);
    }

    // sentinel value for denoting failed retrievals
    private static class FailedImageInfo extends ImageInfo {

        private String url;

        public FailedImageInfo(final String url) {
            this.url = url;
        }

    }


}