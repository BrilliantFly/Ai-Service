const { Client } = require('ssh2');
const fs = require('fs');

const conn = new Client();
const cmds = [
  ['mem-now', 'free -h'],
  ['mysql-process', 'ps aux | grep -v grep | grep mysqld | head -3'],
  ['mysql-ping', 'mysqladmin -uroot -p123456aA@ ping 2>&1'],
  ['mysql-errlog', 'tail -30 /var/log/mysql/error.log 2>/dev/null || journalctl -u mysqld --no-pager -n 30 2>/dev/null || echo NO_LOG'],
  ['dmesg-oom', "dmesg 2>/dev/null | grep -i 'killed process' | tail -5"],
];

conn.on('ready', () => {
  console.log('=== MYSQL STATUS CHECK ===\n');
  (async () => {
    for (const [name, cmd] of cmds) {
      console.log('########## ' + name + ' ##########');
      await exec(cmd);
      console.log('');
    }
    conn.end();
    console.log('=== DONE ===');
  })().catch(e => { console.error('ERROR', e); conn.end(); process.exit(1); });
}).on('error', e => {
  console.error('CONN ERROR:', e.message);
  process.exit(1);
});

conn.connect({
  host: '101.37.83.88',
  port: 22,
  username: 'root',
  privateKey: fs.readFileSync(process.env.USERPROFILE + '\\.ssh\\id_rsa'),
});

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