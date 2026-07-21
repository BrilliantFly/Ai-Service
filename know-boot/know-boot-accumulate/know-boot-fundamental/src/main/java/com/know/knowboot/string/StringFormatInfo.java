package com.know.knowboot.string;

import java.text.MessageFormat;

public class StringFormatInfo {

    public static void main(String[] args) {

        // MessageFormat.format
        String a= "aaa";
        String b= "bb";
        String c= "c";
        StringBuilder sb = new StringBuilder();
        sb.append(a).append(b).append(c);
        System.out.println(MessageFormat.format(" {0} {1} {2} {3}", a, b,"",sb));
        System.out.println(MessageFormat.format(" ''{0}'' '{1}' {2} {3}", a, b,"",sb.toString()));

        // String.format
        String format2 = String.format("我是%s,我%d", "中国人", 18);
        System.out.println(format2);

        // String.join
        String message = String.join("-", "Java", "is", "cool");
        System.out.println(message);

        String[] str =  new String[]{"1", "2","3","4","5","6","7","9"};
        String result = String.join(" **** ",str);
        System.out.println(result);

    }

}
