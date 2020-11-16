package com.iflytek.gulimall.search.service;

import com.alibaba.fastjson.JSONObject;

import com.iflytek.gulimall.common.feign.vo.SkuEsModel;
import com.iflytek.gulimall.search.config.GulimallElasticSearchConfig;
import com.iflytek.gulimall.search.constant.EsConstant;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductSaveService {
    @Autowired
    RestHighLevelClient restHighLevelClient;


    /**
     * 1、使用kibana建立好索引库product和映射关系
     * 2、给es中保存数据
     *
     * @param skuEsModels
     * @return
     */
    public boolean productUp(List<SkuEsModel> skuEsModels) throws IOException {
        //需要批量保存
        BulkRequest bulkRequest = new BulkRequest();
        for (SkuEsModel skuEsModel : skuEsModels) {
            IndexRequest request = new IndexRequest(EsConstant.PRODUCT_INDEX);
            request.id(skuEsModel.getSkuId().toString());
            String skuEsModelJSONStr = JSONObject.toJSONString(skuEsModel);
            request.source(skuEsModelJSONStr, XContentType.JSON);
            bulkRequest.add(request);
        }
        BulkResponse responses = restHighLevelClient.bulk(bulkRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);
        //TODO 上架出错 记录商品上架错误的id
        boolean hasFailures = responses.hasFailures();
        List<String> ids = Arrays.stream(responses.getItems()).map((item) -> {
            return item.getId();
        }).collect(Collectors.toList());
        if (hasFailures){
            log.error("商品上架出错:id为{}",ids);
            return false;
        }else {
            return true;
        }
    }
}
