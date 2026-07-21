package com.know.knowboot.stream;

import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamInfo {

    public static void main(String[] args) {

        //多个参数
        indeterminacyParam("1", "2", "3", "4", "5", "6");
        //lambda表达式
        lambdaExpression();
        //forEach
        forEach();
        //list转Map
        listToMap(getDictList()).forEach((k, v) -> System.out.println(k.toString() + " - " + v.toString()));
        //抽取某一项成为list集合
        getNameList(getDictList()).forEach(t -> {
            System.out.println(t);
        });
        //list排序
        sortedList(getDictList()).forEach(t -> System.out.println(t.getId()));
        //map转list
        Map<Integer, String> map12 = new HashMap<>();
        map12.put(1, "map1");
        map12.put(2, "map2");
        mapToList(map12).forEach(t -> System.out.println(t.getName()));
        //用逗号隔开的字符串
        System.out.println(stringList(getDictList()));
        //一个对象转换成另一个对象
        objectToOther(getDictList()).forEach(t -> System.out.println(t.getName()));
        //两个list的交集
        twoListMix(getDictList(), getDictList2()).forEach(t -> System.out.println(t.getName()));
        //两个list的差集
        twoListDifferenceSet(getDictList(), getDictList2()).forEach(t -> System.out.println(t.getName()));
        //List 转 List<Map<String,Object>>
        listToListMap(getDictList()).forEach(t -> System.out.println(t.keySet()));


        // 遍历/匹配（foreach/find/match）
        List<Integer> list = Arrays.asList(7, 6, 9, 3, 8, 2, 1);
        // 遍历输出符合条件的元素
        list.stream().filter(x -> x > 6).forEach(System.out::println);
        // Optional类是一个可以为null的容器对象。如果值存在则isPresent()方法会返回true，调用get()方法会返回该对象。
        // 匹配第一个
        Optional<Integer> findFirst = list.stream().filter(x -> x > 6).findFirst();
        // 匹配任意（适用于并行流）
        Optional<Integer> findAny = list.parallelStream().filter(x -> x > 6).findAny();
        // 是否包含符合特定条件的元素
        boolean anyMatch = list.stream().anyMatch(x -> x < 6);
        System.out.println("匹配第一个值是否存在：" + findFirst.isPresent());
        System.out.println("匹配第一个值：" + findFirst.get());
        System.out.println("匹配任意一个值：" + findAny.get());
        System.out.println("是否存在大于6的值：" + anyMatch);

        // 筛选（filter）
        List<Integer> list1 = Arrays.asList(6, 7, 3, 8, 1, 2, 9);
        Stream<Integer> stream = list1.stream();
        stream.filter(x -> x > 7).forEach(System.out::println);

        // 筛选员工中工资高于8000的人，并形成新的集合。 形成新集合依赖collect（收集）
        List<Person> personList = new ArrayList<Person>();
        personList.add(new Person("Tom", 8900, 23, "male", "New York"));
        personList.add(new Person("Jack", 7000, 25, "male", "Washington"));
        personList.add(new Person("Lily", 7800, 21, "female", "Washington"));
        personList.add(new Person("Anni", 8200, 24, "female", "New York"));
        personList.add(new Person("Owen", 9500, 25, "male", "New York"));
        personList.add(new Person("Alisa", 7900, 26, "female", "New York"));
        List<String> fiterList = personList.stream().filter(x -> x.getSalary() > 8000).map(Person::getName)
                .collect(Collectors.toList());
        System.out.print("高于8000的员工姓名：" + fiterList);

        // 聚合（max/min/count)
        List<String> list2 = Arrays.asList("adnm", "admmt", "pot", "xbangd", "weoujgsd");

        // 获取String集合中最长的元素
        Optional<String> max = list2.stream().max(Comparator.comparing(String::length));
        System.out.println("最长的字符串：" + max.get());

        // 获取Integer集合中的最大值
        List<Integer> list3 = Arrays.asList(7, 6, 9, 4, 11, 6);
        // 自然排序
        Optional<Integer> max2 = list3.stream().max(Integer::compareTo);
        // 自定义排序
        Optional<Integer> max3 = list3.stream().max(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });
        System.out.println("自然排序的最大值：" + max2.get());
        System.out.println("自定义排序的最大值：" + max3.get());

        // 获取员工工资最高的人
        List<Person> personList1 = new ArrayList<Person>();
        personList1.add(new Person("Tom", 8900, 23, "male", "New York"));
        personList1.add(new Person("Jack", 7000, 25, "male", "Washington"));
        personList1.add(new Person("Lily", 7800, 21, "female", "Washington"));
        personList1.add(new Person("Anni", 8200, 24, "female", "New York"));
        personList1.add(new Person("Owen", 9500, 25, "male", "New York"));
        personList1.add(new Person("Alisa", 7900, 26, "female", "New York"));
        Optional<Person> max1 = personList1.stream().max(Comparator.comparingInt(Person::getSalary));
        System.out.println("员工工资最大值：" + max1.get().getSalary());
        // 计算Integer集合中大于6的元素的个数
        List<Integer> list4 = Arrays.asList(7, 6, 4, 8, 2, 11, 9);
        long count = list4.stream().filter(x -> x > 6).count();
        System.out.println("list中大于6的元素个数：" + count);

        // 映射(map/flatMap)
        // 映射，可以将一个流的元素按照一定的映射规则映射到另一个流中。分为map和flatMap
        // map：接收一个函数作为参数，该函数会被应用到每个元素上，并将其映射成一个新的元素
        // flatMap：接收一个函数作为参数，将流中的每个值都换成另一个流，然后把所有流连接成一个流

        // 英文字符串数组的元素全部改为大写。整数数组每个元素+3
        String[] strArr = { "abcd", "bcdd", "defde", "fTr" };
        List<String> strList = Arrays.stream(strArr).map(String::toUpperCase).collect(Collectors.toList());
        List<Integer> intList = Arrays.asList(1, 3, 5, 7, 9, 11);
        List<Integer> intListNew = intList.stream().map(x -> x + 3).collect(Collectors.toList());
        System.out.println("每个元素大写：" + strList);
        System.out.println("每个元素+3：" + intListNew);

        // 将员工的薪资全部增加1000
        List<Person> personList2 = new ArrayList<Person>();
        personList2.add(new Person("Tom", 8900, 23, "male", "New York"));
        personList2.add(new Person("Jack", 7000, 25, "male", "Washington"));
        personList2.add(new Person("Lily", 7800, 21, "female", "Washington"));
        personList2.add(new Person("Anni", 8200, 24, "female", "New York"));
        personList2.add(new Person("Owen", 9500, 25, "male", "New York"));
        personList2.add(new Person("Alisa", 7900, 26, "female", "New York"));
        // 不改变原来员工集合的方式
        List<Person> personListNew = personList2.stream().map(person -> {
            Person personNew = new Person(person.getName(), 0, 0, null, null);
            personNew.setSalary(person.getSalary() + 10000);
            return personNew;
        }).collect(Collectors.toList());
        System.out.println("一次改动前：" + personList2.get(0).getName() + "-->" + personList2.get(0).getSalary());
        System.out.println("一次改动后：" + personListNew.get(0).getName() + "-->" + personListNew.get(0).getSalary());
        // 改变原来员工集合的方式
        List<Person> personListNew2 = personList2.stream().map(person -> {
            person.setSalary(person.getSalary() + 10000);
            return person;
        }).collect(Collectors.toList());
        System.out.println("二次改动前：" + personList2.get(0).getName() + "-->" + personListNew.get(0).getSalary());
        System.out.println("二次改动后：" + personListNew2.get(0).getName() + "-->" + personListNew.get(0).getSalary());

        // 将两个字符数组合并成一个新的字符数组
        List<String> list5 = Arrays.asList("m,k,l,a", "1,3,5,7");
        List<String> listNew = list5.stream().flatMap(s -> {
            // 将每个元素转换成一个stream
            String[] split = s.split(",");
            Stream<String> s2 = Arrays.stream(split);
            return s2;
        }).collect(Collectors.toList());
        System.out.println("处理前的集合：" + list);
        System.out.println("处理后的集合：" + listNew);

        // 归约(reduce)
        // 归约，也称缩减，顾名思义，是把一个流缩减成一个值，能实现对集合求和、求乘积和求最值操作
        // 求Integer集合的元素之和、乘积和最大值
        List<Integer> list6 = Arrays.asList(1, 3, 2, 8, 11, 4);
        // 求和方式1
        Optional<Integer> sum = list6.stream().reduce((x, y) -> x + y);
        // 求和方式2
        Optional<Integer> sum2 = list6.stream().reduce(Integer::sum);
        // 求和方式3
        Integer sum3 = list6.stream().reduce(0, Integer::sum);
        // 求乘积
        Optional<Integer> product = list6.stream().reduce((x, y) -> x * y);
        // 求最大值方式1
        Optional<Integer> max4 = list6.stream().reduce((x, y) -> x > y ? x : y);
        // 求最大值写法2
        Integer max5 = list6.stream().reduce(1, Integer::max);
        System.out.println("list求和：" + sum.get() + "," + sum2.get() + "," + sum3);
        System.out.println("list求积：" + product.get());
        System.out.println("list求和：" + max4.get() + "," + max5);

        // 求所有员工的工资之和和最高工资
        List<Person> personList5 = new ArrayList<Person>();
        personList5.add(new Person("Tom", 8900, 23, "male", "New York"));
        personList5.add(new Person("Jack", 7000, 25, "male", "Washington"));
        personList5.add(new Person("Lily", 7800, 21, "female", "Washington"));
        personList5.add(new Person("Anni", 8200, 24, "female", "New York"));
        personList5.add(new Person("Owen", 9500, 25, "male", "New York"));
        personList5.add(new Person("Alisa", 7900, 26, "female", "New York"));

        // 求工资之和方式1：
        Optional<Integer> sumSalary = personList5.stream().map(Person::getSalary).reduce(Integer::sum);
        // 求工资之和方式2：
        Integer sumSalary2 = personList5.stream().reduce(0, (sum1, p) -> sum1 += p.getSalary(), (sum1, sum22) -> sum1 + sum22);
        // 求工资之和方式3：
        Integer sumSalary3 = personList5.stream().reduce(0, (sum1, p) -> sum1 += p.getSalary(), Integer::sum);

        // 求最高工资方式1：
        Integer maxSalary = personList5.stream().reduce(0, (max11, p) -> max11 > p.getSalary() ? max11 : p.getSalary(),
                Integer::max);
        // 求最高工资方式2：
        Integer maxSalary2 = personList5.stream().reduce(0, (max11, p) -> max11 > p.getSalary() ? max11 : p.getSalary(),
                (max11, max22) -> max11 > max22 ? max11 : max22);

        System.out.println("工资之和：" + sumSalary.get() + "," + sumSalary2 + "," + sumSalary3);
        System.out.println("最高工资：" + maxSalary + "," + maxSalary2);

        // 收集(collect)
        // collect，收集，可以说是内容最繁多、功能最丰富的部分了
        // 从字面上去理解，就是把一个流收集起来，最终可以是收集成一个值也可以收集成一个新的集合
        // toList、toSet和toMap
        List<Integer> list11 = Arrays.asList(1, 6, 3, 4, 6, 7, 9, 6, 20);
        List<Integer> listNew1 = list11.stream().filter(x -> x % 2 == 0).collect(Collectors.toList());
        Set<Integer> set = list11.stream().filter(x -> x % 2 == 0).collect(Collectors.toSet());

        List<Person> personList11 = new ArrayList<Person>();
        personList11.add(new Person("Tom", 8900, 23, "male", "New York"));
        personList11.add(new Person("Jack", 7000, 25, "male", "Washington"));
        personList11.add(new Person("Lily", 7800, 21, "female", "Washington"));
        personList11.add(new Person("Anni", 8200, 24, "female", "New York"));
        Map<?, Person> map = personList11.stream().filter(p -> p.getSalary() > 8000)
                .collect(Collectors.toMap(Person::getName, p -> p));
        System.out.println("toList:" + listNew1);
        System.out.println("toSet:" + set);
        System.out.println("toMap:" + map);

        // 统计(count/averaging)
        // 计数：count
        // 平均值：averagingInt、averagingLong、averagingDouble
        // 最值：maxBy、minBy
        // 求和：summingInt、summingLong、summingDouble
        // 统计以上所有：summarizingInt、summarizingLong、summarizingDouble
        // 统计员工人数、平均工资、工资总额、最高工资
        List<Person> personList12 = new ArrayList<Person>();
        personList12.add(new Person("Tom", 8900, 23, "male", "New York"));
        personList12.add(new Person("Jack", 7000, 25, "male", "Washington"));
        personList12.add(new Person("Lily", 7800, 21, "female", "Washington"));

        // 求总数
        Long count1 = personList12.stream().collect(Collectors.counting());
        // 求平均工资
        Double average = personList12.stream().collect(Collectors.averagingDouble(Person::getSalary));
        // 求最高工资
        Optional<Integer> max11 = personList12.stream().map(Person::getSalary).collect(Collectors.maxBy(Integer::compare));
        // 求工资之和
        Integer sum1 = personList12.stream().collect(Collectors.summingInt(Person::getSalary));
        // 一次性统计所有信息
        DoubleSummaryStatistics collect = personList12.stream().collect(Collectors.summarizingDouble(Person::getSalary));

        System.out.println("员工总数：" + count1);
        System.out.println("员工平均工资：" + average);
        System.out.println("员工最高工资：" + max11);
        System.out.println("员工工资总和：" + sum1);
        System.out.println("员工工资所有统计：" + collect);

        // 分组(partitioningBy/groupingBy)
        // 分区：将stream按条件分为两个Map，比如员工按薪资是否高于8000分为两部分
        // 分组：将集合分为多个Map，比如员工按性别分组。有单级分组和多级分组
        // 将员工按薪资是否高于8000分为两部分；将员工按性别和地区分组
        List<Person> personList121 = new ArrayList<Person>();
        personList121.add(new Person("Tom", 8900, 10,"male","New York"));
        personList121.add(new Person("Jack", 7000, 10,"male", "Washington"));
        personList121.add(new Person("Lily", 7800, 10,"female", "Washington"));
        personList121.add(new Person("Anni", 8200, 10,"female", "New York"));
        personList121.add(new Person("Owen", 9500, 10,"male", "New York"));
        personList121.add(new Person("Alisa", 7900, 10,"female", "New York"));

        // 将员工按薪资是否高于8000分组
        Map<Boolean, List<Person>> part = personList121.stream().collect(Collectors.partitioningBy(x -> x.getSalary() > 8000));
        // 将员工按性别分组
        Map<String, List<Person>> group = personList121.stream().collect(Collectors.groupingBy(Person::getSex));
        // 将员工先按性别分组，再按地区分组
        Map<String, Map<String, List<Person>>> group2 = personList121.stream().collect(Collectors.groupingBy(Person::getSex, Collectors.groupingBy(Person::getArea)));
        System.out.println("员工按薪资是否大于8000分组情况：" + part);
        System.out.println("员工按性别分组情况：" + group);
        System.out.println("员工按性别、地区：" + group2);

        // 接合(joining)
        // joining可以将stream中的元素用特定的连接符（没有的话，则直接连接）连接成一个字符串
        List<Person> personList122 = new ArrayList<Person>();
        personList122.add(new Person("Tom", 8900, 23, "male", "New York"));
        personList122.add(new Person("Jack", 7000, 25, "male", "Washington"));
        personList122.add(new Person("Lily", 7800, 21, "female", "Washington"));

        String names = personList122.stream().map(p -> p.getName()).collect(Collectors.joining(","));
        System.out.println("所有员工的姓名：" + names);
        List<String> list12 = Arrays.asList("A", "B", "C");
        String string = list12.stream().collect(Collectors.joining("-"));
        System.out.println("拼接后的字符串：" + string);

        // 归约(reducing)
        // Collectors类提供的reducing方法，相比于stream本身的reduce方法，增加了对自定义归约的支持
        List<Person> personList1212 = new ArrayList<Person>();
        personList1212.add(new Person("Tom", 8900, 23, "male", "New York"));
        personList1212.add(new Person("Jack", 7000, 25, "male", "Washington"));
        personList1212.add(new Person("Lily", 7800, 21, "female", "Washington"));
        // 每个员工减去起征点后的薪资之和（这个例子并不严谨，但一时没想到好的例子）
        Integer sum11 = personList1212.stream().collect(Collectors.reducing(0, Person::getSalary, (i, j) -> (i + j - 5000)));
        System.out.println("员工扣税薪资总和：" + sum11);
        // stream的reduce
        Optional<Integer> sum22 = personList.stream().map(Person::getSalary).reduce(Integer::sum);
        System.out.println("员工薪资总和：" + sum22.get());

        // 排序(sorted)
        // sorted，中间操作。有两种排序
        // sorted()：自然排序，流中元素需实现Comparable接口
        // sorted(Comparator com)：Comparator排序器自定义排序
        // 将员工按工资由高到低（工资一样则按年龄由大到小）排序
        List<Person> personList1221 = new ArrayList<Person>();

        personList1221.add(new Person("Sherry", 9000, 24, "female", "New York"));
        personList1221.add(new Person("Tom", 8900, 22, "male", "Washington"));
        personList1221.add(new Person("Jack", 9000, 25, "male", "Washington"));
        personList1221.add(new Person("Lily", 8800, 26, "male", "New York"));
        personList1221.add(new Person("Alisa", 9000, 26, "female", "New York"));

        // 按工资升序排序（自然排序）
        List<String> newList = personList1221.stream().sorted(Comparator.comparing(Person::getSalary)).map(Person::getName)
                .collect(Collectors.toList());
        // 按工资倒序排序
        List<String> newList2 = personList1221.stream().sorted(Comparator.comparing(Person::getSalary).reversed())
                .map(Person::getName).collect(Collectors.toList());
        // 先按工资再按年龄升序排序
        List<String> newList3 = personList1221.stream()
                .sorted(Comparator.comparing(Person::getSalary).thenComparing(Person::getAge)).map(Person::getName)
                .collect(Collectors.toList());
        // 先按工资再按年龄自定义排序（降序）
        List<String> newList4 = personList1221.stream().sorted((p1, p2) -> {
            if (p1.getSalary() == p2.getSalary()) {
                return p2.getAge() - p1.getAge();
            } else {
                return p2.getSalary() - p1.getSalary();
            }
        }).map(Person::getName).collect(Collectors.toList());

        System.out.println("按工资升序排序：" + newList);
        System.out.println("按工资降序排序：" + newList2);
        System.out.println("先按工资再按年龄升序排序：" + newList3);
        System.out.println("先按工资再按年龄自定义降序排序：" + newList4);

        // 提取/组合
        // 流也可以进行合并、去重、限制、跳过等操作
        String[] arr1 = { "a", "b", "c", "d" };
        String[] arr2 = { "d", "e", "f", "g" };

        Stream<String> stream1 = Stream.of(arr1);
        Stream<String> stream2 = Stream.of(arr2);
        // concat:合并两个流 distinct：去重
        List<String> newList1 = Stream.concat(stream1, stream2).distinct().collect(Collectors.toList());
        // limit：限制从流中获得前n个数据
        List<Integer> collect1 = Stream.iterate(1, x -> x + 2).limit(10).collect(Collectors.toList());
        // skip：跳过前n个数据
        List<Integer> collect2 = Stream.iterate(1, x -> x + 2).skip(1).limit(5).collect(Collectors.toList());

        System.out.println("流合并：" + newList1);
        System.out.println("limit：" + collect1);
        System.out.println("skip：" + collect2);

        //去重
//        List<Order> orderList = new ArrayList<>();
//
//        Order order = new Order();
//        order.setId("111111");
//        order.setMemberId("111111");
//        orderList.add(order);
//
//        Order order1 = new Order();
//        order1.setId("111111");
//        order1.setMemberId("111111");
//        orderList.add(order1);
//
//        Order order2 = new Order();
//        order2.setId("111111");
//        order2.setMemberId("111112");
//        orderList.add(order2);
//
//        // 单个属性去重
//        List<Order> orderList1 = orderList.stream().collect(
//                Collectors.collectingAndThen(
//                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Order::getMemberId))),
//                        ArrayList::new)
//        );
//
//        // 多个属性去重
//        List<Order> orderList2 = orderList.stream().collect(
//                Collectors.collectingAndThen(
//                        Collectors.toCollection(
//                                () -> new TreeSet<>(Comparator.comparing(
//                                        o -> o.getMemberId() + "-" + o.getId()
//                                ))),
//                        ArrayList::new)
//        );
//
//        System.out.println(orderList1.size());
//        System.out.println(orderList2.size());


    }

    /**
     * String… excludeProperty表示不定参数，
     * 调用这个方法的时候这里可以传入多个参数。
     *
     * @param param
     */
    public static void indeterminacyParam(String... param) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>String… excludeProperty表示不定参数>>>>>>>>>>>>>>>>>>>");
        System.out.println(param.length);
        if (param != null && param.length > 0) {
            for (int i = 0; i < param.length; i++) {
                System.out.println(param[i]);
            }
        }
    }

    /**
     * lambda 表达式的语法格式如下：
     * (parameters) -> expression 或 (parameters) ->{ statements; }
     * <p>
     * 一般用户stream
     * <p>
     * // 1. 不需要参数,返回值为 5
     * () -> 5
     * // 2. 接收一个参数(数字类型),返回其2倍的值
     * x -> 2 * x
     * // 3. 接受2个参数(数字),并返回他们的差值
     * (x, y) -> x – y
     * // 4. 接收2个int型整数,返回他们的和
     * (int x, int y) -> x + y
     * // 5. 接受一个 string 对象,并在控制台打印,不返回任何值(看起来像是返回void)
     * (String s) -> System.out.print(s)
     */
    public static void lambdaExpression() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>lambda 表达式的语法格式>>>>>>>>>>>>>>>>>>>");
        // 类型声明
        MathOperation addition = (int a, int b) -> a + b;

        // 不用类型声明
        MathOperation subtraction = (a, b) -> a - b;

        // 大括号中的返回语句
        MathOperation multiplication = (int a, int b) -> {
            return a * b;
        };

        // 没有大括号及返回语句
        MathOperation division = (int a, int b) -> a / b;

        System.out.println("10 + 5 = " + operate(10, 5, addition));
        System.out.println("10 - 5 = " + operate(10, 5, subtraction));
        System.out.println("10 x 5 = " + operate(10, 5, multiplication));
        System.out.println("10 / 5 = " + operate(10, 5, division));
    }

    interface MathOperation {
        int operation(int a, int b);
    }

    static int operate(int a, int b, MathOperation mathOperation) {
        return mathOperation.operation(a, b);
    }

    /**
     * foreach循环list
     */
    public static void forEach() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>foreach循环list>>>>>>>>>>>>>>>>>>>");
        getDictList().forEach(t -> System.out.println(t.getName()));
    }

    /**
     * list转Map
     * <p>
     * filter() 方法作用是过滤掉名字为空的对象，当对象的名字为null时，会出现NPE空指针异常
     * (k1,k2)->k2 意思是遇到相同的key时取第二个值
     * (k1,k2)->k1 意思是遇到相同的key时取第一个值
     * <p>
     * 抽取对象的id作为key，name作为value转化为map集合
     *
     * @param dictList
     * @return
     */
    private static HashMap<Integer, String> listToMap(List<Dict> dictList) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>list转Map>>>>>>>>>>>>>>>>>>>");
//        return (HashMap<Integer, String>)dictList.stream()
//                .filter(t -> t.getName()!=null)
//                .collect(Collectors.toMap(Dict::getId,Dict::getName,(k1, k2)->k2));

        return (HashMap<Integer, String>) dictList.stream()
                .filter(t -> t.getName() != null)
                .collect(Collectors.toMap(t -> t.getId(), t -> t.getName(), (k1, k2) -> k2));
    }

    /**
     * map转list
     *
     * @param hashMap
     * @return
     */
    private static List<Dict> mapToList(Map<Integer, String> hashMap) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>map转list>>>>>>>>>>>>>>>>>>>");

//        return hashMap.entrySet().stream().sorted(Comparator.comparing(e -> e.getKey()))
//                .map(e -> new Dict(e.getKey(), e.getValue(),"")).collect(Collectors.toList());

//        return hashMap.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getValue))
//                .map(e -> new Dict(e.getKey(), e.getValue(),"")).collect(Collectors.toList());

        return hashMap.entrySet().stream().sorted(Map.Entry.comparingByKey())
                .map(e -> new Dict(e.getKey(), e.getValue(), "")).collect(Collectors.toList());
    }

    /**
     * 抽取某一项成为list集合
     *
     * @param dictList
     * @return
     */
    private static List<String> getNameList(List<Dict> dictList) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>抽取某一项成为list集合>>>>>>>>>>>>>>>>>>>");
        return dictList.stream().map(Dict::getName).collect(Collectors.toList());
    }

    /**
     * list排序
     * 降序排序（使用reversed()方法）
     *
     * @param dictList
     */
    public static List<Dict> sortedList(List<Dict> dictList) {

        System.out.println(">>>>>>>>>>>>>>>>>>>>list排序>>>>>>>>>>>>>>>>>>>");
        return dictList.stream().sorted(Comparator.comparing(Dict::getId).reversed()).collect(Collectors.toList());
        // 先根据id排序，在根据name排序
        // return dictList.stream().sorted(Comparator.comparing(Dict::getId).reversed().thenComparing(Dict::getName)).collect(Collectors.toList());

    }

    /**
     * 用逗号隔开的字符串
     *
     * @param dictList
     */
    public static String stringList(List<Dict> dictList) {

        System.out.println(">>>>>>>>>>>>>>>>>>>>用逗号隔开的字符串>>>>>>>>>>>>>>>>>>>");
        return dictList.stream().map(Dict::getName).collect(Collectors.joining(","));

    }

    /**
     * 一个对象转换成另一个对象
     *
     * @param dictList
     */
    public static List<DictOther> objectToOther(List<Dict> dictList) {

        System.out.println(">>>>>>>>>>>>>>>>>>>>一个对象转换成另一个对象>>>>>>>>>>>>>>>>>>>");
        return dictList.stream().map(dict -> new DictOther(dict.getId(), dict.getName())).collect(Collectors.toList());

    }

    /**
     * 两个list的交集
     *
     * @param dictList1
     * @param dictList2
     * @return
     */
    public static List<Dict> twoListMix(List<Dict> dictList1, List<Dict> dictList2) {

        System.out.println(">>>>>>>>>>>>>>>>>>>>两个list的交集>>>>>>>>>>>>>>>>>>>");
        return dictList1.stream().filter(dictList2::contains).collect(Collectors.toList());

    }

    /**
     * 两个list的差集
     *
     * @param dictList1
     * @param dictList2
     * @return
     */
    public static List<Dict> twoListDifferenceSet(List<Dict> dictList1, List<Dict> dictList2) {

        System.out.println(">>>>>>>>>>>>>>>>>>>>两个list的差集(dictList1 - dictList2)>>>>>>>>>>>>>>>>>>>");
        return dictList1.stream().filter(item -> !dictList2.contains(item)).collect(Collectors.toList());

    }

    /**
     * List 转 List<Map<String,Object>>
     *
     * @param dictList
     * @return
     */
    public static List<Map<String, Object>> listToListMap(List<Dict> dictList) {

        System.out.println(">>>>>>>>>>>>>>>>>>>>List 转 List<Map<String,Object>>>>>>>>>>>>>>>>>>>>>");
//        return dictList.stream().map(t -> {
//            Map<String,Object> objectMap = new HashedMap();
//            objectMap.put("id",t.getId());
//            objectMap.put("name",t.getName());
//            return objectMap;
//        }).collect(Collectors.toList());

        return dictList.stream().collect(ArrayList::new, (list, t) -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", t.getId());
            map.put("name", t.getName());
            list.add(map);
        }, List::addAll);

    }


    public static List<Dict> getDictList() {
        Dict dict1 = new Dict(1, "dict1", "test");
        Dict dict2 = new Dict(2, "dict2", "test");
        Dict dict3 = new Dict(3, "dict3", "test");
        List<Dict> dicts = new ArrayList<>();
        dicts.add(dict1);
        dicts.add(dict2);
        dicts.add(dict3);
        return dicts;
    }

    public static List<Dict> getDictList2() {
        Dict dict1 = new Dict(1, "dict1", "test");
        Dict dict4 = new Dict(4, "dict4", "test");
        List<Dict> dicts = new ArrayList<>();
        dicts.add(dict1);
        dicts.add(dict4);
        return dicts;
    }
}

@Data
class Person {

    private String name; // 姓名
    private int salary; // 薪资
    private int age; // 年龄
    private String sex; //性别
    private String area; // 地区

    // 构造方法
    public Person(String name, int salary, int age,String sex,String area) {
        this.name = name;
        this.salary = salary;
        this.age = age;
        this.sex = sex;
        this.area = area;
    }

}

class Dict {

    /**
     * id
     */
    private int id;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String desc;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Dict(int id, String name, String desc) {
        this.id = id;
        this.name = name;
        this.desc = desc;
    }

}

class DictOther {

    /**
     * id
     */
    private int id;

    /**
     * 名称
     */
    private String name;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public DictOther(int id, String name) {
        this.id = id;
        this.name = name;
    }

}
