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
    original_name VARCHAR(255) NOT NULL COMMENT '原始文件名',
    storage_name VARCHAR(255) NOT NULL COMMENT '磁盘存储名',
    storage_path VARCHAR(500) NOT NULL COMMENT '磁盘路径',
    file_size BIGINT NOT NULL COMMENT '文件大小（字节）',
    content_type VARCHAR(100) COMMENT '文件类型',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);



