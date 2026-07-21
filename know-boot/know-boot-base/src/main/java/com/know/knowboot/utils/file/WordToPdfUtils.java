package com.know.knowboot.utils.file;

import com.aspose.words.*;
import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.aspectj.weaver.ast.Test;
import org.docx4j.Docx4J;
import org.docx4j.convert.out.FOSettings;
import org.docx4j.fonts.IdentityPlusMapper;
import org.docx4j.fonts.Mapper;
import org.docx4j.fonts.PhysicalFont;
import org.docx4j.fonts.PhysicalFonts;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import java.io.*;

@Slf4j
public class WordToPdfUtils {


    private static String fontsPath;

    public static void main(String[] args) throws Exception {

        String sourcePath = "E:\\Test\\123.docx";
        String targetPath = "E:\\Test\\123.pdf";

        // 第一种
//        wordToPdf(sourcePath,targetPath);

        // 第二种
//        documents4jWordToPdf(sourcePath,targetPath);

        // 第三种
        doc3pdf(sourcePath,targetPath);

//        asposeWordToPdf(sourcePath,targetPath);

//        modifyWordsJar();

    }

    // poi-ooxml 只能转格式docx
    public static void wordToPdf(String sourcePath, String targetPath) throws IOException {

        FileInputStream fileInputStream = new FileInputStream(sourcePath);
        XWPFDocument xwpfDocument = new XWPFDocument(fileInputStream);
        PdfOptions pdfOptions = PdfOptions.create();
        FileOutputStream fileOutputStream = new FileOutputStream(targetPath);
        PdfConverter.getInstance().convert(xwpfDocument,fileOutputStream,pdfOptions);
        fileInputStream.close();
        fileOutputStream.close();

    }

    /**
     * 通过documents4j 实现word转pdf
     *
     * 可以转doc、docx
     *
     * @param sourcePath 源文件地址 如 /root/example.doc
     * @param targetPath 目标文件地址 如 /root/example.pdf
     */
    public static void documents4jWordToPdf(String sourcePath, String targetPath) {
        File inputWord = new File(sourcePath);
        File outputFile = new File(targetPath);
        try  {
            InputStream docxInputStream = new FileInputStream(inputWord);
            OutputStream outputStream = new FileOutputStream(outputFile);
            IConverter converter = LocalConverter.builder().build();
            converter.convert(docxInputStream)
                    .as(DocumentType.DOCX)
                    .to(outputStream)
                    .as(DocumentType.PDF).execute();
            outputStream.close();
        } catch (Exception e) {
            log.error("[documents4J] word转pdf失败:{}", e.toString());
        }
    }

    /**
     * docx
     * @param sourcePath
     * @param targetPath
     */
    public static void doc3pdf(String sourcePath, String targetPath) {
        try {
            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(new File(sourcePath));
            Mapper fontMapper = new IdentityPlusMapper();
            PhysicalFont font  = PhysicalFonts.getPhysicalFonts().get("Comic Sans MS");
            fontMapper.put("Calibri", font);
            wordMLPackage.setFontMapper(fontMapper);

            OutputStream os = new java.io.FileOutputStream(targetPath);
            FOSettings foSettings = Docx4J.createFOSettings();
            foSettings.setWmlPackage(wordMLPackage);

            Docx4J.toFO(foSettings, os, Docx4J.FLAG_EXPORT_PREFER_XSL);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("word转pdf失败");
        }
    }

    /**
     * 通过aspose 将word转pdf
     *
     * @param sourcePath 源文件地址 如 /root/example.doc
     * @param targetPath 目标文件地址 如 /root/example.pdf
     */
    public static void asposeWordToPdf(String sourcePath, String targetPath) {

        // 验证License 若不验证则转化出的pdf文档会有水印产生
        getLicense();

        //根据系统设置字体路径
        setFontsFolder();

        LoadOptions opts = new LoadOptions();
//        opts.setMswVersion(MsWordVersion.WORD_2016);
        opts.getLanguagePreferences().setDefaultEditingLanguage(EditingLanguage.CHINESE_PRC);
        Document doc = null;
        try {
            doc = new Document(sourcePath, opts);
            ParagraphFormat pf = doc.getStyles().getDefaultParagraphFormat();
            pf.clearFormatting();

            PdfSaveOptions options = new PdfSaveOptions();
            // 文字和图像压缩
            options.setExportDocumentStructure(true);
            options.setTextCompression(PdfTextCompression.FLATE);
            options.setImageCompression(PdfImageCompression.AUTO);
            // 接收修订
            doc.acceptAllRevisions();
            // 去掉批注
            NodeCollection nc = doc.getChildNodes(NodeType.COMMENT,true);
            if (nc != null && nc.getCount() > 0) {
                for(int i=0;i<nc.getCount();i++){
                    log.info("清除批注：{}",nc.get(i).getText());
                    Node comment =nc.get(i);
                    comment.getParentNode().removeChild(comment);
                }
            }
            // 将Word另存为PDF
            doc.save(targetPath, options);
        } catch (Exception e) {
            log.error("[aspose] word转pdf失败:{}", e.toString());
        }

    }


    /**
     *          //源，目标
     *         InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
     *         FileItem item = new DiskFileItemFactory().createItem("file" , MediaType.MULTIPART_FORM_DATA_VALUE, true, fileName);
     *         //转PDF
     *         WordToPdfUtils.asposeWordToPdf(inputStream, item.getOutputStream());
     * @param inputStream
     * @param outputStream
     */
    public static void asposeWordToPdf(InputStream inputStream, OutputStream outputStream) {

        // 验证License 若不验证则转化出的pdf文档会有水印产生
        getLicense();

        //根据系统设置字体路径
        setFontsFolder();

        LoadOptions opts = new LoadOptions();
//        opts.setMswVersion(MsWordVersion.WORD_2016);
        opts.getLanguagePreferences().setDefaultEditingLanguage(EditingLanguage.CHINESE_PRC);
        Document doc = null;
        try {
            doc = new Document(inputStream, opts);
            ParagraphFormat pf = doc.getStyles().getDefaultParagraphFormat();
            pf.clearFormatting();

            PdfSaveOptions options = new PdfSaveOptions();
            // 文字和图像压缩
            options.setExportDocumentStructure(true);
            options.setTextCompression(PdfTextCompression.FLATE);
            options.setImageCompression(PdfImageCompression.AUTO);
            // 接收修订
            doc.acceptAllRevisions();
            // 去掉批注
            NodeCollection nc = doc.getChildNodes(NodeType.COMMENT,true);
            if (nc != null && nc.getCount() > 0) {
                for(int i=0;i<nc.getCount();i++){
                    log.info("清除批注：{}",nc.get(i).getText());
                    Node comment =nc.get(i);
                    comment.getParentNode().removeChild(comment);
                }
            }
            // 将Word另存为PDF
            doc.save(outputStream, options);
        } catch (Exception e) {
            log.error("[aspose] word转pdf失败:{}", e.toString());
        }

    }


    /**
     * 根据系统设置字体路径
     */
    public static void setFontsFolder() {
        //判断系统类型
        String os = System.getProperty("os.name");
        if (os != null && os.toLowerCase().startsWith("linux")) {
            //验证字体是否存在
//            String path = "/usr/share/fonts";
            String path = fontsPath;
            File[] files = new File(path).listFiles();
            if (files == null || files.length < 1) {
                throw new RuntimeException("查询不到字体文件");
            }

            FontSettings.getDefaultInstance().setFontsFolder(path + File.separator, true);
        }
    }

    public static void getLicense() {
        try {
            InputStream is = Test.class.getClassLoader().getResourceAsStream("\\apsose\\license.xml"); // license.xml应放在..\WebRoot\WEB-INF\classes路径下
            License aposeLic = new License();
            aposeLic.setLicense(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改words jar包里面的校验
     */
    public static void modifyWordsJar() {
        try {
            //这一步是完整的jar包路径,选择自己解压的jar目录
            ClassPool.getDefault().insertClassPath("D:\\wordtopdf\\aspose-words-21.11-jdk17.jar");
            //获取指定的class文件对象
            CtClass zzZJJClass = ClassPool.getDefault().getCtClass("com.aspose.words.zzXDb");
            //从class对象中解析获取指定的方法
            CtMethod[] methodA = zzZJJClass.getDeclaredMethods("zzY0J");
            //遍历重载的方法
            for (CtMethod ctMethod : methodA) {
                CtClass[] ps = ctMethod.getParameterTypes();
                if (ctMethod.getName().equals("zzY0J")) {
                    System.out.println("ps[0].getName==" + ps[0].getName());
                    //替换指定方法的方法体
                    ctMethod.setBody("{this.zzZ3l = new java.util.Date(Long.MAX_VALUE);this.zzWSL = com.aspose.words.zzYeQ.zzXgr;zzWiV = this;}");
                }
            }
            //这一步就是将破译完的代码放在桌面上
            zzZJJClass.writeFile("D:\\wordtopdf");

            //获取指定的class文件对象
            CtClass zzZJJClassB = ClassPool.getDefault().getCtClass("com.aspose.words.zzYKk");
            //从class对象中解析获取指定的方法
            CtMethod methodB = zzZJJClassB.getDeclaredMethod("zzWy3");
            //替换指定方法的方法体
            methodB.setBody("{return 256;}");
            //这一步就是将破译完的代码放在桌面上
            zzZJJClassB.writeFile("D:\\wordtopdf");
        } catch (Exception e) {
            System.out.println("错误==" + e);
        }
    }



}
