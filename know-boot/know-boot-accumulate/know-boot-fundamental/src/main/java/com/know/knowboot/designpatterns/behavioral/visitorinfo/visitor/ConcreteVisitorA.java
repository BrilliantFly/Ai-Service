package com.know.knowboot.designpatterns.behavioral.visitorinfo.visitor;

import com.know.knowboot.designpatterns.behavioral.visitorinfo.element.ConcreteElementA;
import com.know.knowboot.designpatterns.behavioral.visitorinfo.element.ConcreteElementB;

/**
 * 第一种访问者的操作
 */
public class ConcreteVisitorA implements Visitor {

    @Override
    public void visitConcreteElementA(ConcreteElementA concreteElementA) {
        System.out.println("A元素进行第一种操作");
    }
    @Override
    public void visitConcreteElementB(ConcreteElementB concreteElementB) {
        System.out.println("B元素进行第一种操作");
    }

}
