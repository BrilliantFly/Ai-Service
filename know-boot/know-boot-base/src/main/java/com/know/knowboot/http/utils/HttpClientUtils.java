package com.know.knowboot.http.utils;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class HttpClientUtils {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * GET方法调用接口
     *
     * @param requestUrl
     * @param params
     * @return
     */
    public JSONObject getEntityByParam(String requestUrl, Map<String, String> params) {

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(requestUrl + changeMapToArguments(params), String.class);
        String body = responseEntity.getBody();
        JSONObject jsonObject = JSON.parseObject(body);
        return jsonObject;

    }

    /**
     * GET方法调用接口
     *
     * @param requestUrl
     * @param params
     * @return
     */
    public JSONObject getEntityByParam(String requestUrl, JSONObject params) {

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(requestUrl + changeJsonToArguments(params), String.class);
        String body = responseEntity.getBody();
        JSONObject jsonObject = JSON.parseObject(body);
        return jsonObject;

    }

    /**
     * POST方法调用接口
     *
     * @param params
     * @param url
     * @param headersParams
     * @return
     */
    public JSONObject postEntityByParam(String url, Map<String, String> headersParams, Map<String, String> params) {
        // JSON.toJSONString则是将对象转化为Json字符串
        String mapString = JSON.toJSONString(params);
        // JSON.parseObject，是将Json字符串转化为相应的对象
        JSONObject jsonObjectMap = JSON.parseObject(mapString);
        return postEntityByParam(url, changeMapToHeader(headersParams), jsonObjectMap);
    }

    /**
     * POST方法调用接口
     *
     * @param params
     * @param url
     * @param httpHeaders
     * @return
     */
    public JSONObject postEntityByParam(String url, HttpHeaders httpHeaders, JSONObject params) {
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(params, httpHeaders),
                String.class);
        String body = responseEntity.getBody();
        JSONObject jsonObject = JSON.parseObject(body);
        return jsonObject;
    }

    /**
     * POST方法调用接口
     *
     * @param url
     * @param object
     * @return
     */
    public JSONObject postEntityByParam(String url, Object object) {
        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity(url, object, String.class);
        String body = responseEntity.getBody();
        JSONObject jsonObject = JSON.parseObject(body);
        return jsonObject;
    }

    /**
     * POST方法调用接口
     *
     * @param url
     * @param object
     * @return
     */
    public JSONObject postEntityByParam(String url, JSONObject object) {
        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity(url, object, String.class);
        String body = responseEntity.getBody();
        JSONObject jsonObject = JSON.parseObject(body);
        return jsonObject;
    }

    /**
     * POST方法调用接口
     *
     * @param url
     * @param headersParams
     * @param object
     * @return
     */
    public JSONObject postEntityByParam(String url, Map<String, String> headersParams, Object object) {
        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity(url, new HttpEntity<>(object, changeMapToHeader(headersParams)), String.class);
        String body = responseEntity.getBody();
        JSONObject jsonObject = JSON.parseObject(body);
        return jsonObject;
    }

    /**
     * 把map数据转化为参数
     *
     * @param argument
     * @return
     */
    private String changeMapToArguments(Map<String, String> argument) {
        Set<String> keys = argument.keySet();
        StringBuffer arg = new StringBuffer("?");
        for (String key : keys) {
            arg.append((key) + "=" + urlEncoderText(argument.get(key)) + "&");
        }
        return arg.deleteCharAt(arg.length() - 1).toString();
    }


    /**
     * 把json数据转化为参数，为get请求和post请求stringentity的时候使用
     *
     * @param argument argument 请求参数，json数据类型，map类型，可转化
     * @return 返回拼接参数后的地址
     */
    public String changeJsonToArguments(JSONObject argument) {
        Set<String> keys = argument.keySet();
        for (String key : keys) {
            String value = argument.getString(key);
            argument.put(key, urlEncoderText(value));
        }
        String one = argument.toString();
        String two = "?" + one.substring(1, one.length() - 1).replace(",", "&").replace(":", "=").replace("\"", "");
        return two;
    }

    /**
     * url进行转码，常用于网络请求
     *
     * @param text 需要加密的文本
     * @return 返回加密后的文本
     */
    public String urlEncoderText(String text) {
        String result = "";
        try {
            result = java.net.URLEncoder.encode(text, "utf-8");
        } catch (UnsupportedEncodingException e) {
            log.error("数据格式错误！");
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 把map数据转化为httpHeaders
     *
     * @param argument
     * @return
     */
    private HttpHeaders changeMapToHeader(Map<String, String> argument) {

        HttpHeaders httpHeaders = new HttpHeaders();
        Set<String> keys = argument.keySet();
        for (String key : keys) {
            httpHeaders.add(key, argument.get(key));
        }
        return httpHeaders;
    }

    /**
     * 上传附件
     * @param url
     * @param httpServletRequest
     * @return
     */
    public JSONObject uploadFile(String url, HttpServletRequest httpServletRequest) {
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) httpServletRequest;
        // 获取上传文件对象
        MultipartFile mf = mulReq.getFile("file");
        return uploadFile(url,mf);
    }

    /**
     * 上传附件
     * @param url
     * @param file
     * @return
     */
    public JSONObject uploadFile(String url, MultipartFile file) {
        // 临时文件保存路径
        File fileTemp = FileUtil.mkdir(System.getProperty("user.dir") + "\\temp" + "\\" + file.getOriginalFilename());
        // 转存文件
        try {
            file.transferTo(fileTemp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpHeaders headers = new HttpHeaders();
        //设置提交方式都是表单提交
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        //设置表单信息
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("file_data", new FileSystemResource(fileTemp));
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(params, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                url,requestEntity, String.class);
        FileUtil.del(fileTemp);

        String body = responseEntity.getBody();
        JSONObject jsonObject = JSON.parseObject(body);
        return jsonObject;
    }

}
