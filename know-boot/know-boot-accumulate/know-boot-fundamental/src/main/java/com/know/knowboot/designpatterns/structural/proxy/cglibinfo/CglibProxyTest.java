package com.know.knowboot.designpatterns.structural.proxy.cglibinfo;

public class CglibProxyTest {

    public static void main(String[] args) {
        CglibService service = new CglibService();
        CglibServiceProxy proxy = new CglibServiceProxy(service);
        CglibService proxyInstance = (CglibService)proxy.getProxyInstance();
        proxyInstance.movie();
    }

}
