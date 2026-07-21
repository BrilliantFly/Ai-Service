package com.know.knowboot.designpatterns.creational.factoryinfo.factorymethod;

/**
 * 生产ProductB的具体工厂
 */
public class FactoryB implements IFactory {

    @Override
    public IProduct makeProduct() {
        return new ProductB();
    }

}
