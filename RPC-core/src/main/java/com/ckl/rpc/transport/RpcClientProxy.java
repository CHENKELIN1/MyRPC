package com.ckl.rpc.transport;

import com.ckl.rpc.entity.RpcRequest;
import com.ckl.rpc.entity.RpcResponse;
import com.ckl.rpc.transport.netty.client.NettyClient;
import com.ckl.rpc.transport.socket.client.SocketClient;
import com.ckl.rpc.util.RpcMessageChecker;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Rpc客户端代理
 */
@Slf4j
public class RpcClientProxy {
    //    Rpc客户端
    private final RpcClient client;

    public RpcClientProxy(RpcClient client) {
        this.client = client;
    }

    /**
     * 获取代理
     *
     * @param clazz
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz, String group) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, new RpcClientHandler(group));
    }

    /**
     * 客户端处理器
     */
    private class RpcClientHandler implements InvocationHandler {
        //        业务组
        private final String group;

        public RpcClientHandler(String group) {
            this.group = group;
        }

        public Object invoke(Object proxy, Method method, Object[] args) {
            log.info("调用方法: {}#{}", method.getDeclaringClass().getName(), method.getName());
//          创建RpcRequest
            RpcRequest rpcRequest = new RpcRequest()
                    .setRequestId(UUID.randomUUID().toString())
                    .setInterfaceName(method.getDeclaringClass().getName())
                    .setMethodName(method.getName())
                    .setGroup(this.group)
                    .setParameters(args)
                    .setParamTypes(method.getParameterTypes())
                    .setHeartBeat(false);
//          初始化RpcResponse
            RpcResponse rpcResponse = null;
//          Netty客户端处理方式
            if (client instanceof NettyClient) {
                try {
//                发送请求
                    CompletableFuture<RpcResponse> completableFuture = (CompletableFuture<RpcResponse>) client.sendRequest(rpcRequest);
//                获取返回结果
                    rpcResponse = completableFuture.get();
                } catch (Exception e) {
                    log.error("方法调用请求发送失败", e);
                    return null;
                }
            }
//          Socket客户端处理方式
            if (client instanceof SocketClient) {
//            发送请求并获取响应结果
                rpcResponse = (RpcResponse) client.sendRequest(rpcRequest);
            }
//          检查请求体与响应体
            RpcMessageChecker.check(rpcRequest, rpcResponse);
//          返回响应数据
            return rpcResponse.getData();
        }
    }
}
