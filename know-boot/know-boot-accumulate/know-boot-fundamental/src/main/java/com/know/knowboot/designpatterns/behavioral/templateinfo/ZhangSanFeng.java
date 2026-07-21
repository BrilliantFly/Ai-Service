package com.know.knowboot.designpatterns.behavioral.templateinfo;

/**
 * 具体实现类 - 张三丰
 */
public class ZhangSanFeng extends AbstractSwordsman {

    @Override
    protected void neiGong() {
        System.out.println("运行纯阳无极功");
    }

    @Override
    protected void weapons() {
        System.out.println("使用真武剑");
    }

    @Override
    protected void moves() {
        System.out.println("使用招式神门十三剑");
    }

    @Override
    protected void hook() {
        System.out.println("突然肚子不舒服，老夫要先去上厕所");
    }

}
