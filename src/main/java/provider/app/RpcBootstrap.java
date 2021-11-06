package provider.app;

import consumer.service.CalculatorRemoteImpl;
import provider.service.Calculator;
import provider.service.CalculatorImpl;
import request.ServiceRegister;

import java.io.IOException;

public class RpcBootstrap {
    public static void main(String[] args) throws IOException {
        Calculator calculator=new CalculatorImpl();
        ServiceRegister.export(8081,calculator);
    }
}
