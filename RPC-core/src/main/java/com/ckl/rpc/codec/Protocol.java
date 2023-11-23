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
    //    扩展协议
    ExpendProtocol expendProtocol;
    //    自定义协议标识
    private int magicNumber;
    //    包类型
    private int packageCode;
    //    序列化方式
    private int serializerCode;
    private int compressCode;
    //    数据长度
    private int dataLength;
    //    协议头长度
    private int expandLength;
    //    扩展内容
    private byte[] expandData;
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
        this.compressCode = in.readInt();
        this.dataLength = in.readInt();
        this.expandLength = in.readInt();
        this.expandData = new byte[expandLength];
        in.readBytes(expandData);
        data = new byte[dataLength];
        in.readBytes(data);
        this.expendProtocol = ExpendProtocol.expendProtocolHandleRead(expandData);
    }
}
