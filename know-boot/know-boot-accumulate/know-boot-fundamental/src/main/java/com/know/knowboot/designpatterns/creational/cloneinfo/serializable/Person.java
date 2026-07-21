package com.know.knowboot.designpatterns.creational.cloneinfo.serializable;

import java.io.*;

/**
 * 通过序列化和反序列化的方式实现深复制
 */
public class Person implements Serializable {

    private String name;
    private boolean gender;
    private Interest interest;

    public Person(String name, boolean gender, Interest interest) {
        this.name = name;
        this.gender = gender;
        this.interest = interest;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public boolean isGender() {
        return gender;
    }
    public void setGender(boolean gender) {
        this.gender = gender;
    }
    public Interest getInterest() {
        return interest;
    }
    public void setInterest(Interest interest) {
        this.interest = interest;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", gender=" + gender +
                ", interest=" + interest +
                '}';
    }

    public static void main(String[] args) throws CloneNotSupportedException,ClassNotFoundException, IOException {
        Interest interest = new Interest("摄影");
        Person gg = new Person("gg",false,interest);
        System.out.println(gg);
        //使用序列化和反序列化实现深复制
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(gg);
        byte[] bytes = bos.toByteArray();

        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bis);

        Person dxy = (Person) ois.readObject();   //克隆好的对象！
        dxy.interest.setName("咖啡");

        System.out.println(dxy);
        System.out.println(gg);
    }

}
