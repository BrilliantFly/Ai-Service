@echo off
chcp 65001 >nul
echo ============================================
echo   Know-Boot 服务快速启动脚本
echo ============================================
echo.

:: 停止已有进程
echo [1/6] 停止已有进程...
taskkill /f /im java.exe 2>nul
timeout /t 2 /nobreak >nul

:: 启动 system 模块
echo [2/6] 启动 know-boot-system (端口 8082)...
cd /d E:\Ai-Project\Ai-Service\know-boot
start "know-boot-system" /min java -jar know-boot-system\target\know-boot-system-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev

:: 启动 plan 模块
echo [3/6] 启动 know-boot-plan (端口 8083)...
start "know-boot-plan" /min java -jar know-boot-plan\target\know-boot-plan-0.0.1-SNAPSHOT-execute.jar --spring.profiles.active=dev

:: 启动 knowledge 模块
echo [4/7] 启动 know-boot-knowledge (端口 8086)...
start "know-boot-knowledge" /min java -jar know-boot-knowledge\target\know-boot-knowledge-0.0.1-SNAPSHOT-execute.jar --spring.profiles.active=dev

:: 启动 camera 模块
echo [5/7] 启动 know-boot-camera (端口 8088, standalone)...
cd /d E:\Ai-Project\Ai-Service\know-boot\know-boot-camera
start "know-boot-camera" /min cmd /c "mvn spring-boot:run -Dspring-boot.run.profiles=standalone"

:: 启动 uniapp
echo [6/7] 启动 know-uniapp (端口 8991)...
cd /d E:\Ai-Project\Ai-Front\know-uniapp
start "know-uniapp" /min cmd /c "npm run dev:h5"

:: 启动 know-vue
echo [7/7] 启动 know-vue (端口 5173)...
cd /d E:\Ai-Project\Ai-Front\know-vue
start "know-vue" /min cmd /c "npx vite"

echo.
echo ============================================
echo   所有服务已启动！
echo ============================================
echo.
echo   访问地址:
echo   - uniapp:    http://localhost:8991/mobile/
echo   - 管理后台:  http://localhost:5173/
echo   - system API: http://localhost:8082/
echo   - camera API: http://localhost:8088/know-boot/camera/device/list
echo   - plan API:   http://localhost:8083/
echo   - knowledge API: http://localhost:8086/
echo   - Swagger UI: http://localhost:8086/swagger-ui.html
echo.
echo   按任意键退出...
pause >nul
