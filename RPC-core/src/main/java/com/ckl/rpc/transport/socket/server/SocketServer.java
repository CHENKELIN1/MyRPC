package com.ckl.rpc.transport.socket.server;

import com.ckl.rpc.transport.AbstractRpcServer;
import com.ckl.rpc.factory.ThreadPoolFactory;
import com.ckl.rpc.handler.RequestHandler;
import com.ckl.rpc.hook.ShutdownHook;
import com.ckl.rpc.provider.ServiceProviderImpl;
import com.ckl.rpc.registry.NacosServiceRegistry;
import com.ckl.rpc.serializer.CommonSerializer;
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
public class SocketServer extends AbstractRpcServer {
    //    线程池
    private final ExecutorService threadPool;
    //    序列化方式
    private final CommonSerializer serializer;
    //    请求处理器
    private final RequestHandler requestHandler = new RequestHandler();

    public SocketServer(String host, int port) {
        this(host, port, DEFAULT_SERIALIZER);
    }

    public SocketServer(String host, int port, Integer serializer) {
        this.host = host;
        this.port = port;
        threadPool = ThreadPoolFactory.createDefaultThreadPool("socket-rpc-server");
        this.serviceRegistry = new NacosServiceRegistry();
        this.serviceProvider = new ServiceProviderImpl();
        this.serializer = CommonSerializer.getByCode(serializer);
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
            ShutdownHook.getShutdownHook().addClearAllHook();
            Socket socket;
//            接收消息
            while ((socket = serverSocket.accept()) != null) {
                log.info("消费者连接: {}:{}", socket.getInetAddress(), socket.getPort());
//                使用线程池处理
                threadPool.execute(new SocketRequestHandlerThread(socket, requestHandler, serializer));
            }
//            关闭线程池
            threadPool.shutdown();
        } catch (IOException e) {
            log.error("服务器启动时有错误发生:", e);
        }
    }
}