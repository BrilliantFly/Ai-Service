package com.know.knowboot.context;

/**
 * 租户上下文 Holder
 * 用于在当前线程中传递租户信息
 * 
 * 注意：不直接依赖 Sa-Token，在 know-boot-system 模块中使用时传入用户ID
 */
public class TenantContextHolder {

    /**
     * 租户ID的ThreadLocal
     */
    private static final ThreadLocal<Long> TENANT_ID = new ThreadLocal<>();

    /**
     * 租户编码的ThreadLocal
     */
    private static final ThreadLocal<String> TENANT_CODE = new ThreadLocal<>();

    /**
     * 是否租户管理员
     */
    private static final ThreadLocal<Boolean> IS_TENANT_ADMIN = new ThreadLocal<>();

    /**
     * 用户ID的ThreadLocal
     */
    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();

    /**
     * 设置当前租户上下文
     */
    public static void setTenant(Long tenantId, String tenantCode, Boolean isAdmin) {
        TENANT_ID.set(tenantId);
        TENANT_CODE.set(tenantCode);
        IS_TENANT_ADMIN.set(isAdmin);
    }

    /**
     * 设置租户ID
     */
    public static void setTenantId(Long tenantId) {
        TENANT_ID.set(tenantId);
    }

    /**
     * 获取当前租户ID
     */
    public static Long getTenantId() {
        Long tenantId = TENANT_ID.get();
        if (tenantId == null) {
            // 如果未设置，返回默认租户ID = 0 (系统级)
            return 0L;
        }
        return tenantId;
    }

    /**
     * 获取租户编码
     */
    public static String getTenantCode() {
        return TENANT_CODE.get();
    }

    /**
     * 获取租户编码，如果为空返回 "DEFAULT"
     */
    public static String getTenantCodeOrDefault() {
        String code = TENANT_CODE.get();
        return code != null ? code : "DEFAULT";
    }

    /**
     * 是否租户管理员
     */
    public static Boolean isTenantAdmin() {
        Boolean isAdmin = IS_TENANT_ADMIN.get();
        return isAdmin != null && isAdmin;
    }

    /**
     * 检查是否为系统级租户 (tenantId = 0 或 NULL)
     */
    public static boolean isSystemTenant() {
        Long tenantId = TENANT_ID.get();
        return tenantId == null || tenantId == 0L;
    }

    /**
     * 获取当前登录用户ID
     * 优先从ThreadLocal获取，如果没有则返回null
     */
    public static Long getUserId() {
        return USER_ID.get();
    }

    /**
     * 设置当前登录用户ID
     * 在请求拦截器中设置
     */
    public static void setUserId(Long userId) {
        USER_ID.set(userId);
    }

    /**
     * 清除租户上下文
     */
    public static void clear() {
        TENANT_ID.remove();
        TENANT_CODE.remove();
        IS_TENANT_ADMIN.remove();
        USER_ID.remove();
    }

    /**
     * 设置默认租户
     */
    public static void setDefaultTenant() {
        TENANT_ID.set(0L);
        TENANT_CODE.set("DEFAULT");
        IS_TENANT_ADMIN.set(false);
    }

    /**
     * 从用户登录信息初始化租户上下文
     * 在登录成功后调用
     */
    public static void initFromLogin(Long userId, Long tenantId, String tenantCode, Boolean isAdmin) {
        setTenant(tenantId, tenantCode, isAdmin);
        setUserId(userId);
    }
}