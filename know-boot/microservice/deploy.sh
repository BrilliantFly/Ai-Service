#!/bin/bash
set -e

# ========================================================
# Ai-Project 一键部署脚本 (Linux/Git Bash)
# 服务器: 101.37.83.88
# 部署目录: /opt/ai-agent/apps
# 架构: Spring Boot 微服务 (system/knowledge/plan/camera)
# 前置条件: sshpass、java、maven、node、pnpm
# ========================================================

SSH_HOST="101.37.83.88"
SSH_USER="root"
SSH_PASS="Wanglei!@#123"
DEPLOY_DIR="/opt/ai-agent/apps"
JAVA_HOME="/opt/software/jdk1.8.0_201"
JAVA_BIN="$JAVA_HOME/bin/java"
NGINX_DIR="/opt/software/nginx-1.12.0"
NGINX_BIN="$NGINX_DIR/sbin/nginx"

# 当前脚本所在目录
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"

echo "========================================"
echo "  Ai-Project 打包部署脚本"
echo "  服务: system(8082) plan(8083)"
echo "         camera(8085) knowledge(8086)"
echo "========================================"

# ---------- 1. 构建后端 (4个微服务) ----------
echo ""
echo "[1/5] 构建后端 (know-boot)..."
cd "$SCRIPT_DIR/Ai-Service/know-boot"
mvn clean package -P prod -DskipTests -q
echo "  ✅ 后端构建完成"

# 定位 4 个 JAR
SYSTEM_JAR=$(ls know-boot-system/target/know-boot-system-*.jar | grep -v '\.original$' | head -1)
PLAN_JAR=$(ls know-boot-plan/target/know-boot-plan-*-execute.jar | head -1)
CAMERA_JAR=$(ls know-boot-camera/target/know-boot-camera-*.jar | grep -v '\.original$' | head -1)
KNOWLEDGE_JAR=$(ls know-boot-knowledge/target/know-boot-knowledge-*-execute.jar | head -1)

echo "  📦 $(basename $SYSTEM_JAR)"
echo "  📦 $(basename $PLAN_JAR)"
echo "  📦 $(basename $CAMERA_JAR)"
echo "  📦 $(basename $KNOWLEDGE_JAR)"

if [ -z "$SYSTEM_JAR" ] || [ -z "$PLAN_JAR" ] || [ -z "$CAMERA_JAR" ] || [ -z "$KNOWLEDGE_JAR" ]; then
    echo "❌ 未找到全部 JAR，请检查编译输出"
    exit 1
fi

# ---------- 2. 构建 Vue 管理后台 ----------
echo ""
echo "[2/5] 构建 Vue 管理后台 (know-vue)..."
cd "$SCRIPT_DIR/Ai-Front/know-vue"
npm install --silent 2>/dev/null || true
npm run build 2>/dev/null || npx vite build
echo "  ✅ Vue 构建完成 (dist/)"

# ---------- 3. 构建 UniApp H5 ----------
echo ""
echo "[3/5] 构建 UniApp H5 (移动端)..."
cd "$SCRIPT_DIR/Ai-Front/know-uniapp"
npm install --silent 2>/dev/null || true
npm run build:h5
echo "  ✅ UniApp H5 构建完成 (dist/build/h5/)"

# ---------- 4. 生成 Nginx 配置 ----------
echo ""
echo "[4/5] 生成 Nginx 配置..."
NGINX_CONF="/tmp/nginx-ai-agent.conf"
cat > "$NGINX_CONF" << 'NGINXEOF'
server {
    listen 80;
    server_name _;
    root /opt/ai-agent/apps/know-vue;
    index index.html;

    # ====== 后端微服务路由 ======

    # Knowledge 服务 (port 8086)
    location /api/knowledge/ {
        proxy_pass http://127.0.0.1:8086;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_read_timeout 120s;
    }

    # Plan 服务 (port 8083)
    location /api/plan/ {
        proxy_pass http://127.0.0.1:8083;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_read_timeout 120s;
    }

    # 其他 /api/* -> System 服务 (port 8082) — auth/user 等
    location /api/ {
        proxy_pass http://127.0.0.1:8082;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_read_timeout 120s;
    }

    # Admin API (system 8082)
    location /adminapi/ {
        proxy_pass http://127.0.0.1:8082/adminapi/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_read_timeout 120s;
    }

    # System 原生路由 (port 8082)
    location /system/ {
        proxy_pass http://127.0.0.1:8082/system/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_read_timeout 120s;
    }

    # Camera 服务 (port 8088)
    location /camera/ {
        rewrite ^/camera/(.*)$ /know-boot/$1 break;
        proxy_pass http://127.0.0.1:8088;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
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
        alias /opt/ai-agent/apps/know-mobile/;
        try_files $uri $uri/ /mobile/index.html;
    }

    # Vue 管理后台 SPA
    location / {
        try_files $uri $uri/ /index.html;
    }

    gzip on;
    gzip_types text/plain text/css application/json application/javascript text/xml application/xml text/javascript image/svg+xml;
    gzip_min_length 1k;
    gzip_comp_level 6;
    gzip_vary on;
}
NGINXEOF
echo "  ✅ Nginx 配置已生成"

# ---------- 5. 生成 Systemd Service 文件 ----------
echo ""
echo "[5/5] 生成 Systemd Service (4个)..."

LOG_DIR="/opt/ai-agent/apps/logs"

# System 服务 (8082)
cat > /tmp/know-boot-system.service << SERVEOF
[Unit]
Description=Ai-KnowBoot System Service (8082)
After=network.target

[Service]
Type=simple
User=root
WorkingDirectory=/opt/ai-agent/apps/backend
Environment=JAVA_HOME=$JAVA_HOME
ExecStart=$JAVA_BIN -jar /opt/ai-agent/apps/backend/know-boot-system.jar --spring.profiles.active=prod
ExecStop=/bin/kill -s QUIT \$MAINPID
SuccessExitStatus=143
Restart=always
RestartSec=10
StandardOutput=append:$LOG_DIR/know-boot-system.log
StandardError=append:$LOG_DIR/know-boot-system.err

[Install]
WantedBy=multi-user.target
SERVEOF

# Plan 服务 (8083)
cat > /tmp/know-boot-plan.service << SERVEOF
[Unit]
Description=Ai-KnowBoot Plan Service (8083)
After=network.target

[Service]
Type=simple
User=root
WorkingDirectory=/opt/ai-agent/apps/backend
Environment=JAVA_HOME=$JAVA_HOME
ExecStart=$JAVA_BIN -jar /opt/ai-agent/apps/backend/know-boot-plan.jar --spring.profiles.active=prod
ExecStop=/bin/kill -s QUIT \$MAINPID
SuccessExitStatus=143
Restart=always
RestartSec=10
StandardOutput=append:$LOG_DIR/know-boot-plan.log
StandardError=append:$LOG_DIR/know-boot-plan.err

[Install]
WantedBy=multi-user.target
SERVEOF

# Camera 服务 (8085)
cat > /tmp/know-boot-camera.service << SERVEOF
[Unit]
Description=Ai-KnowBoot Camera Service (8085)
After=network.target

[Service]
Type=simple
User=root
WorkingDirectory=/opt/ai-agent/apps/backend
Environment=JAVA_HOME=$JAVA_HOME
ExecStart=$JAVA_BIN -jar /opt/ai-agent/apps/backend/know-boot-camera.jar --spring.profiles.active=prod
ExecStop=/bin/kill -s QUIT \$MAINPID
SuccessExitStatus=143
Restart=always
RestartSec=10
StandardOutput=append:$LOG_DIR/know-boot-camera.log
StandardError=append:$LOG_DIR/know-boot-camera.err

[Install]
WantedBy=multi-user.target
SERVEOF

# Knowledge 服务 (8086)
cat > /tmp/know-boot-knowledge.service << SERVEOF
[Unit]
Description=Ai-KnowBoot Knowledge Service (8086)
After=network.target

[Service]
Type=simple
User=root
WorkingDirectory=/opt/ai-agent/apps/backend
Environment=JAVA_HOME=$JAVA_HOME
ExecStart=$JAVA_BIN -jar /opt/ai-agent/apps/backend/know-boot-knowledge.jar --spring.profiles.active=prod
ExecStop=/bin/kill -s QUIT \$MAINPID
SuccessExitStatus=143
Restart=always
RestartSec=10
StandardOutput=append:$LOG_DIR/know-boot-knowledge.log
StandardError=append:$LOG_DIR/know-boot-knowledge.err

[Install]
WantedBy=multi-user.target
SERVEOF

echo "  ✅ Systemd 配置已生成 (4个)"

# ========== 推送到远程服务器 ==========
echo ""
echo "========================================"
echo "  推送文件到远程服务器..."
echo "========================================"

SSH_CMD="sshpass -p '$SSH_PASS' ssh -o StrictHostKeyChecking=no -o ConnectTimeout=10 $SSH_USER@$SSH_HOST"
SCP_CMD="sshpass -p '$SSH_PASS' scp -o StrictHostKeyChecking=no -o ConnectTimeout=10"

# 远程创建目录
echo ""
echo ">> 创建远程目录..."
$SSH_CMD "mkdir -p $DEPLOY_DIR/backend $DEPLOY_DIR/know-vue $DEPLOY_DIR/know-mobile $DEPLOY_DIR/logs $DEPLOY_DIR/backend/uploads /var/log"

# 推送 4 个 JAR
echo ""
echo ">> 推送后端 JAR..."
$SCP_CMD "$SYSTEM_JAR"    $SSH_USER@$SSH_HOST:$DEPLOY_DIR/backend/know-boot-system.jar
$SCP_CMD "$PLAN_JAR"      $SSH_USER@$SSH_HOST:$DEPLOY_DIR/backend/know-boot-plan.jar
$SCP_CMD "$CAMERA_JAR"    $SSH_USER@$SSH_HOST:$DEPLOY_DIR/backend/know-boot-camera.jar
$SCP_CMD "$KNOWLEDGE_JAR" $SSH_USER@$SSH_HOST:$DEPLOY_DIR/backend/know-boot-knowledge.jar

# 推送 Vue 前端
echo ""
echo ">> 推送 Vue 前端..."
$SCP_CMD -r "$SCRIPT_DIR/Ai-Front/know-vue/dist/"* $SSH_USER@$SSH_HOST:$DEPLOY_DIR/know-vue/

# 推送 UniApp H5
echo ""
echo ">> 推送 UniApp H5..."
$SCP_CMD -r "$SCRIPT_DIR/Ai-Front/know-uniapp/dist/build/h5/"* $SSH_USER@$SSH_HOST:$DEPLOY_DIR/know-mobile/

# 推送 Nginx 配置
echo ""
echo ">> 推送 Nginx 配置..."
$SCP_CMD "$NGINX_CONF" $SSH_USER@$SSH_HOST:$NGINX_DIR/conf/ai-agent.conf

# 推送 4 个 Service 文件
echo ""
echo ">> 推送 Service 文件..."
$SCP_CMD /tmp/know-boot-system.service    $SSH_USER@$SSH_HOST:/etc/systemd/system/know-boot-system.service
$SCP_CMD /tmp/know-boot-plan.service      $SSH_USER@$SSH_HOST:/etc/systemd/system/know-boot-plan.service
$SCP_CMD /tmp/know-boot-camera.service    $SSH_USER@$SSH_HOST:/etc/systemd/system/know-boot-camera.service
$SCP_CMD /tmp/know-boot-knowledge.service $SSH_USER@$SSH_HOST:/etc/systemd/system/know-boot-knowledge.service

# ========== 远程部署 ==========
echo ""
echo "========================================"
echo "  远程服务器部署..."
echo "========================================"

$SSH_CMD bash -s << 'REMOTE'
set -e

DEPLOY_DIR="/opt/ai-agent/apps"
JAVA_BIN="/opt/software/jdk1.8.0_201/bin/java"
NGINX_BIN="/opt/software/nginx-1.12.0/sbin/nginx"

echo ""
echo ">> 1. 停止旧服务..."
for svc in know-boot-system know-boot-plan know-boot-camera know-boot-knowledge; do
    systemctl stop "$svc.service" 2>/dev/null || true
done

echo ""
echo ">> 2. 检查 Java..."
if [ ! -f "$JAVA_BIN" ]; then
    echo "  ❌ 未找到 Java: $JAVA_BIN"
    exit 1
fi
echo "  ✅ Java: $JAVA_BIN"

echo ""
echo ">> 3. 检查 Nginx..."
if [ ! -f "$NGINX_BIN" ]; then
    echo "  ❌ 未找到 Nginx: $NGINX_BIN"
    exit 1
fi
echo "  ✅ Nginx: $NGINX_BIN"

echo ""
echo ">> 4. 确保数据目录存在..."
mkdir -p /opt/ai-agent/apps/backend/uploads /opt/ai-agent/apps/logs /var/log

echo ""
echo ">> 5. 启动后端服务 (4个)..."
systemctl daemon-reload
for svc in know-boot-system know-boot-plan know-boot-camera know-boot-knowledge; do
    systemctl enable "$svc.service" 2>/dev/null
    systemctl start "$svc.service"
    echo "   启动 $svc ..."
done
echo "   ⏳ 等待 30s 启动..."
sleep 30

for svc in know-boot-system know-boot-plan know-boot-camera know-boot-knowledge; do
    if systemctl is-active --quiet "$svc.service"; then
        echo "   ✅ $svc 运行中"
    else
        echo "   ⚠️  $svc 启动异常，检查: journalctl -u $svc.service -n 50"
    fi
done

echo ""
echo ">> 6. 重启 Nginx..."
$NGINX_BIN -t 2>/dev/null && $NGINX_BIN -s reload 2>/dev/null || true
echo "   ✅ Nginx 已重载"

echo ""
echo "========================================"
echo "  ✅ 部署完成!"
echo "========================================"
echo "  Vue 管理后台:   http://$SSH_HOST/"
echo "  移动端 H5:      http://$SSH_HOST/mobile/"
echo "  API Gateway:    http://$SSH_HOST/api/"
echo "  System:8082  Plan:8083"
echo "  Camera:8085  Knowledge:8086"
echo "========================================"
REMOTE

# 清理临时文件
rm -f "$NGINX_CONF" /tmp/know-boot-system.service /tmp/know-boot-plan.service /tmp/know-boot-camera.service /tmp/know-boot-knowledge.service

echo ""
echo "========================================"
echo "  🎉 全部完成!"
echo "========================================"
