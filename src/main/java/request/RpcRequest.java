package request;

import java.io.Serializable;

public class RpcRequest implements Serializable {

    private static final long serialVersionUID = 6011503509272346423L;
    public Object serviceClass;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] arguments;

    public Object getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(Object serviceClass) {
        this.serviceClass = serviceClass;
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

    public RpcRequest(Object serviceClass, String methodName, Class<?>[] parameterTypes, Object[] arguments) {
        this.serviceClass = serviceClass;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.arguments = arguments;
    }
}
