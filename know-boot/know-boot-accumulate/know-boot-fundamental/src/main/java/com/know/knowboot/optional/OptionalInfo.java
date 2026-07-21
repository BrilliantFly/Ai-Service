package com.know.knowboot.optional;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.function.Predicate;

@Slf4j
public class OptionalInfo {

    public static void main(String[] args) {

        createOptional();
        checkExist();
        isNotNull();
        setDefaultValue();
        filter();
        mapInfo();

    }

    /**
     * 创建 Optional 对象
     */
    public static void createOptional() {

        log.info("---------------------------- 创建 Optional 对象 ------------------------------------");

        // 1）可以使用静态方法 empty() 创建一个空的 Optional 对象
        Optional<String> empty = Optional.empty();
        System.out.println(empty); // 输出：Optional.empty

        // 2）可以使用静态方法 of() 创建一个非空的 Optional 对象
        Optional<String> opt = Optional.of("Java");
        System.out.println(opt); // 输出：Optional[Java]
        // 当然了，传递给 of() 方法的参数必须是非空的，也就是说不能为 null，否则仍然会抛出 NullPointerException。
        String name = null;
//        Optional<String> optnull = Optional.of(name);

        // 3）可以使用静态方法 ofNullable() 创建一个即可空又可非空的 Optional 对象
        String stringInfo = null;
        Optional<String> optOrNull = Optional.ofNullable(stringInfo);
        System.out.println(optOrNull); // 输出：Optional.empty
    }

    /**
     * 判断值是否存在
     */
    public static void checkExist() {

        log.info("---------------------------- 判断值是否存在 ------------------------------------");

        Optional<String> opt = Optional.of("Java");
        System.out.println(opt.isPresent()); // 输出：true

        Optional<String> optOrNull = Optional.ofNullable(null);
        System.out.println(optOrNull.isPresent()); // 输出：false

    }

    /**
     * 非空表达式
     */
    public static void isNotNull() {

        log.info("---------------------------- 非空表达式 ------------------------------------");

        Optional<String> optOrNull = Optional.ofNullable("Java");
        if (optOrNull.isPresent()) {
            System.out.println(optOrNull.get().length());
        }

        Optional<String> opt = Optional.of("Java");
        opt.ifPresent(str -> System.out.println(str.length()));

    }

    /**
     * 设置（获取）默认值
     */
    public static void setDefaultValue() {

        log.info("---------------------------- 设置（获取）默认值 ------------------------------------");

        // orElse() 方法用于返回包裹在 Optional 对象中的值，如果该值不为 null，则返回；否则返回默认值。该方法的参数类型和值得类型一致。
        String nullName = null;
        String name = Optional.ofNullable(nullName).orElse("Java");
        System.out.println(name); // 输出：Java

        // orElseGet() 方法与 orElse() 方法类似，但参数类型不同。如果 Optional 对象中的值为 null，则执行参数中的函数。
        String nullName1 = null;
        String name1 = Optional.ofNullable(nullName1).orElseGet(()->"Java");
        System.out.println(name1); // 输出：Java

        // 区别1
        String name11 = null;
        System.out.println("orElse");
        String name2 = Optional.ofNullable(name11).orElse(getDefaultValue());
        System.out.println(name2);

        System.out.println("orElseGet");
        String name3 = Optional.ofNullable(name11).orElseGet(OptionalInfo::getDefaultValue);
        System.out.println(name3);

        // 区别2   Optional 对象的值不为 null,orElse调用getDefaultValue，而orElseGet不调用，性能更佳
        String name111 = "abc";
        System.out.println("orElse");
        String name22 = Optional.ofNullable(name111).orElse(getDefaultValue());
        System.out.println(name22);

        System.out.println("orElseGet");
        String name33 = Optional.ofNullable(name111).orElseGet(OptionalInfo::getDefaultValue);
        System.out.println(name33);

    }

    public static String getDefaultValue() {
        System.out.println("getDefaultValue");
        return "Java";
    }

    /**
     * 过滤值
     */
    public static void filter() {

        log.info("---------------------------- 过滤值 ------------------------------------");
        String password = "12345";
        Optional<String> opt = Optional.ofNullable(password);
        System.out.println(opt.filter(pwd -> pwd.length() > 6).isPresent());

        Predicate<String> len6 = pwd -> pwd.length() > 6;
        Predicate<String> len10 = pwd -> pwd.length() < 10;
        password = "1234567";
        opt = Optional.ofNullable(password);
        boolean result = opt.filter(len6.and(len10)).isPresent();
        System.out.println(result);

    }

    /**
     * 转换值
     */
    public static void mapInfo() {

        log.info("---------------------------- 转换值 ------------------------------------");
        String name = "Java";
        Optional<String> nameOptional = Optional.of(name);
        Optional<Integer> intOpt = nameOptional
                .map(String::length);

        System.out.println( intOpt.orElse(0));

        String password = "password";
        Optional<String>  opt = Optional.ofNullable(password);
        Predicate<String> len6 = pwd -> pwd.length() > 6;
        Predicate<String> len10 = pwd -> pwd.length() < 10;
        Predicate<String> eq = pwd -> pwd.equals("password");
        boolean result = opt.map(String::toLowerCase).filter(len6.and(len10 ).and(eq)).isPresent();
        System.out.println(result);

    }


}
