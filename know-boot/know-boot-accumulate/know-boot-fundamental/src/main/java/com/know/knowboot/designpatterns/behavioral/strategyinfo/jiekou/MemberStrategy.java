package com.know.knowboot.designpatterns.behavioral.strategyinfo.jiekou;

/**
 * 抽象类策略
 */
public interface MemberStrategy {

    // 一个计算价格的抽象方法
    //price商品的价格 n商品的个数
    public double calcPrice(double price, int n);

}
