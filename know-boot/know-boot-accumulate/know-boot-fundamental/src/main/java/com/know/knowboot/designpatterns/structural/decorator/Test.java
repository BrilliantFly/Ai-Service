package com.know.knowboot.designpatterns.structural.decorator;

public class Test {

    public static void main(String[] args){
        //Battercake battercake = new BaseBattercake();
        Battercake battercake = new MustBattercake();
        //煎饼有点小，想再加一个鸡蛋
        battercake = new EggDecorator(battercake);
        //再加一个鸡蛋
        battercake = new EggDecorator(battercake);
        //很饿，再加根香肠
        battercake = new SausageDecorator(battercake);

        System.out.println(battercake.getMsg() + ",总价" + battercake.getPrice());
    }

}
