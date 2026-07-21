package com.know.knowboot.algorithm;

import lombok.extern.slf4j.Slf4j;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.model.ExtractedResult;

import java.util.ArrayList;
import java.util.List;

/**
 * 字符串相似度比较：JavaWuzzy文档
 * FuzzyWuzzy 是一个简单易用的模糊字符串匹配工具包。github 上 5K 星，它依据 Levenshtein Distance 算法 计算两个序列之间的差异。
 * Levenshtein Distance 算法，又叫 Edit Distance 算法，是指两个字符串之间，由一个转成另一个所需的最少编辑操作次数。
 * 许可的编辑操作包括将一个字符替换成另一个字符，插入一个字符，删除一个字符。一般来说，编辑距离越小，两个串的相似度越大。
 */
@Slf4j
public class FuzzyWuzzy {

    public static void main(String[] args) {

        String a = "abcdeg";
        String b = "abgehicjdklmn";
        method1(a,b);
        method2(a,b);
        method3(a,b);
        method4(a,b);
        method5(a,b);
        method6(a,b);
        method7(a,b);

        List<String> stringList = new ArrayList<>();
        stringList.add("abgehicjdklmn");
        stringList.add("abgehicj");
        stringList.add("abgeh");
        stringList.add("abgehicjdk");
        stringList.add("abge");

        method8(a,stringList);
        method9(a,stringList,3);
        method10(a,stringList);
        method11(a,stringList,90);
        method12(a,stringList);
        method13(a,stringList,60);

    }

    /**
     * 全匹配，对顺序敏感
     * @param s1
     * @param s2
     */
    public static void method1(String s1,String s2){
        int ratio = FuzzySearch.ratio(s1, s2);
        log.info("------------------ 全匹配，对顺序敏感 ------------------ -> {}",ratio);
    }

    /**
     * 搜索匹配(部分匹配)，对顺序敏感
     * @param s1
     * @param s2
     */
    public static void method2(String s1,String s2){
        int ratio = FuzzySearch.partialRatio(s1, s2);
        log.info("------------------ 搜索匹配(部分匹配)，对顺序敏感 ------------------ -> {}",ratio);
    }

    /**
     * 首先做排序，然后全匹配，对顺序不敏感(也就是更换单词位置之后，相似度依然会很高)
     * @param s1
     * @param s2
     */
    public static void method3(String s1,String s2){
        int ratio = FuzzySearch.tokenSortRatio(s1, s2);
        log.info("------------------ 首先做排序，然后全匹配，对顺序不敏感(也就是更换单词位置之后，相似度依然会很高) ------------------ -> {}",ratio);
    }

    /**
     * 首先做排序，然后搜索匹配(部分匹配)，对顺序不敏感
     * @param s1
     * @param s2
     */
    public static void method4(String s1,String s2){
        int ratio = FuzzySearch.tokenSortPartialRatio(s1, s2);
        log.info("------------------ 首先做排序，然后搜索匹配(部分匹配)，对顺序不敏感 ------------------ -> {}",ratio);
    }

    /**
     * 首先取集合(去掉重复词)，然后全匹配，对顺序不敏感，第二个字符串包含第一个字符串就100
     * @param s1
     * @param s2
     */
    public static void method5(String s1,String s2){
        int ratio = FuzzySearch.tokenSetRatio(s1, s2);
        log.info("------------------ 首先取集合(去掉重复词)，然后全匹配，对顺序不敏感，第二个字符串包含第一个字符串就100 ------------------ -> {}",ratio);
    }

    /**
     * 首先取集合，然后搜索匹配(部分匹配)，对顺序不敏感
     * @param s1
     * @param s2
     */
    public static void method6(String s1,String s2){
        int ratio = FuzzySearch.tokenSetPartialRatio(s1, s2);
        log.info("------------------ 首先取集合，然后搜索匹配(部分匹配)，对顺序不敏感 ------------------ -> {}",ratio);
    }

    /**
     * 对顺序敏感，算法不同
     * @param s1
     * @param s2
     */
    public static void method7(String s1,String s2){
        int ratio = FuzzySearch.weightedRatio(s1, s2);
        log.info("------------------ 对顺序敏感，算法不同 ------------------ -> {}",ratio);
    }

    /**
     * 提出一个匹配度最高的
     * @param s1
     * @param list
     */
    public static void method8(String s1, List<String> list){
        ExtractedResult result = FuzzySearch.extractOne(s1, list);
        log.info("------------------ 提出一个匹配度最高的 ------------------ -> {}",result);
    }

    /**
     * 提出num个匹配度最高的
     * @param s1
     * @param list
     * @param num
     */
    public static void method9(String s1, List<String> list,int num){
        List<ExtractedResult> result = FuzzySearch.extractTop(s1, list,num);
        log.info("------------------ 提出num个匹配度最高的 ------------------ -> {}",result);
    }

    /**
     * 计算list中所有String的匹配度
     * @param s1
     * @param list
     */
    public static void method10(String s1, List<String> list){
        List<ExtractedResult> result = FuzzySearch.extractAll(s1, list);
        log.info("------------------ 计算list中所有String的匹配度 ------------------ -> {}",result);
    }

    /**
     * 计算list中所有String的匹配度，并列出score以上的
     * @param s1
     * @param list
     * @param score
     */
    public static void method11(String s1, List<String> list, int score){
        List<ExtractedResult> result = FuzzySearch.extractAll(s1, list,score);
        log.info("------------------ 计算list中所有String的匹配度，并列出score以上的 ------------------ -> {}",result);
    }

    /**
     * 计算list中所有String的匹配度，并按顺序排列
     * @param s1
     * @param list
     */
    public static void method12(String s1, List<String> list){
        List<ExtractedResult> result = FuzzySearch.extractSorted(s1, list);
        log.info("------------------ 计算list中所有String的匹配度，并按顺序排列 ------------------ -> {}",result);
    }

    /**
     * 计算list中所有String的匹配度，并列出score以上的，按顺序排列
     * @param s1
     * @param list
     * @param score
     */
    public static void method13(String s1, List<String> list, int score){
        List<ExtractedResult> result = FuzzySearch.extractSorted(s1, list,score);
        log.info("------------------ 计算list中所有String的匹配度，并列出score以上的，按顺序排列 ------------------ -> {}",result);
    }

}
