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
