package com.ckl.rpc.codec.Netty;

import com.ckl.rpc.codec.ExpendProtocol;
import com.ckl.rpc.codec.Protocol;
import com.ckl.rpc.config.DefaultConfig;
import com.ckl.rpc.entity.RpcRequest;
import com.ckl.rpc.enumeration.PackageType;
import com.ckl.rpc.extension.compress.Compresser;
import com.ckl.rpc.extension.serialize.Serializer;
import com.ckl.rpc.util.CommonUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * 编码器
 */
@Slf4j
public class NettyEncoder extends MessageToByteEncoder {
    //    序列化方式
    private final Serializer serializer;
    private final Compresser compresser;

    public NettyEncoder(Serializer serializer, Compresser compresser) {
        this.serializer = serializer;
        this.compresser = compresser;
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
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) {
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
        out.writeInt(compresser.getCode());
//        将对象序列化
        byte[] data = serializer.serialize(msg);
        int length = data.length;
        data = compresser.compress(data);
//        写入数据长度
        out.writeInt(data.length);
        log.debug("压缩前：{},压缩后：{}", length, data.length);
//        写入扩展协议长度
        out.writeInt(DefaultConfig.EXPEND_LENGTH);
//        写入扩展协议内容
        ExpendProtocol expendProtocol = new ExpendProtocol()
                .setTime(CommonUtil.formatDate(new Date()));
//                .setCompressType(compresser.getCode());
        byte[] expendData = ExpendProtocol.expendProtocolHandleWrite(expendProtocol);
        out.writeBytes(expendData);
//        写入对象数据
        out.writeBytes(data);
    }
}
