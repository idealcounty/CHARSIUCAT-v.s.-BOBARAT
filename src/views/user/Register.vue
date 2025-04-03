<script setup lang="ts">
import {ref, computed} from 'vue'
import {router} from '../../router'
import {userRegister} from "../../api/user.ts"
import {getAllStore} from "../../api/store.ts";

// 输入框值（需要在前端拦截不合法输入：是否为空+额外规则）
const name = ref('')
const identity = ref('')
const tel = ref('')
const address = ref('')
const password = ref('')
const storeId = ref(undefined)
const confirmPassword = ref('')

//对于商家用户，还需要在注册时选择所属商店，从而传入storeId。但由于Lab2才会开发商店模块，所以这里暂且设置唯一一个Id为1的商店1，待Lab2完善
interface StoreItem {
  id: number
  name: string
}

const storeList = ref<StoreItem[]>([])

getAllStore().then(res => {
  storeList.value = res.data.result
})

// 电话号码是否为空
const hasTelInput = computed(() => tel.value != '')
// 密码是否为空
const hasPasswordInput = computed(() => password.value != '')
// 重复密码是否为空
const hasConfirmPasswordInput = computed(() => confirmPassword.value != '')
// 地址是否为空
const hasAddressInput = computed(() => address.value != '')
// 身份是否为空
const hasIdentityChosen = computed(() => identity.value != '')
// 对于商家用户，商店Id是否为空
const hasStoreName = computed(() => storeId.value != undefined)
// 电话号码的规则
const chinaMobileRegex = /^1(3[0-9]|4[579]|5[0-35-9]|6[2567]|7[0-8]|8[0-9]|9[189])\d{8}$/
const telLegal = computed(() => chinaMobileRegex.test(tel.value))
// 重复密码的规则
const isPasswordIdentical = computed(() => password.value == confirmPassword.value)
// 注册按钮可用性
const registerDisabled = computed(() => {
  if (!hasIdentityChosen.value || !agree.value) {
    return true
  } else if (identity.value == 'CUSTOMER') {
    return !(hasTelInput.value && hasPasswordInput.value && hasConfirmPasswordInput && hasAddressInput.value &&
        telLegal.value && isPasswordIdentical.value)
  } else if (identity.value == 'STAFF') {
    return !(hasTelInput.value && hasPasswordInput.value && hasConfirmPasswordInput && hasAddressInput.value &&
        hasStoreName.value && telLegal.value && isPasswordIdentical.value)
  }
})

const loading = ref(false)
const agree = ref(false)
const agreeErrMsg = ref('')

// 注册按钮触发
function handleRegister() {
  // 密码处理
  // const md5: any = new Md5()
  // md5.appendAsciiStr(password.value)
  // password.value = md5.end()
  // confirmPassword.value = md5.end()
  if (registerDisabled.value) {
    return
  }
  userRegister({
    role: identity.value,
    name: name.value,
    phone: tel.value,
    password: password.value,
    address: address.value,
    storeId: storeId.value
  }).then(res => {
    if (res.data.code === '000') {  //类型守卫，它检查 res.data 对象中是否存在名为 code 的属性
      ElMessage({
        message: "注册成功！请登录账号",
        type: 'success',
        center: true,
      })
      router.push({path: "/login"})
    } else if (res.data.code === '400') {
      ElMessage({
        message: res.data.msg,
        type: 'error',
        center: true,
      })
    }
  })
}
</script>


<template>
  <div class="app">
    <div class="register">
      <div class="register-content">
        <div class="register-title">
          创建您的账户
        </div>


        <div class="name">
          <div class="label">
            Sbeam 账户名称
          </div>
          <div class="input-area">
            <input v-model="name" class="input">
          </div>
        </div>

        <div class="tel">
          <label class="label" v-if="!hasTelInput" for="tel">
            注册手机号
          </label>
          <label class="error-warn" v-else-if="!telLegal" for="tel">
            手机号不合法
          </label>
          <label class="label" v-else for="tel">
            注册手机号
          </label>
          <br/>
          <input id="tel" v-model="tel" class="input">
        </div>

        <div class="address">
          <div class="label">
            个人地址
          </div>
          <div class="input-area">
            <input v-model="address" class="input">
          </div>
        </div>

        <div class="password">
          <div class="label">
            账户密码
          </div>
          <div class="input-area">
            <input type="password" v-model="password" class="input">
          </div>
        </div>

        <div class="confirm-password">
          <label class="label" v-if="!hasConfirmPasswordInput">确认密码</label>
          <label v-else-if="!isPasswordIdentical" class="error-warn">密码不一致</label>
          <label class="label" v-else>确认密码</label>
          <br/>
          <div class="input-area">
            <input type="password" v-model="confirmPassword" class="input">
          </div>
        </div>
        <div class="identity">
          <div class="label">
            身份
          </div>
          <div class="select-area">
            <select name="identity"
                    id="identity"
                    v-model="identity"
                    class="select">
              <option value="CUSTOMER">顾客</option>
              <option value="STAFF">商家</option>
            </select>
          </div>
        </div>

        <div class="store" v-if="identity==='STAFF'">
          <div class="label">所属商店</div>
          <div class="select-area">
            <select name="store"
                    id="store"
                    v-model="storeId"
                    class="select">
              <option v-for="item in storeList" :value="item.id" :key="item.id" v-text="item.name"></option>
            </select>

          </div>
        </div>

        <label class="agree">
          <input v-model="agree" class="agree-input" :class="{ error: agreeErrMsg }" type="checkbox"
                 @change="agreeErrMsg = ''">
          我已年满 13 周岁并同意<a href="" target="_blank">《Sbeam 订户协议》</a>和<a href="" target="_blank">《Sbeam
          隐私政策》</a>的条款。
        </label>

        <div v-loading="loading" class="register-button" @click.prevent="handleRegister">
          完成
        </div>

      </div>
    </div>
  </div>

</template>


<style scoped>
.app {
  background-color: #1f2428;
  min-height: calc(100vh);
  font-family: "Motiva Sans", sans-serif;
}

.register {
  position: relative;
  z-index: 1;
  display: flex;
  flex-direction: column;
  row-gap: 32px;
  padding: 80px 0 150px 0;
  background-image: url("../../assets/acct_creation_bg.png");
  background-position: left top;
  background-repeat: no-repeat;
  background-size: cover;
}

.register-content {
  position: relative;
  display: flex;
  flex-direction: column;
  gap: 25px;
  width: 800px;
  padding: 0 36px;
  margin: 0 auto;
}

.register-title {
  margin-bottom: 10px;
  color: #ffffff;
  font-size: 32px;
  font-weight: 200;
}

.label {
  margin-bottom: 2px;
  color: #b8b6b4;
  font-size: 14px;
}

.input-area {
  position: relative;
  display: inline-block;
}

.input {
  box-sizing: border-box;
  width: 300px;
  padding: 8px;
  border: none;
  border-radius: 2px;
  color: #ffffff;
  font-size: 15px;
  font-family: Arial, sans-serif;
  background-color: #32353c;

  &:hover {
    background-color: #393c44;
  }

  &.error {
    outline: 1px solid #c15755;
  }
}

.select {
  background-color: #32353c;
  color: #b8b6b4;
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 3px;
  box-shadow: 1px 1px 0px #000;
  width: 300px;
  padding: 8px;
}

.err-msg {
  position: absolute;
  left: calc(100% + 20px);
  top: 50%;
  padding: 8px;
  border-radius: 4px;
  color: #ffffff;
  background-color: #a0382b;
  font-size: 12px;
  white-space: nowrap;
  transform: translateY(-50%);

  &::before {
    content: "";
    position: absolute;
    right: 100%;
    top: 50%;
    display: inline-block;
    border-top: 8px solid transparent;
    border-right: 8px solid #a0382b;
    border-bottom: 8px solid transparent;
    transform: translate(1px, -50%);
  }
}

.agree {
  position: relative;
  display: flex;
  align-items: center;
  width: max-content;
  color: #b8b6b4;
  font-size: 14px;
  cursor: pointer;

  a {
    color: #ffffff;
    text-decoration: none;

    &:hover {
      color: #66c0f4;
    }
  }
}

.agree-input {
  position: relative;
  box-sizing: border-box;
  width: 20px;
  height: 20px;
  padding: 10px;
  margin: 0 6px 0 0;
  //border: 1px solid #;
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

  &.error {
    outline: 1px solid #c15755;
  }
}

.register-button {
  box-sizing: border-box;
  width: 130px;
  border-radius: 2px;
  margin-top: 10px;
  color: #ffffff;
  font-size: 15px;
  line-height: 36px;
  text-align: center;
  background: linear-gradient(90deg, #06BFFF 0%, #2D73FF 100%);
  cursor: pointer;

  &:hover {
    background: linear-gradient(90deg, #06BFFF 30%, #2D73FF 100%);
  }
}

.error-warn {
  color: red;
  margin-bottom: 2px;
  font-size: 14px;
}

</style>
