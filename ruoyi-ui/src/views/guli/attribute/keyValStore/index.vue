<template>
  <div class="split-layout p-2">
    <el-card shadow="never" class="tree-card">
      <div class="tree-wrapper">
        <GuliProductTree :tree-data="treeData" :is-only-read="true" @clickedNodeData="onTreeClick" />
      </div>
    </el-card>
    <div class="table-div">
      <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
        <div v-show="showSearch" class="mb-[10px]">
          <el-card shadow="hover">
            <el-form ref="queryFormRef" :model="queryParams" :inline="true">
              <el-form-item label="所属分类" prop="catelogId">
                <el-input :model-value="selectedCategoryName" placeholder="点击左侧以选择" readonly disabled />
              </el-form-item>
              <el-form-item label="属性名" prop="attrName">
                <el-input v-model="queryParams.attrName" placeholder="请输入属性名" clearable @keyup.enter="handleQuery" />
              </el-form-item>
              <el-form-item label="属性类型" prop="attrType">
                <el-select v-model="queryParams.attrType" placeholder="请选择属性类型" clearable>
                  <el-option v-for="o in attrTypeOptions" :key="o.value" :label="o.label" :value="o.value" />
                </el-select>
              </el-form-item>
              <el-form-item label="快速展示" prop="showDesc">
                <el-select v-model="queryParams.showDesc" placeholder="请选择快速展示" clearable>
                  <el-option v-for="o in showDescOptions" :key="o.value" :label="o.label" :value="o.value" />
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
                <el-button icon="Refresh" @click="resetQuery">重置</el-button>
              </el-form-item>
            </el-form>
          </el-card>
        </div>
      </transition>

      <el-card shadow="never">
        <template #header>
          <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
              <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['guli:keyValStore:add']">新增</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['guli:keyValStore:edit']"
                >修改</el-button
              >
            </el-col>
            <el-col :span="1.5">
              <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['guli:keyValStore:remove']"
                >删除</el-button
              >
            </el-col>
            <el-col :span="1.5">
              <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['guli:keyValStore:export']">导出</el-button>
            </el-col>
            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
          </el-row>
        </template>

        <el-table v-loading="loading" border :data="keyValStoreList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column label="属性名" align="center" prop="attrName" />
          <el-table-column label="值类型" align="center" prop="valueType">
            <template #default="scope">
              <DictTag :options="valueTypeOptions" :value="scope.row.valueType" />
            </template>
          </el-table-column>
          <el-table-column label="属性图标" align="center" prop="icon" />
          <el-table-column label="可选值列表" align="center" prop="valueSelect" />
          <el-table-column label="属性类型" align="center" prop="attrType">
            <template #default="scope">
              <DictTag :options="attrTypeOptions" :value="scope.row.attrType" />
            </template>
          </el-table-column>
          <el-table-column label="启用状态" align="center" prop="enable">
            <template #default="scope">
              <DictTag :options="enableOptions" :value="scope.row.enable" />
            </template>
          </el-table-column>
          <el-table-column label="快速展示" align="center" prop="showDesc">
            <template #default="scope">
              <DictTag :options="showDescOptions" :value="scope.row.showDesc" />
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" fixed="right" class-name="small-padding fixed-width">
            <template #default="scope">
              <el-tooltip content="修改" placement="top">
                <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['guli:keyValStore:edit']"></el-button>
              </el-tooltip>
              <el-tooltip content="删除" placement="top">
                <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['guli:keyValStore:remove']"></el-button>
              </el-tooltip>
            </template>
          </el-table-column>
        </el-table>

        <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
      </el-card>

      <!-- 添加或修改商品属性对话框 -->
      <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
        <el-form ref="keyValStoreFormRef" :model="form" :rules="rules" label-width="80px">
          <el-form-item label="属性名" prop="attrName">
            <el-input v-model="form.attrName" placeholder="请输入属性名" />
          </el-form-item>
          <el-form-item label="属性图标" prop="icon">
            <el-input v-model="form.icon" placeholder="请输入属性图标" />
          </el-form-item>
          <el-form-item label="可选值列表" prop="valueSelect">
            <el-input v-model="form.valueSelect" placeholder="多个值用逗号分隔" />
          </el-form-item>
          <el-form-item label="启用状态" prop="enable">
            <el-input v-model="form.enable" placeholder="0-禁用, 1-启用" />
          </el-form-item>
          <el-form-item label="所属分类" prop="catelogId">
            <el-tree-select
              v-model="form.catelogId"
              :data="treeSelectData"
              node-key="catId"
              :props="{ label: 'name', children: 'children' }"
              placeholder="请选择所属分类"
              check-strictly
              clearable
            />
          </el-form-item>
          <el-form-item label="快速展示" prop="showDesc">
            <el-input v-model="form.showDesc" placeholder="0-否, 1-是" />
          </el-form-item>
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
            <el-button @click="cancel">取 消</el-button>
          </div>
        </template>
      </el-dialog>

      <!-- 关联对话框 -->
      <el-dialog :title="relationDialog.title" v-model="relationDialog.visible" width="600px" append-to-body>
        <el-empty description="暂无关联数据" />
      </el-dialog>
    </div>
  </div>
</template>

<script setup name="KeyValStore" lang="ts">
import {
  listKeyValStore,
  getKeyValStore,
  delKeyValStore,
  addKeyValStore,
  updateKeyValStore,
  listTreeCategory
} from '@/api/guli/attribute/keyValStore'
import { KeyValStoreVO, KeyValStoreQuery, KeyValStoreForm } from '@/api/guli/attribute/keyValStore/types'
import type { CategoryVO } from '@/api/guli/category/types'
import type { Node } from 'element-plus/es/components/tree/src/model/node'

const { proxy } = getCurrentInstance() as ComponentInternalInstance

// 字典映射
const valueTypeOptions = [
  { value: '0', label: '单个值' },
  { value: '1', label: '多值', elTagType: 'warning' as const }
]
const attrTypeOptions = [
  { value: '0', label: '销售属性', elTagType: 'danger' as const },
  { value: '1', label: '基本属性' },
  { value: '2', label: '双重属性', elTagType: 'warning' as const }
]
const enableOptions = [
  { value: '0', label: '禁用', elTagType: 'info' as const },
  { value: '1', label: '启用', elTagType: 'success' as const }
]
const showDescOptions = [
  { value: '0', label: '否', elTagType: 'info' as const },
  { value: '1', label: '是', elTagType: 'success' as const }
]

const selectedCategoryName = ref('点击左侧以选择')
const treeSelectData = ref<CategoryVO[]>([])
const treeData = ref<CategoryVO[]>([])
const keyValStoreList = ref<KeyValStoreVO[]>([])

const buttonLoading = ref(false)
const loading = ref(true)
const showSearch = ref(true)

const single = ref(true)
const multiple = ref(true)
const total = ref(0)

const ids = ref<Array<string | number>>([])

const queryFormRef = ref<ElFormInstance>()
const keyValStoreFormRef = ref<ElFormInstance>()

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
})

const relationDialog = reactive<DialogOption>({
  visible: false,
  title: '属性关联'
})

const initFormData: KeyValStoreForm = {
  attrId: undefined,
  attrName: undefined,
  searchType: undefined,
  valueType: undefined,
  icon: undefined,
  valueSelect: undefined,
  attrType: undefined,
  enable: undefined,
  catelogId: undefined,
  showDesc: undefined
}
const data = reactive<PageData<KeyValStoreForm, KeyValStoreQuery>>({
  form: { ...initFormData },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    catelogId: undefined,
    attrName: undefined,
    params: {}
  },
  rules: {
    attrId: [{ required: true, message: '属性id不能为空', trigger: 'blur' }]
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 树节点被点击 */
const onTreeClick = (data: CategoryVO, nodePath: Node[]) => {
  queryParams.value.catelogId = data.catId
  selectedCategoryName.value = [...nodePath]
    .reverse()
    .map((n) => (n.data as CategoryVO).name)
    .join('/')
}

/** 查询商品属性列表 */
const getList = async () => {
  if (!queryParams.value.catelogId) {
    keyValStoreList.value = []
    total.value = 0
    loading.value = false
    return
  }
  loading.value = true
  const res = await listKeyValStore(queryParams.value)
  keyValStoreList.value = res.rows
  total.value = res.total
  loading.value = false
}

/** 取消按钮 */
const cancel = () => {
  reset()
  dialog.visible = false
}

/** 表单重置 */
const reset = () => {
  form.value = { ...initFormData }
  keyValStoreFormRef.value?.resetFields()
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value?.resetFields()
  handleQuery()
}

/** 多选框选中数据 */
const handleSelectionChange = (selection: KeyValStoreVO[]) => {
  ids.value = selection.map((item) => item.attrId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
const handleAdd = async () => {
  reset()
  const res = await listTreeCategory()
  treeSelectData.value = res.data
  dialog.visible = true
  dialog.title = '添加商品属性'
}

/** 修改按钮操作 */
const handleUpdate = async (row?: KeyValStoreVO) => {
  reset()
  const treeRes = await listTreeCategory()
  treeSelectData.value = treeRes.data
  const _attrId = row?.attrId || ids.value[0]
  const res = await getKeyValStore(_attrId)
  Object.assign(form.value, res.data)
  dialog.visible = true
  dialog.title = '修改商品属性'
}

/** 提交按钮 */
const submitForm = () => {
  keyValStoreFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true
      if (form.value.attrId) {
        await updateKeyValStore(form.value).finally(() => (buttonLoading.value = false))
      } else {
        await addKeyValStore(form.value).finally(() => (buttonLoading.value = false))
      }
      proxy?.$modal.msgSuccess('操作成功')
      dialog.visible = false
      await getList()
    }
  })
}

/** 删除按钮操作 */
const handleDelete = async (row?: KeyValStoreVO) => {
  const _attrIds = row?.attrId || ids.value
  await proxy?.$modal.confirm('是否确认删除商品属性编号为"' + _attrIds + '"的数据项？').finally(() => (loading.value = false))
  await delKeyValStore(_attrIds)
  proxy?.$modal.msgSuccess('删除成功')
  await getList()
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download(
    'guli/attribute/keyValStore/export',
    {
      ...queryParams.value
    },
    `keyValStore_${new Date().getTime()}.xlsx`
  )
}

onMounted(async () => {
  const res = await listTreeCategory()
  treeData.value = res.data
  getList()
})
</script>

<style scoped lang="scss">
.split-layout {
  display: grid;
  grid-template-columns: 25% 1fr;
  gap: 12px;
  height: 100vh;
  overflow: hidden;
}

.tree-card {
  height: 100%;
  overflow: hidden;
}

.tree-wrapper {
  height: 100%;
  contain: size;
  overflow-y: auto;
}

.table-div {
  display: grid;
  grid-template-rows: auto 1fr;
  gap: 12px;
  min-height: 0;
  overflow-y: auto;

  .el-card {
    overflow: auto;
  }
}
</style>
