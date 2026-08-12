<template>
  <div class="release-page">
    <el-card shadow="never" class="release-card">
      <el-steps :active="activeStep" align-center finish-status="success" class="mb-4">
        <el-step title="基本信息" description="填写SPU基本信息" />
        <el-step title="基本属性" description="设置基本属性" />
        <el-step title="销售属性" description="设置销售属性" />
        <el-step title="SKU信息" description="设置SKU信息" />
      </el-steps>

      <!-- 第一步：基本信息 -->
      <div v-show="activeStep === 0" class="step-content">
        <el-form ref="basicFormRef" :model="spuBaseInfo" :rules="basicRules" label-width="100px" class="step-form">
          <el-form-item label="商品名称" prop="spuName">
            <el-input v-model="spuBaseInfo.spuName" placeholder="请输入商品名称" />
          </el-form-item>
          <el-form-item label="商品描述" prop="spuDescription">
            <el-input v-model="spuBaseInfo.spuDescription" type="textarea" :rows="3" placeholder="请输入商品描述" />
          </el-form-item>
          <el-form-item label="所属分类" prop="catalogId">
            <el-tree-select
              v-model="spuBaseInfo.catalogId"
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
            <el-select v-model="spuBaseInfo.brandId" placeholder="请选择品牌" clearable style="width: 100%" :disabled="!spuBaseInfo.catalogId">
              <el-option v-for="b in brandList" :key="b.brandId" :label="b.brandName" :value="b.brandId" />
            </el-select>
          </el-form-item>
          <el-form-item label="重量" prop="weight">
            <el-input v-model="spuBaseInfo.weight" placeholder="请输入重量" />
          </el-form-item>
          <el-form-item label="上架状态" prop="publishStatus">
            <el-radio-group v-model="spuBaseInfo.publishStatus">
              <el-radio :value="1">上架</el-radio>
              <el-radio :value="0">下架</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="商品主图" prop="MainImgDesc">
            <ImageUpload v-model="mainImgOssId" :limit="1" :is-show-tip="false" />
          </el-form-item>
          <el-form-item label="商品图集" prop="ImgAlbum">
            <ImageUpload v-model="imgAlbumOssId" :limit="20" :is-show-tip="false" />
          </el-form-item>
        </el-form>
        <div class="step-footer">
          <el-button type="primary" @click="nextStep">下一步</el-button>
        </div>
      </div>

      <!-- 第二步：基本属性 -->
      <div v-show="activeStep === 1" class="step-content">
        <el-empty v-if="!attrGroups.length" description="该分类下暂无属性分组" :image-size="60" />
        <el-tabs v-else tab-position="left" class="attr-tabs">
          <el-tab-pane v-for="g in attrGroups" :key="g.attrGroupId" :label="g.attrGroupName">
            <el-form label-width="100px">
              <el-form-item v-for="attr in g.attrs" :key="attr.attrId" :label="attr.attrName">
                <el-select v-model="baseAttrMap[attr.attrId]" :placeholder="'请选择' + attr.attrName" clearable style="width: 100%">
                  <el-option v-for="v in (attr.valueSelect || '').split(';').filter(Boolean)" :key="v" :label="v" :value="v" />
                </el-select>
              </el-form-item>
            </el-form>
          </el-tab-pane>
        </el-tabs>
        <div class="step-footer">
          <el-button @click="activeStep--">上一步</el-button>
          <el-button type="primary" @click="nextStep">下一步</el-button>
        </div>
      </div>

      <!-- 第三步：销售属性 -->
      <div v-show="activeStep === 2" class="step-content">
        <el-empty v-if="!saleAttrs.length" description="该分类下暂无销售属性" :image-size="60" />
        <el-form v-else label-width="100px" class="sale-attr-form">
          <el-form-item v-for="attr in saleAttrs" :key="attr.attrId" :label="attr.attrName">
            <el-checkbox-group v-model="saleAttrMap[attr.attrId]">
              <el-checkbox v-for="v in (attr.valueSelect || '').split(';').filter(Boolean)" :key="v" :label="v" :value="v" />
            </el-checkbox-group>
          </el-form-item>
        </el-form>
        <div class="step-footer">
          <el-button @click="activeStep--">上一步</el-button>
          <el-button type="primary" @click="nextStep">下一步</el-button>
        </div>
      </div>

      <!-- 第四步：SKU信息 -->
      <div v-show="activeStep === 3" class="step-content-wide">
        <el-table v-if="skuTableData.length" :data="skuTableData" row-key="_key" border style="width: 100%">
          <el-table-column type="expand">
            <template #default="{ row }">
              <div class="sku-expand" @click.stop>
                <div v-if="!spuBaseInfo.ImgAlbum?.length" class="text-gray-400">请先在 "基本信息" 上传商品图集</div>
                <div v-else class="sku-image-picker">
                  <div
                    v-for="(img, idx) in spuBaseInfo.ImgAlbum"
                    :key="idx"
                    class="sku-pickable-image"
                    :class="{ 'is-selected': (row.skuImages || []).includes(img.url) }"
                    @click="toggleSkuImage(row, img.url)"
                  >
                    <el-image :src="img.url" fit="cover" style="width: 80px; height: 80px" :preview-src-list="[img.url]" preview-teleported />
                    <span v-if="(row.skuImages || []).includes(img.url)" class="pick-check">✓</span>
                  </div>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="销售属性组合">
            <el-table-column v-for="col in attrColKeys" :key="col.key" :label="col.label" min-width="100">
              <template #default="{ row }">
                <span>{{ row[col.key] }}</span>
              </template>
            </el-table-column>
          </el-table-column>
          <el-table-column label="SKU信息">
            <el-table-column label="商品名称" min-width="140">
              <template #default="{ row }">
                <el-input v-model="row.skuName" placeholder="SKU名称" />
              </template>
            </el-table-column>
            <el-table-column label="标题" min-width="140">
              <template #default="{ row }">
                <el-input v-model="row.skuTitle" placeholder="标题" />
              </template>
            </el-table-column>
            <el-table-column label="副标题" min-width="140">
              <template #default="{ row }">
                <el-input v-model="row.skuSubtitle" placeholder="副标题" />
              </template>
            </el-table-column>
            <el-table-column label="价格" min-width="100">
              <template #default="{ row }">
                <el-input v-model="row.price" placeholder="价格" />
              </template>
            </el-table-column>
            <el-table-column label="库存" min-width="100">
              <template #default="{ row }">
                <el-input v-model="row.stock" placeholder="库存" />
              </template>
            </el-table-column>
          </el-table-column>
        </el-table>
        <el-empty v-else description="请先在第三步选择销售属性" :image-size="60" />
        <div class="step-footer">
          <el-button @click="activeStep--">上一步</el-button>
          <el-button type="primary" :loading="buttonLoading" @click="submitForm">保存提交</el-button>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup name="Release" lang="ts">
import { addRelease, listTreeCategory, listBrands, listAttrGroups, listSaleAttrs } from '@/api/guli/product/maintain/release'
import type { ReleaseForm, SpuInfo, AttrGroupWithAttrsVO, AttrVO } from '@/api/guli/product/maintain/release/types'
import type { CategoryVO } from '@/api/guli/product/category/types'
import type { CategoryBrandRelationVO } from '@/api/guli/product/brand/types'
import { listByIds } from '@/api/system/oss'

const { proxy } = getCurrentInstance() as ComponentInternalInstance

const activeStep = ref(0)
const buttonLoading = ref(false)

// 基础信息
const initBasicInfo: SpuInfo = {
  spuName: undefined,
  spuDescription: undefined,
  catalogId: undefined,
  brandId: undefined,
  weight: undefined,
  publishStatus: 1,
  MainImgDesc: undefined,
  ImgAlbum: []
}

const basicRules = ref({
  'spuName': [{ required: true, message: '商品名称不能为空', trigger: 'blur' }],
  'catalogId': [{ required: true, message: '所属分类不能为空', trigger: 'change' }],
  'brandId': [{ required: true, message: '品牌不能为空', trigger: 'change' }]
})

/**
 * 1. spu基本属性
 * 2. 选中的基本属性值储(仅单值)
 * 3. 选中的销售属性值储(可多选)
 * 4. 属性组合构成的动态表列名
 * 5. sku表格每行数据集合
 */
const spuBaseInfo = ref<SpuInfo>({ ...initBasicInfo })
const baseAttrMap = ref<Record<string | number, string>>({}) // 选择的基本属性值
const saleAttrMap = ref<Record<string | number, string[]>>({}) // 选择的销售属性值
const attrColKeys = ref<{ key: string; label: string }[]>([]) // 动态表头：key=attrId(prop), label=attrName(表头)
const skuTableData = ref<Record<string, any>[]>([]) // SKU表格数据，每行含属性组合+Sku字段

/**
 * 在选中一个category后，依次为category限定下的
 * 1. 属性分组(携带基本属性值储)
 * 2. 销售属性值储
 * 3. 品牌数据
 */
const attrGroups = ref<AttrGroupWithAttrsVO[]>([])
const saleAttrs = ref<AttrVO[]>([])
const brandList = ref<CategoryBrandRelationVO[]>([])

const basicFormRef = ref<ElFormInstance>()
const categoryTree = ref<CategoryVO[]>([])

/**
 * ImageUpload v-model 绑定 ossId 字符串。
 * watch 调 listByIds 查出 OssVO[]，从中取 url 存入业务数据。
 * --- 小知识 ---
 * computed = 纯计算，"x 变了 y 立即算出来"（同步派生值）
 * watch = 副作用，"x 变了去做一件事"（可以异步，比如调接口、写 localStorage）
 */
const mainImgOssId = ref('')
watch(mainImgOssId, async (val) => {
  if (val) {
    const res = await listByIds(val)
    spuBaseInfo.value.MainImgDesc = res.data?.[0]?.url || ''
  } else {
    spuBaseInfo.value.MainImgDesc = undefined
  }
})

const imgAlbumOssId = ref('')
watch(imgAlbumOssId, async (val) => {
  if (val) {
    const res = await listByIds(val)
    spuBaseInfo.value.ImgAlbum = res.data // OssVO[]
  } else {
    spuBaseInfo.value.ImgAlbum = []
  }
})

onMounted(async () => {
  // 加载分类数据
  const res = await listTreeCategory()
  categoryTree.value = res.data
})

const onCategoryChange = async (catelogId: string | number) => {
  // 选中的分类改变，重新查询品牌列表
  spuBaseInfo.value.brandId = undefined
  brandList.value = []
  if (!catelogId) return
  const res = await listBrands(catelogId)
  brandList.value = res.data
}

const nextStep = async () => {
  // 离开步骤0前：校验分类 + 表单校验
  if (activeStep.value === 0) {
    if (!spuBaseInfo.value.catalogId) {
      proxy?.$modal.msgWarning('请先选择所属分类')
      return
    }
    try {
      await basicFormRef.value?.validate()
    } catch {
      return
    }
  }

  // 根据当前步骤预加载下一步数据
  switch (activeStep.value) {
    case 0:
      // 将要进入第二步，加载属性分组
      if (spuBaseInfo.value.catalogId) {
        const res = await listAttrGroups(spuBaseInfo.value.catalogId)
        attrGroups.value = res.data
      }
      break
    case 1:
      // 将要进入第三步，加载销售属性
      if (spuBaseInfo.value.catalogId) {
        const res = await listSaleAttrs(spuBaseInfo.value.catalogId)
        saleAttrs.value = res.data
      }
      break
    case 2:
      // 笛卡尔积：Object.entries 展开为 [attrId, vals[]] 数组
      const entries = Object.entries(saleAttrMap.value).filter(([, vals]) => vals.length > 0)
      const saleAttrCombinations = entries.reduce(
        (acc, [attrId, vals]) => {
          const result: Record<string, string>[] = []
          for (const rec of acc) {
            for (const v of vals) {
              result.push({ ...rec, [attrId]: v })
            }
          }
          return result
        },
        [{} as Record<string, string>]
      )

      // 构建表格列头信息，key=attrId, label=attrName
      const idToName = new Map(saleAttrs.value.map((a) => [String(a.attrId), a.attrName]))
      attrColKeys.value = entries.map(([attrId]) => ({ key: String(attrId), label: idToName.get(String(attrId)) || String(attrId) }))

      // 构建表格的行数据
      skuTableData.value = saleAttrCombinations.map((saleAttr) => ({
        // saleAttrId : saleAttrVal
        ...saleAttr,
        skuName: '',
        skuDesc: '',
        skuDefaultImg: '',
        skuTitle: '',
        skuSubtitle: '',
        price: '',
        stock: 0,
        skuImages: []
      }))
      break
  }
  activeStep.value++
}

const submitForm = async () => {
  // 先提取 attrGroups数组 中的 attr，拍平，放到同一个数组，然后建立 attrId -> name 的映射
  const allAttrs = attrGroups.value.flatMap((g) => g.attrs)
  const idToName = new Map(allAttrs.map((a) => [String(a.attrId), a.attrName]))

  const releaseForm: ReleaseForm = {
    // spu: 基本信息 + 内嵌基本属性
    spu: {
      ...spuBaseInfo.value,
      baseAttrs: Object.entries(baseAttrMap.value)
        .filter(([, val]) => val !== undefined && val !== '')
        .map(([attrId, attrVal]) => ({
          attrId: Number(attrId),
          attrName: idToName.get(String(attrId)),
          attrValue: attrVal
        }))
    },

    // skus: 每个SKU内嵌自己的销售属性组合，key/label 直接从 attrColKeys 取
    skus: skuTableData.value.map((row) => ({
      skuName: row.skuName,
      skuDesc: row.skuDesc,
      skuDefaultImg: row.skuDefaultImg,
      skuTitle: row.skuTitle,
      skuSubtitle: row.skuSubtitle,
      price: row.price,
      stock: Number(row.stock) || 0,
      skuImages: row.skuImages,
      skuAttrs: attrColKeys.value.map((col) => ({
        attrId: Number(col.key),
        attrName: col.label,
        attrValue: row[col.key]
      }))
    }))
  }

  // 提交加载开启
  buttonLoading.value = true
  await addRelease(releaseForm).finally(() => (buttonLoading.value = false))
  proxy?.$modal.msgSuccess('操作成功')

  // 重置所有状态
  spuBaseInfo.value = { ...initBasicInfo }
  mainImgOssId.value = ''
  imgAlbumOssId.value = ''
  baseAttrMap.value = {}
  saleAttrMap.value = {}
  attrColKeys.value = []
  skuTableData.value = []
  activeStep.value = 0
}

/** SKU 图片点选：从图集中勾选该 SKU 的图片 */
const toggleSkuImage = (row: Record<string, any>, img: string) => {
  const current = (row.skuImages || []) as string[]
  const idx = current.indexOf(img)
  // 不 mutate 原数组，整体替换，避免触发 el-table 重渲染收起展开行
  row.skuImages = idx > -1 ? current.filter((_, i) => i !== idx) : [...current, img]
}
</script>

<style scoped lang="scss">
.release-page {
  /*
  不要设置height，除非是百分比，height默认是auto，由内容撑开。
  设置高度后，auto自动撑开会被限制，需要配合overflow处理溢出行为。
  */
  display: flex;
  justify-content: center;
  align-items: flex-start;
  padding: 24px;
}

.release-card {
  width: 100%;
  max-width: 1200px;
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

.attr-tabs {
  min-height: 300px;
}

.step-content-wide {
  width: 100%;
  min-height: 400px;
}

.sku-expand {
  padding: 16px 24px;
}

.sku-image-picker {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.sku-pickable-image {
  position: relative;
  cursor: pointer;
  border: 2px solid transparent;
  border-radius: 4px;
  overflow: hidden;
  transition: border-color 0.2s;

  &:hover {
    border-color: var(--el-color-primary-light-5);
  }

  &.is-selected {
    border-color: var(--el-color-primary);
  }
}

.pick-check {
  position: absolute;
  top: 2px;
  right: 2px;
  width: 18px;
  height: 18px;
  line-height: 18px;
  text-align: center;
  font-size: 12px;
  color: #fff;
  background: var(--el-color-primary);
  border-radius: 50%;
}

// 销售属性 label 与多选框之间加分隔
.sale-attr-form {
  :deep(.el-form-item__label) {
    padding-right: 16px;
    border-right: 2px solid var(--el-border-color);
    margin-right: 16px;
  }
}

// ImageUpload 缩略图尺寸调小
:deep(.el-upload--picture-card) {
  width: 100px;
  height: 100px;
}

:deep(.el-upload-list--picture-card .el-upload-list__item) {
  width: 100px;
  height: 100px;
}
</style>
