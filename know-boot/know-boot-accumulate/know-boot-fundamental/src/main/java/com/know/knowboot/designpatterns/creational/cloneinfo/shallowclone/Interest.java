package com.know.knowboot.designpatterns.creational.cloneinfo.shallowclone;

public class Interest {

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
    public String toString() {
        return "Interest{" +
                "name='" + name + '\'' +
                '}';
    }

}
