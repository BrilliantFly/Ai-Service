package com.know.knowboot.designpatterns.behavioral.mementoinfo;

public class Originator {

    /**
     * 示意，表示原发器的状态
     */
    private String state = "";

    /**
     * 创建备忘录，保存原发器的状态
     *
     * @return 创建好的备忘录对象
     */
    public Memento createMemento() {
        return new MementoImpl(state);
    }

    /**
     * 将原发器恢复到备忘录中保存的状态
     *
     * @param 保存有原发器状态的备忘录对象
     */
    public void recoverFromMemento(Memento memento) {
        MementoImpl mementoImpl = (MementoImpl) memento;
        this.state = mementoImpl.getState();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


}
