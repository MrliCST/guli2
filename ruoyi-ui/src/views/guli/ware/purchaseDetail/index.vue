<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="采购单id" prop="purchaseId">
              <el-input v-model="queryParams.purchaseId" placeholder="请输入采购单id" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="采购商品id" prop="skuId">
              <el-input v-model="queryParams.skuId" placeholder="请输入采购商品id" clearable @keyup.enter="handleQuery" />
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
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['guli:purchaseDetail:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['guli:purchaseDetail:edit']"
              >修改</el-button
            >
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['guli:purchaseDetail:remove']"
              >删除</el-button
            >
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['guli:purchaseDetail:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" border :data="purchaseDetailList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="" align="center" prop="id" v-if="true" />
        <el-table-column label="采购单id" align="center" prop="purchaseId" />
        <el-table-column label="采购商品id" align="center" prop="skuId" />
        <el-table-column label="采购数量" align="center" prop="skuNum" />
        <el-table-column label="采购金额" align="center" prop="skuPrice" />
        <el-table-column label="仓库id" align="center" prop="wareId" />
        <el-table-column label="状态[0新建，1已分配，2正在采购，3已完成，4采购失败]" align="center" prop="status" />
        <el-table-column label="操作" align="center" fixed="right" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['guli:purchaseDetail:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['guli:purchaseDetail:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改采购单详情对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="purchaseDetailFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="采购单id" prop="purchaseId">
          <el-input v-model="form.purchaseId" placeholder="请输入采购单id" />
        </el-form-item>
        <el-form-item label="采购商品id" prop="skuId">
          <el-input v-model="form.skuId" placeholder="请输入采购商品id" />
        </el-form-item>
        <el-form-item label="采购数量" prop="skuNum">
          <el-input v-model="form.skuNum" placeholder="请输入采购数量" />
        </el-form-item>
        <el-form-item label="采购金额" prop="skuPrice">
          <el-input v-model="form.skuPrice" placeholder="请输入采购金额" />
        </el-form-item>
        <el-form-item label="仓库id" prop="wareId">
          <el-input v-model="form.wareId" placeholder="请输入仓库id" />
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

<script setup name="PurchaseDetail" lang="ts">
import { listPurchaseDetail, getPurchaseDetail, delPurchaseDetail, addPurchaseDetail, updatePurchaseDetail } from '@/api/guli/ware/purchaseDetail'
import { PurchaseDetailVO, PurchaseDetailQuery, PurchaseDetailForm } from '@/api/guli/ware/purchaseDetail/types'

const { proxy } = getCurrentInstance() as ComponentInternalInstance

const purchaseDetailList = ref<PurchaseDetailVO[]>([])
const buttonLoading = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref<Array<string | number>>([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)

const queryFormRef = ref<ElFormInstance>()
const purchaseDetailFormRef = ref<ElFormInstance>()

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
})

const initFormData: PurchaseDetailForm = {
  id: undefined,
  purchaseId: undefined,
  skuId: undefined,
  skuNum: undefined,
  skuPrice: undefined,
  wareId: undefined,
  status: undefined
}
const data = reactive<PageData<PurchaseDetailForm, PurchaseDetailQuery>>({
  form: { ...initFormData },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    purchaseId: undefined,
    skuId: undefined,
    params: {}
  },
  rules: {
    id: [{ required: true, message: '不能为空', trigger: 'blur' }]
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询采购单详情列表 */
const getList = async () => {
  loading.value = true
  const res = await listPurchaseDetail(queryParams.value)
  purchaseDetailList.value = res.rows
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
  purchaseDetailFormRef.value?.resetFields()
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
const handleSelectionChange = (selection: PurchaseDetailVO[]) => {
  ids.value = selection.map((item) => item.id)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset()
  dialog.visible = true
  dialog.title = '添加采购单详情'
}

/** 修改按钮操作 */
const handleUpdate = async (row?: PurchaseDetailVO) => {
  reset()
  const _id = row?.id || ids.value[0]
  const res = await getPurchaseDetail(_id)
  Object.assign(form.value, res.data)
  dialog.visible = true
  dialog.title = '修改采购单详情'
}

/** 提交按钮 */
const submitForm = () => {
  purchaseDetailFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true
      if (form.value.id) {
        await updatePurchaseDetail(form.value).finally(() => (buttonLoading.value = false))
      } else {
        await addPurchaseDetail(form.value).finally(() => (buttonLoading.value = false))
      }
      proxy?.$modal.msgSuccess('操作成功')
      dialog.visible = false
      await getList()
    }
  })
}

/** 删除按钮操作 */
const handleDelete = async (row?: PurchaseDetailVO) => {
  const _ids = row?.id || ids.value
  await proxy?.$modal.confirm('是否确认删除采购单详情编号为"' + _ids + '"的数据项？').finally(() => (loading.value = false))
  await delPurchaseDetail(_ids)
  proxy?.$modal.msgSuccess('删除成功')
  await getList()
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download(
    'ware/purchaseDetail/export',
    {
      ...queryParams.value
    },
    `purchaseDetail_${new Date().getTime()}.xlsx`
  )
}

onMounted(() => {
  getList()
})
</script>
