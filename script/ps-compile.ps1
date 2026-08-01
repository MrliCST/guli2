$PSDefaultParameterValues['Out-File:Encoding'] = 'utf8'
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
[Console]::InputEncoding = [System.Text.Encoding]::UTF8
$ErrorActionPreference = "Stop"

# ==========================================
# Maven 编译打包 — 仅 ruoyi 微服务模块
# 不包含 guli-mall 业务模块
# ==========================================

# 项目根目录（脚本在 script/ 下）
$ProjectRoot = Split-Path -Parent $PSScriptRoot

# ruoyi 微服务模块（需产出 JAR，与 ps-buildimage.ps1 对齐）
$SERVICE_MODULES = @(
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

# 设置 JVM 文件编码为 UTF-8，防止 MapStruct 注解处理器用系统 GBK 编码生成
# Java 文件，导致 maven-compiler-plugin（配置为 UTF-8）读取时报"读取文件时出错"
$env:MAVEN_OPTS = "-Dfile.encoding=UTF-8"

Set-Location $ProjectRoot

# Maven 输出量较大，直接输出到终端可能导致截断或被 IDE/CICD 误判为超时 kill。
# 将所有构建日志重定向到 compile.log，终端只展示进度摘要。
$LogFile = Join-Path $ProjectRoot "compile.log"
"" | Out-File $LogFile -Encoding utf8

Write-Host "========================================" -ForegroundColor Cyan
Write-Host " Maven 编译打包（仅 ruoyi 微服务）" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

# ==========================================
# [预处理] 验证指定模块路径 & 清理 target
# ==========================================
# maven-clean-plugin:2.5 在 Windows 上偶发删不干净，导致增量编译残留、
# JAR 打包缺文件。先暴力清一遍所有 target，兜底保证干净。
Write-Host "[预处理] 验证模块路径 & 清理 target..." -ForegroundColor Yellow

foreach ($module in $SERVICE_MODULES) {
    $modulePath = Join-Path $ProjectRoot $module
    if (-not (Test-Path -LiteralPath $modulePath)) {
        Write-Host "  模块路径不存在: $module" -ForegroundColor Red
        exit 1
    }
}

# 递归清所有 target（包括依赖模块）
$targetDirs = Get-ChildItem -Path $ProjectRoot -Directory -Recurse -Filter "target" -ErrorAction SilentlyContinue
$cleanedCount = 0
foreach ($t in $targetDirs) {
    if ($t.FullName -like "*guliMailPlus*") {
        Remove-Item -Recurse -Force $t.FullName -ErrorAction SilentlyContinue
        $relativePath = $t.FullName.Substring($ProjectRoot.Length + 1)
        Write-Host "  [CLEAN] $relativePath" -ForegroundColor DarkGray
        $cleanedCount++
    }
}
Write-Host "  已清理 $cleanedCount 个 target 目录" -ForegroundColor Green
Write-Host ""

# ==========================================
# [1/2] Maven 编译打包
# ==========================================
# -pl "!guli-mall": 排除 guli 业务模块
# --fail-at-end: 即使 guli 模块失败也不阻断 ruoyi 模块
Write-Host "[1/2] 编译打包中... (排除 guli-mall)" -ForegroundColor Yellow -NoNewline
$start = Get-Date
mvn clean package "-pl" "!guli-mall" "-DskipTests" "-Dfile.encoding=UTF-8" >> $LogFile 2>&1
if ($LASTEXITCODE -ne 0) {
    Write-Host " 失败" -ForegroundColor Red
    Write-Host "查看日志: $LogFile" -ForegroundColor Red
    exit 1
}
$elapsed = [math]::Round(((Get-Date) - $start).TotalSeconds)
Write-Host " 完成 (${elapsed}s)" -ForegroundColor Green
Write-Host ""

# ==========================================
# [2/2] 验证 JAR
# ==========================================
Write-Host "[2/2] 验证 JAR..." -ForegroundColor Yellow
$hasError = $false
"`n=== JAR 验证 ===" >> $LogFile




foreach ($module in $SERVICE_MODULES) {
    $moduleName = Split-Path $module -Leaf
    $jarPath = Join-Path $ProjectRoot "$module\target\$moduleName.jar"
    $classesDir = Join-Path $ProjectRoot "$module\target\classes"

    # --- 1. 检查 JAR 存在且非空 ---
    if (-not (Test-Path -LiteralPath $jarPath)) {
        Write-Host "  缺失 $moduleName" -ForegroundColor Red
        "缺失 $moduleName" >> $LogFile
        $hasError = $true
        continue
    }
    $jarSize = (Get-Item $jarPath).Length
    if ($jarSize -eq 0) {
        Write-Host "  失败 $moduleName - JAR 为空" -ForegroundColor Red
        "失败 $moduleName - JAR 为空" >> $LogFile
        $hasError = $true
        continue
    }

    Write-Host "  通过 $moduleName ($jarSize bytes)" -ForegroundColor Green
    "通过 $moduleName ($jarSize bytes)" >> $LogFile
}

if ($hasError) {
    Write-Host ""
    Write-Host "JAR 验证失败!" -ForegroundColor Red
    "验证失败" >> $LogFile
    exit 1
}
Write-Host "全部通过" -ForegroundColor Green
"全部通过" >> $LogFile
