package com.know.knowboot.designpatterns.creational.cloneinfo.shallowclone;

/**
 * 浅克隆：当原型对象被复制时，只复制它本身和其中包含的值类型的成员变量，而引用类型的成员变量并没有复制。
 * 浅克隆对于引用类型，只克隆了引用，因此两个对象的interest公共同一个内存地址，一个对象变化，会引起另一个对象响应的变化。
 */
public class Person implements Cloneable {

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

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public static void main(String[] args) throws CloneNotSupportedException {
        Interest interest = new Interest("摄影");
        Person gg = new Person("gg",false,interest);
        System.out.println(gg);
        Person dxy = (Person)gg.clone();
        dxy.setName("dxy");
        dxy.setGender(true);
        dxy.interest.setName("咖啡");
        System.out.println(dxy);
        System.out.println(gg);
    }


}
