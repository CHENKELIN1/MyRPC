package com.ckl.rpc.error;

import com.ckl.rpc.api.MyTest;
import com.ckl.rpc.bean.BeanFactory;
import com.ckl.rpc.enumeration.GroupName;

public class errorTest {
    public static void main(String[] args) {
        MyTest myTest2 = BeanFactory.getBean(MyTest.class, GroupName.GROUP_1);
        System.out.println(myTest2.getData());
    }
}
