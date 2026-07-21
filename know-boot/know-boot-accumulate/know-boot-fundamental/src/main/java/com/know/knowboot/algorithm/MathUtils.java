package com.know.knowboot.algorithm;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.distribution.NormalDistribution;

import java.util.Random;

@Slf4j
public class MathUtils {


    public static void main(String[] args) {

        log.info("------------- 构建正态分布 -------------- -> {}",getNormalDistribution(5d,6d));
        log.info("-------------  根据x获取函数值增长速度即正态分布曲线值 -------------- -> {}",getSpeed(getNormalDistribution(18d,7d),18d));
        log.info("-------------  根据x获取累计面积值即正态分布值 -------------- -> {}",getArea(getNormalDistribution(18d,7d),18d));
        log.info("-------------  获取符合正态分布的随机数 -------------- -> {}",getRandom(7d));
        log.info("-------------  期望值y，方差z的正态分布随机数（取值与真正的正态分布有极细小的差别，但大致符合） -------------- -> {}",getRandomInfo(7d,5d));

    }

    /**
     * 构建正态分布
     * @param y  期望值，标准正态分布y=0
     * @param z  方差，标准正态分布z=1
     */
    public static NormalDistribution getNormalDistribution(Double y, Double z){
        NormalDistribution normalDistributioin = new NormalDistribution(y,z);
        return normalDistributioin;
    }

    /**
     * 根据x获取函数值增长速度即正态分布曲线值
     * @param x
     * @return
     */
    public static Double getSpeed(NormalDistribution normalDistribution,Double x){
        Double speed = normalDistribution.density(x);
        return speed;
    }

    /**
     * 根据x获取累计面积值即正态分布值
     * @param x
     * @return
     */
    public static Double getArea(NormalDistribution normalDistribution,Double x){
        Double area = normalDistribution.cumulativeProbability(x);
        return area;
    }

    /**
     * 获取符合正态分布的随机数
     * @param x
     * @return
     */
    public static Double getRandom(Double x){
        Random random = new Random();
        // 标准正态分布随机数
        Double getNum = random.nextGaussian();
        return getNum;
    }

    /**
     * 期望值y，方差z的正态分布随机数（取值与真正的正态分布有极细小的差别，但大致符合）
     * @return
     */
    public static Double getRandomInfo(Double y, Double z){
        Random random = new Random();
        Double getNum2 = Math.sqrt(z) * random.nextGaussian() + y;
        return getNum2;
    }

}
