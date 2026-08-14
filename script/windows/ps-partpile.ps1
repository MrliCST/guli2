# ==========================================
# 单个模块：编译 → 验证 JAR → 删旧镜像 → 构建新镜像
# 注意该文件是 utf8 with bom 的编码格式
# ==========================================

$PSDefaultParameterValues['Out-File:Encoding'] = 'utf8'
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
[Console]::InputEncoding = [System.Text.Encoding]::UTF8
$ErrorActionPreference = "Stop"

$ProjectRoot = Split-Path -Parent $PSScriptRoot
$IMAGE_TAG = "2.6.2"
$REGISTRY = "ruoyi"

# 可用模块（与 ps-compile.ps1 / ps-imaging.ps1 对齐）
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

# ---- 显示模块列表 ----
Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host " 单个模块重编译打包" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "可选模块:" -ForegroundColor Yellow
for ($i = 0; $i -lt $MODULES.Count; $i++) {
    $name = Split-Path $MODULES[$i] -Leaf
    $num = "{0,2}" -f ($i + 1)
    Write-Host "  $num. $name" -ForegroundColor White
}
Write-Host ""

# ---- 用户选择 ----
$choice = Read-Host "输入序号 (1-$($MODULES.Count))"
try {
    $idx = [int]$choice - 1
    if ($idx -lt 0 -or $idx -ge $MODULES.Count) {
        Write-Host "无效序号: $choice" -ForegroundColor Red
        exit 1
    }
} catch {
    Write-Host "无效输入: $choice" -ForegroundColor Red
    exit 1
}

$selectedModule = $MODULES[$idx]
$moduleName = Split-Path $selectedModule -Leaf
$modulePath = Join-Path $ProjectRoot $selectedModule
$jarPath = Join-Path $modulePath "target\$moduleName.jar"
$imageName = "$REGISTRY/${moduleName}:$IMAGE_TAG"

Write-Host ""
Write-Host "选中: $moduleName" -ForegroundColor Cyan
Write-Host "  路径: $selectedModule" -ForegroundColor DarkGray
Write-Host "  镜像: $imageName" -ForegroundColor DarkGray
Write-Host ""

# ==========================================
# [1/5] 清理 target
# ==========================================
Write-Host "[1/5] 清理 target..." -ForegroundColor Yellow
$targetDir = Join-Path $modulePath "target"
if (Test-Path $targetDir) {
    Remove-Item -Recurse -Force $targetDir
    Write-Host "  已删除: $selectedModule\target" -ForegroundColor DarkGray
} else {
    Write-Host "  无需清理" -ForegroundColor DarkGray
}
Write-Host ""

# ==========================================
# [2/5] Maven 编译打包
# ==========================================
# -pl 指定模块, -am 同时构建依赖模块, -DskipTests 跳过测试
Write-Host "[2/5] 编译打包 $moduleName ..." -ForegroundColor Yellow
$start = Get-Date
Set-Location $ProjectRoot
$env:MAVEN_OPTS = "-Dfile.encoding=UTF-8"
mvn clean package -pl "$selectedModule" -am -DskipTests "-Dfile.encoding=UTF-8"
if ($LASTEXITCODE -ne 0) {
    Write-Host "  编译失败!" -ForegroundColor Red
    exit 1
}
$elapsed = [math]::Round(((Get-Date) - $start).TotalSeconds)
Write-Host "  完成 (${elapsed}s)" -ForegroundColor Green
Write-Host ""

# ==========================================
# [3/5] 验证 JAR
# ==========================================
Write-Host "[3/5] 验证 JAR..." -ForegroundColor Yellow
if (-not (Test-Path -LiteralPath $jarPath)) {
    Write-Host "  失败 - JAR 不存在: $jarPath" -ForegroundColor Red
    exit 1
}
$jarSize = (Get-Item $jarPath).Length
if ($jarSize -eq 0) {
    Write-Host "  失败 - JAR 为空" -ForegroundColor Red
    exit 1
}
Write-Host "  通过 ($jarSize bytes)" -ForegroundColor Green
Write-Host ""

# ==========================================
# [4/5] 删除旧 Docker 镜像
# ==========================================
Write-Host "[4/5] 删除旧镜像..." -ForegroundColor Yellow
$existing = docker images -q $imageName 2>$null
if ($existing) {
    Write-Host "  删除: $imageName" -ForegroundColor Red
    docker rmi $imageName -f 2>$null
} else {
    Write-Host "  无旧镜像" -ForegroundColor DarkGray
}
Write-Host ""

# ==========================================
# [5/5] 构建 Docker 镜像
# ==========================================
Write-Host "[5/5] 构建 Docker 镜像..." -ForegroundColor Yellow
docker build -t $imageName $modulePath
if ($LASTEXITCODE -ne 0) {
    Write-Host "  镜像构建失败!" -ForegroundColor Red
    exit 1
}
Write-Host ""

# ---- 结果 ----
Write-Host "========================================" -ForegroundColor Green
Write-Host " 完成!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
docker images $imageName --format "table {{.Repository}}:{{.Tag}}`t{{.Size}}"
