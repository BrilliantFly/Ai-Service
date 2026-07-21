package com.know.knowboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 菜单数据初始化器
 * 在摄像头模块启动时，自动将摄像头菜单数据插入 sys_menu_config 表
 * （因 System 模块的 Flyway 依赖被注释，改用此方式确保菜单数据存在）
 */
@Component
public class MenuDataInitializer {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void initCameraMenu() {
        try {
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM sys_menu_config WHERE menu_code = ?",
                Integer.class,
                "camera"
            );
            if (count != null && count == 0) {
                jdbcTemplate.update(
                    "INSERT INTO sys_menu_config (menu_name, menu_code, menu_type, path, is_show, sort, render_type, create_time) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, NOW())",
                    "摄像头", "camera", 2, "/pages/camera/index", 1, 4, 1
                );
                System.out.println("[MenuInit] 摄像头菜单数据已插入 sys_menu_config");
            } else {
                System.out.println("[MenuInit] 摄像头菜单数据已存在，跳过初始化");
            }
        } catch (Exception e) {
            System.err.println("[MenuInit] 摄像头菜单数据初始化失败: " + e.getMessage());
        }
    }
}
