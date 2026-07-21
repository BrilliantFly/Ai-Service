package com.know.knowboot.designpatterns.behavioral.visitorinfo;

import com.know.knowboot.designpatterns.behavioral.visitorinfo.element.ConcreteElementA;
import com.know.knowboot.designpatterns.behavioral.visitorinfo.element.ConcreteElementB;
import com.know.knowboot.designpatterns.behavioral.visitorinfo.visitor.ConcreteVisitorA;
import com.know.knowboot.designpatterns.behavioral.visitorinfo.visitor.ConcreteVisitorB;

public class Test {

    public static void main(String[] args) {
        ObjectStructure objectStructure = new ObjectStructure();
        ConcreteElementA concreteElementA = new ConcreteElementA();
        ConcreteElementB concreteElementB = new ConcreteElementB();
        objectStructure.add(concreteElementA);
        objectStructure.add(concreteElementB);
        objectStructure.accept(new ConcreteVisitorA()); //A元素进行第一种操作 B元素进行第一种操作
        objectStructure.accept(new ConcreteVisitorB()); //A元素进行第二种操作 B元素进行第二种操作
    }

}
