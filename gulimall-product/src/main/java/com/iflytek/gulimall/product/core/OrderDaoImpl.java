//package com.iflytek.gulimall.product.core;
//
//import com.iflytek.gulimall.product.config.SpringContextHolder;
//import com.iflytek.gulimall.product.core.entity.OrderEntity;
//import com.iflytek.gulimall.product.core.impl.JdbcBaseDao;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.annotation.Resource;
//import javax.sql.DataSource;
//import java.util.ArrayList;
//import java.util.List;
//
//@Repository
//public class OrderDaoImpl extends JdbcBaseDao  implements OrderDao{
//
//
////    public OrderDaoImpl(@Qualifier("orderDataSource") DataSource dataSource) {
////        super(dataSource);
////    }
//
//
//    public OrderDaoImpl() {
//        super(SpringContextHolder.getBean("orderDataSource"));
//    }
//
//    @Override
//    public List<OrderEntity> selectAll() {
//        StringBuffer sql = new StringBuffer("SELECT id, member_id  memberId,order_sn orderSn FROM oms_order where 1=1 and id = ? ");
//        List<Object> list = new ArrayList<>();
//        list.add(1);
//        return queryForObjectList(sql.toString(), list.toArray(), OrderEntity.class);
//    }
//
//
//    @Override
//    @Transactional("orderDataSourceTransactionManager")
//    public void update() {
//        StringBuffer sql = new StringBuffer("UPDATE oms_order set order_sn =5 where id = ?");
//        List<Object> list = new ArrayList<>();
//        list.add(1);
//        super.update(sql.toString(),list.toArray());
//        throw new RuntimeException();
//    }
//
//
//
//
//
//
//
//
//
//
//}
