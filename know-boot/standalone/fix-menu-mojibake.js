/** Fix mojibake in sys_menu_config: dry-run preview first, apply after manual review. */
const { Client } = require('ssh2');
const fs = require('fs');
const conn = new Client();
const APPLY = process.argv.includes('--apply');

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
  console.log('Connected. APPLY=' + APPLY + '\n');
  try {
    console.log('=== [0] columns ===');
    await exec("printf 'SHOW COLUMNS FROM sys_menu_config;' | mysql --default-character-set=utf8mb4 -uroot -p123456aA@ know_boot_v1 2>/dev/null");

    // Detect bad rows: value is all-latin1-representable (i.e. double-encoded), dry-run shows fix
    const detect = "SELECT id, menu_type, menu_name, " +
      "CONVERT(CONVERT(menu_name USING latin1) USING utf8mb4) AS menu_name_fixed, " +
      "icon, CONVERT(CONVERT(icon USING latin1) USING utf8mb4) AS icon_fixed, " +
      "render_config, CONVERT(CONVERT(render_config USING latin1) USING utf8mb4) AS render_fixed " +
      "FROM sys_menu_config WHERE " +
      "(CONVERT(menu_name USING latin1) = menu_name AND menu_name REGEXP '[^\\\\x00-\\\\x7F]') OR " +
      "(CONVERT(icon USING latin1) = icon AND icon REGEXP '[^\\\\x00-\\\\x7F]') OR " +
      "(CONVERT(render_config USING latin1) = render_config AND render_config REGEXP '[^\\\\x00-\\\\x7F]');";

    if (APPLY) {
      console.log('\n=== [APPLY] UPDATE ===');
      const update = "UPDATE sys_menu_config SET " +
        "menu_name = IF(CONVERT(menu_name USING latin1) = menu_name AND menu_name REGEXP '[^\\\\x00-\\\\x7F]', CONVERT(CONVERT(menu_name USING latin1) USING utf8mb4), menu_name), " +
        "icon = IF(CONVERT(icon USING latin1) = icon AND icon REGEXP '[^\\\\x00-\\\\x7F]', CONVERT(CONVERT(icon USING latin1) USING utf8mb4), icon), " +
        "big_icon = IF(CONVERT(big_icon USING latin1) = big_icon AND big_icon REGEXP '[^\\\\x00-\\\\x7F]', CONVERT(CONVERT(big_icon USING latin1) USING utf8mb4), big_icon), " +
        "selected_icon = IF(CONVERT(selected_icon USING latin1) = selected_icon AND selected_icon REGEXP '[^\\\\x00-\\\\x7F]', CONVERT(CONVERT(selected_icon USING latin1) USING utf8mb4), selected_icon), " +
        "render_config = IF(CONVERT(render_config USING latin1) = render_config AND render_config REGEXP '[^\\\\x00-\\\\x7F]', CONVERT(CONVERT(render_config USING latin1) USING utf8mb4), render_config) " +
        "WHERE (CONVERT(menu_name USING latin1) = menu_name AND menu_name REGEXP '[^\\\\x00-\\\\x7F]') OR " +
        "(CONVERT(icon USING latin1) = icon AND icon REGEXP '[^\\\\x00-\\\\x7F]') OR " +
        "(CONVERT(big_icon USING latin1) = big_icon AND big_icon REGEXP '[^\\\\x00-\\\\x7F]') OR " +
        "(CONVERT(selected_icon USING latin1) = selected_icon AND selected_icon REGEXP '[^\\\\x00-\\\\x7F]') OR " +
        "(CONVERT(render_config USING latin1) = render_config AND render_config REGEXP '[^\\\\x00-\\\\x7F]');";
      await exec("printf '" + update + "' | mysql --default-character-set=utf8mb4 -uroot -p123456aA@ know_boot_v1 2>/dev/null");
      console.log('\n=== [APPLY] verify remaining bad rows (should be 0) ===');
      await exec("printf 'SELECT COUNT(*) AS remaining_bad FROM sys_menu_config WHERE (CONVERT(menu_name USING latin1) = menu_name AND menu_name REGEXP \"[^\\\\x00-\\\\x7F]\");' | mysql --default-character-set=utf8mb4 -uroot -p123456aA@ know_boot_v1 2>/dev/null");
      console.log('\n=== [APPLY] verify home API ===');
      await exec("curl -s http://127.0.0.1:8082/api/system/menu/config/home | python3 -c \"import sys,json; d=json.load(sys.stdin); [print(m.get('menuName'), '|', m.get('icon')) for m in d.get('data',[])]\" 2>/dev/null || curl -s http://127.0.0.1:8082/api/system/menu/config/home | head -c 300");
    } else {
      console.log('\n=== [DRY-RUN] bad rows + fixed preview ===');
      await exec("printf '" + detect + "' | mysql --default-character-set=utf8mb4 -uroot -p123456aA@ know_boot_v1 2>/dev/null");
    }
    console.log('\n=== DONE ===');
  } catch (e) { console.error('ERR', e.message); }
  conn.end();
}).on('error', e => { console.error('CONN ERROR:', e.message); process.exit(1); });

conn.connect({
  host: '101.37.83.88', port: 22, username: 'root',
  privateKey: fs.readFileSync(process.env.USERPROFILE + '\\.ssh\\id_rsa'),
});