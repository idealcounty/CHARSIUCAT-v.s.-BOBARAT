<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { userInfo, userLogin } from "../../api/user.ts"

const tel = ref('')
const password = ref('')
const rememberMe = ref(true)
const errMsg = ref('')
const loading = ref(false)

const route = useRoute()
const router = useRouter()

const hasTelInput = computed(() => tel.value != '')
const hasPasswordInput = computed(() => password.value != '')
const chinaMobileRegex = /^1(3[0-9]|4[579]|5[0-35-9]|6[2567]|7[0-8]|8[0-9]|9[189])\d{8}$/
const telLegal = computed(() => chinaMobileRegex.test(tel.value))
const loginDisabled = computed(() => {
  return !(hasTelInput.value && telLegal.value && hasPasswordInput.value)
})

// 登录按钮触发
function handleLogin() {
  userLogin({
    phone: tel.value,
    password: password.value
  }).then(res => {
    console.log(res.data.code)
    if (res.data.code === '000') {
      const token = res.data.result
      sessionStorage.setItem('token', token)

      userInfo().then(res => {
        sessionStorage.setItem('name', res.data.result.name)
        sessionStorage.setItem('role', res.data.result.role)
        sessionStorage.setItem('storeId', res.data.result.storeId)
        if(res.data.result.role == "STAFF")
          router.push({path: "/administrator"})
        else if(res.data.result.role == "CUSTOMER")
          router.push({path: "/dashboard"})
      })
    } else if (res.data.code === '400') {
      errMsg.value = '登陆失败'
      password.value = ''
    }
  })
      .catch(error => {
        console.error("登录请求失败:", error);
        errMsg.value = '手机号或密码错误';
      })
      .finally(() => {
        loading.value = false; // 取消加载状态
      });
}

function clearErrMsg() {
  errMsg.value = ''
}

function JumpToRegister() {
  router.push({path:'/register'})
}
</script>

<template>
  <div class="app">
    <div class="login">
      <div class="login-title">登录</div>
      <div class="login-content">
        <div class="login-content-left">
          <!--手机号-->
          <div class="telephonenumber">
            <div class="telephonenumber-label">用手机号登录</div>
            <input v-model="tel" class="telephone-input" name="telephone" @input="clearErrMsg()">
          </div>
          <!--密码-->
          <div class="password">
            <div class="password-label">密码</div>
            <input v-model="password" class="password-input" name="password" @input="clearErrMsg()">
            <!--错误提示-->
            <div class="error-message">{{ errMsg }}</div>
          </div>
          <!--记住我-->
          <label class="remember">
            <input v-model="rememberMe" class="remember-input" type="checkbox">记住我
          </label>
          <!--登录按钮-->
          <div v-loading="loading" class="login-button" @click.prevent="handleLogin" :disabled="loginDisabled">登录</div>
          <!--帮助-->
          <RouterLink class="login-help" to="">请求帮助，我无法登录。</RouterLink>
        </div>

        <div class="login-content-right">
          <!--二维码登录-->
          <div class="qrcode-label">或者用二维码登录</div>
          <div class="qrcode">
            <img src="../../assets/login_qr_code.png" alt="">
          </div>
          <div class="qrcode-tip">通过二维码使用 SBEAM 手机应用登录</div>
        </div>
      </div>
    </div>
    <div class="bottom">
      <div class="bottom-left">
        <div class="register-title">首次使用 SBEAM?</div>
        <div class="register-button" @click="JumpToRegister" :disabled="loginDisabled">创建账户</div>
      </div>
      <div class="bottom-right">
        <!--注册-->
        <div>创建账户既免费又简单。探索成千上万款游戏,与数百万新朋友一起畅玩吧!</div>
        <RouterLink class="learn-more" to="">了解更多关于SBEAM的信息</RouterLink>
      </div>
    </div>
  </div>
</template>

<style scoped>
@import url('https://fonts.cdnfonts.com/css/motiva-sans');

.app {
  background-color: #181a21;
  min-height: calc(100vh - 104px);
  font-family: "Motiva Sans", sans-serif;
}

.login {
  display: flex;
  flex-direction: column;
  align-items: center;
  row-gap: 32px;
  padding: 80px 0 150px 0;
  background-image: url("../../assets/new_login_bg_strong_mask.jpg");
  background-position: center top;
  background-repeat: no-repeat;
}

.login-title {
  box-sizing: border-box;
  width: 700px;
  padding: 8px 16px;
  color: #ffffff;
  font-size: 32px;
  font-weight: 200;
}

.login-content {
  display: flex;
  column-gap: 40px;
  box-sizing: border-box;
  width: 700px;
  padding: 24px 32px;
  border-radius: 4px;
  background-color: #181a21;
}

.login-content-left {
  display: flex;
  flex-direction: column;
  row-gap: 12px;
  flex-grow: 1;
}

.telephonenumber-label, .qrcode-label {
  margin-bottom: 2px;
  color: #1999ff;
  font-size: 12px;
  user-select: none;
}

.password-label {
  margin-bottom: 2px;
  color: #afafaf;
  font-size: 12px;
  user-select: none;
}

.telephone-input, .password-input {
  box-sizing: border-box;
  width: 100%;
  padding: 10px;
  border: none;
  border-radius: 2px;
  outline: none;
  color: #ffffff;
  font-size: 15px;
  font-family: Arial, sans-serif;
  background-color: #32353c;

  &:hover {
    background-color: #393c44;
  }
}

.remember {
  display: flex;
  align-items: center;
  color: #afafaf;
  font-size: 12px;
  cursor: pointer;
}

.remember-input {
  position: relative;
  box-sizing: border-box;
  width: 20px;
  height: 20px;
  padding: 10px;
  margin: 0 6px 0 0;
  border-radius: 2px;
  background-color: #32353c;
  cursor: pointer;
  appearance: none;

  &:checked::after {
    content: "✔";
    position: absolute;
    left: 0;
    top: 0;
    display: inline-block;
    width: 20px;
    height: 20px;
    color: #ffffff;
    font-size: 15px;
    line-height: 20px;
    text-align: center;
  }

  &:hover {
    background-color: #393c44;
  }
}

.login-button {
  align-self: center;
  box-sizing: border-box;
  width: 270px;
  padding: 12px;
  border-radius: 2px;
  color: #ffffff;
  font-size: 16px;
  text-align: center;
  background: linear-gradient(90deg, #06BFFF 0%, #2D73FF 100%);
  cursor: pointer;

  &:hover {
    background: linear-gradient(90deg, #06BFFF 30%, #2D73FF 100%);
  }
}

.register-title {
  font-family: "Motiva Sans", sans-serif;
  font-weight: bold;
  color: white;
  font-size: 24px;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.6);
  letter-spacing: 0.5px;
}

.register-button {
  width: 100px;
  padding: 12px;
  border-radius: 4px;
  color: white;
  font-size: 16px;
  font-weight: bold;
  text-align: center;
  background: linear-gradient(90deg, #06BFFF 0%, #2D73FF 100%);
  cursor: pointer;
  border: none;
  transition: background 0.3s;
}

.register-button:hover {
  background: linear-gradient(90deg, #06BFFF 30%, #2D73FF 100%);
}

.login-error {
  align-self: center;
  height: 16px;
  color: #c15755;
  font-size: 12px;
}

.error-message {
  color: #c15755;
  font-size: 12px;
  margin-top: 4px;
}

.login-help {
  align-self: center;
  color: #afafaf;
  font-size: 12px;

  &:hover {
    color: #c9c9c9;
  }
}

.qrcode {
  width: 200px;
  height: 200px;
  margin-bottom: 8px;
  border-radius: 8px;
  overflow: hidden;
  user-select: none;

  img {
    width: 100%;
    height: 100%;
  }
}

.qrcode-tip {
  color: #afafaf;
  font-size: 12px;
  text-align: center;
}

.bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 700px;
  margin: 50px auto;
  padding: 40px 0;
  color: #b8b6b4;
  border-top: 1px solid rgba(255, 255, 255, 0.2);
}

.bottom-left {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 300px;
  text-align: center;
}

.learn-more {
  color: #b8b6b4;
  font-size: 14px;

  &:hover {
    text-decoration: underline;
    color: #ffffff;
  }
}

.bottom-right {
  width: 360px;
  color: #b8b6b4;
  font-size: 14px;
  line-height: 1.6;
}

.join-button {
  padding: 0 15px;
  border: 1px solid rgba(255, 255, 255, 0.4);
  border-radius: 2px;
  margin: 40px 0 10px 0;
  color: #ffffff;
  font-size: 15px;
  line-height: 30px;
  text-decoration: none;

  &:hover {
    border-color: #ffffff;
  }
}
</style>
