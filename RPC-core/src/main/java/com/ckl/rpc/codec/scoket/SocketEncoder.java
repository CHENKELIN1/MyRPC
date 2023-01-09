package com.ckl.rpc.codec.scoket;

import com.ckl.rpc.codec.DecodeUtil;
import com.ckl.rpc.codec.ExpendProtocol;
import com.ckl.rpc.codec.Protocol;
import com.ckl.rpc.config.DefaultConfig;
import com.ckl.rpc.entity.RpcRequest;
import com.ckl.rpc.enumeration.PackageType;
import com.ckl.rpc.extension.compress.Compresser;
import com.ckl.rpc.extension.serialize.Serializer;
import com.ckl.rpc.util.CommonUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

/**
 * Socket方式写入输出流
 */
public class SocketEncoder {

    //    将对象写入输出流
    public static void writeObject(OutputStream outputStream, Object object, Serializer serializer, Compresser compresser) throws IOException {
//        写入自定义协议头
        outputStream.write(DecodeUtil.intToBytes(Protocol.MAGIC_NUMBER));
//        写入包类型
        if (object instanceof RpcRequest) {
            outputStream.write(DecodeUtil.intToBytes(PackageType.REQUEST_PACK.getCode()));
        } else {
            outputStream.write(DecodeUtil.intToBytes(PackageType.RESPONSE_PACK.getCode()));
        }
//        写入序列化方式
        outputStream.write(DecodeUtil.intToBytes(serializer.getCode()));
        outputStream.write(DecodeUtil.intToBytes(compresser.getCode()));
//        序列化数据
        byte[] data = serializer.serialize(object);
//         数据压缩
        data = compresser.compress(data);
//        写入数据长度
        outputStream.write(DecodeUtil.intToBytes(data.length));
//        写入扩展协议长度
        outputStream.write(DecodeUtil.intToBytes(DefaultConfig.EXPEND_LENGTH));
        ExpendProtocol expendProtocol = new ExpendProtocol()
                .setTime(CommonUtil.formatDate(new Date()));
//                .setCompressType(compresser.getCode());
        byte[] expendData = ExpendProtocol.expendProtocolHandleWrite(expendProtocol);
        outputStream.write(expendData);
        outputStream.write(data);
        outputStream.flush();

    }
}
