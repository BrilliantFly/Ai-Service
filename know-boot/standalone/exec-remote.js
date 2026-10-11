/** Generic remote exec: node exec-remote.js "command here" */
const { Client } = require('ssh2');
const fs = require('fs');
const conn = new Client();
const cmd = process.argv.slice(2).join(' ');

conn.on('ready', () => {
  conn.exec(cmd, (err, stream) => {
    if (err) { console.error(err.message); process.exit(1); }
    stream.on('data', d => process.stdout.write(d.toString()));
    stream.stderr.on('data', d => process.stdout.write(d.toString()));
    stream.on('close', () => { conn.end(); });
  });
}).on('error', e => { console.error('CONN ERROR:', e.message); process.exit(1); });

conn.connect({
  host: '101.37.83.88',
  port: 22,
  username: 'root',
  privateKey: fs.readFileSync(process.env.USERPROFILE + '\\.ssh\\id_rsa'),
});