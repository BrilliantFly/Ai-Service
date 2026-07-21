package com.know.knowboot.aspect;

import com.know.knowboot.annotation.authorize.annotation.DataScope;
import com.know.knowboot.context.AuthContext;
import com.know.knowboot.entity.tenant.SysRoleDataScope;
import com.know.knowboot.service.ISysRoleDataScopeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据权限切面
 * 在执行查询方法前，根据用户角色动态添加数据权限过滤条件
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class DataScopeAspect {

    private final ApplicationContext applicationContext;

    @Before("@annotation(com.know.knowboot.annotation.authorize.annotation.DataScope)")
    public void before(JoinPoint joinPoint) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            DataScope dataScope = method.getAnnotation(DataScope.class);

            if (dataScope == null || !dataScope.enabled()) {
                return;
            }

            AuthContext.UserInfo userInfo = AuthContext.getCurrentUser();
            if (userInfo == null) {
                return;
            }

            Long userId = userInfo.getUserId();
            List<Long> roleIds = userInfo.getRoleIds();
            if (roleIds == null || roleIds.isEmpty()) {
                return;
            }

            ISysRoleDataScopeService dataScopeService = applicationContext.getBean(ISysRoleDataScopeService.class);
            List<Long> visibleDeptIds = getVisibleDeptIds(userId, roleIds, dataScopeService);

            if (visibleDeptIds != null && !visibleDeptIds.isEmpty()) {
                DataScopeContext.setVisibleDeptIds(visibleDeptIds);
                DataScopeContext.setDeptField(dataScope.deptField());
                DataScopeContext.setUserField(dataScope.userField());
                DataScopeContext.setUserId(userId);
                log.debug("数据权限过滤生效，可见部门: {}", visibleDeptIds);
            }
        } catch (Exception e) {
            log.error("数据权限切面执行失败", e);
        }
    }

    private List<Long> getVisibleDeptIds(Long userId, List<Long> roleIds, ISysRoleDataScopeService service) {
        int maxScopeType = 1;
        for (Long roleId : roleIds) {
            Integer scopeType = service.getDataScopeTypeByRoleId(roleId);
            if (scopeType != null && scopeType > maxScopeType) {
                maxScopeType = scopeType;
            }
        }
        switch (maxScopeType) {
            case 1: return null;
            case 2: return service.getUserDeptIds(userId);
            case 3: return service.getUserDeptAndChildIds(userId);
            case 4: return null;
            case 5: return getCustomDeptIds(roleIds, service);
            default: return null;
        }
    }

    private List<Long> getCustomDeptIds(List<Long> roleIds, ISysRoleDataScopeService service) {
        List<Long> allCustomDepts = new ArrayList<>();
        Set<Long> seen = new HashSet<>();
        for (Long roleId : roleIds) {
            List<SysRoleDataScope> configs = service.getByRoleId(roleId);
            if (configs != null) {
                for (SysRoleDataScope config : configs) {
                    if (config.getDeptId() != null && seen.add(config.getDeptId())) {
                        allCustomDepts.add(config.getDeptId());
                    }
                }
            }
        }
        return allCustomDepts;
    }
}

/**
 * 数据权限上下文
 */
class DataScopeContext {
    private static final ThreadLocal<List<Long>> VISIBLE_DEPT_IDS = new ThreadLocal<>();
    private static final ThreadLocal<String> DEPT_FIELD = new ThreadLocal<>();
    private static final ThreadLocal<String> USER_FIELD = new ThreadLocal<>();
    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();

    public static void setVisibleDeptIds(List<Long> deptIds) { VISIBLE_DEPT_IDS.set(deptIds); }
    public static List<Long> getVisibleDeptIds() { return VISIBLE_DEPT_IDS.get(); }
    public static void setDeptField(String field) { DEPT_FIELD.set(field); }
    public static String getDeptField() { return DEPT_FIELD.get(); }
    public static void setUserField(String field) { USER_FIELD.set(field); }
    public static String getUserField() { return USER_FIELD.get(); }
    public static void setUserId(Long userId) { USER_ID.set(userId); }
    public static Long getUserId() { return USER_ID.get(); }
    public static void clear() {
        VISIBLE_DEPT_IDS.remove();
        DEPT_FIELD.remove();
        USER_FIELD.remove();
        USER_ID.remove();
    }
}