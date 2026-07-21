package com.know.knowboot.designpatterns.behavioral.visitorinfo;

import com.know.knowboot.designpatterns.behavioral.visitorinfo.element.Element;
import com.know.knowboot.designpatterns.behavioral.visitorinfo.visitor.Visitor;

import java.util.ArrayList;
import java.util.List;

/**
 * 访问元素
 */
public class ObjectStructure {

    private List<Element> list = new ArrayList<>();
    public void add(Element element){
        list.add(element);
    }
    public void remove(Element element){
        list.remove(element);
    }
    public void accept(Visitor visitor){
        for (Element element : list) {
            element.accept(visitor);
        }
    }

}
