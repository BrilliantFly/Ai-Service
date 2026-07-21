package com.know.knowboot.designpatterns.structural.decorator;

/**
 * 具体组件角色
 * 煎饼基础类
 */
public class BaseBattercake extends Battercake {

    @Override
    public String getMsg(){ return "煎饼";}

    @Override
    public int getPrice(){ return 5;}

}
