# 网盘项目-后端



## 功能点

- [x] 文件的上传、下载、删除、预览、移动位置
- [x] 文件夹的创建、删除、移动位置
- [ ] 区分用户、角色、权限，不同的权限分配给不同的角色，不同的角色分配给不同的用户
- [ ] 大文件切片/分块上传，采用流式数据来给前端发送上传或下载的速度
- [ ] 上传、下载实现断点续传
- [x] 接口维护到Apifox



## 待办

- [ ] 写一个根据token获取用户信息的接口
- [ ] 后端接入Knife4j 调试



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



## 表关联

![DB](/src/main/resources/static/db.svg)



## SQL调用日志

在本项目的根目录上一级新建文件夹`netdisk-elk`

![DB](/src/main/resources/static/elkFolder.png)

里面新建两个文件:

`filebeat.yml`

~~~yml
filebeat.inputs:
  - type: filestream
    paths:
      - /logs/netdisk.log
    json.keys_under_root: true
    json.add_error_key: true

setup.template.name: "netdisk"
setup.template.pattern: "netdisk-*"
setup.template.enabled: true

output.elasticsearch:
  hosts: ["http://elasticsearch:9200"]
  index: "netdisk-sql-%{+yyyy.MM.dd}"
~~~

`docker-compose.yml`

~~~yml
version: '3'
services:
  elasticsearch:
    image: docker.elastic.co/elasticsearch/elasticsearch:8.12.0
    container_name: es
    environment:
      - discovery.type=single-node
      - xpack.security.enabled=false
      - ES_JAVA_OPTS=-Xms1g -Xmx1g
    ports:
      - 9200:9200
    volumes:
      - es_data:/usr/share/elasticsearch/data

  kibana:
    image: docker.elastic.co/kibana/kibana:8.12.0
    container_name: kibana
    ports:
      - 5601:5601
    environment:
      - ELASTICSEARCH_HOSTS=http://elasticsearch:9200
    depends_on:
      - elasticsearch

  filebeat:
    image: docker.elastic.co/beats/filebeat:8.12.0
    container_name: filebeat
    user: root
    command: >
      sh -c "chmod go-w /usr/share/filebeat/filebeat.yml && filebeat -e"
    volumes:
      - ./filebeat.yml:/usr/share/filebeat/filebeat.yml
      - ../netdisk/logs:/logs
    depends_on:
      - elasticsearch

volumes:
  es_data:
~~~

然后在`netdisk-elk`目录下打开终端，执行`docker-compose up -d`，详情查看[这篇文章](https://juejin.cn/post/7605448246995714089)。



## minio

1. 任意位置打开终端，执行`docker pull minio/minio`
2. 镜像拉取完后执行`docker run -d --name minio -p 9000:9000 -p 9001:9001 -v E:/minio/data:/data -e "MINIO_ROOT_USER=minioadmin" -e "MINIO_ROOT_PASSWORD=minioadmin" minio/minio server /data --console-address ":9001"`，注意这里的用户名密码跟Spring Boot配置文件里保持一致，minio要求用户名至少3字符，密码至少8字符



| 参数                              | 类型   | 作用                                                         |
| :-------------------------------- | :----- | :----------------------------------------------------------- |
| `docker`                          | 命令   | Docker 的主命令，调用 Docker 客户端                          |
| `run`                             | 子命令 | 创建并启动一个新容器                                         |
| `-d`                              | 选项   | **detach 模式**，让容器在**后台运行**，不会占用当前命令行窗口 |
| `--name minio`                    | 选项   | 给容器命名为 `minio`，方便后续通过 `docker stop minio` / `docker start minio` 等命令管理 |
| `-p 9000:9000`                    | 选项   | **端口映射**，将宿主机的 9000 端口映射到容器的 9000 端口。 作用：访问宿主机 `localhost:9000` 就等于访问容器内的 MinIO API 服务（上传/下载文件） |
| `-p 9001:9001`                    | 选项   | **端口映射**，将宿主机的 9001 端口映射到容器的 9001 端口。 作用：访问宿主机 `localhost:9001` 可进入 MinIO Web 管理控制台 |
| `-v E:/minio/data:/data`          | 选项   | **数据卷挂载**，将宿主机 `E:/minio/data` 目录挂载到容器内的 `/data` 目录。 作用：MinIO 存储的文件会保存在宿主机 E 盘，删除容器后数据**不会丢失** |
| `-e "MINIO_ROOT_USER=admin"`      | 选项   | **环境变量**，设置 MinIO 的**管理员用户名**为 `admin`，用于登录 Web 控制台 |
| `-e "MINIO_ROOT_PASSWORD=123456"` | 选项   | **环境变量**，设置 MinIO 的**管理员密码**为 `123456`。 ⚠️ 注意：MinIO 要求密码**至少 8 位**，`123456` 太短会导致容器启动失败 |
| `minio/minio`                     | 参数   | 指定使用的**镜像名称**，即 Docker Hub 上的 MinIO 官方镜像    |
| `server`                          | 参数   | **子命令**，告诉 MinIO 以**服务器模式**启动，这是启动 MinIO 服务的必需命令 |
| `/data`                           | 参数   | **目录参数**，指定 MinIO 存储数据的目录为容器内的 `/data`，对应宿主机的 `E:/minio/data` |
| `--console-address ":9001"`       | 选项   | MinIO 的启动选项，指定 **Web 控制台**监听容器的 **9001 端口**。 与 `-p 9001:9001` 配合，实现通过宿主机访问控制台 |

3. 然后访问`localhost:9000`，输入用户名与密码后，创建名为`netdisk`的Bucket（跟Spring Boot配置文件里写的保持一致）



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
  
* 2026-02-11
  
  * 文件查询允许分页以及按照名称过滤
  
  * 引入**P6Spy**，监控SQL并打印日志
  
  * 引入**logstash-logback-encoder**将纯文本日志转为JSON格式日志
  
  * ```
    Spring Boot
        ↓
    P6Spy 监控SQL打印纯文本日志
        ↓
    Logback 输出 JSON 日志
        ↓
    Filebeat（采集日志文件）
        ↓
    Elasticsearch（存储索引）
        ↓
    Kibana（查询 + 可视化）
    ```
  
* 2026-02-24
  
  * 批量操作（移动、删除、下载）
  
* 2026-02-25
  
  * 批量上传
  * 精简SQL日志
  * 我的收藏
  * 最近访问
  
* 2026-02-26
  
  * 最近访问
  
* 2026-08-14
  
  * 补充SQL调用日志的查询方法
  * 补充minio的使用
  
  
  
  