package com.know.knowboot.utils.file;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Created by Administrator on 2019/3/20.
 */
@Slf4j
public class FileUtils {


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
     * 校验文件是否存在
     * @param file
     * @return
     */
    public static Boolean checkFileExists(File file){
        //判断是否存在test命名的文件或者文件夹
        if (file.exists()) {
           return true;
        } else {
           return false;
        }
    }

    /**
     * 创建文件
     * @param file
     * @return
     */
    public static Boolean createFile(File file){
        //判断是否存在test命名的文件或者文件夹
        if (file.exists()) {
           return false;
        } else {
            try {
                file.createNewFile();
                return true;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 删除文件
     * @param file
     * @return
     */
    public static Boolean deleteFile(File file){
        //判断是否存在test命名的文件或者文件夹
        if (file.exists()) {
            file.delete();
            return true;
        } else {
           return false;
        }
    }

    /**
     * 校验文件夹是否存在
     * @param file
     * @return
     */
    public static Boolean checkDirExists(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
               return true;
            } else {
               return false;
            }
        } else {
           return false;
        }
    }

    /**
     * 创建文件夹
     * 在已有的目录下新建一级目录
     * @param file
     * @return
     */
    public static Boolean createSingleDir(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                return false;
            } else {
                //文件不是文件夹，而是文件
                return false;
            }
        } else {
            file.mkdir();
            return true;
        }
    }

    /**
     * 创建文件夹
     * 在已有的目录下新建多级级目录
     * @param file
     * @return
     */
    public static Boolean createMultiDir(File file) {
        if (file.exists()) {
            //判断是否是文件夹   dirFile.isFile()判断是否是文件
            if (file.isDirectory()) {
                return false;
            } else {
                //文件不是文件夹，而是文件
                return false;
            }
        } else {
            file.mkdirs();
            return true;
        }
    }

    /**
     * 文件下载
     * @param response
     * @param request
     * @param dbpath
     * @param fileName
     * @throws Exception
     */
    public void download(HttpServletResponse response, HttpServletRequest request, String dbpath, String fileName) throws Exception{

        InputStream inputStream = null;
        OutputStream outputStream=null;
        try {

//            dbpath = this.getClass().getResource("/template/download/完成使用手册.docx").getPath();
//            fileName = "完成使用手册.docx";

            fileName= URLEncoder.encode(fileName,"utf-8");

            response.setContentType("application/x-msdownload;charset=utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);

            inputStream = new BufferedInputStream(new FileInputStream(dbpath));
            outputStream = response.getOutputStream();
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, len);
            }
            response.flushBuffer();
        } catch (Exception e) {
            log.error("--通过流的方式获取文件异常--" + e.getMessage(), e);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

    /**
     * 获取文件名称[不含后缀名]
     *
     * @param
     * @return String
     */
    public static String getFilePrefix(String fileName) {
        int splitIndex = fileName.lastIndexOf(".");
        return fileName.substring(0, splitIndex).replaceAll("\\s*", "");
    }

    /**
     * 获取文件名称[不含后缀名]
     * 不去掉文件目录的空格
     * @param
     * @return String
     */
    public static String getFilePrefix2(String fileName) {
        int splitIndex = fileName.lastIndexOf(".");
        return fileName.substring(0, splitIndex);
    }

    /**
     * 文件复制
     *方法摘要：这里一句话描述方法的用途
     *@param
     *@return void
     */
    public static void copyFile(String inputFile,String outputFile) throws FileNotFoundException{
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
    public static boolean isPicture(String filename) {
        // 文件名称为空的场合
        if (StringUtils.isEmpty(filename)) {
            // 返回不和合法
            return false;
        }
        // 获得文件后缀名
        //String tmpName = getExtend(filename);
        String tmpName = filename;
        // 声明图片后缀名数组
        String[][] imgeArray = {{"bmp", "0"}, {"dib", "1"},
                {"gif", "2"}, {"jfif", "3"}, {"jpe", "4"},
                {"jpeg", "5"}, {"jpg", "6"}, {"png", "7"},
                {"tif", "8"}, {"tiff", "9"}, {"ico", "10"}};
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
     * 判断文件是否为DWG<br>
     * <br>
     *
     * @param filename
     *            文件名<br>
     *            判断具体文件类型<br>
     * @return 检查后的结果<br>
     * @throws Exception
     */
    public static boolean isDwg(String filename) {
        // 文件名称为空的场合
        if (StringUtils.isEmpty(filename)) {
            // 返回不和合法
            return false;
        }
        // 获得文件后缀名
        String tmpName = getExtend(filename);
        // 声明图片后缀名数组
        return tmpName.equals("dwg");
    }

    /**
     * 删除指定的文件
     *
     * @param strFileName
     *            指定绝对路径的文件名
     * @return 如果删除成功true否则false
     */
    public boolean delete(String strFileName) {
        File fileDelete = new File(strFileName);

        if (!fileDelete.exists() || !fileDelete.isFile()) {
            log.info("错误: " + strFileName + "不存在!");
            return false;
        }

        log.info("--------成功删除文件---------"+strFileName);
        return fileDelete.delete();
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
    public String encodingFileName(String fileName) {
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
     * 根据现有路径获取SWF文件名称
     * @author taoYan
     * @since 2018年7月26日
     */
    public static String getSwfPath(String path){
        String leftSlash = "/";
        if(!File.separator.equals(leftSlash)){
            path = path.replace(File.separator,leftSlash);
        }
        String fileDir = path.substring(0,path.lastIndexOf(leftSlash)+1);//文件目录带/
        int pointPosition = path.lastIndexOf(".");
        String fileName = path.substring(path.lastIndexOf(leftSlash)+1,pointPosition);//文件名不带后缀
        String swfName = PinyinUtil.getPinYinHeadChar(fileName);// 取文件名首字母作为SWF文件名
        return fileDir+swfName+".swf";
    }

    /**
     * 上传txt文件，防止乱码
     * @author taoYan
     * @since 2018年7月26日
     */
    public static void uploadTxtFile(MultipartFile mf, String savePath) throws IOException{
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
                String contents = new String(mf.getBytes(), StandardCharsets.UTF_8);
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
            String contents = new String(mf.getBytes(), StandardCharsets.UTF_8);
            if(StringUtils.isNotBlank(contents)){
                OutputStream out = new FileOutputStream(savePath);
                out.write(contents.getBytes());
                out.close();
            }
        }
    }

    public static String toHexString(int index){
        String hexString = Integer.toHexString(index);
        // 1个byte变成16进制的，只需要2位就可以表示了，取后面两位，去掉前面的符号填充
        hexString = hexString.substring(hexString.length() -2);
        return hexString;
    }

    public static void copyFileToDirectoryByNewFileNm(File srcFile, File destDir, String newFileNm) throws IOException {
        copyFileToDirectoryByNewFileNm(srcFile, destDir, newFileNm, true);
    }

    public static void copyFileToDirectoryByNewFileNm(File srcFile, File destDir, String newFileNm, boolean preserveFileDate) throws IOException {
        if (destDir == null) {
            throw new NullPointerException("Destination must not be null");
        }
        if (destDir.exists() && destDir.isDirectory() == false) {
            throw new IllegalArgumentException("Destination '" + destDir + "' is not a directory");
        }
        copyFile(srcFile, new File(destDir, newFileNm), preserveFileDate);
    }

    public static void copyFileToDirectory(File srcFile, File destDir) throws IOException {
        copyFileToDirectory(srcFile, destDir, true);
    }

    public static void copyFileToDirectory(File srcFile, File destDir, boolean preserveFileDate) throws IOException {
        if (destDir == null) {
            throw new NullPointerException("Destination must not be null");
        }
        if (destDir.exists() && destDir.isDirectory() == false) {
            throw new IllegalArgumentException("Destination '" + destDir + "' is not a directory");
        }
        copyFile(srcFile, new File(destDir, srcFile.getName()), preserveFileDate);
    }

    public static void copyFile(File srcFile, File destFile,
                                boolean preserveFileDate) throws IOException {
        if (srcFile == null) {
            throw new NullPointerException("Source must not be null");
        }
        if (destFile == null) {
            throw new NullPointerException("Destination must not be null");
        }
        if (srcFile.exists() == false) {
            throw new FileNotFoundException("Source '" + srcFile + "' does not exist");
        }
        if (srcFile.isDirectory()) {
            throw new IOException("Source '" + srcFile + "' exists but is a directory");
        }
        if (srcFile.getCanonicalPath().equals(destFile.getCanonicalPath())) {
            throw new IOException("Source '" + srcFile + "' and destination '" + destFile + "' are the same");
        }
        if (destFile.getParentFile() != null && destFile.getParentFile().exists() == false) {
            if (destFile.getParentFile().mkdirs() == false) {
                throw new IOException("Destination '" + destFile + "' directory cannot be created");
            }
        }
        if (destFile.exists() && destFile.canWrite() == false) {
            throw new IOException("Destination '" + destFile + "' exists but is read-only");
        }
        doCopyFile(srcFile, destFile, preserveFileDate);
    }

    private static void doCopyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException {
        if (destFile.exists() && destFile.isDirectory()) {
            throw new IOException("Destination '" + destFile + "' exists but is a directory");
        }

        FileInputStream input = new FileInputStream(srcFile);
        try {
            FileOutputStream output = new FileOutputStream(destFile);
            try {
                IOUtils.copy(input, output);
            } finally {
                IOUtils.closeQuietly(output);
            }
        } finally {
            IOUtils.closeQuietly(input);
        }

        if (srcFile.length() != destFile.length()) {
            throw new IOException("Failed to copy full contents from '" +
                    srcFile + "' to '" + destFile + "'");
        }
        if (preserveFileDate) {
            destFile.setLastModified(srcFile.lastModified());
        }
    }


    public static void main(String[] args){

        File file = new File("d:\\test_file.txt");
        //校验文件是否存在
//        Boolean boolean1 = checkFileExists(file);
//        if (boolean1 == true){
//            System.out.println("存在");
//        }else {
//            System.out.println("不存在");
//        }

        //创建文件
//        Boolean boolean2 = createFile(file);
//        if (boolean2 == true){
//            System.out.println("创建成功");
//        }else {
//            System.out.println("创建失败");
//        }

        //创建文件
//        Boolean boolean3 = deleteFile(file);
//        if (boolean3 == true){
//            System.out.println("删除成功");
//        }else {
//            System.out.println("删除失败");
//        }

        File dir = new File("d:\\test_dir");
        //文件夹是否存在
//        Boolean boolean4 = checkDirExists(dir);
//        if (boolean4 == true){
//            System.out.println("存在");
//        }else {
//            System.out.println("不存在");
//        }
        //创建单一文件夹
//        Boolean boolean5 = createSingleDir(dir);
//        if (boolean5 == true){
//            System.out.println("创建成功");
//        }else {
//            System.out.println("创建失败");
//        }

        File dir2 = new File("d:\\test_dir\\a\\a\\a\\a");
        //创建多个文件夹
//        Boolean boolean6 = createMultiDir(dir2);
//        if (boolean6 == true){
//            System.out.println("创建成功");
//        }else {
//            System.out.println("创建失败");
//        }

    }

}
