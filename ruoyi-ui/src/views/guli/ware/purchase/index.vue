<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="" prop="assigneeName">
              <el-input v-model="queryParams.assigneeName" placeholder="请输入" clearable @keyup.enter="handleQuery" />
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
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['guli:purchase:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['guli:purchase:edit']"
              >修改</el-button
            >
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['guli:purchase:remove']"
              >删除</el-button
            >
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['guli:purchase:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" border :data="purchaseList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="" align="center" prop="id" v-if="true" />
        <el-table-column label="" align="center" prop="assigneeId" />
        <el-table-column label="" align="center" prop="assigneeName" />
        <el-table-column label="" align="center" prop="phone" />
        <el-table-column label="" align="center" prop="priority" />
        <el-table-column label="" align="center" prop="status" />
        <el-table-column label="" align="center" prop="wareId" />
        <el-table-column label="" align="center" prop="amount" />
        <el-table-column label="操作" align="center" fixed="right" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['guli:purchase:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['guli:purchase:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改采购信息对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="purchaseFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="" prop="assigneeId">
          <el-input v-model="form.assigneeId" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="" prop="assigneeName">
          <el-input v-model="form.assigneeName" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="" prop="priority">
          <el-input v-model="form.priority" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="" prop="wareId">
          <el-input v-model="form.wareId" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="" prop="amount">
          <el-input v-model="form.amount" placeholder="请输入" />
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

<script setup name="Purchase" lang="ts">
import { listPurchase, getPurchase, delPurchase, addPurchase, updatePurchase } from '@/api/guli/ware/purchase'
import { PurchaseVO, PurchaseQuery, PurchaseForm } from '@/api/guli/ware/purchase/types'

const { proxy } = getCurrentInstance() as ComponentInternalInstance

const purchaseList = ref<PurchaseVO[]>([])
const buttonLoading = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref<Array<string | number>>([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)

const queryFormRef = ref<ElFormInstance>()
const purchaseFormRef = ref<ElFormInstance>()

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
})

const initFormData: PurchaseForm = {
  id: undefined,
  assigneeId: undefined,
  assigneeName: undefined,
  phone: undefined,
  priority: undefined,
  status: undefined,
  wareId: undefined,
  amount: undefined
}
const data = reactive<PageData<PurchaseForm, PurchaseQuery>>({
  form: { ...initFormData },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    assigneeName: undefined,
    params: {}
  },
  rules: {
    id: [{ required: true, message: '不能为空', trigger: 'blur' }]
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询采购信息列表 */
const getList = async () => {
  loading.value = true
  const res = await listPurchase(queryParams.value)
  purchaseList.value = res.rows
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
  purchaseFormRef.value?.resetFields()
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
const handleSelectionChange = (selection: PurchaseVO[]) => {
  ids.value = selection.map((item) => item.id)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset()
  dialog.visible = true
  dialog.title = '添加采购信息'
}

/** 修改按钮操作 */
const handleUpdate = async (row?: PurchaseVO) => {
  reset()
  const _id = row?.id || ids.value[0]
  const res = await getPurchase(_id)
  Object.assign(form.value, res.data)
  dialog.visible = true
  dialog.title = '修改采购信息'
}

/** 提交按钮 */
const submitForm = () => {
  purchaseFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true
      if (form.value.id) {
        await updatePurchase(form.value).finally(() => (buttonLoading.value = false))
      } else {
        await addPurchase(form.value).finally(() => (buttonLoading.value = false))
      }
      proxy?.$modal.msgSuccess('操作成功')
      dialog.visible = false
      await getList()
    }
  })
}

/** 删除按钮操作 */
const handleDelete = async (row?: PurchaseVO) => {
  const _ids = row?.id || ids.value
  await proxy?.$modal.confirm('是否确认删除采购信息编号为"' + _ids + '"的数据项？').finally(() => (loading.value = false))
  await delPurchase(_ids)
  proxy?.$modal.msgSuccess('删除成功')
  await getList()
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download(
    'ware/purchase/export',
    {
      ...queryParams.value
    },
    `purchase_${new Date().getTime()}.xlsx`
  )
}

onMounted(() => {
  getList()
})
</script>
