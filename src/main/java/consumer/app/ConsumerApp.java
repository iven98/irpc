package consumer.app;

import consumer.service.CalculatorRemoteImpl;
import consumer.service.ConsumerProxy;
import provider.service.Calculator;
import provider.service.CalculatorImpl;

import java.lang.reflect.Proxy;

//client
public class ConsumerApp {
    public static<T> T getService(Class<T> clazz,String ip,int port){
        ConsumerProxy consumerProxy=new ConsumerProxy(ip,port);
        return (T)Proxy.newProxyInstance(ConsumerApp.class.getClassLoader(),new Class<?>[] {clazz},consumerProxy);
    }

    public static void main(String[] args) {
        Calculator calculator= ConsumerApp.getService(Calculator.class,"127.0.0.1",8081);
        int res=calculator.add(100,86);
        System.out.println("res = "+res);
    }
}
