package org.example.service.util;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class RedisUtils {

    private static final int DEFAULT_RETRY_COUNT = 1; // 默认重试次数
    private static final int DEFAULT_RETRY_DELAY_MS = 1; // 默认重试间隔（毫秒）

    private final RedisTemplate<String, String> redisTemplate;
    private final ConcurrentHashMap<String, String> localCache = new ConcurrentHashMap<>();

    public RedisUtils(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 封装 Redis get 操作，支持重试和本地备份
     * @param key Redis 键
     * @return 键对应的值
     */
    public String get(String key) {
        for (int i = 0; i < DEFAULT_RETRY_COUNT; i++) {
            try {
                String value = redisTemplate.opsForValue().get(key);
                if (value != null) {
                    return value;
                }
            } catch (Exception e) {
                if (i == DEFAULT_RETRY_COUNT - 1) {
                    // 最后一次重试失败，记录日志并从本地缓存获取
                    System.err.println("Failed to get value from Redis after " + DEFAULT_RETRY_COUNT + " retries. Falling back to local cache.");
                }
                try {
                    Thread.sleep(DEFAULT_RETRY_DELAY_MS);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        // 如果 Redis 获取失败，从本地缓存获取
        return localCache.get(key);
    }

    /**
     * 封装 Redis set 操作，支持重试和本地备份
     * @param key Redis 键
     * @param value Redis 值
     */
    public void set(String key, String value) {
        for (int i = 0; i < DEFAULT_RETRY_COUNT; i++) {
            try {
                redisTemplate.opsForValue().set(key, value);
                return;
            } catch (Exception e) {
                if (i == DEFAULT_RETRY_COUNT - 1) {
                    // 最后一次重试失败，记录日志并存储到本地缓存
                    System.err.println("Failed to set value in Redis after " + DEFAULT_RETRY_COUNT + " retries. Storing in local cache.");
                    localCache.put(key, value);
                }
                try {
                    Thread.sleep(DEFAULT_RETRY_DELAY_MS);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    /**
     * 封装 Redis delete 操作，支持重试
     * @param keys 要删除的键列表
     */
    public void delete(Iterable<String> keys) {
        for (int i = 0; i < DEFAULT_RETRY_COUNT; i++) {
            try {
                // 将 Iterable<String> 转换为 List<String>
                List<String> keyList = new ArrayList<>();
                keys.forEach(keyList::add);
                redisTemplate.delete(keyList); // 调用 delete 方法
                keys.forEach(localCache::remove);
                return;
            } catch (Exception e) {
                if (i == DEFAULT_RETRY_COUNT - 1) {
                    // 最后一次重试失败，记录日志
                    System.err.println("Failed to delete keys from Redis after " + DEFAULT_RETRY_COUNT + " retries.");
                    keys.forEach(localCache::remove);
                }
                try {
                    Thread.sleep(DEFAULT_RETRY_DELAY_MS);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    /**
     * 封装 Redis increment 操作，支持重试和本地备份
     * @param key Redis 键
     * @return 增加后的值
     */
    public Long increment(String key) {
        for (int i = 0; i < DEFAULT_RETRY_COUNT; i++) {
            try {
                Long value = redisTemplate.opsForValue().increment(key);
                return value;
            } catch (Exception e) {
                if (i == DEFAULT_RETRY_COUNT - 1) {
                    // 最后一次重试失败，记录日志并更新本地缓存
                    System.err.println("Failed to increment value in Redis after " + DEFAULT_RETRY_COUNT + " retries. Updating local cache.");
                    String currentValue = localCache.getOrDefault(key, "0");
                    localCache.put(key, String.valueOf(Long.parseLong(currentValue) + 1));
                }
                try {
                    Thread.sleep(DEFAULT_RETRY_DELAY_MS);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        // 如果 Redis 操作失败，直接返回本地缓存的值
        String currentValue = localCache.getOrDefault(key, "0");
        return Long.parseLong(currentValue);
    }
}
