package com.know.knowboot.designpatterns.creational.singletoninfo;

/**
 * 懒汉式
 * 双重检查方式
 *
 * 双重检查锁模式是一种非常好的单例实现模式，解决了单例、性能、线程安全问题，上面的双重检 测锁模式看上去完美无缺，
 * 其实是存在问题，在多线程的情况下，可能会出现空指针问题，出现问 题的原因是JVM在实例化对象的时候会进行优化和指令重排序操作。
 * 要解决双重检查锁模式带来空指针异常的问题，只需要使用 volatile 关键字, volatile 关 键字可以保证可见性和有序性。
 *
 */
public class Singleton5 {

    // 私有构造方法
    private Singleton5() {
    }

    private static volatile Singleton5 instance;

    // 对外提供静态方法获取该对象
    public static Singleton5 getInstance() {
        // 第一次判断，如果instance不为null，不进入抢锁阶段，直接返回实例
        if (instance == null) {
            synchronized (Singleton5.class) {
                // 抢到锁之后再次判断是否为null
                if (instance == null) {
                    instance = new Singleton5();
                }
            }
        }
        return instance;
    }

}
