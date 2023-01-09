package com.ckl.rpc.codec.scoket;

import com.ckl.rpc.codec.Protocol;
import com.ckl.rpc.codec.ProtocolHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

/**
 * Socket方式从输入流中读取字节并反序列化
 */
@Slf4j
public class SocketDecoder {
    /**
     * 自定义协议头
     */
    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    /**
     * 从流中读取对象
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static Object readObject(InputStream in) throws IOException {
//        协议解析
        Protocol protocol = new Protocol(in);
//        内容处理
        return ProtocolHandler.handleIn(protocol);
    }
}
