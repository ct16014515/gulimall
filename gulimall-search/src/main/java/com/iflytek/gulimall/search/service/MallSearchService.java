package com.iflytek.gulimall.search.service;


import com.alibaba.fastjson.JSON;
import com.iflytek.gulimall.common.model.es.SkuEsModel;
import com.iflytek.gulimall.search.constant.EsConstant;
import com.iflytek.gulimall.search.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.nested.Nested;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MallSearchService {
    @Autowired
    RestHighLevelClient restHighLevelClient;

    /**
     * 查询语句见queryDsl.json
     *
     * @param param
     * @return
     * @throws IOException
     */
    public SearchResult search(SearchParam param) throws IOException {
        /**
         * 构造查询条件
         */
        SearchRequest searchRequest = buildSearchRequest(param);
        if (searchRequest == null) {
            return null;
        }
        SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        /**
         * 封装返回结果
         */
        SearchResult searchResult = getSearchResult(search, param);
        System.out.println("查询结果为------" + searchResult);
        return searchResult;
    }

    /**
     * 得到搜索的返回结果
     *
     * @param search
     * @return
     */
    private SearchResult getSearchResult(SearchResponse search, SearchParam param) {

        SearchResult searchResult = new SearchResult();
        /**
         * 检索结果
         */
        SearchHit[] hits = search.getHits().getHits();
        List<SkuEsModel> collect = Arrays.stream(hits).map(item -> {
            String sourceAsString = item.getSourceAsString();
            SkuEsModel skuEsModel = JSON.parseObject(sourceAsString, SkuEsModel.class);
            //高亮
            HighlightField highlightField = item.getHighlightFields().get("skuTitle");
            if (highlightField != null) {
                Text[] skuTitles = highlightField.getFragments();
                String skuTitle = skuTitles[0].toString();
                skuEsModel.setSkuTitle(skuTitle);
            }
            return skuEsModel;
        }).collect(Collectors.toList());
        /**
         * 分页
         */
        long total = search.getHits().getTotalHits().value;
        Long pageSize = param.getPageSize();//每页大小
        long remainder = total % pageSize;//余数
        long totalPage = remainder > 0 ? total / pageSize + 1 : total / pageSize;//总页码
        searchResult.setTotalPage(totalPage);//总页码
        searchResult.setPageNum(param.getPageNumber());//当前页
        searchResult.setTotal(total);//总记录数
        searchResult.setProducts(collect);
        //聚合结果
        /**
         * 品牌聚合结果
         *  "brandIdAgg" : {
         *       "doc_count_error_upper_bound" : 0,
         *       "sum_other_doc_count" : 0,
         *       "buckets" : [
         *         {
         *           "key" : 4,
         *           "doc_count" : 2,
         *           "brandImgAgg" : {
         *             "doc_count_error_upper_bound" : 0,
         *             "sum_other_doc_count" : 0,
         *             "buckets" : [
         *               {
         *                 "key" : "http://gulimall.oss-cn-shanghai.aliyuncs.com/2019-07-18/299c55e31d7f50ae4dc85faa90d6f445_121_121.jpg",
         *                 "doc_count" : 2
         *               }
         *             ]
         *           },
         *           "brandNameAgg" : {
         *             "doc_count_error_upper_bound" : 0,
         *             "sum_other_doc_count" : 0,
         *             "buckets" : [
         *               {
         *                 "key" : "京东",
         *                 "doc_count" : 2
         *               }
         *             ]
         *           }
         *         }
         *       ]
         *     },
         */
        Aggregations aggregations = search.getAggregations();
        List<BrandVO> brandVOS = new ArrayList<>();
        Terms brandIdAgg = aggregations.get("brandIdAgg");
        List<? extends Terms.Bucket> brandIdBuckets = brandIdAgg.getBuckets();
        for (Terms.Bucket brandIdBucket : brandIdBuckets) {
            BrandVO brandVO = new BrandVO();
            //brandId
            long brandId = brandIdBucket.getKeyAsNumber().longValue();//品牌id
            brandVO.setBrandId(brandId);
            //brandImg
            Terms brandImgAgg = brandIdBucket.getAggregations().get("brandImgAgg");
            List<? extends Terms.Bucket> brandImgBuckets = brandImgAgg.getBuckets();
            for (Terms.Bucket brandImgBucket : brandImgBuckets) {
                String brandImg = brandImgBucket.getKeyAsString();
                brandVO.setBrandImg(brandImg);
            }
            //brandName
            Terms brandNameAgg = brandIdBucket.getAggregations().get("brandNameAgg");
            List<? extends Terms.Bucket> brandNameBuckets = brandNameAgg.getBuckets();
            for (Terms.Bucket brandNameBucket : brandNameBuckets) {
                String brandName = brandNameBucket.getKeyAsString();
                brandVO.setBrandName(brandName);
            }
            brandVOS.add(brandVO);
        }
        searchResult.setBrandVOS(brandVOS);
        // System.out.println("品牌聚合结果为:" + brandVOS);
        /**
         * 分类聚合结果
         * "categoryAgg" : {
         *       "doc_count_error_upper_bound" : 0,
         *       "sum_other_doc_count" : 0,
         *       "buckets" : [
         *         {
         *           "key" : 225,
         *           "doc_count" : 2,
         *           "categoryNameAgg" : {
         *             "doc_count_error_upper_bound" : 0,
         *             "sum_other_doc_count" : 0,
         *             "buckets" : [
         *               {
         *                 "key" : "手机",
         *                 "doc_count" : 2
         *               }
         *             ]
         *           }
         *         }
         *       ]
         *     }
         */
        List<CategoryVO> categoryVOS = new ArrayList<>();
        Terms categoryAgg = aggregations.get("categoryAgg");
        List<? extends Terms.Bucket> categoryAggBuckets = categoryAgg.getBuckets();
        for (Terms.Bucket categoryAggBucket : categoryAggBuckets) {
            CategoryVO categoryVO = new CategoryVO();
            long catalogId = categoryAggBucket.getKeyAsNumber().longValue();
            categoryVO.setCatalogId(catalogId);
            Terms categoryNameAgg = categoryAggBucket.getAggregations().get("categoryNameAgg");
            List<? extends Terms.Bucket> categoryNameAggBuckets = categoryNameAgg.getBuckets();
            for (Terms.Bucket categoryNameAggBucket : categoryNameAggBuckets) {
                String catalogName = categoryNameAggBucket.getKeyAsString();
                categoryVO.setCatalogName(catalogName);
            }
            categoryVOS.add(categoryVO);
        }
        // System.out.println("分类聚合的结果为:" + categoryVOS);
        searchResult.setCategoryVOS(categoryVOS);
        /**
         * 属性聚合
         *  "attrsAgg" : {
         *       "doc_count" : 22,
         *       "attrIdAgg" : {
         *         "doc_count_error_upper_bound" : 0,
         *         "sum_other_doc_count" : 0,
         *         "buckets" : [
         *           {
         *             "key" : 1,
         *             "doc_count" : 6,
         *             "attrNameAgg" : {
         *               "doc_count_error_upper_bound" : 0,
         *               "sum_other_doc_count" : 0,
         *               "buckets" : [
         *                 {
         *                   "key" : "电池容量",
         *                   "doc_count" : 6
         *                 }
         *               ]
         *             },
         *             "attrValueAgg" : {
         *               "doc_count_error_upper_bound" : 0,
         *               "sum_other_doc_count" : 0,
         *               "buckets" : [
         *                 {
         *                   "key" : "3000mAh",
         *                   "doc_count" : 2
         *                 },
         *                 {
         *                   "key" : "4000mAh",
         *                   "doc_count" : 2
         *                 },
         *                 {
         *                   "key" : "5000mAh",
         *                   "doc_count" : 2
         *                 }
         *               ]
         *             }
         *           },
         *           {
         *             "key" : 2,
         *             "doc_count" : 4,
         *             "attrNameAgg" : {
         *               "doc_count_error_upper_bound" : 0,
         *               "sum_other_doc_count" : 0,
         *               "buckets" : [
         *                 {
         *                   "key" : "尺寸",
         *                   "doc_count" : 4
         *                 }
         *               ]
         *             },
         *             "attrValueAgg" : {
         *               "doc_count_error_upper_bound" : 0,
         *               "sum_other_doc_count" : 0,
         *               "buckets" : [
         *                 {
         *                   "key" : "42",
         *                   "doc_count" : 2
         *                 },
         *                 {
         *                   "key" : "55",
         *                   "doc_count" : 2
         *                 }
         *               ]
         *             }
         *           },
         *           {
         *             "key" : 3,
         *             "doc_count" : 4,
         *             "attrNameAgg" : {
         *               "doc_count_error_upper_bound" : 0,
         *               "sum_other_doc_count" : 0,
         *               "buckets" : [
         *                 {
         *                   "key" : "颜色",
         *                   "doc_count" : 4
         *                 }
         *               ]
         *             },
         *             "attrValueAgg" : {
         *               "doc_count_error_upper_bound" : 0,
         *               "sum_other_doc_count" : 0,
         *               "buckets" : [
         *                 {
         *                   "key" : "白色",
         *                   "doc_count" : 2
         *                 },
         *                 {
         *                   "key" : "黑色",
         *                   "doc_count" : 2
         *                 }
         *               ]
         *             }
         *           },
         *           {
         *             "key" : 4,
         *             "doc_count" : 4,
         *             "attrNameAgg" : {
         *               "doc_count_error_upper_bound" : 0,
         *               "sum_other_doc_count" : 0,
         *               "buckets" : [
         *                 {
         *                   "key" : "内存",
         *                   "doc_count" : 4
         *                 }
         *               ]
         *             },
         *             "attrValueAgg" : {
         *               "doc_count_error_upper_bound" : 0,
         *               "sum_other_doc_count" : 0,
         *               "buckets" : [
         *                 {
         *                   "key" : "6g",
         *                   "doc_count" : 2
         *                 },
         *                 {
         *                   "key" : "8g",
         *                   "doc_count" : 2
         *                 }
         *               ]
         *             }
         *           },
         *           {
         *             "key" : 5,
         *             "doc_count" : 4,
         *             "attrNameAgg" : {
         *               "doc_count_error_upper_bound" : 0,
         *               "sum_other_doc_count" : 0,
         *               "buckets" : [
         *                 {
         *                   "key" : "屏幕",
         *                   "doc_count" : 4
         *                 }
         *               ]
         *             },
         *             "attrValueAgg" : {
         *               "doc_count_error_upper_bound" : 0,
         *               "sum_other_doc_count" : 0,
         *               "buckets" : [
         *                 {
         *                   "key" : "4寸",
         *                   "doc_count" : 2
         *                 },
         *                 {
         *                   "key" : "5寸",
         *                   "doc_count" : 2
         *                 }
         *               ]
         *             }
         *           }
         *         ]
         *       }
         *     }
         */
        List<AttrVO> attrVOS = new ArrayList<>();
        Nested attrsAgg = aggregations.get("attrsAgg");
        Terms attrIdAgg = attrsAgg.getAggregations().get("attrIdAgg");
        List<? extends Terms.Bucket> attrIdAggBuckets = attrIdAgg.getBuckets();
        for (Terms.Bucket attrIdAggBucket : attrIdAggBuckets) {
            AttrVO attrVO = new AttrVO();
            long attrId = attrIdAggBucket.getKeyAsNumber().longValue();
            attrVO.setAttrId(attrId);
            //属性名
            Terms attrNameAgg = attrIdAggBucket.getAggregations().get("attrNameAgg");
            List<? extends Terms.Bucket> attrNameAggBuckets = attrNameAgg.getBuckets();
            for (Terms.Bucket attrNameAggBucket : attrNameAggBuckets) {
                String attrName = attrNameAggBucket.getKeyAsString();
                attrVO.setAttrName(attrName);
            }
            //属性值
            Terms attrValueAgg = attrIdAggBucket.getAggregations().get("attrValueAgg");
            List<? extends Terms.Bucket> attrValueAggBuckets = attrValueAgg.getBuckets();
            List<String> attrValues = new ArrayList<>();
            for (Terms.Bucket attrValueAggBucket : attrValueAggBuckets) {
                String attrValue = attrValueAggBucket.getKeyAsString();
                attrValues.add(attrValue);
            }
            attrVO.setAttrValue(attrValues);
            attrVOS.add(attrVO);
        }
        //  System.out.println("属性聚合结果为:" + attrVOS);

        searchResult.setAttrVOS(attrVOS);
        attrVOS.stream().map(item -> {
            return item.getAttrName();
        }).collect(Collectors.toList());


        return searchResult;
    }

    private SearchRequest buildSearchRequest(SearchParam param) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(EsConstant.PRODUCT_INDEX);//索引名称
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //查询条件.如果关键字为空则匹配全部
        QueryBuilder queryBuilder = null;
        //如果入参关键字和分类都为空,则
        if (StringUtils.isEmpty(param.getKeyword())) {
            return null;
        } else {
            queryBuilder = QueryBuilders.matchQuery("skuTitle", param.getKeyword());//关键字匹配
        }
        boolQueryBuilder.must(queryBuilder);//查询
        //过滤条件 分类id
        if (param.getCatalog3Id() != null) {
            TermQueryBuilder termQuery = QueryBuilders.termQuery("catalogId", param.getCatalog3Id());
            boolQueryBuilder.filter(termQuery);
        }
        //过滤条件,品牌id 多个
        if (param.getBrandId() != null && param.getBrandId().size() > 0) {
            TermsQueryBuilder termsQuery = QueryBuilders.termsQuery("brandId", param.getBrandId());
            boolQueryBuilder.filter(termsQuery);
        }
        //是否有库存
        Integer hasStock = param.getHasStock();
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("hasStock", hasStock);
        boolQueryBuilder.filter(termQueryBuilder);
        //价格区间 200_500  _500  500_ 三种
        String skuPrice = param.getSkuPrice();
        if (!StringUtils.isEmpty(skuPrice)) {
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("skuPrice");
            if (skuPrice.startsWith("_")) {//_500
                long endPrice = Long.parseLong(skuPrice.split("_")[1]);
                rangeQueryBuilder.lte(endPrice);
            } else if (skuPrice.endsWith("_")) {//500_
                long startPrice = Long.parseLong(skuPrice.split("_")[0]);
                rangeQueryBuilder.gte(startPrice);
            } else {
                //200_500
                long startPrice = Long.parseLong(skuPrice.split("_")[0]);
                long endPrice = Long.parseLong(skuPrice.split("_")[1]);
                rangeQueryBuilder.gte(startPrice);
                rangeQueryBuilder.lte(endPrice);
            }
            boolQueryBuilder.filter(rangeQueryBuilder);
        }
        //属性筛选 attrs=1_5寸:6寸&attrs=2_安卓:ios
        List<String> attrs = param.getAttrs();
        if (attrs != null && attrs.size() > 0) {
            for (String attr : attrs) {
                //attrs=1_5寸^6寸
                BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
                String[] split = attr.split("_");
                long attrId = Long.parseLong(split[0]);
                String attrValueStr = split[1];
                String[] attrValue = attrValueStr.split(":");
                boolQuery.must(QueryBuilders.termQuery("attrs.attrId", attrId));
                boolQuery.must(QueryBuilders.termsQuery("attrs.attrValue", attrValue));
                NestedQueryBuilder nestedQueryBuilder = QueryBuilders.nestedQuery("attrs", boolQuery, ScoreMode.None);
                boolQueryBuilder.filter(nestedQueryBuilder);
            }
        }

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(boolQueryBuilder);
        /**
         * 页面排序条件
         * saleCount_desc/asc
         * skuPrice_desc/asc
         * hotScore_desc/asc
         */
        String sort = param.getSort();
        if (!StringUtils.isEmpty(sort)) {
            String[] sorts = sort.split("_");
            sourceBuilder.sort(sorts[0], "desc".equals(sorts[1]) ? SortOrder.DESC : SortOrder.ASC);
        }


        /**
         * 分页 
         */
        long pageNumber = param.getPageNumber();//当前页码
        long pageSize = param.getPageSize();//每页大小
        //from= (pagenum-1)size
        sourceBuilder.from((int) ((pageNumber - 1) * pageSize));//起始位置
        sourceBuilder.size((int) pageSize);//每页大小
        /**
         *高亮  <b style="color: red">aaa</b>
         */
        if (!StringUtils.isEmpty(param.getKeyword())) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.preTags("<b style=color:red>");
            highlightBuilder.postTags("</b>");
            highlightBuilder.field("skuTitle");
            sourceBuilder.highlighter(highlightBuilder);
        }
        /**
         * 按照品牌聚合
         * "brandIdAgg": {
         *       "terms": {
         *         "field": "brandId"
         *       },
         *       "aggs": {
         *         "brandNameAgg": {
         *           "terms": {
         *             "field": "brandName"
         *           }
         *         },
         *         "brandImgAgg": {
         *           "terms": {
         *             "field": "brandImg"
         *           }
         *         }
         *       }
         *     },
         */
        TermsAggregationBuilder brandIdAgg = AggregationBuilders.terms("brandIdAgg").field("brandId").size(10);
        TermsAggregationBuilder brandNameAgg = AggregationBuilders.terms("brandNameAgg").field("brandName");
        TermsAggregationBuilder brandImgAgg = AggregationBuilders.terms("brandImgAgg").field("brandImg");
        brandIdAgg.subAggregation(brandNameAgg);//子聚合
        brandIdAgg.subAggregation(brandImgAgg);//子聚合
        sourceBuilder.aggregation(brandIdAgg);
        /**
         * 分类聚合
         *  "categoryAgg": {
         *       "terms": {
         *         "field": "catalogId"
         *       },
         *       "aggs": {
         *         "categoryNameAgg": {
         *           "terms": {
         *             "field": "catalogName.keyword"
         *           }
         *         }
         *       }
         *     }
         */
        TermsAggregationBuilder catalogIdAgg = AggregationBuilders.terms("categoryAgg").field("catalogId");
        TermsAggregationBuilder categoryNameAgg = AggregationBuilders.terms("categoryNameAgg").field("catalogName.keyword");
        catalogIdAgg.subAggregation(categoryNameAgg);
        sourceBuilder.aggregation(catalogIdAgg);
        /**
         * 属性聚合
         * "attrsAgg": {
         *       "nested": {
         *         "path": "attrs"
         *       },
         *       "aggs": {
         *         "attrIdAgg": {
         *           "terms": {
         *             "field": "attrs.attrId"
         *           },
         *           "aggs": {
         *             "attrNameAgg": {
         *               "terms": {
         *                 "field": "attrs.attrName"
         *               }
         *             },
         *             "attrValueAgg": {
         *               "terms": {
         *                 "field": "attrs.attrValue"
         *               }
         *             }
         *           }
         *         }
         *       }
         *     }
         */
        NestedAggregationBuilder nestedAggregationBuilder = AggregationBuilders.nested("attrsAgg", "attrs");
        //attrs.attrId
        TermsAggregationBuilder attrIdAgg = AggregationBuilders.terms("attrIdAgg").field("attrs.attrId");
        // attrs.attrName attrs.attrValue
        TermsAggregationBuilder attrNameAgg = AggregationBuilders.terms("attrNameAgg").field("attrs.attrName");
        TermsAggregationBuilder attrValueAgg = AggregationBuilders.terms("attrValueAgg").field("attrs.attrValue");

        attrIdAgg.subAggregation(attrNameAgg);
        attrIdAgg.subAggregation(attrValueAgg);
        nestedAggregationBuilder.subAggregation(attrIdAgg);
        sourceBuilder.aggregation(nestedAggregationBuilder);
        searchRequest.source(sourceBuilder);
        System.out.println("dsl为:" + sourceBuilder);
        return searchRequest;
    }

    public static void main(String[] args) {
        //价格区间 200_500  _500  500_ 三种
        String s = "200_500";
        String[] s1 = s.split("_");
        System.out.println(s1[0]);
        System.out.println(s1[1]);

    }

}
