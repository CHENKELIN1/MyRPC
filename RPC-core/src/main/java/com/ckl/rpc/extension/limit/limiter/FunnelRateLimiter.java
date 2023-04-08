package com.ckl.rpc.extension.limit.limiter;

import com.ckl.rpc.annotation.MyRpcExtension;
import com.ckl.rpc.config.DefaultConfig;
import com.ckl.rpc.enumeration.LimiterType;
import com.ckl.rpc.extension.limit.Limiter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@MyRpcExtension(limitType = LimiterType.FUNNEL_RATE)
public class FunnelRateLimiter implements DefaultConfig, Limiter {
    //    容量
    private final int capacity = LIMIT_FUNNEL_CAPACITY;
    //    漏水速度
    private final double leakingRate = LIMIT_FUNNEL_LEAKING_RATE;
    //    空余体积
    private int emptyCapacity;
    //    上次漏水时间
    private long lastLeakingTime = System.currentTimeMillis();

    public FunnelRateLimiter() {
        this.emptyCapacity = 0;
    }

    @Override
    public void preHandle() {

    }

    @Override
    public synchronized boolean limit() {
        boolean limited = isLimited();
        if (limited) log.warn("限流器生效：漏斗剩余空间：" + emptyCapacity);
        return limited;
    }

    @Override
    public void afterHandle() {

    }

    private void makeSpace() {
//        计算离上次漏斗时间
        long currentTimeMillis = System.currentTimeMillis();
        long gap = currentTimeMillis - lastLeakingTime;
//        计算上次离漏斗时间到现在漏的水
        double deltaQuota = (int) gap * leakingRate;
//        更新上次漏的水
        lastLeakingTime = currentTimeMillis;
//        间隔过长，整数数字过大溢出
        if (deltaQuota < 0) {
            emptyCapacity = capacity;
        }
//        更新腾出的空间
        emptyCapacity += deltaQuota;
        if (emptyCapacity > capacity) {
            emptyCapacity = capacity;
        }
    }

    private boolean isLimited() {
        makeSpace();
        if (emptyCapacity >= 1) {
            emptyCapacity -= 1;
            return false;
        }
        return true;
    }
}
