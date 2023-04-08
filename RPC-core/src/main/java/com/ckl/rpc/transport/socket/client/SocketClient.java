package com.ckl.rpc.transport.socket.client;

import com.ckl.rpc.enumeration.*;
import com.ckl.rpc.extension.registry.RegistryHandler;
import com.ckl.rpc.factory.ExtensionFactory;
import com.ckl.rpc.codec.scoket.SocketDecoder;
import com.ckl.rpc.codec.scoket.SocketEncoder;
import com.ckl.rpc.config.DefaultConfig;
import com.ckl.rpc.entity.RpcRequest;
import com.ckl.rpc.entity.RpcResponse;
import com.ckl.rpc.exception.RpcException;
import com.ckl.rpc.extension.compress.Compresser;
import com.ckl.rpc.extension.loadbalance.LoadBalancer;
import com.ckl.rpc.extension.serialize.Serializer;
import com.ckl.rpc.registry.ServiceDiscoveryImpl;
import com.ckl.rpc.extension.registry.registryHandler.NacosHandler;
import com.ckl.rpc.registry.ServiceDiscovery;
import com.ckl.rpc.status.StatusHandler;
import com.ckl.rpc.transport.common.client.RpcClient;
import com.ckl.rpc.util.RpcMessageChecker;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Socket客户端
 */
@Slf4j
public class SocketClient implements RpcClient, DefaultConfig {
    //    服务发现者
    private final ServiceDiscovery serviceDiscovery;
    //    序列化方式
    private final Serializer serializer;
    private final Compresser compresser;


    public SocketClient(SerializerCode serializerCode, LoadBalanceType loadBalanceType, CompressType compressType, RegistryCode registryCode) {
        LoadBalancer loadBalancer = ExtensionFactory.getExtension(LoadBalancer.class, loadBalanceType.getCode());
        RegistryHandler registryHandler = ExtensionFactory.getExtension(RegistryHandler.class, registryCode.getCode());
        this.serviceDiscovery = new ServiceDiscoveryImpl(loadBalancer,registryHandler);
        this.serializer = ExtensionFactory.getExtension(Serializer.class, serializerCode.getCode());
        this.compresser = ExtensionFactory.getExtension(Compresser.class, compressType.getCode());
    }

    /**
     * 发送消息
     *
     * @param rpcRequest
     * @return
     */
    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
//        序列化器检查
        if (serializer == null) {
            log.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
//        查询服务socket地址
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest);
//        创建socket连接
        try (Socket socket = new Socket()) {
            socket.connect(inetSocketAddress);
            StatusHandler.ServerHandleSend(inetSocketAddress);
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
//            写入数据
            SocketEncoder.writeObject(outputStream, rpcRequest, serializer, compresser);
//            读出响应数据
            Object obj = SocketDecoder.readObject(inputStream);
            RpcResponse rpcResponse = (RpcResponse) obj;
//            响应检查
            RpcMessageChecker.check(rpcRequest, rpcResponse);
            StatusHandler.ClientHandleReceived(rpcResponse, inetSocketAddress);
            return rpcResponse;
        } catch (IOException e) {
            log.error("调用时有错误发生：", e);
            throw new RpcException("服务调用失败: ", e);
        }
    }

}
