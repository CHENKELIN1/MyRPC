package com.ckl.rpc.extension.limit.limiter;

import com.ckl.rpc.config.DefaultConfig;
import com.ckl.rpc.extension.limit.Limiter;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 计数法
 */
@Data
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
    public boolean limit() {
        return count < 0;
    }

    @Override
    public synchronized void afterHandle() {
        count++;
    }

}
