ALTER TABLE tf_order_total add t_parent_id INT(11) NULL COMMENT '父总订单自关联Id';
ALTER TABLE tf_order_total add t_pay_no VARCHAR(255) NULL COMMENT '支付订单编号';
ALTER TABLE tf_order_total add t_master_id INT(11) NULL COMMENT '大师ID';