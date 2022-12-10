package com.ckl.rpc.util;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.ckl.rpc.enumeration.RpcError;
import com.ckl.rpc.exception.RpcException;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Nacos工具类
 */
@Slf4j
public class NacosUtil {
    //    nacos服务地址
    private static final String SERVER_ADDR = "81.68.85.4:8850";
    //    NamingService 是一种提供将名称映射到对象的方法的服务
    private static final NamingService namingService;
    //    服务名
    private static final Set<String> serviceNames = new HashSet<>();
    //    服务端地址
    private static InetSocketAddress address;

    static {
        namingService = getNacosNamingService();
    }

    /**
     * 连接Nacos创建NamingService
     *
     * @return
     */
    public static NamingService getNacosNamingService() {
        try {
            return NamingFactory.createNamingService(SERVER_ADDR);
        } catch (NacosException e) {
            log.error("连接到Nacos时有错误发生: ", e);
            throw new RpcException(RpcError.FAILED_TO_CONNECT_TO_SERVICE_REGISTRY);
        }
    }

    /**
     * 注册服务
     *
     * @param serviceName 服务名称
     * @param address     服务地址
     * @throws NacosException
     */
    public static void registerService(String serviceName, InetSocketAddress address) throws NacosException {
        namingService.registerInstance(serviceName, address.getHostName(), address.getPort());
        NacosUtil.address = address;
        serviceNames.add(serviceName);
    }

    /**
     * 获取所有实例
     *
     * @param serviceName
     * @return
     * @throws NacosException
     */
    public static List<Instance> getAllInstance(String serviceName) throws NacosException {
        return namingService.getAllInstances(serviceName);
    }

    /**
     * 清空所有注册表
     */
    public static void clearRegistry() {
        if (!serviceNames.isEmpty() && address != null) {
            String host = address.getHostName();
            int port = address.getPort();
            Iterator<String> iterator = serviceNames.iterator();
            while (iterator.hasNext()) {
                String serviceName = iterator.next();
                try {
                    namingService.deregisterInstance(serviceName, host, port);
                } catch (NacosException e) {
                    log.error("注销服务 {} 失败", serviceName, e);
                }
            }
        }
    }
}
