package com.know.knowboot.utils.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 快递工具类(快递100)
 * Created by Administrator on 2019/3/9.
 */
public class ExpressageUtils {

    public List<Map<String, String>> queryLogisticsInfoByKuadi100(String trackingNo) {
        try {
            if(StringUtils.isEmpty(trackingNo)){
                return null;
            }
            // 1.通过快递单号获取是哪个快递公司。
            String typeResult = this.get("http://www.kuaidi100.com/autonumber/autoComNum?text="+trackingNo);
            JSONObject typeJsonObject = (JSONObject) JSON.parse(typeResult);
            JSONArray typeDataArray = (JSONArray) typeJsonObject.get("auto");
            if(typeDataArray.isEmpty()){
                return null;
            }
            JSONObject typeObject = (JSONObject)typeDataArray.get(0);
            String type = typeObject.getString("comCode");
            // 2.通过快递公司及快递单号获取物流信息。
            String kuaidiResult = this.get("http://www.kuaidi100.com/query?type="+type+"&postid="+trackingNo);
            JSONObject jsonObject = (JSONObject) JSON.parse(kuaidiResult);
            String status = jsonObject.getString("status");
            if(!"200".equals(status)){
                return null;
            }
            List<Map<String, String>> list = new ArrayList<Map<String, String>>();
            JSONArray dataArray = (JSONArray) jsonObject.get("data");
            for (int i = 0; i < dataArray.size(); i++) {
                Map<String, String> tempMap = new HashMap<String, String>();
                JSONObject temp = (JSONObject) dataArray.get(i);
                tempMap.put("date", (String) temp.get("time"));
                tempMap.put("logisticsInfo", (String) temp.get("context"));
                list.add(tempMap);
            }
            System.out.println(list);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private String get(String url) throws ClientProtocolException, IOException {
        DefaultHttpClient httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager());
        HttpGet httpGet = new HttpGet(url);
        HttpUriRequest request = null;
        request = httpGet;
        HttpContext localContext = new BasicHttpContext();
        HttpResponse response = httpClient.execute(request, localContext);
        return EntityUtils.toString(response.getEntity());
    }

    public static void main(String[] args) {
        new ExpressageUtils().queryLogisticsInfoByKuadi100("70988856015486");
    }


}
