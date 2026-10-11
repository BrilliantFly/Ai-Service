/** Apply missing incremental SQL to know_boot_v1 with mysqld readiness wait + retry. */
const { Client } = require('ssh2');
const fs = require('fs');

const conn = new Client();
const MY = 'mysql -uroot -p123456aA@ know_boot_v1';
const MIG = '/opt/ai-agent/db/migration';

const MISSING = [
  // [label, table/check, sqlFile]
  ['V1_1 sys_user_info', 'SELECT COUNT(*) FROM information_schema.tables WHERE table_schema=\'know_boot_v1\' AND table_name=\'sys_user_info\'', 'V1_1__sys_user.sql'],
  ['V1_3 sys_tenant_user', 'SELECT COUNT(*) FROM information_schema.tables WHERE table_schema=\'know_boot_v1\' AND table_name=\'sys_tenant_user\'', 'V1_3__sys_tenant_user.sql'],
  ['V1_8 focus menu', 'SELECT COUNT(*) FROM sys_menu_config WHERE menu_code=\'focus\'', 'V1_8__sys_menu_config_focus.sql'],
  ['V1_10 industry menus', 'SELECT COUNT(*) FROM sys_menu_config WHERE menu_code=\'industry\'', 'V1_10__sys_menu_config_industry.sql'],
];

async function exec(cmd) {
  return new Promise((resolve, reject) => {
    conn.exec(cmd, (err, stream) => {
      if (err) return reject(err);
      let out = '';
      stream.on('data', d => { out += d.toString(); process.stdout.write(d.toString()); });
      stream.stderr.on('data', d => { out += d.toString(); process.stdout.write(d.toString()); });
      stream.on('close', () => resolve(out));
    });
  });
}

async function queryCount(sql) {
  const out = await exec(`${MY} -N -e "${sql}" 2>/dev/null | tr -d '[:space:]'`);
  return out.trim();
}

async function waitForMysqld(maxSec) {
  console.log('Waiting for mysqld to be alive ...');
  for (let i = 1; i <= maxSec; i++) {
    try {
      const out = await exec(`mysqladmin -uroot -p123456aA@ ping 2>/dev/null | tail -1`);
      if (out.includes('alive')) return true;
    } catch (e) {}
    await new Promise(r => setTimeout(r, 2000));
  }
  return false;
}

conn.on('ready', async () => {
  console.log('Connected.\n');
  try {
    // Wait up to 120s for mysqld
    const ok = await waitForMysqld(60);
    if (!ok) { console.error('mysqld not alive after 120s'); conn.end(); process.exit(1); return; }
    console.log('mysqld alive.\n');

    // Pre-checks
    console.log('=== DIFF CHECK ===');
    const toApply = [];
    for (const [label, checkSql, file] of MISSING) {
      const c = await queryCount(checkSql);
      const missing = c === '' || c === '0';
      console.log(`  ${label}: ${missing ? 'MISSING -> apply' : 'PRESENT (skip)'}`);
      if (missing) toApply.push(file);
    }

    if (toApply.length === 0) {
      console.log('\nNothing to apply. All migrations present.');
      conn.end();
      return;
    }

    // Apply with per-file check: only if still missing (idempotent, MySQL death-safe)
    console.log('\n=== APPLYING ===');
    for (const file of toApply) {
      const [label, checkSql, f] = MISSING.find(m => m[2] === file);
      const c = await queryCount(checkSql);
      if (c !== '' && c !== '0') { console.log(`  skip ${f} (appeared meanwhile)`); continue; }
      console.log(`  apply ${f} ...`);
      await exec(`(mysqladmin -uroot -p123456aA@ ping 2>/dev/null | grep -q alive || sleep 5) && ${MY} < ${MIG}/${f} 2>&1; echo "  exit=$?"`);
      // verify
      const c2 = await queryCount(checkSql);
      const ok2 = c2 !== '' && c2 !== '0';
      console.log(`  verify ${f}: ${ok2 ? 'OK' : 'FAILED (count=' + c2 + ')'}`);
      if (!ok2) {
        console.log('  mysqld may have died; waiting for it and will continue next run.');
        break;
      }
    }

    console.log('\n=== FINAL VERIFY ===');
    await exec(`${MY} -e "SELECT id,menu_name,menu_code,menu_type,path,sort FROM sys_menu_config WHERE menu_code IN ('focus','industry','industry-enterprise','industry-product','industry-market');" 2>/dev/null`);
    console.log('\n=== APPLY DONE ===');
    conn.end();
  } catch (e) {
    console.error('ERROR', e.message);
    conn.end();
    process.exit(1);
  }
}).on('error', e => { console.error('CONN ERROR:', e.message); process.exit(1); });

conn.connect({
  host: '101.37.83.88',
  port: 22,
  username: 'root',
  privateKey: fs.readFileSync(process.env.USERPROFILE + '\\.ssh\\id_rsa'),
});