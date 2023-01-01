package com.ckl.rpc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * RPC请求体定义
 */
@Data
@AllArgsConstructor
@Accessors(chain = true)
public class RpcRequest implements Serializable {
    //    请求id
    private String requestId;
    //    接口名称
    private String ServiceName;
    //    方法名称
    private String methodName;
    private String group;
    //    请求参数
    private Object[] parameters;
    //    请求参数类型
    private Class<?>[] paramTypes;
    //    心跳标志
    private Boolean heartBeat;

    public RpcRequest() {
    }
}
