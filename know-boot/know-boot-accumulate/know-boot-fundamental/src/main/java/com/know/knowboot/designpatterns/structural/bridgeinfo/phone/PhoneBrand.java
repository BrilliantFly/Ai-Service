package com.know.knowboot.designpatterns.structural.bridgeinfo.phone;

import com.know.knowboot.designpatterns.structural.bridgeinfo.soft.Soft;

/**
 * 手机品牌抽象类及相应的实现类（Implementor）
 */
public abstract class PhoneBrand {

    public Soft soft;   //引用了软件类 类似桥梁，将两个抽象类连通

    public void setSoft(Soft soft) {
        this.soft = soft;
    }

    public abstract void run();

}
