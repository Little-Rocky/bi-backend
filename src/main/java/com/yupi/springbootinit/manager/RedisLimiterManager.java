package com.yupi.springbootinit.manager;

import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.config.RedisConfig;
import com.yupi.springbootinit.exception.BusinessException;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 专门提供给RedisLimiter 限流基础服务的（提供通用的能力）
 * @author Rocky
 */
@Service
public class RedisLimiterManager {

    @Resource
    private RedissonClient redissonClient;

    /**
     * 限流操作
     * @param key 区分不同的限流器，比如不同的用户id应该分开统计
     */
    public void doRateLimit(String key) {
        // 创建限流器
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);
        // OVERALL无论多少服务器都是一起统计，每秒请求2次
        rateLimiter.trySetRate(RateType.OVERALL, 2, 1, RateIntervalUnit.SECONDS);
        // 当每一个操作进来请求一个令牌
        boolean canOp = rateLimiter.tryAcquire(1);
        if (!canOp) {
            throw new BusinessException(ErrorCode.TOO_MANY_REQUEST);
        }
    }


}
