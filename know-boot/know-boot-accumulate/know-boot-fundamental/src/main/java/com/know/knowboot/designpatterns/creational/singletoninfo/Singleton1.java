package com.know.knowboot.designpatterns.creational.singletoninfo;

/**
 * 饿汉式
 * 静态变量创建类的对象
 *
 * 该方式在成员位置声明Singleton类型的静态变量，并创建Singleton类的对象instance。 instance对象是随着类的加载而创建的。如果该对象足够大的话，而一直没有使用就会造成内存 的浪费
 *
 */
public class Singleton1 {

    // 私有构造方法
    private Singleton1() {
    }

    // 在成员位置创建该类的对象
    private static Singleton1 instance = new Singleton1();

    // 对外提供静态方法获取该对象
    public static Singleton1 getInstance() {
        return instance;
    }

}
