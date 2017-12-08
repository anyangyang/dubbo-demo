package com.alibaba.dubbo.Service.ServiceImpl;


import com.alibaba.dubbo.service.DemoService;


public class DemoServiceImpl implements DemoService {
    public String sayHello() {
        System.out.println("hello dubbo");

        return "hello dubbo, this is a demo";
    }
}
