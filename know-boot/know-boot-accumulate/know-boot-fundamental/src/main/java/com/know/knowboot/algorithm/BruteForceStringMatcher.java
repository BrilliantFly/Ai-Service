package com.know.knowboot.algorithm;

import lombok.extern.slf4j.Slf4j;

/**
 * 朴素算法也称暴力解法, 比如: 主串"abbdhhheee", 子串为"hhh"
 */
@Slf4j
public class BruteForceStringMatcher {

    public static int indexOf(String source, String target) {
        if (target.length() == 0) {
            return 0;
        }
        if (source.length() < target.length()) {
            return -1;
        }
        char[] sourceArr = source.toCharArray();
        char[] targetArr = target.toCharArray();
        for (int i = 0; i < source.length(); i++) {
            if (equals(sourceArr, i, i + targetArr.length - 1, targetArr)) {
                return i;
            }
        }
        return -1;
    }

    private static boolean equals(char[] source, int start, int end, char[] target) {
        if (end >= source.length) {
            return false;
        }
        for (int i = 0; i <= end - start; i++) {
            if (source[i + start] != target[i]) {
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
