package com.know.knowboot.utils.ftp;

import com.know.knowboot.utils.file.ZipUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;
import org.apache.commons.net.util.TrustManagerUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * Ftp上传下载工具类
 */
@Slf4j
public class FTPSClientUtils {

    private static final String protocol = "TLS";
    private static final String encoding = "UTF-8";


    /**
     * 通过TLS连接FTP
     * @param host
     * @param port
     * @param username
     * @param password
     * @return
     */
    public static FTPSClient connect(String host, int port, String username, String password) {
        FTPSClient ftpsClient = new FTPSClient(protocol);
        ftpsClient.setTrustManager(TrustManagerUtils.getAcceptAllTrustManager());
        ftpsClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        ftpsClient.setControlEncoding(encoding);
        try {
            ftpsClient.connect(host, port);
            ftpsClient.login(username, password);
            // 获取FTP Server应答
            int replyCode = ftpsClient.getReplyCode();
            log.info("通过TLS连接FTP - FTPServer应答->{}", replyCode);
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                log.error("通过TLS连接FTP - 连接失败");
            }
            return ftpsClient;
        } catch (IOException e) {
            log.error("通过TLS连接FTP - 异常", e);
        }

        return null;
    }


    /**
     * 断开FTP连接
     * @param ftpsClient
     */
    public static void close(FTPSClient ftpsClient) {
        if (ftpsClient != null && ftpsClient.isConnected()) {
            try {
                ftpsClient.logout();
                ftpsClient.disconnect();
            } catch (IOException e) {
                log.error("FTP断开异常", e);
            }
        }
    }

    /**
     * 获取文件列表
     * @param ftpsClient
     * @return
     */
    public static List<FTPFile> dirList(FTPSClient ftpsClient) {
        List<FTPFile> list = null;
        try {
            ftpsClient.enterLocalPassiveMode();
            FTPFile ff[] = ftpsClient.listFiles("/");
            if (ff != null && ff.length > 0) {
                list = new ArrayList<>(ff.length);
                Collections.addAll(list, ff);
            } else {
                list = new ArrayList<>(0);
            }
        } catch (IOException e) {
            log.error("FTP获取文件列表异常", e);
        }
        return list;
    }

    /**
     * 切换目录
     * @param ftpsClient  ftps客户端
     * @param path  切换路径
     * @param forcedIncrease 是否强制创建
     * @return
     */
    public static boolean switchDir(FTPSClient ftpsClient, String path, boolean forcedIncrease) {
        try {
            if (StringUtils.isNotBlank(path)) {
                boolean is = ftpsClient.changeWorkingDirectory(path);
                if (is) {
                    return true;
                }
                // ftpPath不存在，手动创建目录
                if (forcedIncrease) {
                    StringTokenizer token = new StringTokenizer(path, "\\//");
                    while (token.hasMoreTokens()) {
                        String nextPath = token.nextToken();
                        ftpsClient.makeDirectory(nextPath);
                        ftpsClient.changeWorkingDirectory(nextPath);
                    }
                }
            }
        } catch (Exception e) {
            log.error("FTP切换目录异常", e);
        }
        return false;
    }


    /**
     * 切换目录
     * @param ftpsClient  ftps客户端
     * @param path  切换路径
     * @param forcedIncrease 是否强制创建
     * @return
     */
    public static boolean switchDirInfo(FTPSClient ftpsClient, String path, boolean forcedIncrease) {
        try {
            if (StringUtils.isNotBlank(path)) {
                boolean is = ftpsClient.changeWorkingDirectory(path);
                if (is) {
                    return true;
                }
                // ftpPath不存在，手动创建目录
                if (forcedIncrease) {
                    // 该部分为逐级创建
                    String[] split = path.split("/");
                    if (split != null && split.length > 0){
                        for (String str : split) {
                            if(StringUtils.isBlank(str)) {
                                continue;
                            }
                            if (!ftpsClient.changeWorkingDirectory(str)) {
                                log.info(str + "不存在");
                                boolean makeDirectory = ftpsClient.makeDirectory(str);
                                boolean changeWorkingDirectory = ftpsClient.changeWorkingDirectory(str);
                                log.info(str + "创建：" + makeDirectory + ";切换：" + changeWorkingDirectory);
                            } else {
                                System.err.println("存在");
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("FTP切换目录异常", e);
        }
        return false;
    }


    /**
     * 下载
     * @param ftpsClient
     * @param ftpFileName   ftp路径加文件名：/attach/aa.jpg
     * @param savePath
     * @param resourcePath  文件后缀
     * @param fileSuffix
     * @return
     */
    public static String download(FTPSClient ftpsClient, String ftpFileName, String savePath, String resourcePath, String fileSuffix) {
        File dir = new File(savePath + File.separator + resourcePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        FileOutputStream fos = null;
        try {
            String newFilename = UUID.randomUUID().toString() + fileSuffix;
            String saveFile = dir.getAbsolutePath() + File.separator + newFilename;
            fos = new FileOutputStream(new File(saveFile));
            ftpsClient.enterLocalPassiveMode();
            boolean is = ftpsClient.retrieveFile(ftpFileName, fos);
            fos.flush();
            if (is) {
                return newFilename;
            }
        } catch (IOException e) {
            log.error("FTP下载文件异常", e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * SSL下载
     * @param ftpsClient
     * @param ftpFileName   ftp路径加文件名：/attach/aa.jpg
     * @param savePath
     * @param resourcePath  文件后缀
     * @param fileSuffix
     * @return
     */
    public static String sslDownload(FTPSClient ftpsClient, String ftpFileName, String savePath, String resourcePath, String fileSuffix) {
        File dir = new File(savePath + File.separator + resourcePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        FileOutputStream fos = null;
        try {
            String newFilename = UUID.randomUUID().toString() + fileSuffix;
            String saveFile = dir.getAbsolutePath() + File.separator + newFilename;
            fos = new FileOutputStream(new File(saveFile));
            ftpsClient.enterLocalPassiveMode();
            // SSL重点(解决521 PROT P required)
            ftpsClient.execPROT("P");

            boolean is = ftpsClient.retrieveFile(ftpFileName, fos);
            fos.flush();
            if (is) {
                return newFilename;
            }
        } catch (IOException e) {
            log.error("FTP下载文件异常", e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 下载
     * @param ftpsClient
     * @param ftpFileName   ftp路径加文件名：/attach/aa.jpg
     * @param savePath
     * @param resourcePath  文件后缀
     * @param fileSuffix
     * @return
     */
    public static void webDownload(HttpServletResponse response, FTPSClient ftpsClient, String ftpFileName, String savePath, String resourcePath, String fileSuffix) {

        FTPSClient client = null;

        try {

            // 文件下载
            client.enterLocalPassiveMode();

            client.execPROT("P");

            InputStream inStream = client.retrieveFileStream(new String(ftpFileName.getBytes("UTF-8"), "iso-8859-1"));

            response.reset();
            response.setContentType("bin");
//            response.addHeader("Content-Disposition", "attachment; filename=\"" + newFilename + "\"");
            response.addHeader("Content-Disposition", "attachment; filename=" + new String(ftpFileName.getBytes("utf-8"), "ISO8859-1"));
            // 循环取出流中的数据
            byte[] b = new byte[100];
            int len;
            try {
                while ((len = inStream.read(b)) > 0) {
                    response.getOutputStream().write(b, 0, len);
                }
                inStream.close();
            } catch (IOException e) {
                log.error("单个附件下载 - 根据文件名下载文件异常 -> {}", e.getMessage());
                e.printStackTrace();
            }

        } catch (IOException e) {
            log.error("根据文件名下载文件异常,filename:{}", ftpFileName, e);
            throw new RuntimeException("根据文件名下载文件异常,filename:" + ftpFileName);
        } finally {
            FTPSClientUtils.close(client);
        }
    }

//    public void downloadFile(String filename, HttpServletResponse response) {
//
//        FTPSClient client = null;
//
//        // 文件名
//        String ftpFileName = attachProperties.getUploadPath() + "/20220620/34185bafdf514756ea9078e7b002b4bf.jpeg";
//
//        String newFilename = "34185bafdf514756ea9078e7b002b4bf.jpeg";
//
//        client = FTPSClientUtils.connect(ftpProperties.getHost(), ftpProperties.getPort(), ftpProperties.getUsername(), ftpProperties.getPassword());
//        if (null == client || !client.isConnected()) {
//            log.error("根据文件名下载文件失败,获取ftpClient失败,filename:{}", filename);
//        }
//        try {
//            BufferedInputStream bis = null;
//            client.enterLocalPassiveMode();
//            InputStream inStream = client.retrieveFileStream(new String(ftpFileName.getBytes("UTF-8"), "iso-8859-1"));
//
//            // 读到流中
//            // 设置输出的格式
//            response.reset();
//            response.setContentType("bin");
//            response.addHeader("Content-Disposition", "attachment; filename=\"" + newFilename + "\"");
//            // 循环取出流中的数据
//            byte[] b = new byte[100];
//            int len;
//            try {
//                while ((len = inStream.read(b)) > 0)
//                    response.getOutputStream().write(b, 0, len);
//                inStream.close();
//            } catch (IOException e) {
//                log.error("单个附件下载 - 文件下载异常异常 -> {}", e.getMessage());
//                e.printStackTrace();
//            }
//
//
//        } catch (IOException e) {
//            log.error("根据文件名下载文件异常,filename:{}", filename, e);
//        } finally {
//            FTPSClientUtils.close(client);
//        }
//    }
//
//    public void showFile(String filename, HttpServletResponse response) {
//
//        // 文件名
//        String ftpFileName = attachProperties.getUploadPath() + "/20220620/34185bafdf514756ea9078e7b002b4bf.jpeg";
//        FTPSClient client = null;
//
//        client = FTPSClientUtils.connect(ftpProperties.getHost(), ftpProperties.getPort(), ftpProperties.getUsername(), ftpProperties.getPassword());
//        if (null == client || !client.isConnected()) {
//            log.error("根据文件名单个附件预览,获取ftpClient失败,filename:{}", filename);
//        }
//        try {
//            OutputStream out = response.getOutputStream();
//            BufferedInputStream bis = null;
//            client.enterLocalPassiveMode();
//            InputStream is = client.retrieveFileStream(new String(ftpFileName.getBytes("UTF-8"), "iso-8859-1"));
//            bis = new BufferedInputStream(is);
//            byte[] buf = new byte[1024];
//            int len = 0;
//            while ((len = bis.read(buf)) > 0) {
//                out.write(buf, 0, len);
//            }
//            out.flush();
//            bis.close();
//            is.close();
//        } catch (IOException e) {
//            log.error("根据文件名下载文件异常,filename:{}", filename, e);
//        } finally {
//            FTPSClientUtils.close(client);
//        }
//    }

    /**
     * 上传文件，会覆盖FTP上原有文件
     *
     * @param ftpsClient
     * @param localFile
     * @param reName
     * @author niu
     * @createDate 2022/1/20
     */
    public static boolean upload(FTPSClient ftpsClient, File localFile, String reName) {
        String targetName = reName;
        // 设置上传后文件名
        if (reName == null || "".equals(reName)) {
            targetName = localFile.getName();
        }
        FileInputStream fis = null;
        try {
            // 开始上传文件
            fis = new FileInputStream(localFile);
            ftpsClient.setControlEncoding(encoding);
            ftpsClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpsClient.enterLocalPassiveMode();
            boolean is = ftpsClient.storeFile(targetName, fis);
            return is;
        } catch (IOException e) {
            log.error("FTP上传文件异常", e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * SSL上传文件，会覆盖FTP上原有文件
     *
     * @param ftpsClient
     * @param localFile
     * @param reName
     * @author niu
     * @createDate 2022/1/20
     */
    public static boolean sslUpload(FTPSClient ftpsClient, File localFile, String reName) {
        String targetName = reName;
        if (reName == null || "".equals(reName)) {
            targetName = localFile.getName();
        }
        FileInputStream fis = null;
        try {

            fis = new FileInputStream(localFile);
            ftpsClient.setFileType(ftpsClient.BINARY_FILE_TYPE);
            // SSL重点
            ftpsClient.execPBSZ(0);
            ftpsClient.execPROT("P");
            ftpsClient.setControlEncoding("UTF-8");
            ftpsClient.enterLocalPassiveMode();

            boolean is = ftpsClient.storeFile(targetName, fis);
            boolean var6 = is;
            return var6;
        } catch (IOException var16) {
            log.error("FTP上传文件异常", var16);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException var15) {
                    var15.printStackTrace();
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {

//        StringTokenizer token = new StringTokenizer("kjxy\\contract", "\\//");
//        while (token.hasMoreTokens()) {
//            String nextPath = token.nextToken();
//            log.info(nextPath);
//        }

//        FTPSClient client = connect("192.168.1.252", 2021, "qdggzytest", "Flyedt@252");
//        switchDir(client, "kjxy\\contracta\\aaa", true);
//        upload(client, new File("D:\\test111.txt"), "test.txt");
//        close(client);

//        FTPSClient client = connect("192.168.1.252", 2021, "qdggzytest", "Flyedt@252");
//        switchDir(client, "kjxy\\contract", false);
//        download(client, "test.txt", "D:\\tmp", "kjxy\\contract", ".txt");
//        close(client);

          FTPSClient client = connect("www.flyedt.com", 2019, "flyerp", "Flyedtqderp@201921");
          switchDir(client, "/erp/480085442221509/2023-12", false);
          String fileName = sslDownload(client, "E9209BA0-E5DC-4849-AC39-89D512AEC2A3_xls", "D:\\file", "kjxy\\contract", ".zip");
          close(client);

          // 解压
          ZipUtil.unZip("D:\\file\\kjxy\\contract\\" + fileName,"D:\\file\\kjxy\\contract\\");

//        String suffix = ".pdf";
//        String suffixTmp = ftpFileName.substring(ftpFileName.lastIndexOf("."));
//        if (StringUtils.isNotBlank(suffixTmp)) {
//            suffix = suffixTmp;
//        }
//        download(client, "/UpLoadFiles/ztbFile/05e528eb-61a7-45b2-81d4-7d4550df8bd6/99b36d5a-28e5-4497-8080-604d22e8963e", "D:\\tmp", ".pdf");

//        client = FTPSClientUtils.connect(ftpProperties.getHost(), ftpProperties.getPort(), ftpProperties.getUsername(), ftpProperties.getPassword());
//        if (Objects.isNull(client)) {
//            log.error("单个附件下载 - FTP连接失败");
//            for (int i = 1; i <= 5; i++) {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                log.info("单个附件下载 - 第{}次重试FTP连接", i);
//                client = FTPSClientUtils.connect(ftpProperties.getHost(), ftpProperties.getPort(), ftpProperties.getUsername(), ftpProperties.getPassword());
//                if (Objects.isNull(client)) {
//                    log.error("单个附件下载 -第{}次重试FTP连接 - FTP连接失败", i);
//                    continue;
//                }
//                break;
//            }
//        }

    }


}
