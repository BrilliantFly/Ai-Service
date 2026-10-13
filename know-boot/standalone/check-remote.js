const { Client } = require('ssh2');
const fs = require('fs');

const conn = new Client();
const cmds = [
  ['server-info', 'cat /etc/os-release | head -2; echo ---; java -version 2>&1 | head -1; echo ---; nginx -v 2>&1; echo ---; free -h | head -2'],
  ['deploy-dirs', 'ls -la /opt/ai-agent/apps/ 2>&1; echo ---; ls -la /opt/ai-agent/apps/backend/ 2>&1; echo ---; ls -la /opt/ai-agent/apps/backend/*.jar 2>&1'],
  ['systemd', 'systemctl status know-boot-system --no-pager 2>&1 | head -10; echo ---; systemctl list-units --type=service | grep -i know 2>&1'],
  ['db-list', 'mysql -uroot -p123456aA@ -e "SHOW DATABASES;" 2>&1'],
  ['db-tables', 'mysql -uroot -p123456aA@ know_boot_v1 -e "SHOW TABLES;" 2>&1 | head -80'],
  ['migration-history', 'mysql -uroot -p123456aA@ know_boot_v1 -e "SELECT installed_rank, version, description, success FROM flyway_schema_history ORDER BY installed_rank;" 2>&1 | head -60'],
  ['port-8082', 'netstat -tlnp 2>/dev/null | grep ":8082 " || echo "8082 NOT LISTENING"'],
  ['redis-16379', 'ss -tlnp 2>/dev/null | grep ":16379 " || echo "16379 NOT LISTENING"'],
];

conn.on('ready', () => {
  console.log('=== SSH CONNECTED to 101.37.83.88 ===\n');
  (async () => {
    for (const [name, cmd] of cmds) {
      console.log('########## ' + name + ' ##########');
      await exec(cmd);
      console.log('');
    }
    conn.end();
    console.log('=== ALL CHECKS DONE ===');
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