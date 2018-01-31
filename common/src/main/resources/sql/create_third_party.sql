CREATE TABLE `third_party` (
  `thrid_party_id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `open_id` VARCHAR(64) NOT NULL COMMENT '第三方唯一标识',
  `type` INT(3) NOT NULL DEFAULT '0' COMMENT '登录类型0微信1.。。。。',
  `tf_id` INT(11) DEFAULT NULL COMMENT '关联ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `sex` INT(3) DEFAULT '1' COMMENT '性别0女1男',
  `city` VARCHAR(64) DEFAULT NULL COMMENT '微信信息：所在城市',
  `country` VARCHAR(128) DEFAULT NULL COMMENT '微信信息：所在国家',
  `province` VARCHAR(128) DEFAULT NULL COMMENT '微信信息：所在省份',
  `nick` VARCHAR(128) DEFAULT NULL COMMENT '微信信息：名字',
  `tf_type` INT(3) DEFAULT '0' COMMENT '用户类型0为客户端用户1为大师端用户',
  PRIMARY KEY (`thrid_party_id`)
) ENGINE=INNODB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8