package com.know.knowboot.plugin.notice.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class NoticeSmsVo {

    private Integer scene;
    private String mobile;
    private Integer expire;
    private String[] params;

    private String code;

}
