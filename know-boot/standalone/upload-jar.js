const { Client } = require('ssh2');
const fs = require('fs');

const KEY_PATH = process.env.SSH_PRIVATE_KEY || (process.env.USERPROFILE + '\\.ssh\\id_rsa');
const SERVER = {
  host: '101.37.83.88',
  port: 22,
  username: 'root',
  privateKey: fs.readFileSync(KEY_PATH),
};
const local = __dirname + '\\..\\know-boot-system\\target\\know-boot-system-0.0.1-SNAPSHOT.jar';
const remote = '/opt/ai-agent/apps/backend/know-boot-system.jar';

function sftpUpload(conn, localPath, remotePath) {
  return new Promise((resolve, reject) => {
    conn.sftp((err, sftp) => {
      if (err) return reject(err);
      const total = fs.statSync(localPath).size;
      let done = 0;
      sftp.fastPut(localPath, remotePath, { concurrency: 16, chunkSize: 256 * 1024 }, (err2) => {
        if (err2) return reject(err2);
        resolve();
      });
      const timer = setInterval(async () => {
        try {
          const st = await new Promise((res, rej) => sftp.stat(remotePath, (e, s) => e ? rej(e) : res(s)));
          done = st.size;
          const pct = ((done / total) * 100).toFixed(1);
          process.stdout.write('\rUploading JAR: ' + (done / 1048576).toFixed(0) + 'MB / ' + (total / 1048576).toFixed(0) + 'MB (' + pct + '%)     ');
          if (done >= total) { clearInterval(timer); }
        } catch (e) {
          if (e.code !== 'ENOENT') { clearInterval(timer); }
        }
      }, 3000);
    });
  });
}

async function main() {
  if (!fs.existsSync(local)) { console.error('[FATAL] JAR not found:', local); process.exit(1); }
  console.log('JAR size:', (fs.statSync(local).size / 1048576).toFixed(0) + 'MB');
  const conn = new Client();
  console.log('Connecting to', SERVER.host, '...');
  await new Promise((res, rej) => { conn.on('ready', res).on('error', rej); conn.connect(SERVER); });
  console.log('Connected.');
  await sftpUpload(conn, local, remote);
  conn.end();
  process.stdout.write('\n');
  console.log('DONE');
}

main().catch(err => { console.error('\n[FATAL]', err.message); process.exit(1); });