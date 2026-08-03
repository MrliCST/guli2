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
        <GuliProductTree ref="treeComponentRef" @clickedNodeData="onTreeNodeClick" />

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
                <DictTag :options="showStatusOptions" :value="currentNode.showStatus" />
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
import type { CategoryVO, CategoryForm } from '@/components/GuliProductTree/index'

const { proxy } = getCurrentInstance() as ComponentInternalInstance

const showStatusOptions = [
  { value: '0', label: '隐藏', elTagType: 'info' as const },
  { value: '1', label: '显示', elTagType: 'success' as const }
]

const currentNode = ref<CategoryVO | null>(null)
const buttonLoading = ref(false)

// GuliProductTree组件引用, 获取暴露点方法
const treeComponentRef = ref()
const categoryFormRef = ref<ElFormInstance>()

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

/** 树节点被点击 → 显示详情 */
const onTreeNodeClick = (data: CategoryVO) => {
  currentNode.value = data
}

// ========== 按钮回调 ==========

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
  const data = await treeComponentRef.value?.getById(catId as number)
  Object.assign(form.value, data)
  categoryFormRef.value?.resetFields()
  dialog.visible = true
  dialog.title = '修改商品三级分类'
}

/** 提交按钮 */
const submitForm = () => {
  categoryFormRef.value?.validate(async (valid: boolean) => {
    if (!valid) return
    buttonLoading.value = true
    const api = treeComponentRef.value
    if (form.value.catId) {
      await api.update(form.value).finally(() => (buttonLoading.value = false))
    } else {
      await api.add(form.value).finally(() => (buttonLoading.value = false))
    }
    dialog.visible = false
  })
}

/** 删除按钮操作 */
const handleDelete = (row?: CategoryVO) => {
  treeComponentRef.value?.remove(row?.catId as number)
}

/** 批量删除 */
const handleBatchDelete = () => {
  treeComponentRef.value?.removeBatch()
}
</script>

<style scoped lang="scss">
/* 总体div */
.split-layout {
  display: grid;
  grid-template-columns: 340px 1fr;
  min-height: 400px;
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
</style>
