package com.know.knowboot.designpatterns.creational.factoryinfo.factorymethod;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProductB implements IProduct {

    @Override
    public void doSomeThing() {
        log.info("我是ProductB");
    }

}
