package com.know.knowboot.algorithm;

import lombok.extern.slf4j.Slf4j;

/**
 * Rabin-Karp算法
 */
@Slf4j
public class RabinKarpStringMatcher {

    /**
     * 表示进制，参考go语言中Rabin-Karp算法实现中的值
     */
    private static final int R = 16777619;
    /**
     * 哈希值可能太大，取模，随机值BigInteger.probablePrime(31, new Random())
     */
    private static final long Q = 1538824213;

    public static int indexOf(String source, String target) {
        int targetLen = target.length();
        int sourceLen = source.length();

        if (targetLen == 0) {
            return 0;
        }
        if (sourceLen < targetLen) {
            return -1;
        }
        long RM = initRM(target);
        long targetHash = hash(target, 0, targetLen - 1);
        int index = 0;
        long sourceHash = 0;
        while (index <= sourceLen - targetLen) {
            // 开始比较
            sourceHash = nextHash(source, target, index, sourceHash, RM);
            if (sourceHash == targetHash) {
                if (equals(source, index, index + targetLen - 1, target)) {
                    return index;
                }
            }
            index++;
        }
        return -1;
    }

    private static long nextHash(String source, String target, int index, long preHash, long RM) {
        int targetLen = target.length();
        if (index == 0) {
            return hash(source, 0, targetLen - 1);
        }
        long hash = preHash;
        // 去掉第一个字符的hash值
        hash = mod(hash - mod(RM * source.charAt(index - 1)));
        // 加上下一个字符的hash值
        hash = mod(mod(hash * R) + source.charAt(index + targetLen - 1));
        return hash;
    }

    private static long hash(String str, int start, int end) {
        long hash = 0;
        for (int i = start; i <= end; i++) {
            hash = mod(hash * R + str.charAt(i));
        }
        return hash;
    }

    private static long mod(long hash) {
        if (hash < 0) {
            return hash + Q;
        }
        return hash % Q;
    }

    private static long initRM(String target) {
        long RM = 1;
        for (int i = 1; i < target.length(); i++) {
            RM = (R * RM) % Q;
        }
        return RM;
    }

    private static boolean equals(String source, int start, int end, String target) {
        if (end >= source.length()) {
            return false;
        }
        for (int i = 0; i <= end - start; i++) {
            if (source.charAt(i + start) != target.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        int index = indexOf("BBC ABCDAB ABCDABCDABDC","ABCDABD");
        log.info(index + "");
    }

}
