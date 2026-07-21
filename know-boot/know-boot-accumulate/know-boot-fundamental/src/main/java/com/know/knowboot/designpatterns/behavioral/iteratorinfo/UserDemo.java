package com.know.knowboot.designpatterns.behavioral.iteratorinfo;

import java.util.ArrayList;

public class UserDemo {

    public static void main(String[] args) {
        IUser user = new User();
        user.add("tony",12,"beijing","开发部");
        user.add("marry",16,"shanghai","人力部");
        user.add("tom",18,"beijing","开发部");
        user.add("tylor",14,"shenzhen","财务部");

        //遍历一下ArrayList，把所有的数据都取出
        UserIterator userIterator = user.getIterator();
        while(userIterator.hasNext()){
            IUser u = (IUser)userIterator.next();
            System.out.println(u.getUserInfo());
        }
    }

}
