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
  `admin_id` varchar(50) NOT NULL COMMENT '管理员ID',
  `name` varchar(50) NOT NULL COMMENT '管理员姓名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱地址',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号码',
  `avatar` varchar(500) DEFAULT NULL COMMENT '头像地址URL',
  `status` int DEFAULT '1' COMMENT '状态（1-正常，2-冻结，3-其他）',
  `role` int DEFAULT '2' COMMENT '角色类型（2-管理员）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`admin_id`),
  UNIQUE KEY `uk_phone` (`phone`),
  UNIQUE KEY `uk_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='管理员表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES ('1','超级管理员','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','admin@example.com','13800138005','https://shopping-exam.oss-cn-beijing.aliyuncs.com/2026/04/e56b1a72-363a-47af-9616-1303b0c6584f.jpeg',1,2,'2026-04-15 20:41:03'),('10','管理员07','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','admin07@example.com','13800138083','https://example.com/admin9.jpg',1,2,'2025-01-05 14:00:00'),('11','管理员08','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','admin08@example.com','13800138084',NULL,1,2,'2025-01-05 15:00:00'),('12','管理员09','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','admin09@example.com','13800138085','https://example.com/admin11.jpg',1,2,'2025-01-05 16:00:00'),('13','管理员10','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','admin10@example.com','13800138086','https://example.com/admin12.jpg',2,2,'2025-01-05 17:00:00'),('14','管理员11','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','admin11@example.com','13800138087',NULL,1,2,'2025-01-06 08:00:00'),('15','管理员12','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','admin12@example.com','13800138088','https://example.com/admin14.jpg',1,2,'2025-01-06 09:00:00'),('16','管理员13','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','admin13@example.com','13800138089','https://example.com/admin15.jpg',1,2,'2025-01-06 10:00:00'),('17','管理员14','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','admin14@example.com','13800138090',NULL,1,2,'2025-01-06 11:00:00'),('18','管理员15','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','admin15@example.com','13800138091','https://example.com/admin17.jpg',1,2,'2025-01-06 12:00:00'),('19','管理员16','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','admin16@example.com','13800138092','https://example.com/admin18.jpg',1,2,'2025-01-06 13:00:00'),('2','高级管理员','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','senioradmin@example.com','13800138015','https://example.com/admin2.jpg',1,2,'2025-01-01 08:00:00'),('20','管理员17','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','admin17@example.com','13800138093',NULL,2,2,'2025-01-06 14:00:00'),('21','管理员18','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','admin18@example.com','13800138094','https://example.com/admin20.jpg',1,2,'2025-01-06 15:00:00'),('22','管理员19','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','admin19@example.com','13800138095','https://example.com/admin21.jpg',1,2,'2025-01-06 16:00:00'),('23','管理员20','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','admin20@example.com','13800138096',NULL,1,2,'2025-01-06 17:00:00'),('24','管理员21','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','admin21@example.com','13800138097','https://example.com/admin23.jpg',1,2,'2025-01-07 08:00:00'),('25','管理员22','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','admin22@example.com','13800138098','https://example.com/admin24.jpg',1,2,'2025-01-07 09:00:00'),('26','管理员23','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','admin23@example.com','13800138099',NULL,1,2,'2025-01-07 10:00:00'),('27','管理员24','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','admin24@example.com','13800138100','https://example.com/admin26.jpg',1,2,'2025-01-07 11:00:00'),('28','管理员25','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','admin25@example.com','13800138101','https://example.com/admin27.jpg',2,2,'2025-01-07 12:00:00'),('29','管理员26','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','admin26@example.com','13800138102',NULL,1,2,'2025-01-07 13:00:00'),('3','系统管理员','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','sysadmin@example.com','13800138016',NULL,2,2,'2024-12-15 10:00:00'),('30','管理员27','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','admin27@example.com','13800138103','https://example.com/admin29.jpg',1,2,'2025-01-07 14:00:00'),('31','管理员28','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','admin28@example.com','13800138104','https://example.com/admin30.jpg',1,2,'2025-01-07 15:00:00'),('32','管理员29','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','admin29@example.com','13800138105',NULL,1,2,'2025-01-07 16:00:00'),('33','管理员30','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','admin30@example.com','13800138106','https://example.com/admin32.jpg',1,2,'2025-01-07 17:00:00'),('4','管理员01','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','admin01@example.com','13800138077','https://example.com/admin3.jpg',1,2,'2025-01-05 08:00:00'),('5','管理员02','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','admin02@example.com','13800138078',NULL,1,2,'2025-01-05 09:00:00'),('6','管理员03','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','admin03@example.com','13800138079','https://example.com/admin5.jpg',1,2,'2025-01-05 10:00:00'),('7','管理员04','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','admin04@example.com','13800138080','https://example.com/admin6.jpg',2,2,'2025-01-05 11:00:00'),('8','管理员05','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','admin05@example.com','13800138081',NULL,1,2,'2025-01-05 12:00:00'),('9','管理员06','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','admin06@example.com','13800138082','https://example.com/admin8.jpg',1,2,'2025-01-05 13:00:00');
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chat_message`
--

DROP TABLE IF EXISTS `chat_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chat_message` (
  `message_id` bigint NOT NULL AUTO_INCREMENT COMMENT '消息ID',
  `chat_session_id` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '会话ID',
  `sender_id` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '发送者ID',
  `receiver_id` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '接收者ID',
  `content` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '消息内容',
  `message_type` tinyint DEFAULT '1' COMMENT '消息类型: 1-文本, 2-图片, 3-文件',
  `is_read` tinyint DEFAULT '0' COMMENT '是否已读: 0-未读, 1-已读',
  `send_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
  `read_time` datetime DEFAULT NULL COMMENT '阅读时间',
  PRIMARY KEY (`message_id`),
  KEY `idx_session` (`chat_session_id`),
  KEY `idx_sender` (`sender_id`),
  KEY `idx_receiver` (`receiver_id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='聊天消息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat_message`
--

LOCK TABLES `chat_message` WRITE;
/*!40000 ALTER TABLE `chat_message` DISABLE KEYS */;
INSERT INTO `chat_message` VALUES (1,'7e0241dc10a546ecba572df10fd9753a','1','10001','？',1,0,'2026-04-22 20:03:55',NULL),(2,'7e0241dc10a546ecba572df10fd9753a','1','10001','？',1,0,'2026-04-22 20:04:19',NULL),(3,'7e0241dc10a546ecba572df10fd9753a','1','10001','？',1,0,'2026-04-22 20:05:09',NULL),(4,'7e0241dc10a546ecba572df10fd9753a','1','10001','、',1,0,'2026-04-22 20:05:16',NULL),(5,'7e0241dc10a546ecba572df10fd9753a','1','10001','？',1,0,'2026-04-22 20:06:57',NULL),(6,'7e0241dc10a546ecba572df10fd9753a','1','10001','？',1,0,'2026-04-22 20:10:09',NULL),(7,'7e0241dc10a546ecba572df10fd9753a','1','10001','？',1,0,'2026-04-22 20:11:58',NULL),(8,'7e0241dc10a546ecba572df10fd9753a','1','10001','哈喽',1,0,'2026-04-22 20:12:03',NULL),(9,'501bdce922734e90a5cdf309b7ce2311','10001','U1','你好，？？',1,0,'2026-04-22 20:13:34',NULL),(10,'501bdce922734e90a5cdf309b7ce2311','10001','U1','？',1,0,'2026-04-22 20:13:44',NULL),(11,'501bdce922734e90a5cdf309b7ce2311','10001','U1','你好',1,0,'2026-04-22 20:14:19',NULL),(12,'501bdce922734e90a5cdf309b7ce2311','U1','10001','nihao',1,0,'2026-04-22 20:23:53',NULL),(13,'501bdce922734e90a5cdf309b7ce2311','10001','U1','NIHAO',1,0,'2026-04-22 20:24:23',NULL),(14,'501bdce922734e90a5cdf309b7ce2311','10001','U1','NIHAO',1,0,'2026-04-22 20:26:24',NULL),(15,'501bdce922734e90a5cdf309b7ce2311','10001','U1','？',1,0,'2026-04-22 20:26:34',NULL),(16,'501bdce922734e90a5cdf309b7ce2311','U1','10001','？',1,0,'2026-04-22 20:26:50',NULL),(17,'501bdce922734e90a5cdf309b7ce2311','10001','U1','你好',1,0,'2026-04-22 20:30:48',NULL),(18,'501bdce922734e90a5cdf309b7ce2311','U1','10001','你好',1,0,'2026-04-22 20:34:28',NULL),(19,'501bdce922734e90a5cdf309b7ce2311','U1','10001','你好',1,0,'2026-04-22 20:34:34',NULL),(20,'501bdce922734e90a5cdf309b7ce2311','U1','10001','？',1,0,'2026-04-22 20:34:44',NULL),(21,'501bdce922734e90a5cdf309b7ce2311','10001','U1','有机？',1,0,'2026-04-22 20:35:00',NULL),(22,'501bdce922734e90a5cdf309b7ce2311','10001','U1','不会',1,0,'2026-04-22 20:35:03',NULL),(23,'7e0241dc10a546ecba572df10fd9753a','1','10001','NIHAO',1,0,'2026-04-22 21:54:29',NULL),(24,'7e0241dc10a546ecba572df10fd9753a','10001','1','？',1,0,'2026-04-22 21:58:47',NULL),(25,'7e0241dc10a546ecba572df10fd9753a','10001','1','？',1,0,'2026-04-22 21:58:54',NULL),(26,'7e0241dc10a546ecba572df10fd9753a','10001','1','？，？',1,0,'2026-04-22 22:02:16',NULL),(27,'7e0241dc10a546ecba572df10fd9753a','1','10001','有机？',1,0,'2026-04-22 22:02:29',NULL),(28,'501bdce922734e90a5cdf309b7ce2311','10001','U1','？',1,0,'2026-04-22 22:02:59',NULL),(29,'7e0241dc10a546ecba572df10fd9753a','10001','1','？，？？',1,0,'2026-04-22 22:22:09',NULL),(30,'7e0241dc10a546ecba572df10fd9753a','10001','1','你好',1,0,'2026-04-22 22:22:25',NULL),(31,'7e0241dc10a546ecba572df10fd9753a','10001','1','你好',1,0,'2026-04-22 22:24:32',NULL),(32,'7e0241dc10a546ecba572df10fd9753a','10001','1','你好',1,0,'2026-04-22 22:24:48',NULL);
/*!40000 ALTER TABLE `chat_message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chat_session`
--

DROP TABLE IF EXISTS `chat_session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chat_session` (
  `session_id` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '会话ID',
  `user_id` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '发起者ID',
  `target_id` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '接收者ID',
  `last_message` text COLLATE utf8mb4_unicode_ci COMMENT '最后一条消息',
  `last_message_time` datetime DEFAULT NULL COMMENT '最后消息时间',
  `unread_count` int DEFAULT '0' COMMENT '未读消息数',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`session_id`),
  KEY `idx_user_target` (`user_id`,`target_id`),
  KEY `idx_target_user` (`target_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='聊天会话表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat_session`
--

LOCK TABLES `chat_session` WRITE;
/*!40000 ALTER TABLE `chat_session` DISABLE KEYS */;
INSERT INTO `chat_session` VALUES ('501bdce922734e90a5cdf309b7ce2311','10001','U1','？','2026-04-22 22:02:58',15,'2026-04-22 20:13:20','2026-04-22 22:02:58'),('7e0241dc10a546ecba572df10fd9753a','1','10001','你好','2026-04-22 22:24:48',17,'2026-04-22 20:02:55','2026-04-22 22:24:48'),('cd673cf7ccfc4b63aeefce2a72b510d7','10001','U1',NULL,NULL,0,'2026-04-22 20:13:20','2026-04-22 20:13:20');
/*!40000 ALTER TABLE `chat_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `favorite`
--

DROP TABLE IF EXISTS `favorite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `favorite` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '收藏记录ID，主键，自增',
  `user_id` varchar(50) NOT NULL COMMENT '用户ID，关联用户表',
  `product_id` int NOT NULL COMMENT '商品ID，关联商品表',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '收藏创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_product` (`user_id`,`product_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='收藏表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `favorite`
--

LOCK TABLES `favorite` WRITE;
/*!40000 ALTER TABLE `favorite` DISABLE KEYS */;
INSERT INTO `favorite` VALUES (1,'U1712345678901',2,'2025-01-15 10:00:00'),(2,'U1712345678901',4,'2025-01-15 11:00:00'),(3,'U1712345678902',1,'2025-01-15 12:00:00'),(4,'U1712345678903',1,'2025-01-16 09:00:00'),(5,'U1712345678903',5,'2025-01-16 09:30:00'),(6,'U1712345678903',12,'2025-01-16 10:00:00'),(7,'U1712345678904',3,'2025-01-16 10:30:00'),(8,'U1712345678904',8,'2025-01-16 11:00:00'),(9,'U1712345678904',15,'2025-01-16 11:30:00'),(10,'U1712345678905',2,'2025-01-16 12:00:00'),(11,'U1712345678905',6,'2025-01-16 12:30:00'),(12,'U1712345678905',13,'2025-01-16 13:00:00'),(13,'U1712345678906',1,'2025-01-16 13:30:00'),(14,'U1712345678906',4,'2025-01-16 14:00:00'),(15,'U1712345678906',7,'2025-01-16 14:30:00'),(16,'U1712345678907',2,'2025-01-16 15:00:00'),(17,'U1712345678907',5,'2025-01-16 15:30:00'),(18,'U1712345678907',12,'2025-01-16 16:00:00'),(19,'U1712345678901',3,'2025-01-16 16:30:00'),(20,'U1712345678901',15,'2025-01-16 17:00:00'),(21,'U1712345678902',5,'2025-01-16 17:30:00'),(22,'U1712345678902',13,'2025-01-16 18:00:00');
/*!40000 ALTER TABLE `favorite` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notification` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '通知ID，主键，自增',
  `user_id` varchar(50) NOT NULL COMMENT '用户ID，关联用户表',
  `product_id` int DEFAULT NULL COMMENT '商品ID，关联商品表（可选，用于补货通知）',
  `product_name` varchar(100) DEFAULT NULL COMMENT '商品名称（冗余字段）',
  `content` varchar(500) NOT NULL COMMENT '通知内容',
  `is_read` int DEFAULT '0' COMMENT '是否已读：0-未读，1-已读',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '通知创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_is_read` (`is_read`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='通知表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification`
--

LOCK TABLES `notification` WRITE;
/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
INSERT INTO `notification` VALUES (1,'U1712345678901',2,'MacBook Pro 14','您收藏的商品【MacBook Pro 14】已经补货啦，快去看看吧！',0,'2025-01-15 16:00:00'),(2,'U1712345678902',1,'iPhone 15 Pro','您收藏的商品【iPhone 15 Pro】已经补货啦，快去看看吧！',1,'2025-01-15 17:00:00'),(3,'U1712345678901',2,'MacBook Pro 14','您收藏的商品【MacBook Pro 14】已经补货啦，快去看看吧！',0,'2025-01-15 16:00:00'),(4,'U1712345678902',1,'iPhone 15 Pro','您收藏的商品【iPhone 15 Pro】已经补货啦，快去看看吧！',1,'2025-01-15 17:00:00'),(5,'U1712345678903',1,'iPhone 15 Pro','您收藏的商品【iPhone 15 Pro】现在有优惠活动，限时9折！',0,'2025-01-16 14:00:00'),(6,'U1712345678904',3,'AirPods Pro 2','您收藏的商品【AirPods Pro 2】库存紧张，仅剩50件！',0,'2025-01-16 14:30:00'),(7,'U1712345678905',2,'MacBook Pro 14','好消息！您收藏的商品【MacBook Pro 14】价格下调500元！',1,'2025-01-16 15:00:00'),(8,'U1712345678906',4,'iPad Air','您关注的商品【iPad Air】已发货，预计2天后到达！',1,'2025-01-16 15:30:00'),(9,'U1712345678901',3,'AirPods Pro 2','您的订单【O202501161400005555】已取消，退款将在3个工作日内到账。',1,'2025-01-16 16:00:00'),(10,'U1712345678902',5,'Apple Watch','您收藏的商品【Apple Watch】新增用户好评128条，快来看看！',0,'2025-01-16 16:30:00'),(11,'U1712345678903',5,'Studio Display','新品上架通知：【Studio Display】现已开售，首发优惠中！',0,'2025-01-16 17:00:00'),(12,'U1712345678907',2,'MacBook Pro 14','系统通知：您的账户余额不足，请及时充值。',0,'2025-01-16 17:30:00'),(13,'U1712345678904',8,'iPad Pro 12.9','您收藏的商品【iPad Pro 12.9】已补货，库存充足！',1,'2025-01-16 18:00:00'),(14,'U1712345678905',15,'AirPods Max','限时促销：【AirPods Max】直降300元，机会难得！',0,'2025-01-16 18:30:00'),(15,'U1712345678906',12,'MacBook Air M2','您浏览过的商品【MacBook Air M2】有用户提问，去看看？',0,'2025-01-16 19:00:00'),(16,'U1712345678901',13,'iPhone 14 Pro','降价提醒：【iPhone 14 Pro】现价6999元，比之前便宜1000元！',0,'2025-01-16 19:30:00');
/*!40000 ALTER TABLE `notification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order`
--

DROP TABLE IF EXISTS `order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '订单ID，主键，自增',
  `order_num` varchar(50) NOT NULL COMMENT '订单编号，唯一标识',
  `user_id` varchar(50) NOT NULL COMMENT '用户ID，关联用户表',
  `total_amount` decimal(10,2) NOT NULL COMMENT '订单总金额',
  `status` int DEFAULT '0' COMMENT '订单状态：0-待支付，1-已支付，2-缺货等待，3-已完成，4-已取消',
  `receiver_name` varchar(50) DEFAULT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(20) DEFAULT NULL COMMENT '收货人电话',
  `receiver_address` varchar(200) DEFAULT NULL COMMENT '收货地址',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '订单创建时间',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `deliver_time` datetime DEFAULT NULL COMMENT '发货时间',
  `finish_time` datetime DEFAULT NULL COMMENT '订单完成时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '订单备注',
  `receipt_image` varchar(500) DEFAULT NULL COMMENT '小票图片链接',
  `original_amount` decimal(10,2) DEFAULT NULL COMMENT '订单原价（打折前的金额）',
  `discount` decimal(3,2) DEFAULT '1.00' COMMENT '折扣率（0.1-1.0，1.0表示无折扣）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_num` (`order_num`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order`
--

LOCK TABLES `order` WRITE;
/*!40000 ALTER TABLE `order` DISABLE KEYS */;
INSERT INTO `order` VALUES (1,'O202501151430251234','U1712345678901',9898.00,3,'张三','13800138001','北京市朝阳区xxx路xxx号','2025-01-15 14:30:25',NULL,NULL,NULL,NULL,NULL,9898.00,1.00),(2,'O202501151530252345','U1712345678902',1899.00,0,'李四','13800138002','上海市浦东新区xxx路xxx号','2025-01-15 15:30:25',NULL,NULL,NULL,NULL,NULL,1899.00,1.00),(3,'O202501161000001111','U1712345678903',14999.00,3,'王五','13800138006','广州市天河区体育西路123号','2025-01-16 10:00:00','2025-01-16 10:05:00','2025-01-16 14:00:00','2025-01-18 16:00:00','请尽快发货','https://example.com/receipt1.jpg',14999.00,1.00),(4,'O202501161100002222','U1712345678904',6799.00,1,'赵六','13800138007','深圳市南山区科技园路456号','2025-01-16 11:00:00','2025-01-16 11:05:00',NULL,NULL,NULL,NULL,6799.00,1.00),(5,'O202501161200003333','U1712345678905',2697.00,0,'孙七','13800138008','杭州市西湖区文三路789号','2025-01-16 12:00:00',NULL,NULL,NULL,'需要发票',NULL,2697.00,1.00),(6,'O202501161300004444','U1712345678906',11499.00,2,'周八','13800138009','成都市武侯区人民南路321号','2025-01-16 13:00:00','2025-01-16 13:05:00',NULL,NULL,'缺货等待中',NULL,11499.00,1.00),(7,'O202501161400005555','U1712345678901',2999.00,4,'张三','13800138001','北京市朝阳区建国路654号','2025-01-16 14:00:00',NULL,NULL,NULL,'用户取消订单',NULL,2999.00,1.00),(8,'O202501161500006666','U1712345678902',1299.00,3,'李四','13800138002','上海市浦东新区世纪大道987号','2025-01-16 15:00:00','2025-01-16 15:05:00','2025-01-16 18:00:00','2025-01-17 10:00:00',NULL,'https://example.com/receipt2.jpg',1299.00,1.00),(9,'O202501161600007777','U1712345678903',749.00,1,'王五','13800138006','广州市天河区体育西路123号','2025-01-16 16:00:00','2025-01-16 16:05:00',NULL,NULL,NULL,NULL,749.00,1.00),(10,'O202501161700008888','U1712345678904',15998.00,3,'赵六','13800138007','深圳市南山区科技园路456号','2025-01-16 17:00:00','2025-01-16 17:05:00','2025-01-17 09:00:00','2025-01-19 14:00:00','包装精美些','https://example.com/receipt3.jpg',15998.00,1.00),(11,'O202501161800009999','U1712345678907',8999.00,1,'吴九','13800138010','南京市鼓楼区中山路147号','2025-01-16 18:00:00','2025-01-16 18:05:00',NULL,NULL,NULL,NULL,8999.00,1.00),(12,'O202501161900001010','U1712345678905',4399.00,3,'孙七','13800138008','杭州市西湖区文三路789号','2025-01-16 19:00:00','2025-01-16 19:05:00','2025-01-17 10:00:00','2025-01-18 15:00:00',NULL,'https://example.com/receipt4.jpg',4399.00,1.00),(13,'O202501162000001111','U1712345678906',1798.00,0,'周八','13800138009','成都市武侯区人民南路321号','2025-01-16 20:00:00',NULL,NULL,NULL,NULL,NULL,1798.00,1.00),(14,'O202501162100001212','U1712345678901',12998.00,3,'张三','13800138001','北京市朝阳区建国路654号','2025-01-16 21:00:00','2025-01-16 21:05:00','2025-01-17 09:00:00','2025-01-19 11:00:00','加急配送','https://example.com/receipt5.jpg',12998.00,1.00),(15,'O202501170800001313','U1712345678908',4999.00,3,'郑十','13800138017','武汉市江汉区解放大道123号','2025-01-17 08:00:00','2025-01-17 08:05:00','2025-01-17 14:00:00','2025-01-19 10:00:00',NULL,'https://example.com/receipt6.jpg',4999.00,1.00),(16,'O202501170900001414','U1712345678909',6999.00,1,'钱十一','13800138018','长沙市岳麓区枫林路456号','2025-01-17 09:00:00','2025-01-17 09:05:00',NULL,NULL,NULL,NULL,6999.00,1.00),(17,'O202501171000001515','U1712345678910',2499.00,0,'冯十二','13800138019','西安市雁塔区长安南路789号','2025-01-17 10:00:00',NULL,NULL,NULL,'需要礼品包装',NULL,2499.00,1.00),(18,'O202501171100001616','U1712345678911',9999.00,3,'陈十三','13800138020','郑州市金水区花园路321号','2025-01-17 11:00:00','2025-01-17 11:05:00','2025-01-17 16:00:00','2025-01-20 09:00:00',NULL,'https://example.com/receipt7.jpg',9999.00,1.00),(19,'O202501171200001717','U1712345678912',1199.00,4,'褚十四','13800138021','济南市历下区泉城路654号','2025-01-17 12:00:00',NULL,NULL,NULL,'地址错误，取消订单',NULL,1199.00,1.00),(20,'O202501171300001818','U1712345678913',5999.00,3,'卫十五','13800138022','青岛市市南区香港中路987号','2025-01-17 13:00:00','2025-01-17 13:05:00','2025-01-18 09:00:00','2025-01-20 14:00:00',NULL,'https://example.com/receipt8.jpg',5999.00,1.00),(21,'O202501171400001919','U1712345678914',3299.00,1,'蒋十六','13800138023','大连市中山区人民路147号','2025-01-17 14:00:00','2025-01-17 14:05:00',NULL,NULL,NULL,NULL,3299.00,1.00),(22,'O202501171500002020','U1712345678915',7999.00,2,'沈十七','13800138024','厦门市思明区湖滨南路258号','2025-01-17 15:00:00','2025-01-17 15:05:00',NULL,NULL,'等待补货',NULL,7999.00,1.00),(23,'O202501171600002121','U1712345678916',1699.00,3,'韩十八','13800138025','福州市鼓楼区五四路369号','2025-01-17 16:00:00','2025-01-17 16:05:00','2025-01-18 10:00:00','2025-01-19 15:00:00',NULL,'https://example.com/receipt9.jpg',1699.00,1.00),(24,'O202501171700002222','U1712345678917',4788.00,0,'杨十九','13800138026','合肥市蜀山区长江中路741号','2025-01-17 17:00:00',NULL,NULL,NULL,NULL,NULL,4788.00,1.00),(25,'O202501171800002323','U1712345678918',2399.00,3,'朱二十','13800138027','南昌市东湖区八一大道852号','2025-01-17 18:00:00','2025-01-17 18:05:00','2025-01-18 11:00:00','2025-01-20 10:00:00',NULL,'https://example.com/receipt10.jpg',2399.00,1.00),(26,'O202501171900002424','U1712345678919',12999.00,1,'秦二一','13800138028','太原市迎泽区并州路963号','2025-01-17 19:00:00','2025-01-17 19:05:00',NULL,NULL,'发票已开',NULL,12999.00,1.00),(27,'O202501172000002525','U1712345678920',3498.00,3,'尤二二','13800138029','石家庄市桥西区中山西路174号','2025-01-17 20:00:00','2025-01-17 20:05:00','2025-01-18 12:00:00','2025-01-21 09:00:00',NULL,'https://example.com/receipt11.jpg',3498.00,1.00),(28,'O202501172100002626','U1712345678921',1099.00,4,'许二三','13800138030','呼和浩特市赛罕区大学东街285号','2025-01-17 21:00:00',NULL,NULL,NULL,'不想要了',NULL,1099.00,1.00),(29,'O202501172200002727','U1712345678922',5499.00,3,'何二四','13800138031','兰州市城关区东岗西路396号','2025-01-17 22:00:00','2025-01-17 22:05:00','2025-01-18 13:00:00','2025-01-20 16:00:00',NULL,'https://example.com/receipt12.jpg',5499.00,1.00),(30,'O202501172300002828','U1712345678923',2299.00,0,'吕二五','13800138032','银川市兴庆区解放西街407号','2025-01-17 23:00:00',NULL,NULL,NULL,NULL,NULL,2299.00,1.00),(31,'O202501180000002929','U1712345678924',3999.00,1,'施二六','13800138033','西宁市城中区南大街518号','2025-01-18 00:00:00','2025-01-18 00:05:00',NULL,NULL,NULL,NULL,3999.00,1.00),(32,'O202501180100003030','U1712345678925',11999.00,3,'张二七','13800138034','乌鲁木齐市天山区光明路629号','2025-01-18 01:00:00','2025-01-18 01:05:00','2025-01-18 14:00:00','2025-01-22 10:00:00',NULL,'https://example.com/receipt13.jpg',11999.00,1.00),(33,'O202501180200003131','U1712345678926',799.00,3,'孔二八','13800138035','拉萨市城关区北京中路730号','2025-01-18 02:00:00','2025-01-18 02:05:00','2025-01-18 15:00:00','2025-01-19 11:00:00',NULL,'https://example.com/receipt14.jpg',799.00,1.00),(34,'O202501180300003232','U1712345678927',4299.00,2,'曹二九','13800138036','海口市美兰区海府路841号','2025-01-18 03:00:00','2025-01-18 03:05:00',NULL,NULL,'缺货中',NULL,4299.00,1.00),(35,'O202501180400003333','U1712345678928',3599.00,3,'严三十','13800138037','三亚市吉阳区迎宾路952号','2025-01-18 04:00:00','2025-01-18 04:05:00','2025-01-18 16:00:00','2025-01-20 14:00:00',NULL,'https://example.com/receipt15.jpg',3599.00,1.00),(36,'O202501180500003434','U1712345678929',199.00,1,'华三一','13800138038','南宁市青秀区民族大道163号','2025-01-18 05:00:00','2025-01-18 05:05:00',NULL,NULL,NULL,NULL,199.00,1.00),(37,'O202501180600003535','U1712345678930',4699.00,3,'金三二','13800138039','贵阳市云岩区中华北路274号','2025-01-18 06:00:00','2025-01-18 06:05:00','2025-01-18 17:00:00','2025-01-21 09:00:00',NULL,'https://example.com/receipt16.jpg',4699.00,1.00),(38,'O202501180700003636','U1712345678931',1299.00,0,'魏三三','13800138040','昆明市盘龙区北京路385号','2025-01-18 07:00:00',NULL,NULL,NULL,'再考虑一下',NULL,1299.00,1.00),(39,'O202501180800003737','U1712345678932',3999.00,3,'陶三四','13800138041','桂林市秀峰区解放东路496号','2025-01-18 08:00:00','2025-01-18 08:05:00','2025-01-18 18:00:00','2025-01-20 15:00:00',NULL,'https://example.com/receipt17.jpg',3999.00,1.00),(40,'O202501180900003838','U1712345678933',5999.00,1,'姜三五','13800138042','柳州市城中区文昌路507号','2025-01-18 09:00:00','2025-01-18 09:05:00',NULL,NULL,NULL,NULL,5999.00,1.00),(41,'O202501181000003939','U1712345678934',249.00,3,'戚三六','13800138043','遵义市红花岗区海尔大道618号','2025-01-18 10:00:00','2025-01-18 10:05:00','2025-01-18 19:00:00','2025-01-19 16:00:00',NULL,'https://example.com/receipt18.jpg',249.00,1.00),(42,'O202501181100004040','U1712345678935',10999.00,3,'谢三七','13800138044','绵阳市涪城区临园大道729号','2025-01-18 11:00:00','2025-01-18 11:05:00','2025-01-19 09:00:00','2025-01-22 10:00:00',NULL,'https://example.com/receipt19.jpg',10999.00,1.00),(43,'O202501181200004141','U1712345678936',1499.00,4,'邹三八','13800138045','德阳市旌阳区泰山北路830号','2025-01-18 12:00:00',NULL,NULL,NULL,'拍错了',NULL,1499.00,1.00),(44,'O202501181300004242','U1712345678937',7999.00,1,'柏三九','13800138046','宜宾市翠屏区酒都大道941号','2025-01-18 13:00:00','2025-01-18 13:05:00',NULL,NULL,NULL,NULL,7999.00,1.00);
/*!40000 ALTER TABLE `order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_item`
--

DROP TABLE IF EXISTS `order_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_item` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '订单项ID，主键，自增',
  `order_id` int NOT NULL COMMENT '订单ID，关联订单表',
  `product_id` int NOT NULL COMMENT '商品ID，关联商品表',
  `product_name` varchar(100) NOT NULL COMMENT '商品名称（快照）',
  `product_price` decimal(10,2) NOT NULL COMMENT '商品单价（快照）',
  `quantity` int NOT NULL COMMENT '购买数量',
  `product_image` varchar(500) DEFAULT NULL COMMENT '商品图片URL（快照）',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单项表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_item`
--

LOCK TABLES `order_item` WRITE;
/*!40000 ALTER TABLE `order_item` DISABLE KEYS */;
INSERT INTO `order_item` VALUES (1,1,1,'iPhone 15 Pro',7999.00,1,'https://example.com/iphone15.jpg'),(2,1,3,'AirPods Pro 2',1899.00,1,'https://example.com/airpods.jpg'),(3,2,3,'AirPods Pro 2',1899.00,1,'https://example.com/airpods.jpg'),(4,3,2,'MacBook Pro 14',14999.00,1,'https://example.com/macbook.jpg'),(5,4,8,'iPad Pro 12.9',8999.00,1,'https://example.com/ipadpro129.jpg'),(6,5,1,'iPhone 15 Pro',7999.00,1,'https://example.com/iphone15.jpg'),(7,5,11,'Apple Magic Keyboard',1299.00,1,'https://example.com/keyboard.jpg'),(8,5,12,'Apple Magic Mouse',699.00,1,'https://example.com/mouse.jpg'),(9,6,5,'Studio Display',11499.00,1,'https://example.com/display.jpg'),(10,7,5,'Apple Watch',2999.00,1,'https://example.com/watch.jpg'),(11,8,11,'Apple Magic Keyboard',1299.00,2,'https://example.com/keyboard.jpg'),(12,8,3,'HomePod mini',749.00,2,'https://example.com/homepod.jpg'),(13,9,12,'MacBook Air M2',8999.00,1,'https://example.com/macbookair.jpg'),(14,10,15,'AirPods Max',4399.00,1,'https://example.com/airpodsmax.jpg'),(15,11,12,'Apple Magic Mouse',699.00,2,'https://example.com/mouse.jpg'),(16,11,14,'MagSafe双项充电器',1099.00,1,'https://example.com/magsafe2.jpg'),(17,12,2,'MacBook Pro 14',14999.00,1,'https://example.com/macbook.jpg'),(18,12,13,'iPhone 14 Pro',6999.00,1,'https://example.com/iphone14pro.jpg');
/*!40000 ALTER TABLE `order_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `password_reset_token`
--

DROP TABLE IF EXISTS `password_reset_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `password_reset_token` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` varchar(50) NOT NULL COMMENT '用户ID',
  `token` varchar(255) NOT NULL COMMENT '重置Token（UUID）',
  `expire_time` datetime NOT NULL COMMENT '过期时间',
  `used` tinyint DEFAULT '0' COMMENT '是否已使用：0-未使用，1-已使用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_token` (`token`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_expire_time` (`expire_time`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='密码重置Token表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `password_reset_token`
--

LOCK TABLES `password_reset_token` WRITE;
/*!40000 ALTER TABLE `password_reset_token` DISABLE KEYS */;
INSERT INTO `password_reset_token` VALUES (5,'U1712345678901','63695ea5a11746aebeaf7f193d90eac3','2026-04-16 23:10:53',0,'2026-04-16 22:40:53'),(8,'U1712345678937','489847063802439da55969dd40e91f08','2026-04-16 23:14:40',0,'2026-04-16 22:44:40');
/*!40000 ALTER TABLE `password_reset_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '商品ID，主键，自增',
  `name` varchar(100) NOT NULL COMMENT '商品名称',
  `description` text COMMENT '商品描述',
  `price` decimal(10,2) NOT NULL COMMENT '商品价格',
  `stock` int DEFAULT '0' COMMENT '商品库存数量',
  `category` varchar(50) DEFAULT NULL COMMENT '商品分类',
  `image_url` varchar(500) DEFAULT NULL COMMENT '商品图片URL',
  `status` int DEFAULT '1' COMMENT '商品状态：0-下架，1-上架',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_category` (`category`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,'iPhone 15 Pro','苹果最新款手机，A17 Pro芯片',7999.00,100,'手机','https://example.com/iphone15.jpg',1,'2026-04-15 20:41:03','2026-04-15 20:41:03'),(2,'MacBook Pro 14','苹果笔记本电脑，M3 Pro芯片',14999.00,50,'电脑','https://example.com/macbook.jpg',1,'2026-04-15 20:41:03','2026-04-15 20:41:03'),(3,'AirPods Pro 2','苹果无线耳机，主动降噪',1899.00,200,'配件','https://example.com/airpods.jpg',1,'2026-04-15 20:41:03','2026-04-15 20:41:03'),(4,'iPad Air','苹果平板电脑，M2芯片',4799.00,80,'平板','https://example.com/ipad.jpg',1,'2026-04-15 20:41:03','2026-04-15 20:41:03'),(5,'Apple Watch','苹果智能手表',2999.00,150,'穿戴','https://example.com/watch.jpg',1,'2026-04-15 20:41:03','2026-04-15 20:41:03'),(6,'Apple Magic Keyboard','苹果无线键盘，带触控ID和数字键盘',1299.00,120,'配件','https://example.com/keyboard.jpg',1,'2025-01-10 09:00:00','2025-01-15 10:00:00'),(7,'Apple Magic Mouse','苹果无线鼠标，多点触控表面',699.00,180,'配件','https://example.com/mouse.jpg',1,'2025-01-10 09:30:00','2025-01-14 11:00:00'),(8,'HomePod mini','苹果智能音箱，卓越音质',749.00,90,'音响','https://example.com/homepod.jpg',1,'2025-01-11 10:00:00','2025-01-15 14:00:00'),(9,'AirTag 4件装','苹果物品追踪器，4件优惠装',799.00,250,'配件','https://example.com/airtag4.jpg',1,'2025-01-11 10:30:00','2025-01-15 15:00:00'),(10,'Studio Display','苹果27英寸5K视网膜显示器',11499.00,30,'显示器','https://example.com/display.jpg',1,'2025-01-12 11:00:00','2025-01-15 16:00:00'),(11,'Mac mini M2','苹果迷你主机，M2芯片，8GB内存',4499.00,60,'电脑','https://example.com/macmini.jpg',1,'2025-01-12 11:30:00','2025-01-15 17:00:00'),(12,'iPhone 14 Pro','苹果手机，A16仿生芯片',6999.00,150,'手机','https://example.com/iphone14pro.jpg',1,'2025-01-13 12:00:00','2025-01-15 18:00:00'),(13,'iPad Pro 12.9','苹果专业平板，M2芯片，12.9英寸',8999.00,45,'平板','https://example.com/ipadpro129.jpg',1,'2025-01-13 12:30:00','2025-01-15 19:00:00'),(14,'Apple TV 4K','苹果电视盒子，A15仿生芯片',1499.00,100,'娱乐','https://example.com/appletv.jpg',0,'2025-01-14 13:00:00','2025-01-15 20:00:00'),(15,'MagSafe双项充电器','苹果磁吸双项无线充电器',1099.00,200,'配件','https://example.com/magsafe2.jpg',1,'2025-01-14 13:30:00','2025-01-15 21:00:00'),(16,'AirPods Max','苹果头戴式耳机，高保真音频',4399.00,70,'配件','https://example.com/airpodsmax.jpg',1,'2025-01-14 14:00:00','2025-01-15 22:00:00'),(17,'MacBook Air M2','苹果轻薄笔记本，M2芯片',8999.00,85,'电脑','https://example.com/macbookair.jpg',1,'2025-01-15 09:00:00','2025-01-15 23:00:00'),(18,'小米14 Pro','小米旗舰手机，骁龙8 Gen3处理器',4999.00,200,'手机','https://example.com/mi14pro.jpg',1,'2025-01-10 09:00:00','2025-01-16 10:00:00'),(19,'华为Mate 60','华为旗舰手机，麒麟9000S芯片',6999.00,150,'手机','https://example.com/mate60.jpg',1,'2025-01-10 10:00:00','2025-01-16 11:00:00'),(20,'OPPO Find X7','OPPO旗舰手机，天玑9300处理器',3999.00,180,'手机','https://example.com/findx7.jpg',1,'2025-01-10 11:00:00','2025-01-16 12:00:00'),(21,'vivo X100','vivo旗舰手机，蔡司光学镜头',4299.00,160,'手机','https://example.com/x100.jpg',1,'2025-01-10 12:00:00','2025-01-16 13:00:00'),(22,'荣耀Magic6','荣耀旗舰手机，鹰眼相机系统',4699.00,140,'手机','https://example.com/magic6.jpg',1,'2025-01-10 13:00:00','2025-01-16 14:00:00'),(23,'联想ThinkPad X1','商务笔记本，英特尔酷睿Ultra',9999.00,80,'电脑','https://example.com/thinkpadx1.jpg',1,'2025-01-10 14:00:00','2025-01-16 15:00:00'),(24,'戴尔XPS 15','高性能笔记本，OLED屏幕',12999.00,60,'电脑','https://example.com/xps15.jpg',1,'2025-01-10 15:00:00','2025-01-16 16:00:00'),(25,'惠普战66','轻薄商务本，长续航设计',5499.00,120,'电脑','https://example.com/zhan66.jpg',1,'2025-01-10 16:00:00','2025-01-16 17:00:00'),(26,'华硕ROG幻16','游戏笔记本，RTX 4070显卡',11999.00,50,'电脑','https://example.com/rog16.jpg',1,'2025-01-10 17:00:00','2025-01-16 18:00:00'),(27,'微软Surface Laptop 5','触控笔记本，PixelSense屏幕',7999.00,90,'电脑','https://example.com/surface5.jpg',1,'2025-01-10 18:00:00','2025-01-16 19:00:00'),(28,'索尼WH-1000XM5','降噪耳机，30小时续航',2499.00,250,'配件','https://example.com/sonyxm5.jpg',1,'2025-01-11 09:00:00','2025-01-16 20:00:00'),(29,'Bose QC45','降噪耳机，舒适佩戴体验',2299.00,220,'配件','https://example.com/boseqc45.jpg',1,'2025-01-11 10:00:00','2025-01-16 21:00:00'),(30,'JBL Charge 5','便携音箱，IP67防水',1199.00,300,'音响','https://example.com/jblcharge5.jpg',1,'2025-01-11 11:00:00','2025-01-16 22:00:00'),(31,'Marshall Stanmore III','蓝牙音箱，经典摇滚音色',3299.00,70,'音响','https://example.com/marshall3.jpg',1,'2025-01-11 12:00:00','2025-01-16 23:00:00'),(32,'三星Galaxy Tab S9','安卓平板，AMOLED屏幕',5999.00,100,'平板','https://example.com/tabs9.jpg',1,'2025-01-11 13:00:00','2025-01-17 00:00:00'),(33,'华为MatePad Pro','鸿蒙平板，无线充电',4999.00,110,'平板','https://example.com/matepadpro.jpg',1,'2025-01-11 14:00:00','2025-01-17 01:00:00'),(34,'小米手环8','智能手环，150种运动模式',249.00,500,'穿戴','https://example.com/band8.jpg',1,'2025-01-11 15:00:00','2025-01-17 02:00:00'),(35,'Garmin Fenix 7','运动手表，GPS定位',5999.00,80,'穿戴','https://example.com/fenix7.jpg',1,'2025-01-11 16:00:00','2025-01-17 03:00:00'),(36,'罗技MX Master 3S','无线鼠标，静音点击',799.00,350,'配件','https://example.com/mxmaster3s.jpg',1,'2025-01-11 17:00:00','2025-01-17 04:00:00'),(37,'雷蛇黑寡妇V4','机械键盘，绿轴',1299.00,180,'配件','https://example.com/blackwidowv4.jpg',1,'2025-01-11 18:00:00','2025-01-17 05:00:00'),(38,'LG UltraWide 34','带鱼屏显示器，21:9比例',3999.00,60,'显示器','https://example.com/lg34.jpg',1,'2025-01-12 09:00:00','2025-01-17 06:00:00'),(39,'AOC Q27G2','电竞显示器，165Hz刷新率',1699.00,150,'显示器','https://example.com/aoc27.jpg',1,'2025-01-12 10:00:00','2025-01-17 07:00:00'),(40,'任天堂Switch OLED','游戏主机，7英寸OLED屏',2399.00,200,'娱乐','https://example.com/switcholed.jpg',1,'2025-01-12 11:00:00','2025-01-17 08:00:00'),(41,'PS5 Slim','游戏主机，4K游戏体验',3599.00,100,'娱乐','https://example.com/ps5slim.jpg',1,'2025-01-12 12:00:00','2025-01-17 09:00:00'),(42,'Xbox Series X','游戏主机，最强性能',3899.00,90,'娱乐','https://example.com/xboxsx.jpg',0,'2025-01-12 13:00:00','2025-01-17 10:00:00'),(43,'大疆Mini 4 Pro','航拍无人机，4K HDR视频',4788.00,120,'数码','https://example.com/mini4pro.jpg',1,'2025-01-12 14:00:00','2025-01-17 11:00:00'),(44,'GoPro Hero 12','运动相机，5.3K视频',3498.00,140,'数码','https://example.com/hero12.jpg',1,'2025-01-12 15:00:00','2025-01-17 12:00:00'),(45,'Kindle Paperwhite','电子书阅读器，6.8英寸',1099.00,280,'数码','https://example.com/kindlepw.jpg',1,'2025-01-12 16:00:00','2025-01-17 13:00:00'),(46,'Anker充电宝','20000mAh快充，双向快充',199.00,600,'配件','https://example.com/anker20k.jpg',1,'2025-01-12 17:00:00','2025-01-17 14:00:00'),(47,'Belkin三合一无线充','苹果三件套充电器',1299.00,160,'配件','https://example.com/belkin3in1.jpg',1,'2025-01-12 18:00:00','2025-01-17 15:00:00');
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `staff`
--

DROP TABLE IF EXISTS `staff`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `staff` (
  `staff_id` varchar(50) NOT NULL COMMENT '员工ID',
  `name` varchar(50) NOT NULL COMMENT '员工姓名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱地址',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号码',
  `avatar` varchar(500) DEFAULT NULL COMMENT '头像地址URL',
  `status` int DEFAULT '1' COMMENT '状态（1-正常，2-冻结，3-离职）',
  `role` int DEFAULT '1' COMMENT '角色类型（1-员工）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`staff_id`),
  UNIQUE KEY `uk_phone` (`phone`),
  UNIQUE KEY `uk_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='员工表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `staff`
--

LOCK TABLES `staff` WRITE;
/*!40000 ALTER TABLE `staff` DISABLE KEYS */;
INSERT INTO `staff` VALUES ('10001','王员工','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','wangstaff@example.com','13800138003','https://shopping-exam.oss-cn-beijing.aliyuncs.com/2026/04/8313bbcc-6d4d-41ca-8b68-e3168bebcffa.png',1,1,'2026-04-15 20:41:03'),('10002','赵员工','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','zhaostaff@example.com','13800138004',NULL,1,1,'2026-04-15 20:41:03'),('10003','刘员工','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','liustaff@example.com','13800138011','https://example.com/staff1.jpg',1,1,'2025-01-08 09:00:00'),('10004','陈员工','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','chenstaff@example.com','13800138012','https://example.com/staff2.jpg',1,1,'2025-01-09 10:00:00'),('10005','杨员工','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','yangstaff@example.com','13800138013',NULL,2,1,'2025-01-05 11:00:00'),('10006','黄员工','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','huangstaff@example.com','13800138014','https://example.com/staff4.jpg',3,1,'2024-12-20 09:30:00'),('10007','林员工1','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','lin1@example.com','13800138047','https://example.com/staff5.jpg',1,1,'2025-01-10 09:00:00'),('10008','林员工2','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','lin2@example.com','13800138048','https://example.com/staff6.jpg',1,1,'2025-01-10 10:00:00'),('10009','林员工3','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','lin3@example.com','13800138049',NULL,1,1,'2025-01-10 11:00:00'),('10010','林员工4','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','lin4@example.com','13800138050','https://example.com/staff8.jpg',1,1,'2025-01-10 12:00:00'),('10011','林员工5','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','lin5@example.com','13800138051','https://example.com/staff9.jpg',1,1,'2025-01-10 13:00:00'),('10012','林员工6','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','lin6@example.com','13800138052',NULL,2,1,'2025-01-10 14:00:00'),('10013','林员工7','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','lin7@example.com','13800138053','https://example.com/staff11.jpg',1,1,'2025-01-10 15:00:00'),('10014','林员工8','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','lin8@example.com','13800138054','https://example.com/staff12.jpg',1,1,'2025-01-10 16:00:00'),('10015','林员工9','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','lin9@example.com','13800138055',NULL,1,1,'2025-01-10 17:00:00'),('10016','林员工10','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','lin10@example.com','13800138056','https://example.com/staff14.jpg',1,1,'2025-01-10 18:00:00'),('10017','林员工11','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','lin11@example.com','13800138057','https://example.com/staff15.jpg',3,1,'2025-01-11 09:00:00'),('10018','林员工12','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','lin12@example.com','13800138058',NULL,1,1,'2025-01-11 10:00:00'),('10019','林员工13','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','lin13@example.com','13800138059','https://example.com/staff17.jpg',1,1,'2025-01-11 11:00:00'),('10020','林员工14','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','lin14@example.com','13800138060','https://example.com/staff18.jpg',1,1,'2025-01-11 12:00:00'),('10021','林员工15','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','lin15@example.com','13800138061',NULL,2,1,'2025-01-11 13:00:00'),('10022','林员工16','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','lin16@example.com','13800138062','https://example.com/staff20.jpg',1,1,'2025-01-11 14:00:00'),('10023','林员工17','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','lin17@example.com','13800138063','https://example.com/staff21.jpg',1,1,'2025-01-11 15:00:00'),('10024','林员工18','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','lin18@example.com','13800138064',NULL,1,1,'2025-01-11 16:00:00'),('10025','林员工19','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','lin19@example.com','13800138065','https://example.com/staff23.jpg',1,1,'2025-01-11 17:00:00'),('10026','林员工20','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','lin20@example.com','13800138066','https://example.com/staff24.jpg',3,1,'2025-01-11 18:00:00'),('10027','林员工21','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','lin21@example.com','13800138067',NULL,1,1,'2025-01-12 09:00:00'),('10028','林员工22','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','lin22@example.com','13800138068','https://example.com/staff26.jpg',1,1,'2025-01-12 10:00:00'),('10029','林员工23','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','lin23@example.com','13800138069','https://example.com/staff27.jpg',1,1,'2025-01-12 11:00:00'),('10030','林员工24','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','lin24@example.com','13800138070',NULL,1,1,'2025-01-12 12:00:00'),('10031','林员工25','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','lin25@example.com','13800138071','https://example.com/staff29.jpg',1,1,'2025-01-12 13:00:00'),('10032','林员工26','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','lin26@example.com','13800138072','https://example.com/staff30.jpg',2,1,'2025-01-12 14:00:00'),('10033','林员工27','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','lin27@example.com','13800138073',NULL,1,1,'2025-01-12 15:00:00'),('10034','林员工28','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','lin28@example.com','13800138074','https://example.com/staff32.jpg',1,1,'2025-01-12 16:00:00'),('10035','林员工29','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','lin29@example.com','13800138075','https://example.com/staff33.jpg',1,1,'2025-01-12 17:00:00'),('10036','林员工30','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a','lin30@example.com','13800138076',NULL,1,1,'2025-01-12 18:00:00');
/*!40000 ALTER TABLE `staff` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `user_id` varchar(50) NOT NULL COMMENT '用户ID（U+时间戳+随机数）',
  `name` varchar(50) DEFAULT NULL COMMENT '用户姓名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `sex` tinyint DEFAULT '0' COMMENT '性别（0-未知，1-男，2-女，3-其他）',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱地址',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号码',
  `avatar` varchar(500) DEFAULT NULL COMMENT '头像地址URL',
  `money` decimal(10,2) DEFAULT '0.00' COMMENT '账户余额',
  `role` int DEFAULT '0' COMMENT '角色类型（0-普通用户）',
  `status` int NOT NULL DEFAULT '0' COMMENT '用户状态（0-正常，1-拉黑）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `uk_phone` (`phone`),
  UNIQUE KEY `uk_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='普通用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('U1','测试用户一号','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a',1,'2639407457@qq.com','13800138001','https://shopping-exam.oss-cn-beijing.aliyuncs.com/2026/04/d5032244-a24f-4e0e-8a35-56b3e53e6d9a.jpeg',1000.00,0,1,'2026-04-15 22:39:52'),('U1712345678902','李四','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a',2,'lisi@example.com','13800138002',NULL,500.00,0,0,'2026-04-15 22:39:52'),('U1712345678903','王五','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a',1,'wangwu@example.com','13800138006','https://example.com/avatar3.jpg',2500.00,0,0,'2025-01-10 09:00:00'),('U1712345678904','赵六','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a',2,'zhaoliu@example.com','13800138007','https://example.com/avatar4.jpg',1800.00,0,0,'2025-01-11 10:30:00'),('U1712345678905','孙七','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a',1,'sunqi@example.com','13800138008',NULL,3200.00,0,0,'2025-01-12 14:20:00'),('U1712345678906','周八','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a',2,'zhouba@example.com','13800138009','https://example.com/avatar6.jpg',950.00,0,0,'2025-01-13 16:45:00'),('U1712345678907','吴九','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a',3,'wujiu@example.com','13800138010','https://example.com/avatar7.jpg',5000.00,0,1,'2025-01-14 08:15:00'),('U1712345678908','郑十','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a',1,'zhengshi@example.com','13800138017','https://example.com/avatar8.jpg',1500.00,0,0,'2025-01-15 09:00:00'),('U1712345678909','钱十一','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a',2,'qianshiyi@example.com','13800138018','https://example.com/avatar9.jpg',2800.00,0,0,'2025-01-15 10:00:00'),('U1712345678910','冯十二','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a',1,'fengshier@example.com','13800138019',NULL,3500.00,0,0,'2025-01-15 11:00:00'),('U1712345678911','陈十三','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a',2,'chenshisan@example.com','13800138020','https://example.com/avatar11.jpg',900.00,0,0,'2025-01-15 12:00:00'),('U1712345678912','褚十四','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a',1,'chushisi@example.com','13800138021','https://example.com/avatar12.jpg',4200.00,0,0,'2025-01-15 13:00:00'),('U1712345678913','卫十五','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a',3,'weishiwu@example.com','13800138022',NULL,1600.00,0,0,'2025-01-15 14:00:00'),('U1712345678914','蒋十六','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a',2,'jiangshiliu@example.com','13800138023','https://example.com/avatar14.jpg',2100.00,0,0,'2025-01-15 15:00:00'),('U1712345678915','沈十七','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a',1,'shenshiqi@example.com','13800138024','https://example.com/avatar15.jpg',5500.00,0,0,'2025-01-15 16:00:00'),('U1712345678916','韩十八','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a',2,'hanshiba@example.com','13800138025',NULL,750.00,0,0,'2025-01-15 17:00:00'),('U1712345678917','杨十九','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a',1,'yangshijiu@example.com','13800138026','https://example.com/avatar17.jpg',3300.00,0,0,'2025-01-15 18:00:00'),('U1712345678918','朱二十','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a',2,'zhuershi@example.com','13800138027','https://example.com/avatar18.jpg',1900.00,0,0,'2025-01-15 19:00:00'),('U1712345678919','秦二一','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a',1,'qineryi@example.com','13800138028',NULL,2700.00,0,0,'2025-01-15 20:00:00'),('U1712345678920','尤二二','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a',3,'youerer@example.com','13800138029','https://example.com/avatar20.jpg',4800.00,0,0,'2025-01-15 21:00:00'),('U1712345678921','许二三','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a',2,'xuersan@example.com','13800138030','https://example.com/avatar21.jpg',1200.00,0,0,'2025-01-16 08:00:00'),('U1712345678922','何二四','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a',1,'heersi@example.com','13800138031',NULL,3600.00,0,0,'2025-01-16 09:00:00'),('U1712345678923','吕二五','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a',2,'lverwu@example.com','13800138032','https://example.com/avatar23.jpg',2200.00,0,0,'2025-01-16 10:00:00'),('U1712345678924','施二六','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a',1,'shierliu@example.com','13800138033','https://example.com/avatar24.jpg',5100.00,0,1,'2025-01-16 11:00:00'),('U1712345678925','张二七','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a',2,'zhangerqi@example.com','13800138034',NULL,850.00,0,0,'2025-01-16 12:00:00'),('U1712345678926','孔二八','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a',1,'kongerbai@example.com','13800138035','https://example.com/avatar26.jpg',3900.00,0,0,'2025-01-16 13:00:00'),('U1712345678927','曹二九','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a',3,'caoerjiu@example.com','13800138036','https://example.com/avatar27.jpg',1400.00,0,0,'2025-01-16 14:00:00'),('U1712345678928','严三十','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a',2,'yansanshi@example.com','13800138037',NULL,2600.00,0,0,'2025-01-16 15:00:00'),('U1712345678929','华三一','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a',1,'huasanyi@example.com','13800138038','https://example.com/avatar29.jpg',4500.00,0,0,'2025-01-16 16:00:00'),('U1712345678930','金三二','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a',2,'jinsaner@example.com','13800138039','https://example.com/avatar30.jpg',1750.00,0,0,'2025-01-16 17:00:00'),('U1712345678931','魏三三','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a',1,'weisansan@example.com','13800138040',NULL,3100.00,0,0,'2025-01-16 18:00:00'),('U1712345678932','陶三四','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a',2,'taosansi@example.com','13800138041','https://example.com/avatar32.jpg',2400.00,0,0,'2025-01-16 19:00:00'),('U1712345678933','姜三五','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a',1,'jiangsanwu@example.com','13800138042','https://example.com/avatar33.jpg',5800.00,0,0,'2025-01-16 20:00:00'),('U1712345678934','戚三六','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a',3,'qisanliu@example.com','13800138043',NULL,980.00,0,0,'2025-01-16 21:00:00'),('U1712345678935','谢三七','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a',2,'xiesanqi@example.com','13800138044','https://example.com/avatar35.jpg',3700.00,0,0,'2025-01-16 22:00:00'),('U1712345678936','邹三八','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a',1,'zousanba@example.com','13800138045','https://example.com/avatar36.jpg',2050.00,0,0,'2025-01-16 23:00:00'),('U1712345678937','凉拌饵丝','$2a$10$ngZgqPq.XUALMbYEqa6nE.SkEVy/mBZ4Cqx0LND75jGuV9fsXVB/a',2,'liangbanersi@qq.com','13800138046',NULL,4100.00,0,1,'2025-01-17 00:00:00');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-04-27 21:06:25
