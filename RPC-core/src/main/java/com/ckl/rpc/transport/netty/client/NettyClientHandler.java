package com.ckl.rpc.transport.netty.client;

import com.ckl.rpc.factory.ExtensionFactory;
import com.ckl.rpc.config.DefaultConfig;
import com.ckl.rpc.entity.RpcRequest;
import com.ckl.rpc.entity.RpcResponse;
import com.ckl.rpc.enumeration.ResponseCode;
import com.ckl.rpc.extension.compress.Compresser;
import com.ckl.rpc.extension.serialize.Serializer;
import com.ckl.rpc.factory.SingletonFactory;
import com.ckl.rpc.status.StatusHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * Netty客户端处理器
 * TODO
 */
@Slf4j
public class NettyClientHandler extends SimpleChannelInboundHandler<RpcResponse> implements DefaultConfig {
    //    未处理请求
    private final UnprocessedRequests unprocessedRequests;

    public NettyClientHandler() {
        this.unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
    }

    /**
     * 处理读入内容
     *
     * @param ctx the {@link ChannelHandlerContext} which this {@link SimpleChannelInboundHandler}
     *            belongs to
     * @param msg the message to handle
     * @throws Exception e
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) throws Exception {
        try {
//            服务器状态处理
            StatusHandler.ClientHandleReceived(msg, (InetSocketAddress) ctx.channel().remoteAddress());
//            心跳报文
            if (msg.getCode() == ResponseCode.HEART_BEAT.getCode()) {
                log.debug("收到服务器状态: " + msg.getStatus().toString());
            } else {
//                请求报文
                log.debug(String.format("客户端接收到消息: %s", msg));
                unprocessedRequests.complete(msg);
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("过程调用时有错误发生:");
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 状态监控操作：发送心跳报文
     *
     * @param ctx ctx
     * @param evt evt
     * @throws Exception e
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.WRITER_IDLE) {
                SocketAddress socketAddress = ctx.channel().remoteAddress();
                StatusHandler.ServerHandleSend((InetSocketAddress) socketAddress);
                log.info("发送心跳包 [{}]", socketAddress);
                Serializer serializer = ExtensionFactory.getExtension(Serializer.class, DEFAULT_SERIALIZER.getCode());
                Compresser compresser = ExtensionFactory.getExtension(Compresser.class, DEFAULT_SERIALIZER.getCode());
                Channel channel = ChannelProvider.get((InetSocketAddress) socketAddress, serializer, compresser);
                RpcRequest rpcRequest = new RpcRequest();
                rpcRequest.setHeartBeat(true);
                channel.writeAndFlush(rpcRequest).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
