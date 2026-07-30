@echo off
chcp 65001 >nul
title Know-Boot Camera (Standalone Mode)
cd /d E:\Ai-Project\Ai-Service\know-boot\know-boot-camera
echo ============================================
echo   Know-Boot Camera - Standalone Mode (Local)
echo ============================================
echo   Port: 8088
echo   Profile: standalone
echo ============================================
echo.
mvn spring-boot:run -Dspring-boot.run.profiles=standalone
pause