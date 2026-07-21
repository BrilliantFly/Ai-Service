package com.know.knowboot.tree;

import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class AnalysisTree {

    public static void main(String[] args) {

        List<NodeDTO> result = new ArrayList<>();

        List<NodeDTO> stationlist = null;
        if (CollectionUtils.isNotEmpty(stationlist)){
            for (int i = 0;i < stationlist.size();i++){
                NodeDTO nodeDTO = stationlist.get(i);
                nodeDTO.setKeyGuid(UUID.randomUUID().toString());
                if (CollectionUtils.isNotEmpty(nodeDTO.getSub())){
                    getChildren(result,nodeDTO,nodeDTO.getSub());
                }

                nodeDTO.setSub(null);
                result.add(nodeDTO);

            }
        }

        // list 根据某个字段去重
        // 第一步：获取层级
        List<NodeDTO> voList = new ArrayList<>();
        // 去重
        voList = voList.stream().filter(distinctByKey(NodeDTO::getNodeID)).collect(Collectors.toList());

        //多条件去重
        voList = voList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getNodeID() + ";" + o.getNumber()))), ArrayList::new));


    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    /**
     * 解析Json串
     * @param result
     * @param parentNode
     * @param children
     */
    public static void getChildren(List<NodeDTO> result, NodeDTO parentNode, List<NodeDTO> children){
        for (int i = 0;i < children.size();i++){
            NodeDTO dto = children.get(i);
            dto.setKeyGuid(UUID.randomUUID().toString());
            dto.setParGuid(parentNode.getKeyGuid());

            if (CollectionUtils.isNotEmpty(dto.getSub())){
                getChildren(result,dto,dto.getSub());
            }

            dto.setSub(null);
            result.add(dto);
        }
    }

    /**
     * bupsum向上级汇总内容
     * 1计算参数 2工法 3大断面尺寸 0不汇总
     * bhz:金额是否往上级汇总，1汇总，0不汇总
     *
     * tree向上汇总，首先获取根节点，然后从每个根节点向上汇总
     *
     * @param childNode
     * @param allDtos
     */
//    public void bupsumData(IpConfigVO childNode, List<IpConfigVO> allDtos) {
//
//
//        if (CollectionUtils.isNotEmpty(allDtos)) {
//            // 向上一级汇总
//            Optional<IpConfigVO> optional = allDtos.stream().filter(t -> t.getId().equals(childNode.getParentId())).findFirst();
//            if (optional.isPresent()) {
//                IpConfigVO configVO = optional.get();
//                // 计算参数值
//                if (childNode.getBupSum().contains(ArrangeEnum.CALCULATION_PARAMETERS.getCode())) {
//                    if (childNode.getCaliber() != null) {
//                        if (configVO.getCaliber() != null) {
//                            configVO.setCaliber(childNode.getCaliber().add(configVO.getCaliber()));
//                        } else {
//                            configVO.setCaliber(childNode.getCaliber());
//                        }
//                    }
//                }
//
//                // 项目特征
//                // TODO: 2022-11-15 项目特征处理
//
//                // 金额
//                if (childNode.getBhz().contains(BhzEnum.SUMMARY.getCode())) {
//                    // 控制价
//                    if (childNode.getControlPrice() != null && configVO.getControlPrice() != null) {
//                        if (childNode.getControlPrice().getC() != null) {
//                            if (configVO.getControlPrice().getC() != null) {
//                                configVO.getControlPrice().setC(childNode.getControlPrice().getC().add(configVO.getControlPrice().getC()));
//                            } else {
//                                configVO.getControlPrice().setC(childNode.getControlPrice().getC());
//                            }
//                        }
//                    }
//                    // 中标价
//                    if (childNode.getBidWSinningPrice() != null && configVO.getBidWSinningPrice() != null) {
//                        if (childNode.getBidWSinningPrice().getC() != null) {
//                            if (configVO.getBidWSinningPrice().getC() != null) {
//                                configVO.getBidWSinningPrice().setC(childNode.getBidWSinningPrice().getC().add(configVO.getBidWSinningPrice().getC()));
//                            } else {
//                                configVO.getBidWSinningPrice().setC(childNode.getBidWSinningPrice().getC());
//                            }
//                        }
//                    }
//                    // 审定值
//                    if (childNode.getApprovedValue() != null && configVO.getApprovedValue() != null) {
//                        if (childNode.getApprovedValue().getC() != null) {
//                            if (configVO.getApprovedValue().getC() != null) {
//                                configVO.getApprovedValue().setC(childNode.getApprovedValue().getC().add(configVO.getApprovedValue().getC()));
//                            } else {
//                                configVO.getApprovedValue().setC(childNode.getApprovedValue().getC());
//                            }
//                        }
//                    }
//                }
//
//                if (configVO.getParentId() != null) {
//                    bupsumData(configVO, allDtos);
//                }
//
//            }
//        }
//    }

}
