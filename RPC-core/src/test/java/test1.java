import com.ckl.rpc.enumeration.CompressType;
import com.ckl.rpc.enumeration.LimiterType;
import com.ckl.rpc.enumeration.LoadBalanceType;
import com.ckl.rpc.enumeration.SerializerCode;
import com.ckl.rpc.extension.compress.Compresser;
import com.ckl.rpc.extension.limit.Limiter;
import com.ckl.rpc.extension.loadbalance.LoadBalancer;
import com.ckl.rpc.extension.registry.registryHandler.RedisHandler;
import com.ckl.rpc.extension.serialize.Serializer;
import com.ckl.rpc.factory.ExtensionFactory;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.net.InetSocketAddress;
import java.util.Map;

public class test1 {
    @Test
    public void t1() {
        Class<Limiter> limitClass = Limiter.class;
        System.out.println("limitClass.getCanonicalName() = " + limitClass.getCanonicalName());
        System.out.println("limitClass.getName() = " + limitClass.getName());
        System.out.println("limitClass.getAnnotatedInterfaces() = " + limitClass.getAnnotatedInterfaces());
        System.out.println("limitClass.getComponentType() = " + limitClass.getComponentType());
        Class<?>[] classes = limitClass.getClasses();
        for (Class<?> aClass : classes) {
            System.out.println("aClass.getCanonicalName() = " + aClass.getCanonicalName());
        }
    }

    @Test
    public void t2() {
        System.out.println(ExtensionFactory.getExtension(Limiter.class, LimiterType.COUNTER.getCode()));
        System.out.println(ExtensionFactory.getExtension(LoadBalancer.class, LoadBalanceType.LOAD_BALANCE_RANDOM.getCode()));
        System.out.println(ExtensionFactory.getExtension(Compresser.class, CompressType.GZIP.getCode()));
        System.out.println(ExtensionFactory.getExtension(Serializer.class, SerializerCode.KRYO.getCode()));
    }

    @Test
    public void t3() {
        Jedis jedis = new Jedis("localhost", 6380);
        String key = RedisHandler.buildRedisKey("s1", "g1");
        String s1 = RedisHandler.buildRedisValue();
        String f1 = RedisHandler.buildField("s1", "g1", new InetSocketAddress("localhost", 9000));
        String s2 = RedisHandler.buildRedisValue();
        String f2 = RedisHandler.buildField("s1", "g1", new InetSocketAddress("localhost", 9001));
        jedis.hset(key, f1, s1);
        jedis.hset(key, f2, s2);
        System.out.println(key);
        Map<String, String> map = jedis.hgetAll(key);
        for (String s : map.keySet()) {
            System.out.println(s + "\t" + map.get(s));
        }

    }
}
