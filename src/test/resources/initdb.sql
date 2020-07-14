create schema webshop;
set SCHEMA webshop;
CREATE TABLE ws_products(
    product_id integer NOT NULL,
    product_name character varying(512) NOT NULL,
    product_description text,
    product_picture_url character varying(1024),
    product_price double precision,
    creation_date time DEFAULT CURRENT_TIME,
    CONSTRAINT ws_products_pkey PRIMARY KEY (product_id)
);

INSERT INTO webshop.ws_products (product_id, product_name, product_description, product_picture_url, product_price) VALUES (27, 'Rogue Lawyer', 'John Grisham. Rogue Lawyer', 'https://book-ye.com.ua/upload/resize_cache/iblock/ff6/230_355_1/7d009888_917f_11e7_80cf_000c29ae1566_41b6e807_a091_11e7_80d1_000c29ae1566.jpg', 262);