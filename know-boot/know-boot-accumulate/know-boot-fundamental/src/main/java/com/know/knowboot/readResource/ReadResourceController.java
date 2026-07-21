package com.know.knowboot.readResource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.URLDecoder;

@Slf4j
@RestController
@RequestMapping("/readResource")
public class ReadResourceController {

    @GetMapping("/read")
    public void read() {

        function1("template/test.ftl");
        function2("template/test.ftl");
        function3("template/test.ftl");
        function4("template/test.ftl");
        function5("template/test.ftl");
        function6("template/test.ftl");
        function7("template/test.ftl");
        function8("template/test.ftl");

    }


    public void function1(String fileName) {
        try {
            String path = this.getClass().getClassLoader().getResource("").getPath();//注意getResource("")里面是空字符串
            System.out.println(path);
            String filePath = path + fileName;
            System.out.println(filePath);
            getFileContent(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 直接通过文件名getPath来获取路径
     *
     * @param fileName
     * @throws IOException
     */
    public void function2(String fileName) {
        try {
            String path = this.getClass().getClassLoader().getResource(fileName).getPath();//注意getResource("")里面是空字符串
            System.out.println(path);
            String filePath = URLDecoder.decode(path, "UTF-8");//如果路径中带有中文会被URLEncoder,因此这里需要解码
            System.out.println(filePath);
            getFileContent(filePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 直接通过文件名+getFile()来获取
     *
     * @param fileName
     * @throws IOException
     */
    public void function3(String fileName) {
        try {
            String path = this.getClass().getClassLoader().getResource(fileName).getFile();//注意getResource("")里面是空字符串
            System.out.println(path);
            String filePath = URLDecoder.decode(path, "UTF-8");//如果路径中带有中文会被URLEncoder,因此这里需要解码
            System.out.println(filePath);
            getFileContent(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 直接使用getResourceAsStream方法获取流
     * springboot项目中需要使用此种方法，因为jar包中没有一个实际的路径存放文件
     *
     * @param fileName
     * @throws IOException
     */
    public void function4(String fileName) {
        try {
            InputStream in = this.getClass().getClassLoader().getResourceAsStream(fileName);
            getFileContent(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 直接使用getResourceAsStream方法获取流
     * 如果不使用getClassLoader，可以使用getResourceAsStream("/配置测试.txt")直接从resources根路径下获取
     *
     * @param fileName
     * @throws IOException
     */
    public void function5(String fileName) {
        try {
            InputStream in = this.getClass().getResourceAsStream("/" + fileName);
            getFileContent(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过ClassPathResource类获取，建议SpringBoot中使用
     * springboot项目中需要使用此种方法，因为jar包中没有一个实际的路径存放文件
     *
     * @param fileName
     * @throws IOException
     */
    public void function6(String fileName) {
        try {
            ClassPathResource classPathResource = new ClassPathResource(fileName);
            InputStream inputStream = classPathResource.getInputStream();
            getFileContent(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过绝对路径获取项目中文件的位置（不能用于服务器）
     *
     * @param fileName
     * @throws IOException
     */
    public void function7(String fileName) {
        try {
            String rootPath = System.getProperty("user.dir");//E:\WorkSpace\Git\spring-framework-learning-example
            String filePath = rootPath + "\\src\\main\\resources\\" + fileName;
            getFileContent(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过绝对路径获取项目中文件的位置（不能用于服务器）
     *
     * @param fileName
     * @throws IOException
     */
    public void function8(String fileName) {
        try {
            //参数为空
            File directory = new File("");
            //规范路径：getCanonicalPath() 方法返回绝对路径，会把 ..\ 、.\ 这样的符号解析掉
            String rootCanonicalPath = directory.getCanonicalPath();
            //绝对路径：getAbsolutePath() 方法返回文件的绝对路径，如果构造的时候是全路径就直接返回全路径，如果构造时是相对路径，就返回当前目录的路径 + 构造 File 对象时的路径
            String rootAbsolutePath = directory.getAbsolutePath();
            System.out.println(rootCanonicalPath);
            System.out.println(rootAbsolutePath);
            String filePath = rootCanonicalPath + "\\src\\main\\resources\\" + fileName;
            getFileContent(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 根据文件路径读取文件内容
     *
     * @param fileInPath
     * @throws IOException
     */
    public static void getFileContent(Object fileInPath) throws IOException {
        BufferedReader br = null;
        if (fileInPath == null) {
            return;
        }
        if (fileInPath instanceof String) {
            br = new BufferedReader(new FileReader(new File((String) fileInPath)));
        } else if (fileInPath instanceof InputStream) {
            br = new BufferedReader(new InputStreamReader((InputStream) fileInPath));
        }
        String line;
        System.out.println("line::::::" + (line = br.readLine()) != null);
//        while ((line = br.readLine()) != null) {
//            System.out.println(line);
//        }
        br.close();
    }

}
