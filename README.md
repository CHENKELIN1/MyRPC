# MyRPC

实现一个简单的RPC框架

## 协议说明

采用自定义协议防止粘包

```
MagicNumber     4bytes        自定义协议标识
PackageType     4bytes        包类型
SerializerType  4bytes        序列化方式
CompressCode    4bytes        压缩方式
DataLength      4bytes        数据长度
ExpandLength    4bytes        扩展协议长度
ExpandData      expandLength  扩展协议内容
Data            DataLength    传输数据   
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
11. 可扩展协议
12. 工厂统一管理服务
13. 服务器状态监控
14. 自适应负载均衡
15. 业务分组
16. 健康检测
17. 服务限流

## 项目运行

1. 克隆本仓库到本地
2. 加载`pom.xml`依赖
3. 定义接口
4. 服务端实现接口
5. 编写服务提供者
6. 客户端远程调用
7. 启动Nacos
8. 修改配置
9. 启动服务端和客户端

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
    @MyRpcService
    public class MyTestImpl implements MyTest {
        @Override
        public String getData() {
            return "test success";
        }
    }
    ```
3. 服务提供者
    ```java
    @MyRpcServiceScan
    public class TestNettyServer {
        public static void main(String[] args) {
            RpcServer server = new NettyServer("127.0.0.1", 9000);
            server.start();
        }
    }
    ```
4. 客户端远程调用

   ```java
   public class TestNettyClientSingle {
       public static void main(String[] args) {
   //        远程过程调用接口1
           MyTest myTest = BeanFactory.getBean(MyTest.class, DefaultConfig.DEFAULT_GROUP);
           System.out.println(myTest.getData());
           SingletonFactory.getInstance(ServerMonitor.class).showAllMonitorContent();
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
- [x] 熔断限流
- [x] 业务分组
- [x] 添加测试用例

### 深入学习

- [x] Netty相关内容
- [x] 注解反射相关内容
- [x] 动态代理相关内容
- [ ] Nacos相关内容
- [ ] 钩子函数相关内容
- [ ] 多线程，JUC相关内容

### 分支说明

- v1.0:实现基础远程调用;
- v2.0:实现多种序列化器;采用Netty通信;
- v3.0:实现Nacos服务注册发现;负载均衡;自动注册;连接复用;
- v4.0:实现可扩展协议;业务分组;健康检测;服务限流;
- v5.0:优化项目结构;修复错误;扩展负载均衡、服务限流;数据压缩;docs

### 参考

1. https://time.geekbang.org/column/intro/100046201
2. https://github.com/CN-GuoZiyang/My-RPC-Framework
3. https://github.com/Snailclimb/guide-rpc-framework