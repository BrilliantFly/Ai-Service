package com.know.knowboot.designpatterns.structural.adapter;

public class AdapterPattern {

    public static void main(String[] args){

        /**
         * 定义具体使用目标类，并通过Adapter类调用所需要的方法从而实现目标
         */
        Target mAdapter = new Adapter();
        mAdapter.Request();

    }

}
