package com.ckl.rpc.exception;

/**
 * 序列化异常类
 */
public class SerializeException extends RuntimeException {
    public SerializeException(String msg) {
        super(msg);
    }
}
