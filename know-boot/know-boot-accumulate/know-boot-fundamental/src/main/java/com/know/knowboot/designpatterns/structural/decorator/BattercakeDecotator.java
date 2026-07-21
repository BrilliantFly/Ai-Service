package com.know.knowboot.designpatterns.structural.decorator;

/**
 * 抽象装饰器
 */
public class BattercakeDecotator extends Battercake {

    //静态代理，委派
    //(指定抽象类 而不指定BaseBattercake 实体是为了灵活,
    //因为可能以后会有多个类似BaseBattercake 继承了Battercake 的实现类)
    private Battercake battercake;

    public BattercakeDecotator(Battercake battercake) {
        this.battercake = battercake;
    }

    @Override
    public String getMsg() {
        return  this.battercake.getMsg();
    }

    @Override
    public int getPrice() {
        return  this.battercake.getPrice();
    }

}
