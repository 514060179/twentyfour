CREATE TABLE `tf_relation` (
  `relation_id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `r_user_id` VARCHAR(500) NOT NULL COMMENT '用户im的id',
  `r_friend_id` VARCHAR(500) NOT NULL COMMENT '目标用户im的id',
  `r_is_validate` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否验证通过',
  `r_group_id` INT(11) NULL DEFAULT 1 COMMENT '好友分组id，1为默认我的好友',
  `r_visible`  INT(3) NOT NULL DEFAULT 0 COMMENT '好友是否可见(朋友圈等)，0为相互可见，1为A对B可见但B对A不可见，2为A对B不可见同时B对A可见，3为相互不可见',
  `r_see` INT(3) NOT NULL DEFAULT 0 COMMENT '是否看(朋友圈),0为相互看，1为A看B但B不看A，2为A不看B同时B看A，3为相互不看',
  `r_create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `r_update_time` DATETIME NULL COMMENT '更新时间',
  `r_delete` INT(3)	NOT NULL DEFAULT 0 COMMENT '是否拉黑,0为互不拉黑，1为A拉黑B，2为B拉黑A',
  `r_add` INT(3) NULL DEFAULT 1 COMMENT '添加好友形式，1为A添加B，2为相互添加，默认为1',
  PRIMARY KEY (`relation_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8