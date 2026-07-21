package com.know.knowboot.screw;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public enum FileTypeEnum implements Serializable {

    WORD("doc", "WORD"),
    HTML("html", "HTML");


    private final String code;
    private final String name;

}
