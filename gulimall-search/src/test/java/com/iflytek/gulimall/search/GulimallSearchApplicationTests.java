package com.iflytek.gulimall.search;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iflytek.gulimall.search.config.GulimallElasticSearchConfig;
import lombok.Data;
import lombok.ToString;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest
public class GulimallSearchApplicationTests {
    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Test
    public void contextLoads() {
        System.out.println(restHighLevelClient);
    }

    @Test
    public void indexRequest() throws IOException {
        IndexRequest request = new IndexRequest("user");
        request.id("1");
        User user=new User();
        user.setAge(12);
        user.setName("zhangsan");
        user.setGender("???");
        String  jsonString=JSONObject.toJSONString(user);
        System.out.println("???????????????------"+jsonString);
        request.source(jsonString, XContentType.JSON);
        IndexResponse indexResponse = restHighLevelClient.index(request, GulimallElasticSearchConfig.COMMON_OPTIONS);
        System.out.println("???????????????-----"+indexResponse);
    }
    @Test
    public void searchRequest() throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("bank");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //??????????????????
       // MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("address", "mill");
        MatchAllQueryBuilder matchAllQuery = QueryBuilders.matchAllQuery();
        sourceBuilder.query(matchAllQuery);
        //??????
        sourceBuilder.size(100);

       //??????????????????,????????????????????????????????????M??????????????????F?????????????????????????????????????????????????????????
        //??????,??????????????????
        TermsAggregationBuilder ageAgg = AggregationBuilders.terms("ageAgg").field("age");
        //?????????????????????
        TermsAggregationBuilder genderAgg = AggregationBuilders.terms("genderAgg").field("gender.keyword");
        //???????????????????????????????????????
        genderAgg.subAggregation(AggregationBuilders.avg("balanceAvg").field("balance"));
        ageAgg.subAggregation(genderAgg);
        sourceBuilder.aggregation(ageAgg);
        //??????????????????????????????
        ageAgg.subAggregation(AggregationBuilders.avg("balanceAvg").field("balance"));
        System.out.println("DSL???"+ sourceBuilder);
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);
        //?????????????????????
        SearchHit[] hits = searchResponse.getHits().getHits();
        for (SearchHit hit : hits) {
            String asString = hit.getSourceAsString();
            Account account = JSON.parseObject(asString, Account.class);
            System.out.println("???????????????------"+account);
        }
        //?????????????????????
        Aggregations aggregations = searchResponse.getAggregations();
        Terms age=aggregations.get("ageAgg");
        List<? extends Terms.Bucket> list = age.getBuckets();
        for (Terms.Bucket bucket : list) {
            String keyAsString = bucket.getKeyAsString();
            long docCount = bucket.getDocCount();
            Avg avg= bucket.getAggregations().get("balanceAvg");
            String balance = avg.getValueAsString();
            System.out.println("???????????????:"+keyAsString+"?????????:"+docCount+"???,???????????????:"+balance);
            Terms genderTerms = bucket.getAggregations().get("genderAgg");
            List<? extends Terms.Bucket> buckets = genderTerms.getBuckets();
            for (Terms.Bucket bucket1 : buckets) {
                String gender = bucket1.getKeyAsString();
                long genderCount = bucket1.getDocCount();
                Avg balanceAvg = bucket1.getAggregations().get("balanceAvg");
                String valueAsString = balanceAvg.getValueAsString();
                System.out.println("?????????:"+gender+"????????????:"+genderCount+",???????????????:"+valueAsString);
            }
        }
    }
    @Data
    @ToString
    public static class Account
    {
        private int account_number;

        private int balance;

        private String firstname;

        private String lastname;

        private int age;

        private String gender;

        private String address;

        private String employer;

        private String email;

        private String city;

        private String state;

    }








    @Data
    class User {
        private Integer age;
        private String name;
        private String gender;


    }


}
