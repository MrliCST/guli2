<template>
  <div class="guli-tree-pane">
    <el-tree
      ref="treeRef"
      :data="treeData"
      node-key="catId"
      :props="{ label: 'name', children: 'children' }"
      :expand-on-click-node="false"
      :show-checkbox="!isOnlyRead"
      highlight-current
      :draggable="!isOnlyRead"
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
import type { Node } from 'element-plus/es/components/tree/src/model/node'
import type { CategoryVO, CategoryForm } from '@/api/guli/product/category/types'

const props = withDefaults(
  defineProps<{
    treeData: CategoryVO[]
    isOnlyRead?: boolean
    onReady?: (api: { getChosenNode: () => CategoryVO[] }) => void
  }>(),
  { isOnlyRead: false }
)

const emit = defineEmits<{
  clickedNodeData: [data: CategoryVO, nodePath: Node[]]
  dragDrop: [data: CategoryForm[]]
}>()

const treeRef = ref()

// ==================== 获取选中节点 ====================

const getChosenNode = (): CategoryVO[] => {
  return treeRef.value?.getCheckedNodes() ?? []
}

// ==================== 节点点击 ====================

const onNodeClick = (data: CategoryVO, node: Node, _nodeInstance: any, _evt: MouseEvent) => {
  const nodePath: Node[] = []
  let current: Node | null = node
  while (current && current.level > 0) {
    nodePath.push(current)
    current = current.parent
  }
  emit('clickedNodeData', data, nodePath)
}

// ==================== 拖拽相关 ====================

const allowDrag = () => !props.isOnlyRead

const allowDrop = (_draggingNode: any, dropRefNode: any, type: string) => {
  if (type === 'inner') return false
  return _draggingNode.data.parentCid === dropRefNode.data.parentCid
}

const handleDrop = (_draggingNode: any, dropNode: any, _dropType: string) => {
  const parent = dropNode.parent
  const siblings: CategoryVO[] = parent.data?.children ?? props.treeData
  const batchData = siblings
    .map((node, index) => ({ node, index }))
    .filter(({ node, index }) => node.sort !== index)
    .map(({ node, index }) => {
      const form = { ...node, sort: index } as CategoryForm
      return form
    })
  if (batchData.length === 0) return
  emit('dragDrop', batchData)
}

// ==================== 生命周期 ====================

onMounted(() => {
  props.onReady?.({ getChosenNode })
})
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
