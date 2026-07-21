package com.know.knowboot.designpatterns.behavioral.stateinfo;

public class Test {

    // 运行
    public static void main(String[] args) {

        WorkFlow workFlow = new WorkFlow(new Start());
        workFlow.next();
        workFlow.back();
        workFlow.next();
        workFlow.next();
        workFlow.next();
    }

}
