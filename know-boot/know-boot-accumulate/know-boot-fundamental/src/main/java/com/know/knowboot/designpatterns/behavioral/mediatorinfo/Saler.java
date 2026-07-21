package com.know.knowboot.designpatterns.behavioral.mediatorinfo;

/**
 * 房东
 */
public class Saler {

    String account;
    String name;
    Mediator me;

    public void receive(String msg) {
        System.out.println(account + "\t" +name + "receive:");
        System.out.println("\t" +msg);
    }
    public void send(String msg) {
        me.send(this,msg);
    }

    public Saler(String account, String name, Mediator me) {
        this.account = account;
        this.name = name;
        this.me = me;
    }

    public String getAccount() {
        return account;
    }

    public String getName() {
        return name;
    }

}
