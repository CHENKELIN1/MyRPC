package com.ckl.rpc.transport.netty.server;

import com.ckl.rpc.entity.RpcRequest;
import com.ckl.rpc.entity.RpcResponse;
import com.ckl.rpc.factory.SingletonFactory;
import com.ckl.rpc.factory.ThreadPoolFactory;
import com.ckl.rpc.handler.RequestHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;

@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {
    private static final String THREAD_NAME_PREFIX = "netty-server-handler";
    private final ExecutorService threadPool;
    private final RequestHandler requestHandler;

    public NettyServerHandler() {
        this.requestHandler = SingletonFactory.getInstance(RequestHandler.class);
        this.threadPool = ThreadPoolFactory.createDefaultThreadPool(THREAD_NAME_PREFIX);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        try {
            if (msg.getHeartBeat()) {
                log.info("接收到客户端心跳包...");
                return;
            }
            log.info("服务器接收到请求: {}", msg);
            Object result = requestHandler.handle(msg);
            if (ctx.channel().isActive() && ctx.channel().isWritable()) {
                ctx.writeAndFlush(RpcResponse.success(result, msg.getRequestId()));
            } else {
                log.error("通道不可写");
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("处理过程调用时有错误发生:");
        cause.printStackTrace();
        ctx.close();
    }

}
