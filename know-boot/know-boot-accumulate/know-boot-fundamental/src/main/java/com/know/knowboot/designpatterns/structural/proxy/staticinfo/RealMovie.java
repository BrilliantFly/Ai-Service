package com.know.knowboot.designpatterns.structural.proxy.staticinfo;

public class RealMovie implements Movie {

    @Override
    public void play() {
        System.out.println("播放金刚大战哥斯拉");
    }

}
