package com.know.knowboot.designpatterns.behavioral.strategyinfo;

import com.know.knowboot.designpatterns.behavioral.strategyinfo.jiekou.MemberStrategy;

/**
 * 负责和具体的策略类交互
 * 这样的话，具体的算法和直接的客户端调用分离了，使得算法可以独立于客户端独立的变化。
 * 上下文类/环境类
 */
public class MemberContext {

    // 用户折扣策略接口
    private MemberStrategy memberStrategy;

    // 注入构造方法
    public MemberContext(MemberStrategy memberStrategy) {
        this.memberStrategy = memberStrategy;
    }

    // 计算价格
    public double qoutePrice(double goodsPrice, int n){
        // 通过接口变量调用对应的具体策略
        return memberStrategy.calcPrice(goodsPrice, n);
    }

}
