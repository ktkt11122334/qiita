-- 仕様変更
alter table product_master modify product_code VARCHAR(20) NOT NULL;
alter table product_master modify jancode VARCHAR(20) NOT NULL;
alter table product_master modify product_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,

-- EC2のみ
alter table product_master modify   create_ts TIMESTAMP(5) NOT NULL DEFAULT CURRENT_TIMESTAMP(5);
-- [product_master]
CREATE TABLE IF NOT EXISTS product_master (
  product_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  product_code VARCHAR(6) NOT NULL,
  jancode VARCHAR(11) NOT NULL,
  disable_flg BOOLEAN DEFAULT 0,
  create_ts TIMESTAMP(5) NOT NULL,
  last_modified_ts TIMESTAMP(5) NOT NULL
);




alter table tax add disable_flg BOOLEAN NOT NULL DEFAULT 0;
alter table tax modify disable_flg BOOLEAN NOT NULL DEFAULT 0;

-- ****EC2のみ
alter table tax modify   create_ts TIMESTAMP(5) NOT NULL DEFAULT CURRENT_TIMESTAMP(5);
-- [tax]
CREATE TABLE IF NOT EXISTS tax (
  tax_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  tax_rate TINYINT NOT NULL,
  from_change_date DATE NOT NULL,
  disable_flg BOOLEAN NOT NULL DEFAULT 0,
  create_ts TIMESTAMP(5) NOT NULL,
  last_modified_ts TIMESTAMP(5) NOT NULL
);



-- ****EC2のみ
alter table product_master_history modify   create_ts TIMESTAMP(5) NOT NULL DEFAULT CURRENT_TIMESTAMP(5);
-- [product_master_history]
CREATE TABLE IF NOT EXISTS product_master_history (
  product_id BIGINT,
  last_modified_ts TIMESTAMP(5),
  product_name VARCHAR(255),
  price INT NOT NULL,
  tax_id BIGINT NOT NULL,
  create_ts TIMESTAMP(5) NOT NULL,

  PRIMARY KEY(product_id, last_modified_ts),
  CONSTRAINT fk_product_id FOREIGN KEY(product_id) REFERENCES product_master(product_id) ON UPDATE CASCADE,
  CONSTRAINT fk_tax_id FOREIGN KEY(tax_id) REFERENCES tax(tax_id) ON UPDATE CASCADE
);




-- ****EC2のみ
alter table customer modify   create_ts TIMESTAMP(5) NOT NULL DEFAULT CURRENT_TIMESTAMP(5);

-- [customer]
CREATE TABLE IF NOT EXISTS customer (
  customer_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  postal_code VARCHAR(20) NULL,
  first_name VARCHAR(100) NOT NULL,
  last_name VARCHAR(100) NULL,
  tell_number VARCHAR(20) NOT NULL,
  first_address VARCHAR(255) NOT NULL,
  last_address VARCHAR(255),
  mail_address VARCHAR(255) NOT NULL,
  create_ts TIMESTAMP(5) NOT NULL,
  last_modified_ts TIMESTAMP(5) NOT NULL
);




-- **追加
create index idx_order_status on order_header(order_status);
-- ****EC2のみ
alter table order_header modify   create_ts TIMESTAMP(5) NOT NULL DEFAULT CURRENT_TIMESTAMP(5);
-- [order_header]
CREATE TABLE IF NOT EXISTS order_header (
  order_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  order_price INT NOT NULL,
  customer_id BIGINT NOT NULL,
  order_status TINYINT NOT NULL,
  shipping_date DATE,
  create_ts TIMESTAMP(5) NOT NULL,
  last_modified_ts TIMESTAMP(5) NOT NULL,

  CONSTRAINT fk_customer_id FOREIGN KEY(customer_id) REFERENCES customer(customer_id) ON UPDATE CASCADE
);




-- ****EC2のみ
alter table order_detail modify   create_ts TIMESTAMP(5) NOT NULL DEFAULT CURRENT_TIMESTAMP(5);

-- [order_detail]
CREATE TABLE IF NOT EXISTS order_detail (
  order_id BIGINT NOT NULL,
  order_detail_id BIGINT NOT NULL,
  purchase_number INT NOT NULL,
  product_id BIGINT NOT NULL,
  product_last_modified_ts TIMESTAMP(5),
  create_ts TIMESTAMP(5) NOT NULL,
  last_modified_ts TIMESTAMP(5) NOT NULL,

  PRIMARY KEY(order_id, order_detail_id),
  CONSTRAINT fk_purchased_product FOREIGN KEY(product_id, product_last_modified_ts) REFERENCES product_master_history(product_id, last_modified_ts) ON UPDATE CASCADE
);




-- ****EC2のみ
alter table stock modify   create_ts TIMESTAMP(5) NOT NULL DEFAULT CURRENT_TIMESTAMP(5);
-- [stock]
CREATE TABLE IF NOT EXISTS stock (
  stock_id BIGINT  NOT NULL AUTO_INCREMENT PRIMARY KEY,
  product_id BIGINT NOT NULL,
  actual_stock_number INT NOT NULL DEFAULT 0,
  future_shipped_stock_number INT NOT NULL DEFAULT 0,
  standard_stock_number INT NOT NULL DEFAULT 0,
  create_ts TIMESTAMP(5) NOT NULL,
  last_modified_ts TIMESTAMP(5) NOT NULL,

  CONSTRAINT fk_stock_product FOREIGN KEY(product_id) REFERENCES product_master(product_id) ON UPDATE CASCADE
);





-- ****EC2のみ
alter table stock_consumption modify   create_ts TIMESTAMP(5) NOT NULL DEFAULT CURRENT_TIMESTAMP(5);

-- [stock_consumption]
CREATE TABLE IF NOT EXISTS stock_consumption (
  stock_consumption_id BIGINT  NOT NULL AUTO_INCREMENT PRIMARY KEY,
  stock_id BIGINT  NOT NULL,
  consumption_product_number INT DEFAULT 0,
  consumption_date DATE NOT NULL,
  order_id BIGINT NOT NULL,
  order_detail_id BIGINT NOT NULL,
  customer_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  create_ts TIMESTAMP(5) NOT NULL,
  last_modified_ts TIMESTAMP(5) NOT NULL,

  CONSTRAINT fk_consumed_stock FOREIGN KEY(stock_id) REFERENCES stock(stock_id) ON UPDATE CASCADE,
  CONSTRAINT fk_stock_order FOREIGN KEY(order_id, order_detail_id) REFERENCES order_detail(order_id, order_detail_id) ON UPDATE CASCADE,
  CONSTRAINT fk_stock_customer_id FOREIGN KEY(customer_id) REFERENCES customer(customer_id) ON UPDATE CASCADE,
  CONSTRAINT fk_consumed_product FOREIGN KEY(product_id) REFERENCES product_master(product_id) ON UPDATE CASCADE
);





