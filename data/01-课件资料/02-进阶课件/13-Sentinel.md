# Sentinel

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/1462/1657258557058/dd1bac7295f04f1d9339c3fd9d46984e.png)

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/1462/1657258557058/9835188751fa4996bbe38864a0b839fa.png)

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/1462/1657258557058/6efeb6700b8545e6927441d5809d5c72.png)

# 一、熔断、降级、限流

## 1.熔断

&emsp;&emsp;**服务熔断**的作用类似于我们家用的保险丝，当某服务出现不可用或响应超时的情况时，为了防止整个系统出现雪崩，**暂时停止**对该服务的调用。

**停止**是说，当前服务一旦对下游服务进行熔断，当请求到达时，当前服务不再对下游服务进行调用，而是使用设定好的策略(如构建默认值)直接返回。

**暂时**是说，熔断后，并不会一直不再调用下游服务，而是以一定的策略(如每分钟调用 10 次，若均返回成功，则增大调用量)试探调用下游服务，当下游服务恢复可用时，自动停止熔断。

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/1462/1657258557058/487dc0237f5e4bb180af0e9b9bd6175e.png)

## 2.降级

&emsp;&emsp;**降级**是指当自身服务压力增大时，采取一些手段，增强自身服务的处理能力，以保障服务的持续可用。比如，下线非核心服务以保证核心服务的稳定、降低实时性、降低数据一致性。

&emsp;&emsp;为了预防某些功能出现负荷过载或者响应慢的情况，在其内部暂时舍弃一些非核心接口和数据的请求（如评论、积分），而直接返回一个提前准备好的 fallback(退路) 错误处理信息。释放CPU和内存资源，以保证整个系统的稳定性和可用性。

## 3.限流

&emsp;&emsp;**限流**是指上游服务对本服务请求 QPS 超过阙值时，通过一定的策略(如延迟处理、拒绝处理)对上游服务的请求量进行限制，以保证本服务不被压垮，从而持续提供稳定服务。常见的限流算法有滑动窗口、令牌桶、漏桶等。

# 二、Sentinel

## 1.其他组件对比

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/1462/1657258557058/a2c2950056424a4ab23ca48557c4b95b.png)

## 2.Sentinel介绍

&emsp;&emsp;随着微服务的流行，服务和服务之间的稳定性变得越来越重要。Sentinel 以流量为切入点，从流量控制、熔断降级、系统负载保护等多个维度保护服务的稳定性。

Sentinel 的历史：

* 2012 年，Sentinel 诞生，主要功能为入口流量控制。
* 2013-2017 年，Sentinel 在阿里巴巴集团内部迅速发展，成为基础技术模块，覆盖了所有的核心场景。Sentinel 也因此积累了大量的流量归整场景以及生产实践。
* 2018 年，Sentinel 开源，并持续演进。
* 2019 年，Sentinel 朝着多语言扩展的方向不断探索，推出 C++ 原生版本，同时针对 Service Mesh 场景也推出了 Envoy 集群流量控制支持，以解决 Service Mesh 架构下多语言限流的问题。
* 2020 年，推出 Sentinel Go 版本，继续朝着云原生方向演进。

Sentinel 分为两个部分:

* 核心库（Java 客户端）不依赖任何框架/库，能够运行于所有 Java 运行时环境，同时对 Dubbo / Spring Cloud 等框架也有较好的支持。
* 控制台（Dashboard）基于 Spring Boot 开发，打包后可以直接运行，不需要额外的 Tomcat 等应用容器。

Sentinel 可以简单的分为 Sentinel 核心库和 Dashboard。核心库不依赖 Dashboard，但是结合 Dashboard 可以取得最好的效果。

## 3. 基本概念及作用

基本概念：

**资源**：是 Sentinel 的关键概念。它可以是 Java 应用程序中的任何内容，例如，由应用程序提供的服务，或由应用程序调用的其它应用提供的服务，甚至可以是一段代码。在接下来的文档中，我们都会用资源来描述代码块。

只要通过 Sentinel API 定义的代码，就是资源，能够被 Sentinel 保护起来。大部分情况下，可以使用方法签名，URL，甚至服务名称作为资源名来标示资源。

**规则**：围绕资源的实时状态设定的规则，可以包括流量控制规则、熔断降级规则以及系统保护规则。所有规则可以动态实时调整。

主要作用：

1. 流量控制
2. 熔断降级
3. 系统负载保护

我们说的资源，可以是任何东西，服务，服务里的方法，甚至是一段代码。使用 Sentinel 来进行资源保护，主要分为几个步骤:

1. 定义资源
2. 定义规则
3. 检验规则是否生效

先把可能需要保护的资源定义好，之后再配置规则。也可以理解为，只要有了资源，我们就可以在任何时候灵活地定义各种流量控制规则。在编码的时候，只需要考虑这个代码是否需要保护，如果需要保护，就将之定义为一个资源。

## 4.Sentinel应用

**官方文档：**[https://github.com/alibaba/spring-cloud-alibaba/wiki/Sentinel](https://github.com/alibaba/spring-cloud-alibaba/wiki/Sentinel)

### 4.1 Sentinel控制台搭建

下载对应的jar包：https://github.com/alibaba/Sentinel/releases 下载适合自己的版本

通过命令行启动：

java -Dserver.port=8080 -Dcsp.sentinel.dashboard.server=localhost:8080 -Dproject.name=sentinel-dashboard -jar sentinel-dashboard.jar

启动后需要输入账号密码:sentinel sentinel

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/1462/1657258557058/49fcf9e0c3e14906a8690251cc56f12e.png)

登录成功后看到的页面

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/1462/1657258557058/5c2a2831d1d640e4a5b58afaf19ce8d8.png)

### 4.2 服务监控

&emsp;&emsp;如果我们需要把自己的服务被Sentinel监控，那么我们需要添加对应的依赖

```xml
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
        </dependency>
```

然后需要添加对应的属性信息

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/1462/1657258557058/cd7d243a9e794fca924cf92767743750.png)

然后我们访问请求后，在控制台就可以看到对应的监控信息了

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/1462/1657258557058/3480d38e5b884feb9124c9a5f70db2d5.png)

然后我们可以指定简单的限流规则

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/1462/1657258557058/9b3e31c7c62d44b88e1f33b7b059dd55.png)

表示1秒中内只能有一个QPS。超过就限流

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/1462/1657258557058/41b4ee67dcd94f97b418f1cd6995220e.png)

测试我们发送请求快一点就会出现限流的处理

### 4.3 实时监控

&emsp;&emsp;实时监控这块需要引入Acuator

```xml
<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
```

endpoint端点：

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/1462/1657258557058/138b9b5bd9c84fafac3a5d59f237aa03.png)

### 4.4 限流信息自定义

```java
@Component
public class SentinelUrlBlockHandler implements BlockExceptionHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlockException e) throws Exception {
        R r = R.error(500,"访问太快，稍后再试!");
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(r));
    }
}

```

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/1462/1657258557058/787a38ef6ed74a0487d3840dd5726728.png)

## 5.sentinel容器安装

&emsp;&emsp;直接在windows中安装Sentinel启动管理还是不太方便，所以我们还是把Sentinel安装在Docker容器中

拉取对应的镜像文件

```
docker pull bladex/sentinel-dashboard:1.7.2
```

启动容器

```
docker run --name sentinel  -d -p 8858:8858 -d  bladex/sentinel-dashboard:tagname
```

开机自启动

```
docker update --restart=always sentinel
```

访问：

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/1462/1657258557058/e9bba308a5d4472785b22127b1dada1c.png)

对应的客户端的使用：需要调整的配置信息

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/1462/1657258557058/38651d4f76e54bd3897612cce72307fa.png)

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/1462/1657258557058/8a693edbfea74beebbdce1452e98ad2e.png)

## 6.Sentinel监控所有服务

&emsp;&emsp;我们需要对每一个服务都添加Sentinel的配置。

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/1462/1657258557058/0b0803d77df441cb9987f1a65292801c.png)

## 7.熔断降级

https://github.com/alibaba/spring-cloud-alibaba/wiki/Sentinel

1.添加对应的配置

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/1462/1657258557058/b6c715d576f44450a1820104f904a680.png)

2.添加托底的方法

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/1462/1657258557058/228cbe69f66744ebb9c91a0a360675f2.png)

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/1462/1657258557058/179aa155093f4ba98b74d9acd5833e9a.png)

手动配置：客户端的Sentinel版本需要和服务器的版本要保持一致。

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/1462/1657258557058/e78b5c9f98b446108c53d1239b82166d.png)

## 8.自定义资源

https://github.com/alibaba/Sentinel/wiki/%E5%A6%82%E4%BD%95%E4%BD%BF%E7%94%A8#%E5%AE%9A%E4%B9%89%E8%B5%84%E6%BA%90
