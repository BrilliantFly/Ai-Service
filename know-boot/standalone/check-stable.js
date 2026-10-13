const { Client } = require('ssh2');
const fs = require('fs');

const conn = new Client();
const cmds = [
  ['mysqld-now', 'ps aux | grep -v grep | grep mysqld | head -2; systemctl is-active mysqld'],
  ['stable-check', 'sleep 60; systemctl is-active mysqld; ps aux | grep -v grep | grep mysqld | head -1 | awk \'{print "rss=" int($6/1024) "MB"}\'; free -h | head -2; dmesg 2>/dev/null | grep -i \'killed process\' | tail -3'],
];

conn.on('ready', () => {
  (async () => {
    for (const [name, cmd] of cmds) {
      console.log('########## ' + name + ' ##########');
      await exec(cmd);
      console.log('');
    }
    conn.end();
    console.log('=== DONE ===');
  })().catch(e => { console.error('ERROR', e); conn.end(); process.exit(1); });
}).on('error', e => { console.error('CONN ERROR:', e.message); process.exit(1); });

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