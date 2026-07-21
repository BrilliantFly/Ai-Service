package com.know.knowboot.utils.xml.tools;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.*;
import java.util.List;

public class JdomXmlUtils {

    public static void main(String[] args) throws IOException, JDOMException {

        //1.创建SAXBuilder对象
        SAXBuilder saxBuilder = new SAXBuilder();
        //2.创建输入流
        InputStream is = new FileInputStream(new File("D://ipDefine.xml"));
        //3.将输入流加载到build中
        Document document = saxBuilder.build(is);
        //4.获取根节点
        Element rootElement = document.getRootElement();
        //5.获取子节点
        List<Element> children = rootElement.getChildren();
        for (Element child : children) {
            System.out.println("通过rollno获取属性值:"+child.getAttribute("rollno"));
            List<Attribute> attributes = child.getAttributes();
            //打印属性
            for (Attribute attr : attributes) {
                System.out.println(attr.getName()+":"+attr.getValue());
            }
            List<Element> childrenList = child.getChildren();
            System.out.println("======获取子节点-start======");
            for (Element o : childrenList) {
                System.out.println("节点名:"+o.getName()+"---"+"节点值:"+o.getValue());
            }
            System.out.println("======获取子节点-end======");
        }

    }

    /**
     * 生成xml方法
     */
    public static void createXml(){
        try {
            // 1、生成一个根节点
            Element rss = new Element("rss");
            // 2、为节点添加属性
            rss.setAttribute("version", "2.0");
            // 3、生成一个document对象
            Document document = new Document(rss);

            Element channel = new Element("channel");
            rss.addContent(channel);
            Element title = new Element("title");
            title.setText("国内最新新闻");
            channel.addContent(title);

            Format format = Format.getCompactFormat();
            // 设置换行Tab或空格
            format.setIndent("	");
            format.setEncoding("UTF-8");

            // 4、创建XMLOutputter的对象
            XMLOutputter outputer = new XMLOutputter(format);
            // 5、利用outputer将document转换成xml文档
            File file = new File("rssNew.xml");
            outputer.output(document, new FileOutputStream(file));

            System.out.println("生成rssNew.xml成功");
        } catch (Exception e) {
            System.out.println("生成rssNew.xml失败");
        }
    }



}