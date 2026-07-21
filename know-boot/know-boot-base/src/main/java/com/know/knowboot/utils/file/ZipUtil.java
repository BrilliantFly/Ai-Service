package com.know.knowboot.utils.file;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipOutputStream;

public class ZipUtil {
	private static final int buffer = 2048;
	private static final int BUFFER_SIZE = 2 * 1024;
	
	/**
	 * 解压到指定目录
	 * 
	 * @param zipPath
	 * @param descDir
	 * @author isea533
	 */
	@Deprecated
	public static void unZipFiles(String zipPath, String descDir) throws IOException {
		unZipFiles(new File(zipPath), descDir);
	}

	/**
	 * 解压文件到指定目录
	 * 
	 * @param zipFile
	 * @param descDir
	 * @author isea533
	 */
	@SuppressWarnings("rawtypes")
	public static void unZipFiles(File zipFile, String descDir) throws IOException {
		File pathFile = new File(descDir);
		if (!pathFile.exists()) {
			pathFile.mkdirs();
		}
		ZipFile zip = new ZipFile(zipFile);
		for (Enumeration entries = zip.getEntries(); entries.hasMoreElements();) {
			ZipEntry entry = (ZipEntry) entries.nextElement();
			String zipEntryName = entry.getName();
			InputStream in = zip.getInputStream(entry);
			String outPath = (descDir + zipEntryName).replaceAll("\\*", "/");
			outPath = new String(outPath.getBytes(StandardCharsets.UTF_8), "ISO8859-1");
			// 判断路径是否存在,不存在则创建文件路径
			File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
			if (!file.exists()) {
				file.mkdirs();
			}
			// 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
			if (new File(outPath).isDirectory()) {
				continue;
			}
			// 输出文件路径信息
			// System.out.println(outPath);

			OutputStream out = new FileOutputStream(outPath);
			byte[] buf1 = new byte[1024];
			int len;
			while ((len = in.read(buf1)) > 0) {
				out.write(buf1, 0, len);
			}
			in.close();
			out.close();
		}
	}

	/**
	 * 递归压缩文件
	 * 
	 * @param source
	 *            源路径,可以是文件,也可以目录
	 * @param destinct
	 *            目标路径,压缩文件名
	 * @throws IOException
	 */
	public static void compress(String source, String destinct) throws IOException {
		List fileList = loadFilename(new File(source));
		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(new File(destinct)));

		byte[] buffere = new byte[8192];
		int length;
		BufferedInputStream bis;

		for (int i = 0; i < fileList.size(); i++) {
			File file = (File) fileList.get(i);
			zos.putNextEntry(new ZipEntry(getEntryName(source, file)));
			bis = new BufferedInputStream(new FileInputStream(file));

			while (true) {
				length = bis.read(buffere);
				if (length == -1)
					break;
				zos.write(buffere, 0, length);
			}
			bis.close();
			zos.closeEntry();
		}
		zos.close();
	}

	/**
	 * 递归获得该文件下所有文件名(不包括目录名)
	 * 
	 * @param file
	 * @return
	 */
	private static List loadFilename(File file) {
		List filenameList = new ArrayList();
		if (file.isFile()) {
			filenameList.add(file);
		}
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				filenameList.addAll(loadFilename(f));
			}
		}
		return filenameList;
	}

	/**
	 * 获得zip entry 字符串
	 * 
	 * @param base
	 * @param file
	 * @return
	 */
	private static String getEntryName(String base, File file) {
		File baseFile = new File(base);
		String filename = file.getPath();
		// int index=filename.lastIndexOf(baseFile.getName());
		if (baseFile.getParentFile().getParentFile() == null)
			return filename.substring(baseFile.getParent().length());
		return filename.substring(baseFile.getParent().length() + 1);
	}

	/**
	 * 解压Zip文件
	 * 
	 * @param path 文件目录
	 */
	public static void unZip(String path, String savepath) {
		int count = -1;
		File file = null;
		InputStream is = null;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		new File(savepath).mkdir(); // 创建保存目录
		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(path, "gbk"); // 解决中文乱码问题
			Enumeration<?> entries = zipFile.getEntries();

			while (entries.hasMoreElements()) {
				byte[] buf = new byte[buffer];

				ZipEntry entry = (ZipEntry) entries.nextElement();

				String filename = entry.getName();
				boolean ismkdir = false;
				if (filename.lastIndexOf("/") != -1) { // 检查此文件是否带有文件夹
					ismkdir = true;
				}
				filename = savepath + filename;

				if (entry.isDirectory()) { // 如果是文件夹先创建
					file = new File(filename);
					file.mkdirs();
					continue;
				}
				file = new File(filename);
				if (!file.exists()) { // 如果是目录先创建
					if (ismkdir) {
						new File(filename.substring(0, filename.lastIndexOf("/"))).mkdirs(); // 目录先创建
					}
				}
				file.createNewFile(); // 创建文件

				is = zipFile.getInputStream(entry);
				fos = new FileOutputStream(file);
				bos = new BufferedOutputStream(fos, buffer);

				while ((count = is.read(buf)) > -1) {
					bos.write(buf, 0, count);
				}
				bos.flush();
				bos.close();
				fos.close();

				is.close();
			}

			zipFile.close();

		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (bos != null) {
					bos.close();
				}
				if (fos != null) {
					fos.close();
				}
				if (is != null) {
					is.close();
				}
				if (zipFile != null) {
					zipFile.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	

	/**
	 * 
	 * 压缩成ZIP 方法1
	 * 
	 * @param srcDir
	 *            压缩文件夹路径
	 * @param out
	 *            压缩文件输出流
	 * @param KeepDirStructure
	 *            是否保留原来的目录结构,true:保留目录结构;
	 *            false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
	 * @throws RuntimeException
	 *             压缩失败会抛出运行时异常
	 */
	public static void toZip(String srcDir, OutputStream out, boolean KeepDirStructure, String zipFileDic)
			throws RuntimeException {
		long start = System.currentTimeMillis();
		ZipOutputStream zos = null;
		try {
			zos = new ZipOutputStream(out);
			File sourceFile = new File(srcDir);
			compress(sourceFile, zos, zipFileDic, KeepDirStructure);//sourceFile.getName()
			long end = System.currentTimeMillis();
			System.out.println("压缩完成，耗时：" + (end - start) + " ms");
		} catch (Exception e) {
			throw new RuntimeException("zip error from ZipUtils", e);
		} finally {
			if (zos != null) {
				try {
					zos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 
	 * 压缩成ZIP 方法2
	 * 
	 * @param srcFiles
	 *            需要压缩的文件列表
	 * 
	 * @param out
	 *            压缩文件输出流
	 * 
	 * @throws RuntimeException
	 *             压缩失败会抛出运行时异常
	 * 
	 */
	public static void toZip(List<File> srcFiles, OutputStream out) throws RuntimeException {
		long start = System.currentTimeMillis();
		ZipOutputStream zos = null;
		try {
			zos = new ZipOutputStream(out);
			for (File srcFile : srcFiles) {
				byte[] buf = new byte[BUFFER_SIZE];
				zos.putNextEntry(new ZipEntry(srcFile.getName()));
				int len;
				FileInputStream in = new FileInputStream(srcFile);
				while ((len = in.read(buf)) != -1) {
					zos.write(buf, 0, len);
				}
				zos.closeEntry();
				in.close();
			}
			long end = System.currentTimeMillis();
			System.out.println("压缩完成，耗时：" + (end - start) + " ms");

		} catch (Exception e) {
			throw new RuntimeException("zip error from ZipUtils", e);
		} finally {
			if (zos != null) {
				try {
					zos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 递归压缩方法
	 * @param sourceFile
	 *            源文件
	 * @param zos
	 *            zip输出流
	 * @param name
	 *            压缩后的名称
	 * @param KeepDirStructure
	 *            是否保留原来的目录结构,true:保留目录结构;
	 *            false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
	 * @throws Exception
	 * 
	 */
	private static void compress(File sourceFile, ZipOutputStream zos, String name,
			boolean KeepDirStructure) throws Exception {
		byte[] buf = new byte[BUFFER_SIZE];
		if (sourceFile.isFile()) {
			// 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
			zos.putNextEntry(new ZipEntry(name));
			// copy文件到zip输出流中
			int len;
			FileInputStream in = new FileInputStream(sourceFile);
			while ((len = in.read(buf)) != -1) {
				zos.write(buf, 0, len);
			}
			// Complete the entry
			zos.closeEntry();
			in.close();
		} else {
			File[] listFiles = sourceFile.listFiles();
			if (listFiles == null || listFiles.length == 0) {
				// 需要保留原来的文件结构时,需要对空文件夹进行处理
				if (KeepDirStructure) {
					// 空文件夹的处理
					zos.putNextEntry(new ZipEntry(name + "/"));
					// 没有文件，不需要文件的copy
					zos.closeEntry();
				}
			} else {
				for (File file : listFiles) {
					// 判断是否需要保留原来的文件结构
					if (KeepDirStructure) {
						// 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
						// 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
						compress(file, zos, name + "/" + file.getName(), KeepDirStructure);
					} else {
						compress(file, zos, file.getName(), KeepDirStructure);
					}
				}
			}
		}
	}

	public static void main(String[] args) throws Exception {
		/** 测试压缩方法1 */
		FileOutputStream fos1 = new FileOutputStream(new File("C:/2019/青岛市自然科学奖/数理逻辑与数学基础1.zip"));
		ZipUtil.toZip("C:/2019/青岛市自然科学奖/数理逻辑与数学基础1", fos1, true, "2019/青岛市自然科学奖/数理逻辑与数学基础1");

		/** 测试压缩方法2 */
		/*
		 * List<File> fileList = new ArrayList<>();
		 * 
		 * fileList.add(new File("D:/Java/jdk1.7.0_45_64bit/bin/jar.exe"));
		 * 
		 * fileList.add(new File("D:/Java/jdk1.7.0_45_64bit/bin/java.exe"));
		 * 
		 * FileOutputStream fos2 = new FileOutputStream(new
		 * File("c:/mytest02.zip"));
		 * 
		 * ZipUtils.toZip(fileList, fos2);
		 */

		// compress("D:/tomcat-5.5.20","d:/test/testZip.zip");
		//unZip("D:/saas-plugin-web-master-shiro-mybatis.zip", "D:/123");
	}
}
