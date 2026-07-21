package com.know.knowboot.designpatterns.creational.builderinfo;

import com.know.knowboot.designpatterns.creational.builderinfo.extend.Phone;

public class Client {

    public static void main(String[] args) {
        //创建指挥者对象
        Director director = new Director(new MobileBuilder());
        //让指挥者组装自行车
        Bike bike = director.construct();
        System.out.println(bike.getFrame());
        System.out.println(bike.getSeat());

        //创建手机对象,通过构建者对象获取手机对象
        Phone phone = Phone.builder()
                .cpu("intel")
                .screen("三星屏幕")
                .memory("金士顿内存条")
                .mainboard("华硕主板")
                .build();
        System.out.println(phone);
    }

}
