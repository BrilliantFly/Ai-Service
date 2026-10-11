/**
 * Publish 1.0.3 wgt: upload wgt packages to nginx dir, insert/backfill sys_app_version row.
 * Idempotent (ON DUPLICATE KEY UPDATE). Chinese update_desc via UTF-8 SQL file.
 *
 * Build the wgt first:  node build-wgt-103.js
 */
const { Client } = require('ssh2');
const fs = require('fs');
const crypto = require('crypto');

const conn = new Client();
const MY = 'mysql -uroot -p123456aA@ know_boot_v1';
const WGT_DIR = 'E:\\Ai-Project\\Ai-Front\\know-uniapp\\dist';
const REMOTE_DIR = '/opt/ai-agent/upgrade/wgt';
const REMOTE_SQL = '/opt/ai-agent/db/ver_103.sql';
const FILES = ['know-android-1.0.3.wgt', 'know-ios-1.0.3.wgt'];

const UPDATE_DESC = '更新内容：计划类型改为级联字典选择；计划模板列表支持按计划类型动态筛选并显示完整分类路径。';

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

function sftpUpload(localPath, remotePath) {
  return new Promise((resolve, reject) => {
    conn.sftp((err, sftp) => {
      if (err) return reject(err);
      sftp.fastPut(localPath, remotePath, { concurrency: 8, chunkSize: 128 * 1024 }, (err2) => {
        if (err2) return reject(err2);
        resolve();
      });
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
    const files = FILES.map((f) => {
      const buf = fs.readFileSync(WGT_DIR + '\\' + f);
      return { name: f, size: buf.length, md5: crypto.createHash('md5').update(buf).digest('hex') };
    });

    console.log('=== [1/3] Upload wgt ===');
    for (const f of files) {
      console.log(`  ${f.name} (${(f.size / 1024).toFixed(0)} KB, md5=${f.md5.slice(0, 12)}...)`);
      await sftpUpload(WGT_DIR + '\\' + f.name, REMOTE_DIR + '/' + f.name);
    }
    await exec(`ls -l ${REMOTE_DIR}/`);

    // UTF-8 SQL file to avoid shell quoting / encoding issues with Chinese text
    const sql = [
      'SET NAMES utf8mb4;',
      `INSERT INTO sys_app_version (platform,version_code,version_name,download_url,file_name,file_size,md5,force_update,update_desc,status,create_time,update_time,deleted) VALUES`,
      `('android',103,'1.0.3','/upgrade/wgt/know-android-1.0.3.wgt','know-android-1.0.3.wgt',${files[0].size},'${files[0].md5}',0,'${UPDATE_DESC}',1,NOW(),NOW(),0),`,
      `('ios',103,'1.0.3','/upgrade/wgt/know-ios-1.0.3.wgt','know-ios-1.0.3.wgt',${files[1].size},'${files[1].md5}',0,'${UPDATE_DESC}',1,NOW(),NOW(),0)`,
      `ON DUPLICATE KEY UPDATE version_name=VALUES(version_name),download_url=VALUES(download_url),file_name=VALUES(file_name),file_size=VALUES(file_size),md5=VALUES(md5),force_update=VALUES(force_update),update_desc=VALUES(update_desc),status=VALUES(status),update_time=NOW();`
    ].join('\n');

    console.log('\n=== [2/3] Apply sys_app_version 1.0.3 ===');
    await sftpWrite(REMOTE_SQL, sql);
    await exec(`${MY} < ${REMOTE_SQL} 2>&1; echo "  mysql exit=$?"`);

    console.log('\n=== [3/3] Verify ===');
    await exec(`${MY} --default-character-set=utf8mb4 -e "SELECT id,platform,version_code,version_name,download_url,file_size,LEFT(md5,12) AS md5_short,force_update,status,deleted,update_desc FROM sys_app_version ORDER BY id;" 2>&1`);
    console.log('\n--- hasUpdate check (App 1.0.2 -> expect true) ---');
    await exec(`curl -s 'http://127.0.0.1:8082/api/system/app/version/latest?platform=android&versionCode=102'; echo; echo '--- via nginx 9191 ---'; curl -s 'http://127.0.0.1:9191/api/system/app/version/latest?platform=android&versionCode=102'; echo; echo '--- wgt download HEAD ---'; curl -sI 'http://127.0.0.1:9191/upgrade/wgt/know-android-1.0.3.wgt' | head -5`);

    console.log('\n=== PUBLISH 1.0.3 DONE ===');
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
