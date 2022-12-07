package com.ckl.rpc.server;

import com.ckl.rpc.entity.RpcRequest;
import com.ckl.rpc.entity.RpcResponse;
import com.ckl.rpc.enumeration.ResponseCode;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

@Slf4j
public class RequestHandler implements Runnable {

    private Socket socket;
    private Object service;
    public RequestHandler(Socket socket, Object service) {
        this.socket=socket;
        this.service=service;
    }

    @Override
    public void run() {
        try (ObjectInputStream objectInputStream=new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream objectOutputStream=new ObjectOutputStream(socket.getOutputStream())
        ){
            RpcRequest rpcRequest =(RpcRequest) objectInputStream.readObject();
            Object returnObject = invokeMethod(rpcRequest);
            objectOutputStream.writeObject(RpcResponse.success(returnObject));
            objectOutputStream.flush();
        } catch (IOException | ClassNotFoundException | InvocationTargetException |
                 IllegalAccessException e) {
            log.error("调用或发送时出错");
        }
    }

    private Object invokeMethod(RpcRequest rpcRequest) throws IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        Class<?> clazz = Class.forName(rpcRequest.getInterfaceName());
        if(!clazz.isAssignableFrom(service.getClass())) {
            return RpcResponse.fail(ResponseCode.NOT_FOUND_CLASS);
        }
        Method method;
        try {
            method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
        } catch (NoSuchMethodException e) {
            return RpcResponse.fail(ResponseCode.NOT_FOUND_METHOD.name());
        }
        return method.invoke(service, rpcRequest.getParameters());
    }
}
