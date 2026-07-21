package com.know.knowboot.init;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.know.knowboot.entity.tenant.SysRole;
import com.know.knowboot.entity.tenant.SysTenant;
import com.know.knowboot.service.ISysRoleService;
import com.know.knowboot.service.ISysTenantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 租户数据初始化器
 * 新租户创建时自动初始化基础数据
 *
 * 修复方案：使用 MyBatis-Plus 原生查询，避免使用 getByCode/selectOne 等单条查询方法
 * 因为这些方法会触发 PageHelper 拦截器导致参数绑定冲突
 *
 * 配置启用: know.init.tenant-data-initializer-enabled: true
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "know.init.tenant-data-initializer-enabled", havingValue = "true", matchIfMissing = false)
public class TenantDataInitializer implements CommandLineRunner {

    @Autowired
    private ISysTenantService sysTenantService;

    @Autowired
    private ISysRoleService sysRoleService;

    @Override
    public void run(String... args) throws Exception {
        log.info("开始初始化系统默认数据...");
        
        // 1. 检查并初始化默认租户
        initDefaultTenant();
        
        // 2. 检查并初始化默认角色
        initDefaultRoles();
        
        log.info("系统默认数据初始化完成");
    }

    /**
     * 初始化默认租户
     * 使用 list() 而不是 getByCode() 避免 PageHelper 冲突
     */
    private void initDefaultTenant() {
        // 使用 list 查询代替 getByCode，避免 PageHelper 拦截 selectOne
        List<SysTenant> list = sysTenantService.list(
                new LambdaQueryWrapper<SysTenant>()
                        .eq(SysTenant::getCode, "DEFAULT")
        );
        
        if (list == null || list.isEmpty()) {
            SysTenant defaultTenant = new SysTenant();
            defaultTenant.setName("默认租户");
            defaultTenant.setCode("DEFAULT");
            defaultTenant.setStatus(1);
            defaultTenant.setExpireTime(new Date(System.currentTimeMillis() + 365L * 24 * 60 * 60 * 1000));
            defaultTenant.setCreateBy("system");
            defaultTenant.setCreateTime(new Date());
            sysTenantService.save(defaultTenant);
            log.info("创建默认租户: DEFAULT");
        }
    }

    /**
     * 初始化默认角色
     */
    private void initDefaultRoles() {
        // 超级管理员角色
        if (!roleExists("SUPER_ADMIN")) {
            SysRole adminRole = new SysRole();
            adminRole.setRoleName("超级管理员");
            adminRole.setRoleCode("SUPER_ADMIN");
            adminRole.setSort(1);
            adminRole.setStatus(1);
            adminRole.setCreateBy("system");
            adminRole.setCreateTime(new Date());
            sysRoleService.save(adminRole);
            log.info("创建超级管理员角色: SUPER_ADMIN");
        }

        // 普通用户角色
        if (!roleExists("USER")) {
            SysRole userRole = new SysRole();
            userRole.setRoleName("普通用户");
            userRole.setRoleCode("USER");
            userRole.setSort(2);
            userRole.setStatus(1);
            userRole.setCreateBy("system");
            userRole.setCreateTime(new Date());
            sysRoleService.save(userRole);
            log.info("创建普通用户角色: USER");
        }
    }

    /**
     * 检查角色是否存在
     * 使用 list 查询 count 方式，避免 count() 方法
     */
    private boolean roleExists(String roleCode) {
        // 使用 list size 判断是否存在，避免 count() 方法
        List<SysRole> list = sysRoleService.list(
                new LambdaQueryWrapper<SysRole>()
                        .eq(SysRole::getRoleCode, roleCode)
        );
        return list != null && !list.isEmpty();
    }
}