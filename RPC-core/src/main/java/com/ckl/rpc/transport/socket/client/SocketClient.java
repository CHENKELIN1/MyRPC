package com.ckl.rpc.transport.socket.client;

import com.ckl.rpc.codec.SocketDecoder;
import com.ckl.rpc.codec.SocketEncoder;
import com.ckl.rpc.config.DefaultConfig;
import com.ckl.rpc.entity.RpcRequest;
import com.ckl.rpc.entity.RpcResponse;
import com.ckl.rpc.enumeration.LoadBalanceType;
import com.ckl.rpc.enumeration.RpcError;
import com.ckl.rpc.enumeration.SerializerCode;
import com.ckl.rpc.exception.RpcException;
import com.ckl.rpc.loadbalancer.LoadBalancer;
import com.ckl.rpc.registry.NacosServiceDiscovery;
import com.ckl.rpc.registry.ServiceDiscovery;
import com.ckl.rpc.serializer.CommonSerializer;
import com.ckl.rpc.transport.RpcClient;
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
    private final CommonSerializer serializer;

    public SocketClient() {
        this(DEFAULT_SERIALIZER,DEFAULT_LOAD_BALANCE);
    }

    public SocketClient(LoadBalanceType loadBalancer) {
        this(DEFAULT_SERIALIZER, loadBalancer);
    }

    public SocketClient(SerializerCode serializer) {
        this(serializer,DEFAULT_LOAD_BALANCE );
    }

    public SocketClient(SerializerCode serializer, LoadBalanceType loadBalancer) {
        this.serviceDiscovery = new NacosServiceDiscovery(LoadBalancer.getByType(loadBalancer));
        this.serializer = CommonSerializer.getByType(serializer);
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
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest.getInterfaceName(),rpcRequest.getGroup());
//        创建socket连接
        try (Socket socket = new Socket()) {
            socket.connect(inetSocketAddress);
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
//            写入数据
            SocketEncoder.writeObject(outputStream, rpcRequest, serializer);
//            读出响应数据
            Object obj = SocketDecoder.readObject(inputStream);
            RpcResponse rpcResponse = (RpcResponse) obj;
//            响应检查
            RpcMessageChecker.check(rpcRequest, rpcResponse);
            return rpcResponse;
        } catch (IOException e) {
            log.error("调用时有错误发生：", e);
            throw new RpcException("服务调用失败: ", e);
        }
    }

}
