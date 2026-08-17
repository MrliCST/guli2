import { defineConfig, loadEnv } from 'vite'
import createPlugins from './vite/plugins'
import autoprefixer from 'autoprefixer' // css自动添加兼容性前缀
import path from 'path'

export default defineConfig(({ mode, command }) => {
  const env = loadEnv(mode, process.cwd())
  return {
    // 1. 开启 Source Map 支持（开发与构建环境均生成映射）
    build: {
      sourcemap: true
    },
    // 部署生产环境和开发环境下的URL。
    base: env.VITE_APP_CONTEXT_PATH,
    resolve: {
      alias: {
        '@': path.resolve(__dirname, './src')
      },
      extensions: ['.mjs', '.js', '.ts', '.jsx', '.tsx', '.json', '.vue']
    },
    // https://cn.vitejs.dev/config/#resolve-extensions
    plugins: createPlugins(env, command === 'build'),
    server: {
      // 2. 确保开发服务器响应头包含 Source Map 映射信息
      sourcemapIgnoreList: false,
      host: '0.0.0.0',
      port: Number(env.VITE_APP_PORT),
      open: true,
      proxy: {
        [env.VITE_APP_BASE_API]: {
          target: 'http://localhost:8080',
          changeOrigin: true,
          ws: true,
          rewrite: (path) => path.replace(new RegExp('^' + env.VITE_APP_BASE_API), '')
        }
      }
    },
    css: {
      preprocessorOptions: {
        scss: {
          // additionalData: '@use "@/assets/styles/variables.module.scss as *";'
          // javascriptEnabled: true
        }
      },
      postcss: {
        plugins: [
          // 浏览器兼容性
          autoprefixer(),
          {
            postcssPlugin: 'internal:charset-removal',
            AtRule: {
              charset: (atRule) => {
                atRule.remove()
              }
            }
          }
        ]
      }
    },
    // 预编译
    optimizeDeps: {
      include: [
        'vue',
        'vue-router',
        'pinia',
        'axios',
        '@vueuse/core',
        'echarts',
        'vue-i18n',
        '@vueup/vue-quill',
        'image-conversion',
        'element-plus/es/components/**/css'
      ]
    }
  }
})
