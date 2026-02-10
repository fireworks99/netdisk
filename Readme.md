# 网盘项目-后端



## 功能点

- [ ] 文件的上传、下载、删除、预览、移动位置
- [ ] 文件夹的创建、删除、移动位置
- [ ] 区分用户、角色、权限，不同的权限分配给不同的角色，不同的角色分配给不同的用户
- [ ] 大文件切片/分块上传，采用流式数据来给前端发送上传或下载的速度
- [ ] 上传、下载实现断点续传
- [x] 接口维护到Apifox



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



## 更新日志

* 2026-01-06

  * 项目初始化

    * IDEA + Spring Initializr（Spring Boot v2.6.13 / JDK 1.8.0_441 / Java 8）
    * 写建表语句
    * `application.properties`配置数据库连接、初始化sql位置
    * Mybatis最小闭环（Entity => Mapper => XML）
    * 写Controller，调用mapper执行查询语句
  * 添加Service层，集齐三层架构（Controller => Service => DAO）
  
    * Controller: 
      * `XxxController.java`：`private final XxxService xxxService;`
    * Service: 
      * `XxxService.java`
      * `XxxServiceImpl.java`：`private final XxxMapper xxxMapper;`
    * DAO：
      * `XxxMapper.java`
      * `XxxMapper.xml`
  * 封装公共结果`Result<T>{ code; meg; data }`
  * 实现文件上传功能，接口更新到Apifox
  * `application.yml`代替`application.properties`
  
* 2026-01-07

  * 实现文件查询、文件下载功能

* 2026-01-14

  * 封装公共异常
  * 处理跨域请求

* 2026-01-30
  
  * 接入minio，由**本地文件存储**转为**对象存储**
  * 修改FileInfo的数据库表(SQL Table) => Mapper => Service => Controller
  * 修改文件查询、上传、下载逻辑，更新接口到Apifox
  
* 2026-02-03
  
  * MinIO对象存储模型 => 网盘文件模型
  * FileInfo => DiskItem
  
* 2026-02-10
  
  * 回收站（列表查询、恢复、彻底删除）
  * 采用定时任务清理回收站，将过期时间与执行时间提取到配置文件
  * 
  
  
  
  