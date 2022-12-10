# MyRPC

实现一个简单的RPC框架

## 项目结构

```
.
├── RPC-api:接口
├── RPC-client:客户端测试    
├── RPC-common:通用类
│ └── src.main.java.com.ckl.rpc
│                         ├── entity:实体类        
│                         ├── enumeration:枚举类
│                         ├── exception:异常类
│                         ├── factory:工厂类
│                         └── util:工具类
├── RPC-core:核心功能实现
│ └── src.main.java.com.ckl.rpc
│                         ├── annotation:自定义注解
│                         ├── codec:编码译码器
│                         ├── handler:处理器
│                         ├── hook:钩子函数
│                         ├── loadbalancer:负载均衡器
│                         ├── provider:服务提供
│                         ├── registry:服务注册
│                         ├── serializer:序列化
│                         └── transport:通信
│                             ├── netty:Netty通信
│                             │ ├── client:Netty客户端
│                             │ └── server:Netty服务端
│                             └── socket:Socket通信
│                                 ├── client:Socket客户端
│                                 ├── server:Socket服务端
│                                 └── util:Socket工具
└── RPC-server:服务端测试 
```

## 协议说明

采用自定义协议防止粘包

```
MagicNumber     4bytes      自定义协议标识
PackageType     4bytes      包类型
SerializerType  4bytes      序列化方式
DataLength      4bytes      数据长度
Data            DataLength  传输数据   
```

## 功能实现

1. 实现基础远程调用功能
2. 支持多个注册服务
3. 使用Netty传输和通用序列化接口
4. 实现基于Kryo的序列化器
5. 实现基于Hessian的序列化器
6. 实现Netty客户端失败重连
7. 实现基于Nacos实现服务注册与发现
8. 实现自动注销服务
9. 实现负载均衡
10. 服务端自动注册

## 项目运行

1. 克隆本仓库到本地
2. 加载`pom.xml`依赖
3. 定义接口
4. 服务端实现接口
5. 编写服务提供者
6. 客户端远程调用
7. 启动Nacos，在NacosUtil中配置Nacos服务
8. 启动服务端和客户端

## 测试用例

1. 定义接口
    ```java
    package com.ckl.rpc.api;
   
    public interface MyTest {
        String getData();
    }
    ```
2. 服务端实现接口
    ```java
    package com.ckl.rpc.server;
   
    import com.ckl.rpc.annotation.Service;
    import com.ckl.rpc.api.MyTest;
   
    @Service
    public class MyTestImpl implements MyTest {
        @Override
        public String getData() {
            return "test success";
        }
    }
    ```
3. 服务提供者
    ```java
    package com.ckl.rpc.server;
    
    import com.ckl.rpc.annotation.ServiceScan;
    import com.ckl.rpc.serializer.CommonSerializer;
    import com.ckl.rpc.transport.RpcServer;
    import com.ckl.rpc.transport.netty.server.NettyServer;
    
    @ServiceScan
    public class TestNettyServer {
        public static void main(String[] args) {
            RpcServer server = new NettyServer("127.0.0.1", 9000, CommonSerializer.JSON_SERIALIZER);
            server.start();
        }
    }
    ```
4. 客户端远程调用

   ```java
   package com.ckl.rpc;
   
   import com.ckl.rpc.api.MyTest;
   import com.ckl.rpc.serializer.CommonSerializer;
   import com.ckl.rpc.transport.RpcClient;
   import com.ckl.rpc.transport.RpcClientProxy;
   import com.ckl.rpc.transport.netty.client.NettyClient;
   
   public class TestNettyClient {
       public static void main(String[] args) {
   //        创建Netty客户端
           RpcClient rpcClient = new NettyClient(CommonSerializer.JSON_SERIALIZER);
   //        创建rpc客户端代理
           RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcClient);
   //        远程过程调用接口
           MyTest myTest = rpcClientProxy.getProxy(MyTest.class);
           System.out.println(myTest.getData());
       }
   }
   ```

## 更新说明

- U:更新
- A:添加
- D:删除
- T:测试
- O:优化
- F:修复

## TODO list

### 项目方面

- [x] 负载均衡
- [ ] 路由策略
- [ ] 异常重试
- [ ] 熔断限流
- [ ] 业务分组
- [ ] 添加测试用例

### 深入学习

- [ ] Netty相关内容
- [ ] 注解反射相关内容
- [ ] 动态代理相关内容
- [ ] Nacos相关内容
- [ ] 钩子函数相关内容
- [ ] 多线程，JUC相关内容

## 参考

1. https://time.geekbang.org/column/intro/100046201
2. https://github.com/CN-GuoZiyang/My-RPC-Framework
3. https://github.com/Snailclimb/guide-rpc-framework