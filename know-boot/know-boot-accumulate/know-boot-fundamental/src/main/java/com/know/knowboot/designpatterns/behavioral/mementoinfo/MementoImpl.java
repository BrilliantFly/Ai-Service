package com.know.knowboot.designpatterns.behavioral.mementoinfo;

/**
 * 真正的备忘录对象，实现了备忘录窄接口 实现成私有的内部类，不让外部访问
 */
public class MementoImpl  implements Memento {

    /**
     * 示意，表示需要保存的状态
     */
    private String state = "";

    public MementoImpl(String state) {
        super();
        this.state = state;
    }

    public String getState() {
        return state;
    }

}
