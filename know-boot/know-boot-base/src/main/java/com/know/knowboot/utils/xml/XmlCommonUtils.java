package com.know.knowboot.utils.xml;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.XmlUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.XML;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.dom4j.*;
import org.json.JSONObject;

import java.util.Map;
import java.util.Set;

public class XmlCommonUtils {

    public static void main(String[] args) {

        // 节点测试
//        createXml();
//        createXmlWithNamespaceTest();
//        createXmlWithNamespace();

        // 解析xml
//        String path = "D:\\ipDefine(1)-cj-new.xml";
//        parseXmlToJson(path);
//        parseXmlToJSONObject(path);


        // 生成xml
        // 生成xml(INVALID_CHARACTER_ERR: 指定的 XML 字符无效或非法；原因：xml中包含非法字符，比如：-)
//        String string = "{'root':{'root':{'xmlns':'sss','arr':[{'name':'ca','id':'1'},{'name':'cs','id':'2'}],'id':'1','po':{'id':{'xml':'sasd','text':'1'},'code':'AD123'}}}}";
//        stringToJsonObject(string);

        String string1 = "{'root':{'root':{'-xmlns':'sss','arr':[{'name':'ca','id':'1'},{'name':'cs','id':'2'}],'id':'1','po':{'id':{'-xml':'sasd','#text':'1'},'code':'AD123'}}}}";
        Object object = com.alibaba.fastjson.JSONObject.parseObject(string1);
        String xmlInfo = jsonToXml(object);
        System.out.println("------------ xml - parseXmlToJson ------------- json -> " + xmlInfo);

    }


    // ******************************************* 节点测试 ****************************************************

    /**
     * 创建xml
     */
    public static void createXml(){

        System.out.println("--------------------------------------------------------");

        // 创建一个Document对象，表示一个空的xml文档
        Document document = DocumentHelper.createDocument();
        // 创建根节点<root />
        Element root = document.addElement("root");

        // 创建下一级节点<nextRoot />
        Element nextNode = root.addElement("nextRoot");
        // 设置节点属性<nextRoot id="1">
        nextNode.addAttribute("id","1");
        // 设置节点文章<nextRoot id="1">id=1</nextRoot>
        nextNode.setText("id=1");

        document.setRootElement(root);
        System.out.println(document.asXML());

    }

    /**
     * 创建xml 带命名空间
     */
    public static void createXmlWithNamespaceTest(){

        System.out.println("--------------------------------------------------------");

        // 创建一个Document对象，表示一个空的xml文档
        Document document = DocumentHelper.createDocument();

        //设置根节点和命名空间
        QName qName = new QName("root");
        //设置命令空间
        Element rootElement = document.addElement(qName);

        // <root xmlns="id"/>
        Namespace namespace=new Namespace("","id");
        QName qName1=new QName(rootElement.getName(),namespace);
        rootElement.setQName(qName1);
        rootElement.setText("aaa");

        // <root xmlns="id">aaa<xml:nextRoot xmlns:xml="id"/></root>
        // 创建下一级节点
        Element nextNode = rootElement.addElement("nextRoot");
        Namespace namespace1=new Namespace("xml","id");
        QName qName2=new QName(nextNode.getName(),namespace1);
        nextNode.setQName(qName2);

        // 创建跟节点
        document.setRootElement(rootElement);
        System.out.println(document.asXML());

    }

    /**
     * 创建xml 带命名空间及参数
     */
    public static void createXmlWithNamespace(){

        System.out.println("--------------------------------------------------------");

        // 创建一个Document对象，表示一个空的xml文档
        Document document = DocumentHelper.createDocument();

        //设置根节点和命名空间
        QName qName = new QName("root");

        //设置命令空间
        Element rootElement = document.addElement(qName);
        Namespace namespace=new Namespace("xmi","https://.......");
        rootElement.add(namespace);

        // 创建下一级节点 QName:节点名称
        QName qName2=new QName("nextRoot");
        Element nextNode = rootElement.addElement(qName2);
        nextNode.addAttribute("aaa","11");
        nextNode.setText("123");

        // 创建跟节点
        document.setRootElement(rootElement);
        System.out.println(document.asXML());

    }

    // ******************************************* 解析xml ****************************************************
    /**
     * 将xml文件解析成json(cn.hutool)
     *
     * @param path
     */
    public static JSON parseXmlToJson(String path) {
        String xmlString = FileUtil.readString(path, CharsetUtil.charset("UTF-8"));
        JSON jsonObject = XML.toJSONObject(xmlString);

        System.out.println("------------ xml - parseXmlToJson ------------- json -> " + jsonObject.toString());

        parseXmlJsonToOject(jsonObject);

        return jsonObject;

    }

    /**
     * 将xml的json文件解析成对象
     *
     * @param jsonObject
     */
    public static void parseXmlJsonToOject(JSON jsonObject) {
        Object object = jsonObject.toBean(Object.class);

        System.out.println("------------ xml - parseXmlJsonToOject ------------- obj -> " + object.toString());
    }

    /**
     * 将xml文件解析成json(org.json)
     *
     * @param path
     */
    public static JSONObject parseXmlToJSONObject(String path) {
        String xmlString = FileUtil.readString(path, CharsetUtil.charset("UTF-8"));
        org.json.JSONObject jsonObject = org.json.XML.toJSONObject(xmlString);

        System.out.println("------------ xml - parseXmlToJSONObject ------------- json -> " + jsonObject.toString());

        return jsonObject;

    }

    // ******************************************* 生成xml ****************************************************

    /**
     * string解析成jsonobject
     * @param param
     */
    public static void stringToJsonObject(String param) {

        com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSON.parseObject(param);
        jsonToXml(jsonObject);

    }

    /**
     * 将jsonobject转xml
     * @param json
     * @return
     */
    public static String jsonToXml(com.alibaba.fastjson.JSONObject json) {
        Map tmpMap = json.toJavaObject(Map.class);

        String xml = XmlUtil.mapToXmlStr(tmpMap);

        System.out.println("------------ xml - jsonToXml ------------- xml -> " + xml);

        return xml;
    }

    /**
     * json转xml
     *  属性参数和命令空间参数： 包含 -  举例： xml： <root xmlns="sss">   json： "-xmlns"：""
     *  基础参数含有属性值需要配置值字段： 包含 #text, 举例： "#text":"1"
     * @param json
     * @return
     */
    public static String jsonToXml(Object json) {
        Document document = DocumentHelper.createDocument();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonObject jsonObject = new JsonParser().parse(objectMapper.writeValueAsString(json)).getAsJsonObject();

            // 获取第一层节点
            Set<Map.Entry<String, JsonElement>> set = jsonObject.entrySet();
            //设置根节点和命名空间
            QName qName = new QName(set.stream().findFirst().get().getKey());
            //设置命令空间
            Element element = document.addElement(qName);
            document.setRootElement(element);

            // 递归获取其他层级节点
            toXml(set.stream().findFirst().get().getValue(), document.getRootElement(), "");

            return document.asXML();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    /**
     * 解析xml
     * @param jsonElement
     * @param parentElement
     * @param name
     * @return
     */
    private static Element toXml(JsonElement jsonElement, Element parentElement, String name) {
        // json数据，需继续解析
        if (jsonElement instanceof JsonArray) {
            JsonArray sonJsonArray = (JsonArray)jsonElement;
            for (int i = 0; i < sonJsonArray.size(); i++) {
                JsonElement arrayElement = sonJsonArray.get(i);
                toXml(arrayElement, parentElement, name);
            }

            // 一个json对象字符串，需要继续解析
        }else if (jsonElement instanceof JsonObject) {
            JsonObject sonJsonObject = (JsonObject) jsonElement;
            Element currentElement = null;
            if (StringUtils.isNotBlank(name)) {
                currentElement = parentElement.addElement(name);
            }
            Set<Map.Entry<String, JsonElement>> set = sonJsonObject.entrySet();
            for (Map.Entry<String, JsonElement> s : set) {
                toXml(s.getValue(), currentElement != null ? currentElement : parentElement, s.getKey());
            }
        }else if (jsonElement.isJsonNull()){
            parentElement.addElement(name);
        }else {
            //说明是一个键值对的key,可以作为节点插入了
            addAttribute(parentElement, name, jsonElement.getAsString());
        }
        return parentElement;
    }

    /**
     * 添加属性
     * @param element
     * @param name
     * @param value
     */
    private static void addAttribute(Element element, String name, String value) {
        //增加子节点，并为子节点赋值
        if (String.valueOf(name.charAt(0)).equals(com.baomidou.mybatisplus.core.toolkit.StringPool.DASH)){

            // 参数前为 “-”符号的为节点属性 去除“-”符号并添加属性
            String key=name.substring(1);
            Namespace namespace=new Namespace("",value);
            QName qName=new QName(element.getName(),namespace);
            element.setQName(qName);
            element.addAttribute(key,value);

        }else if (StringPool.HASH.equals(String.valueOf(name.charAt(0)))){
            element.addText(value);
        }
        else {
            Element el = element.addElement(name);
            el.addText(value);
        }
    }


}
