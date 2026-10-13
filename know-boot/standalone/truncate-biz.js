// Backup done already. Now truncate 12 biz tables, then verify counts.
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

const TABLES = [
  'biz_customer', 'biz_customer_company', 'biz_customer_followup',
  'biz_customer_industry', 'biz_customer_profile',
  'biz_industry', 'biz_industry_enterprise', 'biz_industry_enterprise_rel',
  'biz_industry_market', 'biz_industry_market_rel', 'biz_industry_product',
  'biz_industry_product_rel',
];

conn.on('ready', async () => {
  console.log('Connected.\n');
  try {
    console.log('=== [1] Rename backup file (fix empty date) ===');
    await exec("mv /opt/ai-agent/db/backup/biz_data_.sql /opt/ai-agent/db/backup/biz_data_20260927.sql 2>/dev/null; ls -la /opt/ai-agent/db/backup/ | tail -2", 20000);

    console.log('\n=== [2] Truncate 12 biz tables ===');
    const stmts = ['SET FOREIGN_KEY_CHECKS=0'];
    for (const t of TABLES) stmts.push('TRUNCATE TABLE ' + t);
    stmts.push('SET FOREIGN_KEY_CHECKS=1');
    await exec("mysql --default-character-set=utf8mb4 -uroot -p123456aA@ know_boot_v1 -e \"" + stmts.join('; ') + ";\" 2>&1");
    console.log('TRUNCATE done.');

    console.log('\n=== [3] Verify counts (all should be 0) ===');
    await exec("mysql --default-character-set=utf8mb4 -uroot -p123456aA@ know_boot_v1 -e \"SELECT table_name, table_rows FROM information_schema.tables WHERE table_schema='know_boot_v1' AND (table_name LIKE 'biz\\_customer%' OR table_name LIKE 'biz\\_industry%') ORDER BY table_name;\" 2>/dev/null", 30000);

    console.log('\n=== [4] Menu entries untouched (still 5 customer/industry items) ===');
    await exec("mysql --default-character-set=utf8mb4 -uroot -p123456aA@ know_boot_v1 -e \"SELECT id,menu_name,menu_code FROM sys_menu_config WHERE menu_code LIKE '%customer%' OR menu_code LIKE '%industry%' OR menu_name LIKE '%客户%' OR menu_name LIKE '%行业%' ORDER BY id;\" 2>/dev/null", 30000);
    console.log('\n=== DONE ===');
  } catch (e) { console.error('ERR', e.message); }
  conn.end();
}).on('error', e => { console.error('CONN ERROR:', e.message); process.exit(1); });

conn.connect({
  host: '101.37.83.88', port: 22, username: 'root',
  privateKey: fs.readFileSync(process.env.USERPROFILE + '\\.ssh\\id_rsa'),
});