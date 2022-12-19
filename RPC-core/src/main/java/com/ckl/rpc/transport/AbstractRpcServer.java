package com.ckl.rpc.transport;

import com.ckl.rpc.annotation.MyRpcService;
import com.ckl.rpc.annotation.MyRpcServiceScan;
import com.ckl.rpc.config.DefaultConfig;
import com.ckl.rpc.entity.ServerStatus;
import com.ckl.rpc.enumeration.RpcError;
import com.ckl.rpc.exception.RpcException;
import com.ckl.rpc.provider.ServiceProvider;
import com.ckl.rpc.registry.ServiceRegistry;
import com.ckl.rpc.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Set;

/**
 * 抽象Rpc服务端
 */
@Slf4j
public abstract class AbstractRpcServer implements RpcServer {
    //    Ip地址
    protected String host;
    //    端口
    protected int port;
    //    服务注册者
    protected ServiceRegistry serviceRegistry;
    //    服务提供者
    protected ServiceProvider serviceProvider;
    //    服务器状态
    protected ServerStatus serverStatus;

    //    扫描服务
    public void scanServices() {
//        获取当前方法全名
        String mainClassName = ReflectUtil.getStackTrace();
        String basePackage;
        try {
            Class<?> startClass = Class.forName(mainClassName);
            if (!startClass.isAnnotationPresent(MyRpcServiceScan.class)) {
                log.warn("启动类缺少 @MyRpcServiceScan 注解");
                basePackage = DefaultConfig.DEFAULT_PACKAGE;
//                throw new RpcException(RpcError.SERVICE_SCAN_PACKAGE_NOT_FOUND);
            } else {
                basePackage = startClass.getAnnotation(MyRpcServiceScan.class).value();
                if ("".equals(basePackage)) {
                    basePackage = mainClassName.substring(0, mainClassName.lastIndexOf("."));
                }
            }
        } catch (ClassNotFoundException e) {
            log.error("出现未知错误");
            throw new RpcException(RpcError.UNKNOWN_ERROR);
        }
//        遍历当前包下所有类
        Set<Class<?>> classSet = ReflectUtil.getClasses(basePackage);
        for (Class<?> clazz : classSet) {
//            若包含service注解
            if (clazz.isAnnotationPresent(MyRpcService.class)) {
//                获取服务名称
                String group = clazz.getAnnotation(MyRpcService.class).group();
//                获取服务
                Object obj;
                try {
                    obj = clazz.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    log.error("创建 " + clazz + " 时有错误发生");
                    continue;
                }
//                注册服务
                if ("".equals(group)) {
                    group = DefaultConfig.DEFAULT_GROUP;
                }
                Class<?>[] interfaces = clazz.getInterfaces();
                for (Class<?> oneInterface : interfaces) {
                    publishService(obj, oneInterface.getCanonicalName(), group);
                }
            }
        }
    }

    /**
     * 发布服务
     *
     * @param service     服务提供者
     * @param serviceName 服务名称
     * @param <T>
     */
    @Override
    public <T> void publishService(T service, String serviceName, String group) {
//        添加服务提供者
        serviceProvider.addServiceProvider(service, serviceName, group);
//        注册服务
        serviceRegistry.register(serviceName, group, new InetSocketAddress(host, port));
    }

}
