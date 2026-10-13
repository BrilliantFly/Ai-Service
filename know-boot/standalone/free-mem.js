/** Free memory on remote: shrink MySQL buffer pool + enable swap, restart mysqld, verify. */
const { Client } = require('ssh2');
const fs = require('fs');

const conn = new Client();

async function exec(cmd, silent = false) {
  return new Promise((resolve, reject) => {
    conn.exec(cmd, (err, stream) => {
      if (err) return reject(err);
      let out = '';
      stream.on('data', d => { out += d.toString(); if (!silent) process.stdout.write(d.toString()); });
      stream.stderr.on('data', d => { out += d.toString(); if (!silent) process.stdout.write(d.toString()); });
      stream.on('close', () => resolve(out));
    });
  });
}

conn.on('ready', async () => {
  console.log('Connected.\n');
  try {
    // 0) snapshot current state
    await exec('echo "BEFORE---"; free -h | head -2; grep innodb_buffer_pool_size /etc/my.cnf');

    // 1) backup my.cnf (timestamped)
    await exec('cp /etc/my.cnf /etc/my.cnf.bak.$(date +%Y%m%d%H%M%S) && ls -la /etc/my.cnf.bak.* | tail -2');

    // 2) set innodb_buffer_pool_size = 256M (idempotent sed)
    await exec(`sed -i -E 's/innodb_buffer_pool_size\\s*=\\s*[0-9]+G/innodb_buffer_pool_size = 256M/' /etc/my.cnf`);
    await exec("grep -n innodb_buffer_pool_size /etc/my.cnf");

    // 3) enable swap usage (vm.swappiness)
    await exec('sysctl -w vm.swappiness=60 && echo "vm.swappiness=60" >> /etc/sysctl.d/99-swap.conf 2>/dev/null; cat /proc/sys/vm/swappiness');

    // 4) restart mysqld
    await exec('systemctl restart mysqld && echo "mysqld restart issued"');
    await exec('sleep 3; systemctl is-active mysqld');

    // 5) wait up to 90s for mysqld to be alive
    let alive = false;
    for (let i = 0; i < 30; i++) {
      const out = await exec(`mysqladmin -uroot -p123456aA@ ping 2>/dev/null | grep -o alive`, true);
      if (out.includes('alive')) { alive = true; break; }
      await new Promise(r => setTimeout(r, 3000));
    }
    console.log(alive ? '\nmysqld is ALIVE.' : '\nmysqld NOT alive after 90s!');

    // 6) verify buffer pool value + memory
    await exec(`mysql -uroot -p123456aA@ -N -e "SHOW VARIABLES LIKE 'innodb_buffer_pool_size';" 2>/dev/null`);
    await exec('sleep 20; free -h | head -2');

    conn.end();
    console.log('\n=== DONE ===');
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