package com.know.knowboot.designpatterns.creational.factoryinfo.abstractfactory;

import lombok.extern.slf4j.Slf4j;

/**
 * 具体产品：李宁足球
 */
@Slf4j
public class LiningFootball implements Football {

    @Override
    public void sayFootball() {
        log.info("我是李宁足球");
    }

}
