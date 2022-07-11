package com.wayn.framework.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.commands.JedisCommands;

import java.util.Set;

@Component
public class RedisOpts {

    // 0 - never expire
    @Value("${spring.redis.expire}")
    private int expire = 0;
    // 数据库位置
    @Value("${spring.redis.databaseIndex}")
    private int databaseIndex = 0;

    public int getExpire() {
        return expire;
    }

    public RedisOpts setExpire(int expire) {
        this.expire = expire;
        return this;
    }

    @Autowired
    private JedisPool jedisPool;

    /**
     * get value from redis
     *
     * @param key
     * @return
     */
    public byte[] get(byte[] key) {
        byte[] value;
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.select(databaseIndex);
            value = jedis.get(key);
        }
        return value;
    }

    public String get(String key) {
        String value = null;
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.select(databaseIndex);
            value = jedis.get(key);
        }
        return value;
    }

    /**
     * set
     *
     * @param key
     * @param value
     * @return
     */
    public byte[] set(byte[] key, byte[] value) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.select(databaseIndex);
            jedis.set(key, value);
            if (this.expire != 0) {
                jedis.expire(key, this.expire);
            }
        }
        return value;
    }

    public String set(String key, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.select(databaseIndex);
            jedis.set(key, value);
            if (this.expire != 0) {
                jedis.expire(key, this.expire);
            }
        }
        return value;
    }

    /**
     * set
     *
     * @param key
     * @param value
     * @param expire
     * @return
     */
    public byte[] set(byte[] key, byte[] value, int expire) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.select(databaseIndex);
            jedis.set(key, value);
            if (expire != 0) {
                jedis.expire(key, expire);
            }
        }
        return value;
    }

    public String set(String key, String value, int expire) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.select(databaseIndex);
            jedis.set(key, value);
            if (expire != 0) {
                jedis.expire(key, (long) expire);
            }
        }
        return value;
    }

    /**
     * del
     *
     * @param key
     */
    public void del(byte[] key) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.select(databaseIndex);
            jedis.del(key);
        }
    }

    public void del(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.select(databaseIndex);
            jedis.del(key);
        }
    }

    /**
     * flush
     */
    public void flushDB() {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.select(databaseIndex);
            jedis.flushDB();
        }
    }

    /**
     * size
     */
    public Long dbSize() {
        Long dbSize;
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.select(databaseIndex);
            dbSize = jedis.dbSize();
        }
        return dbSize;
    }

    /**
     * keys
     *
     * @param pattern
     * @return
     */
    public Set<byte[]> keys(String pattern) {
        Set<byte[]> keys;
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.select(databaseIndex);
            keys = jedis.keys(pattern.getBytes());
        }
        return keys;
    }

    public Set<String> keysStr(String pattern) {
        Set<String> keys;
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.select(databaseIndex);
            keys = jedis.keys(pattern);
        }
        return keys;
    }
}

