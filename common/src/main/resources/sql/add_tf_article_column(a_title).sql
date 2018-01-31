ALTER TABLE `twenty_four`.`tf_article`   
  ADD COLUMN `a_title` VARCHAR(128) DEFAULT '\"\"' NOT NULL COMMENT '文章标题' AFTER `a_read_amount`;