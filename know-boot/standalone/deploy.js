/**
 * Ai-KnowBoot V2 Server Deployment Script (Component-based)
 * 
 * Deploys the full standalone know-boot-system.jar (contains system + plan + camera + knowledge modules)
 * and/or frontend builds independently.
 * 
 * Usage:
 *   node deploy.js                -> deploy ALL (system + vue + mobile)
 *   node deploy.js system         -> backend jar only
 *   node deploy.js vue            -> vue admin frontend only
 *   node deploy.js mobile         -> uniapp H5 only
 *   node deploy.js system vue     -> backend + vue admin (any combination)
 * 
 * Prerequisites (build locally first):
 *   Backend : cd know-boot && mvn clean install -DskipTests (or at least -pl know-boot-plan,know-boot-camera,know-boot-knowledge -am install, then -pl know-boot-system package)
 *   Vue     : cd know-vue && npm run build (output: know-vue/dist)
 *   Uniapp  : cd know-uniapp && npm run build:h5 (output: know-uniapp/dist/build/h5)
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
  // Frontend
  mobileLocal: __dirname + '\\..\\..\\..\\Ai-Front\\know-uniapp\\dist\\build\\h5',
  mobileRemote: '/opt/ai-agent/apps/know-mobile',
  adminLocal: __dirname + '\\..\\..\\..\\Ai-Front\\know-vue\\dist',
  adminRemote: '/opt/ai-agent/apps/know-vue',
};

// Which components to deploy
const args = process.argv.slice(2);
const want = {
  system: args.length === 0 || args.includes('system'),
  vue: args.length === 0 || args.includes('vue'),
  mobile: args.length === 0 || args.includes('mobile'),
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
        rewrite ^/adminapi/(.*)$ /api/$1 break;
        proxy_pass http://127.0.0.1:8082/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    location /adminapi/static/ {
        proxy_pass http://127.0.0.1:8082/adminapi/static/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    location /system/ {
        rewrite ^/system/(.*)$ /api/system/$1 break;
        proxy_pass http://127.0.0.1:8082/;
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

function getDirSize(dir) {
  const fs = require('fs');
  const path = require('path');
  let count = 0;
  const entries = fs.readdirSync(dir, { withFileTypes: true });
  for (const e of entries) {
    const p = path.join(dir, e.name);
    if (e.isDirectory()) count += getDirSize(p);
    else count++;
  }
  return count;
}

async function deployDirViaTar(conn, localDir, remoteDir, label) {
  const fs = require('fs');
  const os = require('os');
  const tmpTar = os.tmpdir() + '\\' + label + '.tar.gz';
  if (!fs.existsSync(localDir)) {
    console.log('[' + label + '] Build not found at', localDir + ', skipping.');
    return false;
  }
  const size = getDirSize(localDir);
  console.log('[' + label + '] Found (' + size + ' files), archiving ...');
  await new Promise((resolve, reject) => {
    const cp = require('child_process');
    const tar = cp.spawn('tar', ['-czf', tmpTar, '-C', localDir, '.']);
    let errOut = '';
    tar.on('close', code => code === 0 ? resolve() : reject(new Error('tar failed: ' + code + '\n' + errOut)));
    tar.stderr.on('data', d => errOut += d.toString());
  });
  const stat = fs.statSync(tmpTar);
  console.log('[' + label + '] Archive created (' + (stat.size / 1024).toFixed(0) + 'KB), uploading ...');
  await sftpUpload(conn, tmpTar, '/tmp/' + label + '.tar.gz');
  console.log('[' + label + '] Uploaded, extracting on server ...');
  await sshExec(conn, `rm -rf ${remoteDir} && mkdir -p ${remoteDir} && tar -xzf /tmp/${label}.tar.gz -C ${remoteDir} && rm /tmp/${label}.tar.gz && echo "[" + ${label} + "] deployed"`);
  fs.unlinkSync(tmpTar);
  console.log('[' + label + '] Deployed to', remoteDir);
  return true;
}

async function main() {
  const fs = require('fs');
  const conn = new Client();

  const parts = [];
  if (want.system) parts.push('system');
  if (want.vue) parts.push('vue');
  if (want.mobile) parts.push('mobile');
  console.log('Deploying components:', parts.join(', '));

  // Check local artifacts
  if (want.system && !fs.existsSync(DEPLOY.jarLocal)) {
    console.error('[ERROR] JAR not found:', DEPLOY.jarLocal);
    console.error('Run: cd know-boot-system && mvn clean package -DskipTests');
    process.exit(1);
  }
  if (want.system) {
    console.log('JAR found:', (fs.statSync(DEPLOY.jarLocal).size / 1024 / 1024).toFixed(0) + 'MB');
  }

  // Connect
  console.log('\nConnecting to', SERVER.host, '...');
  await new Promise((resolve, reject) => {
    conn.on('ready', resolve).on('error', reject);
    conn.connect(SERVER);
  });
  console.log('Connected.\n');

  let step = 1;

  if (want.system) {
    // Step 1: Stop existing services
    console.log('=== [' + (step++) + '/9] Stop existing services ===');
    await sshExec(conn, 'systemctl stop know-boot-system know-boot-plan know-boot-camera know-boot-knowledge 2>&1; pkill -f "know-boot" 2>&1 || true; sleep 2; echo "Stopped"');

    // Step 2: Upload JAR
    console.log('\n=== [' + (step++) + '/9] Upload JAR ===');
    await sftpUpload(conn, DEPLOY.jarLocal, DEPLOY.jarRemote);
    console.log('JAR uploaded to', DEPLOY.jarRemote);

    // Step 3: Write wrapper script
    console.log('\n=== [' + (step++) + '/9] Write wrapper script ===');
    await sftpWrite(conn, DEPLOY.wrapperScript, WRAPPER_SCRIPT);
    await sshExec(conn, 'chmod +x ' + DEPLOY.wrapperScript + '; echo "OK"');

    // Step 4: Write systemd unit
    console.log('\n=== [' + (step++) + '/9] Write systemd unit ===');
    await sftpWrite(conn, DEPLOY.serviceUnit, SERVICE_UNIT);
    await sshExec(conn, 'systemctl daemon-reload; echo "OK"');

    // Step 5: Write nginx config
    console.log('\n=== [' + (step++) + '/9] Write nginx config ===');
    await sftpWrite(conn, DEPLOY.nginxConfig, NGINX_CONFIG);
    await sshExec(conn, 'nginx -t 2>&1 && nginx -s reload 2>&1 && echo "Nginx OK" || echo "Nginx FAILED"');

    // Step 6: Start service
    console.log('\n=== [' + (step++) + '/9] Start service ===');
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
    console.log('\n=== [' + (step++) + '/9] Verify ===');
    await sshExec(conn, 'systemctl is-active know-boot-system');
    await sshExec(conn, 'curl -s -w "\\nHTTP %{http_code}" http://127.0.0.1:8082/api/login/account -X POST -H "Content-Type: application/json" -d \'{"username":"admin","password":"123456"}\' 2>&1 | tail -3');
    await sshExec(conn, 'curl -s -o /dev/null -w "slogan current: %{http_code}\\n" http://127.0.0.1:8082/api/plan/slogan/current');
    await sshExec(conn, 'curl -s -o /dev/null -w "camera page: %{http_code}\\n" http://127.0.0.1:8082/api/camera/device/page');
    await sshExec(conn, 'curl -s -w "\\nHTTP %{http_code}" http://127.0.0.1:80/ 2>&1 | head -3');
  }

  if (want.vue) {
    console.log('\n=== Vue admin frontend ===');
    await deployDirViaTar(conn, DEPLOY.adminLocal, DEPLOY.adminRemote, 'know-vue');
  }

  if (want.mobile) {
    console.log('\n=== Mobile H5 frontend ===');
    await deployDirViaTar(conn, DEPLOY.mobileLocal, DEPLOY.mobileRemote, 'know-mobile');
  }

  if (want.system) {
    console.log('\n=== DEPLOYMENT COMPLETE ===');
    console.log('Vue Admin:  http://101.37.83.88/');
    console.log('Mobile H5:  http://101.37.83.88/mobile/');
    console.log('Login:      POST http://101.37.83.88/api/login/account');
    console.log('Note: Standalone know-boot-system serves system+plan+camera+knowledge on port 8082.');
  } else {
    console.log('\n=== FRONTEND DEPLOY COMPLETE ===');
  }

  conn.end();
}

main().catch(err => {
  console.error('\n[FATAL]', err.message);
  process.exit(1);
});
