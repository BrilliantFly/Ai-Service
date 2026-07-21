package com.know.knowboot.designpatterns.behavioral.visitorinfo.element;

import com.know.knowboot.designpatterns.behavioral.visitorinfo.visitor.Visitor;

/**
 * 具体元素B
 */
public class ConcreteElementB implements Element {

    @Override
    public void accept(Visitor visitor) {
        visitor.visitConcreteElementB(this);
    }

}
