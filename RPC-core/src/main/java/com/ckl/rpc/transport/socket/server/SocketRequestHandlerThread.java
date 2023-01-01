package com.ckl.rpc.transport.socket.server;

import com.ckl.rpc.codec.scoket.SocketDecoder;
import com.ckl.rpc.codec.scoket.SocketEncoder;
import com.ckl.rpc.entity.RpcRequest;
import com.ckl.rpc.entity.RpcResponse;
import com.ckl.rpc.entity.ServerStatus;
import com.ckl.rpc.extension.compress.Compresser;
import com.ckl.rpc.extension.serialize.Serializer;
import com.ckl.rpc.status.ServerStatusHandler;
import com.ckl.rpc.transport.RequestHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * socket请求处理线程
 */
@Slf4j
public class SocketRequestHandlerThread implements Runnable {
    //    socket连接
    private final Socket socket;
    //    请求处理器
    private final RequestHandler requestHandler;
    //    序列化方式
    private final Serializer serializer;
    private final ServerStatus status;
    private final Compresser compresser;

    public SocketRequestHandlerThread(Socket socket, RequestHandler requestHandler, Serializer serializer, ServerStatus status, Compresser compresser) {
        this.socket = socket;
        this.requestHandler = requestHandler;
        this.serializer = serializer;
        this.status = status;
        this.compresser = compresser;
    }

    /**
     * 执行方法
     */
    @Override
    public void run() {
//        创建输入输出流
        try (InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream()) {
//            读取输入
            RpcRequest rpcRequest = (RpcRequest) SocketDecoder.readObject(inputStream);
//            处理Rpc请求
            Object result = requestHandler.handle(rpcRequest);
            RpcResponse<Object> response;
            if (result instanceof RpcResponse) {
                response = (RpcResponse<Object>) result;
                response.setStatus(ServerStatusHandler.updateStatus(status));
            } else {
                //            响应请求
                response = RpcResponse.success(result, rpcRequest.getRequestId(), ServerStatusHandler.updateStatus(status));
            }
//            将请求写入输出流
            SocketEncoder.writeObject(outputStream, response, serializer, compresser);
        } catch (IOException e) {
            log.error("调用或发送时有错误发生：", e);
        }
    }
}
