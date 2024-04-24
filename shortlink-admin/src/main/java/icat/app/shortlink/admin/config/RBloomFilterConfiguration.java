package icat.app.shortlink.admin.config;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 布隆过滤器配置类
 */
@Configuration
public class RBloomFilterConfiguration {

    /**
     * 初始化用于处理用户名检测的布隆过滤器
     */
    @Bean
    public RBloomFilter<String> userRegisterCachePenetrationBloomFilter(RedissonClient redissonClient) {
        RBloomFilter<String> registerCachePenetrationBloomFilter = redissonClient.getBloomFilter("filter");
        registerCachePenetrationBloomFilter.tryInit(100000000L, 0.001);
        return registerCachePenetrationBloomFilter;
    }

}
