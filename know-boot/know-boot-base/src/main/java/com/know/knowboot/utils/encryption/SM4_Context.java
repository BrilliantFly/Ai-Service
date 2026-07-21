package com.know.knowboot.utils.encryption;

/**
 * 国产商用加密算法
 * Created by Administrator on 2019/8/7.
 */
public class SM4_Context {

    public int mode;

    public long[] sk;

    public boolean isPadding;

    public SM4_Context()
    {
        this.mode = 1;
        this.isPadding = true;
        this.sk = new long[32];
    }

}
