package com.ckl.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 服务器健康状态
 */
@Getter
@AllArgsConstructor
public enum ServerHealth {
    NOT_CONNECT("notConnect"),
    HEALTH("health"),
    SUB_HEALTH("subHealth"),
    DEATH("death");
    private String type;
}
