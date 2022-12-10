package com.ckl.rpc.registry;

import java.net.InetSocketAddress;

/**
 * 服务发现
 */
public interface ServiceDiscovery {
    /**
     * 查找服务
     *
     * @param serviceName 服务名称
     * @return 服务对应到socket地址
     */
    InetSocketAddress lookupService(String serviceName);
}
