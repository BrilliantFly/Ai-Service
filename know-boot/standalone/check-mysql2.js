const { Client } = require('ssh2');
const fs = require('fs');

const conn = new Client();
const cmds = [
  ['mysqld-now', 'ps aux | grep -v grep | grep mysqld | head -2; echo ---; mysqladmin -uroot -p123456aA@ ping 2>&1 | tail -1'],
  ['mysql-status', 'systemctl status mysqld --no-pager 2>&1 | head -15'],
  ['mysql-bufferpool', "mysql -uroot -p123456aA@ -e \"SHOW VARIABLES LIKE 'innodb_buffer_pool_size'; SHOW VARIABLES LIKE 'innodb_buffer_pool_instances';\" 2>&1 | tail -5"],
  ['swap-usage', 'free -h | grep -i swap'],
  ['oom-kills-recent', "dmesg 2>/dev/null | grep -i 'killed process' | tail -8"],
];

conn.on('ready', () => {
  console.log('=== MYSQL CURRENT STATE ===\n');
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