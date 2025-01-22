
-- WhiteGlove.page definition

CREATE TABLE `page` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `link` varchar(256) NOT NULL,
  `title` varchar(256) DEFAULT NULL,
  `html` text,
  `created_at` int unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `page_link_IDX` (`link`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
