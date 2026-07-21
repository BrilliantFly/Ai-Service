package com.know.knowboot.service.impl.tenant;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.entity.tenant.SysUserDept;
import com.know.knowboot.mapper.tenant.SysUserDeptMapper;
import com.know.knowboot.service.tenant.ISysUserDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysUserDeptServiceImpl extends ServiceImpl<SysUserDeptMapper, SysUserDept> implements ISysUserDeptService {

    @Autowired
    private SysUserDeptMapper sysUserDeptMapper;

    @Override
    public List<Long> selectDeptIdsByUserId(Long userId) {
        return sysUserDeptMapper.selectDeptIdsByUserId(userId);
    }

    @Override
    public List<Long> selectUserIdsByDeptId(Long deptId) {
        return sysUserDeptMapper.selectUserIdsByDeptId(deptId);
    }

    @Override
    @Transactional
    public boolean assignDepts(Long userId, List<Long> deptIds) {
        // 先删除原有关联
        deleteByUserId(userId);
        // 批量插入新关联
        if (deptIds != null && !deptIds.isEmpty()) {
            List<SysUserDept> list = new ArrayList<>();
            for (Long deptId : deptIds) {
                SysUserDept ud = new SysUserDept();
                ud.setUserId(userId);
                ud.setDeptId(deptId);
                list.add(ud);
            }
            return saveBatch(list);
        }
        return true;
    }

    @Override
    @Transactional
    public boolean deleteByUserId(Long userId) {
        return remove(new LambdaQueryWrapper<SysUserDept>().eq(SysUserDept::getUserId, userId));
    }

    @Override
    @Transactional
    public boolean deleteByDeptId(Long deptId) {
        return remove(new LambdaQueryWrapper<SysUserDept>().eq(SysUserDept::getDeptId, deptId));
    }
}