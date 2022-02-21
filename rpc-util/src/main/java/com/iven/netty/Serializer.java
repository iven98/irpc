package com.iven.netty;

import java.io.IOException;

public interface Serializer {
    byte[] serialize(Object object) throws IOException;

    <T> T deserialize(Class<T> clazz, byte[] bytes) throws IOException;
}
