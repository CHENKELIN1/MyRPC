package com.ckl.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 序列化方式
 */
@Getter
@AllArgsConstructor
public enum SerializerCode {
    KRYO(0),
    JSON(1),
    HESSIAN(2);
    private final int code;
}
