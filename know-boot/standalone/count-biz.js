// Count rows in biz_customer and biz_industry tables.
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
    await exec("mysql --default-character-set=utf8mb4 -uroot -p123456aA@ know_boot_v1 -e \"SELECT table_name, table_rows FROM information_schema.tables WHERE table_schema='know_boot_v1' AND (table_name LIKE 'biz_customer%' OR table_name LIKE 'biz_industry%') ORDER BY table_name;\" 2>/dev/null", 30000);
    console.log('\n=== DONE ===');
  } catch (e) { console.error('ERR', e.message); }
  conn.end();
}).on('error', e => { console.error('CONN ERROR:', e.message); process.exit(1); });

conn.connect({
  host: '101.37.83.88', port: 22, username: 'root',
  privateKey: fs.readFileSync(process.env.USERPROFILE + '\\.ssh\\id_rsa'),
});