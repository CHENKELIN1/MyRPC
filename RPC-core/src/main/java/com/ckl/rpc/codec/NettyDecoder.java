package com.ckl.rpc.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 解码器
 */
@Slf4j
public class NettyDecoder extends ReplayingDecoder {
    /**
     * 对传输内容进行解码，得到RpcRequest或RpcResponse对象，由netty内部调用
     *
     * @param ctx ChannelHandlerContext
     * @param in  流缓存
     * @param out 解码结果存到out内
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
//        协议解析
        Protocol protocol=new Protocol(in);
//        写入处理后结果
        out.add(ProtocolHandler.handleIn(protocol));
    }
}
