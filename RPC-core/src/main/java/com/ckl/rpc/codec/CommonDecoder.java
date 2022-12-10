package com.ckl.rpc.codec;

import com.ckl.rpc.entity.RpcRequest;
import com.ckl.rpc.entity.RpcResponse;
import com.ckl.rpc.enumeration.PackageType;
import com.ckl.rpc.enumeration.RpcError;
import com.ckl.rpc.exception.RpcException;
import com.ckl.rpc.serializer.CommonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 解码器
 */
@Slf4j
public class CommonDecoder extends ReplayingDecoder {
    //    自定义协议标识头
    private static final int MAGIC_NUMBER = 0xCAFEBABE;

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
//        读取一个int4个字节，为magicNumber标识自定义协议
        int magic = in.readInt();
//        不符合magicNumber
        if (magic != MAGIC_NUMBER) {
            log.error("不识别的协议包: {}", magic);
            throw new RpcException(RpcError.UNKNOWN_PROTOCOL);
        }
//        读取一个int4字节，为packageType标识包类型
        int packageCode = in.readInt();
//        解析包类型
        Class<?> packageClass;
        if (packageCode == PackageType.REQUEST_PACK.getCode()) {
            packageClass = RpcRequest.class;
        } else if (packageCode == PackageType.RESPONSE_PACK.getCode()) {
            packageClass = RpcResponse.class;
        } else {
            log.error("不识别的数据包: {}", packageCode);
            throw new RpcException(RpcError.UNKNOWN_PACKAGE_TYPE);
        }
//        读取一个int4字节，为序列化方式标识
        int serializerCode = in.readInt();
//        获取序列化方法
        CommonSerializer serializer = CommonSerializer.getByCode(serializerCode);
        if (serializer == null) {
            log.error("不识别的反序列化器: {}", serializerCode);
            throw new RpcException(RpcError.UNKNOWN_SERIALIZER);
        }
//        读取一个int4字节，为数据字节长度
        int length = in.readInt();
//        根据数据长度，读取数据
        byte[] bytes = new byte[length];
        in.readBytes(bytes);
//        反序列化得到RpcRequest或RpcResponse对象
        Object obj = serializer.deserialize(bytes, packageClass);
        out.add(obj);
    }
}
