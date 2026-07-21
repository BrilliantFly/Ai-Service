package com.know.knowboot.designpatterns.behavioral.publishsubscribeinfo;

public class Client {

    public static void main(String[] args) {
        WeixinSubject mSubscriptionSubject=new WeixinSubject ();
        //创建微信用户
        WeixinUser user1=new WeixinUser("杨影枫");
        WeixinUser user2=new WeixinUser("月眉儿");
        WeixinUser user3=new WeixinUser("紫轩");
        //订阅公众号
        mSubscriptionSubject.addObserver(user1);
        mSubscriptionSubject.addObserver(user2);
        mSubscriptionSubject.addObserver(user3);
        //公众号更新发出消息给订阅的微信用户
        mSubscriptionSubject.notifyObservers("刘望舒的专栏更新了");
    }

}
