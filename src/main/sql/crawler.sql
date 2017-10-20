DROP DATABASE IF EXISTS `crawler_db`;
CREATE DATABASE IF NOT EXISTS `crawler_db`;
USE `crawler_db`;

--
-- Table structure for table `crwlr_vendors`
--
DROP TABLE IF EXISTS `crwlr_vendors`;
CREATE TABLE `crwlr_vendors` (
  `id`           BIGINT AUTO_INCREMENT,
  `name`         VARCHAR(200) NULL,
  `location`     VARCHAR(45)  NULL,
  `positive`     TINYINT      NULL,
  `neutral`      TINYINT      NULL,
  `negative`     TINYINT      NULL,
  `link`         VARCHAR(200) NULL,
  `timeOnLazada` TINYINT      NULL,
  `rating`       DECIMAL      NULL,
  `size`         TINYINT      NULL,
  `shipOnTime`   TINYINT      NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `crwlr_vendors_id` (`id`),
  UNIQUE KEY `crwlr_vendors_name` (`name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

--
-- Table structure for table `crwlr_products`
--
DROP TABLE IF EXISTS `crwlr_products`;
CREATE TABLE `crwlr_products` (
  `id`          BIGINT AUTO_INCREMENT,
  `name`        VARCHAR(200) NOT NULL,
  `category`    VARCHAR(45)  NOT NULL,
  `vendor_name` VARCHAR(45)  NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `crwlr_products` (`id`),
  CONSTRAINT `crwlr_products_vendor_name` FOREIGN KEY (`vendor_name`) REFERENCES `crwlr_vendors` (`name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


