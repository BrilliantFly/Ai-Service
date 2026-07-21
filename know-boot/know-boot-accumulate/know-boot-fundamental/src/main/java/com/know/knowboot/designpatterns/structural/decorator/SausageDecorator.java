package com.know.knowboot.designpatterns.structural.decorator;

/**
 * 香肠类
 */
public class SausageDecorator extends BattercakeDecotator {

    public SausageDecorator(Battercake battercake) {
        super(battercake);
    }

    @Override
    public String getMsg() {
        return super.getMsg()+ "1根香肠";
    }

    @Override
    public int getPrice() {
        return super.getPrice()+2;
    }


}
