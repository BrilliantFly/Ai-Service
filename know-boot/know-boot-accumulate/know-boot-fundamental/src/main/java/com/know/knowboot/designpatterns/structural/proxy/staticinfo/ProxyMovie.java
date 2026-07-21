package com.know.knowboot.designpatterns.structural.proxy.staticinfo;

public class ProxyMovie implements Movie {

    Movie movie;

    public ProxyMovie(Movie movie) {
        this.movie = movie;
    }

    @Override
    public void play() {
        System.out.println("电影还没开始，买点爆米花");
        movie.play();
        System.out.println("电影结束了，买个哥斯拉模型");
    }

}
