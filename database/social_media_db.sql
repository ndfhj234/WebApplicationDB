-- Create the database if it doesn't exist
DROP DATABASE IF EXISTS social_media_db;
CREATE DATABASE social_media_db;
USE social_media_db;

CREATE TABLE account (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    user_role ENUM('user', 'admin', 'super_admin') NOT NULL DEFAULT 'user',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_name (user_name),
    INDEX idx_user_role (user_role)
);

CREATE TABLE posts (
    posts_id INT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(50) NOT NULL,
    post1 VARCHAR(200) DEFAULT NULL,
    post2 VARCHAR(200) DEFAULT NULL,
    post3 VARCHAR(200) DEFAULT NULL,
    post4 VARCHAR(200) DEFAULT NULL,
    post5 VARCHAR(200) DEFAULT NULL,
    last_post_index INT DEFAULT 0,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_name) REFERENCES account(user_name) ON DELETE CASCADE,
    INDEX idx_user_name (user_name)
);

CREATE TABLE follows (
    follows_id INT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(50) NOT NULL,
    follow1 VARCHAR(50) DEFAULT NULL,
    follow2 VARCHAR(50) DEFAULT NULL,
    follow3 VARCHAR(50) DEFAULT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_name) REFERENCES account(user_name) ON DELETE CASCADE,
    INDEX idx_user_name (user_name)
);

CREATE TABLE help_messages (
    message_id INT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(50) NOT NULL,
    message_text TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_read BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_name) REFERENCES account(user_name) ON DELETE CASCADE,
    INDEX idx_user_name (user_name),
    INDEX idx_created_at (created_at)
);

INSERT INTO account (user_name, password, user_role) VALUES
('alex_johnson', 'alex123', 'user'),
('maria_garcia', 'maria123', 'user'),
('david_chen', 'david123', 'user'),
('olivia_patel', 'olivia123', 'user'),
('marcus_kim', 'marcus123', 'user'),
('admin_rachel', 'admin123', 'admin'),
('admin_carlos', 'admin123', 'admin'),
('super_diana', 'super123', 'super_admin'),
('jason_wong', 'jason123', 'user'),
('sophia_miller', 'sophia123', 'user');
