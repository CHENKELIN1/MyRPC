package com.ckl.rpc.extension.limit;

/**
 * 限制器接口
 */
public interface Limiter {
    void preHandle();

    boolean limit();

    void afterHandle();
}
