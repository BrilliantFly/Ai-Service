package com.know.knowboot.designpatterns.creational.factoryinfo.abstractfactory;

/**
 * 具体工厂，负责生产李宁篮球，李宁足球
 */
public class LiningFactoy implements AbstractFactory {

    @Override
    public Basketball makeBasketball() {
        return new LiningBasketball();
    }

    @Override
    public Football makeFootball() {
        return new LiningFootball();
    }

}
