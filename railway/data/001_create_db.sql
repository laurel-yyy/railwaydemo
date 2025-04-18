CREATE DATABASE IF NOT EXISTS railway CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE railway;

CREATE TABLE `t_carriage`
(
    `id`              bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    `train_id`        bigint(20) DEFAULT NULL,
    `carriage_number` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `carriage_type`   int(3) DEFAULT NULL,
    `seat_count`      int(3) DEFAULT NULL,
    `create_time`     datetime DEFAULT NULL,
    `update_time`     datetime DEFAULT NULL,
    `del_flag`        tinyint(1) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY               `idx_train_id` (`train_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=65 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `t_seat`
(
    `id`              bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    `train_id`        bigint(20) DEFAULT NULL,
    `carriage_number` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `seat_number`     varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `seat_type`       int(3) DEFAULT NULL,
    `start_station`   varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `end_station`     varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `price`           int(11) DEFAULT NULL,
    `seat_status`     int(3) DEFAULT NULL,
    `create_time`     datetime DEFAULT NULL,
    `update_time`     datetime DEFAULT NULL,
    `del_flag`        tinyint(1) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY               `idx_train_id` (`train_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1683022080920494081 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `t_station`
(
    `id`          bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    `code`        varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `name`        varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `popular_flag` tinyint(1) DEFAULT NULL,
    `create_time` datetime DEFAULT NULL,
    `update_time` datetime DEFAULT NULL,
    `del_flag`    tinyint(1) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `t_train`
(
    `id`             bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    `train_number`   varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `start_station`  varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `end_station`    varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `sale_time`      datetime DEFAULT NULL,
    `sale_status`    int(3) DEFAULT NULL,
    `departure_time` datetime DEFAULT NULL,
    `arrival_time`   datetime DEFAULT NULL,
    `create_time`    datetime DEFAULT NULL,
    `update_time`    datetime DEFAULT NULL,
    `del_flag`       tinyint(1) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `t_train_station`
(
    `id`             bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    `train_id`       bigint(20) DEFAULT NULL,
    `station_id`     bigint(20) DEFAULT NULL,
    `sequence`       varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `departure`      varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `arrival`        varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `arrival_time`   datetime DEFAULT NULL,
    `departure_time` datetime DEFAULT NULL,
    `stopover_time`  int(3) DEFAULT NULL,
    `create_time`    datetime DEFAULT NULL,
    `update_time`    datetime DEFAULT NULL,
    `del_flag`       tinyint(1) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY              `idx_train_id` (`train_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `t_train_station_price`
(
    `id`          bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    `train_id`    bigint(20) DEFAULT NULL,
    `departure`   varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `arrival`     varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `seat_type`   int(3) DEFAULT NULL,
    `price`       int(11) DEFAULT NULL,
    `create_time` datetime DEFAULT NULL,
    `update_time` datetime DEFAULT NULL,
    `del_flag`    tinyint(1) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY           `idx_train_id` (`train_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1677692017354547201 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `t_train_station_relation`
(
    `id`             bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    `train_id`       bigint(20) DEFAULT NULL,
    `departure`      varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `arrival`        varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `departure_flag` tinyint(1) DEFAULT NULL,
    `arrival_flag`   tinyint(1) DEFAULT NULL,
    `departure_time` datetime DEFAULT NULL,
    `arrival_time`   datetime DEFAULT NULL,
    `create_time`    datetime DEFAULT NULL,
    `update_time`    datetime DEFAULT NULL,
    `del_flag`       tinyint(1) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY              `idx_train_id` (`train_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1677689610742865921 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `t_user_0` (
    `id` bigint(20) unsigned NOT NULL,
    `username` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `password` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `real_name` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_username` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `t_user_1` (
    `id` bigint(20) unsigned NOT NULL,
    `username` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `password` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `real_name` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_username` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `t_user_2` (
    `id` bigint(20) unsigned NOT NULL,
    `username` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `password` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `real_name` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_username` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `t_user_3` (
    `id` bigint(20) unsigned NOT NULL,
    `username` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `password` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `real_name` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_username` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `t_user_4` (
    `id` bigint(20) unsigned NOT NULL,
    `username` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `password` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `real_name` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_username` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `t_user_5` (
    `id` bigint(20) unsigned NOT NULL ,
    `username` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `password` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `real_name` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_username` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `t_user_6` (
    `id` bigint(20) unsigned NOT NULL,
    `username` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `password` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `real_name` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_username` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `t_user_7` (
    `id` bigint(20) unsigned NOT NULL,
    `username` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `password` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `real_name` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_username` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `t_user_8` (
    `id` bigint(20) unsigned NOT NULL,
    `username` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `password` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `real_name` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_username` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `t_user_9` (
    `id` bigint(20) unsigned NOT NULL,
    `username` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `password` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `real_name` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_username` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `t_user_10` (
    `id` bigint(20) unsigned NOT NULL,
    `username` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `password` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `real_name` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_username` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `t_user_11` (
    `id` bigint(20) unsigned NOT NULL,
    `username` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `password` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `real_name` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_username` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `t_user_12` (
    `id` bigint(20) unsigned NOT NULL,
    `username` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `password` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `real_name` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_username` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `t_user_13` (
    `id` bigint(20) unsigned NOT NULL,
    `username` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `password` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `real_name` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_username` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `t_user_14` (
    `id` bigint(20) unsigned NOT NULL,
    `username` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `password` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `real_name` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_username` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `t_user_15` (
    `id` bigint(20) unsigned NOT NULL,
    `username` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `password` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `real_name` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_username` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `t_order_0` (
    `id`             bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    `order_sn`       varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `user_id`        bigint(20) DEFAULT NULL,
    `username`       varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `train_id`       bigint(20) DEFAULT NULL,
    `train_number`   varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `riding_date`    date DEFAULT NULL,
    `departure`      varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `arrival`        varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `departure_time` datetime DEFAULT NULL,
    `arrival_time`   datetime DEFAULT NULL,
    `status`         int(3) DEFAULT NULL,
    `order_time`     datetime DEFAULT NULL,
    `pay_time`       datetime DEFAULT NULL,
    `create_time`    datetime DEFAULT NULL,
    `update_time`    datetime DEFAULT NULL,
    `del_flag`       tinyint(1) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY              `idx_user_id` (`user_id`) USING BTREE,
    KEY              `idx_order_sn` (`order_sn`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `t_order_1` (
    `id`             bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    `order_sn`       varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `user_id`        bigint(20) DEFAULT NULL,
    `username`       varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `train_id`       bigint(20) DEFAULT NULL,
    `train_number`   varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `riding_date`    date DEFAULT NULL,
    `departure`      varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `arrival`        varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `departure_time` datetime DEFAULT NULL,
    `arrival_time`   datetime DEFAULT NULL,
    `status`         int(3) DEFAULT NULL,
    `order_time`     datetime DEFAULT NULL,
    `pay_time`       datetime DEFAULT NULL,
    `create_time`    datetime DEFAULT NULL,
    `update_time`    datetime DEFAULT NULL,
    `del_flag`       tinyint(1) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY              `idx_user_id` (`user_id`) USING BTREE,
    KEY              `idx_order_sn` (`order_sn`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `t_order_2` (
    `id`             bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    `order_sn`       varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `user_id`        bigint(20) DEFAULT NULL,
    `username`       varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `train_id`       bigint(20) DEFAULT NULL,
    `train_number`   varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `riding_date`    date DEFAULT NULL,
    `departure`      varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `arrival`        varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `departure_time` datetime DEFAULT NULL,
    `arrival_time`   datetime DEFAULT NULL,
    `status`         int(3) DEFAULT NULL,
    `order_time`     datetime DEFAULT NULL,
    `pay_time`       datetime DEFAULT NULL,
    `create_time`    datetime DEFAULT NULL,
    `update_time`    datetime DEFAULT NULL,
    `del_flag`       tinyint(1) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY              `idx_user_id` (`user_id`) USING BTREE,
    KEY              `idx_order_sn` (`order_sn`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `t_order_3` (
    `id`             bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    `order_sn`       varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `user_id`        bigint(20) DEFAULT NULL,
    `username`       varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `train_id`       bigint(20) DEFAULT NULL,
    `train_number`   varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `riding_date`    date DEFAULT NULL,
    `departure`      varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `arrival`        varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `departure_time` datetime DEFAULT NULL,
    `arrival_time`   datetime DEFAULT NULL,
    `status`         int(3) DEFAULT NULL,
    `order_time`     datetime DEFAULT NULL,
    `pay_time`       datetime DEFAULT NULL,
    `create_time`    datetime DEFAULT NULL,
    `update_time`    datetime DEFAULT NULL,
    `del_flag`       tinyint(1) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY              `idx_user_id` (`user_id`) USING BTREE,
    KEY              `idx_order_sn` (`order_sn`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `t_order_4` (
    `id`             bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    `order_sn`       varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `user_id`        bigint(20) DEFAULT NULL,
    `username`       varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `train_id`       bigint(20) DEFAULT NULL,
    `train_number`   varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `riding_date`    date DEFAULT NULL,
    `departure`      varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `arrival`        varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `departure_time` datetime DEFAULT NULL,
    `arrival_time`   datetime DEFAULT NULL,
    `status`         int(3) DEFAULT NULL,
    `order_time`     datetime DEFAULT NULL,
    `pay_time`       datetime DEFAULT NULL,
    `create_time`    datetime DEFAULT NULL,
    `update_time`    datetime DEFAULT NULL,
    `del_flag`       tinyint(1) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY              `idx_user_id` (`user_id`) USING BTREE,
    KEY              `idx_order_sn` (`order_sn`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `t_order_5` (
    `id`             bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    `order_sn`       varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `user_id`        bigint(20) DEFAULT NULL,
    `username`       varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `train_id`       bigint(20) DEFAULT NULL,
    `train_number`   varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `riding_date`    date DEFAULT NULL,
    `departure`      varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `arrival`        varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `departure_time` datetime DEFAULT NULL,
    `arrival_time`   datetime DEFAULT NULL,
    `status`         int(3) DEFAULT NULL,
    `order_time`     datetime DEFAULT NULL,
    `pay_time`       datetime DEFAULT NULL,
    `create_time`    datetime DEFAULT NULL,
    `update_time`    datetime DEFAULT NULL,
    `del_flag`       tinyint(1) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY              `idx_user_id` (`user_id`) USING BTREE,
    KEY              `idx_order_sn` (`order_sn`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `t_order_6` (
    `id`             bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    `order_sn`       varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `user_id`        bigint(20) DEFAULT NULL,
    `username`       varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `train_id`       bigint(20) DEFAULT NULL,
    `train_number`   varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `riding_date`    date DEFAULT NULL,
    `departure`      varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `arrival`        varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `departure_time` datetime DEFAULT NULL,
    `arrival_time`   datetime DEFAULT NULL,
    `status`         int(3) DEFAULT NULL,
    `order_time`     datetime DEFAULT NULL,
    `pay_time`       datetime DEFAULT NULL,
    `create_time`    datetime DEFAULT NULL,
    `update_time`    datetime DEFAULT NULL,
    `del_flag`       tinyint(1) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY              `idx_user_id` (`user_id`) USING BTREE,
    KEY              `idx_order_sn` (`order_sn`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `t_order_7` (
    `id`             bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    `order_sn`       varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `user_id`        bigint(20) DEFAULT NULL,
    `username`       varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `train_id`       bigint(20) DEFAULT NULL,
    `train_number`   varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `riding_date`    date DEFAULT NULL,
    `departure`      varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `arrival`        varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `departure_time` datetime DEFAULT NULL,
    `arrival_time`   datetime DEFAULT NULL,
    `status`         int(3) DEFAULT NULL,
    `order_time`     datetime DEFAULT NULL,
    `pay_time`       datetime DEFAULT NULL,
    `create_time`    datetime DEFAULT NULL,
    `update_time`    datetime DEFAULT NULL,
    `del_flag`       tinyint(1) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY              `idx_user_id` (`user_id`) USING BTREE,
    KEY              `idx_order_sn` (`order_sn`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `t_order_8` (
    `id`             bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    `order_sn`       varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `user_id`        bigint(20) DEFAULT NULL,
    `username`       varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `train_id`       bigint(20) DEFAULT NULL,
    `train_number`   varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `riding_date`    date DEFAULT NULL,
    `departure`      varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `arrival`        varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `departure_time` datetime DEFAULT NULL,
    `arrival_time`   datetime DEFAULT NULL,
    `status`         int(3) DEFAULT NULL,
    `order_time`     datetime DEFAULT NULL,
    `pay_time`       datetime DEFAULT NULL,
    `create_time`    datetime DEFAULT NULL,
    `update_time`    datetime DEFAULT NULL,
    `del_flag`       tinyint(1) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY              `idx_user_id` (`user_id`) USING BTREE,
    KEY              `idx_order_sn` (`order_sn`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `t_order_9` (
    `id`             bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    `order_sn`       varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `user_id`        bigint(20) DEFAULT NULL,
    `username`       varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `train_id`       bigint(20) DEFAULT NULL,
    `train_number`   varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `riding_date`    date DEFAULT NULL,
    `departure`      varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `arrival`        varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `departure_time` datetime DEFAULT NULL,
    `arrival_time`   datetime DEFAULT NULL,
    `status`         int(3) DEFAULT NULL,
    `order_time`     datetime DEFAULT NULL,
    `pay_time`       datetime DEFAULT NULL,
    `create_time`    datetime DEFAULT NULL,
    `update_time`    datetime DEFAULT NULL,
    `del_flag`       tinyint(1) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY              `idx_user_id` (`user_id`) USING BTREE,
    KEY              `idx_order_sn` (`order_sn`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `t_order_10` (
    `id`             bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    `order_sn`       varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `user_id`        bigint(20) DEFAULT NULL,
    `username`       varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `train_id`       bigint(20) DEFAULT NULL,
    `train_number`   varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `riding_date`    date DEFAULT NULL,
    `departure`      varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `arrival`        varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `departure_time` datetime DEFAULT NULL,
    `arrival_time`   datetime DEFAULT NULL,
    `status`         int(3) DEFAULT NULL,
    `order_time`     datetime DEFAULT NULL,
    `pay_time`       datetime DEFAULT NULL,
    `create_time`    datetime DEFAULT NULL,
    `update_time`    datetime DEFAULT NULL,
    `del_flag`       tinyint(1) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY              `idx_user_id` (`user_id`) USING BTREE,
    KEY              `idx_order_sn` (`order_sn`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `t_order_11` (
    `id`             bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    `order_sn`       varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `user_id`        bigint(20) DEFAULT NULL,
    `username`       varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `train_id`       bigint(20) DEFAULT NULL,
    `train_number`   varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `riding_date`    date DEFAULT NULL,
    `departure`      varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `arrival`        varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `departure_time` datetime DEFAULT NULL,
    `arrival_time`   datetime DEFAULT NULL,
    `status`         int(3) DEFAULT NULL,
    `order_time`     datetime DEFAULT NULL,
    `pay_time`       datetime DEFAULT NULL,
    `create_time`    datetime DEFAULT NULL,
    `update_time`    datetime DEFAULT NULL,
    `del_flag`       tinyint(1) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY              `idx_user_id` (`user_id`) USING BTREE,
    KEY              `idx_order_sn` (`order_sn`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `t_order_12` (
    `id`             bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    `order_sn`       varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `user_id`        bigint(20) DEFAULT NULL,
    `username`       varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `train_id`       bigint(20) DEFAULT NULL,
    `train_number`   varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `riding_date`    date DEFAULT NULL,
    `departure`      varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `arrival`        varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `departure_time` datetime DEFAULT NULL,
    `arrival_time`   datetime DEFAULT NULL,
    `status`         int(3) DEFAULT NULL,
    `order_time`     datetime DEFAULT NULL,
    `pay_time`       datetime DEFAULT NULL,
    `create_time`    datetime DEFAULT NULL,
    `update_time`    datetime DEFAULT NULL,
    `del_flag`       tinyint(1) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY              `idx_user_id` (`user_id`) USING BTREE,
    KEY              `idx_order_sn` (`order_sn`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `t_order_13` (
    `id`             bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    `order_sn`       varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `user_id`        bigint(20) DEFAULT NULL,
    `username`       varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `train_id`       bigint(20) DEFAULT NULL,
    `train_number`   varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `riding_date`    date DEFAULT NULL,
    `departure`      varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `arrival`        varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `departure_time` datetime DEFAULT NULL,
    `arrival_time`   datetime DEFAULT NULL,
    `status`         int(3) DEFAULT NULL,
    `order_time`     datetime DEFAULT NULL,
    `pay_time`       datetime DEFAULT NULL,
    `create_time`    datetime DEFAULT NULL,
    `update_time`    datetime DEFAULT NULL,
    `del_flag`       tinyint(1) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY              `idx_user_id` (`user_id`) USING BTREE,
    KEY              `idx_order_sn` (`order_sn`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `t_order_14` (
    `id`             bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    `order_sn`       varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `user_id`        bigint(20) DEFAULT NULL,
    `username`       varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `train_id`       bigint(20) DEFAULT NULL,
    `train_number`   varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `riding_date`    date DEFAULT NULL,
    `departure`      varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `arrival`        varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `departure_time` datetime DEFAULT NULL,
    `arrival_time`   datetime DEFAULT NULL,
    `status`         int(3) DEFAULT NULL,
    `order_time`     datetime DEFAULT NULL,
    `pay_time`       datetime DEFAULT NULL,
    `create_time`    datetime DEFAULT NULL,
    `update_time`    datetime DEFAULT NULL,
    `del_flag`       tinyint(1) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY              `idx_user_id` (`user_id`) USING BTREE,
    KEY              `idx_order_sn` (`order_sn`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `t_order_15` (
    `id`             bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    `order_sn`       varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `user_id`        bigint(20) DEFAULT NULL,
    `username`       varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `train_id`       bigint(20) DEFAULT NULL,
    `train_number`   varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `riding_date`    date DEFAULT NULL,
    `departure`      varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `arrival`        varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `departure_time` datetime DEFAULT NULL,
    `arrival_time`   datetime DEFAULT NULL,
    `status`         int(3) DEFAULT NULL,
    `order_time`     datetime DEFAULT NULL,
    `pay_time`       datetime DEFAULT NULL,
    `create_time`    datetime DEFAULT NULL,
    `update_time`    datetime DEFAULT NULL,
    `del_flag`       tinyint(1) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY              `idx_user_id` (`user_id`) USING BTREE,
    KEY              `idx_order_sn` (`order_sn`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;