# ==========================================
# 构建所有 ruoyi 微服务 Docker 镜像
# 注意该文件是 utf8 with bom 的编码格式
# 依赖: 先运行 ps-compile.ps1 编译生成 JAR
# ==========================================

# 设置控制台为utf8编码
$PSDefaultParameterValues['Out-File:Encoding'] = 'utf8'
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
[Console]::InputEncoding = [System.Text.Encoding]::UTF8

$ErrorActionPreference = "Stop"
$IMAGE_TAG = "2.6.2"
$REGISTRY = "ruoyi"

# 项目根目录（脚本在 script/ 下）
$ProjectRoot = Split-Path -Parent $PSScriptRoot

# 需要构建的 ruoyi 微服务模块（不包含 guli 业务模块）
# 每个模块目录下必须有 target/<moduleName>.jar（由 ps-compile.ps1 编译产出）
$MODULES = @(
    "ruoyi-auth",
    "ruoyi-gateway",
    "ruoyi-modules/ruoyi-gen",
    "ruoyi-modules/ruoyi-job",
    "ruoyi-modules/ruoyi-resource",
    "ruoyi-modules/ruoyi-system",
    "ruoyi-modules/ruoyi-workflow",
    "ruoyi-visual/ruoyi-monitor",
    "ruoyi-visual/ruoyi-nacos",
    "ruoyi-visual/ruoyi-seata-server",
    "ruoyi-visual/ruoyi-snailjob-server"
)

Write-Host "========================================" -ForegroundColor Cyan
Write-Host " 构建所有微服务 Docker 镜像" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

# ============== [1/2] 清理旧镜像 ==============
Write-Host "[2/3] 清除旧镜像..." -ForegroundColor Yellow
foreach ($module in $MODULES) {
    $moduleName = Split-Path $module -Leaf
    $imageName = "$REGISTRY/${moduleName}:$IMAGE_TAG"
    $existing = docker images -q $imageName 2>$null
    if ($existing) {
        Write-Host "  删除: $imageName" -ForegroundColor Red
        docker rmi $imageName -f 2>$null
    }
}
Write-Host "  完成" -ForegroundColor Green
Write-Host ""

# ============== [2/2] 构建 Docker 镜像 ==============
Write-Host "[3/3] 构建 Docker 镜像..." -ForegroundColor Yellow
foreach ($module in $MODULES) {
    $moduleName = Split-Path $module -Leaf
    $imageName = "$REGISTRY/${moduleName}:$IMAGE_TAG"
    $contextPath = Join-Path $ProjectRoot $module

    Write-Host "  构建: $imageName" -ForegroundColor Gray
    docker build -t $imageName $contextPath
    if ($LASTEXITCODE -ne 0) {
        Write-Host "  镜像构建失败: $imageName" -ForegroundColor Red
        exit 1
    }
}
Write-Host ""

Write-Host "========================================" -ForegroundColor Green
Write-Host " 所有镜像构建完成!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green

# ============== 显示构建结果 ==============
Write-Host "构建的镜像列表:" -ForegroundColor Cyan
foreach ($module in $MODULES) {
    $moduleName = Split-Path $module -Leaf
    docker images "$REGISTRY/$moduleName" --format "table {{.Repository}}:{{.Tag}}`t{{.Size}}"
}
