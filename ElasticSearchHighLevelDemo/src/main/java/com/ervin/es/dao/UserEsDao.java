package com.ervin.es.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ervin.es.entity.User;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.*;

@Repository
public class UserEsDao {

    @Autowired
    private RestHighLevelClient client;
    @Value("${elasticsearch.user.index}")
    private String userIndex;
    @Value("${elasticsearch.user.type}")
    private String userType;


    /**
     * 往es中插入新数据
     */
    public Map<String, Object> saveUser(User user){
        Map<String,Object> ret = new HashMap<>();

        Map map = null;
        try{
            //实体转换成map以请求es
            map = JSON.parseObject(JSON.toJSONString(user), Map.class);
        }catch (Exception e){
            e.printStackTrace();
        }

        BulkRequest bulkRequest = new BulkRequest();
        //IndexRequest构造函数第三个参数指定id
        bulkRequest.add(new IndexRequest(userIndex,userType,user.getId()).source(map));
        try{
            BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            ret.put("msg",bulkResponse);
            return ret;
        }catch (IOException e){
            ret.put("msg",e.getStackTrace());
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * 从es中删除一条数据
     */
    public Map<String,Object> deleteUser(String id){
        Map<String,Object> ret = new HashMap<>();
        BulkRequest request = new BulkRequest();
        request.add(new DeleteRequest(userIndex,userType,id));
        try {
            BulkResponse bulkResponse = client.bulk(request, RequestOptions.DEFAULT);
            ret.put("msg",bulkResponse);
            return ret;
        }catch (IOException e){
            ret.put("msg",e.getStackTrace());
            e.printStackTrace();
        }
        return ret;

    }

    /**
     * 条件查询
     */
    public List getUsers(Map<String,Object> maps) throws IOException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //判断是否是条件查询
        if (maps != null) {
            Set<String> keys = maps.keySet();
            for (String key: keys) {
                boolQueryBuilder.must(QueryBuilders.matchPhraseQuery(key, maps.get(key)));
            }
        } else {
            MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
            boolQueryBuilder.should(matchAllQueryBuilder);
        }
        searchSourceBuilder.query(boolQueryBuilder).size(10).from(0).sort("age", SortOrder.DESC);//分页以及排序
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(userIndex);
        searchRequest.types(userType);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        List list = new ArrayList();
        for (SearchHit hit: searchResponse.getHits()) {
            String s = JSON.toJSONString(hit);
            JSONObject o = JSON.parseObject(s);
            list.add(o.getJSONObject("sourceAsMap"));
        }
        return list;
    }

}