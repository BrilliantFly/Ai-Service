/** Upload UTF-8 SQL, fix sys_menu_config rows 23-27 with canonical values, verify via DB + API. */
const { Client } = require('ssh2');
const fs = require('fs');
const conn = new Client();
const LOCAL_SQL = 'C:\\Users\\29490\\AppData\\Local\\Temp\\opencode\\fix-menu-final.sql';
const REMOTE_SQL = '/tmp/fix-menu-final.sql';

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
  console.log('Connected.');
  try {
    await new Promise((resolve, reject) => {
      conn.sftp((err, sftp) => {
        if (err) return reject(err);
        const ws = sftp.createWriteStream(REMOTE_SQL);
        ws.on('close', resolve);
        ws.on('error', reject);
        ws.end(fs.readFileSync(LOCAL_SQL));
      });
    });
    console.log('SQL uploaded.');

    console.log('\n=== [1] Apply fix ===');
    await exec('mysql --default-character-set=utf8mb4 -uroot -p123456aA@ know_boot_v1 < /tmp/fix-menu-final.sql 2>&1');

    console.log('\n=== [2] Verify DB rows ===');
    await exec("printf 'SELECT id,menu_name,HEX(menu_name),icon,HEX(icon),render_config FROM sys_menu_config WHERE id IN (23,24,25,26,27) ORDER BY id;' | mysql --default-character-set=utf8mb4 -uroot -p123456aA@ know_boot_v1 2>/dev/null");

    console.log('\n=== [3] Verify home API ===');
    await exec("curl -s http://127.0.0.1:8082/api/system/menu/config/home | python3 -c \"import sys,json; d=json.load(sys.stdin); [print(m['id'], m['menuName'], '|', m['icon']) for m in d.get('data',[])]\" 2>/dev/null", 30000);
    console.log('\n=== DONE ===');
  } catch (e) { console.error('ERR', e.message); }
  conn.end();
}).on('error', e => { console.error('CONN ERROR:', e.message); process.exit(1); });

conn.connect({
  host: '101.37.83.88', port: 22, username: 'root',
  privateKey: fs.readFileSync(process.env.USERPROFILE + '\\.ssh\\id_rsa'),
});