package com.ckl.rpc.transport.netty.client;

import com.ckl.rpc.config.DefaultConfig;
import com.ckl.rpc.entity.RpcRequest;
import com.ckl.rpc.entity.RpcResponse;
import com.ckl.rpc.enumeration.CompressType;
import com.ckl.rpc.enumeration.LoadBalanceType;
import com.ckl.rpc.enumeration.RpcError;
import com.ckl.rpc.enumeration.SerializerCode;
import com.ckl.rpc.exception.RpcException;
import com.ckl.rpc.extension.ExtensionFactory;
import com.ckl.rpc.extension.compress.Compresser;
import com.ckl.rpc.extension.loadbalance.LoadBalancer;
import com.ckl.rpc.extension.serialize.Serializer;
import com.ckl.rpc.factory.SingletonFactory;
import com.ckl.rpc.registry.NacosServiceDiscovery;
import com.ckl.rpc.registry.ServiceDiscovery;
import com.ckl.rpc.status.ServerStatusHandler;
import com.ckl.rpc.transport.RpcClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

/**
 * Netty客户端
 * TODO
 */
@Slf4j
public class NettyClient implements RpcClient {
    private static final EventLoopGroup group;
    private static final Bootstrap bootstrap;

    static {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class);
    }

    //    服务发现者
    private final ServiceDiscovery serviceDiscovery;
    //    序列化方式
    private final Serializer serializer;
    //    未处理请求
    private final UnprocessedRequests unprocessedRequests;
    private final Compresser compresser;


    public NettyClient(SerializerCode serializerCode, LoadBalanceType loadBalanceType, CompressType compressType) {
        this.serviceDiscovery = new NacosServiceDiscovery(ExtensionFactory.getExtension(LoadBalancer.class, loadBalanceType));
        this.serializer = ExtensionFactory.getExtension(Serializer.class, serializerCode);
        this.compresser = ExtensionFactory.getExtension(Compresser.class, compressType);
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
            InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest.getInterfaceName(), rpcRequest.getGroup());
//            获取channel
            Channel channel = ChannelProvider.get(inetSocketAddress, serializer, compresser);
            if (!channel.isActive()) {
                group.shutdownGracefully();
                return null;
            }
//            服务器状态处理
            ServerStatusHandler.handleSend(inetSocketAddress);
//            将请求放入未处理请求容器中
            unprocessedRequests.put(rpcRequest.getRequestId(), resultFuture);
//            使用future处理
            channel.writeAndFlush(rpcRequest).addListener((ChannelFutureListener) future1 -> {
                if (future1.isSuccess()) {
                    if (DefaultConfig.CLIENT_SHOW_DETAIL_REQUEST_LOG)
                        log.info(String.format("客户端发送消息: %s", rpcRequest));
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
