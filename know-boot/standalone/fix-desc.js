/** Fix update_desc mojibake: upload UTF-8 SQL, apply with utf8mb4 client charset, verify via API. */
const { Client } = require('ssh2');
const fs = require('fs');

const conn = new Client();
const LOCAL_SQL = 'C:\\Users\\29490\\AppData\\Local\\Temp\\opencode\\fix-desc.sql';
const REMOTE_SQL = '/tmp/fix-desc.sql';

function exec(cmd, timeoutMs) {
  return new Promise((resolve, reject) => {
    const t = setTimeout(() => { try { stream.close(); } catch (e) {} reject(new Error('EXEC_TIMEOUT')); }, timeoutMs || 60000);
    conn.exec(cmd, (err, stream) => {
      if (err) { clearTimeout(t); return reject(err); }
      let out = '';
      stream.on('data', d => { out += d.toString(); process.stdout.write(d.toString()); });
      stream.stderr.on('data', d => { out += d.toString(); process.stdout.write(d.toString()); });
      stream.on('close', () => { clearTimeout(t); resolve(out); });
    });
  });
}

conn.on('ready', async () => {
  console.log('Connected.');
  await new Promise((resolve, reject) => {
    conn.sftp((err, sftp) => {
      if (err) return reject(err);
      const ws = sftp.createWriteStream(REMOTE_SQL);
      ws.on('close', resolve);
      ws.on('error', reject);
      ws.end(fs.readFileSync(LOCAL_SQL));
    });
  });
  console.log('SQL uploaded.');
  await exec('mysql --default-character-set=utf8mb4 -uroot -p123456aA@ know_boot_v1 < /tmp/fix-desc.sql 2>&1');
  await exec("mysql --default-character-set=utf8mb4 -uroot -p123456aA@ know_boot_v1 -e \"SELECT id,platform,version_code,update_desc FROM sys_app_version WHERE version_code=101;\" 2>&1", 30000);
  await exec("curl -s 'http://127.0.0.1:8082/api/system/app/version/latest?platform=android&versionCode=100' | head -c 400", 30000);
  console.log('\n=== DONE ===');
  conn.end();
}).on('error', e => { console.error('CONN ERROR:', e.message); process.exit(1); });

conn.connect({
  host: '101.37.83.88',
  port: 22,
  username: 'root',
  privateKey: fs.readFileSync(process.env.USERPROFILE + '\\.ssh\\id_rsa'),
});