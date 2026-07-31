<template>
  <div class="p-2">
    <el-card shadow="never">
      <template #header>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" plain icon="Plus" @click="handleAdd()" v-hasPermi="['guli:category:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" @click="handleBatchDelete" v-hasPermi="['guli:category:remove']">批量删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['guli:category:export']">导出</el-button>
          </el-col>
        </el-row>
      </template>

      <div class="split-layout">
        <div class="tree-pane">
          <el-tree
            ref="treeRef"
            :data="categoryList"
            node-key="catId"
            :props="{ label: 'name', children: 'children' }"
            :expand-on-click-node="false"
            show-checkbox
            highlight-current
            draggable
            :allow-drag="allowDrag"
            :allow-drop="allowDrop"
            @node-click="onNodeClick"
            @node-drop="handleDrop"
          >
            <template #default="{ node, data }">
              <div class="tree-row" :class="`tree-level-${data.catLevel}`">
                <span class="tree-label">{{ node.label }}</span>
                <span class="tree-meta">
                  <span class="meta-text">{{ data.productCount || 0 }}件</span>
                </span>
              </div>
            </template>
          </el-tree>
        </div>

        <div class="detail-pane">
          <template v-if="currentNode">
            <div class="detail-header">
              <span class="detail-title">{{ currentNode.name }}</span>
              <span class="detail-actions">
                <el-button type="primary" size="small" @click="handleAdd(currentNode)" v-hasPermi="['guli:category:add']">新增子级</el-button>
                <el-button type="primary" size="small" plain @click="handleUpdate(currentNode)" v-hasPermi="['guli:category:edit']">编辑</el-button>
                <el-button type="danger" size="small" plain @click="handleDelete(currentNode)" v-hasPermi="['guli:category:remove']">删除</el-button>
              </span>
            </div>
            <el-descriptions :column="2" border size="small">
              <el-descriptions-item label="分类ID">{{ currentNode.catId }}</el-descriptions-item>
              <el-descriptions-item label="父级ID">{{ currentNode.parentCid }}</el-descriptions-item>
              <el-descriptions-item label="层级">{{ currentNode.catLevel }}</el-descriptions-item>
              <el-descriptions-item label="排序">{{ currentNode.sort }}</el-descriptions-item>
              <el-descriptions-item label="商品数量">{{ currentNode.productCount || 0 }}</el-descriptions-item>
              <el-descriptions-item label="显示状态">
                <el-tag :type="currentNode.showStatus === 1 ? 'success' : 'info'" size="small">
                  {{ currentNode.showStatus === 1 ? '显示' : '隐藏' }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item v-if="currentNode.icon" label="图标">{{ currentNode.icon }}</el-descriptions-item>
              <el-descriptions-item v-if="currentNode.productUnit" label="计量单位">{{ currentNode.productUnit }}</el-descriptions-item>
            </el-descriptions>
          </template>
          <div v-else class="detail-placeholder">
            <el-empty description="点击左侧分类查看详情" :image-size="80" />
          </div>
        </div>
      </div>
    </el-card>

    <!-- 添加或修改商品三级分类对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="categoryFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="父分类id" prop="parentCid">
          <el-input v-model="form.parentCid" placeholder="请输入父分类id" />
        </el-form-item>
        <el-form-item label="层级" prop="catLevel">
          <el-input v-model="form.catLevel" placeholder="请输入层级" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input v-model="form.sort" placeholder="请输入排序" />
        </el-form-item>
        <el-form-item label="图标地址" prop="icon">
          <el-input v-model="form.icon" placeholder="请输入图标地址" />
        </el-form-item>
        <el-form-item label="计量单位" prop="productUnit">
          <el-input v-model="form.productUnit" placeholder="请输入计量单位" />
        </el-form-item>
        <el-form-item label="商品数量" prop="productCount">
          <el-input v-model="form.productCount" placeholder="请输入商品数量" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Category" lang="ts">
import { listTreeCategory, getCategory, delCategory, addCategory, updateCategory, updateBatchCategory } from '@/api/guli/category'
import { CategoryVO, CategoryForm } from '@/api/guli/category/types'

const { proxy } = getCurrentInstance() as ComponentInternalInstance

const categoryList = ref<CategoryVO[]>([])
const currentNode = ref<CategoryVO | null>(null)
const buttonLoading = ref(false)
const loading = ref(true)

const categoryFormRef = ref<ElFormInstance>()
const treeRef = ref()

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
})

const initFormData: CategoryForm = {
  catId: undefined,
  name: undefined,
  parentCid: undefined,
  catLevel: undefined,
  showStatus: undefined,
  sort: undefined,
  icon: undefined,
  productUnit: undefined,
  productCount: undefined
}

const form = ref<CategoryForm>({ ...initFormData })

const rules = ref({
  name: [{ required: true, message: '分类名称不能为空', trigger: 'blur' }]
})

// ========== dragger ==========

/**
 * 选中树节点以展示细节信息
 * @param data    节点数据对象
 * @param _node   TreeNode 的 node 属性
 * @param _comp   TreeNode 组件实例
 * @param _event  原生 click 事件对象
 */
const onNodeClick = (data: CategoryVO, _node: any, _comp: any, _event: Event) => {
  currentNode.value = data
}

/** 所有节点均可拖拽 */
const allowDrag = () => true

/**
 * 只允许落入同父级节点，不允许作为子节点插入
 * @param draggingNode 正在拖拽的节点
 * @param dropRefNode 拖拽参照节点
 * @param type 相对于参照节点的位置
 */
const allowDrop = (draggingNode: any, dropRefNode: any, type: string) => {
  if (type === 'inner') return false
  return draggingNode.data.parentCid === dropRefNode.data.parentCid
}

/**
 * 拖拽完成 — 同级排序或移动层级
 * @param draggingNode 被拖拽的节点
 * @param dropNode     目标参照节点
 * @param dropType     相对于参照节点的位置（'before' | 'after' | 'inner' | 'none'）
 * @param _event       原生 DragEvent（未使用）
 */
const handleDrop = (_draggingNode: any, dropNode: any, _dropType: string, _event: DragEvent) => {
  // 获取参照节点所在的父级 children 数组（根节点用 categoryList）
  const parent = dropNode.parent
  const siblings: CategoryVO[] = parent.data?.children ?? categoryList.value

  // 按新顺序重新分配 sort，收集需要更新的节点
  const batchData = siblings
    .map((node, index) => ({ node, index }))
    .filter(({ node, index }) => node.sort !== index)
    .map(({ node, index }) => {
      node.sort = index
      return { catId: node.catId, sort: index } as CategoryForm
    })

  // 空数组不执行
  if (batchData.length === 0) return

  // 更新排序
  updateBatchCategory(batchData)
    .then(() => proxy?.$modal.msgSuccess('排序调整成功'))
    .catch(() => proxy?.$modal.msgError('排序调整失败'))
}

// ========== curd ==========

/** 查询商品三级分类树列表 */
const getList = async () => {
  loading.value = true
  const res = await listTreeCategory()
  categoryList.value = res.data
  loading.value = false
}

/** 对话框取消按钮 */
const cancel = () => {
  form.value = { ...initFormData }
  categoryFormRef.value?.resetFields()
  dialog.visible = false
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('guli/category/export', {}, `category_${new Date().getTime()}.xlsx`)
}

/** 新增按钮操作 */
const handleAdd = (row?: CategoryVO) => {
  form.value = { ...initFormData }
  if (row) {
    form.value.parentCid = row.catId as number
  }
  categoryFormRef.value?.resetFields()
  dialog.visible = true
  dialog.title = '添加商品三级分类'
}

/** 修改按钮操作 */
const handleUpdate = async (row?: CategoryVO) => {
  form.value = { ...initFormData }
  const catId = row?.catId
  const res = await getCategory(catId as number)
  Object.assign(form.value, res.data)
  categoryFormRef.value?.resetFields()
  dialog.visible = true
  dialog.title = '修改商品三级分类'
}

/** 提交按钮 */
const submitForm = () => {
  categoryFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true
      if (form.value.catId) {
        await updateCategory(form.value).finally(() => (buttonLoading.value = false))
      } else {
        await addCategory(form.value).finally(() => (buttonLoading.value = false))
      }
      proxy?.$modal.msgSuccess('操作成功')
      dialog.visible = false
      await getList()
    }
  })
}

/** 删除按钮操作 */
const handleDelete = async (row?: CategoryVO) => {
  const catId = row?.catId
  await proxy?.$modal.confirm('是否确认删除商品三级分类编号为"' + catId + '"的数据项？').finally(() => (loading.value = false))
  await delCategory(catId as number)
  proxy?.$modal.msgSuccess('删除成功')
  await getList()
}

/** 批量删除勾选的节点 */
const handleBatchDelete = async () => {
  const checkedNodes = treeRef.value?.getCheckedNodes() as CategoryVO[]
  if (!checkedNodes || checkedNodes.length === 0) {
    proxy?.$modal.msgWarning('请先勾选要删除的分类')
    return
  }

  const names = checkedNodes.map((n) => n.name).join('、')
  try {
    await proxy?.$modal.confirm(`确认删除以下分类：${names}？`)
  } catch {
    return
  }

  const ids = checkedNodes.map((n) => n.catId)
  await delCategory(ids as number[])
  proxy?.$modal.msgSuccess('批量删除成功')

  await getList()
}

onMounted(() => {
  getList()
})
</script>

<style scoped lang="scss">
/* 总体div */
.split-layout {
  display: grid;
  grid-template-columns: 340px 1fr;
  min-height: 400px;
}

/* 树列表区域 */
.tree-pane {
  border-right: 1px solid var(--el-border-color-light);
  overflow-y: auto;
}

/* 展示细节区域 */
.detail-pane {
  padding: 16px 20px;

  .detail-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 16px;
    padding-bottom: 12px;
    border-bottom: 1px solid var(--el-border-color-lighter);
  }

  .detail-title {
    font-size: 18px;
    font-weight: 600;
  }

  .detail-actions {
    display: flex;
    gap: 8px;
    flex-shrink: 0;
  }

  .detail-placeholder {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 100%;
    min-height: 300px;
  }
}

/* 树单行样式 */
.tree-row {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 8px;
  padding-right: 8px;
  font-size: 14px;

  .meta-text {
    font-size: 12px;
    color: var(--el-text-color-placeholder);
  }

  &.tree-level-1 .tree-label {
    font-size: 15px;
    font-weight: 700;
  }

  &.tree-level-2 .tree-label {
    font-size: 14px;
    font-weight: 500;
  }

  &.tree-level-3 .tree-label {
    font-size: 13px;
    font-weight: 400;
    color: var(--el-text-color-secondary);
  }
}
</style>
