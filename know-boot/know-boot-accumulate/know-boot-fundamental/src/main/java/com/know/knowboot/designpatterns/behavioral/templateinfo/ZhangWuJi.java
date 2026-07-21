package com.know.knowboot.designpatterns.behavioral.templateinfo;

/**
 * 具体实现类 - 张无忌
 */
public class ZhangWuJi extends AbstractSwordsman {

    @Override
    protected void neiGong() {
        System.out.println("运行九阳神功");
    }

    @Override
    protected void weapons() {
        System.out.println("");
    }

    @Override
    protected void moves() {
        System.out.println("使用招式乾坤大挪移");
    }

    @Override
    protected boolean hasWeapons() {
        return false;
    }

}
