package com.ckl.rpc.codec;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.IOException;
import java.io.InputStream;

/**
 * 自定义协议
 */
@Setter
@Getter
@ToString
public class Protocol {
    //    可扩展协议长度
    public static final int UNDEFINED_LENGTH = 16;
    //    魔数值
    public static final int MAGIC_NUMBER = 0xCAFEBABE;
    //    int长度
    public static final int INT_LENGTH = 4;
    //    协议固定长度
    public static final int PROTOCOL_STATIC_LENGTH = 32;
    //    自定义协议标识
    private int magicNumber;
    //    包类型
    private int packageCode;
    //    序列化方式
    private int serializerCode;
    //    数据长度
    private int dataLength;
    //    为定义协议
    private byte[] undefined;
    //    数据
    private byte[] data;

    /**
     * 读取ByteBuf内容，解析为协议数据
     *
     * @param in
     */
    public Protocol(ByteBuf in) {
        this.magicNumber = in.readInt();
        this.packageCode = in.readInt();
        this.serializerCode = in.readInt();
        this.dataLength = in.readInt();
        undefined = new byte[UNDEFINED_LENGTH];
        in.readBytes(undefined);
        data = new byte[dataLength];
        in.readBytes(data);
    }

    /**
     * 读取InputStream，解析为协议数据
     *
     * @param in
     * @throws IOException
     */
    public Protocol(InputStream in) throws IOException {
        byte[] buffer = new byte[INT_LENGTH];
        in.read(buffer);
        this.magicNumber = bytesToInt(buffer);
        in.read(buffer);
        this.packageCode = bytesToInt(buffer);
        in.read(buffer);
        this.serializerCode = bytesToInt(buffer);
        in.read(buffer);
        this.dataLength = bytesToInt(buffer);
        undefined = new byte[UNDEFINED_LENGTH];
        in.read(undefined);
        byte[] data = new byte[this.dataLength];
        in.read(data);
        this.data = data;
    }

    public static int bytesToInt(byte[] src) {
        int value;
        value = ((src[0] & 0xFF) << 24)
                | ((src[1] & 0xFF) << 16)
                | ((src[2] & 0xFF) << 8)
                | (src[3] & 0xFF);
        return value;
    }
}
