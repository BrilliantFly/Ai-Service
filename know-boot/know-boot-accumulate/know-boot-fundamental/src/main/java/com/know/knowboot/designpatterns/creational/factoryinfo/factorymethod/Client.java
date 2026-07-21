package com.know.knowboot.designpatterns.creational.factoryinfo.factorymethod;

public class Client {

    public static void main(String[] arges) {
        // 生产ProductA
        FactoryA factoryA = new FactoryA();
        factoryA.makeProduct().doSomeThing();
    }

}
