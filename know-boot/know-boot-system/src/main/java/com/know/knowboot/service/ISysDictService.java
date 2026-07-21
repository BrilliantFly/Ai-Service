package com.know.knowboot.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.know.knowboot.entity.tenant.SysDict;
import com.know.knowboot.entity.tenant.SysDictType;
import com.know.knowboot.mapper.tenant.SysDictMapper;
import com.know.knowboot.mapper.tenant.SysDictTypeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ISysDictService {

    private final SysDictMapper sysDictMapper;
    private final SysDictTypeMapper sysDictTypeMapper;

    public List<SysDict> listByTypeId(Long dictTypeId) {
        return sysDictMapper.selectList(
            new LambdaQueryWrapper<SysDict>()
                .eq(SysDict::getDictTypeId, dictTypeId)
                .eq(SysDict::getDelFlag, 0)
                .orderByAsc(SysDict::getSort)
        );
    }

    public SysDict getById(Long id) {
        return sysDictMapper.selectById(id);
    }

    @Transactional
    public boolean add(SysDict dict) {
        return sysDictMapper.insert(dict) > 0;
    }

    @Transactional
    public boolean update(SysDict dict) {
        return sysDictMapper.updateById(dict) > 0;
    }

    @Transactional
    public boolean delete(Long id) {
        return sysDictMapper.deleteById(id) > 0;
    }

    public List<SysDictType> listAll() {
        return sysDictTypeMapper.selectList(
            new LambdaQueryWrapper<SysDictType>()
                .eq(SysDictType::getDelFlag, 0)
                .orderByAsc(SysDictType::getId)
        );
    }

    public SysDictType getTypeById(Long id) {
        return sysDictTypeMapper.selectById(id);
    }

    @Transactional
    public boolean addType(SysDictType dictType) {
        return sysDictTypeMapper.insert(dictType) > 0;
    }

    @Transactional
    public boolean updateType(SysDictType dictType) {
        return sysDictTypeMapper.updateById(dictType) > 0;
    }

    @Transactional
    public boolean deleteType(Long id) {
        // 删除类型及其所有字典数据
        sysDictMapper.delete(new LambdaQueryWrapper<SysDict>().eq(SysDict::getDictTypeId, id));
        return sysDictTypeMapper.deleteById(id) > 0;
    }
}