import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.ckl.rpc.config.DefaultConfig;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class testNacos {
    @Test
    public void test1() throws NacosException {
        NamingService namingService = NacosFactory.createNamingService(DefaultConfig.DEFAULT_NACOS_SERVER_ADDRESS);
        namingService.registerInstance("s1", "a", "127.0.0.1", 9999);
        namingService.registerInstance("s1", "b", "127.0.0.1", 9998);
        List<Instance> instances = namingService.getAllInstances("s1");
        instances.forEach(System.out::println);

    }

    @Test
    public void t2() {
        String format = new SimpleDateFormat("yyyy/MM/dd/HH:mm:ss").format(new Date());
        System.out.println("format.length() = " + format.length());
        System.out.println("format = " + format);
        byte[] bytes = format.getBytes();
        System.out.println(bytes.length);
        System.out.println("bytes = " + bytes);
        System.out.println("new Date() = " + new Date());
    }
}
