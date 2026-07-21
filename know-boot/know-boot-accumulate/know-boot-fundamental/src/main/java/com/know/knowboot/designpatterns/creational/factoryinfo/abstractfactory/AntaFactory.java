package com.know.knowboot.designpatterns.creational.factoryinfo.abstractfactory;

/**
 * 具体工厂，负责生产安踏篮球，安踏足球
 */
public class AntaFactory implements AbstractFactory {

    @Override
    public Basketball makeBasketball() {
        return new AntaBasketball();
    }

    @Override
    public Football makeFootball() {
        return new AntaFootball();
    }

}
