#!/usr/bin/env bash
# ============================================================
# sh-compile.sh — Linux 版 Maven 编译打包
# 等价于 script/windows/ps-compile.ps1:
#   仅编译 ruoyi 微服务模块(排除 guli-mall 业务模块),
#   产出各模块 target/<module>.jar
# ============================================================
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "${SCRIPT_DIR}/../.." && pwd)"

# ruoyi 微服务模块(需产出 JAR,与 sh-imaging.sh 对齐)
SERVICE_MODULES=(
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

# 设置 JVM 文件编码为 UTF-8,防止 MapStruct 注解处理器用系统 GBK 编码生成
# Java 文件,导致 maven-compiler-plugin(配置为 UTF-8)读取时报"读取文件时出错"
export MAVEN_OPTS="-Dfile.encoding=UTF-8"

cd "$PROJECT_ROOT"

echo "========================================"
echo " Maven 编译打包(仅 ruoyi 微服务)"
echo "========================================"

# [预处理] 验证模块路径
echo "[预处理] 验证模块路径..."
for module in "${SERVICE_MODULES[@]}"; do
  if [ ! -d "$PROJECT_ROOT/$module" ]; then
    echo "  模块路径不存在: $module" >&2
    exit 1
  fi
done
echo "  全部存在"
echo ""

# [1/2] Maven 编译打包(排除 guli-mall)
# 注意: '!guli-mall' 必须用单引号,避免被 bash 当作历史展开
# 日志通过 tee 同时写入 compile.log 并回显终端
echo "[1/2] 编译打包中... (排除 guli-mall)"
START="$(date +%s)"
mvn clean package -pl '!guli-mall' -DskipTests -Dfile.encoding=UTF-8 2>&1 | tee "$PROJECT_ROOT/compile.log"
if [ "${PIPESTATUS[0]}" -ne 0 ]; then
  echo "  失败,查看日志: $PROJECT_ROOT/compile.log" >&2
  exit 1
fi
ELAPSED="$(( $(date +%s) - START ))"
echo "  完成 (${ELAPSED}s)"
echo ""

# [2/2] 验证 JAR
echo "[2/2] 验证 JAR..."
HAS_ERROR=0
for module in "${SERVICE_MODULES[@]}"; do
  MODULE_NAME="$(basename "$module")"
  JAR_PATH="$PROJECT_ROOT/$module/target/$MODULE_NAME.jar"
  if [ ! -f "$JAR_PATH" ]; then
    echo "  缺失 $MODULE_NAME" >&2
    HAS_ERROR=1
    continue
  fi
  if [ ! -s "$JAR_PATH" ]; then
    echo "  失败 $MODULE_NAME - JAR 为空" >&2
    HAS_ERROR=1
    continue
  fi
  echo "  通过 $MODULE_NAME ($(stat -c %s "$JAR_PATH") bytes)"
done

if [ "$HAS_ERROR" -ne 0 ]; then
  echo "" >&2
  echo "JAR 验证失败!" >&2
  exit 1
fi
echo "全部通过"