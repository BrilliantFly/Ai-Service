package com.know.knowboot.designpatterns.behavioral.templateinfo;

/**
 * 创建抽象类，定义算法框架
 */
public abstract class AbstractSwordsman {

    //空实现方法
    protected void hook() {
    }

    protected abstract void neiGong();

    protected abstract void weapons();

    protected abstract void moves();

    protected void meridian() {
        System.out.println("开启正经与奇经");
    }

    protected boolean hasWeapons() {
        return true;
    }

    //该方法为final方法，放置算法框架被覆写
    public final void fighting() {
        //1、首先发内功，抽象方法
        neiGong();
        //2、其次调整经脉，具体方法
        meridian();
        //3、然后【如果有的话】使用武器
        if (hasWeapons()) {
            weapons();
        }
        //4、使用招式
        moves();
        //5、钩子方法
        hook();
    }

}
