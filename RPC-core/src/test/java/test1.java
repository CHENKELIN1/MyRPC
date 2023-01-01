import com.ckl.rpc.enumeration.CompressType;
import com.ckl.rpc.enumeration.LimiterType;
import com.ckl.rpc.enumeration.LoadBalanceType;
import com.ckl.rpc.enumeration.SerializerCode;
import com.ckl.rpc.extension.ExtensionFactory;
import com.ckl.rpc.extension.compress.Compresser;
import com.ckl.rpc.extension.limit.Limiter;
import com.ckl.rpc.extension.loadbalance.LoadBalancer;
import com.ckl.rpc.extension.serialize.Serializer;
import org.junit.Test;

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
        System.out.println(ExtensionFactory.getExtension(Limiter.class, LimiterType.COUNTER));
        System.out.println(ExtensionFactory.getExtension(LoadBalancer.class, LoadBalanceType.LOAD_BALANCE_RANDOM));
        System.out.println(ExtensionFactory.getExtension(Compresser.class, CompressType.GZIP));
        System.out.println(ExtensionFactory.getExtension(Serializer.class, SerializerCode.KRYO));
    }
}
