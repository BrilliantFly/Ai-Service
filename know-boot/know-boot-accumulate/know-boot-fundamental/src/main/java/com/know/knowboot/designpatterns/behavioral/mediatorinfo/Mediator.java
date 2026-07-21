package com.know.knowboot.designpatterns.behavioral.mediatorinfo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 中介
 * 因为中介必须知道所有用户的信息，所以定义了租客和房东的map集合，send() 方法用于接收某一方的信息，并且向另一方的所有对象广播出去。
 * Set<String> set = m2.keySet();//拿到map中的key存入set集合中
 */
public class Mediator {

    Map<String,Renter> m = new HashMap<>();
    Map<String,Saler> m2 = new HashMap<>();

    void addRenter(Renter r) {
        m.put(r.getAccount(),r);
    }
    void addSaler(Saler s) {
        m2.put(s.getAccount(),s);
    }

    public void send(Renter renter, String msg) {
        System.out.println("来自租客"+renter.account+"-"+renter.getName());
        System.out.println("\t"+msg);
        //拿到房东的set集合
        Set<String> set = m2.keySet();
        Iterator<String> iterable = set.iterator();
        //遍历房东set集合
        while (iterable.hasNext()) {
            String key = iterable.next(); //拿到房东的账号
            Saler sa = m2.get(key);//根据账号key拿到房东信息
            //调用房东的receive方法，将租客的信息发送给房东
            sa.receive(renter.getAccount()+"-"+renter.getName()+"-"+msg);
        }
    }
    public void send(Saler saler, String msg) {
        System.out.println("来自租客"+saler.account+"-"+saler.getName());
        System.out.println("\t"+msg);
        //拿到房东的set集合
        Set<String> set = m.keySet();
        Iterator<String> iterable = set.iterator();
        //遍历房东set集合
        while (iterable.hasNext()) {
            String key = iterable.next(); //拿到租客的账号
            Renter sa = m.get(key);//根据账号key拿到租客信息
            //调用租客的receive方法，将房东的信息发送给租客
            sa.receive(saler.getAccount()+"-"+saler.getName()+"-"+msg);
        }
    }

}
