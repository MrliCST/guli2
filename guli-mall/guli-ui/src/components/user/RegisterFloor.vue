<template>
  <section class="registerFloor">
    <div class="registerFloor_inner">
      <!-- 注册框 -->
      <div class="registerBox">
        <div class="registerBox_header">
          <h3>注册新用户</h3>
          <span class="go">我有账号，去<router-link to="/login">登陆</router-link></span>
        </div>

        <form class="registerForm" @submit.prevent="handleRegister">
          <div class="inputRow">
            <span class="icon phoneIcon"></span>
            <input v-model="form.phone" type="text" placeholder="请输入你的手机号" />
          </div>
          <div class="inputRow codeRow">
            <span class="icon codeIcon"></span>
            <input v-model="form.code" type="text" placeholder="验证码" />
            <button type="button" class="codeBtn" @click="sendCode">{{ codeText }}</button>
          </div>
          <div class="inputRow">
            <span class="icon pwdIcon"></span>
            <input v-model="form.password" type="password" placeholder="设置登录密码" />
          </div>
          <div class="inputRow">
            <span class="icon pwdIcon"></span>
            <input v-model="form.confirm" type="password" placeholder="再次确认密码" />
          </div>
          <div class="settingRow">
            <label class="agreeCheck">
              <input type="checkbox" v-model="form.agreed" />
              <span>同意协议并注册《谷粒商城用户协议》</span>
            </label>
          </div>
          <button class="registerBtn" @click.prevent="handleRegister">完成注册</button>
        </form>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'

const form = reactive({ phone: '', code: '', password: '', confirm: '', agreed: true })
const codeText = ref('获取验证码')

let timer: ReturnType<typeof setInterval> | null = null
function sendCode() {
  if (timer) return
  let count: number = 60
  codeText.value = `${count}秒后重发`
  timer = setInterval(() => {
    count--
    codeText.value = `${count}秒后重发`
    if (count <= 0) { clearInterval(timer!); timer = null; codeText.value = '获取验证码' }
  }, 1000)
}

function handleRegister() {
  if (!form.agreed) return
  window.location.href = '/login'
}
</script>

<style scoped lang="scss">
.registerFloor {
  height: 100%;

  .registerFloor_inner {
    height: 100%;
    display: flex;
    justify-content: flex-end;
    align-items: center;
    padding-right: 80px;
    background-image: url('@/assets/user/login/loginbg.png');
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;
  }
}

.registerBox {
  width: 400px;
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0,0,0,0.08);

  .registerBox_header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16px 24px;
    border-bottom: 1px solid #eee;

    h3 { margin: 0; font-size: 16px; color: #333; }
    .go { font-size: 13px; color: #999; }
    .go a { color: #c81623; text-decoration: none; }
  }
}

.registerForm {
  padding: 30px;
  display: grid;
  gap: 16px;

  .inputRow {
    display: flex;
    align-items: center;
    border: 1px solid #ddd;
    border-radius: 4px;
    overflow: hidden;

    &:focus-within { border-color: #c81623; }

    .icon {
      width: 40px;
      height: 40px;
      flex-shrink: 0;
      background: #f5f5f5;
      position: relative;

      &::after {
        content: '📝';
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%,-50%);
        font-size: 16px;
      }
    }

    .pwdIcon::after { content: '🔒'; }
    .phoneIcon::after { content: '📱'; }
    .codeIcon::after { content: '🔢'; }

    input {
      flex: 1;
      height: 40px;
      border: none;
      padding: 0 12px;
      font-size: 14px;
      outline: none;
    }
  }

  .codeRow {
    .codeBtn {
      height: 40px;
      border: none;
      border-left: 1px solid #ddd;
      background: #fff;
      color: #c81623;
      padding: 0 12px;
      font-size: 12px;
      cursor: pointer;
      white-space: nowrap;

      &:hover { background: rgba(200,22,35,0.05); }
    }
  }

  .settingRow {
    display: flex;
    align-items: center;
    font-size: 13px;
  }

  .agreeCheck {
    display: flex;
    align-items: center;
    gap: 4px;
    color: #666;
    cursor: pointer;
  }

  .registerBtn {
    background: #c81623;
    color: #fff;
    border: none;
    height: 44px;
    border-radius: 4px;
    font-size: 16px;
    cursor: pointer;
    font-weight: bold;

    &:hover { opacity: 0.9; }
  }
}
</style>
