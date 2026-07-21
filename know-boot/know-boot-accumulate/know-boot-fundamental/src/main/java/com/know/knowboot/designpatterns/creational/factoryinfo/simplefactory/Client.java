package com.know.knowboot.designpatterns.creational.factoryinfo.simplefactory;

/**
 * 在简单工厂模式中，抽象产品既可以是各个具体产品类实现的共同的接口，也可以是各个具体产品类继承的抽象父类。
 */
public class Client {

    public static void main(String[] args) {
        // 生成产品B
        IProuduct product = SimpleFactory.makeProduct("ProductB");
        product.doSomeThing();
    }

}
