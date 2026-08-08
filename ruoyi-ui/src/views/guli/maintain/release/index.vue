<template>
  <div class="release-page">
    <el-card shadow="never" class="release-card">
      <el-steps :active="activeStep" align-center finish-status="success" class="mb-4">
        <el-step title="基本信息" description="填写SPU基本信息" />
        <el-step title="基本属性" description="设置基本属性" />
        <el-step title="销售属性" description="设置销售属性" />
        <el-step title="SKU信息" description="设置SKU信息" />
        <el-step title="保存完成" description="确认并提交" />
      </el-steps>

      <!-- 第一步：基本信息 -->
      <div v-show="activeStep === 0" class="step-content">
        <el-form ref="basicFormRef" :model="form" :rules="basicRules" label-width="100px" class="step-form">
          <el-form-item label="商品名称" prop="spuName">
            <el-input v-model="form.basicInfo.spuName" placeholder="请输入商品名称" />
          </el-form-item>
          <el-form-item label="商品描述" prop="spuDescription">
            <el-input v-model="form.basicInfo.spuDescription" type="textarea" :rows="3" placeholder="请输入商品描述" />
          </el-form-item>
          <el-form-item label="所属分类" prop="catalogId">
            <el-tree-select
              v-model="form.basicInfo.catalogId"
              :data="categoryTree"
              node-key="catId"
              :props="{ label: 'name', children: 'children' }"
              placeholder="请选择所属分类"
              check-strictly
              clearable
              style="width: 100%"
              @change="onCategoryChange"
            />
          </el-form-item>
          <el-form-item label="品牌" prop="brandId">
            <el-select v-model="form.basicInfo.brandId" placeholder="请选择品牌" clearable style="width: 100%" :disabled="!form.basicInfo.catalogId">
              <el-option v-for="b in brandList" :key="b.brandId" :label="b.brandName" :value="b.brandId" />
            </el-select>
          </el-form-item>
          <el-form-item label="重量" prop="weight">
            <el-input v-model="form.basicInfo.weight" placeholder="请输入重量" />
          </el-form-item>
          <el-form-item label="上架状态" prop="publishStatus">
            <el-radio-group v-model="form.basicInfo.publishStatus">
              <el-radio :value="1">上架</el-radio>
              <el-radio :value="0">下架</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="商品主图" prop="MainImgDesc">
            <ImageUpload v-model="form.basicInfo.MainImgDesc" :limit="1" :is-show-tip="false" />
          </el-form-item>
          <el-form-item label="商品图集" prop="ImgAlbum">
            <ImageUpload v-model="form.basicInfo.ImgAlbum" :limit="5" :is-show-tip="false" />
          </el-form-item>
        </el-form>
        <div class="step-footer">
          <el-button type="primary" @click="nextStep">下一步</el-button>
        </div>
      </div>

      <!-- 第二步：基本属性 -->
      <div v-show="activeStep === 1" class="step-content">
        <el-empty description="基本属性（后续开发）" />
        <div class="step-footer">
          <el-button @click="activeStep--">上一步</el-button>
          <el-button type="primary" @click="activeStep++">下一步</el-button>
        </div>
      </div>

      <!-- 第三步：销售属性 -->
      <div v-show="activeStep === 2" class="step-content">
        <el-empty description="销售属性（后续开发）" />
        <div class="step-footer">
          <el-button @click="activeStep--">上一步</el-button>
          <el-button type="primary" @click="activeStep++">下一步</el-button>
        </div>
      </div>

      <!-- 第四步：SKU信息 -->
      <div v-show="activeStep === 3" class="step-content">
        <el-empty description="SKU信息（后续开发）" />
        <div class="step-footer">
          <el-button @click="activeStep--">上一步</el-button>
          <el-button type="primary" @click="activeStep++">下一步</el-button>
        </div>
      </div>

      <!-- 第五步：保存完成 -->
      <div v-show="activeStep === 4" class="step-content">
        <el-descriptions title="SPU信息确认" :column="2" border>
          <el-descriptions-item label="商品名称">{{ form.basicInfo.spuName }}</el-descriptions-item>
          <el-descriptions-item label="商品描述">{{ form.basicInfo.spuDescription }}</el-descriptions-item>
          <el-descriptions-item label="所属分类">{{ selectedCategoryName }}</el-descriptions-item>
          <el-descriptions-item label="品牌">{{ selectedBrandName }}</el-descriptions-item>
          <el-descriptions-item label="重量">{{ form.basicInfo.weight }}</el-descriptions-item>
          <el-descriptions-item label="上架状态">{{ form.basicInfo.publishStatus === 1 ? '上架' : '下架' }}</el-descriptions-item>
        </el-descriptions>
        <div class="step-footer">
          <el-button @click="activeStep--">上一步</el-button>
          <el-button type="primary" :loading="buttonLoading" @click="submitForm">保存提交</el-button>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup name="Release" lang="ts">
import { addRelease, listTreeCategory, listBrands } from '@/api/guli/maintain/release'
import type { ReleaseForm } from '@/api/guli/maintain/release/types'
import type { CategoryVO } from '@/api/guli/category/types'
import type { CategoryBrandRelationVO } from '@/api/guli/brand/types'

const { proxy } = getCurrentInstance() as ComponentInternalInstance

const activeStep = ref(0)
const buttonLoading = ref(false)

const basicFormRef = ref<ElFormInstance>()
const categoryTree = ref<CategoryVO[]>([])
const brandList = ref<CategoryBrandRelationVO[]>([])

const selectedCategoryName = computed(() => {
  // 简化处理，显示ID
  const c = categoryTree.value.find((n) => n.catId === form.value.basicInfo.catalogId)
  return c?.name || ''
})
const selectedBrandName = computed(() => {
  const b = brandList.value.find((n) => n.brandId === form.value.basicInfo.brandId)
  return b?.brandName || ''
})

const initBasicInfo = {
  spuName: undefined,
  spuDescription: undefined,
  catalogId: undefined,
  brandId: undefined,
  weight: undefined,
  publishStatus: 1,
  MainImgDesc: undefined,
  ImgAlbum: undefined
}

const initFormData: ReleaseForm = {
  id: undefined,
  basicInfo: { ...initBasicInfo },
  baseAttrs: undefined,
  saleAttrs: undefined,
  skus: undefined
}

const form = ref<ReleaseForm>({ ...initFormData })

const basicRules = ref({
  spuName: [{ required: true, message: '商品名称不能为空', trigger: 'blur' }],
  catalogId: [{ required: true, message: '所属分类不能为空', trigger: 'change' }],
  brandId: [{ required: true, message: '品牌不能为空', trigger: 'change' }]
})

onMounted(async () => {
  const res = await listTreeCategory()
  categoryTree.value = res.data
})

const onCategoryChange = async (catelogId: string | number) => {
  form.value.basicInfo.brandId = undefined
  brandList.value = []
  if (!catelogId) return
  const res = await listBrands(catelogId)
  brandList.value = res.data
}

const nextStep = async () => {
  if (activeStep.value === 0) {
    await basicFormRef.value?.validate().catch(() => {})
  }
  activeStep.value++
}

const submitForm = async () => {
  buttonLoading.value = true
  await addRelease(form.value).finally(() => (buttonLoading.value = false))
  proxy?.$modal.msgSuccess('操作成功')
  form.value = { ...initFormData }
  activeStep.value = 0
}
</script>

<style scoped lang="scss">
.release-page {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  padding: 24px;
}

.release-card {
  width: 100%;
  max-width: 900px;
  min-height: 0;
}

.mb-4 {
  margin-bottom: 24px;
}

.step-content {
  max-width: 700px;
  margin: 0 auto;
}

.step-form {
  margin-top: 16px;
}

.step-footer {
  display: flex;
  justify-content: center;
  gap: 12px;
  margin-top: 24px;
  padding-top: 16px;
  border-top: 1px solid var(--el-border-color-lighter);
}
</style>
