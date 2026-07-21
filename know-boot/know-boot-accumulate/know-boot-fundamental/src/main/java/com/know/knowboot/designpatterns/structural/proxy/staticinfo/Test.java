package com.know.knowboot.designpatterns.structural.proxy.staticinfo;

/**
 * 在不改变RealMovie的前提下，使用ProxyMovie对其进行了增强。
 * 但是由于目标对象和代理对象都实现了同一个接口，一旦这个接口添加或者删除方法，那么代理对象和目标对象都要进行相应的操作，耦合度太高。
 * 静态代理的应用：在使用实现Runnable接口实现多线程的时候，将Runnable接口的实现类的对象作为Thread的构造函数的参数；
 */
public class Test {

    public static void main(String[] args) {
        RealMovie movie = new RealMovie();
        ProxyMovie proxyMovie = new ProxyMovie(movie);
        proxyMovie.play();
    }

}
