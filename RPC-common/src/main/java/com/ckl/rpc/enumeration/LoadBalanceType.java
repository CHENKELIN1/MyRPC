package com.ckl.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LoadBalanceType {
    LOAD_BALANCE_RANDOM(0),
    LOAD_BALANCE_ROUND(1);
    private final int code;
}
