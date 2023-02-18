CREATE TABLE `coindesk` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `create_time` datetime(3) DEFAULT CURRENT_TIMESTAMP(3),
    `update_time` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    `chart_name` varchar(50) DEFAULT NULL,
    `disclaimer` varchar(255) DEFAULT NULL,
    `api_update_time` varchar(20) DEFAULT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `bpi` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `create_time` datetime(3) DEFAULT CURRENT_TIMESTAMP(3),
    `update_time` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    `coindesk_id` bigint NOT NULL,
    `currency_code` varchar(50) DEFAULT NULL,
    `rate` varchar(255) DEFAULT NULL,
    `description` varchar(50) DEFAULT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `currency_name` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `create_time` datetime(3) DEFAULT CURRENT_TIMESTAMP(3),
    `update_time` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    `currency_code` varchar(50) DEFAULT NULL,
    `name` varchar(50) DEFAULT NULL,
    `language` varchar(10) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_currency_code_name` (`currency_code`, `name`)
);

INSERT INTO "currency_name" ("currency_code", "name", "language") VALUES ('USD', '美元','zh_TW');
INSERT INTO "currency_name" ("currency_code", "name", "language") VALUES ('GBP', '英鎊','zh_TW');
INSERT INTO "currency_name" ("currency_code", "name", "language") VALUES ('EUR', '歐元','zh_TW');
INSERT INTO "currency_name" ("currency_code", "name", "language") VALUES ('USD', 'United States Dollar','en_US');
INSERT INTO "currency_name" ("currency_code", "name", "language") VALUES ('GBP', 'British Pound Sterling','en_US');
INSERT INTO "currency_name" ("currency_code", "name", "language") VALUES ('EUR', 'Euro','en_US');