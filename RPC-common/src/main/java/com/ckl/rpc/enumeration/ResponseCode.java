package com.ckl.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应状态码
 */
@AllArgsConstructor
@Getter
public enum ResponseCode {

    SUCCESS(200, "调用方法成功"),
    HEART_BEAT(201, "心跳报文"),
    FAIL(500, "调用方法失败"),
    NOT_FOUND_METHOD(500, "未找到指定方法"),
    NOT_FOUND_CLASS(500, "未找到指定类"),
    ILLEGAL_ACCESS_EXCEPTION(500, "非法访问"),
    INVOCATION_TARGET_EXCEPTION(500, "调用目标出错"),
    SERVER_BUSY(500, "服务器繁忙");

    private final int code;
    private final String message;

}
