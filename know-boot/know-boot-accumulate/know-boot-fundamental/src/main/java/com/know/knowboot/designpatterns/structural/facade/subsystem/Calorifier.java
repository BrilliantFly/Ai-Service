package com.know.knowboot.designpatterns.structural.facade.subsystem;

/**
 * 热水器（子系统角色 SubSystem）
 */
public class Calorifier {

    private static Calorifier calorifier = new Calorifier();

    public static Calorifier getInstance() {
        return calorifier;
    }

    public void turnOn() {
        System.out.println("打开热水器");
    }

    public void turnOff() {
        System.out.println("关闭热水器");
    }

}
