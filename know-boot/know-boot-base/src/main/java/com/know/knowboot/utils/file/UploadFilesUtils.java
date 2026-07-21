package com.know.knowboot.utils.file;
import com.know.knowboot.utils.date.LocalDateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.time.LocalDate;

/**
 * 文件上传工具类
 */
@Slf4j
@Component
public class UploadFilesUtils {


//    @Value("${flyedt.uploadFile.path}")
    private String filePath;

//    @Value("${flyedt.uploadFile.form-path}")
    private String formPath;

    /**
     * 文件上传
     *
     * @param httpServletRequest
     * @param response
     * @return
     */
    public void uploadFiles(HttpServletRequest httpServletRequest, HttpServletResponse response) {

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) httpServletRequest;

        // 获取上传文件对象
        MultipartFile mf = multipartRequest.getFile("file");
        // 附件原名称
        String orgName = mf.getOriginalFilename();
        // 获取文件扩展名
        String fileExt = getExtend(orgName);
        // 生成保存文件名
        String fileName = orgName.substring(0, orgName.lastIndexOf(".")) + "_" + System.currentTimeMillis() + orgName.substring(orgName.indexOf("."));
        // 获取文件大小
        Long fileSize = mf.getSize();

        String localDateStr = LocalDateUtils.format(LocalDate.now(), LocalDateUtils.UNSIGNED_DATE_PATTERN);
        // 基础路径
        String basePath = filePath;
        // 保存路径
        String savePath = basePath + "/" + localDateStr + "/";
        // 真实路径
        String realPath = savePath+ "/" + fileName;
        // 相对路径
        String absolutePath = localDateStr + "/" + fileName;

        try {
            File savefile = new File(savePath);
            // 校验文件目录是否存在
            if (!savefile.exists()) {
                savefile.mkdirs();
            }

            File realFile = new File(realPath);
            FileCopyUtils.copy(mf.getBytes(), realFile);
        } catch (Exception e) {
            log.error("文件上传异常 - > {}", e.getMessage());
        }

    }

    /**
     * 文件删除
     * @param delpath
     * @return
     */
    public void deleteFiles(String delpath){
        File fileDelete = new File(delpath);
        if (!fileDelete.exists() || !fileDelete.isFile()) {
            log.error("警告: " + delpath + "不存在!");
        }else{
            if(fileDelete.delete()){
                log.error("成功删除文件");
            }else{
                log.error("没删除成功,请重新试试");
            }
        }
    }


    /**
     * 获取文件扩展名
     *
     * @param filename
     * @return
     */
    public static String getExtend(String filename) {
        return getExtend(filename, "");
    }

    /**
     * 获取文件扩展名
     *
     * @param filename
     * @return
     */
    public static String getExtend(String filename, String defExt) {
        if ((filename != null) && (filename.length() > 0)) {
            int i = filename.lastIndexOf('.');

            if ((i > 0) && (i < (filename.length() - 1))) {
                return (filename.substring(i+1)).toLowerCase();
            }
        }
        return defExt.toLowerCase();
    }

    /**
     * 获取文件名称[不含后缀名]
     *
     * @param
     * @return String
     */
    public  String getFilePrefix(String fileName) {
        int splitIndex = fileName.lastIndexOf(".");
        return fileName.substring(0, splitIndex).replaceAll("\\s*", "");
    }

    /**
     * 获取文件名称[不含后缀名]
     * 不去掉文件目录的空格
     * @param
     * @return String
     */
    public  String getFilePrefix2(String fileName) {
        int splitIndex = fileName.lastIndexOf(".");
        return fileName.substring(0, splitIndex);
    }

    /**
     * 文件复制
     *方法摘要：这里一句话描述方法的用途
     *@param
     *@return void
     */
    public  void copyFile(String inputFile,String outputFile) throws FileNotFoundException {
        File sFile = new File(inputFile);
        File tFile = new File(outputFile);
        FileInputStream fis = new FileInputStream(sFile);
        FileOutputStream fos = new FileOutputStream(tFile);
        int temp = 0;
        byte[] buf = new byte[10240];
        try {
            while((temp = fis.read(buf))!=-1){
                fos.write(buf, 0, temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try {
                fis.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 判断文件是否为图片<br>
     * <br>
     *
     * @param filename
     *            文件名<br>
     *            判断具体文件类型<br>
     * @return 检查后的结果<br>
     * @throws Exception
     */
    public  boolean isPicture(String filename) {
        // 文件名称为空的场合
        if (StringUtils.isEmpty(filename)) {
            // 返回不和合法
            return false;
        }
        // 获得文件后缀名
        //String tmpName = getExtend(filename);
        String tmpName = filename;
        // 声明图片后缀名数组
        String imgeArray[][] = { { "bmp", "0" }, { "dib", "1" },
                { "gif", "2" }, { "jfif", "3" }, { "jpe", "4" },
                { "jpeg", "5" }, { "jpg", "6" }, { "png", "7" },
                { "tif", "8" }, { "tiff", "9" }, { "ico", "10" } };
        // 遍历名称数组
        for (int i = 0; i < imgeArray.length; i++) {
            // 判断单个类型文件的场合
            if (imgeArray[i][0].equals(tmpName.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @Title: encodingFileName 2015-11-26 huangzq add
     * @Description: 防止文件名中文乱码含有空格时%20
     * @param @param fileName
     * @param @return    设定文件
     * @return String    返回类型
     * @throws
     */
    public  String encodingFileName(String fileName) {
        String returnFileName = "";
        try {
            returnFileName = URLEncoder.encode(fileName, "UTF-8");
            returnFileName = StringUtils.replace(returnFileName, "+", "%20");
            if (returnFileName.length() > 150) {
                returnFileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
                returnFileName = StringUtils.replace(returnFileName, " ", "%20");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            log.info("Don't support this encoding ...");
        }
        return returnFileName;
    }


    /**
     * 上传txt文件，防止乱码
     * @author taoYan
     * @since 2018年7月26日
     */
    public  void uploadTxtFile(MultipartFile mf, String savePath) throws IOException{
        //利用utf-8字符集的固定首行隐藏编码原理
        //Unicode:FF FE   UTF-8:EF BB
        byte[] allbytes = mf.getBytes();
        try{
            String head1 = toHexString(allbytes[0]);
            //System.out.println(head1);
            String head2 = toHexString(allbytes[1]);
            //System.out.println(head2);
            if("ef".equals(head1) && "bb".equals(head2)){
                //UTF-8
                String contents = new String(mf.getBytes(),"UTF-8");
                if(StringUtils.isNotBlank(contents)){
                    OutputStream out = new FileOutputStream(savePath);
                    out.write(contents.getBytes());
                    out.close();
                }
            }  else {

                //GBK
                String contents = new String(mf.getBytes(),"GBK");
                OutputStream out = new FileOutputStream(savePath);
                out.write(contents.getBytes());
                out.close();

            }
        } catch(Exception e){
            String contents = new String(mf.getBytes(),"UTF-8");
            if(StringUtils.isNotBlank(contents)){
                OutputStream out = new FileOutputStream(savePath);
                out.write(contents.getBytes());
                out.close();
            }
        }
    }

    public  String toHexString(int index){
        String hexString = Integer.toHexString(index);
        // 1个byte变成16进制的，只需要2位就可以表示了，取后面两位，去掉前面的符号填充
        hexString = hexString.substring(hexString.length() -2);
        return hexString;
    }

    /*判断文件是否存在*/
    public static boolean isExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    /*判断是否是文件夹*/
    public static boolean isDir(String path) {
        File file = new File(path);
        if(file.exists()){
            return file.isDirectory();
        }else{
            return false;
        }
    }

    /**
     * 文件或者目录重命名
     * @param oldFilePath 旧文件路径
     * @param newName 新的文件名,可以是单个文件名和绝对路径
     * @return
     */
    public static boolean renameTo(String oldFilePath, String newName) {
        try {
            File oldFile = new File(oldFilePath);
            //若文件存在
            if(oldFile.exists()){
                //判断是全路径还是文件名
                if (newName.indexOf("/") < 0 && newName.indexOf("\\") < 0){
                    //单文件名，判断是windows还是Linux系统
                    String absolutePath = oldFile.getAbsolutePath();
                    if(newName.indexOf("/") > 0){
                        //Linux系统
                        newName = absolutePath.substring(0, absolutePath.lastIndexOf("/") + 1)  + newName;
                    }else{
                        newName = absolutePath.substring(0, absolutePath.lastIndexOf("\\") + 1)  + newName;
                    }
                }
                File file = new File(newName);
                //判断重命名后的文件是否存在
                if(file.exists()){
                    System.out.println("该文件已存在,不能重命名");
                }else{
                    //不存在，重命名
                    return oldFile.renameTo(file);
                }
            }else {
                System.out.println("原该文件不存在,不能重命名");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /*文件拷贝操作*/
    public static void copy(String sourceFile, String targetFile) {
        File source = new File(sourceFile);
        File target = new File(targetFile);
        target.getParentFile().mkdirs();
        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel in = null;
        FileChannel out = null;
        try {
            fis = new FileInputStream(source);
            fos = new FileOutputStream(target);
            in = fis.getChannel();//得到对应的文件通道
            out = fos.getChannel();//得到对应的文件通道
            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null){
                    out.close();
                }
                if (in != null){
                    in.close();
                }
                if (fos != null){
                    fos.close();
                }
                if (fis != null){
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*读取Text文件操作*/
    public static String readText(String filePath) {
        String lines = "";
        try {
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                lines += line + "\n";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lines;
    }

    /*写入Text文件操作*/
    public static void writeText(String filePath, String content,boolean isAppend) {
        FileOutputStream outputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bufferedWriter = null;
        try {
            outputStream = new FileOutputStream(filePath,isAppend);
            outputStreamWriter = new OutputStreamWriter(outputStream);
            bufferedWriter = new BufferedWriter(outputStreamWriter);
            bufferedWriter.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try{
                if(bufferedWriter != null){
                    bufferedWriter.close();
                }
                if (outputStreamWriter != null){
                    outputStreamWriter.close();
                }
                if (outputStream != null){
                    outputStream.close();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 汪敏 2017-12-20
     * 服务器通用图片批量上传
     * @param picture 大文本对象数组
     * @param mp4TempDir 文件保存全路径
     * @return
     */
    public static boolean uploadLocal(MultipartFile[] picture, String mp4TempDir) {
        boolean bool = false;
        try{
            for (int i = 0; i < picture.length; i++) {
                if (picture[i].getSize() > 0){
                    String originalFilename = picture[i].getOriginalFilename(); //获取图片原来名称
                    String filePathName = mp4TempDir + i + "."+ getExtend(originalFilename); //真实的图片保存相对路径
                    picture[i].transferTo(new File(filePathName));
                }
            }
            bool = true;
            System.out.println("图片上传到服务器完成");
        }catch(Exception e){
            e.printStackTrace();
        }
        return bool;
    }

    /**
     * 通过上一层目录和目录名得到最后的目录层次
     * @param previousDir 上一层目录
     * @param dirName 当前目录名
     * @return
     */
    public static String getSaveDir(String previousDir, String dirName) {
        if (StringUtils.isNotBlank(previousDir)){
            dirName = previousDir + "/" + dirName + "/";
        }else {
            dirName = dirName + "/";
        }
        return dirName;
    }

    /**
     * 如果目录不存在，就创建文件
     * @param dirPath
     * @return
     */
    public static String mkdirs(String dirPath) {
        try{
            File file = new File(dirPath);
            if(!file.exists()){
                file.mkdirs();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return dirPath;
    }


    /**
     * 在Linux系统中读取文件时将文件排序
     * @param filePath
     * @return
     */
    public static File[] fileSort(String filePath){
        File[] files = new File(filePath).listFiles();
        int filesLength = files.length;
        String nextFix = getExtend(files[0].getName());
        File[] fileNames = new File[filesLength];
        for (int i = 0; i < filesLength; i++) {
            for (int j = 0; j < filesLength; j++) {
                String absolutePath = files[j].getAbsolutePath();
                if (absolutePath.endsWith("/" + i + "." + nextFix) || absolutePath.endsWith("\\" + i + "." + nextFix)){
                    fileNames[i] = new File(absolutePath);
                    break;
                }
            }
        }
        return fileNames;
    }


    /**
     * 普通文件下载，文件在服务器里面
     * @param request
     * @param response
     */
    public static void download(HttpServletRequest request, HttpServletResponse response) {
        try{
            //设置文件下载时，文件流的格式
            String realPath = request.getServletContext().getRealPath("/");
            realPath = realPath + "index.jsp";
            System.out.println("下载地址="+realPath);
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(realPath));
            BufferedOutputStream bos =  new BufferedOutputStream(response.getOutputStream());
            //下面这个变量保存的是要下载的文件拼接之后的完整路径
            String downName = realPath.substring(realPath.lastIndexOf("/") + 1);
            System.out.println("下载文件名="+downName);
            response.setHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode(downName,"utf-8"));
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
            try {
                bis.close();
                bos.close();
            }catch (Exception e){
                e.printStackTrace();;
            }
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("下载出错");
        }
    }

    /**
     * 普通文件下载，文件路径固定
     * @param targetFile 下载的文件路径
     * @param response
     */
    public static void download(String targetFile, HttpServletResponse response) {
        try{
            System.out.println("下载文件路径="+targetFile);
            //设置文件下载时，文件流的格式
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(targetFile));
            BufferedOutputStream bos =  new BufferedOutputStream(response.getOutputStream());
            //下面这个变量保存的是要下载的文件拼接之后的完整路径
            String downName = targetFile.substring(targetFile.lastIndexOf("/") + 1);
            System.out.println("下载文件名="+downName);
            response.setHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode(downName,"utf-8"));
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
            try {
                bis.close();
                bos.close();
            }catch (Exception e){
                e.printStackTrace();;
            }
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("下载出错");
        }
    }

    /**
     * 下载网络文件
     * @param targetFile
     * @param response
     */
    public static void downloadUrl(String targetFile, HttpServletResponse response) {
        try{
            URL website = new URL(targetFile);
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream("D:/img/1.zip");//例如：test.txt
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("下载出错");
        }
    }

    /**
     * 删除文件
     * @param fileName
     * @return
     */

    public static boolean delete (String fileName){
        try{
            File sourceFile = new File(fileName);
            if(sourceFile.isDirectory()){
                for (File listFile : sourceFile.listFiles()) {
                    delete(listFile.getAbsolutePath());
                }
            }
            return sourceFile.delete();
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }


}
