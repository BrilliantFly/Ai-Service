package com.know.knowboot.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.know.knowboot.bean.EsPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class EsService {

    /**
     * 需要过滤的文档数据
     */
    public static final String[] IGNORE_KEY = {"@timestamp", "@version", "type"};

    /**
     * set方法前缀
     */
    public static final String SET_METHOD_PREFIX = "set";

    /**
     * 默认类型(整个type类型传闻在8.0版本后可能会废弃,但是目前7.13版本下先保留)
     */
    public static final String DEFAULT_TYPE = "_doc";

    /**
     * 关键字
     */
    public static final String KEYWORD = ".keyword";

    @Resource
    private RestHighLevelClient restHighLevelClient;

    /**
     * 获取低水平客户端
     *
     * @return
     */
    public RestClient getLowLevelClient() {
        return restHighLevelClient.getLowLevelClient();
    }


    /**
     * 方法描述: 剔除指定文档数据,减少不必要的循环
     *
     * @param map 文档数据
     */
    private static void ignoreSource(Map<String, Object> map) {
        for (String key : IGNORE_KEY) {
            map.remove(key);
        }
    }


    /**
     * 方法描述: 将文档数据转化为指定对象
     *
     * @param sourceAsMap 文档数据
     * @param clazz       转换目标Class对象
     */
    private static <T> T dealObject(Map<String, Object> sourceAsMap, Class<T> clazz) {
        try {
            ignoreSource(sourceAsMap);
            Iterator<String> keyIterator = sourceAsMap.keySet().iterator();
            T t = clazz.newInstance();
            while (keyIterator.hasNext()) {
                String key = keyIterator.next();
                String replaceKey = key.replaceFirst(key.substring(0, 1), key.substring(0, 1).toUpperCase());
                Method method = null;
                try {
                    method = clazz.getMethod(SET_METHOD_PREFIX + replaceKey, sourceAsMap.get(key).getClass());
                } catch (NoSuchMethodException e) {
                    continue;
                }
                method.invoke(t, sourceAsMap.get(key));
            }
            return t;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /********************************************************************** 索引 ****************************************************************************/

    /**
     * @description: 创建索引
     * @param: indexName 索引名
     * @return: boolean 返回对象
     */
    public boolean createIndex(String indexName) {
        //返回结果
        boolean exists = true;
        try {
            // 1、创建索引请求
            CreateIndexRequest request = new CreateIndexRequest(indexName);
            // 2、客户端执行请求 indexResponse, 请求后获得相应
            CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
            //判断响应对象是否为空
            if (createIndexResponse.equals("") || createIndexResponse == null) {
                exists = false;
            } else {
                return createIndexResponse.isAcknowledged();
            }
        } catch (IOException e) {
            exists = false;
        }
        return exists;
    }

    /**
     * @description: 测试获取索引，只能判断其是否存在
     * @param: indexName  需要判断的对象
     * @return: 执行结果
     */
    public boolean isIndexExists(String indexName) {
        boolean exists = true;
        try {
            //1.创建索引请求
            GetIndexRequest request = new GetIndexRequest(indexName);
            //2.执行客户端请求
            exists = restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            exists = false;
        }
        return exists;
    }


    /**
     * @description: 删除索引
     * @param: indexName 需要删除的索引对象
     * @return: 执行结果
     */
    public boolean delIndex(String indexName) {
        boolean exists = true;

        try {
            //删除索引请求
            DeleteIndexRequest request = new DeleteIndexRequest(indexName);
            //执行客户端请求
            AcknowledgedResponse delete = restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);
            exists = delete.isAcknowledged();
        } catch (IOException e) {
            exists = false;
        }

        return exists;
    }

    /********************************************************************** 文档 ****************************************************************************/
    /**
     * @description: 创建文档
     * @param: indexName  索引名称
     * @param: obj 文档对象（也可以JSONObject jsonObject）
     * @param: id 文档对象id (不可重复)
     * @return: 执行结果
     */
    public boolean addDocument(String indexName, Object obj, String id) {
        boolean exists = true;
        IndexResponse indexResponse = null;

        try {
            // 创建请求
            IndexRequest request = new IndexRequest(indexName);
            // 规则 put /kuang_index/_doc/1
            request.id(id);
            request.timeout(TimeValue.timeValueDays(1));
            // 将我们的数据放入请求 json
            request.source(JSON.toJSONString(obj), XContentType.JSON);
            // 客户端发送请求，获取响应结果
            indexResponse = restHighLevelClient.index(request, RequestOptions.DEFAULT);

            // 获取结果
            // indexResponse.getResult().toString() 必须转string
            String result = indexResponse.getResult().toString();

            // TODO: 2022-04-22 即可以创建，也可以更新？为什么？
            //判断响应结果对象是否为CREATED
            if (result.equals("CREATED")) {
                exists = true;
                log.info("添加数据成功 索引为: {}, response 状态: {}, id为: {}", indexName, indexResponse.status().getStatus(), indexResponse.getId());
            } else if (result.equals("UPDATED")) {
                exists = false;
                log.info("更新数据成功 索引为: {}, response 状态: {}, id为: {}", indexName, indexResponse.status().getStatus(), indexResponse.getId());
            }


        } catch (IOException e) {
            exists = false;
        }
        return exists;
    }

    /**
     * @description: 更新文档
     * @param: indexName  索引名称
     * @param: obj 文档对象（也可以JSONObject jsonObject）
     * @param: id 文档对象id (不可重复)
     * @return: 执行结果
     */
    public boolean updateDocument(String indexName, Object obj, String id) {
        boolean exists = true;
        try {
            //更新请求
            UpdateRequest updateRequest = new UpdateRequest(indexName, id);

            //保证数据实时更新
            //update.setRefreshPolicy("wait_for");

            updateRequest.timeout("1s");
            updateRequest.doc(JSON.toJSONString(obj), XContentType.JSON);

            //执行更新请求
            UpdateResponse updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);

            // updateResponse.status().toString() 必须string
            if (!updateResponse.status().toString().equals("OK")) {
                exists = false;
            }
            log.info("更新数据成功 索引为: {}, response 状态: {}, id为: {}", indexName, updateResponse.status().getStatus(), updateResponse.getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return exists;
    }

    /**
     * 通过ID 更新数据,保证实时性
     *
     * @param object    要增加的数据
     * @param indexName 索引，类似数据库
     * @param id        数据ID
     * @return
     */
    public void updateDataByIdNoRealTime(Object object, String indexName, String id) throws IOException {
        //更新请求
        UpdateRequest update = new UpdateRequest(indexName, id);

        //保证数据实时更新
        update.setRefreshPolicy("wait_for");

        update.timeout("1s");
        update.doc(JSON.toJSONString(object), XContentType.JSON);
        //执行更新请求
        UpdateResponse updateResponse = restHighLevelClient.update(update, RequestOptions.DEFAULT);

        if (updateResponse.status().toString().equals("OK")) {
            log.info("更新数据成功 索引为: {}, response 状态: {}, id为: {}", indexName, updateResponse.status().getStatus(), updateResponse.getId());
        }

    }


    /**
     * 通过ID删除数据
     *
     * @param indexName 索引，类似数据库
     * @param id        数据ID
     */
    public void deleteDataById(String indexName, String id) throws IOException {
        //删除请求
        DeleteRequest request = new DeleteRequest(indexName, id);

//        request.timeout("1s");

        //执行客户端请求
        DeleteResponse deleteResponse = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
        if (deleteResponse.status().toString().equals("OK")) {
            log.info("删除数据成功 索引为: {}, response 状态: {}, id为: {}", indexName, deleteResponse.status().getStatus(), deleteResponse.getId());
        }
    }


    /**
     * @description: 获取文档，判断是否存在
     * @param: indexName  索引名称
     * @param: id 文档对象id
     * @return: 执行结果
     */
    public boolean isExists(String indexName, String id) {
        boolean exists = true;
        try {
            GetRequest request = new GetRequest(indexName, id);
            // 不获取返回的 _source 的上下文了
            request.fetchSourceContext(new FetchSourceContext(false));
            request.storedFields("_none_");
            exists = restHighLevelClient.exists(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return exists;
    }

    /**
     * 方法描述: 判断文档是否存在
     *
     * @param indexName 索引
     * @param type      类型
     * @param id        文档id
     * @return: boolean
     */
    public boolean isExistsDocument(String indexName, String type, String id) {
        GetRequest request = new GetRequest(indexName, type, id);
        try {
            GetResponse response = restHighLevelClient.get(request, RequestOptions.DEFAULT);
            return response.isExists();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 方法描述: 根据id查询文档
     *
     * @param index 索引
     * @param id    文档id
     * @param clazz 转换目标Class对象
     * @return 对象
     */
    public <T> T selectDocumentById(String index, String id, Class<T> clazz) {
        return selectDocumentById(index, DEFAULT_TYPE, id, clazz);
    }


    /**
     * 方法描述: 根据id查询文档
     *
     * @param index 索引
     * @param type  类型
     * @param id    文档id
     * @param clazz 转换目标Class对象
     * @return 对象
     */
    public <T> T selectDocumentById(String index, String type, String id, Class<T> clazz) {
        try {
            type = type == null || type.equals("") ? DEFAULT_TYPE : type;
            GetRequest request = new GetRequest(index, type, id);
            GetResponse response = restHighLevelClient.get(request, RequestOptions.DEFAULT);
            if (response.isExists()) {
                Map<String, Object> sourceAsMap = response.getSourceAsMap();
                return dealObject(sourceAsMap, clazz);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @description: 根据id获取文档信息
     * @param: indexName  索引名称
     * @param: id 文档对象id
     * @return: 执行结果
     */
    public Map getDocument(String indexName, String id) {
        Map strToMap = null;
        try {
            GetRequest request = new GetRequest(indexName, id);
            GetResponse getResponse = restHighLevelClient.get(request, RequestOptions.DEFAULT);
            strToMap = JSONObject.parseObject(getResponse.getSourceAsString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strToMap;
    }

    /**
     * 通过ID获取数据
     *
     * @param index  索引，类似数据库
     * @param id     数据ID
     * @param fields 需要显示的字段，逗号分隔（缺省为全部字段）
     * @return
     */
    public Map<String, Object> searchDataById(String index, String id, String fields) throws IOException {
        GetRequest request = new GetRequest(index, id);
        if (StringUtils.isNotEmpty(fields)) {
            //只查询特定字段。如果需要查询所有字段则不设置该项。
            request.fetchSourceContext(new FetchSourceContext(true, fields.split(","), Strings.EMPTY_ARRAY));
        }
        GetResponse response = restHighLevelClient.get(request, RequestOptions.DEFAULT);
        Map<String, Object> map = response.getSource();
        //为返回的数据添加id
        map.put("id", response.getId());
        return map;
    }

    /**
     * 方法描述:（筛选条件）获取数据集合
     *
     * @param index         索引
     * @param sourceBuilder 请求条件
     * @param clazz         转换目标Class对象
     * @return: java.util.List<T>
     */
    public <T> List<T> selectDocumentList(String index, SearchSourceBuilder sourceBuilder, Class<T> clazz) {
        try {
            SearchRequest request = new SearchRequest(index);
            if (sourceBuilder != null) {
                // 返回实际命中数
                sourceBuilder.trackTotalHits(true);
                request.source(sourceBuilder);
            }
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            if (response.getHits() != null) {
                List<T> list = new ArrayList<>();
                SearchHit[] hits = response.getHits().getHits();
                for (SearchHit documentFields : hits) {
                    Map<String, Object> sourceAsMap = documentFields.getSourceAsMap();
                    list.add(dealObject(sourceAsMap, clazz));
                }
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 方法描述: 筛选查询,返回使用了<span style='color:red'></span>处理好的数据.
     *
     * @param: index 索引名称
     * @param: sourceBuilder sourceBuilder对象
     * @param: clazz 需要返回的对象类型.class
     * @param: highLight 需要表现的高亮匹配字段
     * @return: java.util.List<T>
     */
    public <T> List<T> selectDocumentListHighLight(String index, SearchSourceBuilder sourceBuilder, Class<T> clazz, String highLight) {
        try {
            SearchRequest request = new SearchRequest(index);
            if (sourceBuilder != null) {
                // 返回实际命中数
                sourceBuilder.trackTotalHits(true);
                //高亮
                HighlightBuilder highlightBuilder = new HighlightBuilder();
                highlightBuilder.field(highLight);
                highlightBuilder.requireFieldMatch(false);//多个高亮关闭
                highlightBuilder.preTags("<span style='color:red'>");
                highlightBuilder.postTags("</span>");
                sourceBuilder.highlighter(highlightBuilder);
                request.source(sourceBuilder);
            }
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            if (response.getHits() != null) {
                List<T> list = new ArrayList<>();
                for (SearchHit documentFields : response.getHits().getHits()) {
                    Map<String, HighlightField> highlightFields = documentFields.getHighlightFields();
                    HighlightField title = highlightFields.get(highLight);
                    Map<String, Object> sourceAsMap = documentFields.getSourceAsMap();
                    if (title != null) {
                        Text[] fragments = title.fragments();
                        String n_title = "";
                        for (Text fragment : fragments) {
                            n_title += fragment;
                        }
                        sourceAsMap.put(highLight, n_title);//高亮替换原来的内容
                    }
                    list.add(dealObject(sourceAsMap, clazz));
                }
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询并分页
     *
     * @param index          索引名称
     * @param from           从第几页开始
     * @param size           文档大小限制
     * @param query          查询条件
     * @param sortField      排序字段
     * @param highlightField 高亮字段
     * @return
     */
    public <T> EsPage<T> searchListData(String index,
                                        Integer from,
                                        Integer size,
                                        SearchSourceBuilder query,
                                        Class<T> clazz,
                                        String sortField,
                                        String highlightField) throws IOException {

        EsPage esPage = new EsPage();
        esPage.setPageNo(from);
        esPage.setPageSize(size);

        // 从第0页开始查询
        from = from - 1;

        SearchRequest request = new SearchRequest(index);
        SearchSourceBuilder builder = query;
        from = from <= 0 ? 0 : from * size;
        //设置确定结果要从哪个索引开始搜索的from选项，默认为0
        builder.from(from);
        builder.size(size);
        if (StringUtils.isNotEmpty(sortField)) {
            //排序字段，注意如果proposal_no是text类型会默认带有keyword性质，需要拼接.keyword
            builder.sort(sortField + ".keyword", SortOrder.ASC);
        }
        //高亮
        HighlightBuilder highlight = new HighlightBuilder();
        highlight.field(highlightField);
        //关闭多个高亮
        highlight.requireFieldMatch(false);
        highlight.preTags("<span style='color:red'>");
        highlight.postTags("</span>");
        builder.highlighter(highlight);
        //不返回源数据。只有条数之类的数据。
        //builder.fetchSource(false);
        request.source(builder);
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        log.info("--------------------- 总数 --------------------------> total -> {}", response.getHits().getTotalHits());
        if (response.status().getStatus() == 200) {
            // 解析对象
            if (response.getHits() != null) {
                List<T> list = new ArrayList<>();
                for (SearchHit documentFields : response.getHits().getHits()) {
                    Map<String, HighlightField> highlightFields = documentFields.getHighlightFields();
                    HighlightField title = highlightFields.get(highlightField);
                    Map<String, Object> sourceAsMap = documentFields.getSourceAsMap();
                    if (title != null) {
                        Text[] fragments = title.fragments();
                        String n_title = "";
                        for (Text fragment : fragments) {
                            n_title += fragment;
                        }
                        sourceAsMap.put(highlightField, n_title);//高亮替换原来的内容
                    }
                    list.add(dealObject(sourceAsMap, clazz));
                }
                esPage.setResult(list);
            }
        }

        esPage.setTotal(Integer.valueOf(response.getHits().getTotalHits().toString().replace(" hits","")));

        return esPage;
    }

    /**
     * 高亮结果集 特殊处理
     * map转对象 JSONObject.parseObject(JSONObject.toJSONString(map), Content.class)
     *
     * @param searchResponse
     * @param highlightField
     */
    public List<Map<String, Object>> setSearchResponse(SearchResponse searchResponse, String highlightField) {
        //解析结果
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            Map<String, HighlightField> high = hit.getHighlightFields();
            HighlightField title = high.get(highlightField);

            hit.getSourceAsMap().put("id", hit.getId());

            Map<String, Object> sourceAsMap = hit.getSourceAsMap();//原来的结果
            //解析高亮字段,将原来的字段换为高亮字段
            if (title != null) {
                Text[] texts = title.fragments();
                String nTitle = "";
                for (Text text : texts) {
                    nTitle += text;
                }
                //替换
                sourceAsMap.put(highlightField, nTitle);
            }
            list.add(sourceAsMap);
        }
        return list;
    }

    /**
     * 创建Es信息
     *
     * @param indexName  索引名称
     * @param obj        文档对象
     * @param documentId 文档对象id (不可重复)
     * @return 执行结果
     */
    public Boolean createEs(String indexName, Object obj, String documentId) {

        // 校验索引是否存在
        boolean indexIsExist = isIndexExists(indexName);
        if (!indexIsExist) {
            log.info("--------------- 索引不存在创建索引 ------------------- index -> {}", indexName);
            // 如果索引不存在创建索引
            boolean createIndex = createIndex(indexName);
            if (!createIndex) {
                log.info("--------------- 创建索引失败 ------------------- index -> {}", indexName);
            }
        }

        // 创建文档
        Boolean addSuccess = addDocument(indexName, obj, documentId);
        return addSuccess;

    }

    /**
     * 更新Es信息
     *
     * @param indexName  索引名称
     * @param obj        文档对象
     * @param documentId 文档对象id (不可重复)
     * @return 执行结果
     */
    public Boolean updateEs(String indexName, Object obj, String documentId) {

        // 校验索引是否存在
        boolean indexIsExist = isIndexExists(indexName);
        if (!indexIsExist) {
            log.info("--------------- 索引不存在创建索引 ------------------- index -> {}", indexName);
            // 如果索引不存在创建索引
            boolean createIndex = createIndex(indexName);
            if (!createIndex) {
                log.info("--------------- 创建索引失败 ------------------- index -> {}", indexName);
            }
        }

        // 创建文档
        Boolean addSuccess = updateDocument(indexName, obj, documentId);
        return addSuccess;

    }

    /**
     * 更新Es信息,保证实时性
     *
     * @param indexName  索引名称
     * @param obj        文档对象
     * @param documentId 文档对象id (不可重复)
     * @return 执行结果
     */
    public void updateEsNoRealTime(String indexName, Object obj, String documentId) throws IOException {

        // 校验索引是否存在
        boolean indexIsExist = isIndexExists(indexName);
        if (!indexIsExist) {
            log.info("--------------- 索引不存在创建索引 ------------------- index -> {}", indexName);
            // 如果索引不存在创建索引
            boolean createIndex = createIndex(indexName);
            if (!createIndex) {
                log.info("--------------- 创建索引失败 ------------------- index -> {}", indexName);
            }
        }

        // 创建文档
        updateDataByIdNoRealTime(obj, indexName, documentId);

    }

}
