/**
 * Apply V1_11 sys_app_version migration + seed test records on remote server.
 * Idempotent: CREATE TABLE IF NOT EXISTS + ON DUPLICATE KEY UPDATE.
 */
const { Client } = require('ssh2');
const fs = require('fs');

const conn = new Client();
const MY = 'mysql -uroot -p123456aA@ know_boot_v1';
const LOCAL_SQL = __dirname + '\\..\\know-boot-system\\src\\main\\resources\\db\\migration\\V1_11__sys_app_version.sql';
const REMOTE_SQL = '/opt/ai-agent/db/migration/V1_11__sys_app_version.sql';

function exec(cmd) {
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

conn.on('ready', async () => {
  console.log('Connected.\n');
  try {
    console.log('=== [1/4] Ensure dirs ===');
    await exec('mkdir -p /opt/ai-agent/db/migration /opt/ai-agent/upgrade/wgt && ls -ld /opt/ai-agent/db/migration /opt/ai-agent/upgrade/wgt');

    console.log('\n=== [2/4] Upload V1_11 SQL ===');
    await sftpWrite(REMOTE_SQL, fs.readFileSync(LOCAL_SQL, 'utf8'));
    console.log('Uploaded:', REMOTE_SQL);

    console.log('\n=== [3/4] Create table (idempotent) ===');
    await exec(`${MY} < ${REMOTE_SQL} 2>&1; echo "  mysql exit=$?"`);

    console.log('\n=== [3.5/4] Seed test records (idempotent) ===');
    await exec(`${MY} -e "INSERT INTO sys_app_version (platform,version_code,version_name,download_url,file_name,file_size,md5,force_update,update_desc,status,create_time,update_time,deleted) VALUES ('android',101,'1.0.1','/upgrade/wgt/know-android-1.0.1.wgt','know-android-1.0.1.wgt',0,NULL,0,'测试更新包：版本 1.0.1 功能优化',1,NOW(),NOW(),0),('ios',101,'1.0.1','/upgrade/wgt/know-ios-1.0.1.wgt','know-ios-1.0.1.wgt',0,NULL,0,'测试更新包：版本 1.0.1 功能优化',1,NOW(),NOW(),0) ON DUPLICATE KEY UPDATE version_name=VALUES(version_name),update_desc=VALUES(update_desc);" 2>&1`);

    console.log('\n=== [4/4] Verify ===');
    await exec(`${MY} -e "SELECT id,platform,version_code,version_name,download_url,force_update,status,deleted FROM sys_app_version ORDER BY id;" 2>&1`);

    console.log('\n=== V1_11 APPLY DONE ===');
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