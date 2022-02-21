package com.iven.util;

import redis.clients.jedis.Jedis;

public class RedisUtil {
    private static Jedis jedis = new Jedis("127.0.0.1", 6379);

    public static void record2Cache(String key, String value) {
        jedis.set(key, value);
    }

    public static String getObject(String key) {
        return jedis.get(key);
    }
}
