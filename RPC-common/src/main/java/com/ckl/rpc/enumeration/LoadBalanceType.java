package com.ckl.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 负载均衡器类型
 */
@Getter
@AllArgsConstructor
public enum LoadBalanceType {
    LOAD_BALANCE_RANDOM(0),
    LOAD_BALANCE_ROUND(1),
    LOAD_BALANCE_ADAPTIVE(2),
    LOAD_BALANCE_CONSISTENT_HASH(3);
    private final int code;
}
