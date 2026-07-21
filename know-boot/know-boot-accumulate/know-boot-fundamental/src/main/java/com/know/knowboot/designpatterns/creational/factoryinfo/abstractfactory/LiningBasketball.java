package com.know.knowboot.designpatterns.creational.factoryinfo.abstractfactory;

import lombok.extern.slf4j.Slf4j;

/**
 * 具体产品：李宁篮球
 */
@Slf4j
public class LiningBasketball implements Basketball {

    @Override
    public void sayBasketball() {
        log.info("我是李宁篮球");
    }

}
