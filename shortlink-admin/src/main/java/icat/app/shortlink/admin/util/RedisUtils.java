package icat.app.shortlink.admin.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public final class RedisUtils {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 向Redis内添加新的键值对
     * @param key 储存的键
     * @param value 储存的值
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 向Redis内put哈希对
     * @param key 储存的键
     * @param hashKey 哈希键
     * @param value 储存的值
     */
    public void setHash(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * 向Redis内添加新的键值对，有效期为N秒
     * @param key 储存的键
     * @param value 储存的值
     * @param seconds 有效期
     */
    public void set(String key, Object value, long seconds) {
        redisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
    }

    /**
     * 向Redis内put新的哈希键值对，有效期为N秒
     * @param key 储存的键
     * @param hashKey 哈希键
     * @param value 储存的值
     * @param seconds 有效期
     */
    public void setHash(String key, String hashKey, Object value, long seconds) {
        redisTemplate.opsForHash().put(key, hashKey, value);
        redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
    }

    /**
     * 查询Redis中是否存在指定键
     * @param key 键
     * @return Redis中是否存在该键
     */
    public Boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 从Redis中或取指定键对应的值
     * @param key 键
     * @return 键对应的值
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 从Redis中获取指定类型的值
     * @param key 键
     * @param clazz 值的类型
     * @return 指定类型的值
     * @param <T> 值的类型
     */
    public <T> T get(String key, Class<T> clazz) {
        return clazz.cast(redisTemplate.opsForValue().get(key));
    }

    /**
     * 从Redis中或取指定哈希键对应的值
     * @param key 键
     * @param hashKey 哈希键
     * @return 键对应的值
     */
    public Object getHash(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * 从Redis中获取对应哈希键指定类型的值
     * @param key 键
     * @param hashKey 哈希键
     * @param clazz 值的类型
     * @return 指定类型的值
     * @param <T> 值的类型
     */
    public <T> T getHash(String key,String hashKey, Class<T> clazz) {
        return clazz.cast(redisTemplate.opsForHash().get(key, hashKey));
    }

    /**
     * 从Redis中删除对应的键值对
     * @param key 键
     */
    public void remove(String key) {
        redisTemplate.delete(key);
    }

}
