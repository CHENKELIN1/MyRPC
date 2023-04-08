package com.ckl.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RegistryCode {
    NULL(-1),
    REDIS(0),
    NACOS(1);
    private int code;
}
