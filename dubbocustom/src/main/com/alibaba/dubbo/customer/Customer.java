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
