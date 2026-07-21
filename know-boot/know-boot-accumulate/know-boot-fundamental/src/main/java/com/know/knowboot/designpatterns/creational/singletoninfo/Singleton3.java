package com.know.knowboot.designpatterns.creational.singletoninfo;

/**
 * 懒汉式
 * 线程不安全
 *
 * 在成员位置声明Singleton类型的静态变量，并没有进行对象的 赋值操作，那么什么时候赋值的呢？当调用getInstance()方法获取Singleton类的对象的时 候才创建Singleton类的对象，这样就实现了懒加载的效果。但是，如果是多线程环境，会出现 线程安全问题。
 *
 */
public class Singleton3 {

    // 私有构造方法
    private Singleton3() {
    }

    // 在成员位置创建该类的对象
    private static Singleton3 instance;

    // 对外提供静态方法获取该对象
    public static Singleton3 getInstance() {
        if (instance == null) {
            instance = new Singleton3();
        }
        return instance;
    }

}
