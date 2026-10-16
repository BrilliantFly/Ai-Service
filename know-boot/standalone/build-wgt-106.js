/**
 * Build 1.0.6 wgt package locally (no HBuilderX GUI needed).
 *
 * Reproduces HBuilderX "制作应用wgt包":
 *   1. take the `uni build -p app` output  (dist/build/app)
 *   2. read manifest.json, strip non-shippable nodes (plus.distribute / confusion / unpackage / dependencies)
 *   3. copy resources to a temp dir + write the minified manifest
 *   4. zip the temp dir with archiver (same lib/method as app-safe-pack/util.zipRecource)
 *
 * Output:
 *   dist/know-android-1.0.6.wgt
 *   dist/know-ios-1.0.6.wgt      (identical content — App wgt is platform-agnostic here)
 *
 * 1.0.6 修复：文件导入选择知识库后上传失败（上传请求未携带登录态、multipart 编码被覆盖）。
 */
const fs = require('fs');
const os = require('os');
const path = require('path');
const archiver = require('E:/software/HBuilderX/HBuilderX/plugins/app-safe-pack/node_modules/archiver');

const SRC = 'E:\\Ai-Project\\Ai-Front\\know-uniapp\\dist\\build\\app';
const OUT_DIR = 'E:\\Ai-Project\\Ai-Front\\know-uniapp\\dist';
const VERSION_NAME = '1.0.6';
const VERSION_CODE = '106';

function copyDir(src, dst) {
    fs.mkdirSync(dst, { recursive: true });
    for (const entry of fs.readdirSync(src, { withFileTypes: true })) {
        const s = path.join(src, entry.name);
        const d = path.join(dst, entry.name);
        if (entry.isDirectory()) copyDir(s, d);
        else fs.copyFileSync(s, d);
    }
}

function zipDir(dir, outFile) {
    return new Promise((resolve, reject) => {
        const output = fs.createWriteStream(outFile);
        const zip = archiver('zip');
        zip.glob('**/*', { cwd: dir, dot: true }, {});
        zip.pipe(output);
        output.on('close', resolve);
        output.on('error', reject);
        zip.on('warning', reject);
        zip.on('error', reject);
        zip.finalize();
    });
}

(async () => {
    if (!fs.existsSync(SRC)) {
        console.error('[ERROR] build output not found:', SRC);
        console.error('Run: cd know-uniapp && uni build -p app --mode app');
        process.exit(1);
    }
    const mfPath = path.join(SRC, 'manifest.json');
    const mf = JSON.parse(fs.readFileSync(mfPath, 'utf8'));
    if (mf.plus) {
        delete mf.plus.distribute;
        delete mf.plus.confusion;
        delete mf.plus.unpackage;
        delete mf.plus.dependencies;
    }
    mf.version = { name: VERSION_NAME, code: VERSION_CODE };
    console.log('[wgt] appid=%s  version=%s/%s', mf.id, mf.version.name, mf.version.code);

    const tmp = path.join(os.tmpdir(), 'wgt-build-' + Date.now());
    copyDir(SRC, tmp);
    fs.writeFileSync(path.join(tmp, 'manifest.json'), JSON.stringify(mf));

    const android = path.join(OUT_DIR, 'know-android-' + VERSION_NAME + '.wgt');
    const ios = path.join(OUT_DIR, 'know-ios-' + VERSION_NAME + '.wgt');
    await zipDir(tmp, android);
    fs.copyFileSync(android, ios);
    fs.rmSync(tmp, { recursive: true, force: true });

    for (const f of [android, ios]) {
        console.log('  %s  %s KB', f, (fs.statSync(f).size / 1024).toFixed(0));
    }
    console.log('[wgt] done');
})().catch(e => { console.error('[FATAL]', e); process.exit(1); });
