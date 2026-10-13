/** Apply targeted mojibake fix for sys_menu_config id 23-27 (triple-encoded), then verify via API. */
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
    console.log('=== [1] Apply fix (decode latin1->utf8mb4 on rows 23-27) ===');
    await exec("printf \"UPDATE sys_menu_config SET menu_name=CONVERT(CONVERT(menu_name USING latin1) USING utf8mb4), icon=CONVERT(CONVERT(icon USING latin1) USING utf8mb4), big_icon=CONVERT(CONVERT(big_icon USING latin1) USING utf8mb4) WHERE id IN (23,24,25,26,27);\" | mysql --default-character-set=utf8mb4 -uroot -p123456aA@ know_boot_v1 2>&1");

    console.log('\n=== [2] Verify rows ===');
    await exec("printf 'SELECT id,menu_type,is_show,menu_name,HEX(menu_name),IFNULL(icon,\"\"),HEX(IFNULL(icon,\"\")) FROM sys_menu_config WHERE id IN (23,24,25,26,27) ORDER BY id;' | mysql --default-character-set=utf8mb4 -uroot -p123456aA@ know_boot_v1 2>/dev/null");

    console.log('\n=== [3] Verify home API (menu names) ===');
    await exec("curl -s http://127.0.0.1:8082/api/system/menu/config/home | python3 -c \"import sys,json; d=json.load(sys.stdin); [print(m['id'], m['menuName'], '|', m['icon']) for m in d.get('data',[])]\" 2>/dev/null", 30000);
    console.log('\n=== DONE ===');
  } catch (e) { console.error('ERR', e.message); }
  conn.end();
}).on('error', e => { console.error('CONN ERROR:', e.message); process.exit(1); });

conn.connect({
  host: '101.37.83.88', port: 22, username: 'root',
  privateKey: fs.readFileSync(process.env.USERPROFILE + '\\.ssh\\id_rsa'),
});