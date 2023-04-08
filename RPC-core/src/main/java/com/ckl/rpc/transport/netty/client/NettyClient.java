package com.ckl.rpc.transport.netty.client;

import com.ckl.rpc.entity.RpcRequest;
import com.ckl.rpc.entity.RpcResponse;
import com.ckl.rpc.enumeration.*;
import com.ckl.rpc.exception.RpcException;
import com.ckl.rpc.extension.compress.Compresser;
import com.ckl.rpc.extension.loadbalance.LoadBalancer;
import com.ckl.rpc.extension.registry.RegistryHandler;
import com.ckl.rpc.extension.serialize.Serializer;
import com.ckl.rpc.factory.ExtensionFactory;
import com.ckl.rpc.factory.SingletonFactory;
import com.ckl.rpc.registry.ServiceDiscovery;
import com.ckl.rpc.registry.ServiceDiscoveryImpl;
import com.ckl.rpc.status.StatusHandler;
import com.ckl.rpc.transport.common.client.RpcClient;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

/**
 * Netty客户端
 * TODO
 */
@Slf4j
public class NettyClient implements RpcClient {

    //    服务发现者
    private final ServiceDiscovery serviceDiscovery;
    //    序列化方式
    private final Serializer serializer;
    //    未处理请求
    private final UnprocessedRequests unprocessedRequests;
    private final Compresser compresser;


    public NettyClient(SerializerCode serializerCode, LoadBalanceType loadBalanceType, CompressType compressType, RegistryCode registryCode) {
        LoadBalancer loadBalancer = ExtensionFactory.getExtension(LoadBalancer.class, loadBalanceType.getCode());
        RegistryHandler registryHandler = ExtensionFactory.getExtension(RegistryHandler.class, registryCode.getCode());
        Serializer serializer = ExtensionFactory.getExtension(Serializer.class, serializerCode.getCode());
        Compresser compresser = ExtensionFactory.getExtension(Compresser.class, compressType.getCode());
        this.serviceDiscovery = new ServiceDiscoveryImpl(loadBalancer, registryHandler);
        this.serializer = serializer;
        this.compresser = compresser;
        this.unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
    }

    /**
     * 发送请求
     *
     * @param rpcRequest
     * @return
     */
    @Override
    public CompletableFuture<RpcResponse> sendRequest(RpcRequest rpcRequest) {
//        检查序列化器
        if (serializer == null) {
            log.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        CompletableFuture<RpcResponse> resultFuture = new CompletableFuture<>();
        try {
//            获取接口socket地址
            InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest);
//            获取channel
            Channel channel = ChannelProvider.get(inetSocketAddress, serializer, compresser);
            if (channel == null) {
                return null;
            }
//            服务器状态处理
            StatusHandler.ServerHandleSend(inetSocketAddress);
//            将请求放入未处理请求容器中
            unprocessedRequests.put(rpcRequest.getRequestId(), resultFuture);
//            使用future处理
            channel.writeAndFlush(rpcRequest).addListener((ChannelFutureListener) future1 -> {
                if (future1.isSuccess()) {
                    log.debug(String.format("客户端发送消息: %s", rpcRequest));
                } else {
                    future1.channel().close();
                    resultFuture.completeExceptionally(future1.cause());
                    log.error("发送消息时有错误发生: ", future1.cause());
                }
            });
        } catch (InterruptedException e) {
            unprocessedRequests.remove(rpcRequest.getRequestId());
            log.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
        return resultFuture;
    }

}
