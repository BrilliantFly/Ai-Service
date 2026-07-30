# ============================================================
#  Know-Boot Microservice Stop (微服务停止)
# ============================================================

Write-Host ""
Write-Host "============================================================" -ForegroundColor Cyan
Write-Host "  Stopping Know-Boot Microservices..." -ForegroundColor Cyan
Write-Host "============================================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "[1/2] Stopping Java services..." -ForegroundColor Yellow
$javaCount = @(Get-Process java -ErrorAction SilentlyContinue).Count
if ($javaCount -gt 0) {
    Get-Process java -ErrorAction SilentlyContinue | Stop-Process -Force
    Write-Host "       $javaCount Java process(es) stopped." -ForegroundColor Green
} else {
    Write-Host "       No Java processes found." -ForegroundColor Gray
}
Start-Sleep -Seconds 1

Write-Host "[2/2] Stopping Node frontends..." -ForegroundColor Yellow
$nodeCount = @(Get-Process node -ErrorAction SilentlyContinue).Count
if ($nodeCount -gt 0) {
    Get-Process node -ErrorAction SilentlyContinue | Stop-Process -Force
    Write-Host "       $nodeCount Node process(es) stopped." -ForegroundColor Green
} else {
    Write-Host "       No Node processes found." -ForegroundColor Gray
}

Write-Host ""
Write-Host "============================================================" -ForegroundColor Cyan
Write-Host "  All services stopped." -ForegroundColor Cyan
Write-Host "============================================================" -ForegroundColor Cyan
