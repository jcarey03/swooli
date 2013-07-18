package com.swooli.extraction.html.media;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swooli.bo.video.VideoReference;
import com.swooli.extraction.html.entity.EmbedEntity;
import com.swooli.extraction.html.entity.IFrameEntity;
import com.swooli.extraction.html.entity.MetaEntity;
import com.swooli.extraction.html.media.DailyMotionVideoMetadataFactory.VideoStrategy;
import com.swooli.extraction.html.util.HtmlUtil;
import com.swooli.extraction.html.util.StringMultivaluedMap;
import com.swooli.extraction.html.util.UrlInfo;
import com.swooli.extraction.html.util.UrlParser;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpEntity;
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

public class DailyMotionVideoMetadataFactory implements VideoMetadataFactory<VideoMetadata> {

    public static final String VIDEO_METADATA_PATTERN = "https://api.dailymotion.com/video/%s?fields=title,thumbnail_medium_url";

    public static final String BASE_VIDEO_URL_PATTERN = "http%s://www.dailymotion.com/embed/video/%s";

    public static final int DEFAULT_CACHE_SIZE = 1000;

    private static final Logger logger = LoggerFactory.getLogger(DailyMotionVideoMetadataFactory.class);

    private boolean secure = false;

    private StringMultivaluedMap  videoUriParams = new StringMultivaluedMap();

    private LinkedHashMap<String, VideoMetadata> lruVideoMetadataCache;

    private String thumbnailFieldName = "thumbnail_medium_url";

    private String titleFieldName = "title";

    private int readTimeout = 1000;

    private int connectionTimeout = 1000;

    private int cacheSize;

    public static enum VideoStrategy {

        IFRAME {

            @Override
            public String extractVideoId(final String url) {

                // example: http://www.dailymotion.com/embed/video/id

                final UrlParser parser = new UrlParser();
                final UrlInfo urlInfo = parser.parseUrl(url);
                if("www.dailymotion.com".equals(urlInfo.getHost())) {
                    if(urlInfo.getPath() != null && urlInfo.getPath().startsWith("/embed//video/")) {
                        final String id = urlInfo.getPath().substring("/embed/video/".length());
                        if(id.contains("/")) {
                            return null;
                        } else {
                            return id;
                        }
                    }
                }
                return null;
            }
        },

        OBJ_EMBED {

            @Override
            public String extractVideoId(final String url) {

                // example: http://www.dailymotion.com/swf/video

                final UrlParser parser = new UrlParser();
                final UrlInfo urlInfo = parser.parseUrl(url);
                if("www.dailymotion.com".equals(urlInfo.getHost())) {
                    if(urlInfo.getPath() != null && urlInfo.getPath().startsWith("/swf/video/")) {
                        final String id = urlInfo.getPath().substring("/swf/video/".length());
                        if(id.contains("/")) {
                            return null;
                        } else {
                            return id;
                        }
                    }
                }
                return null;
            }
        },

        DEFAULT {

            @Override
            public String extractVideoId(final String url) {

                // example: http://www.dailymotion.com/video/

                final UrlParser parser = new UrlParser();
                final UrlInfo urlInfo = parser.parseUrl(url);
                if("www.dailymotion.com".equals(urlInfo.getHost())) {
                    if(urlInfo.getPath() != null && urlInfo.getPath().startsWith("/video/")) {
                        final String id = urlInfo.getPath().substring("/video/".length());
                        if(id.contains("/")) {
                            return null;
                        } else {
                            return id;
                        }
                    }
                }
                return null;
            }
        };

        public abstract String extractVideoId(final String url);

    }

    public DailyMotionVideoMetadataFactory() {
        this(DEFAULT_CACHE_SIZE);
    }

    public DailyMotionVideoMetadataFactory(final int cacheSize) {

        if(cacheSize > 0) {
            lruVideoMetadataCache = new LinkedHashMap<String, VideoMetadata>(cacheSize + 1, 1.0F, true) {
                @Override
                protected boolean removeEldestEntry(final Map.Entry<String, VideoMetadata> eldest) {
                    return lruVideoMetadataCache.size() > cacheSize;
                }
            };
        }
        this.cacheSize = cacheSize;
    }

    @Override
    public VideoMetadata create(final IFrameEntity entity) {
        return createFromMediaSource(entity.getOrigin(), entity.getSrc());
    }

    @Override
    public VideoMetadata create(final EmbedEntity entity) {
        return createFromMediaSource(entity.getOrigin(), entity.getSrc());
    }

    @Override
    public List<VideoMetadata> create(final List<MetaEntity> entities) {

        final List<VideoMetadata> videoMetadataList = new ArrayList<>();

        for(final MetaEntity entity : entities) {
            final String property = HtmlUtil.stripOpenGraphNamespacePrefix(entity.getProperty());
            switch (property) {
                case "video":
                    final VideoMetadata metadata = createFromMediaSource(entity.getOrigin(), entity.getContent());
                    if(metadata != null) {
                        metadata.setOpenGraph(true);
                        videoMetadataList.add(metadata);
                    }
                    break;
            }
        }

        return videoMetadataList;
    }

    private VideoMetadata createFromMediaSource(final String origin, final String src) {
        final UrlParser parser = new UrlParser();
        for(final VideoStrategy strategy : VideoStrategy.values()) {
            final String videoId = strategy.extractVideoId(HtmlUtil.toAbsolute(origin, src));
            if(videoId != null) {

                final VideoReference videoReference = new VideoReference();
                videoReference.setVideoId(videoId);

                // origin
                try {
                    videoReference.setOriginUri(parser.parseUrl(origin).toUri());
                } catch(final URISyntaxException use) {
                    logger.warn("Invalid origin URI {}", origin);
                    return null;
                }

                // video
                final String videoUriStr = String.format(BASE_VIDEO_URL_PATTERN, (secure) ? "s" : "", videoId);
                final UrlInfo videoUriInfo = parser.parseUrl(videoUriStr);
                if(!videoUriParams.isEmpty()) {
                    final String query = parser.toQuery(videoUriParams);
                    videoUriInfo.setQuery(query);
                }
                try {
                     videoReference.setVideoUri(videoUriInfo.toUri());
                } catch(final URISyntaxException use) {
                    logger.warn("Failed creating video URI {} with configured query parameters due to {}", videoUriInfo, use);
                    continue;
                }

                final VideoMetadata videoMetadata = getVideoMetadata(videoId);
                if(videoMetadata == null) {
                    return null;
                }

                // merge metadata
                videoReference.setThumbnailUri(videoMetadata.getVideoReference().getThumbnailUri());
                videoMetadata.setVideoReference(videoReference);

                return videoMetadata;

            }
        }
        return null;
    }

    public void setSecure(final boolean secure) {
        this.secure = secure;
    }

    public boolean isSecure() {
        return secure;
    }

    public StringMultivaluedMap getVideoUriParams() {
        return videoUriParams;
    }

    public void setVideoUriParams(final StringMultivaluedMap videoUriParams) {
        this.videoUriParams = videoUriParams;
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

    public String getThumbnailFieldName() {
        return thumbnailFieldName;
    }

    public void setThumbnailFieldName(final String thumbnailFieldName) {
        this.thumbnailFieldName = thumbnailFieldName;
    }

    public String getTitleFieldName() {
        return titleFieldName;
    }

    public void setTitleFieldName(final String titleFieldName) {
        this.titleFieldName = titleFieldName;
    }

    public int getCacheSize() {
        return cacheSize;
    }

    public void setCacheSize(final int cacheSize) {
        this.cacheSize = cacheSize;
    }

    private HttpClient createHttpClient() {

        final HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, getConnectionTimeout());
        HttpConnectionParams.setSoTimeout(params, getReadTimeout());

        return new DefaultHttpClient(params);
    }

    private VideoMetadata getVideoMetadata(final String videoId) {

        final VideoMetadata cachedVideoMetadata = getFromCache(videoId);
        if(isFailure(cachedVideoMetadata)) {
            logger.info("'Failure' cache hit for {}", ((FailedVideoMetadata) cachedVideoMetadata).url);
            return null;
        } else if(cachedVideoMetadata != null) {
            logger.info("Cache hit for {}", cachedVideoMetadata.getVideoReference().getVideoUri());
            return cachedVideoMetadata;
        }

        final String url = String.format(VIDEO_METADATA_PATTERN, videoId);

        final HttpGet httpGet;
        try {
            httpGet = new HttpGet(url);
        } catch(final IllegalArgumentException iae) {
            logger.warn("Failed to get video metadata info for URL {} due to {}", url, iae);
            addToCache(videoId, new FailedVideoMetadata(url));
            return null;
        }


        final ResponseHandler<VideoMetadata> handler = new ResponseHandler<VideoMetadata>() {
            @Override
            public VideoMetadata handleResponse(final HttpResponse response) throws IOException {

                if(response.getStatusLine().getStatusCode() != 200) {
                    logger.warn("Failed to get video metadata for {}.  Status line: '{}'", url, response.getStatusLine());
                    return null;
                }

                final VideoMetadata videoMetadata = new VideoMetadata();
                videoMetadata.setVideoReference(new VideoReference());

                final HttpEntity entity = response.getEntity();

                final Map<String, Object> result;

                final ObjectMapper mapper = new ObjectMapper();
                result = mapper.readValue(entity.getContent(), new TypeReference<Map<String, Object>>() {});

                if(result.isEmpty()) {
                    return null;
                }

                // thumbnail
                final String thumbnailUrlString = (String) result.get(thumbnailFieldName);
                if(thumbnailUrlString == null) {
                    logger.warn("Video metadata did not contain thumbnail field name {}", thumbnailFieldName);
                    return null;
                }

                final UrlParser parser = new UrlParser();
                try {
                     final URI thumbnailUrl = parser.parseUrl(thumbnailUrlString).toUri();
                     videoMetadata.getVideoReference().setThumbnailUri(thumbnailUrl);
                } catch(final URISyntaxException use) {
                    logger.warn("Invalid thumbnail URI {} in video metadata for {} due to {}", new Object[] {thumbnailUrlString, url, use});
                    return null;
                }

                // title
                final String title = (String) result.get(titleFieldName);
                if(title == null) {
                    logger.warn("Video metadata did not contain title field name {}", new Object[] {titleFieldName});
                    return null;
                }
                videoMetadata.setTitle(title);

                return videoMetadata;
            }
        };

        final HttpClient client = createHttpClient();
        try {
            final VideoMetadata videoMetadata = client.execute(httpGet, handler);
            if(videoMetadata == null) {
                addToCache(videoId, new FailedVideoMetadata(url));
                return null;
            } else {
                addToCache(videoId, videoMetadata);
                return videoMetadata;
            }
        } catch(final IOException ioe) {
            logger.warn("Failed creating video metadata for DailyMotion URL {} due to {}", new Object[] {url, ioe, ioe});
            addToCache(videoId, new FailedVideoMetadata(url));
            return null;
        } finally {
            client.getConnectionManager().shutdown();
        }

    }

    private void addToCache(final String videoId, final VideoMetadata videoMetadata) {
        if(lruVideoMetadataCache != null) {
            lruVideoMetadataCache.put(videoId, videoMetadata);
        }
    }

    private VideoMetadata getFromCache(final String videoId) {
        if(lruVideoMetadataCache == null) {
            return null;
        } else {
            return lruVideoMetadataCache.get(videoId);
        }
    }

    private boolean isFailure(final VideoMetadata videoMetadata) {
        return (videoMetadata instanceof FailedVideoMetadata);
    }

    // sentinel value for denoting failed retrievals
    private static class FailedVideoMetadata extends VideoMetadata {

        private String url;

        public FailedVideoMetadata(final String url) {
            this.url = url;
        }

    }

}