package com.know.knowboot.designpatterns.behavioral.publishsubscribeinfo;

/**
 * 抽象被观察者（Subject）抽象主题，提供了addObserver、deleteObserver、notifyObservers三个方法
 */
public interface Subject {

    //添加观察者
    void addObserver(Observer observer);
    //删除观察者
    void deleteObserver(Observer observer);
    //通知所有的观察者有更新
    void notifyObservers(String message);

}
