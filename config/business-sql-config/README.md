# business-sql-config 执行顺序说明

Guli 商城业务 SQL，需**手动导入**（不会被挂载脚本自动导入，自动导入的只有 `../base-sql-config/`）。

## 前置依赖

所有业务 SQL 均通过 `USE ry-cloud;` 写入 **`ry-cloud`** 数据库，因此必须先初始化该库：

- `../base-sql-config/ry-cloud.sql` —— 创建 `ry-cloud` 数据库及 RuoYi 平台基础表（`sys_*`、`sys_menu` 等），MySQL 容器**首次启动**时会自动执行（按文件名字母序）。
- 若在业务 SQL 之前手动导入了其它库脚本（如 `ry-config`），无影响；关键只有一条：**`ry-cloud` 库必须先存在**。
- `update/` 菜单脚本向 `sys_menu` 插入数据，`sys_menu` 同样由 `ry-cloud.sql` 提供，因此也只需 `ry-cloud` 基础库就绪即可执行。

> ⚠️ **不要导入 `gulimall_admin.sql`**：该文件是另一框架（非云版 RuoYi）的后台表结构，与 `ry-cloud.sql` 的 `sys_*` 表冲突/重复，会把 `ry-cloud` 库弄乱，**已删除**，历史文档中的导入步骤一律忽略。

## 业务 SQL 执行顺序（推荐）

| 顺序 | 文件 | 内容 | 说明 |
|------|------|------|------|
| 1 | `gulimall_pms.sql` | 商品：`pms_*`（spu / sku / 品牌 / 分类 / 属性，含数据） | 核心业务基础 |
| 2 | `gulimall_oms.sql` | 订单：`oms_*` | 依赖商品、会员 |
| 3 | `gulimall_sms.sql` | 营销/优惠：`sms_*` | 优惠券等引用 spu |
| 4 | `gulimall_ums.sql` | 会员：`ums_*` | 会员基础数据 |
| 5 | `gulimall_wms.sql` | 仓储：`wms_*` | 库存引用 sku |

### 为什么是这个顺序

- 各文件开头均有 `SET FOREIGN_KEY_CHECKS=0;`，且业务表之间**没有跨模块外键约束**，因此五个文件从数据库层面看**顺序可互换**，不会因表依赖而失败。
- 推荐顺序按**业务逻辑依赖**排列：商品(pms) → 订单(oms) / 营销(sms) / 会员(ums) → 仓储(wms)。

## update/ 菜单 SQL 顺序

`update/` 下是 RuoYi 后台**菜单初始化脚本**，均插入 `sys_menu`，**只需 `ry-cloud` 基础库初始化完成即可执行**（不依赖任何业务 SQL）：

1. `update/parentMenu.sql` —— 先建父菜单（商品管理 `2079714826489909250`、仓储管理 `2087551906857127937` 及其中间目录）。
2. `update/product/*.sql`、`update/ware/*.sql` —— 子菜单引用父菜单 ID，**必须在 parentMenu.sql 之后**执行。

`product/` 与 `ware/` 内部各文件相互独立，顺序可任意；也可按下面的层级关系分批执行：

```
商品管理(2079714826489909250)
├── 分类维护  categoryMenu.sql
├── 品牌管理  brandMenu.sql
├── 属性管理(2083926009118793729)
│   ├── 属性分组   attrGroupMenu.sql
│   └── 属性值储   keyValStoreMenu.sql
└── 商品维护(2086016318889484290)
    └── 商品发布   releaseMenu.sql

仓储管理(2087551906857127937)
├── 仓库信息  wareInfoMenu.sql
├── 商品库存  wareSkuMenu.sql
└── 采购维护(2087569641141997570)
    ├── 采购信息    purchaseMenu.sql
    └── 采购单详情  purchaseDetailMenu.sql
```

## 导入命令示例

```bash
# 以 ry-cloud 业务库为例（需先启动 MySQL 容器并完成 base-sql-config 初始化）
docker exec -i guli-mysql mysql -uroot -ppassword < config/business-sql-config/gulimall_pms.sql
docker exec -i guli-mysql mysql -uroot -ppassword < config/business-sql-config/gulimall_oms.sql
docker exec -i guli-mysql mysql -uroot -ppassword < config/business-sql-config/gulimall_sms.sql
docker exec -i guli-mysql mysql -uroot -ppassword < config/business-sql-config/gulimall_ums.sql
docker exec -i guli-mysql mysql -uroot -ppassword < config/business-sql-config/gulimall_wms.sql

# 菜单脚本（依赖 sys_menu，来自 ry-cloud.sql）
docker exec -i guli-mysql mysql -uroot -ppassword < config/business-sql-config/update/parentMenu.sql
docker exec -i guli-mysql mysql -uroot -ppassword < config/business-sql-config/update/product/brandMenu.sql
# ... 其余 product/、ware/ 子菜单文件同理
```
