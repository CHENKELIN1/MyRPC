package com.ckl.rpc.codec;

import com.ckl.rpc.util.CommonUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Date;
@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ExpendProtocol {
    public static final int TIME_LENGTH = 19;
    private String time;
    public static byte[] expendProtocolHandleWrite(){
        byte[] bytes = CommonUtil.formatDate(new Date()).getBytes(StandardCharsets.UTF_8);
        return bytes;
    }
    public static void expendProtocolHandleRead(byte[] expandData){
        byte[] time=new byte[ExpendProtocol.TIME_LENGTH];
        System.arraycopy(expandData,0,time,0,ExpendProtocol.TIME_LENGTH);
        ExpendProtocol expendProtocol=new ExpendProtocol()
                .setTime(new String(time));
        log.info("协议扩展字段:"+expendProtocol);
    }
}
