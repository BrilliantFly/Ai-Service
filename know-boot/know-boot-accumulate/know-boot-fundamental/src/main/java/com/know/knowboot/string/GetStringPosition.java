package com.know.knowboot.string;

/**
 * 获取字符串某个字符的位置（多个重复字符）
 */
public class GetStringPosition {

    public static void main(String[] args) {

        //嵌套获取
        String str = "0123456_89_sdfdsdsf_23423_auauauau";
        //获得第一个点的位置
        int index = str.indexOf("_");
        System.out.println("获得第一个点的位置:" + index);
        //根据第一个点的位置 获得第二个点的位置
        index = str.indexOf("_", index + 1);
        System.out.println("根据第一个点的位置 获得第二个点的位置:" + index);


        //根据第一个点的位置 获得第三个点的位置
        index = str.indexOf("_", index + 1);
        System.out.println("根据第一个点的位置 获得第三个点的位置:" + index);


        //根据第一个点的位置 获得第四个点的位置
        index = str.indexOf("_", index + 1);
        System.out.println("根据第一个点的位置 获得第四个点的位置:" + index);


        //获得第倒数第一个点的位置
        int index1 = str.lastIndexOf("_");
        System.out.println("获得倒数第一个点的位置:" + index1);


        //获得第倒数第二个点的位置
        index1 = str.lastIndexOf("_",index - 1);
        System.out.println("获得倒数第二个点的位置:" + index1);
    }

}
