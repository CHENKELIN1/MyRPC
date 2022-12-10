# MyRPC

实现一个简单的RPC框架

## 项目结构

```
├── RPC-api         接口
├── RPC-client      客户端测试
├── RPC-common      实体对象，公共类
├── RPC-core        核心功能
└── RPC-server      服务端测试
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

## 更新说明

- U:更新
- A:添加
- D:删除
- T:测试
- O:优化
- F:修复

## TODO list

### 项目

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

## 参考文档

1. https://time.geekbang.org/column/intro/100046201
2. https://github.com/CN-GuoZiyang/My-RPC-Framework
3. https://github.com/Snailclimb/guide-rpc-framework