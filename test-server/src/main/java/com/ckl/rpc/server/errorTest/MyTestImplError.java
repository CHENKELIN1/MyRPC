package com.ckl.rpc.server.errorTest;

import com.ckl.rpc.annotation.MyRpcService;
import com.ckl.rpc.api.MyTest;
import com.ckl.rpc.enumeration.GroupName;

/**
 * MyTest出错接口实现
 */
@MyRpcService(group = GroupName.GROUP_1)
public class MyTestImplError implements MyTest {
    @Override
    public String getData() {
        int a = 1 / 0;
        return "success";
    }
}
