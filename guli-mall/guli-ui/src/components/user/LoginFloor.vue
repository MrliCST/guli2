<template>
  <section class="loginFloor">
    <div class="loginFloor__inner">
      <!-- logo -->
      <div class="logoArea">
        <img :src="'/img/logo1.png'" alt="谷粒商城" />
      </div>

      <!-- 登录框 -->
      <div class="loginBox">
        <!-- Tab 导航 -->
        <div class="loginBox__tabs">
          <div class="tab" :class="{ active: tab === 'qr' }" @click="tab = 'qr'">
            <h3>扫描登录</h3>
          </div>
          <div class="tab" :class="{ active: tab === 'form' }" @click="tab = 'form'">
            <h3>账户登录</h3>
          </div>
        </div>

        <!-- Tab 内容 -->
        <div class="loginBox__content">
          <!-- 扫码登录 -->
          <div v-show="tab === 'qr'" class="qrPane">
            <p>二维码登录，暂为官网二维码</p>
            <img :src="'/img/wx_cz.jpg'" alt="二维码" />
          </div>

          <!-- 账户登录 -->
          <div v-show="tab === 'form'" class="formPane">
            <div class="inputRow">
              <span class="icon userIcon"></span>
              <input v-model="form.username" type="text" placeholder="邮箱/用户名/手机号" />
            </div>
            <div class="inputRow">
              <span class="icon pwdIcon"></span>
              <input v-model="form.password" type="password" placeholder="请输入密码" />
            </div>
            <div class="settingRow">
              <label class="autoLogin">
                <input type="checkbox" v-model="form.autoLogin" />
                <span>自动登录</span>
              </label>
              <a href="#" class="forget">忘记密码？</a>
            </div>
            <button class="loginBtn" @click.prevent="handleLogin">登 录</button>

            <!-- 第三方登录 -->
            <div class="otherLogin">
              <div class="otherLogin__icons">
                <img v-for="src in socialIcons" :key="src" :src="src" alt="" />
              </div>
              <router-link to="/register" class="registerLink">立即注册</router-link>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'

const tab = ref('form')
const form = reactive({ username: '', password: '', autoLogin: true })
const socialIcons = ['/img/qq.png', '/img/sina.png', '/img/ali.png', '/img/weixin.png']

function handleLogin() {
  if (!form.username || !form.password) return
  window.location.href = '/'
}
</script>

<style scoped>
.loginFloor {
  background: linear-gradient(135deg, #f0f4f8 0%, #e8edf5 100%);
  min-height: 500px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.loginFloor__inner {
  width: var(--page-width);
  display: grid;
  grid-template-columns: 1fr 400px;
  gap: 40px;
  align-items: center;
}

/* logo */
.logoArea { text-align: center; }
.logoArea img { width: 300px; }

/* 登录框 */
.loginBox {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0,0,0,0.08);
}
.loginBox__tabs {
  display: grid;
  grid-template-columns: 1fr 1fr;
  border-bottom: 1px solid #eee;
}
.tab {
  padding: 16px 0;
  text-align: center;
  cursor: pointer;
  border-bottom: 2px solid transparent;
}
.tab.active { border-bottom-color: var(--theme-red); }
.tab h3 { margin: 0; font-size: 16px; color: #666; font-weight: normal; }
.tab.active h3 { color: var(--theme-red); font-weight: bold; }

.loginBox__content { padding: 30px; }

/* 扫码 */
.qrPane { text-align: center; }
.qrPane p { font-size: 13px; color: #999; margin: 0 0 16px; }
.qrPane img { width: 180px; height: 180px; border-radius: 4px; }

/* 表单 */
.formPane { display: grid; gap: 16px; }
.inputRow {
  display: flex;
  align-items: center;
  border: 1px solid #ddd;
  border-radius: 4px;
  overflow: hidden;
}
.inputRow:focus-within { border-color: var(--theme-red); }
.icon { width: 40px; height: 40px; flex-shrink: 0; background: #f5f5f5; position: relative; }
.userIcon::after { content: '👤'; position: absolute; top: 50%; left: 50%; transform: translate(-50%,-50%); font-size: 16px; }
.pwdIcon::after { content: '🔒'; position: absolute; top: 50%; left: 50%; transform: translate(-50%,-50%); font-size: 16px; }
.inputRow input {
  flex: 1;
  height: 40px;
  border: none;
  padding: 0 12px;
  font-size: 14px;
  outline: none;
}

.settingRow {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 13px;
}
.autoLogin { display: flex; align-items: center; gap: 4px; color: #666; cursor: pointer; }
.forget { color: #666; text-decoration: none; }
.forget:hover { color: var(--theme-red); }

.loginBtn {
  background: var(--theme-red);
  color: #fff;
  border: none;
  height: 44px;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
  font-weight: bold;
}
.loginBtn:hover { opacity: 0.9; }

.otherLogin {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-top: 1px solid #eee;
  padding-top: 16px;
}
.otherLogin__icons { display: flex; gap: 10px; }
.otherLogin__icons img { width: 32px; height: 32px; cursor: pointer; }
.registerLink { font-size: 13px; color: var(--theme-red); text-decoration: none; }
</style>
