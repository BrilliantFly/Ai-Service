@echo off
chcp 65001 >nul 2>&1
title Know-Boot Microservice Mode

set "KNOWBOOT_DIR=E:\Ai-Project\Ai-Service\know-boot"
set "VUE_DIR=E:\Ai-Project\Ai-Front\know-vue"
set "UNIAPP_DIR=E:\Ai-Project\Ai-Front\know-uniapp"

set "SYSTEM_JAR=%KNOWBOOT_DIR%\know-boot-system\target\know-boot-system-0.0.1-SNAPSHOT.jar"
set "PLAN_JAR=%KNOWBOOT_DIR%\know-boot-plan\target\know-boot-plan-0.0.1-SNAPSHOT-execute.jar"
set "KNOWLEDGE_JAR=%KNOWBOOT_DIR%\know-boot-knowledge\target\know-boot-knowledge-0.0.1-SNAPSHOT-execute.jar"
set "CAMERA_JAR=%KNOWBOOT_DIR%\know-boot-camera\target\know-boot-camera-0.0.1-SNAPSHOT-execute.jar"

echo.
echo ============================================================
echo   Know-Boot - Microservice Mode
echo   Starting: System(8082) + Plan(8083) + Knowledge(8086)
echo            + Camera(8088) + Vue(5173) + UniApp(8991)
echo ============================================================
echo.

REM === 1. Stop existing ===
echo [1/6] Stopping existing services...
taskkill /f /im java.exe >nul 2>&1
taskkill /f /im node.exe >nul 2>&1
ping 127.0.0.1 -n 3 >nul
echo       Done.
echo.

REM === 2. System (8082) ===
echo [2/6] Starting know-boot-system (port 8082)...
if not exist "%SYSTEM_JAR%" (
    echo [ERROR] JAR not found: %SYSTEM_JAR%
    pause
    exit /b 1
)
start "know-boot-system" /min java -Xms256m -Xmx512m -Dspring.profiles.active=dev -jar "%SYSTEM_JAR%"
ping 127.0.0.1 -n 6 >nul
echo.

REM === 3. Plan (8083) ===
echo [3/6] Starting know-boot-plan (port 8083)...
if not exist "%PLAN_JAR%" (
    echo [ERROR] JAR not found: %PLAN_JAR%
    pause
    exit /b 1
)
start "know-boot-plan" /min java -Xms128m -Xmx256m -Dspring.profiles.active=dev -jar "%PLAN_JAR%"
ping 127.0.0.1 -n 4 >nul
echo.

REM === 4. Knowledge (8086) ===
echo [4/6] Starting know-boot-knowledge (port 8086)...
if not exist "%KNOWLEDGE_JAR%" (
    echo [ERROR] JAR not found: %KNOWLEDGE_JAR%
    pause
    exit /b 1
)
start "know-boot-knowledge" /min java -Xms128m -Xmx256m -Dspring.profiles.active=dev -jar "%KNOWLEDGE_JAR%"
ping 127.0.0.1 -n 4 >nul
echo.

REM === 5. Camera (8088, JAR) ===
echo [5/6] Starting know-boot-camera (port 8088)...
if not exist "%CAMERA_JAR%" (
    echo [WARN] Camera JAR not found. Building now...
    pushd "%KNOWBOOT_DIR%"
    call mvn clean package -DskipTests -pl know-boot-camera -am -q
    popd
    if not exist "%CAMERA_JAR%" (
        echo [ERROR] Camera JAR build failed.
        pause
        exit /b 1
    )
)
start "know-boot-camera" /min java -Xms128m -Xmx256m -Dspring.profiles.active=standalone -jar "%CAMERA_JAR%"
ping 127.0.0.1 -n 4 >nul
echo.

REM === 6. Frontends ===
echo [6/6] Starting frontends...
start "know-vue" /min cmd /c "title know-vue & cd /d %VUE_DIR% && npx vite"
start "know-uniapp" /min cmd /c "title know-uniapp & cd /d %UNIAPP_DIR% && npm run dev:h5"

echo.
echo ============================================================
echo   All microservices started!
echo ============================================================
echo.
echo   Vue Admin:         http://localhost:5173/
echo   UniApp Mobile:     http://localhost:8991/mobile/
echo   System API:        http://localhost:8082/
echo   Plan API:          http://localhost:8083/
echo   Knowledge API:     http://localhost:8086/
echo   Camera API:        http://localhost:8088/know-boot/camera/device/list
echo.
echo   To stop all:       run microservice-stop.bat
echo ============================================================
echo.
pause
