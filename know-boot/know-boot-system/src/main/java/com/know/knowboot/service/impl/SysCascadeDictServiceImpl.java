package com.know.knowboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.know.knowboot.entity.tenant.SysCascadeDictData;
import com.know.knowboot.entity.tenant.SysCascadeDictType;
import com.know.knowboot.exception.BaseException;
import com.know.knowboot.mapper.tenant.SysCascadeDictDataMapper;
import com.know.knowboot.mapper.tenant.SysCascadeDictTypeMapper;
import com.know.knowboot.service.ISysCascadeDictService;
import com.know.knowboot.vo.SysCascadeDictNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 级联字典服务实现
 * 类型 + 树形数据，独立于平铺字典 sys_dict
 */
@Service
@RequiredArgsConstructor
public class SysCascadeDictServiceImpl implements ISysCascadeDictService {

    /** 业务错误码：前端 axios 仅把 0/1/2/200 视为成功，业务校验失败需使用非成功码 */
    private static final int BIZ_ERROR_CODE = 500;

    private static final long ROOT_PARENT_ID = 0L;

    private final SysCascadeDictTypeMapper typeMapper;
    private final SysCascadeDictDataMapper dataMapper;

    private void bizError(String msg) {
        throw new BaseException(BIZ_ERROR_CODE, msg, 1);
    }

    // ==================== 类型 ====================

    @Override
    public List<SysCascadeDictType> listTypes(String typeName, String typeCode, Integer status) {
        LambdaQueryWrapper<SysCascadeDictType> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(typeName), SysCascadeDictType::getTypeName, typeName)
                .like(StringUtils.hasText(typeCode), SysCascadeDictType::getTypeCode, typeCode)
                .eq(status != null, SysCascadeDictType::getStatus, status)
                .orderByAsc(SysCascadeDictType::getId);
        return typeMapper.selectList(wrapper);
    }

    @Override
    public SysCascadeDictType getTypeById(Long id) {
        return typeMapper.selectById(id);
    }

    private SysCascadeDictType getTypeByCode(String typeCode) {
        if (!StringUtils.hasText(typeCode)) {
            return null;
        }
        return typeMapper.selectOne(new LambdaQueryWrapper<SysCascadeDictType>()
                .eq(SysCascadeDictType::getTypeCode, typeCode)
                .last("LIMIT 1"));
    }

    @Override
    @Transactional
    public boolean addType(SysCascadeDictType type) {
        if (!StringUtils.hasText(type.getTypeName())) {
            bizError("类型名称不能为空");
        }
        if (!StringUtils.hasText(type.getTypeCode())) {
            bizError("类型编码不能为空");
        }
        if (existsTypeCode(type.getTypeCode(), null)) {
            bizError("类型编码已存在：" + type.getTypeCode());
        }
        long now = System.currentTimeMillis();
        if (type.getStatus() == null) {
            type.setStatus(1);
        }
        type.setCreateTime(now);
        type.setUpdateTime(now);
        return typeMapper.insert(type) > 0;
    }

    @Override
    @Transactional
    public boolean updateType(SysCascadeDictType type) {
        if (type.getId() == null) {
            bizError("类型ID不能为空");
        }
        SysCascadeDictType exist = typeMapper.selectById(type.getId());
        if (exist == null) {
            bizError("类型不存在");
        }
        if (StringUtils.hasText(type.getTypeCode()) && existsTypeCode(type.getTypeCode(), type.getId())) {
            bizError("类型编码已存在：" + type.getTypeCode());
        }
        type.setUpdateTime(System.currentTimeMillis());
        return typeMapper.updateById(type) > 0;
    }

    @Override
    @Transactional
    public boolean deleteType(Long id) {
        if (id == null) {
            bizError("类型ID不能为空");
        }
        SysCascadeDictType exist = typeMapper.selectById(id);
        if (exist == null) {
            bizError("类型不存在");
        }
        Long dataCount = dataMapper.selectCount(new LambdaQueryWrapper<SysCascadeDictData>()
                .eq(SysCascadeDictData::getDictTypeId, id));
        if (dataCount != null && dataCount > 0) {
            bizError("该类型下存在级联数据，无法删除");
        }
        // 逻辑删除后 type_code 仍占用唯一索引，先改写为墓碑值以释放编码
        releaseTypeCode(id, exist.getTypeCode());
        return typeMapper.deleteById(id) > 0;
    }

    /**
     * 逻辑删除前释放唯一编码：把 type_code 改写为不会冲突的墓碑值。
     * 否则删除后无法再新增相同编码（数据库唯一索引包含已逻辑删除的行）。
     */
    private void releaseTypeCode(Long id, String typeCode) {
        SysCascadeDictType tombstone = new SysCascadeDictType();
        tombstone.setId(id);
        tombstone.setTypeCode(tombstoneValue(typeCode, id));
        typeMapper.updateById(tombstone);
    }

    private boolean existsTypeCode(String typeCode, Long excludeId) {
        LambdaQueryWrapper<SysCascadeDictType> wrapper = new LambdaQueryWrapper<SysCascadeDictType>()
                .eq(SysCascadeDictType::getTypeCode, typeCode)
                .ne(excludeId != null, SysCascadeDictType::getId, excludeId);
        Long count = typeMapper.selectCount(wrapper);
        return count != null && count > 0;
    }

    // ==================== 数据 ====================

    @Override
    public List<SysCascadeDictNode> tree(String typeCode, Long typeId, Integer status) {
        Long resolvedTypeId = resolveTypeId(typeCode, typeId);
        if (resolvedTypeId == null) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<SysCascadeDictData> wrapper = new LambdaQueryWrapper<SysCascadeDictData>()
                .eq(SysCascadeDictData::getDictTypeId, resolvedTypeId)
                .eq(status != null, SysCascadeDictData::getStatus, status)
                .orderByAsc(SysCascadeDictData::getSort)
                .orderByAsc(SysCascadeDictData::getId);
        List<SysCascadeDictData> list = dataMapper.selectList(wrapper);
        return buildTree(list);
    }

    @Override
    public List<SysCascadeDictData> listData(Long typeId) {
        if (typeId == null) {
            return new ArrayList<>();
        }
        return dataMapper.selectList(new LambdaQueryWrapper<SysCascadeDictData>()
                .eq(SysCascadeDictData::getDictTypeId, typeId)
                .orderByAsc(SysCascadeDictData::getSort)
                .orderByAsc(SysCascadeDictData::getId));
    }

    private Long resolveTypeId(String typeCode, Long typeId) {
        if (typeId != null) {
            return typeId;
        }
        SysCascadeDictType type = getTypeByCode(typeCode);
        return type == null ? null : type.getId();
    }

    private List<SysCascadeDictNode> buildTree(List<SysCascadeDictData> list) {
        Map<Long, SysCascadeDictNode> nodeMap = new LinkedHashMap<>();
        for (SysCascadeDictData data : list) {
            SysCascadeDictNode node = new SysCascadeDictNode();
            BeanUtils.copyProperties(data, node);
            nodeMap.put(data.getId(), node);
        }
        List<SysCascadeDictNode> roots = new ArrayList<>();
        for (SysCascadeDictData data : list) {
            SysCascadeDictNode node = nodeMap.get(data.getId());
            Long parentId = data.getParentId();
            if (parentId == null || parentId == ROOT_PARENT_ID) {
                roots.add(node);
                continue;
            }
            SysCascadeDictNode parent = nodeMap.get(parentId);
            if (parent != null) {
                parent.getChildren().add(node);
            } else {
                // 父级被过滤或不存在，作为根节点兜底展示
                roots.add(node);
            }
        }
        return roots;
    }

    @Override
    @Transactional
    public boolean addData(SysCascadeDictData data) {
        if (data.getDictTypeId() == null) {
            bizError("所属类型不能为空");
        }
        if (typeMapper.selectById(data.getDictTypeId()) == null) {
            bizError("所属类型不存在");
        }
        if (!StringUtils.hasText(data.getName())) {
            bizError("名称不能为空");
        }
        if (!StringUtils.hasText(data.getValue())) {
            bizError("编码不能为空");
        }
        if (existsDataValue(data.getDictTypeId(), data.getValue(), null)) {
            bizError("编码已存在：" + data.getValue());
        }
        normalizeAndValidateParent(data, null);
        long now = System.currentTimeMillis();
        if (data.getSort() == null) {
            data.setSort(0);
        }
        if (data.getStatus() == null) {
            data.setStatus(1);
        }
        data.setCreateTime(now);
        data.setUpdateTime(now);
        return dataMapper.insert(data) > 0;
    }

    @Override
    @Transactional
    public boolean updateData(SysCascadeDictData data) {
        if (data.getId() == null) {
            bizError("数据ID不能为空");
        }
        SysCascadeDictData exist = dataMapper.selectById(data.getId());
        if (exist == null) {
            bizError("数据不存在");
        }
        Long typeId = data.getDictTypeId() != null ? data.getDictTypeId() : exist.getDictTypeId();
        data.setDictTypeId(typeId);
        if (StringUtils.hasText(data.getValue()) && existsDataValue(typeId, data.getValue(), data.getId())) {
            bizError("编码已存在：" + data.getValue());
        }
        normalizeAndValidateParent(data, data.getId());
        data.setUpdateTime(System.currentTimeMillis());
        return dataMapper.updateById(data) > 0;
    }

    @Override
    @Transactional
    public boolean deleteData(Long id) {
        if (id == null) {
            bizError("数据ID不能为空");
        }
        SysCascadeDictData exist = dataMapper.selectById(id);
        if (exist == null) {
            bizError("数据不存在");
        }
        Long childCount = dataMapper.selectCount(new LambdaQueryWrapper<SysCascadeDictData>()
                .eq(SysCascadeDictData::getParentId, id));
        if (childCount != null && childCount > 0) {
            bizError("存在下级节点，无法删除");
        }
        // 逻辑删除后 value 仍占用唯一索引，先改写为墓碑值以释放编码
        releaseDataValue(id, exist.getValue());
        return dataMapper.deleteById(id) > 0;
    }

    /**
     * 逻辑删除前释放唯一编码：把 value 改写为不会冲突的墓碑值。
     * 否则删除后无法再新增相同编码（数据库唯一索引包含已逻辑删除的行）。
     */
    private void releaseDataValue(Long id, String value) {
        SysCascadeDictData tombstone = new SysCascadeDictData();
        tombstone.setId(id);
        tombstone.setValue(tombstoneValue(value, id));
        dataMapper.updateById(tombstone);
    }

    /** 生成不超过列长的墓碑编码（保留原始前缀便于排查） */
    private String tombstoneValue(String original, Long id) {
        String suffix = "#del" + id;
        String base = original == null ? "" : original;
        int max = 100 - suffix.length();
        if (base.length() > max) {
            base = base.substring(0, max);
        }
        return base + suffix;
    }

    /**
     * 归一化并校验父级：父级须为同类型下已存在节点，且不能是自己或自己的后代
     */
    private void normalizeAndValidateParent(SysCascadeDictData data, Long selfId) {
        Long parentId = data.getParentId();
        if (parentId == null) {
            data.setParentId(ROOT_PARENT_ID);
            return;
        }
        if (parentId == ROOT_PARENT_ID) {
            return;
        }
        if (selfId != null && parentId.equals(selfId)) {
            bizError("父级不能是自己");
        }
        SysCascadeDictData parent = dataMapper.selectById(parentId);
        if (parent == null) {
            bizError("父级不存在");
        }
        if (!parent.getDictTypeId().equals(data.getDictTypeId())) {
            bizError("父级必须属于同一类型");
        }
        if (selfId != null && isDescendant(selfId, parentId)) {
            bizError("父级不能是自己的下级");
        }
    }

    /**
     * 判断 candidateId 是否为 rootId 的后代
     */
    private boolean isDescendant(Long rootId, Long candidateId) {
        if (rootId == null || candidateId == null) {
            return false;
        }
        Long current = candidateId;
        int guard = 0;
        while (current != null && current != ROOT_PARENT_ID && guard++ < 1000) {
            if (current.equals(rootId)) {
                return true;
            }
            SysCascadeDictData node = dataMapper.selectById(current);
            if (node == null) {
                return false;
            }
            current = node.getParentId();
        }
        return false;
    }

    private boolean existsDataValue(Long typeId, String value, Long excludeId) {
        Long count = dataMapper.selectCount(new LambdaQueryWrapper<SysCascadeDictData>()
                .eq(SysCascadeDictData::getDictTypeId, typeId)
                .eq(SysCascadeDictData::getValue, value)
                .ne(excludeId != null, SysCascadeDictData::getId, excludeId));
        return count != null && count > 0;
    }
}
