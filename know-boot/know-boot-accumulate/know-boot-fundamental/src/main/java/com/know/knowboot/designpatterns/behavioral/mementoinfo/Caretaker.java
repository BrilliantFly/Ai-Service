package com.know.knowboot.designpatterns.behavioral.mementoinfo;

/**
 * 备忘录管理者
 */
public class Caretaker {

    /**
     * 记录被保存的备忘录对象
     */
    private Memento memento = null;

    public Memento getMemento() {
        return memento;
    }

    public void setMemento(Memento memento) {
        this.memento = memento;
    }

}
