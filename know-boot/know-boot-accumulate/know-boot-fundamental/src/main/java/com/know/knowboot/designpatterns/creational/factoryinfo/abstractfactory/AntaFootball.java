package com.know.knowboot.designpatterns.creational.factoryinfo.abstractfactory;

import lombok.extern.slf4j.Slf4j;

/**
 * 具体产品：安踏足球
 */
@Slf4j
public class AntaFootball implements Football {

    @Override
    public void sayFootball() {
        log.info("我是安踏足球");
    }

}
