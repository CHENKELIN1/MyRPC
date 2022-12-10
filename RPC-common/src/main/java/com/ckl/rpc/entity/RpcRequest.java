package com.ckl.rpc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * RPC请求体定义
 */
@Data
@AllArgsConstructor
public class RpcRequest implements Serializable {
    //    请求id
    private String requestId;
    //    接口名称
    private String interfaceName;
    //    方法名称
    private String methodName;
    //    请求参数
    private Object[] parameters;
    //    请求参数类型
    private Class<?>[] paramTypes;
    //    心跳标志
    private Boolean heartBeat;

    public RpcRequest() {
    }
}
