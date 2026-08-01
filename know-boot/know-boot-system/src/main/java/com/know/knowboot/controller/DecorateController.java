package com.know.knowboot.controller;

import com.alibaba.fastjson2.JSON;
import com.know.knowboot.core.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * 首页装修/主题/配置控制器
 */
@Api(tags = "首页装修管理")
@RestController
@RequestMapping("/api/index")
public class DecorateController {

    @Autowired
    private DataSource dataSource;

    @ApiOperation("获取装修配置")
    @GetMapping("/decorate")
    public AjaxResult getDecorate(@RequestParam(required = false, defaultValue = "5") Integer id) {
        if (id == 5) {
            // id=5: 主题配置 — themeStore.getTheme() 期望顶层字段 themeColor1, themeColor2 等
            Map<String, Object> theme = new HashMap<>();
            theme.put("themeColor1", "#6366f1");
            theme.put("themeColor2", "#919ef4");
            theme.put("buttonColor", "white");
            theme.put("navigationBarColor", "#6366f1");
            theme.put("topTextColor", "white");
            return AjaxResult.success(theme);
        }

        // id=2 (或其他非5): 页面装修数据
        // meta=页面设置JSON, data=组件配置JSON
        // 前端 user.vue 对 data.meta / data.data 做 JSON.parse
        Map<String, Object> result = new HashMap<>();

        // meta: 导航栏及页面样式设置 (JSON string)
        String meta = "[{\"content\": {\"title\": \"我的\", \"bg_type\": 1, \"bg_color\": \"#f5f5f5\", \"text_color\": 2, \"title_img\": \"\", \"title_type\": 1}}]";
        result.put("meta", meta);

        // data: 页面组件列表 (JSON string)
        // 参考E:\Ai-Master\know-uniapp-page\profile.html 设计,保留uni-app顶部圆角
        String data = "[{\"name\": \"user-info\", \"content\": {}, \"styles\": {}}," +
                "{\"name\": \"my-service\", \"content\": {" +
                "\"title\": \"我的服务\", \"style\": 1, \"data\": [" +
                "{\"name\": \"我的订单\", \"image\": \"\", \"link\": \"/pages/collection/collection\", \"is_show\": \"1\"}," +
                "{\"name\": \"我的收藏\", \"image\": \"\", \"link\": \"/pages/collection/collection\", \"is_show\": \"1\"}," +
                "{\"name\": \"消息通知\", \"image\": \"\", \"link\": \"/pages/news/news\", \"is_show\": \"1\"}," +
                "{\"name\": \"密码修改\", \"image\": \"\", \"link\": \"/pages/change_password/change_password\", \"is_show\": \"1\"}," +
                "{\"name\": \"绑定手机\", \"image\": \"\", \"link\": \"/pages/bind_mobile/bind_mobile\", \"is_show\": \"1\"}," +
                "{\"name\": \"关于我们\", \"image\": \"\", \"link\": \"/pages/as_us/as_us\", \"is_show\": \"1\"}," +
                "{\"name\": \"我的资料\", \"image\": \"\", \"link\": \"/pages/user_data/user_data\", \"is_show\": \"1\"}," +
                "{\"name\": \"联系客服\", \"image\": \"\", \"link\": \"\", \"is_show\": \"1\"}" +
                "]}, \"styles\": {}}," +
                "{\"name\": \"user-banner\", \"content\": {\"enabled\": true, \"data\": [" +
                "{\"image\": \"\", \"link\": \"\", \"is_show\": \"1\"}" +
                "]}, \"styles\": {}}]";
        result.put("data", data);

        return AjaxResult.success(result);
    }

    @ApiOperation("初始化计划管理菜单(免登录)")
    @GetMapping("/init-plan-menu")
    public AjaxResult initPlanMenu() {
        try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement()) {
            // 1. 创建 sys_admin 表（如不存在）
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS sys_admin (" +
                "  id int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID'," +
                "  username varchar(50) DEFAULT NULL COMMENT '账号'," +
                "  password varchar(100) DEFAULT NULL COMMENT '密码'," +
                "  name varchar(50) DEFAULT NULL COMMENT '昵称'," +
                "  avatar varchar(255) DEFAULT NULL COMMENT '头像'," +
                "  phone varchar(20) DEFAULT NULL COMMENT '手机号'," +
                "  email varchar(100) DEFAULT NULL COMMENT '邮箱'," +
                "  disable tinyint(1) DEFAULT 0 COMMENT '状态 0=正常 1=禁用'," +
                "  create_time datetime DEFAULT NULL COMMENT '创建时间'," +
                "  update_time datetime DEFAULT NULL COMMENT '更新时间'," +
                "  delete_time datetime DEFAULT NULL COMMENT '删除时间'," +
                "  remark varchar(500) DEFAULT NULL COMMENT '备注'," +
                "  PRIMARY KEY (id)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统管理员表'");
            
            // 2. 插入默认管理员（如不存在）
            stmt.executeUpdate(
                "INSERT IGNORE INTO sys_admin (id, username, password, name, disable, create_time) " +
                "VALUES (1, 'admin', '0192023a7bbd73250516f069df18b500', '系统管理员', 0, NOW())");
            
            // 3. 插入计划管理菜单
            stmt.executeUpdate(
                "INSERT IGNORE INTO sys_menu_config (menu_name, menu_code, menu_type, path, is_show, sort, render_type, create_time) " +
                "VALUES " +
                "('习惯管理', 'habit', 2, '/pages/plan/habit/index', 1, 5, 1, NOW())," +
                "('日程管理', 'schedule', 2, '/pages/plan/schedule/index', 1, 6, 1, NOW())");
            
            return AjaxResult.success("初始化成功: sys_admin表 + 计划管理菜单已添加");
        } catch (Exception e) {
            return AjaxResult.failed("初始化失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取网站配置")
    @GetMapping("/config")
    public AjaxResult getConfig() {
        Map<String, Object> config = new HashMap<>();

        // 网站信息
        Map<String, Object> website = new HashMap<>();
        website.put("shop_name", "喵百科");
        website.put("shop_logo", "/adminapi/static/shop_logo.png");
        website.put("shop_desc", "");
        website.put("h5_favicon", "/adminapi/static/web_favicon.ico");
        config.put("website", website);

        // 登录配置
        Map<String, Object> login = new HashMap<>();
        login.put("type", "password");
        login.put("is_force_login", 0);
        config.put("login", login);

        // 样式配置
        Map<String, Object> style = new HashMap<>();
        style.put("theme", "default");
        config.put("style", style);

        // H5配置
        Map<String, Object> webPage = new HashMap<>();
        webPage.put("title", "喵百科");
        config.put("webPage", webPage);

        // 其他
        config.put("copyright", new String[]{});
        config.put("domain", "");

        return AjaxResult.success(config);
    }
}
