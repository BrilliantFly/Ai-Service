package com.know.knowboot.designpatterns.behavioral.iteratorinfo;

import java.util.ArrayList;

/**
 * 用户迭代器
 */
public class UserIterator implements Iterator {

    //用户列表
    ArrayList<IUser> userList = new ArrayList<>();

    //索引位置
    int currentIndex = 0 ;

    //构造函数
    public UserIterator(ArrayList<IUser> userList) {
        this.userList = userList;
    }

    @Override
    public boolean hasNext() {
        if(currentIndex >= userList.size() ||
                userList.get(currentIndex) == null){
            return false;
        }
        return true;
    }

    @Override
    public IUser next() {
        return this.userList.get(currentIndex++);
    }

}
