package com.ckl.rpc.server;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

@Slf4j
public class RpcServer {
    private final ExecutorService threadPool;

    public RpcServer() {
        int corePoolSize=5;
        int maxPoolSize=50;
        long keepAliveTime = 60;
        BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(100);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        this.threadPool = new ThreadPoolExecutor(corePoolSize,maxPoolSize,keepAliveTime,TimeUnit.SECONDS,workingQueue,threadFactory);
    }

    public void register(Object service,int port){
        try (ServerSocket serverSocket=new ServerSocket(port)){
            log.info("服务启动...");
            Socket socket;
            while ((socket=serverSocket.accept())!=null){
                log.info("客户端连接！IP:"+socket.getInetAddress()+" port:"+socket.getPort());
                threadPool.execute(new RequestHandler(socket,service));
            }
        } catch (IOException e) {
            log.error("连接时出错..."+e);
        }
    }
}
