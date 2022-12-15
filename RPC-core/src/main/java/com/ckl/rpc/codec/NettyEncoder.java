package com.ckl.rpc.codec;

import com.ckl.rpc.entity.RpcRequest;
import com.ckl.rpc.enumeration.PackageType;
import com.ckl.rpc.serializer.CommonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 编码器
 */
public class NettyEncoder extends MessageToByteEncoder {
    //    序列化方式
    private final CommonSerializer serializer;

    public NettyEncoder(CommonSerializer serializer) {
        this.serializer = serializer;
    }

    /**
     * 对数据进行编码，由netty内部调用
     *
     * @param ctx ChannelHandlerContext
     * @param msg 编码数据
     * @param out 输出流缓存
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
//        写入int4字节自定义协议标识头
        out.writeInt(Protocol.MAGIC_NUMBER);
//        根据数据类型写入int4字节的包类型
        if (msg instanceof RpcRequest) {
            out.writeInt(PackageType.REQUEST_PACK.getCode());
        } else {
            out.writeInt(PackageType.RESPONSE_PACK.getCode());
        }
//        写入int4字节序列化方式
        out.writeInt(serializer.getCode());
//        将对象序列化
        byte[] bytes = serializer.serialize(msg);
//        写入int4字节对象长度
        out.writeInt(bytes.length);
//        写入为定义协议
        byte[] undefine=new byte[Protocol.UNDEFINED_LENGTH];
        out.writeBytes(undefine);
//        写入对象数据
        out.writeBytes(bytes);
    }
}
