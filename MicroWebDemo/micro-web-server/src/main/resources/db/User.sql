CREATE TABLE `user` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `name` varchar(100) DEFAULT NULL,
    `age` int DEFAULT NULL,
    `email` varchar(100) DEFAULT NULL,
    `isDeleted` int DEFAULT '0',
    `sex` int DEFAULT '0',
    `version` int DEFAULT '0',
    PRIMARY KEY (`id`)
)