package com.know.knowboot.designpatterns.structural.proxy.cglibinfo;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CglibServiceProxy implements MethodInterceptor {

    private Object object;

    public CglibServiceProxy(Object object) {
        this.object = object;
    }

    public Object getProxyInstance() {
        //创建代理类
        Enhancer enhancer = new Enhancer();
        //继承要被代理的类
        enhancer.setSuperclass(object.getClass());
        //设置回调函数
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("电影即将开始，大家有序进入影厅");
        Object obj = method.invoke(object);
        System.out.println("电影结束，大家离开电影院");
        return obj;
    }

}
