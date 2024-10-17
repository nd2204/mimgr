GRANT ALL PRIVILEGES ON mimgrdb.* TO 'mimgr'@'%';
FLUSH PRIVILEGES;

CREATE DATABASE IF NOT EXISTS mimgrdb;

USE mimgrdb;

CREATE TABLE IF NOT EXISTS users (
  id       INT         AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL,
  hash     CHAR(64)    NOT NULL,
  salt     CHAR(16)    NOT NULL
);

CREATE TABLE IF NOT EXISTS categories (
  category_id   INT           AUTO_INCREMENT PRIMARY KEY,
  parent_id     INT,
  category_name VARCHAR(100),
  FOREIGN KEY (parent_id) REFERENCES categories(category_id)
);

CREATE TABLE IF NOT EXISTS products (
  product_id     INT            AUTO_INCREMENT PRIMARY KEY,
  name           VARCHAR(255)   NOT NULL,
  price          DECIMAL(10, 2) NOT NULL,
  description    TEXT,
  stock_quantity INT            NOT NULL DEFAULT 0,
  sku            VARCHAR(50)    UNIQUE,
  category_id    INT,
  image_url      VARCHAR(255),
  created_at     TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
  updated_at     TIMESTAMP      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY(category_id) REFERENCES categories(category_id)
);

CREATE TABLE IF NOT EXISTS orders (
  order_id       INT           AUTO_INCREMENT PRIMARY KEY,
  order_status   VARCHAR(50)   NOT NULL,
  order_date     TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
  total_amount   DECIMAL(10,2) NOT NULL,
  payment_status VARCHAR(50)   NOT NULL
);

CREATE TABLE IF NOT EXISTS order_items (
  order_item_id INT AUTO_INCREMENT PRIMARY KEY,
  order_id      INT,
  product_id    INT,
  quantity      INT NOT NULL,
  unit_price    DECIMAL(10, 2) NOT NULL,
  total_price   DECIMAL(10, 2) NOT NULL,
  FOREIGN KEY (order_id) REFERENCES orders(order_id),
  FOREIGN KEY (product_id) REFERENCES products(product_id)
);

-- Add parent categories first
INSERT INTO categories (category_id, category_name) VALUES 
  (1, 'Keyboards'),
  (2, 'Guitars'),
  (3, 'Drums & Percussion'),
  (4, 'Amplifiers & Effects'),
  (5, 'Wind Instruments'),
  (6, 'String Instruments'),
  (7, 'Accessories');

-- Add subcategory
INSERT INTO categories (category_name, parent_id) VALUES
  ('Pianos', 1), ('Synthesizers', 1),
  ('Electric Guitars', 2), ('Acoustic Guitars', 2), ('Bass Guitars', 2),
  ('Drum Sets', 3), ('Cymbals', 3), ('Electronic Drums', 3), ('Percussion Instruments', 3),
  ('Guitar Amplifiers', 4), ('Bass Amplifiers', 4), ('Pedals & Effects', 4),
  ('Flutes', 5), ('Clarinets', 5), ('Saxophones', 5), ('Trumpets', 5),
  ('Violins', 6), ('Violas', 6), ('Cellos', 6), ('Double Basses', 6),
  ('Microphones', 7), ('Stands', 7), ('Pop Filters', 7), ('Cables', 7);

