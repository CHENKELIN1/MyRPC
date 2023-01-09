package com.ckl.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 限制器类型
 */
@Getter
@AllArgsConstructor
public enum LimiterType {
    COUNTER(0),
    FUNNEL_RATE(1),
    TOKEN_BUCKET(2);
    private final int code;
}
