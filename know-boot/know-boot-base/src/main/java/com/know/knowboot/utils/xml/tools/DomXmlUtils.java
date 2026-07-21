package com.know.knowboot.utils.xml.tools;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class DomXmlUtils {

    public static void main(String[] args) {
//        //1.创建DocumentBuilderFactory对象
//        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        //2.创建DocumentBuilder对象
//        try {
//            DocumentBuilder builder = factory.newDocumentBuilder();
////            Document d = builder.parse("src/main/resources/demo.xml");
//            Document d = builder.parse("D://demo.xml");
//            NodeList sList = d.getElementsByTagName("student");
////            element(sList);
//            node(sList);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        createXml();

    }

    // ****************************************************** DOM方式 ********************************************************
    //用Element方式
    public static void element(NodeList list){
        for (int i = 0; i <list.getLength() ; i++) {
            Element element = (Element) list.item(i);
            NodeList childNodes = element.getChildNodes();
            for (int j = 0; j <childNodes.getLength() ; j++) {
                if (childNodes.item(j).getNodeType()== Node.ELEMENT_NODE) {
                    //获取节点
                    System.out.print(childNodes.item(j).getNodeName() + ":");
                    //获取节点值
                    System.out.println(childNodes.item(j).getFirstChild().getNodeValue());
                }
            }
        }
    }

    public static void node(NodeList list){
        for (int i = 0; i <list.getLength() ; i++) {
            Node node = list.item(i);
            NodeList childNodes = node.getChildNodes();
            for (int j = 0; j <childNodes.getLength() ; j++) {
                if (childNodes.item(j).getNodeType()==Node.ELEMENT_NODE) {
                    System.out.print(childNodes.item(j).getNodeName() + ":");
                    System.out.println(childNodes.item(j).getFirstChild().getNodeValue());
                }
            }
        }
    }

    /**
     * 生成xml方法
     */
    public static void createXml(){
        try {
            // 创建解析器工厂
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = factory.newDocumentBuilder();
            Document document = db.newDocument();
            // 不显示standalone="no"
            document.setXmlStandalone(true);
            Element bookstore = document.createElement("bookstore");
            // 向bookstore根节点中添加子节点book
            Element book = document.createElement("book");

            Element name = document.createElement("name");
            // 不显示内容 name.setNodeValue("不好使");
            name.setTextContent("雷神");
            book.appendChild(name);
            // 为book节点添加属性
            book.setAttribute("id", "1");
            // 将book节点添加到bookstore根节点中
            bookstore.appendChild(book);
            // 将bookstore节点（已包含book）添加到dom树中
            document.appendChild(bookstore);

            // 创建TransformerFactory对象
            TransformerFactory tff = TransformerFactory.newInstance();
            // 创建 Transformer对象
            Transformer tf = tff.newTransformer();

            // 输出内容是否使用换行
            tf.setOutputProperty(OutputKeys.INDENT, "yes");
            // 创建xml文件并写入内容
            tf.transform(new DOMSource(document), new StreamResult(new File("book1.xml")));
            System.out.println("生成book1.xml成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("生成book1.xml失败");
        }
    }



}