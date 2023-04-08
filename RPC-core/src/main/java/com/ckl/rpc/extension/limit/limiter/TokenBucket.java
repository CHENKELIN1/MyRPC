package com.ckl.rpc.extension.limit.limiter;

import com.ckl.rpc.annotation.MyRpcExtension;
import com.ckl.rpc.config.DefaultConfig;
import com.ckl.rpc.enumeration.LimiterType;
import com.ckl.rpc.extension.limit.Limiter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@MyRpcExtension(limitType = LimiterType.TOKEN_BUCKET)
public class TokenBucket implements DefaultConfig, Limiter {
    private final int capacity = LIMIT_TOKEN_BUCKET_CAPACITY;
    private final int rate = LIMIT_TOKEN_BUCKET_RATE;
    private long lastTime = System.currentTimeMillis();
    private int tokens;

    public TokenBucket() {
        this.tokens = capacity;
    }

    @Override
    public void preHandle() {

    }

    @Override
    public synchronized boolean limit() {
        boolean limited = isLimited();
        if (limited) log.warn("限流器：令牌桶：令牌数量：{}", tokens);
        return limited;
    }

    @Override
    public void afterHandle() {

    }

    public synchronized boolean isLimited() {
        long now = System.currentTimeMillis();
        long gap = now - lastTime;
        int add = (int) (gap * rate);
        lastTime = now;
        tokens = Math.min(capacity, tokens + add);
        if (tokens <= 0) {
            return true;
        } else {
            tokens--;
            return false;
        }
    }
}
