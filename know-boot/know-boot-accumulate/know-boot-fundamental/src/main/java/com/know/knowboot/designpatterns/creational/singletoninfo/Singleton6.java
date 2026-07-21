package com.know.knowboot.designpatterns.creational.singletoninfo;

/**
 * 懒汉式
 * 静态内部类方式
 * 第一次加载Singleton类时不会去初始化INSTANCE，只有第一次调用getInstance，虚拟机加 载SingletonHolder 并初始化INSTANCE，这样不仅能确保线程安全，也能保证 Singleton 类的唯一性
 *
 */
public class Singleton6 {

    // 私有构造方法
    private Singleton6() {
    }

    private static class SingletonHolder {
        private static final Singleton6 INSTANCE = new Singleton6();
    }

    // 对外提供静态方法获取该对象
    public static Singleton6 getInstance() {
        return SingletonHolder.INSTANCE;
    }

}
