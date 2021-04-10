package com.iflytek.gulimall.common.utils;

import java.io.Serializable;
import java.util.List;

/**
 * 分页查询返回对象,配置多源数据库时使用
 * @author rclin
 * @param
 */
public class Pagination implements Serializable {
    private static final long serialVersionUID = -6839862525610669627L;
    //支持分页获取的最多记录数量
    public static int MAX_SIZE = 1000;

    private List<?> data;

    private Long total;

    private Integer pageSize = 15;

    private Long pageIndex = 0L;

    private Long pageCount = 0L;//总页数

    private Boolean hasMore;//是否有更多 如果是true说明实际数据量大于总条数

    private Long totalCountMax; //总条数上限

    private String linkPrefix;

    public Pagination() {

    }

    public Pagination(List<?> data, Long total, Integer pageSize,
                      Long pageIndex) {
        this.data = data;
        this.total = total;
        this.pageSize = Pagination.getPageSize(pageSize);
        this.pageIndex = pageIndex;
    }

    public Pagination(List<?> data, Long total, Integer pageSize,
                      Long pageIndex,Long totalCountMax) {
        this.data = data;
        this.total = total;
        this.pageSize = Pagination.getPageSize(pageSize);
        this.pageIndex = pageIndex;
        this.totalCountMax = totalCountMax;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }

    public Long getTotal() {
        if(null != totalCountMax){
            if(getHasMore()){
                return totalCountMax;
            }
        }
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Long pageIndex) {
        this.pageIndex = pageIndex;
    }

    public static Long getStartNumber(Long pageIndex, Integer pageSize) {
//		return (pageIndex - 1) * pageSize;
        //Modify by yangt：分页下标从0开始，获取分页代码列表下标从0计算
        return (pageIndex) * pageSize;
    }

    public static Integer getPageSize(Integer pageSize) {
        if (pageSize.compareTo(0) > 0) {
            return pageSize;
        } else {
            return 100;
        }
    }

    public Long getStartNumber() {
        return pageIndex * pageSize;
    }

    public Integer getPageSize() {
        if (pageSize.compareTo(0) > 0) {
            return pageSize;
        } else {
            return 100;
        }
    }

    public Long getPageCount() {
        total = getTotal();
        if(null != total && null != pageSize){
            pageCount = total / pageSize;
            if(total % pageSize != 0 ){
                pageCount ++;
            }
        }
        return pageCount;
    }

    public void setPageCount(Long pageCount) {
        this.pageCount = pageCount;
    }

    public String getLinkPrefix() {
        return linkPrefix;
    }

    public void setLinkPrefix(String linkPrefix) {
        this.linkPrefix = linkPrefix;
    }

    public Boolean getHasMore() {
        if(null != this.totalCountMax && null == this.hasMore){
            this.hasMore = this.total > totalCountMax;
        }
        return hasMore;
    }

    public void setHasMore(Boolean hasMore) {
        this.hasMore = hasMore;
    }

    public Long getTotalCountMax() {
        return totalCountMax;
    }

    public void setTotalCountMax(Long totalCountMax) {
        this.totalCountMax = totalCountMax;
    }
}