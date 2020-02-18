package com.glimsil.poc.lettuce;

import com.glimsil.poc.lettuce.client.RedisClientProvider;

public class Main {
    public static void main(String[] args) {
        RedisClientProvider redisClientProvider = new RedisClientProvider();
        System.out.println("Inserting.");
        redisClientProvider.getConnection().async().set("hello", "world"); // async insertion
        System.out.println(redisClientProvider.getConnection().sync().get("hello")); // sync get
        redisClientProvider.close();
    }

}
