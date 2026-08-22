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

之后需要把它们打包进入maven仓库中，在构建业务模块时，将会被使用。
需要执行：

mvn install -pl "!guli-mall" "-DskipTests" "-Dfile.encoding=UTF-8"

### Guli 业务模块编译

```powershell
mvn clean compile -pl guli-mall/<module> -am -DskipTests

mvn clean compile -pl guli-mall/guli-product -am -DskipTests

mvn -f guli-mall/pom.xml clean install -DskipTests
```

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

<<<<<<< Updated upstream
### 后续：按需启动
=======
> Nacos 依赖 MySQL（`condition: service_healthy`）就绪后才启动；Seata / SnailJob 依赖 Nacos 就绪。启动顺序由 compose 自动处理。

#### ③ 编译 Ruoyi 微服务模块

对 Ruoyi 微服务模块（排除 `guli-mall` 业务模块）执行 `clean package`，产出各模块 `target/<module>.jar`。编译范
围仅限 `ruoyi-auth`、`ruoyi-gateway`、`ruoyi-modules`、`ruoyi-visual` 及其依赖（`ruoyi-api`、`ruoyi-common`）。

**Windows（PowerShell）：**
>>>>>>> Stashed changes

```powershell
# 启动微服务
docker compose -f .\script\docker-compose-services.yml -p guli-services up -d

# 启动中间件（可选）
docker compose -f .\script\docker-compose-middleware.yml -p guli-middleware up -d
```


**Linux：**

```bash
bash script/linux/sh-compile.sh
```

两个脚本等价：验证模块路径 → `mvn clean package -pl '!guli-mall' -DskipTests`（强制 UTF-8 编码，避免 MapStruct 在 GBK 系统下编码错乱）→ 逐一校验 JAR 非空。

> 若 PowerShell 报执行策略限制，先执行：`Set-ExecutionPolicy -Scope CurrentUser RemoteSigned`，或改用 `powershell -ExecutionPolicy Bypass -File .\script\windows\ps-compile.ps1`。

#### ④ 构建 Docker 镜像

读取第三步产出的 JAR，为每个 Ruoyi 微服务执行 `docker build` 生成镜像（如 `ruoyi/ruoyi-gateway:2.6.2`）。这一步是后续容器启动的关键——**没有镜像，`docker-compose-services.yml` 将无法启动**。

**Windows（PowerShell）：**

```powershell
.\script\windows\ps-imaging.ps1
```

**Linux：**

```bash
bash script/linux/sh-imaging.sh
```

两个脚本等价：清除旧镜像 → 按模块逐个 `docker build -t ruoyi/<name>:2.6.2 <模块目录>`。

> **为什么微服务编排不设 `build:` context？**
> 基础设施（Nacos、Seata、SnailJob）在 `docker-compose-infra.yml` 中配置了 `build: context:`，因为它们数量固定、与平台强绑定。而 Ruoyi 微服务数量多、启动顺序灵活，你通过 `ps-imaging.ps1` / `sh-imaging.sh` 手工构建镜像后，`docker-compose-services.yml` 直接使用 `image:` 拉取本地已有镜像，将"构建"和"启动"两个阶段解耦。

#### ⑤ 启动微服务

```bash
docker compose -f script/docker-compose-services.yml -p guli-services up -d
```

启动 Gateway、Auth、System、Gen、Job、Resource、Workflow、Monitor。**前提**：基础设施已启动且 Nacos 已就绪、第④步镜像已构建。

#### ⑥ 启动中间件（可选）


```bash
docker compose -f script/docker-compose-middleware.yml -p guli-middleware up -d
```

启动 Elasticsearch、Kibana、Logstash、RocketMQ（namesrv/broker/console）、Kafka、Prometheus、Grafana、SkyWalking（OAP/UI）、Nginx。各批次间已用 `depends_on: condition: service_healthy` 编排启动顺序。不需要的服务可只启动其中的一部分，例如仅 ES：

```bash
docker compose -f script/docker-compose-middleware.yml -p guli-middleware up -d elasticsearch
```

### 日常启动 / 停止（数据已初始化）

基础设施与微服务已初始化、镜像已构建后，重启只需：

```bash
# 启动（先 infra 再 services）
docker compose -f script/docker-compose-infra.yml -p guli-infra up -d
docker compose -f script/docker-compose-services.yml -p guli-services up -d

# 查看状态
docker compose -f script/docker-compose-infra.yml -p guli-infra ps
docker compose -f script/docker-compose-services.yml -p guli-services ps

# 查看日志
docker compose -f script/docker-compose-services.yml -p guli-services logs -f ruoyi-gateway

# 停止 / 重启单个服务
docker compose -f script/docker-compose-services.yml -p guli-services restart ruoyi-system
docker compose -f script/docker-compose-services.yml -p guli-services stop ruoyi-system

# 全部停止（不会删除数据，数据在 bind mount 里）
docker compose -f script/docker-compose-infra.yml -p guli-infra down
docker compose -f script/docker-compose-services.yml -p guli-services down
```

> 全部停止用 `down` 即可；数据（MySQL、Redis 等）都在 `data-home/mnt/` 的 bind mount 中，`down` 不会清除。只有重新执行第①步挂载脚本才会清空数据。

---

## Guli 业务模块（guli-mall）

`guli-mall/` 下的商城业务模块（`guli-coupon` `guli-member` `guli-order` `guli-product` `guli-ware`）是**独立的 Spring Boot 应用**，目前**不在任何 docker-compose 编排中、也没有构建镜像**，需要单独编译与运行。

| 模块 | 服务名 | 端口 | 说明 |
|------|--------|------|------|
| guli-coupon | 9211 | | 营销/优惠券 |
| guli-member | 9212 | | 会员 |
| guli-order | 9213 | | 订单 |
| guli-product | 9214 | | 商品 |
| guli-ware | 9215 | | 仓储 |

> 端口取自各模块 `src/main/resources/application.yml` 的 `server.port`。

### 编译

```bash
mvn clean compile -pl guli-mall/guli-product -am -DskipTests
```

### 运行

各模块的 `@profiles.active@`、`@nacos.server@` 等占位符在 **Maven 构建时** 由 profile 过滤替换：

- 默认 `dev`：`nacos.server = guli-nacos:8848`（Docker 网络内主机名，**宿主机解析不了**）
- `prod`：`nacos.server = 127.0.0.1:8848`（宿主机直连）

所以在**宿主机本地运行** guli 模块，要用 `prod` profile 编译并启动，使其注册到映射到宿主机的 Nacos（8848）：

```bash
# 以 prod profile 安装依赖并编译该模块
mvn clean install -pl guli-mall/guli-product -am -DskipTests -Pprod

# 启动
mvn spring-boot:run -pl guli-mall/guli-product -Pprod
```

或者直接在 IDE 中运行各模块的 `XxxApplication.java`（如 `GuliProductApplication`），但注意：IDE 运行使用的是 `target/classes` 下已被过滤的 `application.yml`，**必须以 `prod` profile 构建过一次**，否则仍会指向 `guli-nacos` 主机名导致注册失败。

### 业务数据库

Guli 模块使用的业务库（`gulimall_pms`、`gulimall_oms`、`gulimall_sms`、`gulimall_ums`、`gulimall_wms`、`gulimall_admin`）来自 `config/business-sql-config/`，**不会被挂载脚本自动导入**（自动导入的只有 `base-sql-config/` 的 Ruoyi 平台库）。使用前需手动导入：

```bash
docker exec -i guli-mysql mysql -uroot -ppassword < config/business-sql-config/gulimall_pms.sql
```

---

## 端口一览

### 基础设施（`guli-infra`）

| 服务 | 容器 | 端口 |
|------|------|------|
| MySQL | guli-mysql | 3306 |
| Redis | guli-redis | 6379 |
| Nacos（控制台 `nacos/nacos`） | guli-nacos | 8848 / 9848 / 9849 |
| Seata | guli-seata | 7091 / 8091 |
| RabbitMQ（控制台） | guli-rabbitmq | 5672 / 15672 |
| MinIO（控制台） | guli-minio | 9000 / 9001 |
| SnailJob（控制台） | guli-snailjob | 8800 / 17888 |

### Ruoyi 微服务（`guli-services`）

| 服务 | 容器 | 端口 |
|------|------|------|
| 网关 | guli-gateway | **8080** |
| 认证 | guli-auth | 9210 |
| 系统 | guli-system | 9201（Dubbo 20880） |
| 代码生成 | guli-gen | 9202 |
| 定时任务 | guli-job | 9203 |
| 资源 | guli-resource | 9204 |
| 工作流 | guli-workflow | 9205 |
| 监控 | guli-monitor | 9100 |

### 中间件（`guli-middleware`，可选）

| 服务 | 容器 | 端口 |
|------|------|------|
| Elasticsearch | guli-elasticsearch | 9200 / 9300 |
| Kibana | guli-kibana | 5601 |
| Logstash | guli-logstash | 4560 |
| RocketMQ NameSrv | guli-rmqnamesrv | 9876 |
| RocketMQ Broker | guli-rmqbroker1 | 10911 / 10909 / 10912 |
| RocketMQ Dashboard | guli-rmqconsole | 19876 |
| Kafka | guli-kafka | 9092 / 9093 |
| Prometheus | guli-prometheus | 9090 |
| Grafana | guli-grafana | 3000 |
| SkyWalking OAP | guli-sky-oap | 11800 / 12800 |
| SkyWalking UI | guli-sky-ui | 18080 |
| Nginx | guli-nginx | 80 / 443 |

---

## SQL 初始化链路说明

```
挂载脚本执行
  → 复制 config/base-sql-config/*.sql → data-home/mnt/mysql/db/
  → 挂载到 MySQL 容器的 /docker-entrypoint-initdb.d/
docker-compose-infra.yml 启动 MySQL
  → MySQL 首次启动时发现数据目录为空
  → 自动按文件名字母序执行 /docker-entrypoint-initdb.d/ 下的所有 .sql
  → ry-cloud.sql 写入 Nacos 所需的数据库和初始配置表
  → ry-config.sql 写入 Nacos 中的各微服务初始 yml 配置
  → Nacos 启动后读取这些 yml，各微服务注册时即可获取自己的配置
```

这就是为什么 Nacos 配置中心在全新部署后，就已经存在各微服务的初始配置——它们来自 `data-home/mnt/mysql/db/` 中被自动执行的 SQL，而非手动导入。

> 只有数据目录为空（首次初始化）时才会执行这些 SQL；已有数据不会重复执行。

---

## 常见问题

**Q：`docker compose up` 报 `network gulimail not found`？**
先创建共享网络：`docker network create gulimail`。

**Q：PowerShell 提示"禁止运行脚本"？**
```powershell
Set-ExecutionPolicy -Scope CurrentUser RemoteSigned
# 或单次绕过
powershell -ExecutionPolicy Bypass -File .\script\windows\ps-compile.ps1
```

**Q：guli 业务模块在宿主机启动后注册不到 Nacos / 报 `guli-nacos` 无法解析？**
用 `prod` profile（指向 `127.0.0.1:8848`）重新编译再运行，参见"Guli 业务模块"一节。

**Q：MySQL 中文乱码？**
compose 中已设置 `LANG=C.UTF-8`、`--character-set-server=utf8mb4`，正常情况下不会乱码；若曾用旧库，检查库表字符集是否为 `utf8mb4`。

**Q：如何确认整套后端就绪？**
```bash
docker ps                              # 所有 guli-* 容器 Up 且 gateway 显示 (healthy)
curl http://localhost:8080/actuator/health   # gateway 健康检查
```
>>>>>>> Stashed changes
