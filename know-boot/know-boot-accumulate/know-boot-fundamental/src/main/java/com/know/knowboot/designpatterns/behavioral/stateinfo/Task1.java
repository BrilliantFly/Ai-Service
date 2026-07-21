package com.know.knowboot.designpatterns.behavioral.stateinfo;

/**
 * 环节1
 */
public class Task1 implements FlowTask{

    @Override
    public void next(WorkFlow workFlow) {

        System.out.println("保存Task1环节的信息....");
        workFlow.changeFlowTask(new End());
    }

    @Override
    public void back(WorkFlow workFlow) {

        System.out.println("重置开始环节的信息....");
        workFlow.changeFlowTask(new Start());
    }

}
