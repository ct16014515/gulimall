/*
 Navicat Premium Data Transfer

 Source Server         : gulimall_dev_3306
 Source Server Type    : MySQL
 Source Server Version : 80021
 Source Host           : 192.168.56.10:3306
 Source Schema         : gulimall_oms

 Target Server Type    : MySQL
 Target Server Version : 80021
 File Encoding         : 65001

 Date: 12/03/2021 15:59:58
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for oms_comment
-- ----------------------------
DROP TABLE IF EXISTS `oms_comment`;
CREATE TABLE `oms_comment`  (
  `id` bigint(0) NOT NULL COMMENT '主键id',
  `parent_id` bigint(0) NOT NULL DEFAULT 0 COMMENT '父评价id，当此评价为追评时，有父评价id',
  `mid` bigint(0) NOT NULL DEFAULT 0 COMMENT '用户id',
  `order_product_id` bigint(0) NOT NULL DEFAULT 0 COMMENT '订单商品id',
  `product_id` bigint(0) NOT NULL DEFAULT 0 COMMENT '商品id',
  `desc_star` tinyint(0) UNSIGNED NOT NULL DEFAULT 5 COMMENT '描述相符评分 1 - 5 星级数',
  `logistics_star` tinyint(0) UNSIGNED NOT NULL DEFAULT 5 COMMENT '物流评分',
  `service_star` tinyint(0) UNSIGNED NOT NULL DEFAULT 5 COMMENT '服务态度评分',
  `comment_star` tinyint(0) UNSIGNED NOT NULL DEFAULT 5 COMMENT '主评分 ',
  `comment_text` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '评论内容',
  `biz_type` tinyint(0) UNSIGNED NOT NULL DEFAULT 1 COMMENT '评论类型 1-评价 2-追评',
  `comment_imgs` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '评论图片',
  `comment_imgs_flag` tinyint(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否拥有图片',
  `like_num` int(0) NOT NULL DEFAULT 0 COMMENT '点赞数',
  `store_id` bigint(0) NOT NULL DEFAULT 0 COMMENT '店铺id',
  `hide_flag` tinyint(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否隐藏 1-隐藏 0-不隐藏',
  `sensitive_flag` tinyint(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否包含敏感词 1-包含 0-不包含',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `create_user` bigint(0) NOT NULL DEFAULT 0 COMMENT '创建人id',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后修改时间',
  `update_user` bigint(0) NOT NULL DEFAULT 0 COMMENT '最后修改人id',
  `is_delete` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除 1-删除 0-不删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_mid`(`mid`) USING BTREE COMMENT '用户id索引',
  INDEX `idx_product_id`(`product_id`) USING BTREE COMMENT '商品id索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '评价表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oms_comment
-- ----------------------------

-- ----------------------------
-- Table structure for oms_order
-- ----------------------------
DROP TABLE IF EXISTS `oms_order`;
CREATE TABLE `oms_order`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `member_id` bigint(0) NULL DEFAULT NULL COMMENT 'member_id',
  `order_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '订单号',
  `coupon_id` bigint(0) NULL DEFAULT NULL COMMENT '使用的优惠券',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT 'create_time',
  `member_username` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名',
  `total_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '订单总额',
  `pay_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '应付总额',
  `freight_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '运费金额',
  `promotion_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '促销优化金额（促销价、满减、阶梯价）',
  `integration_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '积分抵扣金额',
  `coupon_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '优惠券抵扣金额',
  `discount_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '后台调整订单使用的折扣金额',
  `pay_type` tinyint(0) NULL DEFAULT NULL COMMENT '支付方式【1->支付宝；2->微信；3->银联； 4->货到付款；】',
  `source_type` tinyint(0) NULL DEFAULT NULL COMMENT '订单来源[0->PC订单；1->app订单]',
  `status` tinyint(0) NULL DEFAULT NULL COMMENT '订单状态【0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单】',
  `delivery_company` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '物流公司(配送方式)',
  `delivery_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '物流单号',
  `auto_confirm_day` int(0) NULL DEFAULT NULL COMMENT '自动确认时间（天）',
  `integration` int(0) NULL DEFAULT NULL COMMENT '可以获得的积分',
  `growth` int(0) NULL DEFAULT NULL COMMENT '可以获得的成长值',
  `bill_type` tinyint(0) NULL DEFAULT NULL COMMENT '发票类型[0->不开发票；1->电子发票；2->纸质发票]',
  `bill_header` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '发票抬头',
  `bill_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '发票内容',
  `bill_receiver_phone` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收票人电话',
  `bill_receiver_email` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收票人邮箱',
  `receiver_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收货人电话',
  `receiver_post_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收货人邮编',
  `receiver_province` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '省份/直辖市',
  `receiver_city` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '城市',
  `receiver_region` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '区',
  `receiver_detail_address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '详细地址',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '订单备注',
  `confirm_status` tinyint(0) NULL DEFAULT NULL COMMENT '确认收货状态[0->未确认；1->已确认]',
  `delete_status` tinyint(0) NULL DEFAULT NULL COMMENT '删除状态【0->未删除；1->已删除】',
  `use_integration` int(0) NULL DEFAULT NULL COMMENT '下单时使用的积分',
  `payment_time` datetime(0) NULL DEFAULT NULL COMMENT '支付时间',
  `delivery_time` datetime(0) NULL DEFAULT NULL COMMENT '发货时间',
  `receive_time` datetime(0) NULL DEFAULT NULL COMMENT '确认收货时间',
  `comment_time` datetime(0) NULL DEFAULT NULL COMMENT '评价时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oms_order
-- ----------------------------
INSERT INTO `oms_order` VALUES (1, 1000000000000234343, '202011121754300451326825688581320705', NULL, '2020-11-12 17:59:04', NULL, NULL, 2.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2020-11-12 17:59:04');
INSERT INTO `oms_order` VALUES (2, 1000000000000234343, '202011121800060641326827097951350785', NULL, '2020-11-12 18:00:08', NULL, NULL, 2.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2020-11-12 18:00:08');
INSERT INTO `oms_order` VALUES (3, 1000000000000234343, '202011121803129081326827881627693057', NULL, '2020-11-12 18:03:13', NULL, NULL, 2.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2020-11-12 18:03:13');
INSERT INTO `oms_order` VALUES (4, 1000000000000234343, '202011121920055101326847228291100673', NULL, '2020-11-12 19:20:06', NULL, NULL, 2.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2020-11-12 19:20:06');
INSERT INTO `oms_order` VALUES (5, 1000000000000234343, '202011121920246941326847308746240002', NULL, '2020-11-12 19:20:25', NULL, NULL, 2.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2020-11-12 19:20:25');
INSERT INTO `oms_order` VALUES (6, 1000000000000234343, '202011122012205341326860377534857217', NULL, '2020-11-12 20:12:21', NULL, NULL, 2.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2020-11-12 20:12:21');
INSERT INTO `oms_order` VALUES (7, 1000000000000234343, '202011122020085521326862340544585730', NULL, '2020-11-12 20:20:09', NULL, NULL, 2.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2020-11-12 20:20:09');
INSERT INTO `oms_order` VALUES (8, 1000000000000234343, '202011122023123001326863111231168514', NULL, '2020-11-12 20:23:12', NULL, NULL, 2.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2020-11-12 20:23:12');
INSERT INTO `oms_order` VALUES (9, 1000000000000234343, '202011122026141281326863873877270530', NULL, '2020-11-12 20:26:14', NULL, NULL, 2.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2020-11-12 20:26:14');
INSERT INTO `oms_order` VALUES (10, 1000000000000234343, '202011122039496551326867294453776386', NULL, '2020-11-12 20:39:50', NULL, NULL, 2.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2020-11-12 20:39:50');
INSERT INTO `oms_order` VALUES (11, 1000000000000234343, '1327184920220565506', NULL, '2020-11-13 17:41:58', 'gl_5e203cc0', 0.03, 0.04, 0.01, 0.00, 0.00, 0.00, NULL, 1, 0, 4, NULL, NULL, 14, 0, 0, NULL, NULL, NULL, NULL, NULL, '林汝诚', '18356024597', '233200', '安徽省', '合肥市', '瑶海区', '瑶海区文一云河湾', NULL, 0, 0, NULL, NULL, NULL, NULL, NULL, '2020-11-13 17:41:58');
INSERT INTO `oms_order` VALUES (12, 1000000000000234343, '1327186583207550977', NULL, '2020-11-13 17:48:34', 'gl_5e203cc0', 0.04, 0.05, 0.01, 0.00, 0.00, 0.00, NULL, 1, 0, 4, NULL, NULL, 14, 0, 0, NULL, NULL, NULL, NULL, NULL, '林汝诚', '18356024597', '233200', '安徽省', '合肥市', '瑶海区', '瑶海区文一云河湾', NULL, 0, 0, NULL, NULL, NULL, NULL, NULL, '2020-11-13 17:48:34');
INSERT INTO `oms_order` VALUES (13, 1000000000000234343, '1327187390522990593', NULL, '2020-11-13 17:51:47', 'gl_5e203cc0', 0.05, 0.06, 0.01, 0.00, 0.00, 0.00, NULL, 1, 0, 4, NULL, NULL, 14, 0, 0, NULL, NULL, NULL, NULL, NULL, '林汝诚', '18356024597', '233200', '安徽省', '合肥市', '瑶海区', '瑶海区文一云河湾', NULL, 0, 0, NULL, NULL, NULL, NULL, NULL, '2020-11-13 17:51:47');
INSERT INTO `oms_order` VALUES (14, 1000000000000234343, '1327187552234381314', NULL, '2020-11-13 17:52:25', 'gl_5e203cc0', 0.05, 0.06, 0.01, 0.00, 0.00, 0.00, NULL, 1, 0, 4, NULL, NULL, 14, 0, 0, NULL, NULL, NULL, NULL, NULL, '林汝诚', '18356024597', '233200', '安徽省', '合肥市', '瑶海区', '瑶海区文一云河湾', NULL, 0, 0, NULL, NULL, NULL, NULL, NULL, '2020-11-13 17:52:25');
INSERT INTO `oms_order` VALUES (15, 1000000000000234343, '1327187837228949506', NULL, '2020-11-13 17:53:33', 'gl_5e203cc0', 0.06, 0.07, 0.01, 0.00, 0.00, 0.00, NULL, 1, 0, 4, NULL, NULL, 14, 0, 0, NULL, NULL, NULL, NULL, NULL, '林汝诚', '18356024597', '233200', '安徽省', '合肥市', '瑶海区', '瑶海区文一云河湾', NULL, 0, 0, NULL, NULL, NULL, NULL, NULL, '2020-11-13 17:53:33');
INSERT INTO `oms_order` VALUES (16, 1000000000000234343, '202011131754527211327188171569504258', NULL, '2020-11-13 17:54:53', NULL, NULL, 2.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2020-11-13 17:54:53');
INSERT INTO `oms_order` VALUES (17, 1000000000000234343, '1328159480839421954', NULL, '2020-11-16 10:14:31', 'gl_5e203cc0', 0.01, 0.02, 0.01, 0.00, 0.00, 0.00, NULL, 1, 0, 4, NULL, NULL, 14, 0, 0, NULL, NULL, NULL, NULL, NULL, '林汝诚', '18356024597', '233200', '安徽省', '合肥市', '瑶海区', '瑶海区文一云河湾', NULL, 0, 0, NULL, NULL, NULL, NULL, NULL, '2020-11-16 10:14:31');
INSERT INTO `oms_order` VALUES (18, 1000000000000234343, '1333408641432195074', NULL, '2020-11-30 21:52:48', 'gl_5e203cc0', 0.01, 0.02, 0.01, 0.00, 0.00, 0.00, NULL, 1, 0, 4, NULL, NULL, 14, 0, 0, NULL, NULL, NULL, NULL, NULL, '林汝诚', '18356024597', '233200', '安徽省', '合肥市', '瑶海区', '瑶海区文一云河湾', NULL, 0, 0, NULL, NULL, NULL, NULL, NULL, '2020-11-30 21:52:48');
INSERT INTO `oms_order` VALUES (19, 1000000000000234343, '1339767998075904002', NULL, '2020-12-18 11:02:37', 'gl_5e203cc0', 0.01, 0.02, 0.01, 0.00, 0.00, 0.00, NULL, 2, 0, 4, NULL, NULL, 14, 0, 0, NULL, NULL, NULL, NULL, NULL, '林汝诚', '18356024597', '233200', '安徽省', '合肥市', '瑶海区', '瑶海区文一云河湾', NULL, 0, 0, NULL, NULL, NULL, NULL, NULL, '2020-12-18 11:02:37');
INSERT INTO `oms_order` VALUES (20, 1000000000000234343, '1351789024360128514', NULL, '2021-01-20 15:09:53', 'gl_5e203cc0', 0.01, 0.02, 0.01, 0.00, 0.00, 0.00, NULL, 1, 0, 4, NULL, NULL, 14, 0, 0, NULL, NULL, NULL, NULL, NULL, '林汝诚', '18356024597', '233200', '安徽省', '合肥市', '瑶海区', '瑶海区文一云河湾', NULL, 0, 0, NULL, NULL, NULL, NULL, NULL, '2021-01-20 15:09:53');

-- ----------------------------
-- Table structure for oms_order_item
-- ----------------------------
DROP TABLE IF EXISTS `oms_order_item`;
CREATE TABLE `oms_order_item`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `order_id` bigint(0) NULL DEFAULT NULL COMMENT 'order_id',
  `order_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'order_sn',
  `spu_id` bigint(0) NULL DEFAULT NULL COMMENT 'spu_id',
  `spu_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'spu_name',
  `spu_pic` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'spu_pic',
  `spu_brand` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '品牌',
  `category_id` bigint(0) NULL DEFAULT NULL COMMENT '商品分类id',
  `sku_id` bigint(0) NULL DEFAULT NULL COMMENT '商品sku编号',
  `sku_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品sku名字',
  `sku_pic` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品sku图片',
  `sku_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '商品sku价格',
  `sku_quantity` int(0) NULL DEFAULT NULL COMMENT '商品购买的数量',
  `sku_attrs_vals` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品销售属性组合（JSON）',
  `promotion_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '商品促销分解金额',
  `coupon_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '优惠券优惠分解金额',
  `integration_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '积分优惠分解金额',
  `real_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '该商品经过优惠后的分解金额',
  `gift_integration` int(0) NULL DEFAULT NULL COMMENT '赠送积分',
  `gift_growth` int(0) NULL DEFAULT NULL COMMENT '赠送成长值',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单项信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oms_order_item
-- ----------------------------
INSERT INTO `oms_order_item` VALUES (1, NULL, '202011121754300451326825688581320705', NULL, NULL, NULL, NULL, NULL, 17, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `oms_order_item` VALUES (2, NULL, '202011121800060641326827097951350785', NULL, NULL, NULL, NULL, NULL, 17, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `oms_order_item` VALUES (3, NULL, '202011121803129081326827881627693057', NULL, NULL, NULL, NULL, NULL, 17, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `oms_order_item` VALUES (4, NULL, '202011121920055101326847228291100673', NULL, NULL, NULL, NULL, NULL, 17, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `oms_order_item` VALUES (5, NULL, '202011121920246941326847308746240002', NULL, NULL, NULL, NULL, NULL, 17, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `oms_order_item` VALUES (6, NULL, '202011122012205341326860377534857217', NULL, NULL, NULL, NULL, NULL, 17, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `oms_order_item` VALUES (7, NULL, '202011122020085521326862340544585730', NULL, NULL, NULL, NULL, NULL, 17, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `oms_order_item` VALUES (8, NULL, '202011122023123001326863111231168514', NULL, NULL, NULL, NULL, NULL, 17, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `oms_order_item` VALUES (9, NULL, '202011122026141281326863873877270530', NULL, NULL, NULL, NULL, NULL, 17, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `oms_order_item` VALUES (10, NULL, '202011122039496551326867294453776386', NULL, NULL, NULL, NULL, NULL, 17, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `oms_order_item` VALUES (11, NULL, '1327184920220565506', 2, '华米', 'https://img14.360buyimg.com/n0/jfs/t1/119440/20/12247/145245/5f23e585Eb5949e29/f3f0b45581931d98.jpg', '华为', 225, 18, '华为Mate 20 X (5G) \"55\",\"粉色\"', 'https://img14.360buyimg.com/n1/s450x450_jfs/t1/25813/29/12657/304911/5c98c8e2E6bcf7d2d/25342f237b56fe97.jpg', 0.01, 3, '{\"颜色\":\"黑色\",\"内存\":\"128g\"}', 0.00, 0.00, 0.00, 0.03, 0, 0);
INSERT INTO `oms_order_item` VALUES (12, NULL, '1327186583207550977', 2, '华米', 'https://img14.360buyimg.com/n0/jfs/t1/119440/20/12247/145245/5f23e585Eb5949e29/f3f0b45581931d98.jpg', '华为', 225, 18, '华为Mate 20 X (5G) \"55\",\"粉色\"', 'https://img14.360buyimg.com/n1/s450x450_jfs/t1/25813/29/12657/304911/5c98c8e2E6bcf7d2d/25342f237b56fe97.jpg', 0.01, 4, '{\"颜色\":\"黑色\",\"内存\":\"128g\"}', 0.00, 0.00, 0.00, 0.04, 0, 0);
INSERT INTO `oms_order_item` VALUES (13, NULL, '1327187390522990593', 2, '华米', 'https://img14.360buyimg.com/n0/jfs/t1/119440/20/12247/145245/5f23e585Eb5949e29/f3f0b45581931d98.jpg', '华为', 225, 18, '华为Mate 20 X (5G) \"55\",\"粉色\"', 'https://img14.360buyimg.com/n1/s450x450_jfs/t1/25813/29/12657/304911/5c98c8e2E6bcf7d2d/25342f237b56fe97.jpg', 0.01, 5, '{\"颜色\":\"黑色\",\"内存\":\"128g\"}', 0.00, 0.00, 0.00, 0.05, 0, 0);
INSERT INTO `oms_order_item` VALUES (14, NULL, '1327187552234381314', 2, '华米', 'https://img14.360buyimg.com/n0/jfs/t1/119440/20/12247/145245/5f23e585Eb5949e29/f3f0b45581931d98.jpg', '华为', 225, 18, '华为Mate 20 X (5G) \"55\",\"粉色\"', 'https://img14.360buyimg.com/n1/s450x450_jfs/t1/25813/29/12657/304911/5c98c8e2E6bcf7d2d/25342f237b56fe97.jpg', 0.01, 5, '{\"颜色\":\"黑色\",\"内存\":\"128g\"}', 0.00, 0.00, 0.00, 0.05, 0, 0);
INSERT INTO `oms_order_item` VALUES (15, NULL, '1327187837228949506', 2, '华米', 'https://img14.360buyimg.com/n0/jfs/t1/119440/20/12247/145245/5f23e585Eb5949e29/f3f0b45581931d98.jpg', '华为', 225, 18, '华为Mate 20 X (5G) \"55\",\"粉色\"', 'https://img14.360buyimg.com/n1/s450x450_jfs/t1/25813/29/12657/304911/5c98c8e2E6bcf7d2d/25342f237b56fe97.jpg', 0.01, 6, '{\"颜色\":\"黑色\",\"内存\":\"128g\"}', 0.00, 0.00, 0.00, 0.06, 0, 0);
INSERT INTO `oms_order_item` VALUES (16, NULL, '202011131754527211327188171569504258', NULL, NULL, NULL, NULL, NULL, 17, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `oms_order_item` VALUES (17, NULL, '1328159480839421954', 2, '华米', 'https://img14.360buyimg.com/n0/jfs/t1/119440/20/12247/145245/5f23e585Eb5949e29/f3f0b45581931d98.jpg', '华为', 225, 17, '华为Mate 20 X (5G) \"55\",\"粉色\"', 'https://img14.360buyimg.com/n1/s450x450_jfs/t1/25813/29/12657/304911/5c98c8e2E6bcf7d2d/25342f237b56fe97.jpg', 0.01, 1, '{\"颜色\":\"白色\",\"内存\":\"64g\"}', 0.00, 0.00, 0.00, 0.01, 0, 0);
INSERT INTO `oms_order_item` VALUES (18, NULL, '1333408641432195074', 2, '华米', 'https://img14.360buyimg.com/n0/jfs/t1/119440/20/12247/145245/5f23e585Eb5949e29/f3f0b45581931d98.jpg', '华为', 225, 17, '华为Mate 20 X (5G) \"55\",\"粉色\"', 'https://img14.360buyimg.com/n1/s450x450_jfs/t1/25813/29/12657/304911/5c98c8e2E6bcf7d2d/25342f237b56fe97.jpg', 0.01, 1, '{\"颜色\":\"白色\",\"内存\":\"64g\"}', 0.00, 0.00, 0.00, 0.01, 0, 0);
INSERT INTO `oms_order_item` VALUES (19, NULL, '1339767998075904002', 2, '华米', 'https://img14.360buyimg.com/n0/jfs/t1/119440/20/12247/145245/5f23e585Eb5949e29/f3f0b45581931d98.jpg', '华为', 225, 17, '华为Mate 20 X (5G) \"55\",\"粉色\"', 'https://img14.360buyimg.com/n1/s450x450_jfs/t1/25813/29/12657/304911/5c98c8e2E6bcf7d2d/25342f237b56fe97.jpg', 0.01, 1, '{\"颜色\":\"白色\",\"内存\":\"64g\"}', 0.00, 0.00, 0.00, 0.01, 0, 0);
INSERT INTO `oms_order_item` VALUES (20, NULL, '1351789024360128514', 2, '华米', 'https://img14.360buyimg.com/n0/jfs/t1/119440/20/12247/145245/5f23e585Eb5949e29/f3f0b45581931d98.jpg', '华为', 225, 17, '华为Mate 20 X (5G) \"55\",\"粉色\"', 'https://img14.360buyimg.com/n1/s450x450_jfs/t1/25813/29/12657/304911/5c98c8e2E6bcf7d2d/25342f237b56fe97.jpg', 0.01, 1, '{\"颜色\":\"白色\",\"内存\":\"64g\"}', 0.00, 0.00, 0.00, 0.01, 0, 0);

-- ----------------------------
-- Table structure for oms_order_operate_history
-- ----------------------------
DROP TABLE IF EXISTS `oms_order_operate_history`;
CREATE TABLE `oms_order_operate_history`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `order_id` bigint(0) NULL DEFAULT NULL COMMENT '订单id',
  `operate_man` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作人[用户；系统；后台管理员]',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '操作时间',
  `order_status` tinyint(0) NULL DEFAULT NULL COMMENT '订单状态【0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单】',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单操作历史记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oms_order_operate_history
-- ----------------------------

-- ----------------------------
-- Table structure for oms_order_return_apply
-- ----------------------------
DROP TABLE IF EXISTS `oms_order_return_apply`;
CREATE TABLE `oms_order_return_apply`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `order_id` bigint(0) NULL DEFAULT NULL COMMENT 'order_id',
  `sku_id` bigint(0) NULL DEFAULT NULL COMMENT '退货商品id',
  `order_sn` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '订单编号',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '申请时间',
  `member_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '会员用户名',
  `return_amount` decimal(10, 0) NULL DEFAULT NULL COMMENT '退款金额',
  `return_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '退货人姓名',
  `return_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '退货人电话',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '申请状态[0->待处理；1->退货中；2->已完成；3->已拒绝]',
  `handle_time` datetime(0) NULL DEFAULT NULL COMMENT '处理时间',
  `sku_img` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品图片',
  `sku_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品名称',
  `sku_brand` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品品牌',
  `sku_attrs_vals` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品销售属性(JSON)',
  `sku_count` int(0) NULL DEFAULT NULL COMMENT '退货数量',
  `sku_price` decimal(10, 0) NULL DEFAULT NULL COMMENT '商品单价',
  `sku_real_price` decimal(10, 0) NULL DEFAULT NULL COMMENT '商品实际支付单价',
  `reason` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '原因',
  `description述` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `desc_pics` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '凭证图片，以逗号隔开',
  `handle_note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '处理备注',
  `handle_man` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '处理人员',
  `receive_man` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收货人',
  `receive_time` datetime(0) NULL DEFAULT NULL COMMENT '收货时间',
  `receive_note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收货备注',
  `receive_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收货电话',
  `company_address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '公司收货地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单退货申请' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oms_order_return_apply
-- ----------------------------

-- ----------------------------
-- Table structure for oms_order_return_reason
-- ----------------------------
DROP TABLE IF EXISTS `oms_order_return_reason`;
CREATE TABLE `oms_order_return_reason`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '退货原因名',
  `sort` int(0) NULL DEFAULT NULL COMMENT '排序',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '启用状态',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT 'create_time',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '退货原因' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oms_order_return_reason
-- ----------------------------

-- ----------------------------
-- Table structure for oms_order_setting
-- ----------------------------
DROP TABLE IF EXISTS `oms_order_setting`;
CREATE TABLE `oms_order_setting`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `flash_order_overtime` int(0) NULL DEFAULT NULL COMMENT '秒杀订单超时关闭时间(分)',
  `normal_order_overtime` int(0) NULL DEFAULT NULL COMMENT '正常订单超时时间(分)',
  `confirm_overtime` int(0) NULL DEFAULT NULL COMMENT '发货后自动确认收货时间（天）',
  `finish_overtime` int(0) NULL DEFAULT NULL COMMENT '自动完成交易时间，不能申请退货（天）',
  `comment_overtime` int(0) NULL DEFAULT NULL COMMENT '订单完成后自动好评时间（天）',
  `member_level` tinyint(0) NULL DEFAULT NULL COMMENT '会员等级【0-不限会员等级，全部通用；其他-对应的其他会员等级】',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单配置信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oms_order_setting
-- ----------------------------

-- ----------------------------
-- Table structure for oms_payment_info
-- ----------------------------
DROP TABLE IF EXISTS `oms_payment_info`;
CREATE TABLE `oms_payment_info`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `order_sn` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '订单号（对外业务号）',
  `order_id` bigint(0) NULL DEFAULT NULL COMMENT '订单id',
  `alipay_trade_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '支付宝交易流水号',
  `total_amount` decimal(10, 0) NULL DEFAULT NULL COMMENT '支付总金额',
  `subject` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '交易内容',
  `payment_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '支付状态',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `confirm_time` datetime(0) NULL DEFAULT NULL COMMENT '确认时间',
  `callback_content` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '回调内容',
  `callback_time` datetime(0) NULL DEFAULT NULL COMMENT '回调时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '支付信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oms_payment_info
-- ----------------------------

-- ----------------------------
-- Table structure for oms_refund_info
-- ----------------------------
DROP TABLE IF EXISTS `oms_refund_info`;
CREATE TABLE `oms_refund_info`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `order_return_id` bigint(0) NULL DEFAULT NULL COMMENT '退款的订单',
  `refund` decimal(10, 0) NULL DEFAULT NULL COMMENT '退款金额',
  `refund_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '退款交易流水号',
  `refund_status` tinyint(1) NULL DEFAULT NULL COMMENT '退款状态',
  `refund_channel` tinyint(0) NULL DEFAULT NULL COMMENT '退款渠道[1-支付宝，2-微信，3-银联，4-汇款]',
  `refund_content` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '退款信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oms_refund_info
-- ----------------------------

-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(0) NOT NULL,
  `xid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `context` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(0) NOT NULL,
  `log_created` datetime(0) NOT NULL,
  `log_modified` datetime(0) NOT NULL,
  `ext` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ux_undo_log`(`xid`, `branch_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 38 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of undo_log
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
