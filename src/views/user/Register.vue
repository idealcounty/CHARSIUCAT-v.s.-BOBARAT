<script setup lang="ts">
import {ref, computed} from 'vue'
import {router} from '../../router'
import {userRegister} from "../../api/user.ts"
import { getAllStore } from "../../api/store.ts";

// 输入框值（需要在前端拦截不合法输入：是否为空+额外规则）
const name = ref('')
const identity = ref('')
const tel = ref('')
const address = ref('')
const password = ref('')
const confirmPassword = ref('')

//对于商家用户，还需要在注册时选择所属商店，从而传入storeId。但由于Lab2才会开发商店模块，所以这里暂且设置唯一一个Id为1的商店1，待Lab2完善
interface StoreItem {
  id: number
  name: string
}

const storeList = ref<StoreItem[]>([])
const storeId = ref(undefined)
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
  if (!hasIdentityChosen.value) {
    return true
  } else if (identity.value == 'CUSTOMER') {
    return !(hasTelInput.value && hasPasswordInput.value && hasConfirmPasswordInput && hasAddressInput.value &&
        telLegal.value && isPasswordIdentical.value)
  } else if (identity.value == 'STAFF') {
    return !(hasTelInput.value && hasPasswordInput.value && hasConfirmPasswordInput && hasAddressInput.value &&
        hasStoreName.value && telLegal.value && isPasswordIdentical.value)
  }
})

// 注册按钮触发
function handleRegister() {
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
  <el-main class="main-frame bgimage">
    <el-card class="login-card">
      <div>
        <h1>创建一个新的账户</h1>

        <el-form>
          <el-row>
            <el-col :span="15">
              <el-form-item>
                <label for="name">昵称</label>
                <el-input id="name"
                          v-model="name"
                          placeholder="请输入昵称"/>
              </el-form-item>
            </el-col>

            <el-col :span="1"></el-col>

            <el-col :span="8">
              <el-form-item>
                <label for="identity">身份</label>
                <el-select id="identity"
                           v-model="identity"
                           placeholder="请选择"
                           style="width: 100%;"
                >
                  <el-option value="CUSTOMER" label="顾客"/>
                  <el-option value="STAFF" label="商家"/>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="8">
              <el-form-item>

                <label v-if="!hasTelInput" for="tel">
                  注册手机号
                </label>
                <label v-else-if="!telLegal" for="tel" class="error-warn">
                  手机号不合法
                </label>
                <label v-else for="tel">
                  注册手机号
                </label>

                <el-input id="tel"
                          v-model="tel" :class="{'error-warn-input' :(hasTelInput && !telLegal)}"
                          placeholder="请输入手机号"/>
              </el-form-item>
            </el-col>

            <el-col :span="1"></el-col>

            <el-col :span="15" v-if="identity!=='STAFF'">
              <el-form-item>
                <label for="address">
                  地址
                </label>
                <el-input id="address"
                          v-model="address"
                          placeholder="请输入地址"/>
              </el-form-item>
            </el-col>

            <el-col :span="7" v-if="identity==='STAFF'">
              <el-form-item>
                <label for="address">
                  地址
                </label>
                <el-input id="address"
                          v-model="address"
                          placeholder="请输入地址"/>
              </el-form-item>
            </el-col>

            <el-col :span="1" v-if="identity==='STAFF'"></el-col>

            <el-col :span="7" v-if="identity==='STAFF'">
              <el-form-item>
                <label for="address">
                  所属商店
                </label>
                <el-select id="identity" v-model="storeId" placeholder="请选择" style="width: 100%;">
                  <el-option v-for="item in storeList" :key="item.id" :label="item.name"
                             :value="item.id" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item>
            <label for="password">密码</label>
            <el-input type="password" id="password" v-model="password" placeholder="••••••••"/>
          </el-form-item>

          <el-form-item>
            <label v-if="!hasConfirmPasswordInput">确认密码</label>
            <label v-else-if="!isPasswordIdentical" class="error-warn">密码不一致</label>
            <label v-else>确认密码</label>

            <el-input type="password" id="confirm-password" v-model="confirmPassword"
                      :class="{'error-warn-input' :(hasConfirmPasswordInput && !isPasswordIdentical)}"
                      placeholder="••••••••"/>
          </el-form-item>

          <span class="button-group">
            <el-button @click.prevent="handleRegister" :disabled="registerDisabled" type="primary">
              创建账户
            </el-button>

            <router-link to="/login" v-slot="{navigate}">
              <el-button @click="navigate">
                去登录
              </el-button>
            </router-link>
          </span>

        </el-form>
      </div>

    </el-card>
  </el-main>

</template>


<style scoped>
.main-frame {
  width: 100%;
  height: 100%;

  display: flex;
  align-items: center;
  justify-content: center;
}

.bgimage {
  background-image: url("../../assets/shopping-1s-1084px.svg");
}

.login-card {
  width: 60%;
  padding: 10px;
}

.error-warn {
  color: red;
}

.error-warn-input {
  --el-input-focus-border-color: red;
}

.button-group {
  padding-top: 10px;
  display: flex;
  flex-direction: row;
  gap: 30px;
  align-items: center;
  justify-content: right;
}
</style>
