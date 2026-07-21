package com.know.knowboot.designpatterns.creational.factoryinfo.abstractfactory;

public class Client {

    public static void main(String[] args){
        // 生产李宁篮球和安踏足球
        LiningFactoy liningFactoy = new LiningFactoy();
        AntaFactory antaFactory = new AntaFactory();

        liningFactoy.makeBasketball().sayBasketball();
        antaFactory.makeFootball().sayFootball();
    }

}
