package com.ckl.rpc.codec;

import com.ckl.rpc.entity.RpcRequest;
import com.ckl.rpc.entity.RpcResponse;
import com.ckl.rpc.enumeration.PackageType;
import com.ckl.rpc.enumeration.RpcError;
import com.ckl.rpc.exception.RpcException;
import com.ckl.rpc.serializer.CommonSerializer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProtocolHandler {
    public static Object handleIn(Protocol protocol){
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
        CommonSerializer serializer = CommonSerializer.getByCode(protocol.getSerializerCode());
        if (serializer == null) {
            log.error("不识别的反序列化器: {}", protocol.getSerializerCode());
            throw new RpcException(RpcError.UNKNOWN_SERIALIZER);
        }
        log.info(protocol.toString());
//        反序列化得到RpcRequest或RpcResponse对象
        return serializer.deserialize(protocol.getData(), packageClass);
    }
}
