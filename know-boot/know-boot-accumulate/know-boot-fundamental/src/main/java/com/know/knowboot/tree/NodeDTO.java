package com.know.knowboot.tree;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class NodeDTO {

    private String keyGuid;
    private String parGuid;

    public String number;
    public String name;
    public BigDecimal cost;
    public Integer nodeID;

    public List<NodeDTO> sub;

}
