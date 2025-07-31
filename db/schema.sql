-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: localhost    Database: hotel_management_db
-- ------------------------------------------------------
-- Server version	8.0.41

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `hotels`
--

DROP TABLE IF EXISTS `hotels`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hotels` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `phone` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hotels`
--

LOCK TABLES `hotels` WRITE;
/*!40000 ALTER TABLE `hotels` DISABLE KEYS */;
INSERT INTO `hotels` VALUES (1,'2025-07-06 18:08:49.000000','2025-07-28 15:55:28.577104','123 Athens St, Athens','contact@hotelaueb.gr','Hotel Aueb','+302103456787'),(2,'2025-07-06 18:08:49.000000',NULL,'45 Beach Road, Crete','info@hotelsunshine.gr','Hotel Sunshine','+302821234567'),(3,'2025-07-06 18:08:49.000000',NULL,'12 Main St, Thessaloniki','cityinn@hotel.gr','City Inn','+302310987654'),(4,'2025-07-06 18:08:49.000000',NULL,'89 Forest Avenue, Ioannina','info@mountainescape.gr','Mountain Escape','+302651234567'),(5,'2025-07-06 18:08:49.000000',NULL,'777 Island Bay, Rhodes','reservations@bluelagoon.gr','Blue Lagoon Resort','+302241112233'),(6,'2025-07-22 18:23:37.998456','2025-07-22 18:23:37.998456','1, Lagkadia, Arkadias','','Maniatis Hotel','');
/*!40000 ALTER TABLE `hotels` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservation_statuses`
--

DROP TABLE IF EXISTS `reservation_statuses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservation_statuses` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKek2rk8o9mpb1u6s9uxpy8fud3` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservation_statuses`
--

LOCK TABLES `reservation_statuses` WRITE;
/*!40000 ALTER TABLE `reservation_statuses` DISABLE KEYS */;
INSERT INTO `reservation_statuses` VALUES (3,'CANCELLED'),(4,'COMPLETED'),(2,'CONFIRMED'),(1,'PENDING');
/*!40000 ALTER TABLE `reservation_statuses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservations`
--

DROP TABLE IF EXISTS `reservations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservations` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `check_in_date` datetime(6) NOT NULL,
  `check_out_date` datetime(6) NOT NULL,
  `room_id` bigint NOT NULL,
  `reservation_status_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKcjchk2dqtyq2bwfdy3fm8u3pu` (`room_id`),
  KEY `FK9797oco8nqtlnv7bovwjmyf4r` (`reservation_status_id`),
  KEY `FKbqc054ubmoqj00bl3mey759qx` (`user_id`),
  CONSTRAINT `FK9797oco8nqtlnv7bovwjmyf4r` FOREIGN KEY (`reservation_status_id`) REFERENCES `reservation_statuses` (`id`),
  CONSTRAINT `FKbqc054ubmoqj00bl3mey759qx` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKcjchk2dqtyq2bwfdy3fm8u3pu` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservations`
--

LOCK TABLES `reservations` WRITE;
/*!40000 ALTER TABLE `reservations` DISABLE KEYS */;
INSERT INTO `reservations` VALUES (1,'2025-07-15 22:44:59.109354','2025-07-30 20:50:22.170809','2025-08-01 04:00:00.000000','2025-08-03 17:00:00.000000',10,2,4),(4,'2025-07-28 22:04:35.207431','2025-07-30 20:07:23.261534','2025-07-31 15:00:00.000000','2025-08-04 15:00:00.000000',15,1,4),(5,'2025-07-28 22:34:23.725892','2025-07-30 20:57:36.247557','2025-07-31 05:30:00.000000','2025-08-01 02:00:00.000000',3,3,5),(8,'2025-07-30 15:03:08.174003','2025-07-30 20:24:24.526711','2025-08-12 05:00:00.000000','2025-08-16 04:00:00.000000',5,1,7),(9,'2025-07-30 16:09:19.549249','2025-07-30 18:00:36.395849','2025-10-01 05:00:00.000000','2025-10-03 05:00:00.000000',7,4,4),(11,'2025-07-30 20:58:50.380525','2025-07-30 20:59:00.370253','2025-09-19 02:00:00.000000','2025-09-21 02:00:00.000000',8,1,7);
/*!40000 ALTER TABLE `reservations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKkjfxuseyl03cjt89s39o9a3mo` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'ADMIN'),(2,'EMPLOYEE'),(4,'GUEST');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room_statuses`
--

DROP TABLE IF EXISTS `room_statuses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room_statuses` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKd840wpwyxqj149yjs88653kgu` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room_statuses`
--

LOCK TABLES `room_statuses` WRITE;
/*!40000 ALTER TABLE `room_statuses` DISABLE KEYS */;
INSERT INTO `room_statuses` VALUES (1,'AVAILABLE'),(4,'CLEANING'),(5,'MAINTENANCE'),(2,'OCCUPIED'),(3,'RESERVED');
/*!40000 ALTER TABLE `room_statuses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room_types`
--

DROP TABLE IF EXISTS `room_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room_types` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKpekfetw93tpemlf9nmrkfhq69` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room_types`
--

LOCK TABLES `room_types` WRITE;
/*!40000 ALTER TABLE `room_types` DISABLE KEYS */;
INSERT INTO `room_types` VALUES (2,'DOUBLE'),(4,'FAMILY'),(1,'SINGLE'),(3,'SUITE');
/*!40000 ALTER TABLE `room_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rooms`
--

DROP TABLE IF EXISTS `rooms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rooms` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `price_per_night` double NOT NULL,
  `room_number` varchar(255) NOT NULL,
  `hotel_id` bigint NOT NULL,
  `room_status_id` bigint NOT NULL,
  `room_type_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8qpw5a8v9svmmscn7svto8rft` (`hotel_id`),
  KEY `FKifa5rqfn6l3bcuacaoq8hu6pv` (`room_status_id`),
  KEY `FKfrnj07geijggu5s8uj1xqkhmd` (`room_type_id`),
  CONSTRAINT `FK8qpw5a8v9svmmscn7svto8rft` FOREIGN KEY (`hotel_id`) REFERENCES `hotels` (`id`),
  CONSTRAINT `FKfrnj07geijggu5s8uj1xqkhmd` FOREIGN KEY (`room_type_id`) REFERENCES `room_types` (`id`),
  CONSTRAINT `FKifa5rqfn6l3bcuacaoq8hu6pv` FOREIGN KEY (`room_status_id`) REFERENCES `room_statuses` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rooms`
--

LOCK TABLES `rooms` WRITE;
/*!40000 ALTER TABLE `rooms` DISABLE KEYS */;
INSERT INTO `rooms` VALUES (1,'2025-07-06 18:24:54.000000','2025-07-27 00:56:51.995406',130,'101',1,1,1),(2,'2025-07-06 18:24:54.000000','2025-07-28 16:07:45.972533',120.5,'201',1,1,2),(3,'2025-07-06 18:24:54.000000',NULL,200,'103',1,1,3),(4,'2025-07-06 18:24:54.000000',NULL,85,'201',2,1,1),(5,'2025-07-06 18:24:54.000000',NULL,110,'202',2,1,2),(6,'2025-07-06 18:24:54.000000',NULL,180,'203',2,1,3),(7,'2025-07-06 18:24:54.000000',NULL,75,'301',3,1,1),(8,'2025-07-06 18:24:54.000000',NULL,105,'302',3,1,2),(9,'2025-07-06 18:24:54.000000',NULL,160,'303',3,1,3),(10,'2025-07-06 18:24:54.000000',NULL,70,'401',4,1,1),(11,'2025-07-06 18:24:54.000000',NULL,100,'402',4,1,2),(12,'2025-07-06 18:24:54.000000',NULL,190,'403',4,1,3),(13,'2025-07-06 18:24:54.000000',NULL,95,'501',5,1,1),(14,'2025-07-06 18:24:54.000000',NULL,130,'502',5,1,2),(15,'2025-07-06 18:24:54.000000',NULL,220,'503',5,1,3),(19,'2025-07-27 02:22:10.219035','2025-07-27 03:13:06.492251',400,'602',6,1,3);
/*!40000 ALTER TABLE `rooms` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `username` varchar(50) NOT NULL,
  `role_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6j5t70rd2eub907qysjvvd76n` (`email`),
  UNIQUE KEY `UKlgkd7iin2rkv9xkrkvdf6do2v` (`username`),
  KEY `FKp5sxbjiwpsgy5kuqrhpqi5tod` (`role_id`),
  CONSTRAINT `FKp5sxbjiwpsgy5kuqrhpqi5tod` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (4,'2025-07-06 19:34:14.000000',NULL,'routsis@aueb.gr','$2a$11$UcVgITbwwOqcL5HrzbLah.hcMh7nKYzYwwNXyJ/4S62aGg.UbRWAG','michroutsis',1),(5,'2025-07-17 23:30:13.638031','2025-07-17 23:36:54.321873','new_claire@example.com','$2a$11$TEhwM.52qdooFbtEJsbhPuS9YQW80l7CCyNAz0HS/bY.k1r9aSXB2','claire',2),(7,'2025-07-21 17:27:51.189889','2025-07-24 17:50:18.162042','vasilis1@example.com','$2a$11$q06DBC7bu4ZRuzPeJptbRu./1jvJFRinzrX4pbS7bbEh64Ce3pez.','vasilis',4),(8,'2025-07-24 17:54:27.933662','2025-07-30 21:00:51.350651','tasos_new@example.com','$2a$11$NNjUVpYPlzEYXlbLUdm10uMQYiZKWu8u1Qfojhqrpxcafmci9TPbW','tasos',2);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-31  0:41:28
