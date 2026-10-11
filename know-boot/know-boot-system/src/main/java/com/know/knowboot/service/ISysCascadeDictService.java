package com.know.knowboot.service;

import com.know.knowboot.entity.tenant.SysCascadeDictData;
import com.know.knowboot.entity.tenant.SysCascadeDictType;
import com.know.knowboot.vo.SysCascadeDictNode;

import java.util.List;

/**
 * 级联字典服务接口
 * 类型 + 树形数据，独立于平铺字典 sys_dict
 */
public interface ISysCascadeDictService {

    // ==================== 类型 ====================

    /**
     * 类型列表（按名称/编码/状态过滤）
     */
    List<SysCascadeDictType> listTypes(String typeName, String typeCode, Integer status);

    /**
     * 类型详情
     */
    SysCascadeDictType getTypeById(Long id);

    /**
     * 新增类型（type_code 全局唯一）
     */
    boolean addType(SysCascadeDictType type);

    /**
     * 修改类型
     */
    boolean updateType(SysCascadeDictType type);

    /**
     * 删除类型（存在数据则拒绝）
     */
    boolean deleteType(Long id);

    // ==================== 数据 ====================

    /**
     * 级联树；typeCode / typeId 二者其一即可，status 可选
     */
    List<SysCascadeDictNode> tree(String typeCode, Long typeId, Integer status);

    /**
     * 平铺列表
     */
    List<SysCascadeDictData> listData(Long typeId);

    /**
     * 新增数据（value 同类型内唯一；父级须同类型且存在）
     */
    boolean addData(SysCascadeDictData data);

    /**
     * 修改数据（父级不可为自己或后代；value 唯一）
     */
    boolean updateData(SysCascadeDictData data);

    /**
     * 删除数据（存在下级则拒绝）
     */
    boolean deleteData(Long id);
}
