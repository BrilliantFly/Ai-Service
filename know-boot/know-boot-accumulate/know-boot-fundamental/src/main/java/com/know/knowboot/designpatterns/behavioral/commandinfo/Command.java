package com.know.knowboot.designpatterns.behavioral.commandinfo;

/**
 * 创建命令
 */
public class Command {

    private Receiver receiver;

    public Command(){
        receiver = new Receiver();
    }

    public void execute(){
        System.out.println("命令传到了接收者");
        receiver.action();
    }

}
