package request;

import java.io.Serializable;

public class CalculateRpcRequest implements Serializable {
    private static final long serialVersionUID = 7503710091945320739L;
    private int a;
    private int b;
    private String name;

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CalculateRpcRequest{" +
                "a=" + a +
                ", b=" + b +
                ", name='" + name + '\'' +
                '}';
    }
}
