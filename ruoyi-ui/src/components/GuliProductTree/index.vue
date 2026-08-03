<template>
  <div class="guli-tree-pane">
    <el-tree
      ref="treeRef"
      :data="treeData"
      node-key="catId"
      :props="{ label: 'name', children: 'children' }"
      :expand-on-click-node="false"
      :show-checkbox="isShowCheckbox"
      highlight-current
      :draggable="isDraggable"
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
</template>

<script setup lang="ts">
import { listTreeCategory, getCategory, addCategory, updateCategory, delCategory, updateBatchCategory } from './index'
import type { CategoryVO, CategoryForm } from './index'

const props = withDefaults(
  defineProps<{
    isShowCheckbox?: boolean
    isDraggable?: boolean
  }>(),
  { isShowCheckbox: true, isDraggable: true }
)

const emit = defineEmits<{ clickedNodeData: [data: CategoryVO] }>()

const { proxy } = getCurrentInstance() as ComponentInternalInstance
const treeRef = ref()
const treeData = ref<CategoryVO[]>([])

// ==================== 内部数据 ====================

const loadTree = async () => {
  const res = await listTreeCategory()
  treeData.value = res.data
}

const onNodeClick = (data: CategoryVO) => {
  emit('clickedNodeData', data)
}

// ==================== 拖拽相关 ====================

const allowDrag = () => true

const allowDrop = (_draggingNode: any, dropRefNode: any, type: string) => {
  if (type === 'inner') return false
  return _draggingNode.data.parentCid === dropRefNode.data.parentCid
}

const handleDrop = (_draggingNode: any, dropNode: any, _dropType: string) => {
  const parent = dropNode.parent
  const siblings: CategoryVO[] = parent.data?.children ?? treeData.value
  const batchData = siblings
    .map((node, index) => ({ node, index }))
    .filter(({ node, index }) => node.sort !== index)
    .map(({ node, index }) => {
      const form = { ...node, sort: index } as CategoryForm
      return form
    })
  if (batchData.length === 0) return
  updateBatchCategory(batchData)
    .then(() => proxy?.$modal.msgSuccess('排序调整成功'))
    .catch(() => proxy?.$modal.msgError('排序调整失败'))
}

// ====================  对外暴露的CRUD ====================

/** 根据 ID 查询单个分类 */
const getById = async (catId: number) => {
  const res = await getCategory(catId)
  return res.data
}

/** 新增分类 */
const add = async (form: CategoryForm) => {
  await addCategory(form)
  proxy?.$modal.msgSuccess('新增成功')
  await loadTree()
}

/** 修改分类 */
const update = async (form: CategoryForm) => {
  await updateCategory(form)
  proxy?.$modal.msgSuccess('修改成功')
  await loadTree()
}

/** 删除分类（单个或批量） */
const remove = async (ids: number | number[]) => {
  const idArr = Array.isArray(ids) ? ids : [ids]
  try {
    await proxy?.$modal.confirm('是否确认删除所选分类？')
  } catch {
    return
  }
  await delCategory(idArr as number[])
  proxy?.$modal.msgSuccess('删除成功')
  await loadTree()
}

/** 批量删除当前勾选的节点 */
const removeBatch = async () => {
  const checked = treeRef.value?.getCheckedNodes() as CategoryVO[]
  if (!checked || checked.length === 0) {
    proxy?.$modal.msgWarning('请先勾选要删除的分类')
    return
  }
  await remove(checked.map((n) => n.catId as number))
}

// 暴露方法点
defineExpose({ loadTree, getById, add, update, remove, removeBatch })

onMounted(() => loadTree())
</script>

<style lang="scss" scoped>
.guli-tree-pane {
  border-right: 1px solid var(--el-border-color-light);
  overflow-y: auto;
}

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
