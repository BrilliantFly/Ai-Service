@echo off
chcp 65001 >nul 2>&1
title Ai-KnowBoot V2 - Server Deployment (System Only)

echo ============================================================
echo   Ai-KnowBoot V2 Deployment - System Only Mode
echo   Target Server: 101.37.83.88
echo ============================================================
echo.
echo This script will:
echo   1. Stop all existing services
echo   2. Deploy know-boot-system.jar to server
echo   3. Configure systemd service (standalone mode)
echo   4. Configure Nginx routing
echo   5. Start the service
echo.
echo Prerequisites:
echo   - node.js installed
echo   - ssh2 package (npm install ssh2)
echo.
pause

node "%~dp0deploy.js"
if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Deployment failed!
    pause
)
