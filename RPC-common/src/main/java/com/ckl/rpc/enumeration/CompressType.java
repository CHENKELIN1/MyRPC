package com.ckl.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CompressType {
    NULL(-1),
    GZIP(0);
    private int code;
}
