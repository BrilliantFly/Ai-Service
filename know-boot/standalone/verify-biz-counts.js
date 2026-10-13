// Precise COUNT(*) verification for 12 biz tables after truncate.
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

const q = "SELECT 'biz_customer' t, COUNT(*) c FROM biz_customer UNION ALL SELECT 'biz_customer_company', COUNT(*) FROM biz_customer_company UNION ALL SELECT 'biz_customer_followup', COUNT(*) FROM biz_customer_followup UNION ALL SELECT 'biz_customer_industry', COUNT(*) FROM biz_customer_industry UNION ALL SELECT 'biz_customer_profile', COUNT(*) FROM biz_customer_profile UNION ALL SELECT 'biz_industry', COUNT(*) FROM biz_industry UNION ALL SELECT 'biz_industry_enterprise', COUNT(*) FROM biz_industry_enterprise UNION ALL SELECT 'biz_industry_enterprise_rel', COUNT(*) FROM biz_industry_enterprise_rel UNION ALL SELECT 'biz_industry_market', COUNT(*) FROM biz_industry_market UNION ALL SELECT 'biz_industry_market_rel', COUNT(*) FROM biz_industry_market_rel UNION ALL SELECT 'biz_industry_product', COUNT(*) FROM biz_industry_product UNION ALL SELECT 'biz_industry_product_rel', COUNT(*) FROM biz_industry_product_rel;";

conn.on('ready', async () => {
  console.log('Connected.\n');
  try {
    console.log('=== precise counts (expect 0 everywhere) ===');
    await exec("mysql --default-character-set=utf8mb4 -uroot -p123456aA@ know_boot_v1 -e \"" + q + "\" 2>/dev/null", 30000);
    console.log('\n=== DONE ===');
  } catch (e) { console.error('ERR', e.message); }
  conn.end();
}).on('error', e => { console.error('CONN ERROR:', e.message); process.exit(1); });

conn.connect({
  host: '101.37.83.88', port: 22, username: 'root',
  privateKey: fs.readFileSync(process.env.USERPROFILE + '\\.ssh\\id_rsa'),
});