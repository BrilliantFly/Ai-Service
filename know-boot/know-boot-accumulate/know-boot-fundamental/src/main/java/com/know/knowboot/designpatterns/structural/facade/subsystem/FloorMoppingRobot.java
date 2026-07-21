package com.know.knowboot.designpatterns.structural.facade.subsystem;

/**
 * 扫地机器人（子系统角色 SubSystem）
 */
public class FloorMoppingRobot {

    private static FloorMoppingRobot floorMoppingRobot = new FloorMoppingRobot();

    public static FloorMoppingRobot getInstance() {
        return floorMoppingRobot;
    }

    public void turnOn() {
        System.out.println("打开扫地机器人");
    }

    public void turnOff() {
        System.out.println("关闭扫地机器人");
    }

}
