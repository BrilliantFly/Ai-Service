package com.know.knowboot.algorithm.sort;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Random;

@Slf4j
public class ArraySortTest {

    private static int[] array;
    private static int[] sortedArray;

    // 计数排序等不支持负数排序
    private static int[] positiveArray;
    private static int[] positiveArraySorted;

    public static void main(String[] args) throws Exception {

        // 生成随机数组
        array = randomArray(-1000, 1000, 100);
        // 使用 Arrays.sort() 排序作为对比
        sortedArray = Arrays.copyOf(array, array.length);
        Arrays.sort(sortedArray);

        positiveArray = randomArray(0, 1000, 100);
        positiveArraySorted = Arrays.copyOf(positiveArray, positiveArray.length);
        Arrays.sort(positiveArraySorted);

        log.info("------------- array ----------------- -> {}",array);

        IArraySort bubbleSort = new BubbleSort();
        log.info("------------- 冒泡排序 BubbleSort ----------------- -> {}",bubbleSort.sort(array));

        IArraySort bucketSort = new BucketSort();
        log.info("------------- 桶排序 BucketSort ----------------- -> {}",bucketSort.sort(array));

        IArraySort countingSort = new CountingSort();
        log.info("------------- 计数排序 countingSort ----------------- -> {}",countingSort.sort(positiveArray));

        IArraySort heapSort = new HeapSort();
        log.info("------------- 堆排序 heapSort ----------------- -> {}",heapSort.sort(array));

        IArraySort insertSort = new InsertSort();
        log.info("------------- 插入排序 insertSort ----------------- -> {}",insertSort.sort(array));

        IArraySort mergeSort = new MergeSort();
        log.info("------------- 归并排序 mergeSort ----------------- -> {}",mergeSort.sort(array));

        IArraySort quickSort = new QuickSort();
        log.info("------------- 快速排序 quickSort ----------------- -> {}",quickSort.sort(array));

        IArraySort radixSort = new RadixSort();
        log.info("------------- 基数排序 radixSort ----------------- -> {}",radixSort.sort(array));

        IArraySort selectionSort = new SelectionSort();
        log.info("------------- 选择排序 selectionSort ----------------- -> {}",selectionSort.sort(array));

        IArraySort shellSort = new ShellSort();
        log.info("------------- 希尔排序 shellSort ----------------- -> {}",shellSort.sort(array));

    }

    /**
     * 随机指定范围内N个不重复的数
     * 在初始化的无重复待选数组中随机产生一个数放入结果中，
     * 将待选数组被随机到的数，用待选数组(len-1)下标对应的数替换
     * 然后从len-2里随机产生下一个随机数，如此类推
     *
     * @param max 指定范围最大值
     * @param min 指定范围最小值
     * @param n   随机数个数
     * @return int[] 随机数结果集
     */
    public static int[] randomArray(int min, int max, int n) {
        int len = max - min + 1;

        if (max < min || n > len) {
            return null;
        }

        //初始化给定范围的待选数组
        int[] source = new int[len];
        for (int i = min; i < min + len; i++) {
            source[i - min] = i;
        }

        int[] result = new int[n];
        Random rd = new Random();
        int index = 0;
        for (int i = 0; i < result.length; i++) {
            //待选数组0到(len-2)随机一个下标
            index = Math.abs(rd.nextInt() % len--);
            //将随机到的数放入结果集
            result[i] = source[index];
            //将待选数组中被随机到的数，用待选数组(len-1)下标对应的数替换
            source[index] = source[len];
        }
        return result;
    }

}
