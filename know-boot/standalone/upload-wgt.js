/** Upload wgt packages to remote and backfill file_size/md5 in sys_app_version. */
const { Client } = require('ssh2');
const fs = require('fs');

const conn = new Client();
const MY = 'mysql -uroot -p123456aA@ know_boot_v1';
const WGT_DIR = 'E:\\Ai-Project\\Ai-Front\\know-uniapp\\dist';
const REMOTE_DIR = '/opt/ai-agent/upgrade/wgt';
const FILES = ['know-android-1.0.1.wgt', 'know-ios-1.0.1.wgt'];

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

conn.on('ready', async () => {
  console.log('Connected.\n');
  try {
    console.log('=== [1/2] Upload wgt ===');
    for (const f of FILES) {
      const local = WGT_DIR + '\\' + f;
      const st = fs.statSync(local);
      console.log(`  ${f} (${(st.size / 1024).toFixed(0)} KB) -> ${REMOTE_DIR}/${f}`);
      await sftpUpload(local, REMOTE_DIR + '/' + f);
    }
    await exec(`ls -l ${REMOTE_DIR}/`);

    console.log('\n=== [2/2] Backfill file_size/md5 in sys_app_version ===');
    const files = FILES.map((f) => {
      const st = fs.statSync(WGT_DIR + '\\' + f);
      const md5 = require('crypto').createHash('md5').update(fs.readFileSync(WGT_DIR + '\\' + f)).digest('hex');
      return { name: f, size: st.size, md5 };
    });
    for (const f of files) {
      const platform = f.name.includes('ios') ? 'ios' : 'android';
      await exec(`${MY} -e "UPDATE sys_app_version SET file_size=${f.size}, md5='${f.md5}', update_time=NOW() WHERE platform='${platform}' AND version_code=101;" 2>&1`);
      console.log(`  ${f.name}: size=${f.size}, md5=${f.md5}`);
    }
    await exec(`${MY} -e "SELECT id,platform,version_code,version_name,download_url,file_size,LEFT(md5,12) AS md5_short,force_update FROM sys_app_version ORDER BY id;" 2>&1`);

    console.log('\n=== WGT UPLOAD DONE ===');
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