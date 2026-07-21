package com.know.knowboot.designpatterns.behavioral.strategyinfo.jiekou;

/**
 * 普通会员——不打折
 */
public class PrimaryMemberStrategy implements MemberStrategy {

    //重写策略方法具体实现功能
    @Override
    public double calcPrice(double price, int n) {
        return price * n;
    }

}
