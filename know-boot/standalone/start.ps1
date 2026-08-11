# ============================================================
#  Know-Boot - Standalone Mode (单机模式)
#  启动: System(8082) + Vue(5173) + UniApp(8991)  (Camera is in-system)
# ============================================================

$ErrorActionPreference = "Stop"
$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path

# ---- Config ----
$KNOWBOOT_DIR = "E:\Ai-Project\Ai-Service\know-boot"
$VUE_DIR = "E:\Ai-Project\Ai-Front\know-vue"
$UNIAPP_DIR = "E:\Ai-Project\Ai-Front\know-uniapp"
$SYSTEM_JAR = "$KNOWBOOT_DIR\know-boot-system\target\know-boot-system-0.0.1-SNAPSHOT.jar"
$DB_URL = "jdbc:mysql://101.37.83.88:3306/know_boot_v1?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true"
$DB_USER = "root"
$DB_PASS = "123456aA@"
$REDIS_HOST = "127.0.0.1"
$REDIS_PORT = "16379"
$REDIS_DB = "6"

Write-Host ""
Write-Host "============================================================" -ForegroundColor Cyan
Write-Host "  Know-Boot - Standalone Mode (单机模式)" -ForegroundColor Cyan
Write-Host "  System(8082) + Vue(5173) + UniApp(8991)" -ForegroundColor Cyan
Write-Host "============================================================" -ForegroundColor Cyan
Write-Host ""

# ---- 1. Kill existing ----
Write-Host "[1/3] Stopping existing services..." -ForegroundColor Yellow
Get-Process java, node -ErrorAction SilentlyContinue | Stop-Process -Force -ErrorAction SilentlyContinue
Start-Sleep -Seconds 2
Write-Host "       Done." -ForegroundColor Green
Write-Host ""

# ---- 2. System (8082) ----
Write-Host "[2/3] Starting know-boot-system (port 8082)..." -ForegroundColor Yellow
if (-not (Test-Path $SYSTEM_JAR)) {
    Write-Host "[ERROR] JAR not found: $SYSTEM_JAR" -ForegroundColor Red
    Write-Host "  Build first: mvn clean package -DskipTests -pl know-boot-system -am" -ForegroundColor Yellow
    exit 1
}

$systemArgs = @(
    "-Xms256m", "-Xmx512m", "-XX:MaxMetaspaceSize=128m"
    "-Dspring.profiles.active=dev"
    "-Dspring.datasource.url=$DB_URL"
    "-Dspring.datasource.username=$DB_USER"
    "-Dspring.datasource.password=$DB_PASS"
    "-Dspring.redis.host=$REDIS_HOST"
    "-Dspring.redis.port=$REDIS_PORT"
    "-Dspring.redis.database=$REDIS_DB"
    "-Dknow.mode=standalone"
    "-Dseata.enabled=false"
    "-Dfeign.circuitbreaker.enabled=false"
    "-Dfeign.loadbalancer.enabled=false"
    "-Dspring.autoconfigure.exclude=com.alibaba.cloud.seata.feign.SeataFeignAutoConfiguration,com.alibaba.cloud.seata.feign.SeataFeignClientAutoConfiguration"
    "-jar", "`"$SYSTEM_JAR`""
)
$sysJob = Start-Process -FilePath "java" -ArgumentList $systemArgs -WindowStyle Hidden -PassThru -RedirectStandardOutput "$env:TEMP\sys-standalone.log" -RedirectStandardError "$env:TEMP\sys-standalone.err"
Write-Host "       PID: $($sysJob.Id)" -ForegroundColor Green
Start-Sleep -Seconds 8

# ---- 3. Frontends ----
Write-Host "[3/3] Starting frontends..." -ForegroundColor Yellow
Start-Process -FilePath "cmd" -ArgumentList "/c", "title know-vue & cd /d `"$VUE_DIR`" && npx vite" -WindowStyle Hidden
Start-Process -FilePath "cmd" -ArgumentList "/c", "title know-uniapp & cd /d `"$UNIAPP_DIR`" && npm run dev:h5" -WindowStyle Hidden

# ---- Done ----
Write-Host ""
Write-Host "============================================================" -ForegroundColor Cyan
Write-Host "  All services started!" -ForegroundColor Cyan
Write-Host "============================================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "  Vue Admin:      http://localhost:5173/" -ForegroundColor White
Write-Host "  UniApp Mobile:  http://localhost:8991/mobile/" -ForegroundColor White
Write-Host "  System API:     http://localhost:8082/" -ForegroundColor White
Write-Host "  Camera API:     http://localhost:8082/api/camera/device/list" -ForegroundColor White
Write-Host ""
Write-Host "  To stop all:    standalone-stop.ps1" -ForegroundColor Yellow
Write-Host "============================================================" -ForegroundColor Cyan
