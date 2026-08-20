<template>
  <section class="registerFloor">
    <div class="registerFloor__inner">
      <!-- logo -->
      <div class="logoArea">
        <img :src="'/img/logo1.png'" alt="谷粒商城" />
      </div>

      <!-- 注册框 -->
      <div class="registerBox">
        <div class="registerBox__header">
          <h3>注册新用户</h3>
          <span class="go">我有账号，去<router-link to="/login">登陆</router-link></span>
        </div>

        <form class="registerForm" @submit.prevent="handleRegister">
          <div class="formRow">
            <label class="formRow__label">手机号：</label>
            <div class="formRow__control">
              <input v-model="form.phone" type="text" placeholder="请输入你的手机号" />
            </div>
          </div>
          <div class="formRow">
            <label class="formRow__label">验证码：</label>
            <div class="formRow__control codeRow">
              <input v-model="form.code" type="text" placeholder="验证码" />
              <button type="button" class="codeBtn" @click="sendCode">{{ codeText }}</button>
            </div>
          </div>
          <div class="formRow">
            <label class="formRow__label">登录密码：</label>
            <div class="formRow__control">
              <input v-model="form.password" type="password" placeholder="设置登录密码" />
            </div>
          </div>
          <div class="formRow">
            <label class="formRow__label">确认密码：</label>
            <div class="formRow__control">
              <input v-model="form.confirm" type="password" placeholder="再次确认密码" />
            </div>
          </div>
          <div class="formRow">
            <label class="formRow__label"></label>
            <div class="formRow__control agreeRow">
              <input type="checkbox" v-model="form.agreed" />
              <span>同意协议并注册《谷粒商城用户协议》</span>
            </div>
          </div>
          <div class="formRow">
            <label class="formRow__label"></label>
            <div class="formRow__control">
              <button type="submit" class="registerBtn">完成注册</button>
            </div>
          </div>
        </form>
      </div>
    </div>
  </section>
</template>

<script setup>
import { reactive, ref } from 'vue'

const form = reactive({ phone: '', code: '', password: '', confirm: '', agreed: true })
const codeText = ref('获取验证码')

let timer = null
function sendCode() {
  if (timer) return
  let count = 60
  codeText.value = `${count}秒后重发`
  timer = setInterval(() => {
    count--
    codeText.value = `${count}秒后重发`
    if (count <= 0) { clearInterval(timer); timer = null; codeText.value = '获取验证码' }
  }, 1000)
}

function handleRegister() {
  if (!form.agreed) return
  window.location.href = '/login'
}
</script>

<style scoped>
.registerFloor {
  background: linear-gradient(135deg, #f0f4f8 0%, #e8edf5 100%);
  min-height: 500px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.registerFloor__inner {
  width: var(--page-width);
  display: grid;
  grid-template-columns: 1fr 500px;
  gap: 40px;
  align-items: center;
}

/* logo */
.logoArea { text-align: center; }
.logoArea img { width: 300px; }

/* 注册框 */
.registerBox {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.08);
  overflow: hidden;
}
.registerBox__header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
  border-bottom: 1px solid #eee;
}
.registerBox__header h3 { margin: 0; font-size: 16px; color: #333; }
.go { font-size: 13px; color: #999; }
.go a { color: var(--theme-red); text-decoration: none; }

/* 表单 */
.registerForm { padding: 24px; display: grid; gap: 16px; }
.formRow {
  display: grid;
  grid-template-columns: 90px 1fr;
  gap: 10px;
  align-items: center;
}
.formRow__label { font-size: 13px; color: #666; text-align: right; }
.formRow__control input {
  width: 100%;
  height: 38px;
  border: 1px solid #ddd;
  border-radius: 4px;
  padding: 0 12px;
  font-size: 14px;
  outline: none;
  box-sizing: border-box;
}
.formRow__control input:focus { border-color: var(--theme-red); }
.codeRow { display: grid; grid-template-columns: 1fr 120px; gap: 8px; }
.codeBtn {
  height: 38px;
  border: 1px solid var(--theme-red);
  background: #fff;
  color: var(--theme-red);
  border-radius: 4px;
  font-size: 12px;
  cursor: pointer;
}
.codeBtn:hover { background: rgba(200,22,35,0.05); }
.agreeRow { display: flex; align-items: center; gap: 6px; font-size: 13px; color: #666; }
.registerBtn {
  width: 100%;
  height: 42px;
  background: var(--theme-red);
  color: #fff;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
}
.registerBtn:hover { opacity: 0.9; }
</style>
