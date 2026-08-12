<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="sku_id" prop="skuId">
              <el-input v-model="queryParams.skuId" placeholder="请输入sku_id" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="仓库id" prop="wareId">
              <el-input v-model="queryParams.wareId" placeholder="请输入仓库id" clearable @keyup.enter="handleQuery" />
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
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['guli:wareSku:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['guli:wareSku:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['guli:wareSku:remove']"
              >删除</el-button
            >
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['guli:wareSku:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" border :data="wareSkuList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="id" align="center" prop="id" v-if="true" />
        <el-table-column label="sku_id" align="center" prop="skuId" />
        <el-table-column label="仓库id" align="center" prop="wareId" />
        <el-table-column label="库存数" align="center" prop="stock" />
        <el-table-column label="sku_name" align="center" prop="skuName" />
        <el-table-column label="锁定库存" align="center" prop="stockLocked" />
        <el-table-column label="操作" align="center" fixed="right" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['guli:wareSku:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['guli:wareSku:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改商品库存对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="wareSkuFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="sku_id" prop="skuId">
          <el-input v-model="form.skuId" placeholder="请输入sku_id" />
        </el-form-item>
        <el-form-item label="仓库id" prop="wareId">
          <el-input v-model="form.wareId" placeholder="请输入仓库id" />
        </el-form-item>
        <el-form-item label="库存数" prop="stock">
          <el-input v-model="form.stock" placeholder="请输入库存数" />
        </el-form-item>
        <el-form-item label="sku_name" prop="skuName">
          <el-input v-model="form.skuName" placeholder="请输入sku_name" />
        </el-form-item>
        <el-form-item label="锁定库存" prop="stockLocked">
          <el-input v-model="form.stockLocked" placeholder="请输入锁定库存" />
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

<script setup name="WareSku" lang="ts">
import { listWareSku, getWareSku, delWareSku, addWareSku, updateWareSku } from '@/api/guli/ware/wareSku'
import { WareSkuVO, WareSkuQuery, WareSkuForm } from '@/api/guli/ware/wareSku/types'

const { proxy } = getCurrentInstance() as ComponentInternalInstance

const wareSkuList = ref<WareSkuVO[]>([])
const buttonLoading = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref<Array<string | number>>([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)

const queryFormRef = ref<ElFormInstance>()
const wareSkuFormRef = ref<ElFormInstance>()

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
})

const initFormData: WareSkuForm = {
  id: undefined,
  skuId: undefined,
  wareId: undefined,
  stock: undefined,
  skuName: undefined,
  stockLocked: undefined
}
const data = reactive<PageData<WareSkuForm, WareSkuQuery>>({
  form: { ...initFormData },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    skuId: undefined,
    wareId: undefined,
    params: {}
  },
  rules: {
    id: [{ required: true, message: 'id不能为空', trigger: 'blur' }]
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询商品库存列表 */
const getList = async () => {
  loading.value = true
  const res = await listWareSku(queryParams.value)
  wareSkuList.value = res.rows
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
  wareSkuFormRef.value?.resetFields()
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
const handleSelectionChange = (selection: WareSkuVO[]) => {
  ids.value = selection.map((item) => item.id)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset()
  dialog.visible = true
  dialog.title = '添加商品库存'
}

/** 修改按钮操作 */
const handleUpdate = async (row?: WareSkuVO) => {
  reset()
  const _id = row?.id || ids.value[0]
  const res = await getWareSku(_id)
  Object.assign(form.value, res.data)
  dialog.visible = true
  dialog.title = '修改商品库存'
}

/** 提交按钮 */
const submitForm = () => {
  wareSkuFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true
      if (form.value.id) {
        await updateWareSku(form.value).finally(() => (buttonLoading.value = false))
      } else {
        await addWareSku(form.value).finally(() => (buttonLoading.value = false))
      }
      proxy?.$modal.msgSuccess('操作成功')
      dialog.visible = false
      await getList()
    }
  })
}

/** 删除按钮操作 */
const handleDelete = async (row?: WareSkuVO) => {
  const _ids = row?.id || ids.value
  await proxy?.$modal.confirm('是否确认删除商品库存编号为"' + _ids + '"的数据项？').finally(() => (loading.value = false))
  await delWareSku(_ids)
  proxy?.$modal.msgSuccess('删除成功')
  await getList()
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download(
    'ware/wareSku/export',
    {
      ...queryParams.value
    },
    `wareSku_${new Date().getTime()}.xlsx`
  )
}

onMounted(() => {
  getList()
})
</script>
