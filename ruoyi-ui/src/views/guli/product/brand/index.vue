<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="品牌名" prop="name">
              <el-input v-model="queryParams.name" placeholder="请输入品牌名" clearable @keyup.enter="handleQuery" />
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
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['guli:brand:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['guli:brand:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['guli:brand:remove']"
              >删除</el-button
            >
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['guli:brand:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" border :data="brandList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="品牌id" align="center" prop="brandId" v-if="true" />
        <el-table-column label="品牌名" align="center" prop="name" />
        <el-table-column label="品牌Logo" align="center" prop="logo">
          <template #default="scope">
            <el-image
              v-if="scope.row.logo"
              :src="scope.row.logo"
              style="width: 48px; height: 48px; border-radius: 4px"
              fit="cover"
              :preview-src-list="[scope.row.logo]"
              preview-teleported
            />
            <span v-else>暂无Logo</span>
          </template>
        </el-table-column>
        <el-table-column label="介绍" align="center" prop="descript" />
        <el-table-column label="显示状态" align="center" prop="showStatus">
          <template #default="scope">
            <DictTag :options="showStatusOptions" :value="scope.row.showStatus" />
          </template>
        </el-table-column>
        <el-table-column label="检索首字母" align="center" prop="firstLetter" />
        <el-table-column label="排序" align="center" prop="sort" />
        <el-table-column label="操作" align="center" fixed="right" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['guli:brand:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['guli:brand:remove']"></el-button>
            </el-tooltip>
            <el-tooltip content="关联" placement="top">
              <el-button link type="primary" icon="Connection" @click="handleRelation(scope.row)" v-hasPermi="['guli:brand:edit']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>

    <!-- 添加或修改品牌对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="800px" append-to-body>
      <el-form ref="brandFormRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="品牌名" prop="name">
          <el-input v-model="form.name" placeholder="请输入品牌名" />
        </el-form-item>
        <el-form-item label="品牌Logo" prop="logo">
          <ImageUpload v-model="logoOssId" :limit="1" :is-show-tip="false" />
        </el-form-item>
        <el-form-item label="介绍" prop="descript">
          <el-input v-model="form.descript" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="检索首字母" prop="firstLetter">
          <el-input v-model="form.firstLetter" placeholder="请输入检索首字母" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input v-model="form.sort" placeholder="请输入排序" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 品牌分类关联对话框 -->
    <el-dialog :title="relationDialog.title" v-model="relationDialog.visible" width="700px" append-to-body @opened="handleOpenRelationDialog">
      <el-button type="primary" plain icon="Plus" class="mb8" @click="handleOpenAddRelation">新增</el-button>

      <el-table v-loading="relationLoading" border :data="relationList">
        <el-table-column label="关联ID" align="center" prop="id" />
        <el-table-column label="品牌名" align="center" prop="brandName" />
        <el-table-column label="分类名" align="center" prop="catelogName" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="取消关联" placement="top">
              <el-button link type="danger" icon="Delete" @click="handleDeleteRelation(scope.row)"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination
        v-show="relationTotal > 0"
        :total="relationTotal"
        v-model:page="relationPageNum"
        v-model:limit="relationPageSize"
        @pagination="getRelationList"
      />
    </el-dialog>

    <!-- 新增品牌分类关联对话框 -->
    <el-dialog :title="addRelationDialog.title" v-model="addRelationDialog.visible" width="500px" append-to-body>
      <el-form label-width="80px">
        <el-form-item label="品牌">
          <el-input :model-value="currentRelationBrand?.name" disabled />
        </el-form-item>
        <el-form-item label="分类">
          <el-tree-select
            v-model="addCatelogId"
            :data="treeSelectOptions"
            node-key="catId"
            :props="{ label: 'name', children: 'children' }"
            placeholder="请选择分类"
            check-strictly
            clearable
            style="width: 100%"
            @visible-change="loadTreeOptions"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" :disabled="!addCatelogId" @click="handleAddRelation">确 定</el-button>
          <el-button @click="addRelationDialog.visible = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Brand" lang="ts">
import { listBrand, getBrand, delBrand, addBrand, updateBrand, listCbr, addCbr, delCbr } from '@/api/guli/product/brand'
import { BrandVO, BrandQuery, BrandForm, CategoryBrandRelationVO } from '@/api/guli/product/brand/types'
import type { CategoryVO } from '@/api/guli/product/category/types'
import { listTreeCategory } from '@/api/guli/product/brand'
import { listByIds } from '@/api/system/oss'

const { proxy } = getCurrentInstance() as ComponentInternalInstance

const showStatusOptions = [
  { value: '0', label: '隐藏', elTagType: 'info' as const },
  { value: '1', label: '显示', elTagType: 'success' as const }
]

const brandList = ref<BrandVO[]>([])
const buttonLoading = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref<Array<string | number>>([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)

const queryFormRef = ref<ElFormInstance>()
const brandFormRef = ref<ElFormInstance>()

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
})

// 品牌分类关联
const relationDialog = reactive<DialogOption>({
  visible: false,
  title: ''
})
const relationList = ref<CategoryBrandRelationVO[]>([])
const relationLoading = ref(false)
const relationTotal = ref(0)
const relationPageNum = ref(1)
const relationPageSize = ref(10)
const currentRelationBrand = ref<BrandVO | null>(null)

// 新增关联
const addRelationDialog = reactive<DialogOption>({
  visible: false,
  title: ''
})
const addCatelogId = ref<string | number>('')
const treeSelectOptions = ref<CategoryVO[]>([])

const initFormData: BrandForm = {
  brandId: undefined,
  name: undefined,
  logo: undefined,
  descript: undefined,
  showStatus: undefined,
  firstLetter: undefined,
  sort: undefined
}
const data = reactive<PageData<BrandForm, BrandQuery>>({
  form: { ...initFormData },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    name: undefined,
    params: {}
  },
  rules: {
    brandId: [{ required: true, message: '品牌id不能为空', trigger: 'blur' }]
  }
})

const { queryParams, form, rules } = toRefs(data)

/** ImageUpload 吐 ossId 串，watch 调 API 取 url 写入 form.logo */
const logoOssId = ref('')
watch(logoOssId, async (val) => {
  if (val) {
    const res = await listByIds(val)
    form.value.logo = res.data?.[0]?.url || ''
  } else {
    form.value.logo = ''
  }
})

/** 查询品牌列表 */
const getList = async () => {
  loading.value = true
  const res = await listBrand(queryParams.value)
  brandList.value = res.rows
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
  logoOssId.value = ''
  brandFormRef.value?.resetFields()
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
const handleSelectionChange = (selection: BrandVO[]) => {
  ids.value = selection.map((item) => item.brandId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset()
  dialog.visible = true
  dialog.title = '添加品牌'
}

/** 修改按钮操作 */
const handleUpdate = async (row?: BrandVO) => {
  reset()
  const _brandId = row?.brandId || ids.value[0]
  const res = await getBrand(_brandId)
  Object.assign(form.value, res.data)
  dialog.visible = true
  dialog.title = '修改品牌'
}

/** 提交按钮 */
const submitForm = () => {
  brandFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true
      if (form.value.brandId) {
        await updateBrand(form.value).finally(() => (buttonLoading.value = false))
      } else {
        await addBrand(form.value).finally(() => (buttonLoading.value = false))
      }
      proxy?.$modal.msgSuccess('操作成功')
      dialog.visible = false
      await getList()
    }
  })
}

/** 删除按钮操作 */
const handleDelete = async (row?: BrandVO) => {
  const _brandIds = row?.brandId || ids.value
  await proxy?.$modal.confirm('是否确认删除品牌编号为"' + _brandIds + '"的数据项？').finally(() => (loading.value = false))
  await delBrand(_brandIds)
  proxy?.$modal.msgSuccess('删除成功')
  await getList()
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download(
    'product/brand/export',
    {
      ...queryParams.value
    },
    `brand_${new Date().getTime()}.xlsx`
  )
}

/** 打开关联对话框（入口） */
const handleRelation = async (row: BrandVO) => {
  currentRelationBrand.value = row
  relationDialog.title = `品牌分类关联 - ${row.name}`
  relationDialog.visible = true
}

/** 关联对话框打开后加载 */
const handleOpenRelationDialog = () => {
  relationPageNum.value = 1
  getRelationList()
}

/** 查询关联列表 */
const getRelationList = async () => {
  relationLoading.value = true
  try {
    const res = await listCbr({
      pageNum: relationPageNum.value,
      pageSize: relationPageSize.value,
      brandId: currentRelationBrand.value?.brandId,
      params: {}
    })
    relationList.value = (res as any).rows
    relationTotal.value = (res as any).total
  } finally {
    relationLoading.value = false
  }
}

/** 加载树形选项 */
const loadTreeOptions = async (visible: boolean) => {
  if (visible && treeSelectOptions.value.length === 0) {
    const res = await listTreeCategory()
    treeSelectOptions.value = res.data
  }
}

/** 打开新增关联对话框 */
const handleOpenAddRelation = () => {
  addCatelogId.value = ''
  addRelationDialog.title = `新增关联 - ${currentRelationBrand.value?.name}`
  addRelationDialog.visible = true
}

/** 新增关联 */
const handleAddRelation = async () => {
  if (!addCatelogId.value) return
  await addCbr({
    brandId: currentRelationBrand.value?.brandId,
    catelogId: addCatelogId.value
  })
  proxy?.$modal.msgSuccess('新增关联成功')
  addRelationDialog.visible = false
  await getRelationList()
}

/** 取消关联 */
const handleDeleteRelation = async (row: CategoryBrandRelationVO) => {
  await proxy?.$modal.confirm('是否确认取消品牌"' + row.brandName + '"与分类"' + row.catelogName + '"的关联？')
  await delCbr(row.id)
  proxy?.$modal.msgSuccess('取消关联成功')
  await getRelationList()
}

onMounted(() => {
  getList()
})
</script>

<style scoped lang="scss"></style>
