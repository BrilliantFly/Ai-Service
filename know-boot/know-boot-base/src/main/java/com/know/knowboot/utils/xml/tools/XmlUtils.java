package com.know.knowboot.utils.xml.tools;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

@Slf4j
public class XmlUtils {


    public static void main(String[] args) throws DocumentException, IOException {
        String textFromFile = FileUtils.readFileToString(new File("D:\\ipDefine.xml"), "UTF-8");
//        Map<String, Object> map = xml2map(textFromFile, false);
        Map<String, Object> map = xml2mapWithAttr(textFromFile, true);

        Document document = map2xml(map);
        String s = document.asXML();
        System.out.println(s);
    }

    /**
     * xml转map无属性
     *
     * @param xmlStr
     * @param isRoot
     * @return
     */
    public static Map xml2map(String xmlStr, boolean isRoot) {
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(xmlStr);
        } catch (DocumentException e) {
            e.printStackTrace();
//            log.error("xml转map无属性 - 异常", e);
        }
        if (Objects.isNull(doc)) {
//            log.error("xml转map无属性 - doc为空");
            return null;
        }
        Element root = doc.getRootElement();
        Map<String, Object> map = xml2map(root);
        if (root.elements().size() == 0 && root.attributes().size() == 0) {
            return map;
        }
        if (isRoot) {
            //在返回的map里加根节点键（如果需要）
            Map<String, Object> rootMap = new HashMap<>();
            rootMap.put(root.getName(), map);
            return rootMap;
        }
        return map;
    }


    /**
     * xml转map带属性
     *
     * @param xmlStr
     * @param isRoot 是否需要在返回的map里加根节点键
     * @return
     */
    public static Map xml2mapWithAttr(String xmlStr, boolean isRoot) {
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(xmlStr);
        } catch (DocumentException e) {
            e.printStackTrace();
//            log.error("xml转map带属性 - 异常", e);
        }
        if (Objects.isNull(doc)) {
//            log.error("xml转map带属性 - doc为空");
            return null;
        }
        Element root = doc.getRootElement();
        Map<String, Object> map = xml2mapWithAttr(root);
        if (root.elements().size() == 0 && root.attributes().size() == 0) {
            //根节点只有一个文本内容
            return map;
        }
        if (isRoot) {
            //在返回的map里加根节点键（如果需要）
            Map<String, Object> rootMap = new HashMap<>();
            rootMap.put(root.getName(), map);
            return rootMap;
        }
        return map;
    }

    /**
     * xml转map 不带属性
     *
     * @param element
     * @return
     */
    private static Map xml2map(Element element) {
        Map map = new LinkedHashMap();
        List list = element.elements();
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Element iter = (Element) list.get(i);
                List mapList = new ArrayList();
                if (iter.elements().size() > 0) {
                    Map m = xml2map(iter);
                    if (map.get(iter.getName()) != null) {
                        Object obj = map.get(iter.getName());
                        if (!(obj instanceof List)) {
                            mapList = new ArrayList();
                            mapList.add(obj);
                            mapList.add(m);
                        }
                        if (obj instanceof List) {
                            mapList = (List) obj;
                            mapList.add(m);
                        }
                        map.put(iter.getName(), mapList);
                    } else {
                        map.put(iter.getName(), m);
                    }
                } else {
                    if (map.get(iter.getName()) != null) {
                        Object obj = map.get(iter.getName());
                        if (!(obj instanceof List)) {
                            mapList = new ArrayList();
                            mapList.add(obj);
                            mapList.add(iter.getText());
                        }
                        if (obj instanceof List) {
                            mapList = (List) obj;
                            mapList.add(iter.getText());
                        }
                        map.put(iter.getName(), mapList);
                    } else {
                        map.put(iter.getName(), iter.getText());
                    }
                }
            }
        } else {
            map.put(element.getName(), element.getText());
        }
        return map;
    }

    /**
     * xml转map 带属性
     *
     * @param element
     * @return
     */
    private static Map xml2mapWithAttr(Element element) {
        Map<String, Object> map = new LinkedHashMap<>();
        List<Element> list = element.elements();
        List<Attribute> listAttr0 = element.attributes(); // 当前节点的所有属性的list
        for (Attribute attr : listAttr0) {
            map.put("@" + attr.getName(), attr.getValue());
        }
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Element iter = list.get(i);
                List mapList = new ArrayList();
                if (iter.elements().size() > 0) {
                    Map m = xml2mapWithAttr(iter);
                    if (map.get(iter.getName()) != null) {
                        Object obj = map.get(iter.getName());
                        if (!(obj instanceof List)) {
                            mapList = new ArrayList();
                            mapList.add(obj);
                            mapList.add(m);
                        }
                        if (obj instanceof List) {
                            mapList = (List) obj;
                            mapList.add(m);
                        }
                        map.put(iter.getName(), mapList);
                    } else {
                        map.put(iter.getName(), m);
                    }
                } else {
                    List<Attribute> listAttr = iter.attributes(); // 当前节点的所有属性的list
                    Map<String, Object> attrMap = null;
                    boolean hasAttributes = false;
                    if (listAttr.size() > 0) {
                        hasAttributes = true;
                        attrMap = new LinkedHashMap<>();
                        for (Attribute attr : listAttr) {
                            attrMap.put("@" + attr.getName(), attr.getValue());
                        }
                    }
                    if (map.get(iter.getName()) != null) {
                        Object obj = map.get(iter.getName());
                        if (!(obj instanceof List)) {
                            mapList = new ArrayList();
                            mapList.add(obj);
                            if (hasAttributes) {
                                attrMap.put("#text", iter.getText());
                                mapList.add(attrMap);
                            } else {
                                mapList.add(iter.getText());
                            }
                        }
                        if (obj instanceof List) {
                            mapList = (List) obj;
                            if (hasAttributes) {
                                attrMap.put("#text", iter.getText());
                                mapList.add(attrMap);
                            } else {
                                mapList.add(iter.getText());
                            }
                        }
                        map.put(iter.getName(), mapList);
                    } else {
                        if (hasAttributes) {
                            attrMap.put("#text", iter.getText());
                            map.put(iter.getName(), attrMap);
                        } else {
                            map.put(iter.getName(), iter.getText());
                        }
                    }
                }
            }
        } else {
            // 根节点的
            if (listAttr0.size() > 0) {
                map.put("#text", element.getText());
            } else {
                map.put(element.getName(), element.getText());
            }
        }
        return map;
    }

    /**
     * map转xml map中没有根节点的键
     *
     * @param map
     * @param rootName
     * @throws DocumentException
     * @throws IOException
     */
    public static String map2xml(Map<String, Object> map, String rootName) {
        Document doc = DocumentHelper.createDocument();
        Element root = DocumentHelper.createElement(rootName);
        doc.add(root);
        map2xml(map, root);
        if (Objects.isNull(root)) {
            return null;
        }
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" + root.asXML();
    }

    /**
     * map转xml map中含有根节点的键
     *
     * @param map
     * @throws DocumentException
     * @throws IOException
     */
    public static Document map2xml(Map<String, Object> map) {
        Iterator<Map.Entry<String, Object>> entries = map.entrySet().iterator();
        if (entries.hasNext()) { //获取第一个键创建根节点
            Map.Entry<String, Object> entry = entries.next();
            Document doc = DocumentHelper.createDocument();
            Element root = DocumentHelper.createElement(entry.getKey());
            doc.add(root);
            map2xml((Map) entry.getValue(), root);
            return doc;
        }
        return null;
    }

    /**
     * map转xml
     *
     * @param map
     * @param body xml元素
     * @return
     */
    private static Element map2xml(Map<String, Object> map, Element body) {
        Iterator<Map.Entry<String, Object>> entries = map.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, Object> entry = entries.next();
            String key = entry.getKey();
            Object value = entry.getValue();
            if (key.startsWith("@")) {    //属性
                body.addAttribute(key.substring(1, key.length()), Objects.isNull(value) ? "" : value.toString());
            } else if (key.equals("#text")) { //有属性时的文本
                body.setText(Objects.isNull(value) ? "" : value.toString());
            } else {
                if (value instanceof List) {
                    List list = (List) value;
                    Object obj;
                    for (int i = 0; i < list.size(); i++) {
                        obj = list.get(i);
                        //list里是map或String，不会存在list里直接是list的，
                        if (obj instanceof Map) {
                            Element subElement = body.addElement(key);
                            map2xml((Map) list.get(i), subElement);
                        } else {
                            body.addElement(key).setText((String) list.get(i));
                        }
                    }
                } else if (value instanceof Map) {
                    Element subElement = body.addElement(key);
                    map2xml((Map) value, subElement);
                } else {
                    body.addElement(key).setText(Objects.isNull(value) ? "" : value.toString());
                }
            }
        }
        return body;
    }

    /**
     * 格式化输出xml
     *
     * @param xmlStr
     * @return
     * @throws DocumentException
     * @throws IOException
     */
    public static String formatXml(String xmlStr) {
        Document document = null;
        try {
            document = DocumentHelper.parseText(xmlStr);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return formatXml(document);
    }

    /**
     * 格式化输出xml
     *
     * @param document
     * @return
     * @throws DocumentException
     * @throws IOException
     */
    public static String formatXml(Document document) {
        // 格式化输出格式
        OutputFormat format = OutputFormat.createPrettyPrint();
        //format.setEncoding("UTF-8");
        StringWriter writer = new StringWriter();
        // 格式化输出流
        XMLWriter xmlWriter = new XMLWriter(writer, format);
        // 将document写入到输出流
        try {
            xmlWriter.write(document);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                xmlWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return writer.toString();
    }

    public static List<Map<String, Object>> map2xmlArr(Map<String, Object> content, String title) {
        List<Map<String, Object>> contentList = new ArrayList();
        content.forEach((k, v) -> {
            contentList.add(new HashMap<String, Object>() {{
                put("@" + title, k);
                put("#text", v);
            }});
        });
        return contentList;
    }

}
