package com.know.knowboot.designpatterns.behavioral.stateinfo;

/**
 * 结单环节
 */
public class End implements FlowTask {

    @Override
    public void next(WorkFlow workFlow) {

        System.out.println("结单....");
    }

    @Override
    public void back(WorkFlow workFlow) {

        System.out.println("重置结单环节的信息....");
        workFlow.changeFlowTask(new Task1());
    }

}
