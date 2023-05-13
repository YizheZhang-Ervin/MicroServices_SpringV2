CREATE TABLE `jwt` (
   `id` bigint NOT NULL AUTO_INCREMENT,
   `userId` varchar(20) UNIQUE NOT NULL,
   `encryptedPwd` varchar(200) DEFAULT NULL,
   `token` varchar(500) DEFAULT NULL,
   `refreshToken` varchar(100) DEFAULT NULL,
   `createTime` datetime DEFAULT NULL,
   `tokenExpireTime` datetime DEFAULT NULL,
   `refreshTokenExpireTime` datetime DEFAULT NULL,
   `isDeleted` int DEFAULT '0',
   PRIMARY KEY (`id`)
);