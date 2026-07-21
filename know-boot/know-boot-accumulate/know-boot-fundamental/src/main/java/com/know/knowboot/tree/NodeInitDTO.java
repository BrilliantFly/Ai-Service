package com.know.knowboot.tree;

import lombok.Data;

import java.util.List;

@Data
public class NodeInitDTO {

    private String bidID;
    private String planID;
    private List<NodeDTO> stationlist;

}
