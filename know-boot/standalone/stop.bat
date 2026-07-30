@echo off
chcp 65001 >nul 2>&1
title Know-Boot Stop

echo ============================================================
echo   Stopping Know-Boot Standalone Services...
echo ============================================================
echo.

echo [1/2] Stopping Java services (system, camera)...
taskkill /f /im java.exe >nul 2>&1
if %errorlevel% equ 0 ( echo       Java processes stopped. ) else ( echo       No Java processes found. )
ping 127.0.0.1 -n 2 >nul

echo [2/2] Stopping Node frontends (vue, uniapp)...
taskkill /f /im node.exe >nul 2>&1
if %errorlevel% equ 0 ( echo       Node processes stopped. ) else ( echo       No Node processes found. )

echo.
echo ============================================================
echo   All services stopped.
echo ============================================================
ping 127.0.0.1 -n 3 >nul
