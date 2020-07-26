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
        user.setGender("男");
        String  jsonString=JSONObject.toJSONString(user);
        System.out.println("请求参数为------"+jsonString);
        request.source(jsonString, XContentType.JSON);
        IndexResponse indexResponse = restHighLevelClient.index(request, GulimallElasticSearchConfig.COMMON_OPTIONS);
        System.out.println("响应结果为-----"+indexResponse);
    }
    @Test
    public void searchRequest() throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("bank");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //构造查询条件
       // MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("address", "mill");
        MatchAllQueryBuilder matchAllQuery = QueryBuilders.matchAllQuery();
        sourceBuilder.query(matchAllQuery);
        //分页
        sourceBuilder.size(100);

       //查询年龄分布,并且查询这个年龄段性别为M的平均薪资和F的平均薪资以及这个年龄的的总体平均薪资
        //聚合,求出年龄分布
        TermsAggregationBuilder ageAgg = AggregationBuilders.terms("ageAgg").field("age");
        //再按照性别分布
        TermsAggregationBuilder genderAgg = AggregationBuilders.terms("genderAgg").field("gender.keyword");
        //按照年龄分布后得到平均工资
        genderAgg.subAggregation(AggregationBuilders.avg("balanceAvg").field("balance"));
        ageAgg.subAggregation(genderAgg);
        sourceBuilder.aggregation(ageAgg);
        //这个年龄段的平均薪资
        ageAgg.subAggregation(AggregationBuilders.avg("balanceAvg").field("balance"));
        System.out.println("DSL为"+ sourceBuilder);
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);
        //获取检索的结果
        SearchHit[] hits = searchResponse.getHits().getHits();
        for (SearchHit hit : hits) {
            String asString = hit.getSourceAsString();
            Account account = JSON.parseObject(asString, Account.class);
            System.out.println("检索结果为------"+account);
        }
        //获取聚合的结果
        Aggregations aggregations = searchResponse.getAggregations();
        Terms age=aggregations.get("ageAgg");
        List<? extends Terms.Bucket> list = age.getBuckets();
        for (Terms.Bucket bucket : list) {
            String keyAsString = bucket.getKeyAsString();
            long docCount = bucket.getDocCount();
            Avg avg= bucket.getAggregations().get("balanceAvg");
            String balance = avg.getValueAsString();
            System.out.println("年龄分布为:"+keyAsString+"岁的有:"+docCount+"人,平均工资为:"+balance);
            Terms genderTerms = bucket.getAggregations().get("genderAgg");
            List<? extends Terms.Bucket> buckets = genderTerms.getBuckets();
            for (Terms.Bucket bucket1 : buckets) {
                String gender = bucket1.getKeyAsString();
                long genderCount = bucket1.getDocCount();
                Avg balanceAvg = bucket1.getAggregations().get("balanceAvg");
                String valueAsString = balanceAvg.getValueAsString();
                System.out.println("性别为:"+gender+"的人数为:"+genderCount+",平均工资为:"+valueAsString);
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
