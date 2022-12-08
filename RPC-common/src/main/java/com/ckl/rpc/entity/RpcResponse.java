package com.ckl.rpc.entity;

import com.ckl.rpc.enumeration.ResponseCode;
import lombok.Data;

import java.io.Serializable;

@Data
public class RpcResponse<T> implements Serializable {
    private String requestId;
    private Integer code;
    private String msg;
    private T data;

    public RpcResponse(){}
    public static <T> RpcResponse<T> success(T data,String requestId) {
        RpcResponse<T> response = new RpcResponse<>();
        response.setCode(ResponseCode.SUCCESS.getCode());
        response.setData(data);
        response.setRequestId(requestId);
        return response;
    }

    public static <T> RpcResponse<T> fail(T data,String requestId){
        RpcResponse<T> response=new RpcResponse<>();
        response.setCode(ResponseCode.FAIL.getCode());
        response.setMsg(ResponseCode.FAIL.getMessage());
        response.setRequestId(requestId);
        return response;
    }
}
