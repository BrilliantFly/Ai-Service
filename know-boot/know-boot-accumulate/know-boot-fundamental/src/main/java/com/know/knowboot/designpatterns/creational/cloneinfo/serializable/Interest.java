package com.know.knowboot.designpatterns.creational.cloneinfo.serializable;

import java.io.Serializable;

public class Interest implements Serializable {

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
