#!/usr/bin/env bash
# ============================================================
# sh-mounts.sh — Linux 版挂载目录初始化脚本
# 等价于 ps-remounts.ps1:重建 data-home/mnt/ 目录结构、
# 复制 config/mnt-config/ 配置、复制 base SQL 到 mysql/db/
#
# 说明:data-home/mnt 可能归 root 所有(容器自动创建),
# 本脚本在无写权限时自动改用 docker(以 root 身份)执行。
# ============================================================
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "${SCRIPT_DIR}/.." && pwd)"
MNT_DIR="${PROJECT_ROOT}/data-home/mnt"
CONFIG_DIR="${PROJECT_ROOT}/config/mnt-config"
SQL_DIR="${PROJECT_ROOT}/config/base-sql-config"

# 需要创建的目录(与 ps-remounts.ps1 一致)
DIRS=(
  # --- infra ---
  mysql/data mysql/conf mysql/db
  redis/conf redis/data
  nacos/logs
  # --- middleware ---
  nginx/cert nginx/conf nginx/html nginx/log
  minio/data minio/config
  ruoyi-seata-server/logs
  skywalking/agent
  elk/elasticsearch/plugins elk/elasticsearch/data elk/elasticsearch/logs
  elk/kibana/config
  elk/logstash/pipeline elk/logstash/config
  rocketmq/namesrv/logs rocketmq/broker1/conf rocketmq/broker1/logs rocketmq/broker1/store
  rabbitmq rabbitmq/log rabbitmq/data
  kafka/data
  prometheus grafana
  shardingproxy/conf shardingproxy/ext-lib
  # --- services ---
  ruoyi-gateway/logs ruoyi-auth/logs ruoyi-system/logs ruoyi-gen/logs
  ruoyi-job/logs ruoyi-resource/logs ruoyi-workflow/logs ruoyi-monitor/logs
  snailjob/logs
)

echo "========================================"
echo " 初始化 data-home/mnt/ 目录结构"
echo "========================================"

# 判断是否可直接写(当前用户是 root,或 data-home 可写)
if [ -w "${MNT_DIR}" ] || [ "$(id -u)" -eq 0 ]; then
  RUN="local"
else
  RUN="docker"
  echo "[NOTE] data-home/mnt 无写权限,改用 docker(root) 执行"
fi

EXEC() { # 在 root 环境下执行
  if [ "$RUN" = "docker" ]; then
    docker run --rm -v "${PROJECT_ROOT}":/src:ro -v "${MNT_DIR}":/mnt alpine:latest sh -c "$1"
  else
    eval "$1"
  fi
}

# 创建所有子目录
dir_list=""
for d in "${DIRS[@]}"; do
  dir_list="${dir_list} mkdir -p /mnt/${d};"
done
EXEC "set -e; ${dir_list}"
echo "[OK] 目录结构就绪"

# 复制 config/mnt-config 全部配置 → data-home/mnt/
echo "========================================"
echo " 复制 config/mnt-config → data-home/mnt/"
echo "========================================"
EXEC "set -e; cp -a /src/config/mnt-config/. /mnt/"
echo "[OK] 配置复制完成"

# 复制 MySQL 初始化 SQL → data-home/mnt/mysql/db/
echo "========================================"
echo " 复制 base SQL → mysql/db/"
echo "========================================"
EXEC "set -e; mkdir -p /mnt/mysql/db; cp -a /src/config/base-sql-config/*.sql /mnt/mysql/db/"
echo "[OK] SQL 复制完成"

echo "========================================"
echo " 完成!data-home/mnt/ 目录已准备就绪"
echo "========================================"
