package com.ckl.rpc.transport.common.handler;

import com.ckl.rpc.entity.RpcRequest;
import com.ckl.rpc.entity.RpcResponse;
import com.ckl.rpc.entity.Status;
import com.ckl.rpc.enumeration.ResponseCode;
import com.ckl.rpc.provider.ServiceProvider;
import com.ckl.rpc.provider.ServiceProviderImpl;
import com.ckl.rpc.status.StatusHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 请求处理器
 */
@Slf4j
public class RequestHandler {
    //    服务提供者
    private static final ServiceProvider serviceProvider;

    static {
        serviceProvider = new ServiceProviderImpl();
    }

    /**
     * 处理RpcRequest
     *
     * @param rpcRequest 请求体
     * @return 请求结果
     */
    public RpcResponse<Object> handle(RpcRequest rpcRequest, Status status) {
//        根据请求获取服务对象
        Object service = serviceProvider.getServiceProvider(rpcRequest.getServiceName(), rpcRequest.getGroup());
//        调用目标方法
        return invokeTargetMethod(rpcRequest, service, status);
    }

    /**
     * 调用目标方法
     *
     * @param rpcRequest 请求体
     * @param service    服务对象
     * @return 请求结果
     */
    private RpcResponse<Object> invokeTargetMethod(RpcRequest rpcRequest, Object service, Status status) {
//        处理结果
        Object result;
        try {
//            获取方法
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
//            调用方法
            result = method.invoke(service, rpcRequest.getParameters());
            log.info("服务:{} 成功调用方法:{}", rpcRequest.getServiceName(), rpcRequest.getMethodName());
        } catch (NoSuchMethodException e) {
            return RpcResponse.fail(ResponseCode.NOT_FOUND_METHOD, rpcRequest.getRequestId());
        } catch (IllegalAccessException e) {
            return RpcResponse.fail(ResponseCode.ILLEGAL_ACCESS_EXCEPTION, rpcRequest.getRequestId());
        } catch (InvocationTargetException e) {
            return RpcResponse.fail(ResponseCode.INVOCATION_TARGET_EXCEPTION, rpcRequest.getRequestId());
        }
        return RpcResponse.success(result, rpcRequest.getRequestId(), StatusHandler.ServerUpdateStatus(status));
    }
}
