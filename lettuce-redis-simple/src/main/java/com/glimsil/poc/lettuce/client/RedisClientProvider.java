package com.glimsil.poc.lettuce.client;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;

public class RedisClientProvider {
    private final RedisClient client;

    private StatefulRedisConnection<String, String> connection;

    public RedisClientProvider() {
        client = RedisClient.create("redis://localhost/0");
        connection = client.connect();
    }

    public StatefulRedisConnection<String, String> getConnection() {
        if(null == connection || !connection.isMulti()) {
            connection = client.connect();
        }
        return connection;
    }

    public void close() {
        connection.close();
    }

}
