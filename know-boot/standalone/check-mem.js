const { Client } = require('ssh2');
const fs = require('fs');

const conn = new Client();
const cmds = [
  ['mysqld-alive', 'ps aux | grep -v grep | grep mysqld | head -2; echo ---; mysqladmin -uroot -p123456aA@ ping 2>&1 | tail -1'],
  ['top-mem', 'ps aux --sort=-rss | head -15 | awk \'{printf "%s %s %s %sMB %s\\n", $1, $2, $3, int($6/1024), $11}\''],
  ['mem-now', 'free -h'],
  ['java-procs', 'ps aux | grep -v grep | grep java | head -5'],
];

conn.on('ready', () => {
  console.log('=== MEMORY DIAGNOSIS ===\n');
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