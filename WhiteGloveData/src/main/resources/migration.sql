
-- WhiteGlove.pages definition

CREATE TABLE `pages` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `link` varchar(256) NOT NULL,
  `title` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `html` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `created_at` bigint unsigned NOT NULL,
  `updated_at` bigint unsigned NOT NULL,
  `scraped_at` bigint unsigned DEFAULT NULL,
  `host` varchar(256) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `page_link_IDX` (`link`) USING BTREE,
  FULLTEXT KEY `pages_html_IDX` (`html`)
) ENGINE=InnoDB AUTO_INCREMENT=76309 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- WhiteGlove.page_headers definition

CREATE TABLE `page_headers` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `page_id` bigint unsigned NOT NULL,
  `type` varchar(100) NOT NULL,
  `value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `created_at` bigint unsigned NOT NULL,
  `updated_at` bigint unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `page_headers_unique_page_id_type` (`page_id`,`type`)
) ENGINE=InnoDB AUTO_INCREMENT=162178 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- WhiteGlove.page_links definition

CREATE TABLE `page_links` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `page_id` bigint unsigned NOT NULL,
  `link` varchar(256) NOT NULL,
  `created_at` bigint unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `page_links_unique_page_id_link` (`page_id`,`link`)
) ENGINE=InnoDB AUTO_INCREMENT=1922994 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- WhiteGlove.domains definition

CREATE TABLE `domains` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `domain` varchar(260) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `robots_txt` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `domains_unique_domain` (`domain`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;