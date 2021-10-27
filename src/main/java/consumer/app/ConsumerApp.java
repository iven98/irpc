package consumer.app;

import consumer.service.CalculatorRemoteImpl;
//client
public class ConsumerApp {
    public static void main(String[] args) {
        //发起rpc请求
        CalculatorRemoteImpl cal=new CalculatorRemoteImpl();
        int res=cal.add(1000,210);
        System.out.println("res= "+res);
    }
}
