@echo off
chcp 65001 >nul 2>&1
title Know-Boot Standalone Mode

set "KNOWBOOT_DIR=E:\Ai-Project\Ai-Service\know-boot"
set "VUE_DIR=E:\Ai-Project\Ai-Front\know-vue"
set "UNIAPP_DIR=E:\Ai-Project\Ai-Front\know-uniapp"

set "SYSTEM_JAR=%KNOWBOOT_DIR%\know-boot-system\target\know-boot-system-0.0.1-SNAPSHOT.jar"

set DB_URL=jdbc:mysql://101.37.83.88:3306/know_boot_v1?useUnicode=true^&characterEncoding=UTF-8^&autoReconnect=true^&useSSL=false^&serverTimezone=Asia/Shanghai^&allowPublicKeyRetrieval=true

echo.
echo ============================================================
echo   Know-Boot - Standalone Mode
echo   Starting: System(8082) + Vue(5173) + UniApp(8991)
echo ============================================================
echo.

REM === 1. Stop existing ===
echo [1/3] Stopping existing services...
taskkill /f /im java.exe >nul 2>&1
taskkill /f /im node.exe >nul 2>&1
ping 127.0.0.1 -n 3 >nul
echo       Done.
echo.

REM === 2. System (8082, standalone) ===
echo [2/3] Starting know-boot-system (port 8082)...
if not exist "%SYSTEM_JAR%" (
    echo [ERROR] JAR not found: %SYSTEM_JAR%
    echo   Build: cd %KNOWBOOT_DIR% ^&^& mvn clean package -DskipTests -pl know-boot-system -am
    pause
    exit /b 1
)

start "know-boot-system" /min java ^
    -Xms256m -Xmx512m -XX:MaxMetaspaceSize=128m ^
    -Dspring.profiles.active=dev ^
    "-Dspring.datasource.url=%DB_URL%" ^
    -Dspring.datasource.username=root ^
    -Dspring.datasource.password=123456aA@ ^
    -Dspring.redis.host=127.0.0.1 ^
    -Dspring.redis.port=6379 ^
    -Dspring.redis.database=6 ^
    -Dknow.mode=standalone ^
    -Dseata.enabled=false ^
    -Dfeign.circuitbreaker.enabled=false ^
    -Dfeign.loadbalancer.enabled=false ^
    "-Dspring.autoconfigure.exclude=com.alibaba.cloud.seata.feign.SeataFeignAutoConfiguration,com.alibaba.cloud.seata.feign.SeataFeignClientAutoConfiguration" ^
    -jar "%SYSTEM_JAR%"

ping 127.0.0.1 -n 9 >nul
echo.

REM === 3. Frontends ===
echo [3/3] Starting frontends...
start "know-vue" /min cmd /c "title know-vue & cd /d %VUE_DIR% && npx vite"
start "know-uniapp" /min cmd /c "title know-uniapp & cd /d %UNIAPP_DIR% && npm run dev:h5"

echo.
echo ============================================================
echo   All services started!
echo ============================================================
echo.
echo   Vue Admin:      http://localhost:5173/
echo   UniApp Mobile:  http://localhost:8991/mobile/
echo   System API:     http://localhost:8082/
echo   Camera API:     http://localhost:8082/api/camera/device/list  (via /api/)
echo.
echo   To stop all:    run standalone-stop.bat
echo ============================================================
echo.
pause
