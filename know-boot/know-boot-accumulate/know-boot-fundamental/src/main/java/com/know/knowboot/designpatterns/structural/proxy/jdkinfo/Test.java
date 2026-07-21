package com.know.knowboot.designpatterns.structural.proxy.jdkinfo;

import com.know.knowboot.designpatterns.structural.proxy.staticinfo.Movie;
import com.know.knowboot.designpatterns.structural.proxy.staticinfo.RealMovie;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * 代理类没有实现Movie接口，而是实现了InvocationHandler接口，然后重写了invoke方法，与Movie接口的耦合度降低
 */
public class Test {

    public static void main(String[] args) {
        RealMovie realMovie = new RealMovie();
        InvocationHandler proxyMovie = new ProxyMovie(realMovie);
        // 创建代理对象
        Movie movie = (Movie) Proxy.newProxyInstance(realMovie.getClass().getClassLoader(), realMovie.getClass().getInterfaces(), proxyMovie);
        System.out.println(movie.getClass().getSimpleName());
        movie.play();
    }

}
