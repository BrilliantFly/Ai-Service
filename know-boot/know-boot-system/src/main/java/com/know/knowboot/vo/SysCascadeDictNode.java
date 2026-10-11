package com.know.knowboot.vo;

import com.know.knowboot.entity.tenant.SysCascadeDictData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 级联字典树节点
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("级联字典树节点")
public class SysCascadeDictNode extends SysCascadeDictData {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("子节点")
    private List<SysCascadeDictNode> children = new ArrayList<>();
}
