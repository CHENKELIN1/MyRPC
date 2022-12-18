import org.junit.Test;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

public class t1 {
    @Test
    public void fun(){
        OperatingSystemMXBean osmxb = ManagementFactory.getOperatingSystemMXBean();
        System.out.println(osmxb.getSystemLoadAverage());
        System.out.println(osmxb.getAvailableProcessors());
    }
}
