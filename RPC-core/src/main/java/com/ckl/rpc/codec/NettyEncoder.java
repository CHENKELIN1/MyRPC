package com.ckl.rpc.codec;

import com.ckl.rpc.config.DefaultConfig;
import com.ckl.rpc.entity.RpcRequest;
import com.ckl.rpc.enumeration.PackageType;
import com.ckl.rpc.serializer.CommonSerializer;
import com.ckl.rpc.util.CommonUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.StandardCharsets;
import java.util.Date;

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
        byte[] data = serializer.serialize(msg);
//        写入数据长度
        out.writeInt(data.length);
//        写入扩展协议长度
        out.writeInt(DefaultConfig.EXPEND_LENGTH);
//        写入扩展协议内容
        byte[] expendData = ExpendProtocol.expendProtocolHandleWrite();
        out.writeBytes(expendData);
//        写入对象数据
        out.writeBytes(data);
    }
}
