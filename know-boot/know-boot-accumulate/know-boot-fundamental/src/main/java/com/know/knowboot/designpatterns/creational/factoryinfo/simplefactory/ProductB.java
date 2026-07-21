package com.know.knowboot.designpatterns.creational.factoryinfo.simplefactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProductB implements IProuduct {

    @Override
    public void doSomeThing() {
        log.info("我是ProductB");
    }

}
