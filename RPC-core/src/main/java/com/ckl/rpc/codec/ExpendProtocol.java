package com.ckl.rpc.codec;

import com.ckl.rpc.config.DefaultConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ExpendProtocol {
    public static final int TIME_LENGTH = 19;
    //    public static final int COMPRESS_LENGTH = 4;
    private String time;
//    private Integer compressType;

    public static byte[] expendProtocolHandleWrite(ExpendProtocol e) {
        byte[] bytes = new byte[DefaultConfig.EXPEND_LENGTH];
        System.arraycopy(e.getTime().getBytes(StandardCharsets.UTF_8), 0, bytes, 0, TIME_LENGTH);
//        System.arraycopy(DecodeUtil.intToBytes(e.getCompressType()),0,bytes,TIME_LENGTH,COMPRESS_LENGTH);
        return bytes;
    }

    public static ExpendProtocol expendProtocolHandleRead(byte[] expandData) {
        byte[] time = new byte[TIME_LENGTH];
//        byte[] compressType = new byte[COMPRESS_LENGTH];
        System.arraycopy(expandData, 0, time, 0, TIME_LENGTH);
//        System.arraycopy(expandData,TIME_LENGTH,compressType,0,COMPRESS_LENGTH);
        ExpendProtocol expendProtocol = new ExpendProtocol()
                .setTime(new String(time));
//                .setCompressType(DecodeUtil.bytesToInt(compressType));
        log.debug("协议扩展字段:" + expendProtocol);
        return expendProtocol;
    }
}
