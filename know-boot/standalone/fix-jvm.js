/**
 * Fix know-boot-system startup OOM on remote:
 * 1) Stop service + kill stuck java
 * 2) Rewrite start-system.sh with new JVM tuning (Metaspace 128m, Xms64m/Xmx320m)
 * 3) Start service and poll port 8082 up to 300s
 */
const { Client } = require('ssh2');
const fs = require('fs');

const conn = new Client();
const KEY_PATH = process.env.USERPROFILE + '\\.ssh\\id_rsa';

const JVM = {
  heap: '-Xms64m -Xmx320m',
  metaspace: '-XX:MaxMetaspaceSize=128m',
  java: '/usr/bin/java',
};

const DB = { host: '127.0.0.1', port: 3306, name: 'know_boot_v1', user: 'root', pass: '123456aA@' };
const REDIS = { host: '127.0.0.1', port: 16379, database: 1 };

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

function exec(cmd, timeoutMs) {
  return new Promise((resolve, reject) => {
    const t = setTimeout(() => { stream.close(); reject(new Error('EXEC_TIMEOUT:' + cmd.slice(0, 80))); }, timeoutMs || 90000);
    conn.exec(cmd, (err, stream) => {
      if (err) { clearTimeout(t); return reject(err); }
      let out = '';
      stream.on('data', d => { out += d.toString(); process.stdout.write(d.toString()); });
      stream.stderr.on('data', d => { out += d.toString(); process.stdout.write(d.toString()); });
      stream.on('close', () => { clearTimeout(t); resolve(out); });
    });
  });
}

function sftpWrite(remotePath, content) {
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

const sleep = ms => new Promise(r => setTimeout(r, ms));

conn.on('ready', async () => {
  console.log('Connected.\n');
  try {
    console.log('=== [1/5] Stop service & kill stuck java ===');
    await exec('systemctl stop know-boot-system 2>&1; pkill -9 -f "know-boot-system.jar" 2>&1; sleep 3; ps aux | grep know-boot-system.jar | grep -v grep | wc -l');
    await sleep(2000);

    console.log('\n=== [2/5] Rewrite wrapper with new JVM tuning ===');
    await sftpWrite('/opt/ai-agent/apps/backend/start-system.sh', WRAPPER_SCRIPT);
    await exec('chmod +x /opt/ai-agent/apps/backend/start-system.sh && head -5 /opt/ai-agent/apps/backend/start-system.sh');

    console.log('\n=== [3/5] Start service ===');
    await exec('systemctl start know-boot-system 2>&1; echo started');

    console.log('\n=== [4/5] Poll port 8082 (max 300s) ===');
    let found = false;
    for (let i = 1; i <= 100; i++) {
      await sleep(3000);
      const r = await exec('netstat -tlnp 2>/dev/null | grep ":8082 " || echo NOT_READY', 20000);
      if (!r.includes('NOT_READY')) { console.log('\n  Port 8082 LISTENING after ' + (i * 3) + 's!'); found = true; break; }
      if (i % 10 === 0) console.log('  ...waiting (' + (i * 3) + 's)');
      if (i % 15 === 0) {
        const alive = await exec('systemctl is-active know-boot-system; ps aux | grep know-boot-system.jar | grep -v grep | awk \'{print $3, $4}\' | head -1', 20000);
        console.log('  [status] ' + alive.trim());
      }
    }

    console.log('\n=== [5/5] Result ===');
    await exec('systemctl is-active know-boot-system', 20000);
    if (found) {
      await exec('curl -s -w "\\nHTTP %{http_code}" http://127.0.0.1:8082/api/login/account -X POST -H "Content-Type: application/json" -d \'{"username":"admin","password":"123456"}\' 2>&1 | tail -2', 30000);
    } else {
      console.log('Port 8082 NOT ready. Tail of journal:');
      await exec('journalctl -u know-boot-system --no-pager -n 25 2>&1 | tail -25', 30000);
    }
    console.log('\n=== FIX DONE ===');
    conn.end();
  } catch (e) {
    console.error('ERROR', e.message);
    conn.end();
    process.exit(1);
  }
}).on('error', e => { console.error('CONN ERROR:', e.message); process.exit(1); });

conn.connect({
  host: '101.37.83.88',
  port: 22,
  username: 'root',
  privateKey: fs.readFileSync(KEY_PATH),
});