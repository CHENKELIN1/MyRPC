package com.ckl.rpc.entity;

import com.ckl.rpc.enumeration.ResponseCode;
import lombok.Data;

import java.io.Serializable;

/**
 * RPC响应体定义
 */
@Data
public class RpcResponse<T> implements Serializable {
    //    请求id
    private String requestId;
    //    响应码
    private Integer code;
    //    响应消息
    private String msg;
    //    响应返回数据
    private T data;
    //    服务器状态
    private Status status;

    public RpcResponse() {
    }

    /**
     * @param data      返回的数据
     * @param requestId 请求id
     * @return RpcResponse Rpc响应体
     */
    public static <T> RpcResponse<T> success(T data, String requestId, Status status) {
        RpcResponse<T> response = new RpcResponse<>();
        response.setCode(ResponseCode.SUCCESS.getCode());
        response.setData(data);
        response.setRequestId(requestId);
        response.setStatus(status);
        return response;
    }

    /**
     * @param code      错误响应码
     * @param requestId 请求id
     * @return RpcResponse Rpc响应体
     */
    public static <T> RpcResponse<T> fail(ResponseCode code, String requestId) {
        RpcResponse<T> response = new RpcResponse<>();
        response.setCode(code.getCode());
        response.setMsg(code.getMessage());
        response.setRequestId(requestId);
        return response;
    }

    /**
     * 心跳响应
     *
     * @param status    服务器状态
     * @param requestId 请求ID
     * @return RpcResponse
     */
    public static <T> RpcResponse<T> heartBeat(Status status, String requestId) {
        RpcResponse<T> response = new RpcResponse<>();
        response.setCode(ResponseCode.HEART_BEAT.getCode());
        response.setStatus(status);
        response.setRequestId(requestId);
        return response;
    }
}
