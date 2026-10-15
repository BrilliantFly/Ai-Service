/**
 * Ai-KnowBoot V2 Database Script Sync + Incremental Apply
 * 
 * 1) Uploads local db/ and migration/ SQL scripts to server /opt/ai-agent/db/
 * 2) Applies ONLY missing incremental scripts to know_boot_v1 (idempotent):
 *    - V1_1  sys_user_info (CREATE TABLE, only if table missing)
 *    - V1_3  sys_tenant_user (CREATE TABLE IF NOT EXISTS)
 *    - V1_8  focus menu (INSERT IGNORE, only if missing)
 *    - V1_10 industry menus (INSERT IGNORE, only if missing)
 *    - V1_12 kb_uploaded_file (CREATE TABLE IF NOT EXISTS)
 *    - V1_13 knowledge file-upload menu/data (INSERT ... WHERE NOT EXISTS)
 * 
 * Run: node db-sync.js
 */
const { Client } = require('ssh2');
const fs = require('fs');
const os = require('os');
const path = require('path');
const cp = require('child_process');

const KEY_PATH = process.env.USERPROFILE + '\\.ssh\\id_rsa';
const SERVER = {
  host: '101.37.83.88',
  port: 22,
  username: 'root',
  privateKey: fs.readFileSync(KEY_PATH),
};

const LOCAL_DB = __dirname + '\\..\\db';
const LOCAL_MIG = __dirname + '\\..\\know-boot-system\\src\\main\\resources\\db\\migration';
const REMOTE_DB_DIR = '/opt/ai-agent/db';
const REMOTE_MIG_DIR = '/opt/ai-agent/db/migration';
const MY = 'mysql -uroot -p123456aA@ know_boot_v1';

function sshExec(conn, cmd) {
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

function sftpUpload(conn, localPath, remotePath) {
  return new Promise((resolve, reject) => {
    conn.sftp((err, sftp) => {
      if (err) return reject(err);
      sftp.fastPut(localPath, remotePath, { concurrency: 8, chunkSize: 128 * 1024 }, e => {
        if (e) return reject(e);
        resolve();
      });
    });
  });
}

function tarDir(localDir, label) {
  const tmpTar = os.tmpdir() + '\\' + label + '.tar.gz';
  if (fs.existsSync(tmpTar)) fs.unlinkSync(tmpTar);
  return new Promise((resolve, reject) => {
    const tar = cp.spawn('tar', ['-czf', tmpTar, '-C', localDir, '.']);
    tar.on('close', code => code === 0 ? resolve(tmpTar) : reject(new Error('tar failed: ' + code)));
  });
}

async function main() {
  const conn = new Client();

  // ---- 0. Check local dirs ----
  if (!fs.existsSync(LOCAL_DB)) { console.error('[ERROR] db dir not found:', LOCAL_DB); process.exit(1); }
  if (!fs.existsSync(LOCAL_MIG)) { console.error('[ERROR] migration dir not found:', LOCAL_MIG); process.exit(1); }

  console.log('=== DB SYNC: upload + incremental apply ===\n');

  await new Promise((resolve, reject) => {
    conn.on('ready', resolve).on('error', reject);
    conn.connect(SERVER);
  });
  console.log('Connected to', SERVER.host, '\n');

  // ---- 1. Upload db/ and migration/ ----
  const dbTar = await tarDir(LOCAL_DB, 'db-scripts');
  const migTar = await tarDir(LOCAL_MIG, 'db-migration');
  console.log('Archives created.');

  console.log('\n[1/4] Uploading db/ scripts ...');
  await sftpUpload(conn, dbTar, '/tmp/db-scripts.tar.gz');
  await sshExec(conn, `rm -rf ${REMOTE_DB_DIR} && mkdir -p ${REMOTE_DB_DIR} && tar -xzf /tmp/db-scripts.tar.gz -C ${REMOTE_DB_DIR} && rm /tmp/db-scripts.tar.gz && find ${REMOTE_DB_DIR} -name "*.sql" | wc -l && echo "db/ uploaded"`);
  fs.unlinkSync(dbTar);

  console.log('\n[2/4] Uploading migration/ scripts ...');
  await sftpUpload(conn, migTar, '/tmp/db-migration.tar.gz');
  await sshExec(conn, `rm -rf ${REMOTE_MIG_DIR} && mkdir -p ${REMOTE_MIG_DIR} && tar -xzf /tmp/db-migration.tar.gz -C ${REMOTE_MIG_DIR} && rm /tmp/db-migration.tar.gz && find ${REMOTE_MIG_DIR} -name "*.sql" | wc -l && echo "migration/ uploaded"`);
  fs.unlinkSync(migTar);

  // ---- 2. Check which tables/menus exist ----
  console.log('\n[3/4] Checking remote DB diff ...');
  const exists = {};
  const nameQ = async (label, sql) => {
    const out = await sshExec(conn, `${MY} -N -e "${sql}" 2>/dev/null | tr -d '[:space:]'`);
    exists[label] = out.length > 0 && out !== '0';
    console.log(`  ${label}: ${exists[label] ? 'PRESENT' : 'MISSING'}${out ? ' (' + out + ')' : ''}`);
  };
  await nameQ('sys_user_info', "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema='know_boot_v1' AND table_name='sys_user_info'");
  await nameQ('sys_tenant_user', "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema='know_boot_v1' AND table_name='sys_tenant_user'");
  await nameQ('menu_focus', "SELECT COUNT(*) FROM sys_menu_config WHERE menu_code='focus'");
  await nameQ('menu_industry', "SELECT COUNT(*) FROM sys_menu_config WHERE menu_code='industry'");
  await nameQ('kb_uploaded_file', "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema='know_boot_v1' AND table_name='kb_uploaded_file'");
  await nameQ('menu_file_upload', "SELECT COUNT(*) FROM sys_menu WHERE path='/knowledge/file' AND del_flag=0");

  // ---- 3. Apply missing incremental scripts ----
  console.log('\n[4/4] Applying missing scripts ...');

  if (!exists.sys_user_info) {
    console.log('  >> Applying V1_1__sys_user.sql ...');
    await sshExec(conn, `${MY} < ${REMOTE_MIG_DIR}/V1_1__sys_user.sql 2>&1 && echo "  V1_1 OK"`);
  } else {
    console.log('  >> Skip V1_1 (sys_user_info exists)');
  }

  if (!exists.sys_tenant_user) {
    console.log('  >> Applying V1_3__sys_tenant_user.sql ...');
    await sshExec(conn, `${MY} < ${REMOTE_MIG_DIR}/V1_3__sys_tenant_user.sql 2>&1 && echo "  V1_3 OK"`);
  } else {
    console.log('  >> Skip V1_3 (sys_tenant_user exists)');
  }

  if (!exists.menu_focus) {
    console.log('  >> Applying V1_8__sys_menu_config_focus.sql ...');
    await sshExec(conn, `${MY} < ${REMOTE_MIG_DIR}/V1_8__sys_menu_config_focus.sql 2>&1 && echo "  V1_8 OK"`);
  } else {
    console.log('  >> Skip V1_8 (focus menu exists)');
  }

  if (!exists.menu_industry) {
    console.log('  >> Applying V1_10__sys_menu_config_industry.sql ...');
    await sshExec(conn, `${MY} < ${REMOTE_MIG_DIR}/V1_10__sys_menu_config_industry.sql 2>&1 && echo "  V1_10 OK"`);
  } else {
    console.log('  >> Skip V1_10 (industry menus exist)');
  }

  if (!exists.kb_uploaded_file) {
    console.log('  >> Applying V1_12__kb_uploaded_file.sql ...');
    await sshExec(conn, `${MY} < ${REMOTE_MIG_DIR}/V1_12__kb_uploaded_file.sql 2>&1 && echo "  V1_12 OK"`);
  } else {
    console.log('  >> Skip V1_12 (kb_uploaded_file exists)');
  }

  if (!exists.menu_file_upload) {
    console.log('  >> Applying V1_13__sys_menu_file_upload.sql ...');
    await sshExec(conn, `${MY} < ${REMOTE_MIG_DIR}/V1_13__sys_menu_file_upload.sql 2>&1 && echo "  V1_13 OK"`);
  } else {
    console.log('  >> Skip V1_13 (file-upload menu exists)');
  }

  // ---- 4. Verify ----
  console.log('\n=== VERIFY ===');
  await sshExec(conn, "mysql -uroot -p123456aA@ know_boot_v1 -e \"SELECT id,menu_name,menu_code,menu_type,path,sort FROM sys_menu_config WHERE menu_code IN ('focus','industry','industry-enterprise','industry-product','industry-market','file-upload');\" 2>/dev/null");
  await sshExec(conn, "mysql -uroot -p123456aA@ know_boot_v1 -e \"SELECT id,parent_id,menu_name,path,component,perms,menu_type,sort FROM sys_menu WHERE path='/knowledge/file' OR perms LIKE 'knowledge:file:%' ORDER BY menu_type,sort;\" 2>/dev/null");
  await sshExec(conn, "mysql -uroot -p123456aA@ know_boot_v1 -N -e \"SELECT CONCAT('kb_uploaded_file table exists: ', COUNT(*)) FROM information_schema.tables WHERE table_schema='know_boot_v1' AND table_name='kb_uploaded_file';\" 2>/dev/null");
  console.log('\n=== DB SYNC COMPLETE ===');

  conn.end();
}

main().catch(err => {
  console.error('\n[FATAL]', err.message);
  process.exit(1);
});