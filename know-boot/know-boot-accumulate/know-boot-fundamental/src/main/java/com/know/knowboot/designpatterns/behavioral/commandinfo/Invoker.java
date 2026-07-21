package com.know.knowboot.designpatterns.behavioral.commandinfo;

/**
 * 创建调用者
 */
public class Invoker {

    private Command command;
    public Invoker(Command command){
        this.command = command;
    }

    public void send(){
        System.out.println("调用者收到命令");
        command.execute();
    }

}
