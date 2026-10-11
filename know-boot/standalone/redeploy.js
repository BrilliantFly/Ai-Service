/**
 * Redeploy: upload new jar (with @NotLogin fix), fix nginx /upgrade/wgt/ location,
 * restart service, poll 8082, verify version API + wgt download.
 */
const { Client } = require('ssh2');
const fs = require('fs');

const conn = new Client();
const JAR_LOCAL = __dirname + '\\..\\know-boot-system\\target\\know-boot-system-0.0.1-SNAPSHOT.jar';
const JAR_REMOTE = '/opt/ai-agent/apps/backend/know-boot-system.jar';

function exec(cmd, timeoutMs) {
  return new Promise((resolve, reject) => {
    const t = setTimeout(() => { try { stream.close(); } catch (e) {} reject(new Error('EXEC_TIMEOUT:' + cmd.slice(0, 80))); }, timeoutMs || 90000);
    conn.exec(cmd, (err, stream) => {
      if (err) { clearTimeout(t); return reject(err); }
      let out = '';
      stream.on('data', d => { out += d.toString(); process.stdout.write(d.toString()); });
      stream.stderr.on('data', d => { out += d.toString(); process.stdout.write(d.toString()); });
      stream.on('close', () => { clearTimeout(t); resolve(out); });
    });
  });
}

function sftpUpload(localPath, remotePath) {
  return new Promise((resolve, reject) => {
    conn.sftp((err, sftp) => {
      if (err) return reject(err);
      sftp.fastPut(localPath, remotePath, { concurrency: 16, chunkSize: 256 * 1024 }, (err2) => {
        if (err2) return reject(err2);
        resolve();
      });
    });
  });
}

const sleep = ms => new Promise(r => setTimeout(r, ms));

conn.on('ready', async () => {
  console.log('Connected.\n');
  try {
    console.log('=== [1/5] Upload new jar (' + (fs.statSync(JAR_LOCAL).size / 1048576).toFixed(0) + ' MB) ===');
    await sftpUpload(JAR_LOCAL, JAR_REMOTE);
    console.log('JAR uploaded.');

    console.log('\n=== [2/5] Fix nginx location /upgrade/wgt/ ===');
    await exec("sed -i 's|location /upgrade/ {|location /upgrade/wgt/ {|' /etc/nginx/conf.d/default.conf && grep -n 'upgrade' /etc/nginx/conf.d/default.conf");
    await exec('nginx -t 2>&1 && nginx -s reload 2>&1 && echo NGINX_OK || echo NGINX_FAILED');

    console.log('\n=== [3/5] Restart service ===');
    await exec('systemctl restart know-boot-system 2>&1; echo restarted');

    console.log('\n=== [4/5] Poll port 8082 (max 300s) ===');
    let found = false;
    for (let i = 1; i <= 100; i++) {
      await sleep(3000);
      const r = await exec('netstat -tlnp 2>/dev/null | grep ":8082 " || echo NOT_READY', 20000);
      if (!r.includes('NOT_READY')) { console.log('\n  Port 8082 LISTENING after ' + (i * 3) + 's!'); found = true; break; }
      if (i % 10 === 0) console.log('  ...waiting (' + (i * 3) + 's)');
    }

    console.log('\n=== [5/5] Verify ===');
    if (!found) {
      console.log('NOT ready. journal tail:');
      await exec('journalctl -u know-boot-system --no-pager -n 30 2>&1 | tail -30', 30000);
      conn.end();
      return;
    }
    await exec("echo '--- vc=100 (expect hasUpdate=true) ---'; curl -s 'http://127.0.0.1:8082/api/system/app/version/latest?platform=android&versionCode=100'; echo; echo '--- vc=101 (expect hasUpdate=false) ---'; curl -s 'http://127.0.0.1:8082/api/system/app/version/latest?platform=android&versionCode=101'; echo; echo '--- via nginx 9191 vc=100 ---'; curl -s 'http://127.0.0.1:9191/api/system/app/version/latest?platform=android&versionCode=100'; echo; echo '--- nginx wgt download HEAD ---'; curl -sI 'http://127.0.0.1:9191/upgrade/wgt/know-android-1.0.1.wgt' | head -6", 30000);

    console.log('\n=== REDEPLOY DONE ===');
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
  privateKey: fs.readFileSync(process.env.USERPROFILE + '\\.ssh\\id_rsa'),
});