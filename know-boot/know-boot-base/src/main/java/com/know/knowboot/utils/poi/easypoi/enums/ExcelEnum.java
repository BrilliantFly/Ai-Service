package com.know.knowboot.utils.poi.easypoi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public enum ExcelEnum implements Serializable {

    TITLE_ROWS(1,"表格标题行占用行数（合并单元格）"),
    HEADER_ROWS(1,"表头占用行数（合并单元格）")
    ;

    private Integer value;
    private String msg;


}
