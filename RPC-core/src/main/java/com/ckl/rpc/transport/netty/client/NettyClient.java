package com.ckl.rpc.transport.netty.client;

import com.ckl.rpc.transport.RpcClient;
import com.ckl.rpc.entity.RpcRequest;
import com.ckl.rpc.entity.RpcResponse;
import com.ckl.rpc.enumeration.RpcError;
import com.ckl.rpc.exception.RpcException;
import com.ckl.rpc.factory.SingletonFactory;
import com.ckl.rpc.loadbalancer.LoadBalancer;
import com.ckl.rpc.loadbalancer.RandomLoadBalancer;
import com.ckl.rpc.registry.NacosServiceDiscovery;
import com.ckl.rpc.registry.ServiceDiscovery;
import com.ckl.rpc.serializer.CommonSerializer;
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
    private final CommonSerializer serializer;
    //    未处理请求
    private final UnprocessedRequests unprocessedRequests;

    public NettyClient() {
        this(DEFAULT_SERIALIZER, new RandomLoadBalancer());
    }

    public NettyClient(LoadBalancer loadBalancer) {
        this(DEFAULT_SERIALIZER, loadBalancer);
    }

    public NettyClient(Integer serializer) {
        this(serializer, new RandomLoadBalancer());
    }

    public NettyClient(Integer serializer, LoadBalancer loadBalancer) {
        this.serviceDiscovery = new NacosServiceDiscovery(loadBalancer);
        this.serializer = CommonSerializer.getByCode(serializer);
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
            InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest.getInterfaceName());
//            获取channel
            Channel channel = ChannelProvider.get(inetSocketAddress, serializer);
            if (!channel.isActive()) {
                group.shutdownGracefully();
                return null;
            }
//            将请求放入未处理请求容器中
            unprocessedRequests.put(rpcRequest.getRequestId(), resultFuture);
//            使用future处理
            channel.writeAndFlush(rpcRequest).addListener((ChannelFutureListener) future1 -> {
                if (future1.isSuccess()) {
                    log.info(String.format("客户端发送消息: %s", rpcRequest.toString()));
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
