package com.ckl.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransmissionType {
    SOCKET(0),
    NETTY(1);
    private final int code;
}
