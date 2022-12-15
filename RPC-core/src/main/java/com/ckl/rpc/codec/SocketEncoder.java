package com.ckl.rpc.codec;

import com.ckl.rpc.entity.RpcRequest;
import com.ckl.rpc.enumeration.PackageType;
import com.ckl.rpc.serializer.CommonSerializer;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Socket方式写入输出流
 */
public class SocketEncoder {

    //    将对象写入输出流
    public static void writeObject(OutputStream outputStream, Object object, CommonSerializer serializer) throws IOException {
//        写入自定义协议头
        outputStream.write(intToBytes(Protocol.MAGIC_NUMBER));
//        写入包类型
        if (object instanceof RpcRequest) {
            outputStream.write(intToBytes(PackageType.REQUEST_PACK.getCode()));
        } else {
            outputStream.write(intToBytes(PackageType.RESPONSE_PACK.getCode()));
        }
//        写入序列化方式
        outputStream.write(intToBytes(serializer.getCode()));
//        序列化数据
        byte[] data = serializer.serialize(object);
//        写入数据长度
        outputStream.write(intToBytes(data.length));
//        写入未定义协议
        byte[] undefine=new byte[Protocol.UNDEFINED_LENGTH];
        outputStream.write(undefine);
        outputStream.write(data);
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
