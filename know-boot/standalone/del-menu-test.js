/** Soft-delete sys_menu 6032 (公司管理) and verify */
const { Client } = require('ssh2');
const fs = require('fs');
const conn = new Client();

conn.on('ready', () => {
  const run = (cmd) => new Promise((resolve, reject) => {
    conn.exec(cmd, (err, stream) => {
      if (err) return reject(err);
      let out = '';
      stream.on('data', (d) => { out += d.toString(); process.stdout.write(d.toString()); });
      stream.stderr.on('data', (d) => { out += d.toString(); process.stdout.write(d.toString()); });
      stream.on('close', () => resolve(out));
    });
  });
  (async () => {
    await run(`mysql -uroot -p123456aA@ know_boot_v1 -e "UPDATE sys_menu SET del_flag=1 WHERE id=6032 AND del_flag=0; SELECT ROW_COUNT() AS affected; SELECT id,menu_name,menu_type,status,del_flag FROM sys_menu WHERE id=6032;" 2>&1`);
    conn.end();
  })().catch((e) => { console.error(e); conn.end(); });
}).on('error', (e) => { console.error('CONN ERROR:', e.message); process.exit(1); });

conn.connect({
  host: '101.37.83.88',
  port: 22,
  username: 'root',
  privateKey: fs.readFileSync(process.env.USERPROFILE + '\\.ssh\\id_rsa'),
});