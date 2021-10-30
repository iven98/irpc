package request;

import java.io.Serializable;

public class RpcRequest implements Serializable {

    private static final long serialVersionUID = 6011503509272346423L;
    public String className;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] arguments;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    public RpcRequest() {
    }

    public RpcRequest(String className, String methodName, Class<?>[] parameterTypes, Object[] arguments) {
        this.className = className;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.arguments = arguments;
    }
}
