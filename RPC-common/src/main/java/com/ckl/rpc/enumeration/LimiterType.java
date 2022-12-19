package com.ckl.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 限制器类型
 */
@Getter
@AllArgsConstructor
public enum LimiterType {
    COUNTER("counter");
    private String type;
}
