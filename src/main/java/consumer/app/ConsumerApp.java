package consumer.app;

import consumer.service.CalculatorRemoteImpl;
import consumer.service.ConsumerProxy;
import provider.service.Calculator;
import provider.service.CalculatorImpl;

import java.lang.reflect.Proxy;

//client
public class ConsumerApp {

    public static void main(String[] args) {
        //发起rpc请求
//        CalculatorRemoteImpl cal=new CalculatorRemoteImpl();
//        int res=cal.add(1000,210);
//        System.out.println("res= "+res);

        ConsumerProxy proxy=new ConsumerProxy(CalculatorImpl.class);
        Calculator cal=(Calculator) Proxy.newProxyInstance(Calculator.class.getClassLoader(),new Class<?>[]{Calculator.class},proxy);
        int a=cal.div(5,1);
        System.out.println("RPC CONNECTED!"+a);
    }
}
