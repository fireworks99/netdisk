USE netdisk;

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL
);

-- 文件信息表
CREATE TABLE IF NOT EXISTS file_info (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    original_name VARCHAR(255),
    bucket_name VARCHAR(100),
    object_key VARCHAR(500),
    file_size BIGINT,
    content_type VARCHAR(100),
    file_ext VARCHAR(20),
    uploader_id BIGINT,
    etag VARCHAR(64),
    is_public TINYINT(1),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);



