package com.know.knowboot.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.entity.tenant.SysMenuConfig;
import com.know.knowboot.entity.tenant.SysUser;
import com.know.knowboot.mapper.tenant.SysMenuConfigMapper;
import com.know.knowboot.service.ISysMenuConfigService;
import com.know.knowboot.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 菜单配置服务实现
 */
@Service
public class SysMenuConfigServiceImpl extends ServiceImpl<SysMenuConfigMapper, SysMenuConfig> implements ISysMenuConfigService {

    @Autowired
    private SysMenuConfigMapper sysMenuConfigMapper;

    @Autowired
    private ISysUserService sysUserService;

    /**
     * 菜单类型常量
     */
    public static final Integer MENU_TYPE_TABBAR = 1;
    public static final Integer MENU_TYPE_HOME = 2;
    public static final Integer MENU_TYPE_SIDEBAR = 3;

    private static final Map<String, String> HOME_ICON_MAP = new HashMap<>();
    private static final Map<String, String> HOME_RENDER_CONFIG_MAP = new HashMap<>();

    static {
        HOME_ICON_MAP.put("customer", "static/images/home/customer.svg");
        HOME_ICON_MAP.put("crm", "static/images/home/customer.svg");
        HOME_ICON_MAP.put("collection", "static/images/home/collection.svg");
        HOME_ICON_MAP.put("favorite", "static/images/home/collection.svg");
        HOME_ICON_MAP.put("service", "static/images/home/service.svg");
        HOME_ICON_MAP.put("support", "static/images/home/service.svg");
        HOME_ICON_MAP.put("camera", "static/images/home/camera.svg");
        HOME_ICON_MAP.put("device", "static/images/home/camera.svg");
        HOME_ICON_MAP.put("monitor", "static/images/home/camera.svg");
        HOME_ICON_MAP.put("schedule", "static/images/home/schedule.svg");
        HOME_ICON_MAP.put("plan", "static/images/home/schedule.svg");
        HOME_ICON_MAP.put("calendar", "static/images/home/schedule.svg");
        HOME_ICON_MAP.put("habit", "static/images/home/habit.svg");
        HOME_ICON_MAP.put("checkin", "static/images/home/habit.svg");
        HOME_ICON_MAP.put("data", "static/images/home/data.svg");
        HOME_ICON_MAP.put("analysis", "static/images/home/data.svg");
        HOME_ICON_MAP.put("alert", "static/images/home/alert.svg");
        HOME_ICON_MAP.put("warning", "static/images/home/alert.svg");
        HOME_ICON_MAP.put("alarm", "static/images/home/alert.svg");
        HOME_ICON_MAP.put("finance", "static/images/home/export.svg");
        HOME_ICON_MAP.put("wallet", "static/images/home/export.svg");
        HOME_ICON_MAP.put("report", "static/images/home/export.svg");
        HOME_ICON_MAP.put("export", "static/images/home/export.svg");
        HOME_ICON_MAP.put("gantt", "static/images/home/gantt.svg");
        HOME_ICON_MAP.put("news", "static/images/home/data.svg");
        HOME_ICON_MAP.put("article", "static/images/home/data.svg");
        HOME_ICON_MAP.put("content", "static/images/home/data.svg");
        HOME_ICON_MAP.put("focus", "static/images/home/schedule.svg");

        HOME_RENDER_CONFIG_MAP.put("customer", "{\"sections\":[\"quick\",\"tool\"],\"title\":\"客户管理\",\"desc\":\"智能跟进提醒，高效维护客户关系\",\"tag\":\"客户\",\"thumbBg\":\"linear-gradient(135deg,#ede9fe,#ddd6fe)\",\"thumbImage\":\"/static/images/home/customer.svg\"}");
        HOME_RENDER_CONFIG_MAP.put("crm", HOME_RENDER_CONFIG_MAP.get("customer"));
        HOME_RENDER_CONFIG_MAP.put("collection", "{\"sections\":[\"quick\",\"tool\"],\"title\":\"我的收藏\",\"desc\":\"快速回到你保存过的内容与入口\",\"tag\":\"收藏\",\"thumbBg\":\"linear-gradient(135deg,#fff7ed,#fde68a)\",\"thumbImage\":\"/static/images/home/collection.svg\"}");
        HOME_RENDER_CONFIG_MAP.put("favorite", HOME_RENDER_CONFIG_MAP.get("collection"));
        HOME_RENDER_CONFIG_MAP.put("service", "{\"sections\":[\"quick\",\"tool\"],\"title\":\"在线客服支持\",\"desc\":\"快速联系平台客服获取帮助\",\"tag\":\"服务\",\"thumbBg\":\"linear-gradient(135deg,#d1fae5,#99f6e4)\",\"thumbImage\":\"/static/images/home/service.svg\"}");
        HOME_RENDER_CONFIG_MAP.put("support", HOME_RENDER_CONFIG_MAP.get("service"));
        HOME_RENDER_CONFIG_MAP.put("camera", "{\"sections\":[\"quick\",\"recommend\"],\"title\":\"智能摄像头接入指引\",\"desc\":\"快速了解如何配置和接入你的设备\",\"tag\":\"设备\",\"thumbBg\":\"linear-gradient(135deg,#e0e7ff,#c7d2fe)\",\"thumbImage\":\"/static/images/home/camera.svg\"}");
        HOME_RENDER_CONFIG_MAP.put("device", HOME_RENDER_CONFIG_MAP.get("camera"));
        HOME_RENDER_CONFIG_MAP.put("monitor", HOME_RENDER_CONFIG_MAP.get("camera"));
        HOME_RENDER_CONFIG_MAP.put("schedule", "{\"sections\":[\"quick\",\"recommend\",\"tool\"],\"title\":\"四象限工作法\",\"desc\":\"高效管理你的每日任务\",\"tag\":\"效率\",\"thumbBg\":\"linear-gradient(135deg,#d1fae5,#a7f3d0)\",\"thumbImage\":\"/static/images/home/schedule.svg\"}");
        HOME_RENDER_CONFIG_MAP.put("plan", HOME_RENDER_CONFIG_MAP.get("schedule"));
        HOME_RENDER_CONFIG_MAP.put("calendar", HOME_RENDER_CONFIG_MAP.get("schedule"));
        HOME_RENDER_CONFIG_MAP.put("habit", "{\"sections\":[\"quick\",\"recommend\"],\"title\":\"21 天习惯养成计划\",\"desc\":\"用科学方法培养持续力\",\"tag\":\"习惯\",\"thumbBg\":\"linear-gradient(135deg,#fce7f3,#fbcfe8)\",\"thumbImage\":\"/static/images/home/habit.svg\"}");
        HOME_RENDER_CONFIG_MAP.put("checkin", HOME_RENDER_CONFIG_MAP.get("habit"));
        HOME_RENDER_CONFIG_MAP.put("data", "{\"sections\":[\"quick\",\"tool\"],\"title\":\"数据分析\",\"desc\":\"设备与业务数据洞察\",\"tag\":\"数据\",\"thumbBg\":\"linear-gradient(135deg,#e0f2fe,#bae6fd)\",\"thumbImage\":\"/static/images/home/data.svg\"}");
        HOME_RENDER_CONFIG_MAP.put("analysis", HOME_RENDER_CONFIG_MAP.get("data"));
        HOME_RENDER_CONFIG_MAP.put("alert", "{\"sections\":[\"quick\",\"tool\"],\"title\":\"告警中心\",\"desc\":\"异常事件统一追踪与处理\",\"tag\":\"工具\",\"thumbBg\":\"linear-gradient(135deg,#fee2e2,#fecaca)\",\"thumbImage\":\"/static/images/home/alert.svg\"}");
        HOME_RENDER_CONFIG_MAP.put("warning", HOME_RENDER_CONFIG_MAP.get("alert"));
        HOME_RENDER_CONFIG_MAP.put("alarm", HOME_RENDER_CONFIG_MAP.get("alert"));
        HOME_RENDER_CONFIG_MAP.put("finance", "{\"sections\":[\"quick\",\"tool\"],\"title\":\"财务管理\",\"desc\":\"查看财务信息与收支概况\",\"tag\":\"财务\",\"thumbBg\":\"linear-gradient(135deg,#ede9fe,#ddd6fe)\",\"thumbImage\":\"/static/images/home/export.svg\"}");
        HOME_RENDER_CONFIG_MAP.put("wallet", HOME_RENDER_CONFIG_MAP.get("finance"));
        HOME_RENDER_CONFIG_MAP.put("report", "{\"sections\":[\"tool\"],\"title\":\"报表导出\",\"desc\":\"一键生成运营报告\",\"tag\":\"工具\",\"thumbBg\":\"linear-gradient(135deg,#ede9fe,#ddd6fe)\",\"thumbImage\":\"/static/images/home/export.svg\"}");
        HOME_RENDER_CONFIG_MAP.put("export", HOME_RENDER_CONFIG_MAP.get("report"));
        HOME_RENDER_CONFIG_MAP.put("gantt", "{\"sections\":[\"tool\"],\"title\":\"甘特图\",\"desc\":\"项目计划与进度追踪\",\"tag\":\"工具\",\"thumbBg\":\"linear-gradient(135deg,#d1fae5,#a7f3d0)\",\"thumbImage\":\"/static/images/home/gantt.svg\"}");
        HOME_RENDER_CONFIG_MAP.put("news", "{\"sections\":[\"quick\",\"recommend\"],\"title\":\"热门资讯\",\"desc\":\"及时了解平台资讯与行业动态\",\"tag\":\"资讯\",\"thumbBg\":\"linear-gradient(135deg,#dbeafe,#bfdbfe)\",\"thumbImage\":\"/static/images/home/data.svg\"}");
        HOME_RENDER_CONFIG_MAP.put("article", HOME_RENDER_CONFIG_MAP.get("news"));
        HOME_RENDER_CONFIG_MAP.put("content", HOME_RENDER_CONFIG_MAP.get("news"));
        HOME_RENDER_CONFIG_MAP.put("focus", "{\"sections\":[\"quick\"],\"title\":\"番茄专注\",\"desc\":\"开启一轮 25 分钟专注与休息\",\"tag\":\"专注\",\"thumbBg\":\"linear-gradient(135deg,#fca5a5,#f97316)\",\"thumbImage\":\"/static/images/home/schedule.svg\"}");
    }

    @Override
    public List<SysMenuConfig> listConfig(SysMenuConfig query, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<SysMenuConfig> wrapper = buildQueryWrapper(query);
        return page(new Page<>(pageNum, pageSize), wrapper).getRecords();
    }

    @Override
    public List<SysMenuConfig> listConfig(SysMenuConfig query) {
        return list(buildQueryWrapper(query));
    }

    @Override
    public List<SysMenuConfig> listByMenuType(Integer menuType) {
        List<SysMenuConfig> configs = sysMenuConfigMapper.selectByMenuType(menuType);
        return normalizeConfigs(configs, menuType);
    }

    @Override
    public List<SysMenuConfig> listByUserIdAndMenuType(Long userId, Integer menuType) {
        // 获取用户信息判断是否是超级管理员
        SysUser user = sysUserService.getById(userId);
        if (user != null && user.getId() == 1L) {
            // 超级管理员，返回所有可见菜单
            return normalizeConfigs(sysMenuConfigMapper.selectVisibleByMenuType(menuType), menuType);
        }
        // 普通用户，返回所有可见菜单（权限过滤在后续实现）
        return normalizeConfigs(sysMenuConfigMapper.selectVisibleByMenuType(menuType), menuType);
    }

    @Override
    public List<SysMenuConfig> listTabBar() {
        return listByMenuType(MENU_TYPE_TABBAR);
    }

    @Override
    public List<SysMenuConfig> listTabBarByUserId(Long userId) {
        return listByUserIdAndMenuType(userId, MENU_TYPE_TABBAR);
    }

    @Override
    public List<SysMenuConfig> listHomeMenu() {
        return listByMenuType(MENU_TYPE_HOME);
    }

    @Override
    public List<SysMenuConfig> listHomeMenuByUserId(Long userId) {
        return listByUserIdAndMenuType(userId, MENU_TYPE_HOME);
    }

    @Override
    public List<SysMenuConfig> listByUserId(Long userId) {
        // 返回用户所有可见菜单（tabBar + 首页）
        List<SysMenuConfig> tabBarList = listTabBarByUserId(userId);
        List<SysMenuConfig> homeList = listHomeMenuByUserId(userId);
        tabBarList.addAll(homeList);
        return tabBarList;
    }

    @Override
    public SysMenuConfig getConfigById(Long id) {
        return getById(id);
    }

    @Override
    public boolean addConfig(SysMenuConfig config) {
        config.setCreateBy(getCurrentUsername());
        config.setCreateTime(new Date());
        return save(config);
    }

    @Override
    public boolean updateConfig(SysMenuConfig config) {
        config.setUpdateBy(getCurrentUsername());
        config.setUpdateTime(new Date());
        return updateById(config);
    }

    @Override
    public boolean deleteConfig(Long id) {
        // 逻辑删除
        SysMenuConfig config = new SysMenuConfig();
        config.setId(id);
        config.setDelFlag(1);
        return updateById(config);
    }

    /**
     * 构建查询条件
     */
    private LambdaQueryWrapper<SysMenuConfig> buildQueryWrapper(SysMenuConfig query) {
        LambdaQueryWrapper<SysMenuConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(query.getMenuName() != null, SysMenuConfig::getMenuName, query.getMenuName())
                .like(query.getMenuCode() != null, SysMenuConfig::getMenuCode, query.getMenuCode())
                .eq(query.getMenuType() != null, SysMenuConfig::getMenuType, query.getMenuType())
                .eq(query.getIsShow() != null, SysMenuConfig::getIsShow, query.getIsShow())
                .orderByAsc(SysMenuConfig::getSort);
        return wrapper;
    }

    /**
     * 获取当前用户名
     */
    private String getCurrentUsername() {
        try {
            Object loginId = StpUtil.getLoginId();
            return loginId != null ? loginId.toString() : "system";
        } catch (Exception e) {
            return "system";
        }
    }

    private List<SysMenuConfig> normalizeConfigs(List<SysMenuConfig> configs, Integer menuType) {
        if (configs == null || configs.isEmpty()) {
            return configs;
        }
        if (!MENU_TYPE_HOME.equals(menuType)) {
            return configs;
        }
        List<SysMenuConfig> normalized = new ArrayList<>();
        for (SysMenuConfig config : configs) {
            normalized.add(normalizeHomeConfig(config));
        }
        return normalized;
    }

    private SysMenuConfig normalizeHomeConfig(SysMenuConfig config) {
        if (config == null || config.getMenuCode() == null) {
            return config;
        }
        String menuCode = config.getMenuCode().toLowerCase();
        String mappedIcon = HOME_ICON_MAP.get(menuCode);
        if (mappedIcon != null && !mappedIcon.isEmpty()) {
            config.setIcon(mappedIcon);
        }
        String renderConfig = HOME_RENDER_CONFIG_MAP.get(menuCode);
        if (renderConfig != null && !renderConfig.isEmpty()) {
            config.setRenderConfig(renderConfig);
        }
        return config;
    }
}
