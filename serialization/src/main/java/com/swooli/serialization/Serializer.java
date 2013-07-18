package com.swooli.serialization;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * A serializer/deserializer for object to byte serialization.
 *
 * @author jmcarey
 */
public interface Serializer {

    /**
     * Serializes the given object to the given output stream.
     *
     * @param <T> the object type
     * @param obj the object to serialize
     * @param type the class type
     * @param os the output stream
     *
     * @throws SerializationException if serialization fails
     */
    public <T> void serialize(T obj, Class<T> type, OutputStream os) throws SerializationException;

    /**
     * Deserializes an object of the given type from the given input stream.
     *
     * @param <T> the object type
     * @param type the class type
     * @param is the input stream
     *
     * @return the object
     *
     * @throws SerializationException if serialization fails
     */
    public <T> T deserialize(Class<T> type, InputStream is) throws SerializationException;

}
