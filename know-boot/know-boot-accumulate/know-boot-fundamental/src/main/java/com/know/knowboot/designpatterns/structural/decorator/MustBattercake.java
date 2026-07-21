package com.know.knowboot.designpatterns.structural.decorator;

/**
 * 具体组件角色
 * 必买套餐
 */
public class MustBattercake extends Battercake {

    @Override
    public String getMsg() {

        return "煎饼+可乐";
    }

    @Override
    public int getPrice() {
        return 10;
    }

}
