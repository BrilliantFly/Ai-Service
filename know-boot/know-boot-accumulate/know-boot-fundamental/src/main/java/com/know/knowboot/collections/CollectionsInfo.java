package com.know.knowboot.collections;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;

@Slf4j
public class CollectionsInfo {

    public static void main(String[] args) {

        //空集合
        //当一个方法的返回值是集合时，如果返回值为空时，建议不要返回 null，而是返回一个空集合。
        //但如果每次都新建一个 ArrayList 或者 HashMap，则会造成一定的内存浪费，Collections 为 List、Set、Map 等集合类定义了相应的空集合常量。
        //空集合使用的单独定义的数据结构，内部不存储数据，所以占用内存是最小的，但也存在一些限制：不可变更，新增、修改、删除元素都会导致异常。
        // 空 List
        Collections.emptyList();
        // 空 Set
        Collections.emptySet();
        // 空 Map
        Collections.emptyMap();

        // 一个元素的集合
        // 在工作中经常会碰到这种情况：对方提供的接口需要的是一个集合，但我们只有一个元素，于是，我们只得创建一个集合，然后把这个元素加入集合再传过去。
        // 通常集合都会预留空间，以备后续还有元素可能加入，但对于只有一个元素的场景则造成了空间浪费，因为不会有后续元素加入。
        // 与空集合一样，singleton 集合也使用的单独定义的数据结构，内部只存储一个元素，内存占用少，同样不可变更，新增、修改、删除元素都会导致异常。
        // List
        List<String> list = Collections.singletonList("Hello");
        // Set
        Set<String> set = Collections.singleton("Hello");
        // Map
        Map<String, Integer> map = Collections.singletonMap("Hello", 100);

        // 不可变集合
        // Collections 工具类提供了一系列的 unmodifiableXXX 的方法，用于构建不可变集合，经过此类方法处理后的集合不可以进行新增、修改、删除元素操作，否则会抛出异常。
        // List
        List<String> list1 = Collections.unmodifiableList(list);
        // Set
        Set<String> set1 = Collections.unmodifiableSet(set);
        // Map
        Map<String, Integer> map1 = Collections.unmodifiableMap(map);

        // 线程安全集合
        // Collections 工具类提供了一系列的 synchronizedXXX 的方法，用于构建线程安全集合，经过此类方法处理后的集合可以保证线程安全。
        // 但是，这种处理方法一般来说性能不高，应该优先选择集合类型中的线程安全处理类（比如：ConcurrentHashMap）
        // List
        List<String> list2 = Collections.synchronizedList(list);
        // Set
        Set<String> set2 = Collections.synchronizedSet(set);
        // Map
        Map<String, Integer> map2 = Collections.synchronizedMap(map);

        // 批量增加
        List<Integer> list3 = new ArrayList<>();
        Collections.addAll(list3, 1, 2, 3);
        // 输出：[1,2,3]
        System.out.println(list3);

        // 填充
        // 将集合中的所有元素填充为指定元素
        List<Integer> list4 = new ArrayList<>();
        Collections.addAll(list4, 1, 2, 3);
        Collections.fill(list4, 10);
        // 输出：[10,10,10]
        System.out.println(list4);

        // 交换位置
        List<Integer> list5 = new ArrayList<>();
        Collections.addAll(list5, 1, 2, 3);
        Collections.swap(list5, 0, 1);
        // 输出：[2,1,3]
        System.out.println(list5);

        // 排序
        List<Integer> list6 = new ArrayList<>();
        Collections.addAll(list6, 3, 2, 1);
        Collections.sort(list6);
        // 输出：[1,2,3]
        System.out.println(list6);
        // 也可以使用自定义 Comparator 进行排序，比如，下例中使用字符串的长度进行排序。
        List<String> list7 = new ArrayList<>();
        Collections.addAll(list7, "Hello", "Hi", "H");
        // 输出：[H, Hi, Hello]
        Collections.sort(list7, Comparator.comparing(String::length));
        System.out.println(list7);

        // 逆序
        List<Integer> list8 = new ArrayList<>();
        Collections.addAll(list8, 3, 2, 1);
        Collections.reverse(list8);
        // 输出：[1,2,3]
        System.out.println(list8);

        // 随机排序
        // 这个方法在测试的时候非常好用。使用 shuffle() 方法进行随机排序，发现每次输出的结果都不一样。
        List<Integer> list9 = new ArrayList<>();
        Collections.addAll(list9, 3, 2, 1);
        Collections.shuffle(list9);
        System.out.println(list9);

        // 排序
        List<TestInfo> tests = new ArrayList<>();
        TestInfo test = new TestInfo();
        test.setName("1");
        test.setIndex(1);
        tests.add(test);

        TestInfo test1 = new TestInfo();
        test1.setName("2");
        test1.setIndex(2);
        tests.add(test1);

        System.out.println(tests.toString());

        Collections.sort(tests, new Comparator<TestInfo>() {
            @Override
            public int compare(TestInfo o1, TestInfo o2) {

                // 正整数: 当前对象的值 > 比较对象的值 ， 位置排在后
                // 零：当前对象的值 = 比较对象的值 ， 位置不变
                // 负整数，当前对象的值 < 比较对象的值 ， 位置排在前
                // 升序 o1.getIndex()  - o2.getIndex()
                // 降序 o2.getIndex()  - o1.getIndex()

                if (o1.getIndex() > o2.getIndex()){
                    return 1;
                }else if (o1.getIndex() == o2.getIndex()){
                    return 0;
                }else {
                    return -1;
                }

            }
        });

        System.out.println(tests.toString());

        List<String> list111 = new ArrayList<>();
        // 判断集合list是否为空,同时判断list为null，为空集合
        if (CollectionUtils.isEmpty(list111)) {
            // CollectionUtils -> org.apache.commons.collections.CollectionUtils;
            log.info("list111为空");
        }

        List<String> list1111 = null;
        // 判断集合list是否为空,同时判断list为null，为空集合
        if (CollectionUtils.isEmpty(list1111)) {
            // CollectionUtils -> org.apache.commons.collections.CollectionUtils;
            log.info("list1111为空");
        }

        // 先判断是否为null 在判断isEmpty  不然空指针
        if (list1111 == null || list1111.isEmpty()){
            log.info("list1111为空");
        }

        // 判断集合list是否为空,同时判断list不为null，不为空集合
        if (CollectionUtils.isNotEmpty(list111)) {
            // CollectionUtils -> org.apache.commons.collections.CollectionUtils;
            log.info("list111不为空");
        }

    }

}

@Data
class TestInfo{

    private String name;
    private Integer index;

}
