package com.know.knowboot.designpatterns.behavioral.commandinfo;

/**
 *  用户发出请求
 */
public class CommandMain {

    public static void main(String[] args) {
        Command command = new Command();
        Invoker invoker = new Invoker(command);
        System.out.println("用户发起请求");
        invoker.send();
    }

}
