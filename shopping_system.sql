-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: shopping_system
-- ------------------------------------------------------
-- Server version	8.0.34

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin` (
  `admin_id` int NOT NULL AUTO_INCREMENT COMMENT '管理员 ID（主键）',
  `username` varchar(50) NOT NULL COMMENT '管理员账号',
  `password` varchar(100) NOT NULL COMMENT '密码（加密存储）',
  `name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `status` int DEFAULT '1' COMMENT '状态（1-正常，0-禁用）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `wallet` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '钱包余额',
  PRIMARY KEY (`admin_id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='管理员表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES (1,'admin','123456','系统管理员','13800138000','admin@example.com',1,'2026-03-31 15:55:51',0.00),(2,'admin01','123456','张管理员','13800138001','admin01@example.com',1,'2026-04-01 10:48:04',0.00),(3,'admin02','123456','李管理员','13800138002','admin02@example.com',1,'2026-04-01 10:48:04',0.00),(4,'admin03','123456','王管理员','13800138003','admin03@example.com',1,'2026-04-01 10:48:04',0.00),(5,'admin04','123456','赵管理员','13800138004','admin04@example.com',1,'2026-04-01 10:48:04',0.00),(6,'admin05','123456','孙管理员','13800138005','admin05@example.com',1,'2026-04-01 10:48:04',0.00),(7,'admin06','123456','周管理员','13800138006','admin06@example.com',1,'2026-04-01 10:48:04',0.00),(8,'admin07','123456','吴管理员','13800138007','admin07@example.com',1,'2026-04-01 10:48:04',0.00),(9,'admin08','123456','郑管理员','13800138008','admin08@example.com',1,'2026-04-01 10:48:04',0.00),(10,'admin09','123456','陈管理员','13800138009','admin09@example.com',1,'2026-04-01 10:48:04',0.00),(11,'admin10','123456','林管理员','13800138010','admin10@example.com',1,'2026-04-01 10:48:04',0.00),(12,'admin11','123456','黄管理员','13800138011','admin11@example.com',1,'2026-04-01 10:48:04',0.00),(13,'admin12','123456','刘管理员','13800138012','admin12@example.com',1,'2026-04-01 10:48:04',0.00),(14,'admin13','123456','谢管理员','13800138013','admin13@example.com',1,'2026-04-01 10:48:04',0.00),(15,'admin14','123456','韩管理员','13800138014','admin14@example.com',1,'2026-04-01 10:48:04',0.00),(16,'admin15','123456','冯管理员','13800138015','admin15@example.com',1,'2026-04-01 10:48:04',0.00),(17,'admin16','123456','褚管理员','13800138016','admin16@example.com',1,'2026-04-01 10:48:04',0.00),(18,'admin17','123456','卫管理员','13800138017','admin17@example.com',1,'2026-04-01 10:48:04',0.00),(19,'admin18','123456','蒋管理员','13800138018','admin18@example.com',1,'2026-04-01 10:48:04',0.00),(20,'admin19','123456','沈管理员','13800138019','admin19@example.com',1,'2026-04-01 10:48:04',0.00),(21,'admin20','123456','姚管理员','13800138020','admin20@example.com',1,'2026-04-01 10:48:04',0.00);
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cart`
--

DROP TABLE IF EXISTS `cart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '购物车记录 ID（主键）',
  `user_id` varchar(50) NOT NULL COMMENT '用户 ID（外键关联 user.user_id）',
  `product_id` int NOT NULL COMMENT '商品 ID（外键关联 product.id）',
  `quantity` int DEFAULT '1' COMMENT '购买数量',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_product` (`user_id`,`product_id`),
  KEY `fk_cart_product` (`product_id`),
  CONSTRAINT `fk_cart_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_cart_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='购物车表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart`
--

LOCK TABLES `cart` WRITE;
/*!40000 ALTER TABLE `cart` DISABLE KEYS */;
INSERT INTO `cart` VALUES (1,'U001',1,2,'2026-04-01 10:48:04','2026-04-01 10:48:04'),(2,'U001',3,1,'2026-04-01 10:48:04','2026-04-01 10:48:04'),(3,'U002',2,1,'2026-04-01 10:48:04','2026-04-01 10:48:04'),(4,'U002',5,2,'2026-04-01 10:48:04','2026-04-01 10:48:04'),(5,'U003',4,1,'2026-04-01 10:48:04','2026-04-01 10:48:04'),(6,'U003',6,1,'2026-04-01 10:48:04','2026-04-01 10:48:04'),(7,'U004',7,2,'2026-04-01 10:48:04','2026-04-01 10:48:04'),(8,'U004',8,1,'2026-04-01 10:48:04','2026-04-01 10:48:04'),(9,'U005',9,1,'2026-04-01 10:48:04','2026-04-01 10:48:04'),(10,'U005',10,2,'2026-04-01 10:48:04','2026-04-01 10:48:04'),(11,'U006',11,1,'2026-04-01 10:48:04','2026-04-01 10:48:04'),(12,'U006',12,1,'2026-04-01 10:48:04','2026-04-01 10:48:04'),(13,'U007',13,2,'2026-04-01 10:48:04','2026-04-01 10:48:04'),(14,'U007',14,1,'2026-04-01 10:48:04','2026-04-01 10:48:04'),(15,'U008',15,3,'2026-04-01 10:48:04','2026-04-01 10:48:04'),(16,'U008',16,1,'2026-04-01 10:48:04','2026-04-01 10:48:04'),(17,'U009',17,1,'2026-04-01 10:48:04','2026-04-01 10:48:04'),(18,'U009',18,2,'2026-04-01 10:48:04','2026-04-01 10:48:04'),(19,'U010',19,1,'2026-04-01 10:48:04','2026-04-01 10:48:04'),(20,'U010',20,1,'2026-04-01 10:48:04','2026-04-01 10:48:04');
/*!40000 ALTER TABLE `cart` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notification` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '通知ID',
  `user_id` varchar(50) NOT NULL COMMENT '用户ID',
  `product_id` int DEFAULT NULL COMMENT '商品ID',
  `product_name` varchar(100) DEFAULT NULL COMMENT '商品名称',
  `content` text NOT NULL COMMENT '通知内容',
  `is_read` tinyint DEFAULT '0' COMMENT '是否已读（0-未读，1-已读）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_is_read` (`is_read`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='消息通知表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification`
--

LOCK TABLES `notification` WRITE;
/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
INSERT INTO `notification` VALUES (1,'U001',1,'iPhone 15 Pro','您收藏的商品【iPhone 15 Pro】已经补货啦，快去看看吧！',1,'2026-04-11 15:04:26'),(2,'U002',1,'iPhone 15 Pro','您收藏的商品【iPhone 15 Pro】已经补货啦，快去看看吧！',0,'2026-04-11 15:07:09'),(3,'U001',1,'iPhone 15 Pro','您收藏的商品【iPhone 15 Pro】已经补货啦，快去看看吧！',0,'2026-04-11 15:07:09');
/*!40000 ALTER TABLE `notification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order`
--

DROP TABLE IF EXISTS `order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '订单 ID（主键）',
  `order_num` varchar(50) NOT NULL COMMENT '订单编号（唯一）',
  `user_id` varchar(50) NOT NULL COMMENT '用户 ID（外键关联 user.user_id）',
  `total_amount` decimal(10,2) NOT NULL COMMENT '订单总金额',
  `status` int DEFAULT '0' COMMENT '订单状态（0-待付款，1-已付款，2-已发货，3-已完成，4-已取消）',
  `receiver_name` varchar(50) DEFAULT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(20) DEFAULT NULL COMMENT '收货人电话',
  `receiver_address` varchar(200) DEFAULT NULL COMMENT '收货地址',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
  `pay_time` datetime DEFAULT NULL COMMENT '付款时间',
  `deliver_time` datetime DEFAULT NULL COMMENT '发货时间',
  `finish_time` datetime DEFAULT NULL COMMENT '完成时间',
  `remark` text COMMENT '订单备注',
  `receipt_image` varchar(200) DEFAULT NULL COMMENT '收据/凭证图片链接',
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_num` (`order_num`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_order_num` (`order_num`),
  KEY `idx_status` (`status`),
  CONSTRAINT `fk_order_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order`
--

LOCK TABLES `order` WRITE;
/*!40000 ALTER TABLE `order` DISABLE KEYS */;
INSERT INTO `order` VALUES (1,'ORD20260401001','U001',8999.00,1,'张三','13800138001','北京市朝阳区 XX 街道','2026-04-01 10:48:04',NULL,NULL,NULL,'请尽快发货',NULL),(2,'ORD20260401002','U002',14999.00,2,'李四','13800138002','上海市浦东新区 XX 路','2026-04-01 10:48:04',NULL,NULL,NULL,'需要发票',NULL),(3,'ORD20260401003','U003',1999.00,3,'王五','13800138003','广州市天河区 XX 小区','2026-04-01 10:48:04',NULL,NULL,NULL,'',NULL),(4,'ORD20260401004','U004',6999.00,1,'赵六','13800138004','深圳市南山区 XX 大厦','2026-04-01 10:48:04',NULL,NULL,NULL,'周末配送',NULL),(5,'ORD20260401005','U005',5999.00,1,'孙七','13800138005','杭州市西湖区 XX 花园','2026-04-01 10:48:04',NULL,NULL,NULL,'',NULL),(6,'ORD20260401006','U006',4999.00,2,'周八','13800138006','成都市武侯区 XX 广场','2026-04-01 10:48:04',NULL,NULL,NULL,'放快递柜',NULL),(7,'ORD20260401007','U007',5499.00,1,'吴九','13800138007','南京市鼓楼区 XX 新村','2026-04-01 10:48:04',NULL,NULL,NULL,'',NULL),(8,'ORD20260401008','U008',5699.00,3,'郑十','13800138008','武汉市江汉区 XX 国际','2026-04-01 10:48:04',NULL,NULL,NULL,'需要签收',NULL),(9,'ORD20260401009','U009',12999.00,1,'陈一','13800138009','西安市雁塔区 XX 中心','2026-04-01 10:48:04',NULL,NULL,NULL,'',NULL),(10,'ORD20260401010','U010',11999.00,2,'林二','13800138010','重庆市渝中区 XX 大厦','2026-04-01 10:48:04',NULL,NULL,NULL,'工作日配送',NULL),(11,'ORD20260401011','U011',6999.00,0,'黄三','13800138011','天津市和平区 XX 广场','2026-04-01 10:48:04',NULL,NULL,NULL,'',NULL),(12,'ORD20260401012','U012',7499.00,1,'刘四','13800138012','长沙市岳麓区 XX 苑','2026-04-01 10:48:04',NULL,NULL,NULL,'下午配送',NULL),(13,'ORD20260401013','U013',9999.00,3,'谢五','13800138013','郑州市金水区 XX 路','2026-04-01 10:48:04',NULL,NULL,NULL,'',NULL),(14,'ORD20260401014','U014',2499.00,1,'韩六','13800138014','济南市历下区 XX 街','2026-04-01 10:48:04',NULL,NULL,NULL,'需要验货',NULL),(15,'ORD20260401015','U015',2299.00,2,'冯七','13800138015','青岛市市南区 XX 湾','2026-04-01 10:48:04',NULL,NULL,NULL,'',NULL),(16,'ORD20260401016','U016',3199.00,1,'褚八','13800138016','沈阳市和平区 XX 城','2026-04-01 10:48:04',NULL,NULL,NULL,'周末在家',NULL),(17,'ORD20260401017','U017',1688.00,0,'卫九','13800138017','大连市中山区 XX 广场','2026-04-01 10:48:04',NULL,NULL,NULL,'',NULL),(18,'ORD20260401018','U018',399.00,2,'蒋十','13800138018','厦门市思明区 XX 里','2026-04-01 10:48:04',NULL,NULL,NULL,'放门口',NULL),(19,'ORD20260401019','U019',4799.00,1,'沈一','13800138019','昆明市五华区 XX 道','2026-04-01 10:48:04',NULL,NULL,NULL,'',NULL),(20,'ORD20260401020','U020',3999.00,3,'姚二','13800138020','哈尔滨市南岗区 XX 街','2026-04-01 10:48:04',NULL,NULL,NULL,'需要收据',NULL),(21,'O202604021904303298','U001',23974002.00,3,'张飒','15011223366','广东工业大学','2026-04-02 19:04:30',NULL,NULL,NULL,'尽快',NULL),(22,'O202604081901242654','U001',5236801.00,3,'NIANBFOI','12345678900','田中','2026-04-08 19:01:24','2026-04-08 19:01:24','2026-04-08 19:01:24','2026-04-11 19:11:26','尽快发货',NULL),(23,'O202604081911256373','U001',26997.00,3,'123','123456789000','123','2026-04-08 19:11:26','2026-04-11 14:15:59','2026-04-08 19:11:26','2026-04-11 14:15:59','123','https://shopping-exam.oss-cn-beijing.aliyuncs.com/2026/04/a3ad1baf-5798-42b5-92f8-c9771ff78ffe.png');
/*!40000 ALTER TABLE `order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_item`
--

DROP TABLE IF EXISTS `order_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_item` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '订单项 ID（主键）',
  `order_id` int NOT NULL COMMENT '订单 ID（外键关联 order.id）',
  `product_id` int NOT NULL COMMENT '商品 ID（外键关联 product.id）',
  `product_name` varchar(100) DEFAULT NULL COMMENT '商品名称（冗余字段）',
  `product_price` decimal(10,2) DEFAULT NULL COMMENT '商品单价（购买时的价格）',
  `quantity` int DEFAULT NULL COMMENT '购买数量',
  `product_image` varchar(200) DEFAULT NULL COMMENT '商品图片',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  KEY `fk_order_item_product` (`product_id`),
  CONSTRAINT `fk_order_item_order` FOREIGN KEY (`order_id`) REFERENCES `order` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_order_item_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单明细表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_item`
--

LOCK TABLES `order_item` WRITE;
/*!40000 ALTER TABLE `order_item` DISABLE KEYS */;
INSERT INTO `order_item` VALUES (1,1,1,'iPhone 15 Pro',8999.00,1,'/images/iphone15pro.jpg'),(2,2,4,'MacBook Pro',14999.00,1,'/images/macbookpro.jpg'),(3,3,5,'AirPods Pro',1999.00,1,'/images/airpodspro.jpg'),(4,4,6,'华为 Mate 60 Pro',6999.00,1,'/images/mate60pro.jpg'),(5,5,7,'小米 14 Ultra',5999.00,1,'/images/mi14ultra.jpg'),(6,6,8,'OPPO Find X7',4999.00,1,'/images/findx7.jpg'),(7,7,9,'vivo X100 Pro',5499.00,1,'/images/vivox100pro.jpg'),(8,8,10,'荣耀 Magic6 Pro',5699.00,1,'/images/magic6pro.jpg'),(9,9,11,'戴尔 XPS 15',12999.00,1,'/images/xps15.jpg'),(10,10,12,'联想 ThinkPad X1',11999.00,1,'/images/thinkpadx1.jpg'),(11,11,13,'惠普战 66',6999.00,1,'/images/hpzhan66.jpg'),(12,12,14,'华硕灵耀 14',7499.00,1,'/images/asuslingyao14.jpg'),(13,13,15,'微软 Surface Pro 9',9999.00,1,'/images/surfacepro9.jpg'),(14,14,16,'Sony WH-1000XM5',2499.00,1,'/images/sony1000xm5.jpg'),(15,15,17,'Bose QC45',2299.00,1,'/images/boseqc45.jpg'),(16,16,18,'Apple Watch S9',3199.00,1,'/images/applewatchs9.jpg'),(17,17,19,'华为 Watch GT4',1688.00,1,'/images/watchgt4.jpg'),(18,18,20,'小米手环 8 Pro',399.00,1,'/images/miband8pro.jpg'),(19,19,21,'iPad Air 5',4799.00,1,'/images/ipadair5.jpg'),(20,20,22,'华为 MatePad Pro',3999.00,1,'/images/matepadpro.jpg'),(21,21,1,'iPhone 15 Pro',8999.00,999,NULL),(22,21,2,'MacBook Pro',14999.00,999,NULL),(23,22,1,'iPhone 15 Pro',8999.00,521,NULL),(24,22,3,'AirPods Pro',1999.00,123,NULL),(25,22,7,'vivo X100 Pro',5499.00,55,NULL),(26,23,1,'iPhone 15 Pro',8999.00,3,NULL);
/*!40000 ALTER TABLE `order_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '商品 ID（主键）',
  `name` varchar(100) NOT NULL COMMENT '商品名称',
  `description` text COMMENT '商品描述',
  `price` decimal(10,2) NOT NULL COMMENT '商品价格',
  `stock` int DEFAULT '0' COMMENT '库存数量',
  `category` varchar(50) DEFAULT NULL COMMENT '商品分类',
  `image_url` varchar(200) DEFAULT NULL COMMENT '商品图片 URL',
  `status` int DEFAULT '1' COMMENT '商品状态（1-上架，0-下架）',
  PRIMARY KEY (`id`),
  KEY `idx_category` (`category`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,'iPhone 15 Pro','苹果最新款手机',8999.00,3,'手机数码',NULL,1),(2,'MacBook Pro','苹果笔记本电脑',14999.00,50,'电脑办公',NULL,1),(3,'AirPods Pro','无线蓝牙耳机',1999.00,200,'手机数码',NULL,1),(4,'华为 Mate 60 Pro','华为旗舰手机，麒麟芯片',6999.00,150,'手机数码',NULL,1),(5,'小米 14 Ultra','徕卡影像，骁龙 8Gen3',5999.00,200,'手机数码',NULL,1),(6,'OPPO Find X7','哈苏影像，天玑 9300',4999.00,180,'手机数码',NULL,1),(7,'vivo X100 Pro','蔡司光学，天玑 9300',5499.00,160,'手机数码',NULL,1),(8,'荣耀 Magic6 Pro','鹰眼相机，骁龙 8Gen3',5699.00,140,'手机数码',NULL,1),(9,'戴尔 XPS 15','高性能轻薄本',12999.00,80,'电脑办公',NULL,1),(10,'联想 ThinkPad X1','商务笔记本',11999.00,60,'电脑办公',NULL,1),(11,'惠普战 66','商务办公本',6999.00,120,'电脑办公',NULL,1),(12,'华硕灵耀 14','OLED 屏轻薄本',7499.00,100,'电脑办公',NULL,1),(13,'微软 Surface Pro 9','二合一平板笔记本',9999.00,70,'电脑办公',NULL,1),(14,'Sony WH-1000XM5','降噪无线耳机',2499.00,300,'影音设备',NULL,1),(15,'Bose QC45','头戴式降噪耳机',2299.00,250,'影音设备',NULL,1),(16,'Apple Watch S9','智能手表',3199.00,200,'智能穿戴',NULL,1),(17,'华为 Watch GT4','长续航智能手表',1688.00,220,'智能穿戴',NULL,1),(18,'小米手环 8 Pro','大屏运动手环',399.00,500,'智能穿戴',NULL,1),(19,'iPad Air 5','苹果平板电脑',4799.00,150,'平板电脑',NULL,1),(20,'华为 MatePad Pro','鸿蒙平板',3999.00,130,'平板电脑',NULL,1),(21,'三星 Galaxy Tab S9','安卓旗舰平板',5999.00,90,'平板电脑',NULL,1),(22,'Switch OLED','任天堂游戏机',2099.00,180,'游戏设备',NULL,1),(23,'PS5','索尼游戏主机',3899.00,100,'游戏设备',NULL,1);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `staff`
--

DROP TABLE IF EXISTS `staff`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `staff` (
  `staff_id` int NOT NULL AUTO_INCREMENT COMMENT '员工 ID（主键）',
  `name` varchar(50) NOT NULL COMMENT '姓名',
  `password` varchar(100) NOT NULL COMMENT '密码（加密存储）',
  `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `position` varchar(50) DEFAULT NULL COMMENT '职位',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态，1正常，2冻结，3离职',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '入职时间',
  PRIMARY KEY (`staff_id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='员工表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `staff`
--

LOCK TABLES `staff` WRITE;
/*!40000 ALTER TABLE `staff` DISABLE KEYS */;
INSERT INTO `staff` VALUES (1,'张三丰','123456','13900139001','zhangsan@example.com','销售经理',1,'2026-04-01 10:48:04'),(2,'李四光','123456','13900139002','lisi@example.com','客服专员',1,'2026-04-01 10:48:04'),(3,'王五','123456','13900139003','wangwu@example.com','仓库管理',1,'2026-04-01 10:48:04'),(4,'赵六','123456','13900139004','zhaoliu@example.com','配送员',1,'2026-04-01 10:48:04'),(5,'孙七','123456','13900139005','sunqi@example.com','财务',1,'2026-04-01 10:48:04'),(6,'周八','123456','13900139006','zhouba@example.com','人事',1,'2026-04-01 10:48:04'),(7,'吴九','123456','13900139007','wujiu@example.com','技术',1,'2026-04-01 10:48:04'),(8,'郑十','123456','13900139008','zhengshi@example.com','运营',1,'2026-04-01 10:48:04'),(9,'陈一','123456','13900139009','chenyi@example.com','市场',3,'2026-04-01 10:48:04'),(10,'林二','123456','13900139010','liner@example.com','采购',1,'2026-04-01 10:48:04'),(11,'黄三','123456','13900139011','huangsan@example.com','质检',1,'2026-04-01 10:48:04'),(12,'刘四','123456','13900139012','liusi@example.com','售后',1,'2026-04-01 10:48:04'),(13,'谢五','123456','13900139013','xiewu@example.com','销售',1,'2026-04-01 10:48:04'),(14,'韩六','123456','13900139014','hanliu@example.com','库管',1,'2026-04-01 10:48:04'),(15,'冯七','123456','13900139015','fengqi@example.com','配送',1,'2026-04-01 10:48:04'),(16,'褚八','123456','13900139016','chuba@example.com','前台',1,'2026-04-01 10:48:04'),(17,'卫九','123456','13900139017','weijiu@example.com','后勤',1,'2026-04-01 10:48:04'),(18,'蒋十','123456','13900139018','jiangshi@example.com','保安',1,'2026-04-01 10:48:04'),(19,'沈一','123456','13900139019','shenyi@example.com','保洁',1,'2026-04-01 10:48:04'),(20,'姚二','123456','13900139020','yaoer@example.com','维修',1,'2026-04-01 10:48:04'),(21,'我的天','123456',NULL,NULL,NULL,1,'2026-04-02 17:26:15');
/*!40000 ALTER TABLE `staff` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `user_id` varchar(50) NOT NULL COMMENT '用户 ID（主键）',
  `name` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码（加密存储）',
  `sex` varchar(10) DEFAULT NULL COMMENT '性别',
  `money` decimal(10,2) DEFAULT '0.00' COMMENT '账户余额',
  PRIMARY KEY (`user_id`),
  KEY `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('U001','张三','123456','男',70762199.00),('U002','李四','123456','女',2000.00),('U003','王五','123456','男',1500.00),('U004','赵六','123456','女',2500.00),('U005','孙七','123456','男',3000.00),('U006','周八','123456','女',1800.00),('U007','吴九','123456','男',2200.00),('U008','郑十','123456','女',3500.00),('U009','陈一','123456','男',1200.00),('U010','林二','123456','女',2800.00),('U011','黄三','123456','男',4000.00),('U012','刘四','123456','女',1600.00),('U013','谢五','123456','男',2100.00),('U014','韩六','123456','女',3200.00),('U015','冯七','123456','男',1900.00),('U016','褚八','123456','女',2600.00),('U017','卫九','123456','男',3800.00),('U018','蒋十','123456','女',1400.00),('U019','沈一','123456','男',2300.00),('U020','姚二','123456','女',3100.00),('U021','康三','123456','男',1700.00),('U022','钱四','123456','女',2900.00);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_favorite`
--

DROP TABLE IF EXISTS `user_favorite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_favorite` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '收藏记录ID',
  `user_id` varchar(50) NOT NULL COMMENT '用户ID',
  `product_id` int NOT NULL COMMENT '商品ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_product` (`user_id`,`product_id`) COMMENT '用户和商品的唯一约束',
  KEY `idx_user_id` (`user_id`),
  KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户商品收藏表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_favorite`
--

LOCK TABLES `user_favorite` WRITE;
/*!40000 ALTER TABLE `user_favorite` DISABLE KEYS */;
INSERT INTO `user_favorite` VALUES (3,'U001',1,'2026-04-11 07:47:50'),(4,'U002',1,'2026-04-11 15:06:55');
/*!40000 ALTER TABLE `user_favorite` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-04-12 18:52:26
