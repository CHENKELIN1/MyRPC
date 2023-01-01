package com.ckl.rpc.transport.netty.server;

import com.ckl.rpc.codec.NettyDecoder;
import com.ckl.rpc.codec.NettyEncoder;
import com.ckl.rpc.config.DefaultConfig;
import com.ckl.rpc.entity.ServerStatus;
import com.ckl.rpc.enumeration.LimiterType;
import com.ckl.rpc.extension.ExtensionFactory;
import com.ckl.rpc.extension.limit.Limiter;
import com.ckl.rpc.extension.serialize.Serializer;
import com.ckl.rpc.hook.ShutdownHook;
import com.ckl.rpc.provider.ServiceProviderImpl;
import com.ckl.rpc.registry.NacosServiceRegistry;
import com.ckl.rpc.transport.AbstractRpcServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * Netty服务端
 */
@Slf4j
public class NettyServer extends AbstractRpcServer implements DefaultConfig {
    //    序列化器
    private final Serializer serializer;

    public NettyServer(String host, int port) {
        this(host, port, DEFAULT_SERIALIZER.getCode(), DEFAULT_LIMITER);
    }

    public NettyServer(String host, int port, Integer serializer, LimiterType limiterType) {
        this.host = host;
        this.port = port;
        serviceRegistry = new NacosServiceRegistry();
        serviceProvider = new ServiceProviderImpl();
        this.serializer = ExtensionFactory.getExtension(Serializer.class, serializer);
        this.serverStatus = new ServerStatus();
        this.limiter = ExtensionFactory.getExtension(Limiter.class, limiterType);
        scanServices();
    }

    /**
     * 启动服务
     */
    @Override
    public void start() {
//        添加注销服务的钩子函数
        ShutdownHook.getShutdownHook().addClearAllHook();
//        TODO
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.SO_BACKLOG, 256)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new IdleStateHandler(NETTY_READER_IDLE_TIME, NETTY_WRITER_IDLE_TIME, NETTY_ALL_IDLE_TIME, NETTY_IDLE_TIME_UNIT))
//                                    添加编码器
                                    .addLast(new NettyEncoder(serializer))
//                                    添加解码器
                                    .addLast(new NettyDecoder())
//                                    添加Netty客户端处理器
                                    .addLast(new NettyServerHandler(serverStatus, limiter));
                        }
                    });
            ChannelFuture future = serverBootstrap.bind(host, port).sync();
            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            log.error("启动服务器时有错误发生: ", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
