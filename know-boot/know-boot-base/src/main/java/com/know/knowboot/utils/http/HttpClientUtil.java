package com.know.knowboot.utils.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * httpclient工具类
 *
 *
 */
public class HttpClientUtil {
	
	private static final Logger LOG = LoggerFactory.getLogger(HttpClientUtil.class);

	/**
	 * get请求
	 * @param url
	 * @return
	 */
	public static String doGet(String url) {
		
		String respContent = null;
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		try {
			// 创建httpClient实例. 
			httpClient = HttpClients.createDefault();
			
			URIBuilder uriBuilder = new URIBuilder(url);
			HttpGet httpGet = new HttpGet(uriBuilder.build());
			
			// 设置超时
			setTimeout(httpGet);

			// 执行请求
			response = httpClient.execute(httpGet);
			
			// 响应状态
			int statusCode = response.getStatusLine().getStatusCode();
			
			/** 请求发送成功，并得到响应 **/
			if (statusCode == HttpStatus.SC_OK) {
				respContent = EntityUtils.toString(response.getEntity(), "utf-8");
			} else {
				throw new RuntimeException("访问失败！！HTTP_STATUS=" + statusCode);
			}
		} catch (Exception e) {
			LOG.error("http调用异常", e);
			throw new RuntimeException("http调用异常", e);
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				LOG.error("http连接关闭异常", e);
			}
		}
		return respContent;
	}
	
	/**
	 * post请求
	 * @param url
	 * @param map
	 * @return
	 */
	public static String doPost(String url, Map<String, String> map) {
		String respContent = null;
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		try {
			// 创建httpClient实例. 
			httpClient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			
			// 设置超时
			setTimeout(httpPost);
			
			// 创建参数队列
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			for(String key : map.keySet()){
				nameValuePairs.add(new BasicNameValuePair(key, map.get(key)));
			}
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));

			// 执行请求
			response = httpClient.execute(httpPost);
			
			// 响应状态
			int statusCode = response.getStatusLine().getStatusCode();
			
			/** 请求发送成功，并得到响应 **/
			if (statusCode == HttpStatus.SC_OK) {
				respContent = EntityUtils.toString(response.getEntity(), "utf-8");
			} else {
				throw new RuntimeException("访问失败！！HTTP_STATUS=" + statusCode);
			}
		} catch (Exception e) {
			LOG.error("http调用异常", e);
			throw new RuntimeException("http调用异常", e);
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				LOG.error("http连接关闭异常", e);
			}
		}
		return respContent;
	}
	
	/**
	 * 下载
	 * @param url 下载地址
	 * @param downloadPath 下载文件存放全路径（带文件名）
	 * @param map 请求参数
	 */
	public static void download(String url, String downloadPath, Map<String, String> map) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		InputStream is = null;
		FileOutputStream fos = null;
		try {
			// 创建httpClient实例. 
			httpClient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			
			// 设置超时
			setTimeout(httpPost);
			
			// 创建参数队列
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			for(String key : map.keySet()){
				nameValuePairs.add(new BasicNameValuePair(key, map.get(key)));
			}
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));

			// 执行请求
			response = httpClient.execute(httpPost);
			
			// 响应状态
			int statusCode = response.getStatusLine().getStatusCode();
			
			/** 请求发送成功，并得到响应 **/
			if (statusCode == HttpStatus.SC_OK) {
				is = response.getEntity().getContent();
				File file = new File(downloadPath);
				fos = new FileOutputStream(file);
				byte[] buffer = new byte[1024 * 4];
				int len = -1;
				while ((len = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
				}
			} else {
				throw new RuntimeException("访问失败！！HTTP_STATUS=" + statusCode);
			}
		} catch (Exception e) {
			LOG.error("http调用异常", e);
			throw new RuntimeException("http调用异常", e);
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
				if (is != null) {
					is.close();
				}
				if (response != null) {
					response.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				LOG.error("http连接关闭异常", e);
			}
		}
	}
	
	/**
	 * 设置超时时间
	 * @param httpRequest
	 */
	private static void setTimeout(HttpRequestBase httpRequest) {
		RequestConfig requestConfig = RequestConfig.custom()
		        .setConnectTimeout(180000).setConnectionRequestTimeout(180000)  
		        .setSocketTimeout(200000).build();  
		httpRequest.setConfig(requestConfig);
	}
	
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		try {
			//String url = "http://localhost:8080/yff/api/cgFormDataController.do?getFormInfo";
			String url = "http://localhost:8080/zjps/service/api/review/pushProject";
			Map<String, String> map = new HashMap<String, String>();
			map.put("sign", "7DECE0736CBC3C8ECB92472E9349443D");
			map.put("body", "{\"applyUnit\":\"瑞雪肉食加工\",\"projectId\":\"297e5a475f2465d0015f2489f1900033\",\"projectName\":\"冷冻技术\",\"projectSource\":\"3\"}");
			String resp = doPost(url, map);
			
//			String url = "https://mtax.qd-n-tax.gov.cn:7443/qyyf/DownFile?serial_no=100008&fjid=21ef21c3bf5942f1834898497bf4c076";
//			download(url, "d:/超链接.txt", new HashMap<String, String>());
		} catch (Exception e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println("耗时：" + (end - start));
	}
	/***
	 * 模拟表单提交
	 * @param url
	 * @param map
	 * @return
	 */
	public static String doFormSubmission(String url, Map<String, String> map) {
		String respContent = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
        try {
            url = url+"?serial_no="+map.get("serial_no");
            HttpPost httppost = new HttpPost(url);
            map.remove("serial_no");
            
            StringBody xml = new StringBody(map.get("xml"), ContentType.create("utf-8"));
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            multipartEntityBuilder.addPart("xml", xml);
            map.remove("xml");
            
            if(map!=null&&map.size()>0){
            	Set<String> nameList = map.keySet();
            	for (String name : nameList) {
            		String filePath = map.get(name);//根据key值，获取文件路径
            		
            		File file = new File(filePath);
            		FileBody fileStream = new FileBody(file);
            			
            		multipartEntityBuilder.addPart(name, fileStream);
            	}
            }
            HttpEntity requestEntity = multipartEntityBuilder.build();
            
            httppost.setEntity(requestEntity);

            response = httpclient.execute(httppost);
            HttpEntity responseEntity = response.getEntity();
            //获取接口传递过来的数据
            respContent = EntityUtils.toString(responseEntity, "utf-8");
        }catch (Exception e) {
			LOG.error("http调用异常", e);
			throw new RuntimeException("http调用异常", e);
        }finally {
            try {
            	if (response != null) {
					response.close();
				}
				if (httpclient != null) {
					httpclient.close();
				}
			} catch (IOException e) {
				LOG.error("http连接关闭异常", e);
			}
        }//finally
        return respContent;
	}
}
