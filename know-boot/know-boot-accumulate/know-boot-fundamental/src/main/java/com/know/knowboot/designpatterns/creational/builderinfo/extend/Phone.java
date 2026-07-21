package com.know.knowboot.designpatterns.creational.builderinfo.extend;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class Phone {

    private String cpu;
    private String screen;
    private String memory;
    private String mainboard;

}
