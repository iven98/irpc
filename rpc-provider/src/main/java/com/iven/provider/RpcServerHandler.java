package com.iven.provider;

import com.alibaba.fastjson.JSON;
import com.iven.util.RegisterCenter;
import com.iven.util.entity.RpcRegisterEntity;
import com.iven.util.entity.RpcResponse;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@ChannelHandler.Sharable
public class RpcServerHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
        //msg为接收到的请求信息
        RpcResponse response = new RpcResponse();
        //将请求信息解码
        RpcRegisterEntity rpcRegisterEntity=JSON.parseObject(msg,RpcRegisterEntity.class);
        //通过反射得到远程调用的类并执行该方法
        Object result = invoke(rpcRegisterEntity);
        try {
            //返回体
            response.setResult(result);
        } catch (Exception exception) {
            exception.printStackTrace();
            response.setException(exception);
        }
        //写入返回数据
        ctx.writeAndFlush(JSON.toJSONString(response));
    }

    private Object invoke(RpcRegisterEntity entity) {
        try {
            //接口名
            String interfaceName = entity.getServiceImplClassFullName();
//            String implClassName = RegisterCenter.getProviderData(interfaceName);
            //类名
            String implClassName = entity.getServiceImplClassFullName();
            Class<?> clazz = Class.forName(implClassName);
            String methodName = entity.getMethodName();
            Class<?>[] parameterTypes = entity.getParameterTypes();
            Object[] parameters = entity.getParameters();
            Method method = clazz.getMethod(methodName, parameterTypes);
            //通过反射得到结果
            return method.invoke(clazz.newInstance(), parameters);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            return e;
        }
    }
    //当Channel处理于活动状态时被调用
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        System.out.println(ctx.channel().remoteAddress().toString());
        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        System.out.println(JSON.toJSONString(cause));
        super.exceptionCaught(ctx, cause);
    }
}