package com.know.knowboot.designpatterns.structural.facade.subsystem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 空调（子系统角色 SubSystem）
 */
public class AirConditioner {

    private static AirConditioner airConditioner = new AirConditioner();

    public static AirConditioner getInstance() {
        return airConditioner;
    }

    public void turnOn() {
        System.out.println("打开空调");
    }

    public void turnOff() {
        System.out.println("关闭空调");
    }

}
