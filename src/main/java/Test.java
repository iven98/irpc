import consumer.app.ConsumerApp;
import provider.service.Calculator;
import provider.service.CalculatorImpl;

public class Test {
    public static void main(String[] args) {
        Calculator calculator= ConsumerApp.getService(Calculator.class,"127.0.0.1",8001);
        int res=calculator.add(100,86);
        System.out.println(res);
    }
}
