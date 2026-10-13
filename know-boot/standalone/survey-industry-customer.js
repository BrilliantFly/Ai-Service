/** Survey customer/industry related rows in sys_menu_config + related business tables. */
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
    console.log('=== [1] sys_menu_config: customer/industry rows ===');
    await exec("mysql --default-character-set=utf8mb4 -uroot -p123456aA@ know_boot_v1 -e \"SELECT id,menu_name,menu_code,menu_type,parent_id,path,is_show,sort,del_flag FROM sys_menu_config WHERE menu_code LIKE '%customer%' OR menu_code LIKE '%industry%' OR menu_name LIKE '%客户%' OR menu_name LIKE '%行业%' ORDER BY id;\" 2>/dev/null", 30000);

    console.log('\n=== [2] business tables: customer/industry related ===');
    await exec("mysql --default-character-set=utf8mb4 -uroot -p123456aA@ know_boot_v1 -e \"SELECT table_name FROM information_schema.tables WHERE table_schema='know_boot_v1' AND (table_name LIKE '%customer%' OR table_name LIKE '%industry%');\" 2>/dev/null", 30000);

    console.log('\n=== [3] count rows in those tables ===');
    await exec("mysql --default-character-set=utf8mb4 -uroot -p123456aA@ know_boot_v1 -e \"SELECT COUNT(*) AS cnt FROM sys_customer;\" 2>/dev/null", 30000);
    console.log('\n=== DONE ===');
  } catch (e) { console.error('ERR', e.message); }
  conn.end();
}).on('error', e => { console.error('CONN ERROR:', e.message); process.exit(1); });

conn.connect({
  host: '101.37.83.88', port: 22, username: 'root',
  privateKey: fs.readFileSync(process.env.USERPROFILE + '\\.ssh\\id_rsa'),
});