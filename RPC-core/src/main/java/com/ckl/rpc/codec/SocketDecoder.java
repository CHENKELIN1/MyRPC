package com.ckl.rpc.codec;

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
        Protocol protocol=new Protocol(in);
        return ProtocolHandler.handleIn(protocol);
    }
}