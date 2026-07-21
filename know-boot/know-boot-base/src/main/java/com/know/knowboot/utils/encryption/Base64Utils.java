package com.know.knowboot.utils.encryption;


import java.util.Base64;

/**
 * base64
 */
public class Base64Utils {

    public static void main(String[] args) {
        //Base64编码
        String str = "12345678abc";
        byte[] encodedAsBytes = str.getBytes();
        String encodedMime = Base64.getMimeEncoder().encodeToString(encodedAsBytes);
        System.out.println("编码:"+encodedMime);


        //Base64解码
        byte[] decodedBytes = Base64.getMimeDecoder().decode(encodedMime);
        String decodedMime = new String(decodedBytes);
        System.out.println("解码:"+decodedMime); //727dced7-15c7-48c6-bb11-416ab51f98bc-2a19434a-3a64-496e-b07b-b51b0445384c-22525be7-82c7-4a72-8594-238712d4d59e


    }

}
