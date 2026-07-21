package com.know.knowboot.designpatterns.structural.decorator;

/**
 * 鸡蛋类
 */
public class EggDecorator extends BattercakeDecotator {

    public EggDecorator(Battercake battercake) {
        super(battercake);
    }

    @Override
    public String getMsg() {
        return super.getMsg()+ "1个鸡蛋";
    }

    @Override
    public int getPrice() {
        return super.getPrice()+1;
    }

}
