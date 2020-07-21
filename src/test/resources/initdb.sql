create schema webshop;
set SCHEMA webshop;
CREATE TABLE ws_products(
    product_id integer AUTO_INCREMENT NOT NULL,
    product_name character varying(512) NOT NULL,
    product_description text,
    product_picture_url character varying(1024),
    product_price double precision,
    creation_date time DEFAULT CURRENT_TIME,
    product_version_id character varying(128),
    CONSTRAINT ws_products_pkey PRIMARY KEY (product_id)
);


INSERT INTO webshop.ws_products (product_id, product_name, product_description, product_picture_url, product_price) VALUES (32, 'Here I Am', 'Jonathan Safran Foer. Here I Am', 'https://book-ye.com.ua/upload/resize_cache/iblock/741/230_355_1/82b3af82_9bb2_11e8_8100_000c29ae1566_d00cc72a_9bb2_11e8_8100_000c29ae1566.jpg', 262);
INSERT INTO webshop.ws_products (product_id, product_name, product_description, product_picture_url, product_price) VALUES (33, 'Let It Snow', 'John Green. Let It Snow', 'https://book-ye.com.ua/upload/resize_cache/iblock/e81/230_355_1/4efcfc9e_9c82_11e8_8103_000c29ae1566_a3daee8b_9c82_11e8_8103_000c29ae1566.jpg', 243);
INSERT INTO webshop.ws_products (product_id, product_name, product_description, product_picture_url, product_price) VALUES (1, 'iPhone', 'Apple iPhone X', 'https://ss7.vzw.com/is/image/VerizonWireless/iphone7-front-matblk?$device-lg$', 18000);
INSERT INTO webshop.ws_products (product_id, product_name, product_description, product_picture_url, product_price) VALUES (22, 'Р”РѕРј С‚РµРЅРµР№', 'РњСЌРґРµР»РёРЅ Р Сѓ. Р”РѕРј С‚РµРЅРµР№', 'https://book-ye.com.ua/upload/resize_cache/iblock/eba/230_355_1/5b29f0d9_29e1_11e8_80ea_000c29ae1566_3364fa68_29e2_11e8_80ea_000c29ae1566.jpg', 138);
INSERT INTO webshop.ws_products (product_id, product_name, product_description, product_picture_url, product_price) VALUES (23, 'The Lost Symbol. Corgi Books', 'Dan Brown. The Lost Symbol. Corgi Books', 'https://book-ye.com.ua/upload/resize_cache/iblock/06f/230_355_1/67f56ead_4246_11e8_80f3_000c29ae1566_b2b00382_4246_11e8_80f3_000c29ae1566.jpg', 324);
INSERT INTO webshop.ws_products (product_id, product_name, product_description, product_picture_url, product_price) VALUES (24, 'It : film tie-in edition of Stephen...', 'Stephen King. It : film tie-in edition of Stephen...', 'https://book-ye.com.ua/upload/resize_cache/iblock/7b7/230_355_1/f02736e4_430c_11e8_80f3_000c29ae1566_83f10533_da08_11e9_8120_000c29ae1566.jpg', 324);
INSERT INTO webshop.ws_products (product_id, product_name, product_description, product_picture_url, product_price) VALUES (25, 'Paris for One and Other Stories. Mi...', 'Jojo Moyes. Paris for One and Other Stories. Mi...', 'https://book-ye.com.ua/upload/resize_cache/iblock/6ee/230_355_1/3a8834da_423e_11e8_80f3_000c29ae1566_a40cc420_a709_11ea_8138_000c29ae1566.jpg', 262);
INSERT INTO webshop.ws_products (product_id, product_name, product_description, product_picture_url, product_price) VALUES (26, 'Finders Keepers', 'Stephen King. Finders Keepers', 'https://book-ye.com.ua/upload/resize_cache/iblock/cbc/230_355_1/c768082a_917e_11e7_80cf_000c29ae1566_38bc26aa_a112_11e7_80d1_000c29ae1566.jpg', 262);
INSERT INTO webshop.ws_products (product_id, product_name, product_description, product_picture_url, product_price) VALUES (27, 'Rogue Lawyer', 'John Grisham. Rogue Lawyer', 'https://book-ye.com.ua/upload/resize_cache/iblock/ff6/230_355_1/7d009888_917f_11e7_80cf_000c29ae1566_41b6e807_a091_11e7_80d1_000c29ae1566.jpg', 262);
INSERT INTO webshop.ws_products (product_id, product_name, product_description, product_picture_url, product_price) VALUES (28, 'The Bazaar of Bad Dreams Export', 'Stephen King. The Bazaar of Bad Dreams Export', 'https://book-ye.com.ua/upload/resize_cache/iblock/1f5/230_355_1/91c36244_917f_11e7_80cf_000c29ae1566_469831ce_a092_11e7_80d1_000c29ae1566.jpg', 262);
INSERT INTO webshop.ws_products (product_id, product_name, product_description, product_picture_url, product_price) VALUES (29, 'Baby Touch and Feel Puppies', '. Baby Touch and Feel Puppies', 'https://book-ye.com.ua/upload/resize_cache/iblock/cc3/230_355_1/a51d09d2_26cb_11e8_80e8_000c29ae1566_f0a4b5b5_26cb_11e8_80e8_000c29ae1566.jpg', 144);
INSERT INTO webshop.ws_products (product_id, product_name, product_description, product_picture_url, product_price) VALUES (30, 'Baby Touch and Feel Kittens', '. Baby Touch and Feel Kittens', 'https://book-ye.com.ua/upload/resize_cache/iblock/e5d/230_355_1/1b9599da_26cd_11e8_80e8_000c29ae1566_6e98a9a3_26cd_11e8_80e8_000c29ae1566.jpg', 144);
INSERT INTO webshop.ws_products (product_id, product_name, product_description, product_picture_url, product_price) VALUES (31, 'A Man Called Ove', 'Fredrik Backman. A Man Called Ove', 'https://book-ye.com.ua/upload/resize_cache/iblock/f84/230_355_1/a89cec75_9b27_11e8_8100_000c29ae1566_e3b6c5c2_3d04_11ea_8126_000c29ae1566.jpg', 300);
commit;