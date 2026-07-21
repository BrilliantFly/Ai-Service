package com.know.knowboot.poi.easyexcel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.enums.BooleanEnum;
import com.alibaba.excel.enums.poi.BorderStyleEnum;
import lombok.Data;

@Data
@ColumnWidth(value = 20)   //列宽度 注解可以写在类上方，也可以写在字段上方
@ContentRowHeight(value = 30)  //列高度 -1 自动行高
@HeadRowHeight(value = 35)  //表头高度
public class ExcelVo {

    @ExcelProperty(value = "表头单元格一", index = 0)
    private String headOne;

    @ExcelProperty(value = {"表头单元格二", "Come"}, index = 1)
    // 自动换行
    @ContentStyle(wrapped = BooleanEnum.TRUE,borderLeft = BorderStyleEnum.THIN)
    private String headTwoCome;
    @ExcelProperty(value = {"表头单元格二", "On"}, index = 2)
    private String headTwoOn;

    @ExcelProperty(value = {"表头单元格三", "Come"}, index = 3)
    private String headThreeCome;
    @ExcelProperty(value = {"表头单元格三", "On"}, index = 4)
    private String headThreeOn;

    @ExcelProperty(value = {"表头单元格四", "Come"}, index = 5)
    private String headFourCome;
    @ExcelProperty(value = {"表头单元格四", "On"}, index = 6)
    private String headFourOn;

    @ExcelProperty(value = {"表头单元格五", "Come"}, index = 7)
    private String headFiveCome;
    @ExcelProperty(value = {"表头单元格五", "On"}, index = 8)
    private String headFiveOn;

    @ExcelProperty(value = {"表头单元格六", "Come"}, index = 9)
    private String headSixCome;
    @ExcelProperty(value = {"表头单元格六", "On"}, index = 10)
    private String headSixOn;

    @ColumnWidth(value = 30)
    @ExcelProperty(value = "表头单元格七", index = 11)
    private String headSeven;

}
