package com.ckl.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CompressType {
    GZIP(0);
    private int code;
}
