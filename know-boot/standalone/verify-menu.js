/** Verify sys_menu_config data (charset) + home menu API output. */
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
    console.log('=== [1] DB rows (utf8mb4 client) ===');
    await exec("printf 'SELECT id,menu_type,menu_name,title FROM sys_menu_config ORDER BY id LIMIT 25;' | mysql --default-character-set=utf8mb4 -uroot -p123456aA@ know_boot_v1 2>&1", 30000);

    console.log('\n=== [2] RAW bytes of one row (HEX) ===');
    await exec("printf \"SELECT id,HEX(menu_name) FROM sys_menu_config ORDER BY id LIMIT 3;\" | mysql --default-character-set=utf8mb4 --raw -uroot -p123456aA@ know_boot_v1 2>&1", 30000);

    console.log('\n=== [3] Home menu API ===');
    await exec('curl -s http://127.0.0.1:8082/api/system/menu/config/home | head -c 800', 30000);
    console.log('');
    console.log('\n=== DONE ===');
  } catch (e) { console.error('ERR', e.message); }
  conn.end();
}).on('error', e => { console.error('CONN ERROR:', e.message); process.exit(1); });

conn.connect({
  host: '101.37.83.88', port: 22, username: 'root',
  privateKey: fs.readFileSync(process.env.USERPROFILE + '\\.ssh\\id_rsa'),
});