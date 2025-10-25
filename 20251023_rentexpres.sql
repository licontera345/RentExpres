CREATE DATABASE  IF NOT EXISTS `rentexpres` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `rentexpres`;
-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: localhost    Database: rentexpres
-- ------------------------------------------------------
-- Server version	8.0.43

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
-- Table structure for table `address`
--

DROP TABLE IF EXISTS `address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `address` (
  `address_id` int NOT NULL AUTO_INCREMENT,
  `city_id` int NOT NULL,
  `street` varchar(255) NOT NULL,
  `number` varchar(10) NOT NULL,
  PRIMARY KEY (`address_id`),
  KEY `idx_address_city` (`city_id`),
  CONSTRAINT `fk_address_city` FOREIGN KEY (`city_id`) REFERENCES `city` (`city_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `address`
--

LOCK TABLES `address` WRITE;
/*!40000 ALTER TABLE `address` DISABLE KEYS */;
INSERT INTO `address` VALUES (1,1,'prueba','1');
/*!40000 ALTER TABLE `address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `city`
--

DROP TABLE IF EXISTS `city`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `city` (
  `city_id` int NOT NULL AUTO_INCREMENT,
  `city_name` varchar(255) NOT NULL,
  `province_id` int NOT NULL,
  PRIMARY KEY (`city_id`),
  KEY `idx_city_province` (`province_id`),
  CONSTRAINT `fk_city_province` FOREIGN KEY (`province_id`) REFERENCES `province` (`province_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `city`
--

LOCK TABLES `city` WRITE;
/*!40000 ALTER TABLE `city` DISABLE KEYS */;
INSERT INTO `city` VALUES (1,'prueba',1);
/*!40000 ALTER TABLE `city` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee` (
  `employee_id` int NOT NULL AUTO_INCREMENT,
  `employee_name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role_id` int NOT NULL,
  `headquarters_id` int NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name1` varchar(255) NOT NULL,
  `last_name2` varchar(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `phone` varchar(50) NOT NULL,
  `active_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1 = active, 0 = inactive',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`employee_id`),
  UNIQUE KEY `uq_employee_email` (`email`),
  UNIQUE KEY `uq_employee_name` (`employee_name`),
  KEY `idx_employee_role` (`role_id`),
  KEY `idx_employee_headquarters` (`headquarters_id`),
  CONSTRAINT `fk_employee_headquarters` FOREIGN KEY (`headquarters_id`) REFERENCES `headquarters` (`headquarters_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_employee_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
INSERT INTO `employee` VALUES (1,'u','1',3,3,'u','u','u','u@rentexpres.com','620337444',1,'2025-10-20 18:53:00',NULL),(3,'emp.rental','Q071eWGViCGXLKRFtsAxt+o4WW2ZbHZFn0QZjU8Lkzrkjw97eumwD5J3LaYdEdMk',2,3,'Laura','Gómez',NULL,'laura.gomez@rentexpres.com','620333444',1,'2025-10-17 17:04:03',NULL);
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `headquarters`
--

DROP TABLE IF EXISTS `headquarters`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `headquarters` (
  `headquarters_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(80) NOT NULL,
  `phone` varchar(30) NOT NULL,
  `email` varchar(120) NOT NULL,
  `address_id` int NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`headquarters_id`),
  UNIQUE KEY `uq_headquarters_email` (`email`),
  KEY `idx_headquarters_address` (`address_id`),
  CONSTRAINT `fk_headquarters_address` FOREIGN KEY (`address_id`) REFERENCES `address` (`address_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `headquarters`
--

LOCK TABLES `headquarters` WRITE;
/*!40000 ALTER TABLE `headquarters` DISABLE KEYS */;
INSERT INTO `headquarters` VALUES (3,'Oficina Central','910000111','centralHQ@rentexpres.com',1,'2025-10-17 17:04:02',NULL);
/*!40000 ALTER TABLE `headquarters` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `language`
--

DROP TABLE IF EXISTS `language`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `language` (
  `language_id` int NOT NULL AUTO_INCREMENT,
  `iso_code` varchar(8) NOT NULL,
  `name` varchar(60) NOT NULL,
  PRIMARY KEY (`language_id`),
  UNIQUE KEY `uk_language_iso_code` (`iso_code`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `language`
--

LOCK TABLES `language` WRITE;
/*!40000 ALTER TABLE `language` DISABLE KEYS */;
INSERT INTO `language` VALUES (1,'en','English'),(2,'es','Español'),(3,'fr','Français');
/*!40000 ALTER TABLE `language` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `province`
--

DROP TABLE IF EXISTS `province`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `province` (
  `province_id` int NOT NULL AUTO_INCREMENT,
  `province_name` varchar(255) NOT NULL,
  PRIMARY KEY (`province_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `province`
--

LOCK TABLES `province` WRITE;
/*!40000 ALTER TABLE `province` DISABLE KEYS */;
INSERT INTO `province` VALUES (1,'prueba');
/*!40000 ALTER TABLE `province` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rental`
--

DROP TABLE IF EXISTS `rental`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rental` (
  `rental_id` int NOT NULL AUTO_INCREMENT,
  `start_date_effective` datetime NOT NULL,
  `end_date_effective` datetime NOT NULL,
  `initial_km` int NOT NULL,
  `final_km` int NOT NULL,
  `rental_status_id` int NOT NULL,
  `total_cost` decimal(10,2) NOT NULL,
  `reservation_id` int NOT NULL,
  `pickup_headquarters_id` int NOT NULL,
  `return_headquarters_id` int NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`rental_id`),
  KEY `idx_rental_status` (`rental_status_id`),
  KEY `idx_rental_reservation` (`reservation_id`),
  KEY `fk_rental_pickup_headquarters` (`pickup_headquarters_id`),
  KEY `fk_rental_return_headquarters` (`return_headquarters_id`),
  CONSTRAINT `fk_rental_pickup_headquarters` FOREIGN KEY (`pickup_headquarters_id`) REFERENCES `headquarters` (`headquarters_id`),
  CONSTRAINT `fk_rental_reservation` FOREIGN KEY (`reservation_id`) REFERENCES `reservation` (`reservation_id`),
  CONSTRAINT `fk_rental_return_headquarters` FOREIGN KEY (`return_headquarters_id`) REFERENCES `headquarters` (`headquarters_id`),
  CONSTRAINT `fk_rental_status` FOREIGN KEY (`rental_status_id`) REFERENCES `rental_status` (`rental_status_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rental`
--

LOCK TABLES `rental` WRITE;
/*!40000 ALTER TABLE `rental` DISABLE KEYS */;
/*!40000 ALTER TABLE `rental` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rental_status`
--

DROP TABLE IF EXISTS `rental_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rental_status` (
  `rental_status_id` int NOT NULL,
  `status_name` varchar(50) NOT NULL,
  PRIMARY KEY (`rental_status_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rental_status`
--

LOCK TABLES `rental_status` WRITE;
/*!40000 ALTER TABLE `rental_status` DISABLE KEYS */;
INSERT INTO `rental_status` VALUES (1,'In Progress'),(2,'Completed'),(3,'Canceled');
/*!40000 ALTER TABLE `rental_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rental_status_language`
--

DROP TABLE IF EXISTS `rental_status_language`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rental_status_language` (
  `rental_status_id` int NOT NULL,
  `language_id` int NOT NULL,
  `translated_name` varchar(80) NOT NULL,
  PRIMARY KEY (`rental_status_id`,`language_id`),
  KEY `fk_rental_status_language_lang` (`language_id`),
  CONSTRAINT `fk_rental_status_language_lang` FOREIGN KEY (`language_id`) REFERENCES `language` (`language_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_rental_status_language_status` FOREIGN KEY (`rental_status_id`) REFERENCES `rental_status` (`rental_status_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rental_status_language`
--

LOCK TABLES `rental_status_language` WRITE;
/*!40000 ALTER TABLE `rental_status_language` DISABLE KEYS */;
INSERT INTO `rental_status_language` VALUES (1,1,'In Progress'),(1,2,'En curso'),(1,3,'En cours'),(2,1,'Completed'),(2,2,'Completado'),(2,3,'Terminé'),(3,1,'Canceled'),(3,2,'Cancelado'),(3,3,'Annulé');
/*!40000 ALTER TABLE `rental_status_language` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservation`
--

DROP TABLE IF EXISTS `reservation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservation` (
  `reservation_id` int NOT NULL AUTO_INCREMENT,
  `vehicle_id` int NOT NULL,
  `user_id` int NOT NULL,
  `start_date` datetime NOT NULL,
  `end_date` datetime NOT NULL,
  `employee_id` int NOT NULL,
  `reservation_status_id` int NOT NULL,
  `pickup_headquarters_id` int NOT NULL,
  `return_headquarters_id` int NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`reservation_id`),
  KEY `idx_reservation_vehicle` (`vehicle_id`),
  KEY `idx_reservation_user` (`user_id`) /*!80000 INVISIBLE */,
  KEY `idx_reservation_employee` (`employee_id`),
  KEY `fk_reservation_status` (`reservation_status_id`),
  KEY `fk_reservation_pickup_headquarters` (`pickup_headquarters_id`),
  KEY `fk_reservation_return_headquarters` (`return_headquarters_id`),
  CONSTRAINT `fk_reservation_employee` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`employee_id`),
  CONSTRAINT `fk_reservation_pickup_headquarters` FOREIGN KEY (`pickup_headquarters_id`) REFERENCES `headquarters` (`headquarters_id`),
  CONSTRAINT `fk_reservation_return_headquarters` FOREIGN KEY (`return_headquarters_id`) REFERENCES `headquarters` (`headquarters_id`),
  CONSTRAINT `fk_reservation_status` FOREIGN KEY (`reservation_status_id`) REFERENCES `reservation_status` (`reservation_status_id`),
  CONSTRAINT `fk_reservation_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
  CONSTRAINT `fk_reservation_vehicle` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicle` (`vehicle_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservation`
--

LOCK TABLES `reservation` WRITE;
/*!40000 ALTER TABLE `reservation` DISABLE KEYS */;
INSERT INTO `reservation` VALUES (2,3,1,'2025-10-15 15:04:03','2025-10-20 15:04:03',3,1,3,3,'2025-10-17 17:04:03',NULL);
/*!40000 ALTER TABLE `reservation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservation_status`
--

DROP TABLE IF EXISTS `reservation_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservation_status` (
  `reservation_status_id` int NOT NULL AUTO_INCREMENT,
  `status_name` varchar(50) NOT NULL,
  PRIMARY KEY (`reservation_status_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservation_status`
--

LOCK TABLES `reservation_status` WRITE;
/*!40000 ALTER TABLE `reservation_status` DISABLE KEYS */;
INSERT INTO `reservation_status` VALUES (1,'Pending'),(2,'Confirmed'),(3,'Canceled');
/*!40000 ALTER TABLE `reservation_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservation_status_language`
--

DROP TABLE IF EXISTS `reservation_status_language`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservation_status_language` (
  `reservation_status_id` int NOT NULL,
  `language_id` int NOT NULL,
  `translated_name` varchar(80) NOT NULL,
  PRIMARY KEY (`reservation_status_id`,`language_id`),
  KEY `fk_reservation_status_language_lang` (`language_id`),
  CONSTRAINT `fk_reservation_status_language_lang` FOREIGN KEY (`language_id`) REFERENCES `language` (`language_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_reservation_status_language_status` FOREIGN KEY (`reservation_status_id`) REFERENCES `reservation_status` (`reservation_status_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservation_status_language`
--

LOCK TABLES `reservation_status_language` WRITE;
/*!40000 ALTER TABLE `reservation_status_language` DISABLE KEYS */;
INSERT INTO `reservation_status_language` VALUES (1,1,'Pending'),(1,2,'Pendiente'),(1,3,'En attente'),(2,1,'Confirmed'),(2,2,'Confirmada'),(2,3,'Confirmée'),(3,1,'Canceled'),(3,2,'Cancelada'),(3,3,'Annulée');
/*!40000 ALTER TABLE `reservation_status_language` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
  `role_id` int NOT NULL,
  `role_name` varchar(50) NOT NULL,
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `uq_role_name` (`role_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'ADMIN'),(3,'CLIENT'),(2,'EMPLOYEE');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name1` varchar(45) NOT NULL,
  `last_name2` varchar(45) DEFAULT NULL,
  `birth_date` date NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role_id` int NOT NULL,
  `phone` varchar(20) NOT NULL,
  `address_id` int NOT NULL,
  `active_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1 = active, 0 = inactive',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `uq_user_email` (`email`),
  UNIQUE KEY `uq_user_username` (`username`),
  KEY `idx_user_address` (`address_id`),
  KEY `idx_user_role` (`role_id`),
  CONSTRAINT `fk_user_address` FOREIGN KEY (`address_id`) REFERENCES `address` (`address_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_user_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'user.test','Juan','López',NULL,'1985-05-01','juan@test.com','abc123.',3,'611111111',1,1,'2025-10-17 17:04:03',NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vehicle`
--

DROP TABLE IF EXISTS `vehicle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vehicle` (
  `vehicle_id` int NOT NULL AUTO_INCREMENT,
  `brand` varchar(255) NOT NULL,
  `model` varchar(255) NOT NULL,
  `manufacture_year` int NOT NULL,
  `daily_price` decimal(10,2) NOT NULL,
  `license_plate` varchar(255) NOT NULL,
  `vin_number` varchar(17) NOT NULL,
  `current_mileage` int NOT NULL DEFAULT '0',
  `vehicle_status_id` int NOT NULL,
  `category_id` int NOT NULL DEFAULT '1',
  `current_headquarters_id` int NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`vehicle_id`),
  UNIQUE KEY `uk_vehicle_license_plate` (`license_plate`),
  UNIQUE KEY `uk_vehicle_vin_number` (`vin_number`),
  KEY `idx_vehicle_status` (`vehicle_status_id`),
  KEY `idx_vehicle_category` (`category_id`),
  KEY `idx_vehicle_headquarters` (`current_headquarters_id`),
  CONSTRAINT `fk_vehicle_category` FOREIGN KEY (`category_id`) REFERENCES `vehicle_category` (`category_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_vehicle_headquarters` FOREIGN KEY (`current_headquarters_id`) REFERENCES `headquarters` (`headquarters_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_vehicle_status` FOREIGN KEY (`vehicle_status_id`) REFERENCES `vehicle_status` (`vehicle_status_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vehicle`
--

LOCK TABLES `vehicle` WRITE;
/*!40000 ALTER TABLE `vehicle` DISABLE KEYS */;
INSERT INTO `vehicle` VALUES (3,'Toyota','Yaris',2020,35.00,'0000AAA','VIN0000AAA0000AAA',45000,1,1,3,'2025-10-17 17:04:03',NULL);
/*!40000 ALTER TABLE `vehicle` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vehicle_category`
--

DROP TABLE IF EXISTS `vehicle_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vehicle_category` (
  `category_id` int NOT NULL,
  `category_name` varchar(50) NOT NULL,
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vehicle_category`
--

LOCK TABLES `vehicle_category` WRITE;
/*!40000 ALTER TABLE `vehicle_category` DISABLE KEYS */;
INSERT INTO `vehicle_category` VALUES (1,'Compact'),(2,'SUV'),(3,'Van');
/*!40000 ALTER TABLE `vehicle_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vehicle_category_language`
--

DROP TABLE IF EXISTS `vehicle_category_language`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vehicle_category_language` (
  `category_id` int NOT NULL,
  `language_id` int NOT NULL,
  `translated_name` varchar(80) NOT NULL,
  PRIMARY KEY (`category_id`,`language_id`),
  KEY `fk_vehiclecategorylang_language` (`language_id`),
  CONSTRAINT `fk_vehiclecategorylang_category` FOREIGN KEY (`category_id`) REFERENCES `vehicle_category` (`category_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_vehiclecategorylang_language` FOREIGN KEY (`language_id`) REFERENCES `language` (`language_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vehicle_category_language`
--

LOCK TABLES `vehicle_category_language` WRITE;
/*!40000 ALTER TABLE `vehicle_category_language` DISABLE KEYS */;
INSERT INTO `vehicle_category_language` VALUES (1,1,'Compact'),(1,2,'Compacto'),(1,3,'Compact'),(2,1,'SUV'),(2,2,'Todoterreno'),(2,3,'SUV'),(3,1,'Van'),(3,2,'Furgoneta'),(3,3,'Fourgonnette');
/*!40000 ALTER TABLE `vehicle_category_language` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vehicle_status`
--

DROP TABLE IF EXISTS `vehicle_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vehicle_status` (
  `vehicle_status_id` int NOT NULL,
  `status_name` varchar(50) NOT NULL,
  PRIMARY KEY (`vehicle_status_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vehicle_status`
--

LOCK TABLES `vehicle_status` WRITE;
/*!40000 ALTER TABLE `vehicle_status` DISABLE KEYS */;
INSERT INTO `vehicle_status` VALUES (1,'Available'),(2,'Maintenance'),(3,'Rented');
/*!40000 ALTER TABLE `vehicle_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vehicle_status_language`
--

DROP TABLE IF EXISTS `vehicle_status_language`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vehicle_status_language` (
  `vehicle_status_id` int NOT NULL,
  `language_id` int NOT NULL,
  `translated_name` varchar(80) NOT NULL,
  PRIMARY KEY (`vehicle_status_id`,`language_id`),
  KEY `fk_vehicle_status_language_lang` (`language_id`),
  CONSTRAINT `fk_vehicle_status_language_lang` FOREIGN KEY (`language_id`) REFERENCES `language` (`language_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_vehicle_status_language_status` FOREIGN KEY (`vehicle_status_id`) REFERENCES `vehicle_status` (`vehicle_status_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vehicle_status_language`
--

LOCK TABLES `vehicle_status_language` WRITE;
/*!40000 ALTER TABLE `vehicle_status_language` DISABLE KEYS */;
INSERT INTO `vehicle_status_language` VALUES (1,1,'Available'),(1,2,'Disponible'),(1,3,'Disponible'),(2,1,'Maintenance'),(2,2,'Mantenimiento'),(2,3,'Maintenance'),(3,1,'Rented'),(3,2,'Alquilado'),(3,3,'Loué');
/*!40000 ALTER TABLE `vehicle_status_language` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-10-23 18:32:08
