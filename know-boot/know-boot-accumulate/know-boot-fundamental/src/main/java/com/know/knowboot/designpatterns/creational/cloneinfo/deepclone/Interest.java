package com.know.knowboot.designpatterns.creational.cloneinfo.deepclone;

public class Interest implements Cloneable {

    private String name;

    public Interest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "Interest{" +
                "name='" + name + '\'' +
                '}';
    }

}
