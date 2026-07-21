package com.know.knowboot.designpatterns.behavioral.visitorinfo.visitor;

import com.know.knowboot.designpatterns.behavioral.visitorinfo.element.ConcreteElementA;
import com.know.knowboot.designpatterns.behavioral.visitorinfo.element.ConcreteElementB;

/**
 * 访问者
 */
public interface Visitor {

    void visitConcreteElementA(ConcreteElementA concreteElementA);  //访问A元素
    void visitConcreteElementB(ConcreteElementB concreteElementB);  //访问B元素

}
