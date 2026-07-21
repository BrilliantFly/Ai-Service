package com.know.knowboot.designpatterns.behavioral.visitorinfo.element;

import com.know.knowboot.designpatterns.behavioral.visitorinfo.visitor.Visitor;

/**
 * 不会改变的数据结构
 */
public interface Element {

    void accept(Visitor visitor);

}
