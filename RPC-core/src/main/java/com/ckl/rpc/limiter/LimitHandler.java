package com.ckl.rpc.limiter;

public interface LimitHandler {
    void preHandle();
    boolean limit();
    void afterHandle();
}
