package icat.app.shortlink.project.config;

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
     * 初始化用于检测短链接Uri的布隆过滤器
     */
    @Bean
    public RBloomFilter<String> useShortUriCreateCachePenetrationBloomFilter(RedissonClient redissonClient) {
        RBloomFilter<String> registerCachePenetrationBloomFilter = redissonClient.getBloomFilter("short-uri-filter");
        registerCachePenetrationBloomFilter.tryInit(100000000L, 0.001);
        return registerCachePenetrationBloomFilter;
    }

}
