package com.know.knowboot.designpatterns.behavioral.strategyinfo;

import com.know.knowboot.designpatterns.behavioral.strategyinfo.jiekou.AdvanceMemberStrategy;
import com.know.knowboot.designpatterns.behavioral.strategyinfo.jiekou.IntermediateMemberStrategy;
import com.know.knowboot.designpatterns.behavioral.strategyinfo.jiekou.MemberStrategy;
import com.know.knowboot.designpatterns.behavioral.strategyinfo.jiekou.PrimaryMemberStrategy;

public class Application {

    public static void main(String[] args) {

        // 具体行为策略
        MemberStrategy primaryMemberStrategy = new PrimaryMemberStrategy(); // 接口回调（向上转型）
        MemberStrategy intermediateMemberStrategy = new IntermediateMemberStrategy();
        MemberStrategy advanceMemberStrategy = new AdvanceMemberStrategy();

        // 用户选择不同策略
        MemberContext primaryContext = new MemberContext(primaryMemberStrategy);
        MemberContext intermediateContext = new MemberContext(intermediateMemberStrategy);
        MemberContext advanceContext = new MemberContext(advanceMemberStrategy);

        //计算一本300块钱的书
        System.out.println("普通会员的价格："+ primaryContext.qoutePrice(300,1));// 普通会员：300
        System.out.println("中级会员的价格："+ intermediateContext.qoutePrice(300,1));// 中级会员 270
        System.out.println("高级会员的价格："+ advanceContext.qoutePrice(300,1));// 高级会员240
    }

}
