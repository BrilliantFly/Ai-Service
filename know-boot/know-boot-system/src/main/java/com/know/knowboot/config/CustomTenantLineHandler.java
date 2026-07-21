package com.know.knowboot.config;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.know.knowboot.context.TenantContextHolder;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;

import java.util.HashSet;
import java.util.Set;

/**
 * 多租户SQL处理器
 * MyBatis Plus TenantLineHandler 实现
 * 
 * 自动在SQL中注入租户ID过滤条件
 */
@Slf4j
public class CustomTenantLineHandler implements TenantLineHandler {

    /**
     * 不进行租户过滤的表
     * 系统级表不使用多租户
     */
    private static final Set<String> IGNORE_TENANT_TABLES = new HashSet<>();

    static {
        // 系统级表
        IGNORE_TENANT_TABLES.add("sys_tenant");
        IGNORE_TENANT_TABLES.add("sys_dict_type");
        IGNORE_TENANT_TABLES.add("sys_dict_data");
        IGNORE_TENANT_TABLES.add("sys_permission");
        IGNORE_TENANT_TABLES.add("sys_role_permission");
        IGNORE_TENANT_TABLES.add("sys_menu_permission");
        IGNORE_TENANT_TABLES.add("sys_oper_log");
        IGNORE_TENANT_TABLES.add("sys_login_log");
        // 用户表（用户属于哪个租户通过user_tenant关联）
        IGNORE_TENANT_TABLES.add("sys_user");
        // 角色表（角色属于哪个租户通过tenant_id字段）
        IGNORE_TENANT_TABLES.add("sys_role");
    }

    /**
     * 获取租户ID
     * 从 TenantContextHolder 获取当前租户
     */
    @Override
    public Expression getTenantId() {
        Long tenantId = TenantContextHolder.getTenantId();
        log.debug("当前租户ID: {}", tenantId);
        return new LongValue(tenantId);
    }

    /**
     * 判断表是否需要租户过滤
     * 
     * @param tableName 表名
     * @return true: 需要过滤, false: 跳过
     */
    @Override
    public boolean ignoreTable(String tableName) {
        // 移除表前缀（如 sys_）
        if (tableName.startsWith("sys_")) {
            String shortName = tableName.substring(4);
            if (IGNORE_TENANT_TABLES.contains(shortName)) {
                return true; // 忽略，不添加租户条件
            }
        }
        
        // 检查是否在忽略列表中
        if (IGNORE_TENANT_TABLES.contains(tableName)) {
            return true; // 忽略，不添加租户条件
        }
        
        // 系统租户不过滤
        if (TenantContextHolder.isSystemTenant()) {
            return true; // 跳过租户过滤
        }
        
        return false; // 需要添加租户条件
    }

    /**
     * 获取租户ID字段名
     * 默认使用 tenant_id
     */
    @Override
    public String getTenantIdColumn() {
        return "tenant_id";
    }
}