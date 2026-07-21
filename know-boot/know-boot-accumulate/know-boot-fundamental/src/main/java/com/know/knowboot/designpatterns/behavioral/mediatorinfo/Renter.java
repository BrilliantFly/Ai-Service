package com.know.knowboot.designpatterns.behavioral.mediatorinfo;

/**
 * 租客
 * receive() 方法用于租客接收来自中介者的信息；send() 方法用于租客向中介者发信息。
 */
public class Renter {

    String account;//账号
    String name;
    Mediator me;//用户和中介者通信

    public void receive(String msg) {
        System.out.println(account + "\t" +name + "receive:");
        System.out.println("\t" +msg);
    }
    public void send(String msg) {
        me.send(this,msg);
    }

    public Renter(String account, String name, Mediator me) {
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
