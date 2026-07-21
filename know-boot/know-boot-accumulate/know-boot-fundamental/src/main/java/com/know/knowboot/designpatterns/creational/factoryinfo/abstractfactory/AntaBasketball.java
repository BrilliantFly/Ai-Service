package com.know.knowboot.designpatterns.creational.factoryinfo.abstractfactory;

import lombok.extern.slf4j.Slf4j;

/**
 * 具体产品：安踏篮球
 */
@Slf4j
public class AntaBasketball implements Basketball {

    @Override
    public void sayBasketball() {
        log.info("我是安踏篮球");
    }

}
