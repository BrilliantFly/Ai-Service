package com.know.knowboot.utils.xml;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.*;
import org.json.XML;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
public class XmlToJsonUtil {

    private XmlToJsonUtil(){

    }

    public static void main(String[] args) throws IOException {

        String textFromFile = FileUtils.readFileToString(new File("D:\\cateip_ie_线路.xml"), "UTF-8");

        System.out.println("---------------------------- 第一种方式 ---------------------------------");
        JSONObject jsonObject = xml2Json(textFromFile);
        log.info("----------------------- XmlToJsonUtil ------------------------- {}",jsonObject);

        System.out.println("---------------------------- 第二种方式 ---------------------------------");
        org.json.JSONObject jsonObject1 = XML.toJSONObject(textFromFile);
        log.info("-----------------------  org.json.JSONObject ------------------------- {}",jsonObject1);

        System.out.println("---------------------------- 第三种方式 ---------------------------------");
        cn.hutool.json.JSONObject jsonObject2 = cn.hutool.json.XML.toJSONObject(textFromFile);
        //        String s = FileUtil.readString(dataDTO.getPath(), CharsetUtil.charset("UTF-8"));
//        String s = FileUtil.readString(file, CharsetUtil.charset("UTF-8"));
        log.info("-----------------------  org.json.JSONObject ------------------------- {}",jsonObject2);



        // 解析txt中的json文件，然后转对象列表
//        log.info("---------------------------------------- 解析指标Json文件 ----------------------------------------");
//        String s = FileUtil.readString("D:\\7号线二期工程指标-源文件.txt", CharsetUtil.charset("UTF-8"));
//        JSONObject jsonObject = JSONObject.parseObject(s);
//        log.info(s);
//        log.info(jsonObject.toJSONString());
//        log.info("------------ json ------------ {}",jsonObject.get("stationlist"));
//        List<DtTargetJson> jsonList = JSON.parseArray(jsonObject.get("stationlist").toString(),DtTargetJson.class);
//        log.info("------------ List<DtTargetJson> ------------ {}",jsonList);


        // xml转json，然后转对象
//        log.info("---------------------------------------- 解析指标配置文件 ----------------------------------------");
//        String xmlString = FileUtil.readString("D:\\cateip_ie_线路.xml", CharsetUtil.charset("UTF-8"));
//        cn.hutool.json.JSON jsonObject1 = cn.hutool.json.XML.toJSONObject(xmlString);
//        log.info("------------ xml to json ------------ {}",jsonObject1);
//        XmlParseInfo xmlParseInfo = jsonObject1.toBean(XmlParseInfo.class);
//        log.info("------------ json to bean ------------ {}",jsonObject1);

    }

    /**
     * xml转json
     *
     * @param xmlStr
     * @return
     * @throws DocumentException
     */
    public static JSONObject xml2Json(String xmlStr) {
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(xmlStr);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        JSONObject json = new JSONObject();
        dom4j2Json(doc.getRootElement(), json);
        return json;
    }

    /**
     * xml转json
     *
     * @param element
     * @param json
     */
    private static void dom4j2Json(Element element, JSONObject json) {
        // 如果是属性
        for (Object o : element.attributes()) {
            Attribute attr = (Attribute) o;
            if (StringUtils.isNotBlank(attr.getValue())) {
                json.put("@" + attr.getName(), attr.getValue());
            }
        }
        List<Element> chdEl = element.elements();
        if (chdEl.isEmpty() && StringUtils.isNotBlank(element.getText())) {// 如果没有子元素,只有一个值
            json.put(element.getName(), element.getText());
        }
        for (Element e : chdEl) {// 有子元素
            if (!e.elements().isEmpty()) {// 子元素也有子元素
                JSONObject chdjson = new JSONObject();
                dom4j2Json(e, chdjson);
                Object o = json.get(e.getName());
                if (o != null) {
                    JSONArray jsona = null;
                    if (o instanceof JSONObject) {// 如果此元素已存在,则转为jsonArray
                        JSONObject jsono = (JSONObject) o;
                        json.remove(e.getName());
                        jsona = new JSONArray();
                        jsona.add(jsono);
                        jsona.add(chdjson);
                    }
                    if (o instanceof JSONArray) {
                        jsona = (JSONArray) o;
                        jsona.add(chdjson);
                    }
                    json.put(e.getName(), jsona);
                } else {
                    if (!chdjson.isEmpty()) {
                        json.put(e.getName(), chdjson);
                    }
                }
            } else {// 子元素没有子元素
                for (Object o : element.attributes()) {
                    Attribute attr = (Attribute) o;
                    if (StringUtils.isNotBlank(attr.getValue())) {
                        json.put("@" + attr.getName(), attr.getValue());
                    }
                }
                if (!e.getText().isEmpty()) {
                    json.put(e.getName(), e.getText());
                }
            }
        }
    }

}
