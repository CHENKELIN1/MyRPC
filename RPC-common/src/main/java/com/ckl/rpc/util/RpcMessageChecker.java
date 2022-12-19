package com.ckl.rpc.util;

import com.ckl.rpc.entity.RpcRequest;
import com.ckl.rpc.entity.RpcResponse;
import com.ckl.rpc.enumeration.ResponseCode;
import com.ckl.rpc.enumeration.RpcError;
import com.ckl.rpc.exception.RpcException;
import lombok.extern.slf4j.Slf4j;

/**
 * Rpc消息检查
 */
@Slf4j
public class RpcMessageChecker {
    //    接口名称
    public static final String INTERFACE_NAME = "interfaceName";

    private RpcMessageChecker() {
    }

    /**
     * 检查RPC通信内容
     *
     * @param rpcRequest  Rpc请求体
     * @param rpcResponse Rpc响应体
     */
    public static void check(RpcRequest rpcRequest, RpcResponse rpcResponse) {
//        响应体为空
        if (rpcResponse == null) {
            log.error("调用服务失败,serviceName:{}", rpcRequest.getInterfaceName());
            throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }
//        请求号不对应
        if (!rpcRequest.getRequestId().equals(rpcResponse.getRequestId())) {
            throw new RpcException(RpcError.RESPONSE_NOT_MATCH, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }
//        服务调用失败
        if (rpcResponse.getCode() == null) {
            log.error("调用服务失败:获取不到响应号,serviceName:{},RpcResponse:{}", rpcRequest.getInterfaceName(), rpcResponse);
            throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }
        if (!rpcResponse.getCode().equals(ResponseCode.SUCCESS.getCode())) {
            log.error("服务调用失败:reason:{},serviceName:{},RpcResponse:{}", rpcResponse.getMsg(), rpcRequest.getInterfaceName(), rpcResponse);
            throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName() + rpcResponse.getMsg());
        }
    }
}
