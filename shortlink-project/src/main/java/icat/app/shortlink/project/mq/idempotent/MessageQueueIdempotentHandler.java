package icat.app.shortlink.project.mq.idempotent;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 消息队列幂等处理器
 */
@Component
@RequiredArgsConstructor
public class MessageQueueIdempotentHandler {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String IDEMPOTENT_PREFIX = "shortlink:idempotent_";

    /**
     * 判断当前消息是否消息过
     * @param messageId 消息唯一标识
     * @return 是否消费过
     */
    public boolean isMessageProcessed(String messageId) {
        String key = IDEMPOTENT_PREFIX + messageId;
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, "0", 10, TimeUnit.MINUTES));
    }

    public boolean isAccomplished(String messageId) {
        String key = IDEMPOTENT_PREFIX + messageId;
        return Objects.equals(redisTemplate.opsForValue().get(key), "1");
    }

    public void putAccomplishedFlag(String messageId) {
        String key = IDEMPOTENT_PREFIX + messageId;
        redisTemplate.opsForValue().set(key, "1", 10, TimeUnit.MINUTES);
    }

    /**
     * 删除消息消费标志
     * @param messageId 消息唯一标识
     */
    public void deleteMessageProcessedFlag(String messageId) {
        String key = IDEMPOTENT_PREFIX + messageId;
        redisTemplate.delete(key);
    }

}
