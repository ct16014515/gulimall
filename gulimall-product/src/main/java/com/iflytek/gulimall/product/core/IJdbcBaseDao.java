package com.iflytek.gulimall.product.core;

import com.iflytek.gulimall.common.utils.Pagination;
import org.springframework.jdbc.core.JdbcOperations;

import java.util.List;
import java.util.Map;

public interface IJdbcBaseDao extends JdbcOperations {
    /**
     * 只查询一列数据类型对象。用于只有一行查询结果的数据
     * @param sql
     * @param params
     * @param cla Integer.class,Float.class,Double.Class,Long.class,Boolean.class,Char.class,Byte.class,Short.class
     * @return
     */
    Object queryOneColumnForSigetonRow(String sql,Object[] params,Class cla);

    /**
     * 查询返回实体对象集合
     * @param sql    sql语句
     * @param params 填充sql问号占位符数
     * @param cla    实体对象类型
     * @return
     */
    List queryForObjectList(String sql, Object[] params, final Class cla);

    /**
     * 查询返回List<Map<String,Object>>格式数据,每一个Map代表一行数据，列名为key
     * @param sql  sql语句
     * @param params 填充问号占位符数
     * @return
     */
    List<Map<String,Object>> queryForMaps(String sql, Object[] params);

    /**
     * 查询分页（MySQL数据库）
     * @param sql     终执行查询的语句
     * @param params  填充sql语句中的问号占位符数
     * @param pageIndex    想要第几页的数据
     * @param pageSize 每页显示多少条数
     * @param cla     要封装成的实体元类型
     * @return        pageList对象
     */
    Pagination queryPageForMySQL(String sql, Object[] params, long pageIndex, int pageSize, Class cla) ;
}

