package com.know.knowboot.designpatterns.behavioral.stateinfo;

/**
 * 工单
 * 状态的环境，用于执行状态对应的行为
 */
public class WorkFlow {

    private FlowTask task;

    public WorkFlow(FlowTask task) {
        this.task = task;
    }

    public void changeFlowTask(FlowTask task) {

        System.out.println(this.task.getClass().getName() + "结束，当前环节:" + task.getClass().getName());
        this.task = task;
    }

    public void next() {
        System.out.println("下一步");
        this.task.next(this);
    }

    public void back() {
        System.out.println("驳回");
        this.task.back(this);
    }

}
