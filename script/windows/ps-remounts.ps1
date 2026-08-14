$ErrorActionPreference = "Stop"
$ProjectRoot = Split-Path -Parent $PSScriptRoot  # 切到根目录
$DockerDir = Join-Path $ProjectRoot "config\mnt-config"
$SqlDir   = Join-Path $ProjectRoot "config\base-sql-config" # SQL 初始化脚本目录
$MntDir   = Join-Path $ProjectRoot "data-home\mnt"

# ============================================================
# 定义需要创建的目录列表
# ============================================================
$Directories = @(
    # --- infra ---
    "mysql\data"
    "mysql\conf"
    "mysql\db"      # MySQL 首次启动自动执行的初始化 SQL（挂载到 /docker-entrypoint-initdb.d/）
    "redis\conf"
    "redis\data"
    "nacos\logs"

    # --- middleware ---
    "nginx\cert"
    "nginx\conf"
    "nginx\html"
    "nginx\log"
    "minio\data"
    "minio\config"
    "ruoyi-seata-server\logs"
    "skywalking\agent"
    "elk\elasticsearch\plugins"
    "elk\elasticsearch\data"
    "elk\elasticsearch\logs"
    "elk\kibana\config"
    "elk\logstash\pipeline"
    "elk\logstash\config"
    "rocketmq\namesrv\logs"
    "rocketmq\broker1\conf"
    "rocketmq\broker1\logs"
    "rocketmq\broker1\store"
    "rabbitmq"
    "rabbitmq\log"
    "rabbitmq\data"
    "kafka\data"
    "prometheus"
    "grafana"
    "shardingproxy\conf"
    "shardingproxy\ext-lib"

    # --- services ---
    "ruoyi-gateway\logs"
    "ruoyi-auth\logs"
    "ruoyi-system\logs"
    "ruoyi-gen\logs"
    "ruoyi-job\logs"
    "ruoyi-resource\logs"
    "ruoyi-workflow\logs"
    "ruoyi-monitor\logs"
    "snailjob\logs"
)

# ============================================================
# 定义需要复制的文件映射：源（相对于 config/mnt-config/）-> 目标（相对于 data-home/mnt/）
# ============================================================
$FileCopies = @(
    # Redis 配置
    @{ Src = "redis\conf\redis.conf";           Dst = "redis\conf\redis.conf" },

    # Nginx 配置
    @{ Src = "nginx\conf\nginx.conf";           Dst = "nginx\conf\nginx.conf" },

    # SkyWalking Agent（整个目录）
    @{ Src = "skywalking\agent";                Dst = "skywalking\agent"; IsDir = $true },

    # ELK
    @{ Src = "elk\kibana\config\kibana.yml";   Dst = "elk\kibana\config\kibana.yml" },
    @{ Src = "elk\logstash\pipeline\logstash.conf"; Dst = "elk\logstash\pipeline\logstash.conf" },
    @{ Src = "elk\logstash\config\logstash.yml";    Dst = "elk\logstash\config\logstash.yml" },

    # RocketMQ broker 配置
    @{ Src = "rocketmq\broker1\conf\broker.conf"; Dst = "rocketmq\broker1\conf\broker.conf" },

    # Prometheus
    @{ Src = "prometheus\prometheus.yml";       Dst = "prometheus\prometheus.yml" },

    # Grafana
    @{ Src = "grafana\grafana.ini";             Dst = "grafana\grafana.ini" },

    # ShardingSphere Proxy 配置
    @{ Src = "shardingproxy\conf\server.yaml";                     Dst = "shardingproxy\conf\server.yaml" },
    @{ Src = "shardingproxy\conf\config-sharding.yaml";           Dst = "shardingproxy\conf\config-sharding.yaml" },
    @{ Src = "shardingproxy\conf\config-readwrite-splitting.yaml"; Dst = "shardingproxy\conf\config-readwrite-splitting.yaml" },
    @{ Src = "shardingproxy\conf\config-encrypt.yaml";            Dst = "shardingproxy\conf\config-encrypt.yaml" },
    @{ Src = "shardingproxy\conf\config-mask.yaml";               Dst = "shardingproxy\conf\config-mask.yaml" },
    @{ Src = "shardingproxy\conf\config-shadow.yaml";             Dst = "shardingproxy\conf\config-shadow.yaml" }
)

# ============================================================
# 主逻辑
# ============================================================

Write-Host "========================================" -ForegroundColor Cyan
Write-Host " 初始化 data-home/mnt/ 目录结构" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# 清空 data-home/mnt/ 目录
if (Test-Path -LiteralPath $MntDir) {
    Write-Host "[CLEAR] 清空 data-home/mnt/ 目录..." -ForegroundColor Yellow
    Remove-Item -LiteralPath $MntDir -Recurse -Force
    Write-Host "[CLEAR] data-home/mnt/ 已清空" -ForegroundColor Yellow
}
Write-Host ""

# 创建 data-home/mnt/ 根目录
if (-not (Test-Path -LiteralPath $MntDir)) {
    New-Item -ItemType Directory -Path $MntDir -Force | Out-Null
    Write-Host "[CREATE] $MntDir" -ForegroundColor Yellow
} else {
    Write-Host "[EXISTS] $MntDir" -ForegroundColor DarkGray
}

# 创建所有子目录
$createdCount = 0
foreach ($dir in $Directories) {
    $fullPath = Join-Path $MntDir $dir
    if (-not (Test-Path -LiteralPath $fullPath)) {
        New-Item -ItemType Directory -Path $fullPath -Force | Out-Null
        Write-Host "[CREATE] $dir/" -ForegroundColor Green
        $createdCount++
    }
}
Write-Host ""
Write-Host "已创建 $createdCount 个新目录" -ForegroundColor Cyan
Write-Host ""

# 复制配置文件
Write-Host "========================================" -ForegroundColor Cyan
Write-Host " 复制配置文件到 data-home/mnt/" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$copiedCount = 0
foreach ($file in $FileCopies) {
    $srcPath = Join-Path $DockerDir $file.Src
    $dstPath = Join-Path $MntDir $file.Dst

    if (-not (Test-Path -LiteralPath $srcPath)) {
        Write-Host "[SKIP] 源文件不存在: $($file.Src)" -ForegroundColor DarkYellow
        continue
    }

    if ($file.IsDir) {
        # 目录复制：先清空目标再复制
        if (Test-Path -LiteralPath $dstPath) {
            Remove-Item -LiteralPath $dstPath -Recurse -Force
        }
        Copy-Item -LiteralPath $srcPath -Destination $dstPath -Recurse -Force
        Write-Host "[COPY] $($file.Src) -> $($file.Dst)/" -ForegroundColor Green
    } else {
        # 文件复制
        $dstParent = Split-Path -Parent $dstPath
        if (-not (Test-Path -LiteralPath $dstParent)) {
            New-Item -ItemType Directory -Path $dstParent -Force | Out-Null
        }
        Copy-Item -LiteralPath $srcPath -Destination $dstPath -Force
        Write-Host "[COPY] $($file.Src) -> $($file.Dst)" -ForegroundColor Green
    }
    $copiedCount++
}

Write-Host ""
Write-Host "已复制 $copiedCount 个文件/目录" -ForegroundColor Cyan
Write-Host ""

# ============================================================
# MySQL 初始化 SQL：复制 config/base-sql-config/*.sql -> data-home/mnt/mysql/db/
# 挂载到 /docker-entrypoint-initdb.d/，MySQL 首次启动时按文件名字母序自动执行
# 注意：只有数据目录为空（首次初始化）时才会执行，已有数据不会重复执行
# ============================================================
$MySqlDbDir = Join-Path $MntDir "mysql\db"

Write-Host "========================================" -ForegroundColor Cyan
Write-Host " 复制 MySQL 初始化 SQL" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$sqlCount = 0
if (Test-Path -LiteralPath $SqlDir) {
    $sqlFiles = Get-ChildItem -LiteralPath $SqlDir -Filter "*.sql" -File | Sort-Object Name
    foreach ($sql in $sqlFiles) {
        $dstPath = Join-Path $MySqlDbDir $sql.Name
        Copy-Item -LiteralPath $sql.FullName -Destination $dstPath -Force
        Write-Host "[COPY] sql/$($sql.Name) -> mysql/db/$($sql.Name)" -ForegroundColor Green
        $sqlCount++
    }
} else {
    Write-Host "[SKIP] SQL 目录不存在: config/base-sql-config/" -ForegroundColor DarkYellow
}
Write-Host ""
Write-Host "已复制 $sqlCount 个 SQL 文件到 mysql/db/" -ForegroundColor Cyan
Write-Host ""

Write-Host "========================================" -ForegroundColor Cyan
Write-Host " 完成！data-home/mnt/ 目录已准备就绪" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
