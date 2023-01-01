package com.ckl.rpc.transport.netty.server;

import com.ckl.rpc.config.DefaultConfig;
import com.ckl.rpc.entity.RpcRequest;
import com.ckl.rpc.entity.RpcResponse;
import com.ckl.rpc.entity.ServerStatus;
import com.ckl.rpc.enumeration.ResponseCode;
import com.ckl.rpc.extension.limit.Limiter;
import com.ckl.rpc.factory.SingletonFactory;
import com.ckl.rpc.factory.ThreadPoolFactory;
import com.ckl.rpc.status.ServerStatusHandler;
import com.ckl.rpc.transport.RequestHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;

/**
 * Netty服务器处理器
 */
@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {
    //    线程名前缀
    private static final String THREAD_NAME_PREFIX = "netty-server-handler";
    //    线程池
    private final ExecutorService threadPool;
    //    请求处理器
    private final RequestHandler requestHandler;
    private final ServerStatus serverStatus;
    private final Limiter limiter;

    public NettyServerHandler(ServerStatus serverStatus, Limiter limiter) {
        this.requestHandler = SingletonFactory.getInstance(RequestHandler.class);
        this.threadPool = ThreadPoolFactory.createDefaultThreadPool(THREAD_NAME_PREFIX);
        this.serverStatus = serverStatus;
        this.limiter = limiter;
    }

    /**
     * Netty内部调用
     * TODO
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) {
        try {
            RpcResponse response;
            serverStatus.addReceived();
//            获取心跳
            if (msg.getHeartBeat()) {
                if (DefaultConfig.SERVER_SHOW_HEART_BEAT_LOG) log.info("接收到客户端心跳包...");
                response = RpcResponse.heartBeat(ServerStatusHandler.updateStatus(serverStatus), msg.getRequestId());
            } else {
                limiter.preHandle();
                if (DefaultConfig.SERVER_SHOW_DETAIL_REQUEST_LOG) log.info("服务器接收到请求: {}", msg);
                if (limiter.limit()) {
                    response = RpcResponse.fail(ResponseCode.SERVER_BUSY, msg.getRequestId());
                } else {
//                  处理请求得到结果
                    Object result = requestHandler.handle(msg);
                    if (result instanceof RpcResponse) {
                        response = (RpcResponse) result;
                        response.setStatus(ServerStatusHandler.updateStatus(serverStatus));
                    } else {
                        response = RpcResponse.success(result, msg.getRequestId(), ServerStatusHandler.updateStatus(serverStatus));
                    }
                }
                limiter.afterHandle();
            }
//          若通道可写
            if (ctx.channel().isActive() && ctx.channel().isWritable()) {
//                写入响应数据
                ctx.writeAndFlush(response);
            } else {
                log.error("通道不可写");
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    /**
     * 异常处理方式
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("处理过程调用时有错误发生:");
        cause.printStackTrace();
        ctx.close();
    }

}
