package com.know.knowboot.designpatterns.creational.singletoninfo;

/**
 * 恶汉式
 * 在静态代码块中创建该类对象
 *
 * 该方式在成员位置声明Singleton类型的静态变量，而对象的创建是在静态代码块中，也是对着 类的加载而创建。所以和饿汉式的方式1基本上一样，当然该方式也存在内存浪费问题。
 *
 */
public class Singleton2 {

    // 私有构造方法
    private Singleton2() {
    }

    // 在成员位置创建该类的对象
    private static Singleton2 instance;

    static {
        instance = new Singleton2();
    }

    // 对外提供静态方法获取该对象
    public static Singleton2 getInstance() {
        return instance;
    }

}
