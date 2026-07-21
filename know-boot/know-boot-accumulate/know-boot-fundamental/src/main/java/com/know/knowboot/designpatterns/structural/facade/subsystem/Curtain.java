package com.know.knowboot.designpatterns.structural.facade.subsystem;

/**
 * 窗帘（子系统角色 SubSystem）
 */
public class Curtain {

    private static Curtain curtain = new Curtain();

    public static Curtain getInstance() {
        return curtain;
    }

    public void turnOn() {
        System.out.println("打开窗帘");
    }

    public void turnOff() {
        System.out.println("关闭窗帘");
    }

}
