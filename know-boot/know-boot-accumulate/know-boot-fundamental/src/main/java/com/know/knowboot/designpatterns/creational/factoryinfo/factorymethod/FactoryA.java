package com.know.knowboot.designpatterns.creational.factoryinfo.factorymethod;

/**
 * 生产ProductA的具体工厂
 */
public class FactoryA implements IFactory {

    @Override
    public IProduct makeProduct() {
        return new ProductA();
    }

}
