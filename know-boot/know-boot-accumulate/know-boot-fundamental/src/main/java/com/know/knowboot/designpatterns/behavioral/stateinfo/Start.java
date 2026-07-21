package com.know.knowboot.designpatterns.behavioral.stateinfo;

/**
 * // 开始环节
 */
public class Start implements FlowTask {

    @Override
    public void next(WorkFlow workFlow) {

        System.out.println("保存开始环节的信息....");
        workFlow.changeFlowTask(new Task1());
    }

    @Override
    public void back(WorkFlow workFlow) {

        System.out.println("已经是第一步了....");
    }

}
