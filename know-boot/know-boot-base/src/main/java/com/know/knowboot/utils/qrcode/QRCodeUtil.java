package com.know.knowboot.utils.qrcode;

import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by Administrator on 2019/12/13.
 */
public class QRCodeUtil {


    /*********************************************************二维码生成********************************************************************/
    /**
     * 二维码生成
     * @param content 内容
     * @param imgPath 路径
     * @param needCompress
     * @return
     * @throws Exception
     */
    private static BufferedImage createImage(String content, String imgPath, boolean needCompress) throws Exception {
        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // 内容所使用字符集编码
        hints.put(EncodeHintType.CHARACTER_SET, QRCondeConstant.QR_CHARSET);
        //设置白边
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QRCondeConstant.QRCODE_SIZE, QRCondeConstant.QRCODE_SIZE,
                hints);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        if (imgPath == null || "".equals(imgPath)) {
            return image;
        }
        // 插入图片
        QRCodeUtil.insertImage(image, imgPath, needCompress);
        return image;
    }

    private static void insertImage(BufferedImage source, String imgPath, boolean needCompress) throws Exception {
        File file = new File(imgPath);
        if (!file.exists()) {
            System.err.println("" + imgPath + "   该文件不存在！");
            return;
        }
        Image src = ImageIO.read(new File(imgPath));
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        if (needCompress) { // 压缩LOGO
            if (width > QRCondeConstant.LOGO_WIDTH) {
                width = QRCondeConstant.LOGO_WIDTH;
            }
            if (height > QRCondeConstant.LOGO_HEIGHT) {
                height = QRCondeConstant.LOGO_HEIGHT;
            }
            Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
            g.dispose();
            src = image;
        }
        // 插入LOGO
        Graphics2D graph = source.createGraphics();
        int x = (QRCondeConstant.QRCODE_SIZE - width) / 2;
        int y = (QRCondeConstant.QRCODE_SIZE - height) / 2;
        graph.drawImage(src, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }

    /**
     * 生成二维码
     * @param content 二维码内容
     * @param imgPath 二维码LOGO路径
     * @param destPath 生成的二维码的路径及名称
     * @param needCompress
     * @throws Exception
     */
    public static void encode(String content, String imgPath, String destPath, boolean needCompress) throws Exception {
        BufferedImage image = QRCodeUtil.createImage(content, imgPath, needCompress);
        mkdirs(destPath);
        // String file = new Random().nextInt(99999999)+".jpg";
        // ImageIO.write(image, FORMAT_NAME, new File(destPath+"/"+file));
        ImageIO.write(image, QRCondeConstant.QR_FORMAT_NAME, new File(destPath));
    }

    public static BufferedImage encode(String content, String imgPath, boolean needCompress) throws Exception {
        BufferedImage image = QRCodeUtil.createImage(content, imgPath, needCompress);
        return image;
    }

    public static void mkdirs(String destPath) {
        File file = new File(destPath);
        // 当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir．(mkdir如果父目录不存在则会抛出异常)
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
    }

    public static void encode(String content, String imgPath, String destPath) throws Exception {
        QRCodeUtil.encode(content, imgPath, destPath, false);
    }
    // 被注释的方法
	/*
	 * public static void encode(String content, String destPath, boolean
	 * needCompress) throws Exception { QRCodeUtil.encode(content, null, destPath,
	 * needCompress); }
	 */

    public static void encode(String content, String destPath) throws Exception {
        QRCodeUtil.encode(content, null, destPath, false);
    }

    public static void encode(String content, String imgPath, OutputStream output, boolean needCompress)
            throws Exception {
        BufferedImage image = QRCodeUtil.createImage(content, imgPath, needCompress);
        ImageIO.write(image, QRCondeConstant.QR_FORMAT_NAME, output);
    }

    public static void encode(String content, OutputStream output) throws Exception {
        QRCodeUtil.encode(content, null, output, false);
    }

    public static String decode(File file) throws Exception {
        BufferedImage image;
        image = ImageIO.read(file);
        if (image == null) {
            return null;
        }
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result;
        Hashtable hints = new Hashtable();
        hints.put(DecodeHintType.CHARACTER_SET, QRCondeConstant.QR_CHARSET);
        result = new MultiFormatReader().decode(bitmap, hints);
        String resultStr = result.getText();
        return resultStr;
    }

    public static String decode(String path) throws Exception {
        return QRCodeUtil.decode(new File(path));
    }

    /*********************************************************图片合成********************************************************************/
    /**
     * 二维码图片合成
     * @param backgroundPath 背景路径
     * @param backGroundX 背景宽度
     * @param backGroundY 背景高度
     * @param qrCodePath 二维码路径
     * @param qrcodeX 二维码宽度
     * @param qrcodeY 二维码高度
     * @param qrcodeOsX 二维码X偏移量
     * @param qrcodeOsY 二维码Y偏移量
     * @param word 文字
     * @param wordOsX 文字X偏移量
     * @param wordOsY 文字Y偏移量
     * @param outPutPath 输出路径
     * @return
     */
    public static String overlapImage(String backgroundPath,int backGroundX,int backGroundY,String qrCodePath,int qrcodeX,int qrcodeY,int qrcodeOsX,int qrcodeOsY,String word,int wordOsX,int wordOsY,String outPutPath){
        try {
            //设置图片大小
//            BufferedImage background = resizeImage(618,1000, ImageIO.read(new File("这里是背景图片的路径！")));
            BufferedImage background = resizeImage(backGroundX,backGroundY, ImageIO.read(new File(backgroundPath)));
//            BufferedImage qrCode = resizeImage(150,150,ImageIO.read(new File("这里是插入二维码图片的路径！")));
            BufferedImage qrCode = resizeImage(qrcodeX,qrcodeY,ImageIO.read(new File(qrCodePath)));
            //在背景图片中添加入需要写入的信息，例如：扫描下方二维码，欢迎大家添加我的淘宝返利机器人，居家必备，省钱购物专属小秘书！
            //String message = "扫描下方二维码，欢迎大家添加我的淘宝返利机器人，居家必备，省钱购物专属小秘书！";
            Graphics2D g = background.createGraphics();
            //字体样式
            g.setColor(Color.black);
            g.setFont(new Font("微软雅黑",Font.BOLD,30));
            //添加文字
            g.drawString(word,wordOsX ,wordOsY);
            //在背景图片上添加二维码图片
            g.drawImage(qrCode, qrcodeOsX, qrcodeOsY, qrCode.getWidth(), qrCode.getHeight(), null);
            g.dispose();
//            ImageIO.write(background, "jpg", new File("这里是一个输出图片的路径"));
            ImageIO.write(background, "jpg", new File(outPutPath));
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static BufferedImage resizeImage(int x, int y, BufferedImage bfi){
        BufferedImage bufferedImage = new BufferedImage(x, y, BufferedImage.TYPE_INT_RGB);
        bufferedImage.getGraphics().drawImage(
                bfi.getScaledInstance(x, y, Image.SCALE_SMOOTH), 0, 0, null);
        return bufferedImage;
    }

    public static void main(String[] args) {

        File dirCreateQrcode = new File(QRCondeConstant.QR_CREATE_DIR);
        File dirCombine = new File(QRCondeConstant.QR_COMBINE_DIR);
        //校验文件夹是否存在
        if (dirCreateQrcode.isDirectory()) {
            System.out.println("文件夹已存在");
        } else {
            //创建文件夹
            dirCreateQrcode.mkdir();
        }
        if (dirCombine.isDirectory()) {
            System.out.println("文件夹已存在");
        } else {
            //创建文件夹
            dirCombine.mkdir();
        }

        java.util.List<String> stringList = new ArrayList<>();
        stringList.add("AB0001");
        stringList.add("AB0002");
        stringList.add("AB0003");
        stringList.add("AB0004");
        if (stringList != null && stringList.size() > 0) {
            for (int i = 0; i < stringList.size(); i++) {
                // 存放在二维码中的内容
                String text = stringList.get(i);
                // 生成的二维码的路径及名称
                String destPath = QRCondeConstant.QR_CREATE_DIR + "/" + text + ".jpg";
                try {
                    //生成二维码
                    QRCodeUtil.encode(text, "", destPath, true);
                    // 解析二维码
                    String str = QRCodeUtil.decode(destPath);
                    // 打印出解析出的内容
                    System.out.println(str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (stringList != null && stringList.size() > 0) {
            for (int i = 0; i < stringList.size(); i++) {
                // 存放在二维码中的内容
                String text = stringList.get(i);
                // 生成的二维码的路径及名称
                String destPath = QRCondeConstant.QR_CREATE_DIR + "/" + text + ".jpg";
                File file = new File(destPath);
                // 判断文件是否存在
                if (file.exists()) {
//                    URL url = this.getClass().getClassLoader().getResource("backGround.jpg");
                    System.out.println("合成二维码");
                    //测试图片在docs
                    String path = "D:/test/background.jpg";
                    //输入路径是文件
                    String outPath = QRCondeConstant.QR_COMBINE_DIR + "/" + text + ".jpg";
                    QRCodeUtil.overlapImage(path, QRCondeConstant.BACKGROUND_X,QRCondeConstant.BACKGROUND_Y,
                            destPath,QRCondeConstant.QRCODE_X,QRCondeConstant.QRCODE_Y,QRCondeConstant.QRCODE_OS_X,QRCondeConstant.QRCODE_OS_Y,
                            text,QRCondeConstant.WORD_OS_X,QRCondeConstant.WORD_OS_Y,outPath);
                } else {
                    System.out.println("文件不存在");
//                    try {
//                        file.createNewFile();
//                        System.out.println("创建文件");
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }
            }
        }

    }

}
