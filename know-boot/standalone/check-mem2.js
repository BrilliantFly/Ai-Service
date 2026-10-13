const { Client } = require('ssh2');
const fs = require('fs');

const conn = new Client();
const cmds = [
  ['mysqld-conn', 'ps aux | grep -v grep | grep mysqld | head -2'],
  ['mycnf', 'cat /etc/my.cnf 2>/dev/null; echo ===; ls /etc/my.cnf.d/ 2>/dev/null; echo ===; cat /etc/my.cnf.d/*.cnf 2>/dev/null | head -60'],
  ['python-procs', 'ps -o pid,user,rss,etime,args -p $(pgrep -f "/app/api" | tr "\\n" "," | sed "s/,$//") 2>/dev/null | head -10'],
  ['next-server', 'ps aux | grep -v grep | grep next-server | head -2'],
  ['all-mem-procs', 'ps aux --sort=-rss | awk \'NR>1 && $6/1024>80 {printf "%s %s %s %sMB %s\\n", $1, $2, $3, int($6/1024), $11}\' | head -20'],
];

conn.on('ready', () => {
  console.log('=== MEMORY OWNERSHIP ===\n');
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