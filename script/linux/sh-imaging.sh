#!/usr/bin/env bash
# ============================================================
# sh-imaging.sh — Linux 版 Docker 镜像构建
# 等价于 script/windows/ps-imaging.ps1:
#   清除旧镜像 → 为每个 ruoyi 微服务构建 Docker 镜像
# 依赖: 先运行 sh-compile.sh 生成 JAR
# ============================================================
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "${SCRIPT_DIR}/../.." && pwd)"

IMAGE_TAG="2.6.2"
REGISTRY="ruoyi"

# 需要构建的 ruoyi 微服务模块(不包含 guli 业务模块)
# 每个模块目录下必须有 target/<moduleName>.jar(由 sh-compile.sh 编译产出)
MODULES=(
  "ruoyi-auth"
  "ruoyi-gateway"
  "ruoyi-modules/ruoyi-gen"
  "ruoyi-modules/ruoyi-job"
  "ruoyi-modules/ruoyi-resource"
  "ruoyi-modules/ruoyi-system"
  "ruoyi-modules/ruoyi-workflow"
  "ruoyi-visual/ruoyi-monitor"
  "ruoyi-visual/ruoyi-nacos"
  "ruoyi-visual/ruoyi-seata-server"
  "ruoyi-visual/ruoyi-snailjob-server"
)

echo "========================================"
echo " 构建所有微服务 Docker 镜像"
echo "========================================"

# [1/2] 清除旧镜像
echo "[1/2] 清除旧镜像..."
for module in "${MODULES[@]}"; do
  MODULE_NAME="$(basename "$module")"
  IMAGE_NAME="$REGISTRY/${MODULE_NAME}:$IMAGE_TAG"
  if docker images -q "$IMAGE_NAME" 2>/dev/null | grep -q .; then
    echo "  删除: $IMAGE_NAME"
    docker rmi "$IMAGE_NAME" -f
  fi
done
echo "  完成"
echo ""

# [2/2] 构建 Docker 镜像
echo "[2/2] 构建 Docker 镜像..."
for module in "${MODULES[@]}"; do
  MODULE_NAME="$(basename "$module")"
  IMAGE_NAME="$REGISTRY/${MODULE_NAME}:$IMAGE_TAG"
  echo "  构建: $IMAGE_NAME"
  docker build -t "$IMAGE_NAME" "$PROJECT_ROOT/$module"
done
echo ""

echo "========================================"
echo " 所有镜像构建完成!"
echo "========================================"
echo "构建的镜像列表:"
docker images --format "table {{.Repository}}:{{.Tag}}\t{{.Size}}" | grep "^$REGISTRY/" || true