# dubbo-demo
dubbo 入门学习
http://dubbo.io/books/dubbo-user-book/  使用者官方文档

http://blog.csdn.net/ichsonx/article/details/39008519  对于 dubbo 基础的讲解


http://blog.csdn.net/noaman_wgs/article/details/70214612 



本文档参考 http://blog.csdn.net/noaman_wgs/article/details/70214612 

此 demo 采用 Dubbo spring zookeeper java7 maven3


1、安装 zookeeper  ——— 参考文档：http://blog.csdn.net/tlk20071/article/details/52028945


        window环境下，zookeeper 的安装 参考 http://blog.csdn.net/tlk20071/article/details/52028945

        mac 环境下，zookeeper 的安装参考：http://blog.csdn.net/whereismatrix/article/details/50420099


1.1、因为当前的环境为 mac ，并且 zookeeper 在 mac 环境下提供 brew 安装，所以非常方便

        1.1.1、  首先在 terminal 输入 brew info zookeeper, 如下图所示，我们可以看到当前环境中，并没有安装 zookeeper

            
    
        1.1.2、接下来就是用 brew 安装 zookeeper，输入 brew install zookeeper, 然后等待提示安装完成
  

    
    如上图所示，安装成功

        1.1.3、增加 zookeeper 的配置文件，安装成功之后，/usr/local/etc/zookeeper/ 已经有了缺省的配置文件
    
        

    
    
        1.1.4、暂时不需要明白配置文件的含义，用 less -N /usr/local/etc/zookeeper/zoo.cfg，查看配置文件的内容
    
          1 # The number of milliseconds of each tick
      2 tickTime=2000
      3 # The number of ticks that the initial 
      4 # synchronization phase can take
      5 initLimit=10
      6 # The number of ticks that can pass between 
      7 # sending a request and getting an acknowledgement
      8 syncLimit=5
      9 # the directory where the snapshot is stored.
     10 # do not use /tmp for storage, /tmp here is just 
     11 # example sakes.
     12 dataDir=/usr/local/var/run/zookeeper/data
     13 # the port at which the clients will connect
     14 clientPort=2181
     15 # the maximum number of client connections.
     16 # increase this if you need to handle more clients
     17 #maxClientCnxns=60
     18 #
     19 # Be sure to read the maintenance section of the 
     20 # administrator guide before turning on autopurge.
     21 #
     22 # http://zookeeper.apache.org/doc/current/zookeeperAdmin.html#sc_maintenance
     23 #
     24 # The number of snapshots to retain in dataDir
     25 #autopurge.snapRetainCount=3
     26 # Purge task interval in hours
     27 # Set to "0" to disable auto purge feature
     28 #autopurge.purgeInterval=1

     具体的 zookeeper 配置详解参考：http://blog.csdn.net/qinglu000/article/details/23844359

    ps：zookeeper 往期版本下载地址 http://archive.apache.org/dist/zookeeper/

     1.1.5、启动 zookeeper

       1.1.5.1、 可以先用 zkServer status 查看 zookeeper 的状态，

        


       1.1.5.2、 通过 zkServer start 启动 zookeeper

        

        1.1.5.3、 通过 zookeeper status 再来查看下 zookeeper 的状态

        
        
  1.1.6、在 terminal 输入  zkCli 打开 zookeeper 客户端

                    

由上图可知，当前的连接数为 0



ps： 下面教程中，我是使用 通过 zookeeper 官网下载的方式来使用 zookeeper



2 、接下来创建 dubbo 的demo ，一共是 三大模块，dubbo-api、dubbo-consumer、dubbo-provider




在最外层的 pom.xml 中导入依赖 jar 包


3、   首先在 dubbo-api 这个 moudle 中创建一个 interface 

            


该接口创建完成后，用 mvn install 来打包一下这个 moudle
4、然后在 dubbo-provider  中实现这个接口

      4.1、因为是在不同的 moudle 中，所以我们需要先在 dubbo-provider 的 pom.xml  中添加 dubbo-api
 的依赖

        

5、接下来在 spring 配置文件中 曝露这个服务

        
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://code.alibabatech.com/schema/dubbo
       http://code.alibabatech.com/schema/dubbo/dubbo.xsd" >


    <!--定义提供方应用信息-->
    <dubbo:application name="provider" owner="anyang" organization="dubbox" />
    <!--zookeeper 注册中心，使用之前需要先启动-->
    <dubbo:registry address="zookeeper://localhost:2181" />
    <!--使用端口号暴露服务-->
    <dubbo:protocol name="dubbo" port="20880" />
    <!--使用 dubbo 协议要暴露的服务-->
    <dubbo:service interface="com.alibaba.dubbo.service.DemoService" ref="demoService" protocol="dubbo" />
    
    <!--要暴露的接口的bean-->
    <bean id="demoService" class="com.alibaba.dubbo.Service.ServiceImpl.DemoServiceImpl" />



</beans>


6、加载配置文件，启动远程服务

    
package com.alibaba.dubbo.loadService;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class provider {

    public static void main(String[] args) throws IOException{

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("provider.xml");
        System.out.println(context.getDisplayName() + ":here");
        context.start();
        System.in.read(); // 按任意键推出服务
    }
}


7、再来创建消费者

    7.1、因为要调用 DemoService 这个接口的服务，所以需要在当前模块中添加  dubbo 的依赖
                
        
<dependency>
    <groupId>com.demo</groupId>
    <artifactId>dubbo-api</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
            

    7.1、首先 在 moudle dubbo-custom中创建 消费者的配置文件
    
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://code.alibabatech.com/schema/dubbo
       http://code.alibabatech.com/schema/dubbo/dubbo.xsd" >


    <!--消费者 应用信息-->
    <dubbo:application name="dubbo-customer" owner="anyang" organization="anyang" />

    <!--注册中心-->
    <dubbo:registry address="zookeeper://localhost:2181" />

    <!--使用 dubbo 协议调用定义好的 api 接口-->
    <dubbo:reference interface="com.alibaba.dubbo.service.DemoService" id="demoService" />




</beans>


7.3、加载 customer 配置文件，调用远程服务

    
package main.com.alibaba.dubbo.customer;
import com.alibaba.dubbo.service.DemoService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Customer {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:customer.xml");
        context.start();
        System.out.println("开始远程调用服务");
        DemoService demoService = (DemoService) context.getBean("demoService");
        System.out.println(demoService.sayHello());
    }
}



8、依次启动服务

    8.1、在 terminal 输入 zkCli 启动 zookeeper
    
        


    8.2、启动 provider 并在 zookeeper 注册自己的服务 （ps：provider 要在 customer 之前启动）

        

8.3、启动 customer 并从 zookeeper 订阅自己需要的服务
        
    





    

        
          

       

              
      
            
        





