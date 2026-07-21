package com.know.knowboot.designpatterns.structural.facade;

import com.know.knowboot.designpatterns.structural.facade.subsystem.AirConditioner;
import com.know.knowboot.designpatterns.structural.facade.subsystem.Calorifier;
import com.know.knowboot.designpatterns.structural.facade.subsystem.Curtain;
import com.know.knowboot.designpatterns.structural.facade.subsystem.FloorMoppingRobot;

/**
 * 小爱同学（外观角色 Facade）
 */
public class XiaoaiClassmate {

    // 定义子系统对象
    private AirConditioner airConditioner;
    private Calorifier calorifier;
    private Curtain curtain;
    private FloorMoppingRobot floorMoppingRobot;

    // 通过构造器根据单例模式获得子系统对象
    public XiaoaiClassmate() {
        this.airConditioner = AirConditioner.getInstance();
        this.calorifier = Calorifier.getInstance();
        this.curtain = Curtain.getInstance();
        this.floorMoppingRobot = FloorMoppingRobot.getInstance();
    }

    public void getUp() {
        System.out.println("我要起床啦！");
        airConditioner.turnOn();
        calorifier.turnOn();
        curtain.turnOff();
    }

    public void goToWork() {
        System.out.println("我要去上班啦");
        airConditioner.turnOff();
        calorifier.turnOff();
        floorMoppingRobot.turnOn();
    }

}
