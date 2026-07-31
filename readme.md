# 谷粒商城 — 开发指南

## 项目介绍

本项目包含两套模块体系：

| 类型 | 模块 | 说明 |
|------|------|------|
| **Ruoyi 微服务** | `ruoyi-auth` `ruoyi-gateway` `ruoyi-modules/*` `ruoyi-visual/*` | 平台基础设施，如认证、网关、系统管理、监控等 |
| **Guli 业务模块** | `guli-mall/*` | 商城业务，如商品、订单、会员、仓储等 |

### 目录说明

```
├── config/
│   ├── local-config/         # 本地开发使用的配置（如 Nacos、Grafana）
│   ├── mnt-config/           # Docker 容器挂载配置，由 ps-mounts.ps1 拷贝到 data-home/mnt/
│   ├── base-sql-config/      # Ruoyi 平台基础 SQL（ry-cloud、ry-config 等）
│   └── business-sql-config/  # Guli 商城业务 SQL（gulimall_pms、gulimall_oms 等）
│
├── script/
│   ├── ps-compile.ps1                # Maven 编译脚本
│   ├── ps-buildimage.ps1             # Docker 镜像构建脚本
│   ├── ps-mounts.ps1                 # 挂载目录初始化脚本
│   ├── docker-compose-infra.yml      # 基础设施编排
│   ├── docker-compose-middleware.yml  # 中间件编排
│   └── docker-compose-services.yml   # 微服务编排
│
├── dockerfiles/              # 需要自定义构建的中间件 Dockerfile
├── data-home/
│   ├── mnt/                  # 容器挂载数据（由 ps-mounts.ps1 生成）
│   └── logs/                 # 日志
```

---

## 三层架构

| 层级 | 编排文件 | 说明 |
|------|----------|------|
| **基础设施** | `docker-compose-infra.yml` | MySQL、Redis、Nacos、Seata、RabbitMQ、MinIO、SnailJob |
| **中间件** | `docker-compose-middleware.yml` | Elasticsearch、Kibana、Logstash、RocketMQ、Kafka、Prometheus、Grafana、SkyWalking、Nginx |
| **微服务** | `docker-compose-services.yml` | Gateway、Auth、System、Gen、Job、Resource、Workflow、Monitor |

> 基础设施是平台运行的必要依赖，需最先启动。中间件按业务需求逐步添加。微服务由你手动构建镜像后启动。

---

## 快速开始

### 前置：创建共享网络（仅首次）

```bash
docker network create gulimail
```

### 第一步：编译 Ruoyi 微服务模块

```powershell
.\script\ps-compile.ps1
```

使用 Maven 对 Ruoyi 微服务模块（排除 guli-mall 业务模块）执行 `clean package`，产出各模块的 `target/<module>.jar`。编译范围仅限 `ruoyi-auth`、`ruoyi-gateway`、`ruoyi-modules`、`ruoyi-visual` 及其依赖（`ruoyi-api`、`ruoyi-common`），不包含 guli 业务模块。

### 第二步：构建 Docker 镜像

```powershell
.\script\ps-buildimage.ps1
```

读取第一步产出的 JAR，为每个 Ruoyi 微服务执行 `docker build` 生成镜像（如 `ruoyi/ruoyi-gateway:2.6.2`）。这一步是后续容器启动的关键——没有镜像，`docker-compose-services.yml` 将无法启动。

> **为什么微服务编排不设 `build:` context？**  
> 基础设施（Nacos、Seata、SnailJob）在 `docker-compose-infra.yml` 中配置了 `build: context:`，因为它们数量固定、与平台强绑定。而 Ruoyi 微服务数量多、启动顺序灵活，你通过 `ps-buildimage.ps1` 手工构建镜像后，`docker-compose-services.yml` 直接使用 `image:` 拉取本地已有镜像，将"构建"和"启动"两个阶段解耦。

### 第三步：初始化挂载目录

```powershell
.\script\ps-mounts.ps1
```

清空并重建 `data-home/mnt/`，将 `config/mnt-config/` 中的配置文件复制进去，同时将 `config/base-sql-config/` 的 SQL 脚本放入 `mysql/db/`。这一步确保所有容器的持久化数据和配置就位。

> **SQL 初始化链路说明**
>
> ```
> ps-mounts.ps1 执行
>   → 复制 config/base-sql-config/*.sql → data-home/mnt/mysql/db/
>   → 挂载到 MySQL 容器的 /docker-entrypoint-initdb.d/
> docker-compose-infra.yml 启动 MySQL
>   → MySQL 首次启动时发现数据目录为空
>   → 自动按文件名字母序执行 /docker-entrypoint-initdb.d/ 下的所有 .sql
>   → ry-cloud.sql 写入 Nacos 所需的数据库和初始配置表
>   → ry-config.sql 写入 Nacos 中的各微服务初始 yml 配置
>   → Nacos 启动后读取这些 yml，各微服务注册时即可获取自己的配置
> ```
>
> 这就是为什么 Nacos 配置中心在全新部署后，就已经存在各微服务的初始配置——它们来自 `data-home/mnt/mysql/db/` 中被自动执行的 SQL，而非手动导入。

### 第四步：启动基础设施

```powershell
docker compose -f .\script\docker-compose-infra.yml -p guli-infra up -d
```

启动 MySQL、Redis、Nacos、Seata、RabbitMQ、MinIO、SnailJob。其中：

- **中央仓库镜像**：MySQL、Redis、MinIO、RabbitMQ 直接从 Docker Hub 拉取，无需本地构建
- **本地构建镜像**：Nacos、Seata、SnailJob 由于 `docker-compose-infra.yml` 中已配置 `build: context:` 指向 `ruoyi-visual/`，首次启动时 Compose 会自动构建

### 后续：按需启动

```powershell
# 启动微服务
docker compose -f .\script\docker-compose-services.yml -p guli-services up -d

# 启动中间件（可选）
docker compose -f .\script\docker-compose-middleware.yml -p guli-middleware up -d
```

---

## 附：清理

```bash
# 匿名卷
docker volume ls                    # 查看所有卷
docker volume prune                 # 清理未使用的卷
```
