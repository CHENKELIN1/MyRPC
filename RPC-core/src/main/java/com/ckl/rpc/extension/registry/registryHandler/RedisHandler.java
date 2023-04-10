package com.ckl.rpc.extension.registry.registryHandler;

import com.ckl.rpc.annotation.MyRpcExtension;
import com.ckl.rpc.config.DefaultConfig;
import com.ckl.rpc.entity.Register;
import com.ckl.rpc.enumeration.RegistryCode;
import com.ckl.rpc.extension.registry.RegistryHandler;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@MyRpcExtension(registryCode = RegistryCode.REDIS)
public class RedisHandler implements RegistryHandler {
    private static final Jedis redisCLi;

    static {
        redisCLi = new Jedis(DefaultConfig.DEFAULT_REDIS_HOST, DefaultConfig.DEFAULT_REDIS_PORT);
        String ping = redisCLi.ping();
        log.info("与服务注册中心连接成功 ping:{}", ping);
    }

    @Override
    public void registerService(String serviceName, String group, InetSocketAddress address) throws Exception {
        String key = buildRedisKey(serviceName, group);
        String field = buildField(address);
        String value = buildRedisValue();
        Long result = redisCLi.hset(key, field, value);
        if (result == 0) {
            log.error("服务注册失败:key:{},field:{},value:{}", key, field, value);
        }
    }

    @Override
    public List<InetSocketAddress> getAllInstance(String serviceName, String group) throws Exception {
        String key = buildRedisKey(serviceName, group);
        Map<String, String> map = redisCLi.hgetAll(key);
        return map.keySet().stream().map(RedisHandler::fieldToAddress).collect(Collectors.toList());
    }

    @Override
    public void clearRegistry(Set<Register> service, InetSocketAddress address) throws Exception {
        if (!service.isEmpty() && address != null) {
            String field = buildField(address);
            for (Register register : service) {
                try {
                    String key = buildRedisKey(register.getServiceName(), register.getGroup());
                    Long res = redisCLi.hdel(key, field);
                    log.info("注销服务:service:{},address:{},res:{}",key,field,res);
                } catch (Exception e) {
                    log.error("注销服务 {},{} 失败", register.getServiceName(), register.getGroup(), e);
                }
            }
        }
    }

    private static final String REGISTRY = "registry";
    private static final String SPLIT = ":";

    public static String buildRedisKey(String serviceName, String group) {
        return REGISTRY + SPLIT + serviceName + SPLIT + group;
    }

    public static String buildRedisValue() {
        return String.valueOf(System.currentTimeMillis());
    }

    public static String buildField(InetSocketAddress inetSocketAddress) {
        return inetSocketAddress.getHostName() + SPLIT + inetSocketAddress.getPort();
    }

    public static InetSocketAddress fieldToAddress(String field) {
        String[] split = field.split(SPLIT);
        return new InetSocketAddress(split[0], Integer.parseInt(split[1]));
    }
}
