# ========================================================
# Ai-Project 一键部署脚本 (PowerShell for Windows)
# 服务器: 101.37.83.88
# 部署目录: /opt/ai-agent/apps
# 架构: Spring Boot 微服务 (system/knowledge/plan/camera)
# 前置: Java / Maven / Node.js 已安装
# 推荐: 先在服务器配好 SSH 密钥免密登录
# ========================================================

param(
    [string]$Server = "101.37.83.88",
    [string]$User = "root",
    [string]$Password = "Wanglei!@#123",
    [string]$DeployDir = "/opt/ai-agent/apps",
    [string]$JavaHome = "/opt/software/jdk1.8.0_201",
    [string]$NginxDir = "/opt/software/nginx-1.12.0",
    [switch]$SkipBuild,
    [switch]$SkipUpload
)

$ProjectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Ai-Project 部署脚本 (PowerShell)" -ForegroundColor Cyan
Write-Host "  服务: system(8082) plan(8083) camera(8085) knowledge(8086)" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

# ---------- SSH/SCP 辅助函数 ----------
function Invoke-SSH {
    param([string]$Command)
    $sshArgs = @("-o StrictHostKeyChecking=no", "-o ConnectTimeout=10", "$User@$Server", $Command)
    & "ssh" @sshArgs 2>&1
}

function Invoke-SCP {
    param([string]$LocalPath, [string]$RemotePath)
    $scpArgs = @("-o StrictHostKeyChecking=no", "-o ConnectTimeout=10", "-r", $LocalPath, "${User}@${Server}:${RemotePath}")
    & "scp" @scpArgs 2>&1
}

# ---------- 1. 构建后端 (4个微服务) ----------
if (-not $SkipBuild) {
    Write-Host "`n[1/4] 构建后端 (know-boot: system/knowledge/plan/camera)..." -ForegroundColor Yellow
    Set-Location "$ProjectRoot\Ai-Service\know-boot"
    & "mvn" "clean", "package", "-P", "prod", "-DskipTests", "-q"
    if ($LASTEXITCODE -ne 0) { Write-Host "❌ 后端构建失败" -ForegroundColor Red; exit 1 }
    Write-Host "  ✅ 后端构建完成" -ForegroundColor Green

    # 定位 4 个 JAR
    $systemJar  = Get-ChildItem "know-boot-system\target\know-boot-system-*.jar"     | Where-Object { $_.Name -notlike '*.original' } | Select-Object -First 1
    $planJar    = Get-ChildItem "know-boot-plan\target\know-boot-plan-*-execute.jar"  | Select-Object -First 1
    $cameraJar  = Get-ChildItem "know-boot-camera\target\know-boot-camera-*.jar"      | Where-Object { $_.Name -notlike '*.original' } | Select-Object -First 1
    $knowledgeJar = Get-ChildItem "know-boot-knowledge\target\know-boot-knowledge-*-execute.jar" | Select-Object -First 1

    Write-Host "  📦 $($systemJar.Name)" -ForegroundColor Green
    Write-Host "  📦 $($planJar.Name)" -ForegroundColor Green
    Write-Host "  📦 $($cameraJar.Name)" -ForegroundColor Green
    Write-Host "  📦 $($knowledgeJar.Name)" -ForegroundColor Green

    if (-not $systemJar -or -not $planJar -or -not $cameraJar -or -not $knowledgeJar) {
        Write-Host "❌ 未找到全部 JAR，请检查编译输出" -ForegroundColor Red; exit 1
    }
} else {
    Write-Host "`n[1/4] 跳过构建" -ForegroundColor Yellow
    # 从 target 目录读取已有 JAR
    Set-Location "$ProjectRoot\Ai-Service\know-boot"
    $systemJar  = Get-ChildItem "know-boot-system\target\know-boot-system-*.jar"     | Where-Object { $_.Name -notlike '*.original' } | Select-Object -First 1
    $planJar    = Get-ChildItem "know-boot-plan\target\know-boot-plan-*-execute.jar"  | Select-Object -First 1
    $cameraJar  = Get-ChildItem "know-boot-camera\target\know-boot-camera-*.jar"      | Where-Object { $_.Name -notlike '*.original' } | Select-Object -First 1
    $knowledgeJar = Get-ChildItem "know-boot-knowledge\target\know-boot-knowledge-*-execute.jar" | Select-Object -First 1
}

# ---------- 2. 构建 Vue 管理后台 ----------
if (-not $SkipBuild) {
    Write-Host "`n[2/4] 构建 Vue 管理后台 (know-vue)..." -ForegroundColor Yellow
    Set-Location "$ProjectRoot\Ai-Front\know-vue"
    & "npm" "install" "--silent" 2>&1 | Out-Null
    & "npx" "vite" "build" 2>&1
    if ($LASTEXITCODE -ne 0) {
        Write-Host "  ❌ Vue 构建失败，尝试跳过 tsc 检查..." -ForegroundColor Yellow
        & "npx" "vite" "build" "--mode" "production" 2>&1
    }
    if ($LASTEXITCODE -ne 0) { Write-Host "❌ Vue 构建失败" -ForegroundColor Red; exit 1 }
    Write-Host "  ✅ Vue 构建完成" -ForegroundColor Green
} else {
    Write-Host "`n[2/4] 跳过构建" -ForegroundColor Yellow
}

# ---------- 3. 构建 UniApp H5 ----------
if (-not $SkipBuild) {
    Write-Host "`n[3/4] 构建 UniApp H5 (移动端)..." -ForegroundColor Yellow
    Set-Location "$ProjectRoot\Ai-Front\know-uniapp"
    & "npm" "install" "--silent" 2>&1 | Out-Null
    & "npm" "run" "build:h5" 2>&1
    if ($LASTEXITCODE -ne 0) { Write-Host "❌ UniApp 构建失败" -ForegroundColor Red; exit 1 }
    Write-Host "  ✅ UniApp H5 构建完成" -ForegroundColor Green
} else {
    Write-Host "`n[3/4] 跳过构建" -ForegroundColor Yellow
}

# ---------- 4. 推送 & 远程部署 ----------
if (-not $SkipUpload) {
    Write-Host "`n[4/4] 推送文件到远程服务器..." -ForegroundColor Yellow

    # SSH 连通性检查
    Write-Host "  >> 检查 SSH 连接..." -ForegroundColor Gray
    $testResult = Invoke-SSH "echo OK" 2>&1 | Out-String
    if ($testResult -notmatch 'OK') {
        Write-Host "  ⚠️  SSH 免密登录未生效，尝试 sshpass..." -ForegroundColor Yellow
        $sshpassTest = & "sshpass" "--version" 2>&1 | Out-String
        if ($sshpassTest -match 'sshpass') {
            function Invoke-SSH { param([string]$Command) & "sshpass" "-p" $Password "ssh" "-o StrictHostKeyChecking=no" "-o ConnectTimeout=10" "$User@$Server" $Command 2>&1 }
            function Invoke-SCP { param([string]$LocalPath, [string]$RemotePath) & "sshpass" "-p" $Password "scp" "-o StrictHostKeyChecking=no" "-o ConnectTimeout=10" "-r" $LocalPath "${User}@${Server}:${RemotePath}" 2>&1 }
            Write-Host "  ✅ 使用 sshpass 登录" -ForegroundColor Green
        } else {
            Write-Host "  ❌ 无法连接服务器。请手动执行以下操作之一：" -ForegroundColor Red
            Write-Host "     1. 生成 SSH key: ssh-keygen -t rsa -b 4096" -ForegroundColor Red
            Write-Host "     2. 复制到服务器: ssh-copy-id root@101.37.83.88" -ForegroundColor Red
            Write-Host "     3. 或安装 sshpass: choco install sshpass" -ForegroundColor Red
            exit 1
        }
    } else {
        Write-Host "  ✅ SSH 连接正常" -ForegroundColor Green
    }

    # 远程创建目录
    Write-Host "  >> 创建远程目录..."
    Invoke-SSH "mkdir -p $DeployDir/backend $DeployDir/know-vue $DeployDir/know-mobile $DeployDir/logs $DeployDir/backend/uploads /var/log" | Out-Null

    # 推送 4 个 JAR
    Write-Host "  >> 推送后端 JAR..."
    Invoke-SCP $systemJar.FullName    "$DeployDir/backend/know-boot-system.jar"
    Invoke-SCP $planJar.FullName      "$DeployDir/backend/know-boot-plan.jar"
    Invoke-SCP $cameraJar.FullName    "$DeployDir/backend/know-boot-camera.jar"
    Invoke-SCP $knowledgeJar.FullName "$DeployDir/backend/know-boot-knowledge.jar"

    # 推送 Vue 前端
    Write-Host "  >> 推送 Vue 前端..."
    Invoke-SCP "$ProjectRoot\Ai-Front\know-vue\dist\*" "$DeployDir/know-vue/"

    # 推送 UniApp H5
    Write-Host "  >> 推送 UniApp H5..."
    Invoke-SCP "$ProjectRoot\Ai-Front\know-uniapp\dist\build\h5\*" "$DeployDir/know-mobile/"

    # 推送 Nginx 配置（微服务多路由）
    Write-Host "  >> 推送 Nginx 配置..."
    $nginxConf = @"
server {
    listen 80;
    server_name _;
    root $DeployDir/know-vue;
    index index.html;

    # ====== 后端微服务路由 ======

    # Knowledge 服务 (port 8086) - /api/knowledge/*
    location /api/knowledge/ {
        proxy_pass http://127.0.0.1:8086;
        proxy_set_header Host `$host;
        proxy_set_header X-Real-IP `$remote_addr;
        proxy_set_header X-Forwarded-For `$proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto `$scheme;
        proxy_read_timeout 120s;
    }

    # Plan 服务 (port 8083) - /api/plan/*
    location /api/plan/ {
        proxy_pass http://127.0.0.1:8083;
        proxy_set_header Host `$host;
        proxy_set_header X-Real-IP `$remote_addr;
        proxy_set_header X-Forwarded-For `$proxy_add_x_forwarded_for;
        proxy_read_timeout 120s;
    }

    # 其他 /api/* 路由 -> System 服务 (port 8082) — 含 auth/user 等
    location /api/ {
        proxy_pass http://127.0.0.1:8082;
        proxy_set_header Host `$host;
        proxy_set_header X-Real-IP `$remote_addr;
        proxy_set_header X-Forwarded-For `$proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto `$scheme;
        proxy_read_timeout 120s;
    }

    # Admin API (system 8082)
    location /adminapi/ {
        proxy_pass http://127.0.0.1:8082/adminapi/;
        proxy_set_header Host `$host;
        proxy_set_header X-Real-IP `$remote_addr;
        proxy_set_header X-Forwarded-For `$proxy_add_x_forwarded_for;
        proxy_read_timeout 120s;
    }

    # System 原生路由 (port 8082)
    location /system/ {
        proxy_pass http://127.0.0.1:8082/system/;
        proxy_set_header Host `$host;
        proxy_set_header X-Real-IP `$remote_addr;
        proxy_set_header X-Forwarded-For `$proxy_add_x_forwarded_for;
        proxy_read_timeout 120s;
    }

    # Camera 服务 (port 8085)
    location /camera/ {
        proxy_pass http://127.0.0.1:8085;
        proxy_set_header Host `$host;
        proxy_set_header X-Real-IP `$remote_addr;
        proxy_set_header X-Forwarded-For `$proxy_add_x_forwarded_for;
        proxy_read_timeout 120s;
    }

    # 图片等静态资源
    location /uploads/ {
        alias /opt/ai-agent/apps/backend/uploads/;
        expires 7d;
        add_header Cache-Control "public, immutable";
    }

    # 移动端 H5 (UniApp)
    location /mobile/ {
        alias $DeployDir/know-mobile/;
        try_files `$uri `$uri/ /mobile/index.html;
    }

    # Vue 管理后台 SPA
    location / {
        try_files `$uri `$uri/ /index.html;
    }

    gzip on;
    gzip_types text/plain text/css application/json application/javascript text/xml application/xml text/javascript image/svg+xml;
    gzip_min_length 1k;
    gzip_comp_level 6;
    gzip_vary on;
}
"@
    # 通过 SSH 写入 nginx 配置
    Invoke-SSH "cat > $NginxDir/conf/ai-agent.conf << 'NGINXEOF'
$nginxConf
NGINXEOF" | Out-Null

    # 推送 4 个 Systemd 服务文件
    Write-Host "  >> 推送 Systemd 服务文件..."

    $JAVA_BIN = "$JavaHome/bin/java"

    $LOG_DIR = "$DeployDir/logs"

    # System 服务 (8082)
    $svcSystem = @"
[Unit]
Description=Ai-KnowBoot System Service (8082)
After=network.target

[Service]
Type=simple
User=root
WorkingDirectory=$DeployDir/backend
Environment=JAVA_HOME=$JavaHome
ExecStart=$JAVA_BIN -jar $DeployDir/backend/know-boot-system.jar --spring.profiles.active=prod
ExecStop=/bin/kill -s QUIT `$MAINPID
SuccessExitStatus=143
Restart=always
RestartSec=10
StandardOutput=append:$LOG_DIR/know-boot-system.log
StandardError=append:$LOG_DIR/know-boot-system.err

[Install]
WantedBy=multi-user.target
"@

    # Plan 服务 (8083)
    $svcPlan = @"
[Unit]
Description=Ai-KnowBoot Plan Service (8083)
After=network.target

[Service]
Type=simple
User=root
WorkingDirectory=$DeployDir/backend
Environment=JAVA_HOME=$JavaHome
ExecStart=$JAVA_BIN -jar $DeployDir/backend/know-boot-plan.jar --spring.profiles.active=prod
ExecStop=/bin/kill -s QUIT `$MAINPID
SuccessExitStatus=143
Restart=always
RestartSec=10
StandardOutput=append:$LOG_DIR/know-boot-plan.log
StandardError=append:$LOG_DIR/know-boot-plan.err

[Install]
WantedBy=multi-user.target
"@

    # Camera 服务 (8085)
    $svcCamera = @"
[Unit]
Description=Ai-KnowBoot Camera Service (8085)
After=network.target

[Service]
Type=simple
User=root
WorkingDirectory=$DeployDir/backend
Environment=JAVA_HOME=$JavaHome
ExecStart=$JAVA_BIN -jar $DeployDir/backend/know-boot-camera.jar --spring.profiles.active=prod
ExecStop=/bin/kill -s QUIT `$MAINPID
SuccessExitStatus=143
Restart=always
RestartSec=10
StandardOutput=append:$LOG_DIR/know-boot-camera.log
StandardError=append:$LOG_DIR/know-boot-camera.err

[Install]
WantedBy=multi-user.target
"@

    # Knowledge 服务 (8086)
    $svcKnowledge = @"
[Unit]
Description=Ai-KnowBoot Knowledge Service (8086)
After=network.target

[Service]
Type=simple
User=root
WorkingDirectory=$DeployDir/backend
Environment=JAVA_HOME=$JavaHome
ExecStart=$JAVA_BIN -jar $DeployDir/backend/know-boot-knowledge.jar --spring.profiles.active=prod
ExecStop=/bin/kill -s QUIT `$MAINPID
SuccessExitStatus=143
Restart=always
RestartSec=10
StandardOutput=append:$LOG_DIR/know-boot-knowledge.log
StandardError=append:$LOG_DIR/know-boot-knowledge.err

[Install]
WantedBy=multi-user.target
"@

    Invoke-SSH "cat > /etc/systemd/system/know-boot-system.service << 'SERVEOF'
$svcSystem
SERVEOF" | Out-Null
    Invoke-SSH "cat > /etc/systemd/system/know-boot-plan.service << 'SERVEOF'
$svcPlan
SERVEOF" | Out-Null
    Invoke-SSH "cat > /etc/systemd/system/know-boot-camera.service << 'SERVEOF'
$svcCamera
SERVEOF" | Out-Null
    Invoke-SSH "cat > /etc/systemd/system/know-boot-knowledge.service << 'SERVEOF'
$svcKnowledge
SERVEOF" | Out-Null

    $JAVA_BIN = "$JavaHome/bin/java"
    $NGINX_BIN = "$NginxDir/sbin/nginx"

    # ========== 远程部署命令 ==========
    Write-Host "`n  >> 远程部署..." -ForegroundColor Yellow
    $remoteCommands = @"
set -e

JAVA_BIN="$JAVA_BIN"
NGINX_BIN="$NGINX_BIN"

echo '  >> 停止旧服务...'
for svc in know-boot-system know-boot-plan know-boot-camera know-boot-knowledge; do
    systemctl stop "$svc.service" 2>/dev/null || true
done

echo '  >> 检查 Java...'
if [ ! -f "`$JAVA_BIN" ]; then
    echo "  ❌ 未找到 Java: `$JAVA_BIN"
    exit 1
fi
echo "  ✅ Java: `$JAVA_BIN"

echo '  >> 检查 Nginx...'
if [ ! -f "`$NGINX_BIN" ]; then
    echo "  ❌ 未找到 Nginx: `$NGINX_BIN"
    exit 1
fi
echo "  ✅ Nginx: `$NGINX_BIN"

echo '  >> 启动后端服务...'
systemctl daemon-reload
for svc in know-boot-system know-boot-plan know-boot-camera know-boot-knowledge; do
    systemctl enable "$svc.service" 2>/dev/null
    systemctl start "$svc.service"
    echo "  启动 $svc ..."
done
echo '  ⏳ 等待 30s 启动...'
sleep 30

for svc in know-boot-system know-boot-plan know-boot-camera know-boot-knowledge; do
    if systemctl is-active --quiet "$svc.service"; then
        echo "  ✅ $svc 运行中"
    else
        echo "  ⚠️  $svc 启动异常, 检查: journalctl -u $svc.service -n 30"
    fi
done

echo '  >> 重载 Nginx...'
`$NGINX_BIN -t 2>/dev/null && `$NGINX_BIN -s reload 2>/dev/null || true
echo '  ✅ Nginx 已重载'

echo ''
echo '========================================'
echo '  ✅ 部署完成!'
echo '========================================'
echo '  Vue 管理后台:   http://$Server/'
echo '  移动端 H5:      http://$Server/mobile/'
echo '  API Gateway:    http://$Server/api/'
echo '  System:8082  Plan:8083'
echo '  Camera:8085  Knowledge:8086'
echo '========================================'
"@
    Invoke-SSH "$remoteCommands"
} else {
    Write-Host "`n[4/4] 跳过推送" -ForegroundColor Yellow
}

Set-Location $ProjectRoot
Write-Host "`n🎉 脚本执行完毕!" -ForegroundColor Cyan
