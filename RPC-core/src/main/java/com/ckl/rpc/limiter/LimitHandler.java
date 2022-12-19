package com.ckl.rpc.limiter;

/**
 * 限制器接口
 */
public interface LimitHandler {
    void preHandle();

    boolean limit();

    void afterHandle();
}
