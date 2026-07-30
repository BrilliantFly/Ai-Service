# ============================================================
#  Know-Boot - Microservice Mode (微服务模式)
#  启动: System(8082) + Plan(8083) + Knowledge(8086)
#        + Camera(8088) + Vue(5173) + UniApp(8991)
# ============================================================

$ErrorActionPreference = "Stop"
$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path

# Load shared config
. (Join-Path $ScriptDir "scripts-config.ps1")

Write-Host ""
Write-Host "============================================================" -ForegroundColor Cyan
Write-Host "  Know-Boot - Microservice Mode (微服务模式)" -ForegroundColor Cyan
Write-Host "  System(8082) + Plan(8083) + Knowledge(8086)" -ForegroundColor Cyan
Write-Host "  + Camera(8088) + Vue(5173) + UniApp(8991)" -ForegroundColor Cyan
Write-Host "============================================================" -ForegroundColor Cyan
Write-Host ""

# ---- 1. Kill existing ----
Write-Host "[1/6] Stopping existing services..." -ForegroundColor Yellow
Get-Process java, node -ErrorAction SilentlyContinue | Stop-Process -Force -ErrorAction SilentlyContinue
Start-Sleep -Seconds 2
Write-Host "       Done." -ForegroundColor Green
Write-Host ""

# ---- Helper to start a Java service ----
function Start-JavaService {
    param([string]$Name, [string]$Jar, [string]$Args, [string]$LogPrefix)
    if (-not (Test-Path $Jar)) {
        Write-Host "[ERROR] $Name JAR not found: $Jar" -ForegroundColor Red
        return $null
    }
    $outLog = "$env:TEMP\$LogPrefix-ms.log"
    $errLog = "$env:TEMP\$LogPrefix-ms.err"
    $p = Start-Process -FilePath "java" -ArgumentList $Args.Split(" ") -WindowStyle Hidden -PassThru `
        -RedirectStandardOutput $outLog -RedirectStandardError $errLog
    Write-Host "       PID: $($p.Id)" -ForegroundColor Green
    return $p
}

# ---- 2. System (8082) ----
Write-Host "[2/6] Starting know-boot-system (port 8082)..." -ForegroundColor Yellow
$sysArgs = "-Xms256m -Xmx512m -Dspring.profiles.active=dev -jar `"$SYSTEM_JAR`""
Start-JavaService "System" $SYSTEM_JAR $sysArgs "sys"
Start-Sleep -Seconds 5

# ---- 3. Plan (8083) ----
Write-Host "[3/6] Starting know-boot-plan (port 8083)..." -ForegroundColor Yellow
$planArgs = "-Xms128m -Xmx256m -Dspring.profiles.active=dev -jar `"$PLAN_JAR`""
Start-JavaService "Plan" $PLAN_JAR $planArgs "plan"
Start-Sleep -Seconds 3

# ---- 4. Knowledge (8086) ----
Write-Host "[4/6] Starting know-boot-knowledge (port 8086)..." -ForegroundColor Yellow
$knowArgs = "-Xms128m -Xmx256m -Dspring.profiles.active=dev -jar `"$KNOWLEDGE_JAR`""
Start-JavaService "Knowledge" $KNOWLEDGE_JAR $knowArgs "knowledge"
Start-Sleep -Seconds 3

# ---- 5. Camera (8088) ----
Write-Host "[5/6] Starting know-boot-camera (port 8088)..." -ForegroundColor Yellow
if (-not (Test-Path $CAMERA_JAR)) {
    Write-Host "  [WARN] Camera JAR not found. Building..." -ForegroundColor Yellow
    Push-Location $KNOWBOOT_DIR
    & mvn clean package -DskipTests -pl know-boot-camera -am -q
    Pop-Location
    if (-not (Test-Path $CAMERA_JAR)) {
        Write-Host "[ERROR] Camera JAR build failed." -ForegroundColor Red
        exit 1
    }
}
$cameraArgs = "-Xms128m -Xmx256m -Dspring.profiles.active=standalone -jar `"$CAMERA_JAR`""
Start-JavaService "Camera" $CAMERA_JAR $cameraArgs "camera"
Start-Sleep -Seconds 3

# ---- 6. Frontends ----
Write-Host "[6/6] Starting frontends..." -ForegroundColor Yellow
Start-Process -FilePath "cmd" -ArgumentList "/c", "title know-vue & cd /d `"$VUE_DIR`" && npx vite" -WindowStyle Normal
Start-Process -FilePath "cmd" -ArgumentList "/c", "title know-uniapp & cd /d `"$UNIAPP_DIR`" && npm run dev:h5" -WindowStyle Normal

# ---- Done ----
Write-Host ""
Write-Host "============================================================" -ForegroundColor Cyan
Write-Host "  All microservices started!" -ForegroundColor Cyan
Write-Host "============================================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "  Vue Admin:         http://localhost:5173/" -ForegroundColor White
Write-Host "  UniApp Mobile:     http://localhost:8991/mobile/" -ForegroundColor White
Write-Host "  System API:        http://localhost:8082/" -ForegroundColor White
Write-Host "  Plan API:          http://localhost:8083/" -ForegroundColor White
Write-Host "  Knowledge API:     http://localhost:8086/" -ForegroundColor White
Write-Host "  Camera API:        http://localhost:8088/know-boot/camera/device/list" -ForegroundColor White
Write-Host ""
Write-Host "  To stop all:       microservice-stop.ps1" -ForegroundColor Yellow
Write-Host "============================================================" -ForegroundColor Cyan
