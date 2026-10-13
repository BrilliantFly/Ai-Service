/** Dump full sys_menu_config with HEX to identify mojibake rows precisely. */
const { Client } = require('ssh2');
const fs = require('fs');
const conn = new Client();

function exec(cmd, timeoutMs) {
  return new Promise((resolve, reject) => {
    const t = setTimeout(() => { try { stream.close(); } catch (e) {} reject(new Error('EXEC_TIMEOUT')); }, timeoutMs || 60000);
    conn.exec(cmd, (err, stream) => {
      if (err) { clearTimeout(t); return reject(err); }
      let out = '';
      stream.on('data', d => { out += d.toString(); process.stdout.write(d.toString()); });
      stream.stderr.on('data', d => { out += d.toString(); process.stdout.write(d.toString()); });
      stream.on('close', () => { clearTimeout(t); resolve(out); });
    });
  });
}

conn.on('ready', async () => {
  console.log('Connected.\n');
  try {
    console.log('=== full dump (id | type | show | sort | name | HEX(name) | icon | HEX(icon)) ===');
    await exec("printf 'SELECT id,menu_type,is_show,sort,menu_name,HEX(menu_name),IFNULL(icon,\"\"),HEX(IFNULL(icon,\"\")) FROM sys_menu_config ORDER BY id;' | mysql --default-character-set=utf8mb4 -uroot -p123456aA@ know_boot_v1 2>&1", 30000);
    console.log('\n=== DONE ===');
  } catch (e) { console.error('ERR', e.message); }
  conn.end();
}).on('error', e => { console.error('CONN ERROR:', e.message); process.exit(1); });

conn.connect({
  host: '101.37.83.88', port: 22, username: 'root',
  privateKey: fs.readFileSync(process.env.USERPROFILE + '\\.ssh\\id_rsa'),
});