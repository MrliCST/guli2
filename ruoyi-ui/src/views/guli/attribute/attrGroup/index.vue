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
              <el-form-item label="组名" prop="attrGroupName">
                <el-input v-model="queryParams.attrGroupName" placeholder="请输入组名" clearable @keyup.enter="handleQuery" />
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
              <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['guli:attrGroup:add']">新增</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['guli:attrGroup:edit']"
                >修改</el-button
              >
            </el-col>
            <el-col :span="1.5">
              <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['guli:attrGroup:remove']"
                >删除</el-button
              >
            </el-col>
            <el-col :span="1.5">
              <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['guli:attrGroup:export']">导出</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button type="primary" plain icon="Connection" @click="handleRelation" :disabled="single" v-hasPermi="['guli:attrGroup:edit']"
                >关联</el-button
              >
            </el-col>
            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
          </el-row>
        </template>

        <el-table v-loading="loading" border :data="attrGroupList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column label="组名" align="center" prop="attrGroupName" />
          <el-table-column label="排序" align="center" prop="sort" />
          <el-table-column label="描述" align="center" prop="descript" />
          <el-table-column label="组图标" align="center" prop="icon" />
          <el-table-column label="所属分类id" align="center" prop="catelogId" />
          <el-table-column label="操作" align="center" fixed="right" class-name="small-padding fixed-width">
            <template #default="scope">
              <el-tooltip content="修改" placement="top">
                <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['guli:attrGroup:edit']"></el-button>
              </el-tooltip>
              <el-tooltip content="删除" placement="top">
                <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['guli:attrGroup:remove']"></el-button>
              </el-tooltip>
            </template>
          </el-table-column>
        </el-table>

        <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
      </el-card>

      <!-- 添加或修改属性分组对话框 -->
      <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
        <el-form ref="attrGroupFormRef" :model="form" :rules="rules" label-width="80px">
          <el-form-item label="组名" prop="attrGroupName">
            <el-input v-model="form.attrGroupName" placeholder="请输入组名" />
          </el-form-item>
          <el-form-item label="排序" prop="sort">
            <el-input v-model="form.sort" placeholder="请输入排序" />
          </el-form-item>
          <el-form-item label="描述" prop="descript">
            <el-input v-model="form.descript" placeholder="请输入描述" />
          </el-form-item>
          <el-form-item label="组图标" prop="icon">
            <el-input v-model="form.icon" placeholder="请输入组图标" />
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
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
            <el-button @click="cancel">取 消</el-button>
          </div>
        </template>
      </el-dialog>

      <!-- 属性分组关联对话框 -->
      <el-dialog :title="relationDialog.title" v-model="relationDialog.visible" width="800px" append-to-body @opened="getRelationList">
        <el-button type="primary" plain icon="Plus" class="mb8" @click="handleOpenAddRelation">新增关联</el-button>

        <el-table v-loading="relationLoading" border :data="relationList">
          <el-table-column label="关联ID" align="center" prop="id" />
          <el-table-column label="属性名" align="center" prop="attrName" />
          <el-table-column label="排序" align="center" prop="attrSort" />
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template #default="scope">
              <el-tooltip content="编辑" placement="top">
                <el-button link type="primary" icon="Edit" @click="handleOpenEditRelation(scope.row)"></el-button>
              </el-tooltip>
              <el-tooltip content="删除" placement="top">
                <el-button link type="danger" icon="Delete" @click="handleDeleteRelation(scope.row)"></el-button>
              </el-tooltip>
            </template>
          </el-table-column>
        </el-table>
      </el-dialog>

      <!-- 新增/编辑关联对话框 -->
      <el-dialog :title="addRelationDialog.title" v-model="addRelationDialog.visible" width="500px" append-to-body>
        <el-form label-width="80px">
          <el-form-item label="属性分组">
            <el-input :model-value="currentAttrGroup?.attrGroupName" disabled />
          </el-form-item>
          <el-form-item label="属性值储">
            <el-select v-model="addAttrId" placeholder="请选择属性" clearable style="width: 100%" :disabled="isEditMode">
              <el-option v-for="attr in availableAttrs" :key="attr.attrId" :label="attr.attrName" :value="attr.attrId" />
            </el-select>
          </el-form-item>
          <el-form-item label="排序号">
            <el-input v-model="addAttrSort" placeholder="请输入排序号" />
          </el-form-item>
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button type="primary" :disabled="!addAttrId" @click="handleSubmitRelation">确 定</el-button>
            <el-button @click="addRelationDialog.visible = false">取 消</el-button>
          </div>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script setup name="AttrGroup" lang="ts">
import { listAttrGroup, getAttrGroup, delAttrGroup, addAttrGroup, updateAttrGroup, listTreeCategory } from '@/api/guli/attribute/attrGroup'
import { AttrGroupVO, AttrGroupQuery, AttrGroupForm } from '@/api/guli/attribute/attrGroup/types'
import type { CategoryVO } from '@/api/guli/category/types'
import type { Node } from 'element-plus/es/components/tree/src/model/node'
import { listRelations, addRelation, updateRelation, delRelation, listAvailableAttrs } from '@/api/guli/attribute/attrGroup'
import type { AttrAttrgroupRelationVO } from '@/api/guli/attribute/attrGroup/types'
import type { KeyValStoreVO } from '@/api/guli/attribute/keyValStore/types'

const { proxy } = getCurrentInstance() as ComponentInternalInstance

const buttonLoading = ref(false)
const loading = ref(true)
const showSearch = ref(true)

// 单选/多选
const single = ref(true)
const multiple = ref(true)
const total = ref(0)

const selectedCategoryName = ref('点击左侧以选择')
const treeSelectData = ref<CategoryVO[]>([])
const treeData = ref<CategoryVO[]>([])
const attrGroupList = ref<AttrGroupVO[]>([])
const ids = ref<Array<string | number>>([])

const queryFormRef = ref<ElFormInstance>()
const attrGroupFormRef = ref<ElFormInstance>()

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
})

const initFormData: AttrGroupForm = {
  attrGroupId: undefined,
  attrGroupName: undefined,
  sort: undefined,
  descript: undefined,
  icon: undefined,
  catelogId: undefined
}
const data = reactive<PageData<AttrGroupForm, AttrGroupQuery>>({
  form: { ...initFormData },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    catelogId: undefined,
    attrGroupName: undefined,
    params: {}
  },
  rules: {}
})

const { queryParams, form, rules } = toRefs(data)

/** 树节点被点击 */
const onTreeClick = (data: CategoryVO, nodePath: Node[]) => {
  queryParams.value.catelogId = data.catId
  // nodePath: [叶子, 父, 祖父, ...]
  selectedCategoryName.value = nodePath
    .reverse()
    .map((n) => (n.data as CategoryVO).name)
    .join('/')
}

/** 查询属性分组列表 */
const getList = async () => {
  if (!queryParams.value.catelogId) {
    attrGroupList.value = []
    total.value = 0
    loading.value = false
    return
  }
  loading.value = true
  const res = await listAttrGroup(queryParams.value)
  attrGroupList.value = res.rows
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
  attrGroupFormRef.value?.resetFields()
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
const handleSelectionChange = (selection: AttrGroupVO[]) => {
  ids.value = selection.map((item) => item.attrGroupId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
const handleAdd = async () => {
  reset()
  const res = await listTreeCategory()
  treeSelectData.value = res.data
  dialog.visible = true
  dialog.title = '添加属性分组'
}

/** 修改按钮操作 */
const handleUpdate = async (row?: AttrGroupVO) => {
  reset()
  const treeRes = await listTreeCategory()
  treeSelectData.value = treeRes.data
  const _attrGroupId = row?.attrGroupId || ids.value[0]
  const res = await getAttrGroup(_attrGroupId)
  Object.assign(form.value, res.data)
  dialog.visible = true
  dialog.title = '修改属性分组'
}

/** 提交按钮 */
const submitForm = () => {
  attrGroupFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true
      if (form.value.attrGroupId) {
        await updateAttrGroup(form.value).finally(() => (buttonLoading.value = false))
      } else {
        await addAttrGroup(form.value).finally(() => (buttonLoading.value = false))
      }
      proxy?.$modal.msgSuccess('操作成功')
      dialog.visible = false
      await getList()
    }
  })
}

/** 删除按钮操作 */
const handleDelete = async (row?: AttrGroupVO) => {
  const _attrGroupIds = row?.attrGroupId || ids.value
  await proxy?.$modal.confirm('是否确认删除属性分组编号为"' + _attrGroupIds + '"的数据项？').finally(() => (loading.value = false))
  await delAttrGroup(_attrGroupIds)
  proxy?.$modal.msgSuccess('删除成功')
  await getList()
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download(
    'guli/attribute/attrGroup/export',
    {
      ...queryParams.value
    },
    `attrGroup_${new Date().getTime()}.xlsx`
  )
}

// ========== 关联业务 ==========

const relationDialog = reactive<DialogOption>({
  visible: false,
  title: ''
})
const relationList = ref<AttrAttrgroupRelationVO[]>([])
const relationLoading = ref(false)

const addRelationDialog = reactive<DialogOption>({
  visible: false,
  title: ''
})
const availableAttrs = ref<KeyValStoreVO[]>([])
const addAttrId = ref<string | number>('')
const addAttrSort = ref(0)
const isEditMode = ref(false)
const editingRelationId = ref<string | number>('')
const currentAttrGroup = ref<AttrGroupVO | null>(null)

/** 打开关联对话框 */
const handleRelation = async () => {
  currentAttrGroup.value = attrGroupList.value.find((a) => a.attrGroupId === ids.value[0]) || null
  if (!currentAttrGroup.value) return
  relationDialog.title = `属性分组关联 - ${currentAttrGroup.value.attrGroupName}`
  relationDialog.visible = true
}

/** 查询关联列表 */
const getRelationList = async () => {
  if (!currentAttrGroup.value) return
  relationLoading.value = true
  try {
    const res = await listRelations({ attrGroupId: currentAttrGroup.value.attrGroupId })
    relationList.value = res.data
  } finally {
    relationLoading.value = false
  }
}

/** 打开新增关联对话框 */
const handleOpenAddRelation = async () => {
  if (!currentAttrGroup.value) return
  isEditMode.value = false
  editingRelationId.value = ''
  addAttrId.value = ''
  addAttrSort.value = 0
  addRelationDialog.title = `新增关联 - ${currentAttrGroup.value.attrGroupName}`
  addRelationDialog.visible = true
  // 加载可关联的属性
  const res = await listAvailableAttrs(currentAttrGroup.value.catelogId)
  availableAttrs.value = res.data
}

/** 打开编辑关联对话框 */
const handleOpenEditRelation = async (row: AttrAttrgroupRelationVO) => {
  if (!currentAttrGroup.value) return
  isEditMode.value = true
  editingRelationId.value = row.id
  addAttrId.value = row.attrId
  addAttrSort.value = row.attrSort
  addRelationDialog.title = `编辑关联 - ${row.attrName}`
  addRelationDialog.visible = true
  // 编辑时也需要可选项（仅显示当前项，置灰不可改）
  const res = await listAvailableAttrs(currentAttrGroup.value.catelogId)
  availableAttrs.value = res.data
}

/** 提交新增/编辑关联 */
const handleSubmitRelation = async () => {
  if (!addAttrId.value || !currentAttrGroup.value) return
  if (isEditMode.value) {
    await updateRelation({
      id: editingRelationId.value,
      attrSort: addAttrSort.value
    })
    proxy?.$modal.msgSuccess('修改排序成功')
  } else {
    await addRelation({
      attrId: addAttrId.value,
      attrGroupId: currentAttrGroup.value.attrGroupId,
      attrSort: addAttrSort.value
    })
    proxy?.$modal.msgSuccess('新增关联成功')
  }
  addRelationDialog.visible = false
  await getRelationList()
}

/** 删除关联 */
const handleDeleteRelation = async (row: AttrAttrgroupRelationVO) => {
  await proxy?.$modal.confirm('是否确认取消属性"' + row.attrName + '"与分组"' + row.attrGroupName + '"的关联？')
  await delRelation(row.id)
  proxy?.$modal.msgSuccess('取消关联成功')
  await getRelationList()
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
