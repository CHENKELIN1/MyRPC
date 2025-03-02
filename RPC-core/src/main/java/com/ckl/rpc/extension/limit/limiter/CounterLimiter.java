package com.ckl.rpc.extension.limit.limiter;

import com.ckl.rpc.annotation.MyRpcExtension;
import com.ckl.rpc.config.DefaultConfig;
import com.ckl.rpc.enumeration.LimiterType;
import com.ckl.rpc.extension.limit.Limiter;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 计数法
 */
@Data
@Slf4j
@NoArgsConstructor
@MyRpcExtension(limitType = LimiterType.COUNTER)
public class CounterLimiter implements Limiter, DefaultConfig {
    private int count;

    {
        this.count = LIMIT_COUNTER_LIMITER_COUNT;
    }

    @Override
    public synchronized void preHandle() {
    }

    @Override
    public synchronized boolean limit() {
        count--;
        boolean res = count < 0;
        if (res) log.error("拦截请求：拦截器：Counter：count:{}", count);
        return res;
    }

    @Override
    public synchronized void afterHandle() {
        count++;
    }

}
