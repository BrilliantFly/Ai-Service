package com.know.knowboot.designpatterns.behavioral.publishsubscribeinfo;

/**
 * 具体观察者（ConcrereObserver）微信用户是观察者，里面实现了更新的方法：
 */
public class WeixinUser implements Observer {

    // 微信用户名
    private String name;
    public WeixinUser(String name) {
        this.name = name;
    }
    @Override
    public void update(String message) {
        System.out.println(name + "-" + message);
    }

}
