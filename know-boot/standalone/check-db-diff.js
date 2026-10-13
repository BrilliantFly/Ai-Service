const { Client } = require('ssh2');
const fs = require('fs');

const conn = new Client();
const cmds = [
  ['repeat-end-date-col', "mysql -uroot -p123456aA@ know_boot_v1 -e \"SHOW COLUMNS FROM plan_schedule_event LIKE 'repeat_end_date';\" 2>/dev/null"],
  ['menu-config-structure', "mysql -uroot -p123456aA@ know_boot_v1 -e \"SHOW COLUMNS FROM sys_menu_config;\" 2>/dev/null"],
  ['menu-config-data', "mysql -uroot -p123456aA@ know_boot_v1 -e \"SELECT id,menu_name,menu_code,menu_type,path,sort FROM sys_menu_config ORDER BY menu_type,sort LIMIT 40;\" 2>/dev/null"],
  ['sys-menu-count', "mysql -uroot -p123456aA@ know_boot_v1 -e \"SELECT COUNT(*) total, SUM(del_flag=0) alive FROM sys_menu;\" 2>/dev/null"],
  ['sys-menu-industry', "mysql -uroot -p123456aA@ know_boot_v1 -e \"SELECT id,menu_name,menu_code,path FROM sys_menu WHERE menu_name LIKE '%industry%' OR menu_code LIKE '%industry%' OR menu_name LIKE '%行业%' LIMIT 20;\" 2>/dev/null"],
  ['sys-user', "mysql -uroot -p123456aA@ know_boot_v1 -e \"SELECT id,username,nickname FROM sys_user LIMIT 10;\" 2>/dev/null"],
  ['sys-admin', "mysql -uroot -p123456aA@ know_boot_v1 -e \"SELECT id,username FROM sys_admin LIMIT 10;\" 2>/dev/null"],
  ['biz-industry-count', "mysql -uroot -p123456aA@ know_boot_v1 -e \"SELECT COUNT(*) cnt FROM biz_industry;\" 2>/dev/null"],
  ['db-version', "mysql -uroot -p123456aA@ -e \"SELECT VERSION();\" 2>/dev/null"],
];

conn.on('ready', () => {
  console.log('=== CHECKING REMOTE DB DIFF ===\n');
  (async () => {
    for (const [name, cmd] of cmds) {
      console.log('########## ' + name + ' ##########');
      await exec(cmd);
      console.log('');
    }
    conn.end();
    console.log('=== DONE ===');
  })().catch(e => { console.error('ERROR', e); conn.end(); process.exit(1); });
}).on('error', e => {
  console.error('CONN ERROR:', e.message);
  process.exit(1);
});

conn.connect({
  host: '101.37.83.88',
  port: 22,
  username: 'root',
  privateKey: fs.readFileSync(process.env.USERPROFILE + '\\.ssh\\id_rsa'),
});

function exec(cmd) {
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