package com.iven.consumer;

import com.alibaba.fastjson.JSON;
import com.iven.annotation.RpcClient;
import com.iven.annotation.RpcComponentScan;
import com.iven.annotation.RpcConsumer;
import com.iven.proccess.ClassScanner;
import com.iven.proccess.RpcClientContentHandler;
import com.iven.service.HelloService;
import com.iven.util.RedisUtil;
import com.iven.util.RegisterCenter;
import com.iven.util.entity.RpcRegisterEntity;
//import com.iven.util.entity.RpcRegisterEntity2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@RpcConsumer
@RpcComponentScan("com.iven.consumer")
public class Consumer {
    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);

    @RpcClient
    private HelloService helloService;

    public static void main(String[] args) {
        initServiceCustomer();
        Map<Class, Object> rpcClientContentMap = RpcClientContentHandler.getRpcClientContentMap();
        Consumer consumer = (Consumer) rpcClientContentMap.get(Consumer.class);
        String res = consumer.helloService.sayHello("iven");
        logger.info("消费者远程调用返回结果：{}", res);
    }

    private static void initServiceCustomer() {
        final String CUSTOMER_KEY = "%s:customer";
        final String PROVIDER_KEY = "%s:provider";
        ClassScanner.init(Consumer.class);
        Set<Class> classSet = ClassScanner.getClassSet();
        classSet.forEach(c -> {
            Field[] declaredFields = c.getDeclaredFields();
            for (Field field : declaredFields) {
                try {
                    RpcClient annotation = field.getAnnotation(RpcClient.class);
                    if (Objects.nonNull(annotation)) {
                        Class<?> fieldType = field.getType();
                        String name = fieldType.getName();
//                        RedisUtil.record2Cache(String.format(CUSTOMER_KEY, name), c.getName());
//                        String serviceObject = RedisUtil.getObject(String.format(PROVIDER_KEY, name));
//                        RpcRegisterEntity2 rpcRegisterEntity2 = JSON.parseObject(serviceObject, RpcRegisterEntity2.class);
//                        RpcRegisterEntity2 rpcRegisterEntity2=new RpcRegisterEntity2();
                        RpcRegisterEntity rpcRegisterEntity = new RpcRegisterEntity();
                        rpcRegisterEntity.setHost(InetAddress.getLocalHost().getHostAddress());
                        rpcRegisterEntity.setInterfaceClassFullName(name);
//                        rpcRegisterEntity2.setInterfaceClassFullName(name);
                        RegisterCenter.registerConsumer(rpcRegisterEntity);
                        Object consumer = c.newInstance();
                        Object proxyInstance = getProxyInstance(fieldType);
                        field.set(consumer, proxyInstance);
                        RpcClientContentHandler.initRpcClientContent(c, consumer);
                    }
                } catch (InstantiationException | IllegalAccessException | UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static <T> T getProxyInstance(Class<T> tClass) {
        return (T) Proxy.newProxyInstance(tClass.getClassLoader(),
                new Class[]{tClass}, new ConsumerProxyInvocationHandler(tClass));
    }
}
