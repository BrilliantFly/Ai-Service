package com.know.knowboot.bigdecimal;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
public class BigDecimalInfo {

    public static void main(String[] args) {
        //  String转BigDecimal
        String a = "50.00";  //字符串类型，必须是数字，否则会报错， java.lang.NumberFormatException 异常
        BigDecimal b = new BigDecimal(a);

        //  BigDecimal加法运算
        BigDecimal decimalA = new BigDecimal(100);
        BigDecimal decimalB = new BigDecimal(200);
        BigDecimal decimalC = new BigDecimal(300);
        log.info("---------------------- 加法 ----------------------- {}", decimalC.add(decimalA).add(decimalB));

        //  BigDecimal减法运算
        BigDecimal decimalD = new BigDecimal(100);
        BigDecimal decimalE = new BigDecimal(200);
        BigDecimal decimalF = new BigDecimal(300);
        log.info("---------------------- 减法 ----------------------- {}", decimalF.subtract(decimalE).subtract(decimalD));

        //  BigDecimal乘法运算
        BigDecimal decimalG = new BigDecimal(100);
        BigDecimal decimalH = new BigDecimal(200);
        BigDecimal decimalI = new BigDecimal(300);
        log.info("---------------------- 乘法 ----------------------- {}",decimalG.multiply(decimalH).multiply(decimalI));

        // BigDecimal避免踩坑
        BigDecimal decimalJ = new BigDecimal("20.12");//string数据转换BigDecimal
        BigDecimal decimalK = new BigDecimal("60.11");//string数据转换BigDecimal
        BigDecimal decimalL = new BigDecimal("70");//string数据转换BigDecimal
        //进行相加再相乘操作时注意不要直接用符号+、-、*等，后面是四舍五入的格式
        log.info("---------------------- 运算 ----------------------- {}",(decimalJ.add(decimalK).add(decimalL)).multiply(new BigDecimal("5"))
                .setScale(2, BigDecimal.ROUND_HALF_UP));

        //   保留两位小数
        //   第一参数表示除数， 第二个参数表示小数点后保留位数，
        //   第三个参数表示舍入模式，只有在作除法运算或四舍五入时才用到舍入模式，有下面这几种
        //   ROUND_CEILING    //向正无穷方向舍入
        //   ROUND_DOWN    //向零方向舍入
        //   ROUND_FLOOR    //向负无穷方向舍入
        //   ROUND_HALF_DOWN    //向（距离）最近的一边舍入，除非两边（的距离）是相等,如果是这样，向下舍入, 例如1.55 保留一位小数结果为1.5
        //   ROUND_HALF_EVEN    //向（距离）最近的一边舍入，除非两边（的距离）是相等,如果是这样，如果保留位数是奇数，使用ROUND_HALF_UP，如果是偶数，使用ROUND_HALF_DOWN
        //   ROUND_HALF_UP    //向（距离）最近的一边舍入，除非两边（的距离）是相等,如果是这样，向上舍入, 1.55保留一位小数结果为1.6
        //   ROUND_UNNECESSARY    //计算结果是精确的，不需要舍入模式
        //   ROUND_UP    //向远离0的方向舍入
        log.info("---------------------- 保留两位小数RoundingMode.HALF_UP ----------------------- {}",new BigDecimal("2.123456").setScale(2, RoundingMode.HALF_UP));
        log.info("---------------------- 保留两位小数BigDecimal.ROUND_HALF_UP ----------------------- {}",new BigDecimal("2.123456").setScale(2, BigDecimal.ROUND_HALF_UP));

    }

}
