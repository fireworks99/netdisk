# 网盘项目



## 功能点

1. 实现文件的上传、下载、删除、预览、移动位置 
2. 区分用户、角色、权限，不同的权限分配给不同的角色，不同的角色分配给不同的用户 
3. 大文件切片/分块上传，采用流式数据来给前端发送上传或下载的速度
4. 上传实现断点续传
5. 后端采用Springboot，前端采用Vue3
6. 前端通过mock可独立于后端运行
7. 前端项目通过Github Actions自动部署于Github Pages



## 初始化

```sql
-- 手动创建数据库
CREATE DATABASE IF NOT EXISTS netdisk
DEFAULT CHARACTER SET utf8mb4;
```

1. 安装 MySQL 8+
2. 创建数据库 netdisk
3. 修改 application.properties 中的数据库账号密码
4. 启动项目，数据库表将自动创建