package com.swooli.serialization.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swooli.bo.video.collection.VideoCollectionPreview;
import com.swooli.serialization.SerializationException;
import com.swooli.serialization.Serializer;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A serializer/deserializer decorator around the Jackson library.  Objects are not compressed during
 * serialization by default.
 *
 * @author jmcarey
 */
public class JacksonSerializer implements Serializer {

    private static final Logger logger = LoggerFactory.getLogger(JacksonSerializer.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    private boolean compress = false;

    public JacksonSerializer() {
        mapper.addMixInAnnotations(VideoCollectionPreview.class, VideoCollectionPreviewMixIn.class);
        mapper.addMixInAnnotations(VideoCollectionPreview.class, VideoCollectionPreviewMixIn.class);
    }

    @Override
    public <T> T deserialize(final Class<T> type, final InputStream is) throws SerializationException {

        InputStream decoratedIs = null;
        try {

            if (compress) {
                decoratedIs = new GZIPInputStream(new BufferedInputStream(is));
            } else {
                decoratedIs = new BufferedInputStream(is);
            }

            return mapper.readValue(decoratedIs, type);

        } catch (final IOException ioe) {
            throw new SerializationException(ioe);
        } finally {
            if(decoratedIs != null) {
                try {
                    decoratedIs.close();
                } catch(final IOException ioe) {
                    logger.warn("Failed to close input stream", ioe);
                }
            }
        }
    }

    @Override
    public <T> void serialize(final T obj, final Class<T> type, final OutputStream os) throws SerializationException {

        OutputStream decoratedOs = null;
        try {

            if (compress) {
                decoratedOs = new GZIPOutputStream(new BufferedOutputStream(os));
            } else {
                decoratedOs = new BufferedOutputStream(os);
            }

            mapper.writeValue(decoratedOs, obj);

        } catch (final IOException ioe) {
            throw new SerializationException(ioe);
        } finally {
            try {
                if(decoratedOs != null) {
                    decoratedOs.close();
                }
            } catch (final IOException ioe) {
                logger.warn("Failed to close output stream", ioe);
            }
        }
    }

    public boolean isCompress() {
        return compress;
    }

    public void setCompress(final boolean compress) {
        this.compress = compress;
    }

}