/*
 Navicat Premium Dump SQL

 Source Server         : MySQL80
 Source Server Type    : MySQL
 Source Server Version : 80039 (8.0.39)
 Source Host           : localhost:3306
 Source Schema         : tulipvase

 Target Server Type    : MySQL
 Target Server Version : 80039 (8.0.39)
 File Encoding         : 65001

 Date: 16/12/2024 21:13:52
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for address
-- ----------------------------
DROP TABLE IF EXISTS `address`;
CREATE TABLE `address`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `last_name` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `line1` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `line2` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `postal_code` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `mobile` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `user_id` int NOT NULL,
  `city_id` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_address_user1_idx`(`user_id` ASC) USING BTREE,
  INDEX `fk_address_city1_idx`(`city_id` ASC) USING BTREE,
  CONSTRAINT `fk_address_city1` FOREIGN KEY (`city_id`) REFERENCES `city` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_address_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of address
-- ----------------------------
INSERT INTO `address` VALUES (1, 'Chandupa', 'Jayalath', 'Anuradhapura', 'Anuradhapura', '50000', '0773729462', 13, 2);
INSERT INTO `address` VALUES (14, 'Chandupa', 'Jayalath', 'LC.59, National housing scheme airport road', 'Airport Road', '50000', '0773729462', 15, 2);
INSERT INTO `address` VALUES (15, 'Chandupa', 'Jayalath', 'LC.59, National housing scheme airport road', 'Airport Road', '50000', '0773729462', 16, 2);

-- ----------------------------
-- Table structure for cart
-- ----------------------------
DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `product_id` int NOT NULL,
  `qty` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_cart_user1_idx`(`user_id` ASC) USING BTREE,
  INDEX `fk_cart_product1_idx`(`product_id` ASC) USING BTREE,
  CONSTRAINT `fk_cart_product1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_cart_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 62 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cart
-- ----------------------------

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES (3, 'Arduino Uno');
INSERT INTO `category` VALUES (15, 'Sensors');
INSERT INTO `category` VALUES (16, 'LED');
INSERT INTO `category` VALUES (17, 'Accessories');
INSERT INTO `category` VALUES (18, 'Measuring Tools');

-- ----------------------------
-- Table structure for city
-- ----------------------------
DROP TABLE IF EXISTS `city`;
CREATE TABLE `city`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of city
-- ----------------------------
INSERT INTO `city` VALUES (1, 'Colombo');
INSERT INTO `city` VALUES (2, 'Anuradhapura');
INSERT INTO `city` VALUES (3, 'Jaffna');

-- ----------------------------
-- Table structure for color
-- ----------------------------
DROP TABLE IF EXISTS `color`;
CREATE TABLE `color`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of color
-- ----------------------------
INSERT INTO `color` VALUES (7, 'red');
INSERT INTO `color` VALUES (8, 'yellow');
INSERT INTO `color` VALUES (9, 'green');
INSERT INTO `color` VALUES (10, 'blue');

-- ----------------------------
-- Table structure for model
-- ----------------------------
DROP TABLE IF EXISTS `model`;
CREATE TABLE `model`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `category_id` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_model_category_idx`(`category_id` ASC) USING BTREE,
  CONSTRAINT `fk_model_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of model
-- ----------------------------
INSERT INTO `model` VALUES (3, 'Arduino', 3);
INSERT INTO `model` VALUES (21, 'Ultrasonic Sensor', 15);
INSERT INTO `model` VALUES (22, 'Infrared Sensor', 15);
INSERT INTO `model` VALUES (23, 'SMD Pixel LED', 16);
INSERT INTO `model` VALUES (24, 'LED Module', 16);
INSERT INTO `model` VALUES (25, 'IR Controller', 17);
INSERT INTO `model` VALUES (26, 'Multi Meter', 18);

-- ----------------------------
-- Table structure for order_item
-- ----------------------------
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_id` int NOT NULL,
  `qty` int NOT NULL,
  `order_status_id` int NOT NULL,
  `orders_id` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_order_item_product1_idx`(`product_id` ASC) USING BTREE,
  INDEX `fk_order_item_order_status1_idx`(`order_status_id` ASC) USING BTREE,
  INDEX `fk_order_item_orders1_idx`(`orders_id` ASC) USING BTREE,
  CONSTRAINT `fk_order_item_order_status1` FOREIGN KEY (`order_status_id`) REFERENCES `order_status` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_order_item_orders1` FOREIGN KEY (`orders_id`) REFERENCES `orders` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_order_item_product1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 41 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_item
-- ----------------------------
INSERT INTO `order_item` VALUES (39, 36, 1, 1, 43);
INSERT INTO `order_item` VALUES (40, 38, 1, 1, 44);

-- ----------------------------
-- Table structure for order_status
-- ----------------------------
DROP TABLE IF EXISTS `order_status`;
CREATE TABLE `order_status`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_status
-- ----------------------------
INSERT INTO `order_status` VALUES (1, 'Paid');
INSERT INTO `order_status` VALUES (2, 'Processing');
INSERT INTO `order_status` VALUES (3, 'Shipped');
INSERT INTO `order_status` VALUES (4, 'Deliverd');
INSERT INTO `order_status` VALUES (5, 'Pending');

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `date_time` datetime NOT NULL,
  `address_id` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_order_user1_idx`(`user_id` ASC) USING BTREE,
  INDEX `fk_order_address1_idx`(`address_id` ASC) USING BTREE,
  CONSTRAINT `fk_order_address1` FOREIGN KEY (`address_id`) REFERENCES `address` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_order_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 45 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES (38, 13, '2024-12-11 23:42:47', 1);
INSERT INTO `orders` VALUES (39, 15, '2024-12-14 23:48:10', 14);
INSERT INTO `orders` VALUES (40, 13, '2024-12-15 09:39:43', 1);
INSERT INTO `orders` VALUES (41, 13, '2024-12-15 09:42:12', 1);
INSERT INTO `orders` VALUES (42, 13, '2024-12-15 10:28:14', 1);
INSERT INTO `orders` VALUES (43, 13, '2024-12-15 22:52:20', 1);
INSERT INTO `orders` VALUES (44, 16, '2024-12-16 10:14:46', 15);

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `model_id` int NOT NULL,
  `title` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `description` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `color_id` int NOT NULL,
  `price` double NOT NULL,
  `qty` int NOT NULL,
  `date_time` datetime NOT NULL,
  `product_status_id` int NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_product_model1_idx`(`model_id` ASC) USING BTREE,
  INDEX `fk_product_color1_idx`(`color_id` ASC) USING BTREE,
  INDEX `fk_product_product_status1_idx`(`product_status_id` ASC) USING BTREE,
  INDEX `fk_product_user1_idx`(`user_id` ASC) USING BTREE,
  CONSTRAINT `fk_product_color1` FOREIGN KEY (`color_id`) REFERENCES `color` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_product_model1` FOREIGN KEY (`model_id`) REFERENCES `model` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_product_product_status1` FOREIGN KEY (`product_status_id`) REFERENCES `product_status` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_product_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 40 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `product` VALUES (33, 3, 'Arduino Uno', 'Microcontroller: ATmega328P \r\nOperating Voltage: 5V \r\nInput Voltage (recommended): 7-12V \r\nInout Voltage (limit): 6-20V \r\nDigital I/O Pins: 14 (of which 6 provide PWM output) \r\nPWM Digital I/O Pins: 6 \r\nAnalog Input Pins: 6 \r\nDC Current per I/O Pin: 20 mA \r\nDC current for 3.3V Pin: 50 mA \r\nFlash Memory: 32 KB (ATmega328P) of which 0.5 KB used by bootloader\r\nSRAM: 2 KB (ATmega328P)\r\nEEPROM: 1 KB (ATmega328P)\r\nClock Speed: 16 MHz\r\nLED_BUILTIN: 13\r\nLength: 68.6 mm \r\nWidth: 58.4 mm \r\nWeight: 25 g ', 10, 1500, 20, '2024-12-15 21:32:10', 1, 13);
INSERT INTO `product` VALUES (34, 21, 'Utrasonic Sensor', 'Operating voltage: +5V\r\nTheoretical Measuring Distance: 2cm to 450cm\r\nPractical Measuring Distance: 2cm to 80cm\r\nAccuracy: 3mm\r\nMeasuring angle covered: <15>\r\nOperating Current: <15mA>\r\nOperating Frequency: 40 Hz', 10, 300, 30, '2024-12-15 21:52:36', 1, 13);
INSERT INTO `product` VALUES (35, 22, 'Infrared Speed Sensor Module', 'Using imported groove optical coupling sensor, width 5mm.\r\nIt has the output state light, if output high level, the lights are off. if output low level ,it on.\r\nThe working voltage of 3.3V to 5V.\r\nOutput: digital switch output (0 and 1).\r\nEquipped with a fixed bolt hole, easily install.\r\nSmall board PCB size: 3.2 cm x 1.4 cm.\r\nUse the LM393 wide voltage comparer.', 10, 400, 25, '2024-12-15 22:00:29', 1, 13);
INSERT INTO `product` VALUES (36, 23, 'SMD Pixel LED WS2812B- Waterproof', 'nput Voltage(V) : DC5V\r\nLamp Power : 18W/m\r\nLamp Luminous Flux(lm) : 18-20lm/pcs\r\nWorking Temperature(â) : -30 â 60\r\nWorking Lifetime(Hour) : 50000\r\nApplication : office, Decorative Lighting\r\nLED Quantity : 300LEDs (60LEDs/m)\r\nIP Rating : IP65\r\nLength : 5m', 10, 450, 99, '2024-12-15 22:07:37', 1, 13);
INSERT INTO `product` VALUES (37, 24, 'LED Module (3 LEDs Module)', '3 LEDs per Unit', 10, 150, 50, '2024-12-15 22:34:18', 1, 13);
INSERT INTO `product` VALUES (38, 25, 'WS2812 Mini Dream Color IR Controller', 'Voltage: DC5V~24V\r\nWire length:225mm', 8, 2000, 29, '2024-12-15 22:50:04', 1, 13);
INSERT INTO `product` VALUES (39, 26, 'DIgital Multi Meter', 'This is the Updated Description', 7, 1499, 10, '2024-12-16 10:18:21', 1, 16);

-- ----------------------------
-- Table structure for product_status
-- ----------------------------
DROP TABLE IF EXISTS `product_status`;
CREATE TABLE `product_status`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of product_status
-- ----------------------------
INSERT INTO `product_status` VALUES (1, 'Active');
INSERT INTO `product_status` VALUES (2, 'Inactive');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `last_name` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `email` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `password` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `verification` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (6, 'Nimal', 'Kumara', 'Nimal@gmail.com', 'Abcd@123', 'Verified');
INSERT INTO `user` VALUES (7, 'Nishantha', 'Chandradasa', 'sachinthadilshanchandradasa@gmail.com', 'Dila@123', 'Verified');
INSERT INTO `user` VALUES (8, 'Dilshan', 'Chandradasa', 'sachintha4930@gmail.com', 'Dila@123', 'Verified');
INSERT INTO `user` VALUES (9, 'Sahintha', 'Dilshan', 'dilsh4930@gmail.com', 'Abcd@123', 'Verified');
INSERT INTO `user` VALUES (10, 'Sachintha', 'Dilshan', 'dilshan14930@gmail.com', 'Abcd@123', 'Verified');
INSERT INTO `user` VALUES (11, 'Daham', 'Bandara', 'dahamn09@gmail.com', 'Abcd@123', 'Verified');
INSERT INTO `user` VALUES (12, 'Sachintha', 'Dilshan', 'dilshan4930@gmail.com', 'Abcd@123', 'Verified');
INSERT INTO `user` VALUES (13, 'Chandupa', 'Jayalath', 'chandupajayalath20@gmail.com', 'Chandupa@2022', 'Verified');
INSERT INTO `user` VALUES (15, 'Chandupa', 'Jayalath', 'chandupajayalath33@gmail.com', 'Chandupa@123', 'Verified');
INSERT INTO `user` VALUES (16, 'Chandupa', 'Jayalath', 'chandupa54@gmail.com', 'CHandupa@1234', 'Verified');

SET FOREIGN_KEY_CHECKS = 1;
