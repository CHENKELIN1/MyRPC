package com.ckl.rpc.error;

import com.ckl.rpc.api.MyTest;
import com.ckl.rpc.bean.BeanFactory;
import com.ckl.rpc.enumeration.GroupName;

/**
 * 客户端错误测试用例
 */
public class errorTest {
    public static void main(String[] args) {
        MyTest myTest2 = BeanFactory.getBean(MyTest.class, GroupName.GROUP_1);
        System.out.println(myTest2.getData());
    }
}
