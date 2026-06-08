-- ============================================================
-- Weather Travel Planner 数据库初始化脚本
-- ============================================================
-- 用于创建所有必需的表结构

-- 创建数据库
CREATE DATABASE IF NOT EXISTS travel_planner CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE travel_planner;

-- ============================================================
-- 用户表
-- ============================================================
-- 存储用户账户信息
CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                     username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码（加密存储）',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '电话',
    avatar_url VARCHAR(255) COMMENT '头像URL',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_username (username)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ============================================================
-- 景点表
-- ============================================================
-- 存储景区基本信息
CREATE TABLE IF NOT EXISTS attractions (
                                           id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                           name VARCHAR(255) NOT NULL COMMENT '景点名称',
    city VARCHAR(100) NOT NULL COMMENT '所在城市',
    description TEXT COMMENT '景点描述',
    price DECIMAL(10,2) COMMENT '门票价格',
    rating FLOAT DEFAULT 0 COMMENT '评分（0-5）',
    category VARCHAR(50) COMMENT '景点类别（自然风景、文化遗产、娱乐等）',
    latitude DOUBLE COMMENT '纬度',
    longitude DOUBLE COMMENT '经度',
    open_time VARCHAR(50) COMMENT '开放时间',
    close_time VARCHAR(50) COMMENT '关闭时间',
    image_url VARCHAR(255) COMMENT '景点图片URL',
    visit_time_min INT DEFAULT 60 COMMENT '最少游玩时间（分钟）',
    visit_time_max INT DEFAULT 180 COMMENT '最多游玩时间（分钟）',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    -- 创建索引以加快查询
    INDEX idx_city (city),
    INDEX idx_category (category),
    INDEX idx_location (latitude, longitude)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='景点表';

-- ============================================================
-- 路线表
-- ============================================================
-- 存储用户的旅游路线规划
CREATE TABLE IF NOT EXISTS routes (
                                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                      user_id BIGINT NOT NULL COMMENT '用户ID',
                                      title VARCHAR(255) COMMENT '路线标题',
    description TEXT COMMENT '路线描述',
    start_city VARCHAR(100) NOT NULL COMMENT '出发城市',
    end_city VARCHAR(100) NOT NULL COMMENT '目的地城市',
    start_date DATE NOT NULL COMMENT '开始日期',
    end_date DATE NOT NULL COMMENT '结束日期',
    duration INT COMMENT '旅游天数',
    total_distance INT COMMENT '总距离（米）',
    total_price DECIMAL(10,2) COMMENT '总价格',
    weather_score INT COMMENT '天气友好度评分（0-100）',
    route_data LONGTEXT COMMENT '路线详细数据（JSON格式存储）',
    status VARCHAR(50) DEFAULT 'draft' COMMENT '路线状态：draft(草稿)/published(已发布)/completed(已完成)',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_dates (start_date, end_date)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='路线表';

-- ============================================================
-- 路线-景点关联表
-- ============================================================
-- 存储路线中包含的景点及其顺序
CREATE TABLE IF NOT EXISTS route_attractions (
                                                 id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                                 route_id BIGINT NOT NULL COMMENT '路线ID',
                                                 attraction_id BIGINT NOT NULL COMMENT '景点ID',
                                                 day_number INT NOT NULL COMMENT '第几天',
                                                 sequence INT NOT NULL COMMENT '当天的顺序（第几个景点）',
                                                 start_time TIME COMMENT '开始时间',
                                                 end_time TIME COMMENT '结束时间',
                                                 travel_time INT COMMENT '交通耗时（分钟）',
                                                 notes TEXT COMMENT '备注',
                                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                                 FOREIGN KEY (route_id) REFERENCES routes(id) ON DELETE CASCADE,
    FOREIGN KEY (attraction_id) REFERENCES attractions(id) ON DELETE CASCADE,
    -- 唯一约束：同一路线的同一天同一景点只能出现一次
    UNIQUE KEY unique_route_attraction (route_id, attraction_id, day_number, sequence)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='路线-景点关联表';

-- ============================================================
-- 天气缓存表
-- ============================================================
-- 缓存天气数据，避免频繁调用API
CREATE TABLE IF NOT EXISTS weather_cache (
                                             id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                             city VARCHAR(100) NOT NULL COMMENT '城市名称',
    date_str DATE NOT NULL COMMENT '日期',
    temp INT COMMENT '温度（°C）',
    feels_like INT COMMENT '体感温度（°C）',
    humidity INT COMMENT '湿度（%）',
    rain_probability INT COMMENT '降雨概率（%）',
    wind_speed INT COMMENT '风速（m/s）',
    wind_level INT COMMENT '风力等级（0-12）',
    uv_index FLOAT COMMENT '紫外线指数',
    description VARCHAR(255) COMMENT '天气描述',
    weather_data LONGTEXT COMMENT '完整天气数据（JSON格式）',
    cached_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '缓存时间',
    expires_at TIMESTAMP COMMENT '过期时间',
    UNIQUE KEY unique_city_date (city, date_str)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='天气缓存表';

-- ============================================================
-- 订单表
-- ============================================================
-- 存储用户的订单信息
CREATE TABLE IF NOT EXISTS orders (
                                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                      user_id BIGINT NOT NULL COMMENT '用户ID',
                                      route_id BIGINT NOT NULL COMMENT '路线ID',
                                      order_no VARCHAR(100) NOT NULL UNIQUE COMMENT '订单号',
    total_price DECIMAL(10,2) COMMENT '总价格',
    status VARCHAR(50) DEFAULT 'pending' COMMENT '订单状态：pending(待支付)/paid(已支付)/cancelled(已取消)',
    payment_method VARCHAR(50) COMMENT '支付方式',
    payment_time TIMESTAMP COMMENT '支付时间',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (route_id) REFERENCES routes(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_order_no (order_no)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单表';

-- ============================================================
-- 订单-门票关联表
-- ============================================================
-- 订单中的具体门票信息
CREATE TABLE IF NOT EXISTS order_tickets (
                                             id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                             order_id BIGINT NOT NULL COMMENT '订单ID',
                                             attraction_id BIGINT NOT NULL COMMENT '景点ID',
                                             quantity INT COMMENT '购买数量',
                                             unit_price DECIMAL(10,2) COMMENT '单价',
    total_price DECIMAL(10,2) COMMENT '总价',
    visit_date DATE COMMENT '参观日期',
    status VARCHAR(50) DEFAULT 'paid' COMMENT '门票状态',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (attraction_id) REFERENCES attractions(id) ON DELETE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单-门票关联表';

-- ============================================================
-- 评论表
-- ============================================================
-- 用户对景点的评论和评分
CREATE TABLE IF NOT EXISTS reviews (
                                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                       user_id BIGINT NOT NULL COMMENT '用户ID',
                                       attraction_id BIGINT NOT NULL COMMENT '景点ID',
                                       rating INT COMMENT '评分（1-5）',
                                       content TEXT COMMENT '评论内容',
                                       images LONGTEXT COMMENT '图片URLs（JSON数组格式）',
                                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                       FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (attraction_id) REFERENCES attractions(id) ON DELETE CASCADE,
    INDEX idx_attraction_id (attraction_id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评论表';

-- ============================================================
-- 创建视图（方便查询）
-- ============================================================

-- 用户路线统计视图
CREATE OR REPLACE VIEW user_route_stats AS
SELECT
    u.id,
    u.username,
    COUNT(r.id) as total_routes,
    SUM(CASE WHEN r.status = 'completed' THEN 1 ELSE 0 END) as completed_routes,
    AVG(r.weather_score) as avg_weather_score
FROM users u
         LEFT JOIN routes r ON u.id = r.user_id
GROUP BY u.id, u.username;

-- 景点热度视图
CREATE OR REPLACE VIEW attraction_popularity AS
SELECT
    a.id,
    a.name,
    a.city,
    COUNT(DISTINCT ra.route_id) as used_in_routes,
    COUNT(DISTINCT r.id) as total_reviews,
    AVG(r.rating) as avg_rating
FROM attractions a
         LEFT JOIN route_attractions ra ON a.id = ra.attraction_id
         LEFT JOIN reviews r ON a.id = r.attraction_id
GROUP BY a.id, a.name, a.city;

-- ============================================================
-- 插入示例数据（可选）
-- ============================================================

-- 插入样本用户
INSERT INTO users (username, password, email) VALUES
                                                  ('admin', 'admin123', 'admin@example.com'),
                                                  ('user1', 'user123', 'user1@example.com');

-- 插入样本景点
INSERT INTO attractions (name, city, description, price, rating, category, latitude, longitude, visit_time_min, visit_time_max) VALUES
                                                                                                                                    ('西湖', '杭州', '中国最美的湖泊之一', 0, 4.8, '自然风景', 30.2741, 120.1551, 120, 240),
                                                                                                                                    ('灵隐寺', '杭州', '著名的佛教寺庙', 30, 4.7, '文化遗产', 30.2383, 120.1042, 60, 120),
                                                                                                                                    ('千岛湖', '杭州', '国家5A级景区', 150, 4.6, '自然风景', 29.6300, 118.9800, 180, 300);