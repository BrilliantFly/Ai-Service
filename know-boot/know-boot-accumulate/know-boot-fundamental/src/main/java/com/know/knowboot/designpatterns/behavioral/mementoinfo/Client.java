package com.know.knowboot.designpatterns.behavioral.mementoinfo;

public class Client {

    public static void main(String[] args) {
        // 创建一个原发器
        Originator o = new Originator();
        // 设置其初始状态
        o.setState("state 0");
        // 打印原发器当前的状态
        System.out.println("原发器的初始状态：" + o.getState());
        // 将原发器当前的状态保存到备忘录中
        Memento memento = o.createMemento();
        // 创建一个管理者
        Caretaker c = new Caretaker();
        // 将创建好的备忘录交给管理者进行管理
        c.setMemento(memento);
        // 改变原发器的状态
        o.setState("state 1");
        // 打印原发器当前的状态
        System.out.println("原发器改变后状态：" + o.getState());
        // 将原发器状态恢复到备忘录保存的状态
        o.recoverFromMemento(c.getMemento());
        // 再次打印原发器当前的状态
        System.out.println("原发器回复后状态：" + o.getState());
    }

}
