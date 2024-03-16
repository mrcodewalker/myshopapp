CREATE DATABASE shopapp;
USE shopapp;
--     Cần có bảng user
CREATE TABLE users(
	id INT PRIMARY KEY AUTO_INCREMENT,
	fullname VARCHAR(100) DEFAULT '',
	phone_number VARCHAR(10) NOT NULL,
	address VARCHAR(200) DEFAULT '',
	user_password VARCHAR(100) NOT NULL DEFAULT '', -- encoded
	created_at DATETIME,
	updated_at DATETIME,
	is_active TINYINT(1) DEFAULT 1,
	date_of_birth DATE,
	facebook_account_id INT DEFAULT 0,
	google_account_id INT DEFAULT 0
);
ALTER TABLE users ADD COLUMN role_id INT;

CREATE TABLE roles(
	id INT PRIMARY KEY,
	name VARCHAR(20) NOT NULL
);

ALTER TABLE users ADD FOREIGN KEY (role_id) REFERENCES roles(id);

CREATE TABLE tokens(
	id INT PRIMARY KEY AUTO_INCREMENT,
	token VARCHAR(255) UNIQUE NOT NULL,
	token_type VARCHAR(50) NOT NULL,
	expiration_date DATETIME,
	revoked TINYINT(1) NOT NULL,
	expired TINYINT(1) NOT NULL,
	user_id INT,
	FOREIGN KEY (user_id) REFERENCES users(id)
);

-- dang nhap tu fb hoac google
CREATE TABLE social_accounts(
	id INT PRIMARY KEY AUTO_INCREMENT,
	provider VARCHAR(20) NOT NULL COMMENT 'The main of name social network',
	provider_id VARCHAR(50) NOT NULL,
	email VARCHAR(150) NOT NULL COMMENT 'Email Account',
	name VARCHAR(100) NOT NULL COMMENT 'Name Account',
	user_id INT,
	FOREIGN KEY (user_id) REFERENCES users(id)
);
-- category ( danh muc san pham )
CREATE TABLE categories(
	id INT PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(100) NOT NULL DEFAULT '' COMMENT 'Name of category, example: food, toys,..'
);
-- bang chua san pham ( kieu: laptop macbook, iphone 15 pro max,...)
CREATE TABLE products(
	id INT PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(350) COMMENT 'Product name',
	price FLOAT NOT NULL CHECK(price>=0),
	thumbnail VARCHAR(300) DEFAULT '',
	description LONGTEXT DEFAULT '',
	created_at DATETIME,
	updated_at DATETIME,
	category_id INT,
	FOREIGN KEY (category_id) REFERENCES categories(id)
);
CREATE TABLE product_images(
	id INT PRIMARY KEY AUTO_INCREMENT,
	product_id INT,
	FOREIGN KEY (product_id) REFERENCES products(id),
	CONSTRAINT fk_product_images_product_id
		FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
	image_url VARCHAR(300)
);
-- Dat hang - orders
CREATE TABLE orders(
	id INT PRIMARY KEY AUTO_INCREMENT,
	user_id INT,
	FOREIGN KEY (user_id) REFERENCES users(id),
	fullname VARCHAR(100) DEFAULT '',
	email VARCHAR(100) DEFAULT '',
	phone_number VARCHAR(20) NOT NULL,
	address VARCHAR(200) NOT NULL,
	note VARCHAR(100) DEFAULT '',
	order_date DATETIME DEFAULT CURRENT_TIMESTAMP(),
	status VARCHAR(20),
	total_money FLOAT CHECK(total_money>=0)
);
ALTER TABLE orders ADD COLUMN shipping_method VARCHAR(100); 
ALTER TABLE orders ADD COLUMN shipping_address VARCHAR(200); 
ALTER TABLE orders ADD COLUMN shipping_date DATE; 
ALTER TABLE orders ADD COLUMN tracking_number VARCHAR(100); 
ALTER TABLE orders ADD COLUMN payment_method VARCHAR(100); 
-- xoa 1 don hang => xoa mem => them truong active
ALTER TABLE orders ADD COLUMN active TINYINT(1);
-- Trang thai don hang nhan 1 so cai cu the
ALTER TABLE orders 
MODIFY COLUMN status ENUM('pending','processing','shipped','delivered','cancelled')
COMMENT 'Order Status';

-- orders detail
CREATE TABLE order_details(
	id INT PRIMARY KEY AUTO_INCREMENT,
	order_id INT,
	FOREIGN KEY (order_id) REFERENCES orders(id),
	product_id INT,
	FOREIGN KEY (product_id) REFERENCES products(id),
	price FLOAT CHECK(price>=0),
	number_of_products INT CHECK(number_of_products>0),
	total_money FLOAT CHECK(total_money>=0),
	color VARCHAR(20) DEFAULT ''
);
CREATE TABLE coupons (
    id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(255),
    discount_value FLOAT,
    discount_type VARCHAR(255),
    expiration_date DATE
);
/* UPDATE products p
SET p.thumbnail = (
    SELECT product_images.image_url
    FROM product_images
    WHERE product_images.product_id = p.id
    ORDER BY product_images.id
    LIMIT 1
);
*/

CREATE TABLE comments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    user_id INT NOT NULL,
    comment_content TEXT,
    rating INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);
ALTER TABLE comments
ADD COLUMN is_active BOOLEAN,
ADD COLUMN order_id INT,
ADD FOREIGN KEY (order_id) REFERENCES orders(id);

ALTER TABLE users 
ADD COLUMN avatar VARCHAR(300) DEFAULT '';
UPDATE users 
SET avatar = 'default_avatar.png';
