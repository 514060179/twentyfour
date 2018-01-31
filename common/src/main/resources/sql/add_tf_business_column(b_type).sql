ALTER TABLE `twenty_four`.`tf_business`   
  ADD COLUMN `b_type` VARCHAR(11) DEFAULT '1' NULL COMMENT '业务类型1奇门遁甲2紫微斗数3子平八字4铁板神数5掌相' AFTER `b_update_time`;
