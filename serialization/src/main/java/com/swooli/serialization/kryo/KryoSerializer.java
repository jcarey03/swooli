package com.swooli.serialization.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
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
 * A serializer/deserializer decorator around the Kryo library.  Objects are not compressed during
 * serialization by default.
 *
 * @author jmcarey
 */
public class KryoSerializer implements Serializer {

    private static final Logger logger = LoggerFactory.getLogger(KryoSerializer.class);

    private Kryo kryo;

    private boolean compress;

    public KryoSerializer() {
        kryo = new Kryo();
        kryo.setReferences(false);
        compress = false;
    }

    @Override
    public <T> T deserialize(Class<T> type, InputStream is) throws SerializationException {
        Input input = null;
        try {

            if (compress) {
                input = new Input(new GZIPInputStream(new BufferedInputStream(is)));
            } else {
                input = new Input(new BufferedInputStream(is));
            }

            return kryo.readObjectOrNull(input, type);

        } catch (final IOException ioe) {
            throw new SerializationException(ioe);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (final KryoException ke) {
                    logger.warn("Failed to close Kryo input", ke);
                }
            }
        }
    }

    @Override
    public <T> void serialize(T obj, Class<T> type, OutputStream os) throws SerializationException {
        Output output = null;
        try {

            if (compress) {
                output = new Output(new GZIPOutputStream(new BufferedOutputStream(os)));
            } else {
                output = new Output(new BufferedOutputStream(os));
            }

            kryo.writeObjectOrNull(output, obj, type);

        } catch (final IOException ioe) {
            throw new SerializationException(ioe);
        } finally {
            try {
                if(output != null) {
                    output.close();
                }
            } catch (final KryoException ke) {
                logger.warn("Failed to close Kryo output", ke);
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