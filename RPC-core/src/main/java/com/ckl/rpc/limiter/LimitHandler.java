package com.ckl.rpc.limiter;

import com.ckl.rpc.enumeration.LimiterType;
import com.ckl.rpc.factory.SingletonFactory;

public interface LimitHandler {
    void preHandle();
    boolean limit();
    void afterHandle();
    static Limiter getInstance(LimiterType type){
        Limiter limiter = SingletonFactory.getInstance(Limiter.class);
        switch (type){
            default: limiter.setLimitHandler(new CounterLimitHandler());
        }
        return limiter;
    }
}
