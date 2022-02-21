package com.iven.util.entity;

import java.io.Serializable;

public class RpcRegisterEntity implements Serializable {
    /**
     * 对服务消费者，该值表示调用方全类名；对服务提供者而言，该值表示接口实现全类名
     */
    private String interfaceClassFullName;

    /**
     * 接口Ip
     */
    private String host;

    /**
     * 接口端口号
     */
    private int port;
    /**
     * 服务提供者实现类名
     */
    private String serviceImplClassFullName;

    private String methodName;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    private Class<?>[] parameterTypes;

    private Object[] parameters;

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public RpcRegisterEntity() {
    }

    public RpcRegisterEntity(String interfaceFullName, String host, int port) {
        this.interfaceClassFullName = interfaceFullName;
        this.host = host;
        this.port = port;
    }

    public String getInterfaceClassFullName() {
        return interfaceClassFullName;
    }

    public void setInterfaceClassFullName(String interfaceClassFullName) {
        this.interfaceClassFullName = interfaceClassFullName;
    }

    public String getHost() {
        return host;
    }

    public RpcRegisterEntity setHost(String host) {
        this.host = host;
        return this;
    }

    public int getPort() {
        return port;
    }

    public RpcRegisterEntity setPort(int port) {
        this.port = port;
        return this;
    }

    public String getServiceImplClassFullName() {
        return serviceImplClassFullName;
    }

    public RpcRegisterEntity setServiceImplClassFullName(String serviceImplClassFullName) {
        this.serviceImplClassFullName = serviceImplClassFullName;
        return this;
    }

    @Override
    public String toString() {
        return "RpcRegisterEntity{" +
                "interfaceFullName='" + interfaceClassFullName + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                '}';
    }
}
