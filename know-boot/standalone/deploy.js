/**
 * Ai-KnowBoot V2 Server Deployment Script
 * 
 * Deploys ONLY know-boot-system.jar in standalone mode.
 * System serves: auth, user, role, menu, dept, tenant, logs
 * Plan/Camera/Knowledge services are NOT deployed (by design).
 * 
 * Usage: node deploy-v2-server.js
 * 
 * Prerequisites:
 *   npm install ssh2
 */

const { Client } = require('ssh2');

// ============ CONFIG ============
const SERVER = {
  host: '101.37.83.88',
  port: 22,
  username: 'root',
  password: 'Wanglei!@#123',
};

const DEPLOY = {
  jarLocal: __dirname + '\\..\\know-boot-system\\target\\know-boot-system-0.0.1-SNAPSHOT.jar',
  jarRemote: '/opt/ai-agent/apps/backend/know-boot-system.jar',
  wrapperScript: '/opt/ai-agent/apps/backend/start-system.sh',
  serviceUnit: '/etc/systemd/system/know-boot-system.service',
  nginxConfig: '/etc/nginx/conf.d/default.conf',
};

const JVM = {
  heap: '-Xms256m -Xmx512m',
  metaspace: '-XX:MaxMetaspaceSize=128m',
  java: '/usr/bin/java',
};

const DB = {
  host: '127.0.0.1',
  port: 3306,
  name: 'know_boot_v1',
  user: 'root',
  pass: '123456aA@',
};

const REDIS = {
  host: '127.0.0.1',
  port: 6379,
  database: 1,
};
// ============ END CONFIG ============

function sshExec(conn, cmd) {
  return new Promise((resolve, reject) => {
    conn.exec(cmd, (err, stream) => {
      if (err) return reject(err);
      let out = '';
      stream.on('data', d => { out += d.toString(); process.stdout.write(d.toString()); });
      stream.stderr.on('data', d => { out += d.toString(); process.stdout.write(d.toString()); });
      stream.on('close', () => resolve(out));
    });
  });
}

function sftpWrite(conn, remotePath, content) {
  return new Promise((resolve, reject) => {
    conn.sftp((err, sftp) => {
      if (err) return reject(err);
      const ws = sftp.createWriteStream(remotePath);
      ws.on('close', resolve);
      ws.on('error', reject);
      ws.end(content);
    });
  });
}

function sftpUpload(conn, localPath, remotePath) {
  return new Promise((resolve, reject) => {
    conn.sftp((err, sftp) => {
      if (err) return reject(err);
      const ws = sftp.createWriteStream(remotePath);
      ws.on('close', resolve);
      ws.on('error', reject);
      const fs = require('fs');
      fs.createReadStream(localPath).pipe(ws);
    });
  });
}

const WRAPPER_SCRIPT = `#!/bin/bash
cd /opt/ai-agent/apps/backend
exec ${JVM.java} \\
  ${JVM.heap} \\
  ${JVM.metaspace} \\
  -Dspring.profiles.active=prod \\
  "-Dspring.datasource.url=jdbc:mysql://${DB.host}:${DB.port}/${DB.name}?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true" \\
  -Dspring.datasource.username=${DB.user} \\
  -Dspring.datasource.password=${DB.pass} \\
  -Dspring.redis.host=${REDIS.host} \\
  -Dspring.redis.port=${REDIS.port} \\
  -Dspring.redis.database=${REDIS.database} \\
  -Dseata.enabled=false \\
  -Dknow.mode=standalone \\
  -Dfeign.circuitbreaker.enabled=false \\
  -Dfeign.loadbalancer.enabled=false \\
  "-Dspring.autoconfigure.exclude=com.alibaba.cloud.seata.feign.SeataFeignAutoConfiguration,com.alibaba.cloud.seata.feign.SeataFeignClientAutoConfiguration" \\
  -Dmybatis-plus.global-config.db-config.table-prefix="" \\
  -Dmybatis-plus.global-config.db-config.id-type=auto \\
  -Dmybatis-plus.global-config.db-config.logic-delete-field=deleted \\
  -Dmybatis-plus.global-config.db-config.logic-delete-value=1 \\
  -Dmybatis-plus.global-config.db-config.logic-not-delete-value=0 \\
  -jar know-boot-system.jar \\
  "$@"`;

const SERVICE_UNIT = `[Unit]
Description=Know Boot System Service (Standalone Mode)
After=network.target

[Service]
Type=simple
User=root
WorkingDirectory=/opt/ai-agent/apps/backend
ExecStart=${DEPLOY.wrapperScript}
Restart=always
RestartSec=30
SuccessExitStatus=143

[Install]
WantedBy=multi-user.target`;

const NGINX_CONFIG = `server {
    listen 80;
    server_name _;

    client_max_body_size 100M;
    client_body_buffer_size 128k;

    location / {
        root /opt/ai-agent/apps/know-vue;
        index index.html;
        try_files $uri $uri/ /index.html;
    }

    location /api/ {
        proxy_pass http://127.0.0.1:8082/api/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_connect_timeout 60s;
        proxy_read_timeout 120s;
    }

    location /adminapi/ {
        proxy_pass http://127.0.0.1:8082/adminapi/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    location /system/ {
        proxy_pass http://127.0.0.1:8082/system/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    location /tenant/ {
        proxy_pass http://127.0.0.1:8082/tenant/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    location /uploads/ {
        alias /opt/ai-agent/apps/backend/uploads/;
    }

    location /mobile/ {
        alias /opt/ai-agent/apps/know-mobile/;
        index index.html;
        try_files $uri $uri/ /mobile/index.html;
    }

    error_page 500 502 503 504 /50x.html;
    location = /50x.html {
        root /usr/share/nginx/html;
    }
}`;

async function main() {
  const fs = require('fs');
  const conn = new Client();

  // Check local JAR
  if (!fs.existsSync(DEPLOY.jarLocal)) {
    console.error('[ERROR] JAR not found:', DEPLOY.jarLocal);
    console.error('Run: cd know-boot-system && mvn clean package -DskipTests');
    process.exit(1);
  }
  console.log('JAR found:', (fs.statSync(DEPLOY.jarLocal).size / 1024 / 1024).toFixed(0) + 'MB');

  // Connect
  console.log('\nConnecting to', SERVER.host, '...');
  await new Promise((resolve, reject) => {
    conn.on('ready', resolve).on('error', reject);
    conn.connect(SERVER);
  });
  console.log('Connected.\n');

  // Step 1: Stop all services
  console.log('=== [1/7] Stop existing services ===');
  await sshExec(conn, 'systemctl stop know-boot-system know-boot-plan know-boot-camera know-boot-knowledge 2>&1; pkill -f "know-boot" 2>&1 || true; sleep 2; echo "Stopped"');

  // Step 2: Upload JAR
  console.log('\n=== [2/7] Upload JAR ===');
  await sftpUpload(conn, DEPLOY.jarLocal, DEPLOY.jarRemote);
  console.log('JAR uploaded to', DEPLOY.jarRemote);

  // Step 3: Write wrapper script
  console.log('\n=== [3/7] Write wrapper script ===');
  await sftpWrite(conn, DEPLOY.wrapperScript, WRAPPER_SCRIPT);
  await sshExec(conn, 'chmod +x ' + DEPLOY.wrapperScript + '; echo "OK"');

  // Step 4: Write systemd unit
  console.log('\n=== [4/7] Write systemd unit ===');
  await sftpWrite(conn, DEPLOY.serviceUnit, SERVICE_UNIT);
  await sshExec(conn, 'systemctl daemon-reload; echo "OK"');

  // Step 5: Write nginx config
  console.log('\n=== [5/7] Write nginx config ===');
  await sftpWrite(conn, DEPLOY.nginxConfig, NGINX_CONFIG);
  await sshExec(conn, 'nginx -t 2>&1 && nginx -s reload 2>&1 && echo "Nginx OK" || echo "Nginx FAILED"');

  // Step 6: Start service
  console.log('\n=== [6/7] Start service ===');
  await sshExec(conn, 'systemctl start know-boot-system 2>&1; echo "Started. Waiting for port 8082..."');

  let found = false;
  for (let i = 1; i <= 60; i++) {
    await new Promise(r => setTimeout(r, 2000));
    const result = await sshExec(conn, 'netstat -tlnp 2>/dev/null | grep ":8082 " || echo "NOT_READY"');
    if (!result.includes('NOT_READY')) {
      console.log('\n  Port 8082 LISTENING after ' + (i * 2) + 's!');
      found = true;
      break;
    }
    if (i % 10 === 0) console.log('  ...waiting (' + (i * 2) + 's)');
  }

  // Step 7: Verify
  console.log('\n=== [7/7] Verify ===');
  await sshExec(conn, 'systemctl is-active know-boot-system');
  await sshExec(conn, 'curl -s -w "\\nHTTP %{http_code}" http://127.0.0.1:8082/api/login/account -X POST -H "Content-Type: application/json" -d \'{"username":"admin","password":"123456"}\' 2>&1 | tail -3');
  await sshExec(conn, 'curl -s -w "\\nHTTP %{http_code}" http://127.0.0.1:80/ 2>&1 | head -3');
  await sshExec(conn, 'free -m; uptime');

  console.log('\n=== DEPLOYMENT COMPLETE ===');
  console.log('Vue Admin:  http://101.37.83.88/');
  console.log('Login:      POST http://101.37.83.88/api/login/account');
  console.log('API Docs:   http://101.37.83.88/api/ (Swagger if enabled)');
  console.log('Note: In standalone mode, only know-boot-system runs on port 8082.');
  console.log('Plan/Camera/Knowledge APIs are served by the system JAR internally');

  conn.end();
}

main().catch(err => {
  console.error('\n[FATAL]', err.message);
  process.exit(1);
});
