import com.ckl.rpc.bean.BeanFactory;
import org.junit.Test;

import java.util.Map;

public class test1 {
    @Test
    public void fun(){
        Map bean = BeanFactory.getBean(Map.class);
    }
}
