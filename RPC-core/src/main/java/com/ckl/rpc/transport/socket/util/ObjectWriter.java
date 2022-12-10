package com.ckl.rpc.transport.socket.util;

import com.ckl.rpc.entity.RpcRequest;
import com.ckl.rpc.enumeration.PackageType;
import com.ckl.rpc.serializer.CommonSerializer;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Socket方式写入输出流
 */
public class ObjectWriter {
    //    自定义协议头
    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    //    将对象写入输出流
    public static void writeObject(OutputStream outputStream, Object object, CommonSerializer serializer) throws IOException {
//        写入自定义协议头
        outputStream.write(intToBytes(MAGIC_NUMBER));
//        写入包类型
        if (object instanceof RpcRequest) {
            outputStream.write(intToBytes(PackageType.REQUEST_PACK.getCode()));
        } else {
            outputStream.write(intToBytes(PackageType.RESPONSE_PACK.getCode()));
        }
//        写入序列化方式
        outputStream.write(intToBytes(serializer.getCode()));
//        写入序列化后的数据
        byte[] bytes = serializer.serialize(object);
        outputStream.write(intToBytes(bytes.length));
        outputStream.write(bytes);
        outputStream.flush();

    }

    private static byte[] intToBytes(int value) {
        byte[] src = new byte[4];
        src[0] = (byte) ((value >> 24) & 0xFF);
        src[1] = (byte) ((value >> 16) & 0xFF);
        src[2] = (byte) ((value >> 8) & 0xFF);
        src[3] = (byte) (value & 0xFF);
        return src;
    }
}
