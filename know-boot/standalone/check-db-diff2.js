const { Client } = require('ssh2');
const fs = require('fs');

const conn = new Client();
const cmds = [
  ['focus-menu', "mysql -uroot -p123456aA@ know_boot_v1 -e \"SELECT id,menu_name,menu_code,menu_type,sort FROM sys_menu_config WHERE menu_code IN ('focus','device','slogan') OR menu_name LIKE '%专注%';\" 2>/dev/null"],
  ['sys-menu-industry-raw', "mysql -uroot -p123456aA@ know_boot_v1 -e \"SELECT id,menu_name,menu_code FROM sys_menu WHERE menu_code LIKE '%industry%';\" 2>/dev/null"],
  ['sys-menu-sample', "mysql -uroot -p123456aA@ know_boot_v1 -e \"SELECT id,menu_name,menu_code,parent_id FROM sys_menu WHERE del_flag=0 LIMIT 15;\" 2>/dev/null"],
  ['role-dept-dict', "mysql -uroot -p123456aA@ know_boot_v1 -e \"SELECT 'roles' t, COUNT(*) c FROM sys_role WHERE del_flag=0 UNION ALL SELECT 'depts', COUNT(*) FROM sys_dept WHERE del_flag=0 UNION ALL SELECT 'dicts', COUNT(*) FROM sys_dict WHERE del_flag=0 UNION ALL SELECT 'dict_types', COUNT(*) FROM sys_dict_type WHERE del_flag=0 UNION ALL SELECT 'tabbars', COUNT(*) FROM sys_tabbar WHERE del_flag=0;\" 2>/dev/null"],
  ['user-role-menu-link', "mysql -uroot -p123456aA@ know_boot_v1 -e \"SELECT 'user_role' t, COUNT(*) c FROM sys_user_role UNION ALL SELECT 'role_menu', COUNT(*) FROM sys_role_menu;\" 2>/dev/null"],
  ['tabbar-data', "mysql -uroot -p123456aA@ know_boot_v1 -e \"SELECT id,name,url,sort FROM sys_tabbar WHERE del_flag=0 ORDER BY sort;\" 2>/dev/null"],
  ['flyway-config-in-jar', "unzip -p /opt/ai-agent/apps/backend/know-boot-system.jar BOOT-INF/classes/application-prod.yml 2>/dev/null | grep -A3 -i flyway; echo ---; unzip -p /opt/ai-agent/apps/backend/know-boot-system.jar BOOT-INF/classes/application.yml 2>/dev/null | grep -A3 -i flyway"],
  ['db-directory-on-server', "ls -la /opt/ai-agent/ 2>&1; echo ---; find /opt/ai-agent -maxdepth 2 -name '*.sql' 2>/dev/null | head -20"],
];

conn.on('ready', () => {
  console.log('=== CHECK 2 ===\n');
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