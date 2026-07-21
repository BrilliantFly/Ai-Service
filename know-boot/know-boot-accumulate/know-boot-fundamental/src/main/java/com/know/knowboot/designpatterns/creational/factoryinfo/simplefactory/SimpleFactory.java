package com.know.knowboot.designpatterns.creational.factoryinfo.simplefactory;

public class SimpleFactory {

    static IProuduct makeProduct(String productName) {
        if ("ProductA".equals(productName)) {
            return new ProductA();
        } else if ("ProductB".equals(productName)) {
            return new ProductB();
        } else {
            return null;
        }
    }

}
