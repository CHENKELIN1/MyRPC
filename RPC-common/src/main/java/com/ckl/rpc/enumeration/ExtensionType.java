package com.ckl.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExtensionType {
    NULL(-1),
    SERIALIZER(0),
    LOAD_BALANCE(1),
    LIMIT(2),
    COMPRESS(3);
    private int type;
}
