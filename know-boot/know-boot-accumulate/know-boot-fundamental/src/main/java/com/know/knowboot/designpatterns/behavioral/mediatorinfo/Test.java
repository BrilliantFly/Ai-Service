package com.know.knowboot.designpatterns.behavioral.mediatorinfo;

public class Test {

    public static void main(String[] args) {
        Mediator mediator = new Mediator();
        //租客
        Renter renter1 = new Renter("1001","zhangyin",mediator);
        Renter renter2 = new Renter("1002","minmin",mediator);
        Renter renter3 = new Renter("1003","sisi",mediator);
        mediator.addRenter(renter1);
        mediator.addRenter(renter2);
        mediator.addRenter(renter3);
        //租户
        Saler saler1 = new Saler("2001","dongzi",mediator);
        Saler saler2 = new Saler("2002","haozi",mediator);
        Saler saler3 = new Saler("2003","xiangzi",mediator);
        mediator.addSaler(saler1);
        mediator.addSaler(saler2);
        mediator.addSaler(saler3);

        renter2.send("Minmin要租房！");
        System.out.println("====================================");
        saler2.send("浩子有房要出租！");
    }

}
