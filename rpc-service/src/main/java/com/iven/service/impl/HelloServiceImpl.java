package com.iven.service.impl;
import com.iven.annotation.RpcProvider;
import com.iven.service.HelloService;

@RpcProvider
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return "hello, " + name;
    }
}
