package com.know.knowboot.designpatterns.behavioral.templateinfo;

public class Test {

    public static void main(String[] args) {
        /**
         * 模板方法模式
         */
        ZhangWuJi zhangWuJi = new ZhangWuJi();
        zhangWuJi.fighting();

        ZhangSanFeng zhangSanFeng = new ZhangSanFeng();
        zhangSanFeng.fighting();

    }

}
