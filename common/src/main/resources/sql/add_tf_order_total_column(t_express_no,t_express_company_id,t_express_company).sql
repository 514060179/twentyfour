ALTER TABLE tf_order_total add t_express_no VARCHAR(255) NULL COMMENT '物流编号';
ALTER TABLE tf_order_total add t_express_company_id INT(11) NULL COMMENT '物流公司ID';
ALTER TABLE tf_order_total add t_express_company VARCHAR(255) NULL COMMENT '物流公司名字';