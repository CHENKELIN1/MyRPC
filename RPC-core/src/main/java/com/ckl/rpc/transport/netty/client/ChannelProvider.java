package com.ckl.rpc.transport.netty.client;

import com.ckl.rpc.codec.Netty.NettyDecoder;
import com.ckl.rpc.codec.Netty.NettyEncoder;
import com.ckl.rpc.config.DefaultConfig;
import com.ckl.rpc.extension.compress.Compresser;
import com.ckl.rpc.extension.serialize.Serializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

/**
 * TODO
 * 用于获取 Channel 对象
 */
@Slf4j
public class ChannelProvider implements DefaultConfig {
    private static final Bootstrap bootstrap;
    //    存储channel对象
    private static final Map<String, Channel> channels = new ConcurrentHashMap<>();

    static {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                //连接的超时时间，超过这个时间还是建立不上的话则代表连接失败
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                //是否开启 TCP 底层心跳机制
                .option(ChannelOption.SO_KEEPALIVE, true)
                //TCP默认开启了 Nagle 算法，该算法的作用是尽可能的发送大数据快，减少网络传输。TCP_NODELAY 参数的作用就是控制是否启用 Nagle 算法。
                .option(ChannelOption.TCP_NODELAY, true);
    }

    /**
     * 获取channel对象
     *
     * @param inetSocketAddress socket地址
     * @param serializer        序列化方式
     * @return channel
     * @throws InterruptedException exception
     */
    public static Channel get(InetSocketAddress inetSocketAddress, Serializer serializer, Compresser compresser) throws InterruptedException {
//        得到channel key
        String key = inetSocketAddress.toString() + serializer.getCode() + compresser.getCode();
        if (channels.containsKey(key)) {
            Channel channel = channels.get(key);
            if (channel.isActive()) {
//            若有channel并且活跃则返回
                return channel;
            } else {
//                若失效，则删除记录
                channels.remove(key);
            }
        }
//        创建bootstrap
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                /*自定义序列化编解码器*/
                // RpcResponse -> ByteBuf
                ch.pipeline()
                        .addLast(new IdleStateHandler(NETTY_READER_IDLE_TIME, NETTY_WRITER_IDLE_TIME, NETTY_ALL_IDLE_TIME, NETTY_IDLE_TIME_UNIT))
                        .addLast(new NettyEncoder(serializer, compresser))
                        .addLast(new NettyDecoder())
                        .addLast(new NettyClientHandler());
            }
        });
        Channel channel;
        try {
//            连接客户端
            channel = connect(inetSocketAddress);
        } catch (ExecutionException e) {
            log.error("连接客户端时有错误发生", e);
            return null;
        }
//        保存记录
        channels.put(key, channel);
//        返回连接
        return channel;
    }

    /**
     * 客户端连接
     *
     * @param inetSocketAddress socket地址
     * @return channel
     * @throws ExecutionException   e
     * @throws InterruptedException e
     */
    private static Channel connect(InetSocketAddress inetSocketAddress) throws ExecutionException, InterruptedException {
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        bootstrap.connect(inetSocketAddress).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                log.info("客户端连接成功!");
                completableFuture.complete(future.channel());
            } else {
                throw new IllegalStateException();
            }
        });
        return completableFuture.get();
    }
}
