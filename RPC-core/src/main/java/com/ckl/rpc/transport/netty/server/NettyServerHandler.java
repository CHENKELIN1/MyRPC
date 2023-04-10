package com.ckl.rpc.transport.netty.server;

import com.ckl.rpc.entity.RpcRequest;
import com.ckl.rpc.entity.RpcResponse;
import com.ckl.rpc.entity.Status;
import com.ckl.rpc.enumeration.ResponseCode;
import com.ckl.rpc.extension.limit.Limiter;
import com.ckl.rpc.factory.SingletonFactory;
import com.ckl.rpc.status.StatusHandler;
import com.ckl.rpc.transport.common.handler.RequestHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * Netty服务器处理器
 */
@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {
    //    请求处理器
    private final RequestHandler requestHandler;
    private final Status status;
    private final Limiter limiter;

    public NettyServerHandler(Status status, Limiter limiter) {
        this.requestHandler = SingletonFactory.getInstance(RequestHandler.class);
        this.status = status;
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
            RpcResponse<Object> response;
            status.addReceived();
//            获取心跳
            if (msg.getHeartBeat()) {
                log.info("接收到客户端: {} 的心跳包...",ctx.channel().remoteAddress());
                response = RpcResponse.heartBeat(StatusHandler.ServerUpdateStatus(status), msg.getRequestId());
            } else {
                limiter.preHandle();
                log.info("服务器接收到请求: {}", msg);
                if (limiter.limit()) {
                    response = RpcResponse.fail(ResponseCode.SERVER_BUSY, msg.getRequestId());
                } else {
//                  处理请求得到结果
                    response = requestHandler.handle(msg, status);
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
