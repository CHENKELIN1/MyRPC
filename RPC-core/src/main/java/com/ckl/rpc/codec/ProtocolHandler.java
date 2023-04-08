package com.ckl.rpc.codec;

import com.ckl.rpc.factory.ExtensionFactory;
import com.ckl.rpc.entity.RpcRequest;
import com.ckl.rpc.entity.RpcResponse;
import com.ckl.rpc.enumeration.PackageType;
import com.ckl.rpc.enumeration.RpcError;
import com.ckl.rpc.exception.RpcException;
import com.ckl.rpc.extension.compress.Compresser;
import com.ckl.rpc.extension.serialize.Serializer;
import lombok.extern.slf4j.Slf4j;

/**
 * 协议处理器
 */
@Slf4j
public class ProtocolHandler {
    /**
     * 协议处理方法
     *
     * @param protocol 协议内容
     * @return RpcRequest或RpcResponse对象
     */
    public static Object handleIn(Protocol protocol) {
//        不符合magicNumber
        if (protocol.getMagicNumber() != Protocol.MAGIC_NUMBER) {
            log.error("不识别的协议包: {}", protocol.getMagicNumber());
            throw new RpcException(RpcError.UNKNOWN_PROTOCOL);
        }
//        解析包类型
        Class<?> packageClass;
        if (protocol.getPackageCode() == PackageType.REQUEST_PACK.getCode()) {
            packageClass = RpcRequest.class;
        } else if (protocol.getPackageCode() == PackageType.RESPONSE_PACK.getCode()) {
            packageClass = RpcResponse.class;
        } else {
            log.error("不识别的数据包: {}", protocol.getPackageCode());
            throw new RpcException(RpcError.UNKNOWN_PACKAGE_TYPE);
        }
//        获取序列化方法
        Serializer serializer = ExtensionFactory.getExtension(Serializer.class, protocol.getSerializerCode());
        Compresser compresser = ExtensionFactory.getExtension(Compresser.class, protocol.getCompressCode());
        if (serializer == null) {
            log.error("不识别的反序列化器: {}", protocol.getSerializerCode());
            throw new RpcException(RpcError.UNKNOWN_SERIALIZER);
        }
        if (compresser == null) {
            log.error("不识别的反压缩器: {}", protocol.getSerializerCode());
            throw new RpcException(RpcError.UNKNOWN_COMPRESS);
        }
        byte[] data = compresser.decompress(protocol.getData());
        log.debug("解压缩前:{},解压缩后：{}", protocol.getData().length, data.length);
//        反序列化得到RpcRequest或RpcResponse对象
        return serializer.deserialize(data, packageClass);
    }
}
