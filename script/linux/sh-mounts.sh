#!/usr/bin/env bash
# ============================================================
# sh-mounts.sh — Linux 版挂载目录初始化脚本
# 等价于 ps-remounts.ps1:重建 data-home/mnt/ 目录结构、
# 复制 config/mnt-config/ 配置、复制 base SQL 到 mysql/db/
#
# 说明:data-home/mnt 可能归 root 所有(容器自动创建),
# 本脚本统一以 docker(root 身份)执行,确保写权限。
# ============================================================
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "${SCRIPT_DIR}/../.." && pwd)"
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

EXEC() { # 以 docker(root 身份)执行
  docker run --rm -v "${PROJECT_ROOT}":/src:ro -v "${MNT_DIR}":/mnt alpine:latest sh -c "$1"
}

# 创建所有子目录
dir_list=""
for d in "${DIRS[@]}"; do
  dir_list="${dir_list} mkdir -p /mnt/${d};"
done
EXEC "set -e; ${dir_list}"
echo "[OK] 目录结构就绪"

# 复制 config/mnt-config 全部配置 → data-home/mnt/
# 注意:用 tar 而非 cp -a。cp -a 会把 config 下运行期数据目录(*/data、*/store)的
# 属主覆盖成宿主用户(如 redis/data、kafka/data),导致容器内进程(uid 999 等)
# 无法写入 —— redis 的 RDB 快照曾因此失败(MISCONF)。tar --exclude 跳过它们,
# 让数据目录保持容器写入时的属主。
echo "========================================"
echo " 复制 config/mnt-config → data-home/mnt/(跳过 */data、*/store)"
echo "========================================"
EXEC "set -e; cd /src/config/mnt-config && tar --exclude='*/data' --exclude='*/store' -cf - . | (cd /mnt && tar -xpf -)"
echo "[OK] 配置复制完成(运行期数据目录已跳过)"

# 复制 MySQL 初始化 SQL → data-home/mnt/mysql/db/
echo "========================================"
echo " 复制 base SQL → mysql/db/"
echo "========================================"
EXEC "set -e; mkdir -p /mnt/mysql/db; cp -a /src/config/base-sql-config/*.sql /mnt/mysql/db/"
echo "[OK] SQL 复制完成"

echo "========================================"
echo " 完成!data-home/mnt/ 目录已准备就绪"
echo "========================================"
