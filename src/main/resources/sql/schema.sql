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

create table if not exists disk_item (
    id bigint auto_increment primary key comment '资源ID',

    -- 资源基本信息
    type varchar(20) not null comment '资源类型：FILE / FOLDER',
    name varchar(255) not null comment '文件或文件夹名称',

    -- 树结构
    parent_id bigint null comment '父文件夹ID，根目录为 NULL',
    owner_id bigint not null comment '所属用户ID',

    -- ========== 文件专属字段（文件夹为 NULL） ==========
    bucket_name varchar(100) null comment 'MinIO bucket',
    object_key varchar(500) null comment 'MinIO object key',
    file_size bigint null comment '文件大小（字节）',
    content_type varchar(100) null comment 'MIME 类型',
    file_ext varchar(20) null comment '文件扩展名',
    etag varchar(64) null comment '对象ETag',

    -- ========== 状态控制 ==========
    is_deleted tinyint(1) not null default 0 comment '是否已删除（回收站）',
    deleted_at datetime null comment '删除时间',

    -- 时间戳
    create_time datetime not null default current_timestamp comment '创建时间',
    update_time datetime not null default current_timestamp
        on update current_timestamp comment '更新时间',

    -- 索引
    index idx_owner_parent (owner_id, parent_id),
    index idx_owner_deleted (owner_id, is_deleted),
    index idx_object_key (object_key),

    -- 约束
    constraint chk_type check (type in ('FILE', 'FOLDER'))
) engine=InnoDB
  default charset=utf8mb4
    comment='网盘资源表（文件/文件夹统一）';




