<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="仓库名" prop="name">
              <el-input v-model="queryParams.name" placeholder="请输入仓库名" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="仓库地址" prop="address">
              <el-input v-model="queryParams.address" placeholder="请输入仓库地址" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="区域编码" prop="areacode">
              <el-input v-model="queryParams.areacode" placeholder="请输入区域编码" clearable @keyup.enter="handleQuery" />
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
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['guli:wareInfo:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['guli:wareInfo:edit']"
              >修改</el-button
            >
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['guli:wareInfo:remove']"
              >删除</el-button
            >
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['guli:wareInfo:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" border :data="wareInfoList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="id" align="center" prop="id" v-if="true" />
        <el-table-column label="仓库名" align="center" prop="name" />
        <el-table-column label="仓库地址" align="center" prop="address" />
        <el-table-column label="区域编码" align="center" prop="areacode" />
        <el-table-column label="操作" align="center" fixed="right" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['guli:wareInfo:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['guli:wareInfo:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改仓库信息对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="wareInfoFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="仓库名" prop="name">
          <el-input v-model="form.name" placeholder="请输入仓库名" />
        </el-form-item>
        <el-form-item label="仓库地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入仓库地址" />
        </el-form-item>
        <el-form-item label="区域编码" prop="areacode">
          <el-input v-model="form.areacode" placeholder="请输入区域编码" />
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

<script setup name="WareInfo" lang="ts">
import { listWareInfo, getWareInfo, delWareInfo, addWareInfo, updateWareInfo } from '@/api/guli/ware/wareInfo'
import { WareInfoVO, WareInfoQuery, WareInfoForm } from '@/api/guli/ware/wareInfo/types'

const { proxy } = getCurrentInstance() as ComponentInternalInstance

const wareInfoList = ref<WareInfoVO[]>([])
const buttonLoading = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref<Array<string | number>>([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)

const queryFormRef = ref<ElFormInstance>()
const wareInfoFormRef = ref<ElFormInstance>()

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
})

const initFormData: WareInfoForm = {
  id: undefined,
  name: undefined,
  address: undefined,
  areacode: undefined
}
const data = reactive<PageData<WareInfoForm, WareInfoQuery>>({
  form: { ...initFormData },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    name: undefined,
    address: undefined,
    areacode: undefined,
    params: {}
  },
  rules: {
    id: [{ required: true, message: 'id不能为空', trigger: 'blur' }]
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询仓库信息列表 */
const getList = async () => {
  loading.value = true
  const res = await listWareInfo(queryParams.value)
  wareInfoList.value = res.rows
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
  wareInfoFormRef.value?.resetFields()
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
const handleSelectionChange = (selection: WareInfoVO[]) => {
  ids.value = selection.map((item) => item.id)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset()
  dialog.visible = true
  dialog.title = '添加仓库信息'
}

/** 修改按钮操作 */
const handleUpdate = async (row?: WareInfoVO) => {
  reset()
  const _id = row?.id || ids.value[0]
  const res = await getWareInfo(_id)
  Object.assign(form.value, res.data)
  dialog.visible = true
  dialog.title = '修改仓库信息'
}

/** 提交按钮 */
const submitForm = () => {
  wareInfoFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true
      if (form.value.id) {
        await updateWareInfo(form.value).finally(() => (buttonLoading.value = false))
      } else {
        await addWareInfo(form.value).finally(() => (buttonLoading.value = false))
      }
      proxy?.$modal.msgSuccess('操作成功')
      dialog.visible = false
      await getList()
    }
  })
}

/** 删除按钮操作 */
const handleDelete = async (row?: WareInfoVO) => {
  const _ids = row?.id || ids.value
  await proxy?.$modal.confirm('是否确认删除仓库信息编号为"' + _ids + '"的数据项？').finally(() => (loading.value = false))
  await delWareInfo(_ids)
  proxy?.$modal.msgSuccess('删除成功')
  await getList()
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download(
    'ware/wareInfo/export',
    {
      ...queryParams.value
    },
    `wareInfo_${new Date().getTime()}.xlsx`
  )
}

onMounted(() => {
  getList()
})
</script>
