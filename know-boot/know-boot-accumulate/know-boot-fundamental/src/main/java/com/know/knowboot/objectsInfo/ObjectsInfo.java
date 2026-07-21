package com.know.knowboot.objectsInfo;

import java.util.Objects;

public class ObjectsInfo {

    public static void main(String[] args) {

        System.out.println("-------------------------- Objects 的 isNull 方法用于判断对象是否为空 -------------------------------------");
        // Objects 的 isNull 方法用于判断对象是否为空，而 nonNull 方法判断对象是否不为空
        Integer integer = new Integer(1);
        if (Objects.isNull(integer)) {
            System.out.println("对象为空");
        }
        if (Objects.nonNull(integer)) {
            System.out.println("对象不为空");
        }

        System.out.println("-------------------------- 对象为空时，抛出空指针异常，可以使用 Objects 的 requireNonNull 方法 -------------------------------------");
        // 对象为空时，抛出空指针异常，可以使用 Objects 的 requireNonNull 方法
        Integer integer1 = null;
//        Objects.requireNonNull(integer1);

        System.out.println("-------------------------- 判断两个对象是否相等 -------------------------------------");
        // 判断两个对象是否相等
        Integer integer3 = new Integer(1);
        Integer integer4 = new Integer(1);
        System.out.println(Objects.equals(integer3, integer4));

        System.out.println("-------------------------- 获取对象的hashcode -------------------------------------");
        // 获取对象的hashcode
        String str = new String("沉默王二");
        System.out.println(Objects.hashCode(str));

        System.out.println("-------------------------- 比较两个数组 -------------------------------------");
        // 比较两个数组
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 3};
        int[] array3 = {1, 2, 4};
        System.out.println(Objects.deepEquals(array1, array2)); // 输出：true（因为 array1 和 array2 的内容相同）
        System.out.println(Objects.deepEquals(array1, array3)); // 输出：false（因为 array1 和 array3 的内容不同）

        // 对于非数组对象，deepEquals() 的行为与 equals() 相同
        String string1 = "hello";
        String string2 = "hello";
        String string3 = "world";
        System.out.println(Objects.deepEquals(string1, string2)); // 输出：true（因为 string1 和 string2 相同）
        System.out.println(Objects.deepEquals(string1, string3)); // 输出：false（因为 string1 和 string3 不同）

        String[][] nestedArray1 = {{"A", "B"}, {"C", "D"}};
        String[][] nestedArray2 = {{"A", "B"}, {"C", "D"}};
        String[][] nestedArray3 = {{"A", "B"}, {"C", "E"}};
        System.out.println(Objects.deepEquals(nestedArray1, nestedArray2)); // 输出：true (因为嵌套数组元素相同)
        System.out.println(Objects.deepEquals(nestedArray1, nestedArray3)); // 输出：false (因为嵌套数组元素不同)


    }

}
