package com.swooli.extraction.html;

import com.swooli.extraction.html.entity.EmbedEntity;
import com.swooli.extraction.html.entity.HtmlEntityHandler;
import com.swooli.extraction.html.entity.IFrameEntity;
import com.swooli.extraction.html.entity.ImageEntity;
import com.swooli.extraction.html.entity.MetaEntity;
import com.swooli.extraction.html.entity.TitleEntity;
import com.swooli.extraction.html.media.DailyMotionVideoMetadataFactory;
import com.swooli.extraction.html.media.ImageMetadata;
import com.swooli.extraction.html.media.ImageMetadataFactory;
import com.swooli.extraction.html.media.ImageMetadataFactoryImpl;
import com.swooli.extraction.html.media.VideoMetadata;
import com.swooli.extraction.html.media.VideoMetadataFactory;
import com.swooli.extraction.html.media.VimeoVideoMetadataFactory;
import com.swooli.extraction.html.media.YouTubeVideoMetadataFactory;
import com.swooli.extraction.html.util.HtmlUtil;
import com.swooli.extraction.html.util.UrlParser;
import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.ccil.cowan.tagsoup.Parser;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public class WebPageSummaryFactory {

    private Collection<VideoMetadataFactory<?>> videoMetadataFactories;

    private ImageMetadataFactory<?> imageMetadataFactory;

    private int connectionTimeout = 1500;
    
    private int readTimeout = 1500;
    
    public WebPageSummaryFactory() {

        videoMetadataFactories = new ArrayList<>();
        videoMetadataFactories.add(new YouTubeVideoMetadataFactory());
        videoMetadataFactories.add(new VimeoVideoMetadataFactory());
        videoMetadataFactories.add(new DailyMotionVideoMetadataFactory());

        imageMetadataFactory = new ImageMetadataFactoryImpl();
    }

    public WebPageSummary create(final String url) {

        BufferedInputStream bis = null;
        try {
            
            final URLConnection conn = new URL(url).openConnection();
            conn.setConnectTimeout(connectionTimeout);
            conn.setReadTimeout(readTimeout);
            
            bis = new BufferedInputStream(conn.getInputStream());
                        
            final XMLReader xr = new Parser();
            final HtmlEntityHandler handler = new HtmlEntityHandler(url);
            xr.setContentHandler(handler);
            xr.parse(new InputSource(bis));

            final WebPageSummary webPageSummary = new WebPageSummary();
            webPageSummary.setUrl(url);
            webPageSummary.setHost(new UrlParser().parseUrl(url).getHost());
            
            final Set<String> uniqueVideoUris = new HashSet<>();
            final Set<String> uniqueImageUris = new HashSet<>();

            /*
             * Add meta entries to unique URI sets.  Even though some meta entries are not URIs,
             * they can still be added to the sets.  They just won't match anything.
             */
            for(final MetaEntity entity : handler.getMetaEntities()) {
                final String absoluteUri = HtmlUtil.toAbsolute(entity.getOrigin(), entity.getContent());
                uniqueImageUris.add(absoluteUri);
                uniqueVideoUris.add(absoluteUri);
            }

            // add videos from meta section to result first because they are highest priority
            for(final VideoMetadataFactory<?> factory : videoMetadataFactories) {
                final List<? extends VideoMetadata> videoMetadataList = factory.create(handler.getMetaEntities());
                webPageSummary.getVideos().addAll(videoMetadataList);
            }

            // iframe videos are the latest trend, so they are second priority
            VideoMetadata videoMetadata;
            for(final IFrameEntity entity : handler.getIFrameEntities()) {
                final String absoluteUri = HtmlUtil.toAbsolute(entity.getOrigin(), entity.getSrc());
                if(uniqueVideoUris.contains(absoluteUri)) {
                    continue;
                } else {
                    uniqueVideoUris.add(absoluteUri);
                    for(final VideoMetadataFactory<?> factory : videoMetadataFactories) {
                        videoMetadata = factory.create(entity);
                        if(videoMetadata != null) {
                            webPageSummary.getVideos().add(videoMetadata);
                            break;  // no need to check other factories
                        }
                    }
                }
            }

            // object/embed are legacy, so add them last
            for(final EmbedEntity entity : handler.getEmbedEntities()) {
                final String absoluteUri = HtmlUtil.toAbsolute(entity.getOrigin(), entity.getSrc());
                if(uniqueVideoUris.contains(absoluteUri)) {
                    continue;
                } else {
                    uniqueVideoUris.add(absoluteUri);
                    for(final VideoMetadataFactory<?> factory : videoMetadataFactories) {
                        videoMetadata = factory.create(entity);
                        if(videoMetadata != null) {
                            webPageSummary.getVideos().add(videoMetadata);
                            break;  // no need to check other factories
                        }
                    }
                }
            }

            // add images from meta section to result first because they are highest priority
            final List<? extends ImageMetadata> imageMetadataList = imageMetadataFactory.create(handler.getMetaEntities());
            webPageSummary.getImages().addAll(imageMetadataList);

            // add images in body as second priority
            for(final ImageEntity entity : handler.getImageEntities()) {
                final String absoluteUri = HtmlUtil.toAbsolute(entity.getOrigin(), entity.getSrc());
                if(uniqueImageUris.contains(absoluteUri)) {
                    continue;
                } else {
                    uniqueImageUris.add(absoluteUri);
                    final ImageMetadata imageMetadata = imageMetadataFactory.create(entity);
                    if(imageMetadata != null) {
                        webPageSummary.getImages().add(imageMetadata);
                    }
                }
            }

            // try to set title, falling back to meta tags otherwise
            final TitleEntity titleEntity = handler.getTitleEntity();
            if(titleEntity != null) {
                webPageSummary.setTitle(titleEntity.getTitle());
            }

            // add title and description
            for(final MetaEntity entity : handler.getMetaEntities()) {
                final String propertyNoPrefix = HtmlUtil.stripOpenGraphNamespacePrefix(entity.getProperty());
                switch (propertyNoPrefix) {
                    case "title":
                        if(StringUtils.isBlank(webPageSummary.getTitle())) {
                            webPageSummary.setTitle(entity.getContent());
                            break;
                        }
                    case "description":
                        webPageSummary.setDescription(entity.getContent());
                        break;
                }
            }

            return webPageSummary;

        } catch(final Exception ex) {
            return null;
        } finally {
            IOUtils.closeQuietly(bis);
        }
    }

    public void setVideoMetadataFactories(final Collection<VideoMetadataFactory<?>> videoMetadataFactories) {
        this.videoMetadataFactories = videoMetadataFactories;
    }

    public void setImageMetadataFactory(final ImageMetadataFactory<?> imageMetadataFactory) {
        this.imageMetadataFactory = imageMetadataFactory;
    }

    public void setConnectionTimeout(final int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public void setReadTimeout(final int readTimeout) {
        this.readTimeout = readTimeout;
    }

}