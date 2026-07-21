package com.know.knowboot.designpatterns.behavioral.stateinfo;

/**
 * 流程环节抽象
 */
public interface FlowTask {

    void next(WorkFlow workFlow);

    void back(WorkFlow workFlow);

}
