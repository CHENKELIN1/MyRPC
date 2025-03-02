package com.ckl.rpc.transport.socket.server;

import com.ckl.rpc.enumeration.RegistryCode;
import com.ckl.rpc.extension.registry.RegistryHandler;
import com.ckl.rpc.factory.ExtensionFactory;
import com.ckl.rpc.config.DefaultConfig;
import com.ckl.rpc.entity.Status;
import com.ckl.rpc.enumeration.CompressType;
import com.ckl.rpc.enumeration.SerializerCode;
import com.ckl.rpc.extension.compress.Compresser;
import com.ckl.rpc.extension.serialize.Serializer;
import com.ckl.rpc.factory.ThreadPoolFactory;
import com.ckl.rpc.hook.ShutdownHook;
import com.ckl.rpc.provider.ServiceProviderImpl;
import com.ckl.rpc.registry.ServiceRegistryImpl;
import com.ckl.rpc.transport.common.handler.RequestHandler;
import com.ckl.rpc.transport.common.server.AbstractRpcServer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

/**
 * Socket服务端
 */
@Slf4j
public class SocketServer extends AbstractRpcServer implements DefaultConfig {
    //    线程池
    private final ExecutorService threadPool;
    //    序列化方式
    private final Serializer serializer;
    private final Compresser compresser;
    //    请求处理器
    private final RequestHandler requestHandler = new RequestHandler();

    public SocketServer(String host, int port) {
        this(host, port, DEFAULT_SERIALIZER, DEFAULT_COMPRESSER,DEFAULT_SERVER_REGISTRY);
    }

    public SocketServer(String host, int port, SerializerCode serializer, CompressType compressType,RegistryCode registryCode) {
        this.host = host;
        this.port = port;
        threadPool = ThreadPoolFactory.createDefaultThreadPool("socket-rpc-server");
        this.serviceRegistry = new ServiceRegistryImpl(ExtensionFactory.getExtension(RegistryHandler.class, registryCode.getCode()));
        this.serviceProvider = new ServiceProviderImpl();
        this.serializer = ExtensionFactory.getExtension(Serializer.class, serializer.getCode());
        this.compresser = ExtensionFactory.getExtension(Compresser.class, compressType.getCode());
        this.status = new Status();
        scanServices();
    }

    /**
     * 启动服务
     */
    @Override
    public void start() {
//        创建连接
        try (ServerSocket serverSocket = new ServerSocket()) {
//            绑定ip端口
            serverSocket.bind(new InetSocketAddress(host, port));
            log.info("服务器启动……");
//            添加钩子函数
            ShutdownHook.getShutdownHook().addClearAllHook(serviceRegistry);
            Socket socket;
//            接收消息
            while ((socket = serverSocket.accept()) != null) {
                log.info("消费者连接: {}:{}", socket.getInetAddress(), socket.getPort());
//                使用线程池处理
                threadPool.execute(new SocketRequestHandlerThread(socket, requestHandler, serializer, status, compresser));
            }
//            关闭线程池
            threadPool.shutdown();
        } catch (IOException e) {
            log.error("服务器启动时有错误发生:", e);
        }
    }
}