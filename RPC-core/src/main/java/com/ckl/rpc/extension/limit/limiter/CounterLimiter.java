package com.ckl.rpc.extension.limit.limiter;

import com.ckl.rpc.config.DefaultConfig;
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
public class CounterLimiter implements Limiter, DefaultConfig {
    private int count;

    {
        this.count = LIMIT_COUNTER_LIMITER_COUNT;
    }

    @Override
    public synchronized void preHandle() {
        count--;
    }

    @Override
    public synchronized boolean limit() {
        boolean res = count < 0;
        if (res) log.error("拦截请求：拦截器：Counter：count:{}", count);
        return res;
    }

    @Override
    public synchronized void afterHandle() {
        count++;
    }

}
