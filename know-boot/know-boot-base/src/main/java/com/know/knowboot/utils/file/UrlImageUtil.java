package com.know.knowboot.utils.file;
import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2019/5/23.
 * 微信头像下载转本地头像
 */
public class UrlImageUtil {

    public static void avatarUrl(String imageUrl,String imageName) throws IOException {
        URL url = new URL(imageUrl);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        // 得到URL的输入流
        InputStream input = con.getInputStream();
        // 设置数据缓冲
        byte[] bs = new byte[1024 * 2];
        // 读取到的数据长度
        int len = 0;
        // 输出的文件流保存图片至本地
        OutputStream os = new FileOutputStream(imageName);
        while ((len = input.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        os.close();
        input.close();
    }

    public static void main(String[] args) throws Exception {

        String ctxPath = "D:/software/nginx-1.12.1/html/jjb/identity/";
        Date nowDate = new Date();
        String nowday= new SimpleDateFormat("yyyyMMdd").format(nowDate);
        String filePath = ctxPath + nowday;
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();// 创建文件根目录
        }

        /**
         * 生成文件名
         */
        String myfilename = "";
        String noextfilename = "aaa";// 自定义文件名称不带扩展
        String extend = ".jpg";
        myfilename = noextfilename + "." + extend; // 自定义文件名称

        String savePath = file.getPath() + File.separator + myfilename;

        String url = "https://wx.qlogo.cn/mmopen/vi_32/dCf27CJ0M3dxyyNbZscestNrrRej4tYUk7MyWib0FWp41o0FiboubqictTibDp853vKtkULvrXHXG3eKxoBxFSJD1Q/132";
        avatarUrl(url,savePath);

//        AttachmentEntity attachmentEntity = new AttachmentEntity();
//        attachmentEntity.setAttachmentName(myfilename);
//        attachmentEntity.setAbsolutePath(savePath);
//        String webPath = "jjb/identity/" + nowday + "/" + myfilename;
//        attachmentEntity.setWebPath(webPath);
//        attachmentEntity.setDeleteFlag(TypeConstants.DEL_FLAG_NO);
//        String pathId = (String) this.systemService.save(attachmentEntity);

    }


}
