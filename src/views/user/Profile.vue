<script setup lang="ts">
import {onMounted, ref} from "vue";
import {uploadImage} from "../../api/tools.ts";
import {userInfo,userInfoUpdate} from "../../api/user.ts";

const userId = ref(0)
const userName = ref('')
const userPhone = ref('')
const userPassword = ref('')
const userAddress = ref('')
const userRole = ref('')
const userCreateTime = ref('')
const userBalance = ref(0)
const userAvatar = ref('')

const name = ref('')
const password = ref('')
const address = ref('')

const AvatarUrl = ref('')
const AvatarUrlList = ref([])

const current = ref(1)

const loading = ref(false)
const avatarIsUpdated = ref(false)

async function getUserInfo() {
  const res = await userInfo()
  console.log(res.data.code)
  if (res.data.code === '000') {
    const result = res.data.result
    userId.value = result.id
    userName.value = result.name
    userPhone.value = result.userPhone
    userPassword.value = result.password
    userAddress.value = result.address
    userRole.value = result.role
    userCreateTime.value = result.createTime
    userBalance.value = result.balance
    userAvatar.value = result.avatar

    name.value = userName.value
    address.value = userAddress.value
    AvatarUrl.value = userAvatar.value || 'https://steam-1314488277.cos.ap-guangzhou.myqcloud.com/assets%2Fdefault_avatar.jpg'
  } else if (res.data.code === '400') {
    console.log('未登录')
  }
}

onMounted(async () => {
  await getUserInfo()
})

function uploadHttpRequest() {
  return new XMLHttpRequest()
}

function handleChangeAvatar(file: any, fileList: any) {
  AvatarUrlList.value = fileList
  avatarIsUpdated.value = true
  console.log(AvatarUrlList.value[0])
}

function handleRemoveAvatar(file: any, fileList: any) {
  AvatarUrlList.value = fileList
  avatarIsUpdated.value = false
  console.log(AvatarUrlList.value[0])
}

async function generateAvatarUrl() {
  let formData = new FormData()
  formData.append('file', (AvatarUrlList.value[0] as any).raw)
  const imageResponse = await uploadImage(formData)
  AvatarUrl.value = imageResponse.data.result
  console.log('头像上传返回 URL：', AvatarUrl.value)
}

async function handleUpdateUser() {
  if (avatarIsUpdated.value) {
    await generateAvatarUrl()
  }
  console.log(userId.value)
  console.log(name.value,password.value,address.value,AvatarUrl.value)
  
  const updateData: any = {
    name: name.value,
    address: address.value,
    avatar: AvatarUrl.value,
  }
  
  if (password.value && password.value.trim() !== '') {
    updateData.password = password.value
  }
  
  await userInfoUpdate(Number(userId.value), updateData).then(res => {
    console.log(res)
    if (res.data.code === '000') {
      ElMessage({
        message: "更新成功！",
        type: 'success',
        center: true,
      })
      password.value = ''
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
    <div class="app_background"></div>
    <div class="pagecontent">
      <div class="profile_small_header_bg">
        <div class="profile_small_header_texture">
          <img class="profile_small_header_avatar" :src="userAvatar || 'https://steam-1314488277.cos.ap-guangzhou.myqcloud.com/assets%2Fdefault_avatar.jpg'">
          <div class="profile_small_header_text">
            <span class="profile_small_header_name">{{ userName }}</span>
            <span class="profile_small_header_arrow">»</span>
            <span class="profile_small_header_location">编辑个人资料</span>
          </div>
          <div class="profile_small_header_additional">
          </div>
        </div>
      </div>
      <div class="grouppage_content_area groupadmin_content">
        <div class="maincontent">
          <div class="hasContentBoxes">
            <div class="react_root">
              <div class="profile_ctn">
                <div class="leftcol">
                  <div v-if="current != 1" class="button_item" @click="current = 1">一般</div>
                  <div v-if="current == 1" class="button_item_focus">一般</div>
                  <div v-if="current != 2" class="button_item" @click="current = 2">头像</div>
                  <div v-if="current == 2" class="button_item_focus">头像</div>
                </div>
                <div class="rightcol">
                  <div v-if="current == 1" class="normal_ctn">
                    <div class="DialogHeader">一般</div>
                    <div class="DialogBodyText">
                      <div style="display: block;">
                        设置您的个人资料名称和详情。提供您的真实姓名等额外信息可以帮助好友在 Steam 社区上找到您。
                        <br><br>
                        您的个人资料名称和头像会在整个 Steam 平台上代表您，必需适合向所有受众展示。请查看
                        <a class="Focusable" href="https://support.steampowered.com/kb_article.php?ref=6864-RYOA-6811">常见问题</a>，
                        获取更多详情。
                      </div>
                    </div>
                    <div class="modify_ctn">
                      <div class="subtitle">一般</div>
                      <div class="modify">
                        <div class="DialogInputLabelGroup _DialogLayout DialogRequirementLabel">
                          <label>
                            <div class="DialogLabel">个人资料名称
                              <span class="DialogInputRequirementLabel"></span>
                            </div>
                            <div class="DialogInput_Wrapper _DialogLayout Panel Focusable">
                              <input v-model="name" class="DialogInput DialogInputPlaceholder DialogTextInputBase Focusable">
                            </div>
                          </label>
                          <label>
                            <div class="DialogLabel">密码
                              <span class="DialogInputRequirementLabel"></span>
                            </div>
                            <div class="DialogInput_Wrapper _DialogLayout Panel Focusable">
                              <input v-model="password" type="password" placeholder="输入新密码（留空则不修改）" class="DialogInput DialogInputPlaceholder DialogTextInputBase Focusable">
                            </div>
                          </label>
                          <label>
                            <div class="DialogLabel">地址
                              <span class="DialogInputRequirementLabel"></span>
                            </div>
                            <div class="DialogInput_Wrapper _DialogLayout Panel Focusable">
                              <input v-model="address" class="DialogInput DialogInputPlaceholder DialogTextInputBase Focusable">
                            </div>
                          </label>
                        </div>
                      </div>
                    </div>
                    <div class="save_button_area">
                      <div v-loading="loading" class="DialogButton Focusable" @click="handleUpdateUser">保存</div>
                    </div>
                  </div>
                  <div v-if="current == 2" class="avatar_ctn">
                    <div class="DialogBody">
                      <div class="DialogHeader">头像</div>
                      <div class="DialogBodyText">选择您的头像图片。</div>
                      <div class="DialogBodyAvatar">
                        <div class="avatar_display">
                          <div class="avatar_large">
                            <img class="avatar_large" :src="userAvatar || 'https://steam-1314488277.cos.ap-guangzhou.myqcloud.com/assets%2Fdefault_avatar.jpg'">
                            <div class="avatar_subtitle">184px</div>
                          </div>
                          <div class="avatar_medium">
                            <img class="avatar_medium" :src="userAvatar || 'https://steam-1314488277.cos.ap-guangzhou.myqcloud.com/assets%2Fdefault_avatar.jpg'">
                            <div class="avatar_subtitle">64px</div>
                          </div>
                          <div class="avatar_small">
                            <img class="avatar_small" :src="userAvatar || 'https://steam-1314488277.cos.ap-guangzhou.myqcloud.com/assets%2Fdefault_avatar.jpg'">
                            <div class="avatar_subtitle">32px</div>
                          </div>
                        </div>
                        <div class="avatar_modify">
                          <el-upload
                              class="custom-upload"
                              :http-request="uploadHttpRequest"
                              :on-change="handleChangeAvatar"
                              :on-remove="handleRemoveAvatar"
                              :file-list="AvatarUrlList"
                              :limit="1"
                              :auto-upload="false"
                              :show-file-list="false"
                          >
                            <div class="upload_wrapper">
                              <button
                                  type="button"
                                  class="DialogButtonAvatar Focusable"
                              >
                                上传您的头像
                              </button>
                              <div class="upload_tip">从您的设备上传一个文件。图片应为方形，至少为 184px x 184px。</div>
                            </div>
                          </el-upload>
                        </div>
                      </div>
                      <div class="save_button_area">
                        <div v-loading="loading" class="DialogButton Focusable" @click="handleUpdateUser">保存</div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.app {
  position: relative;
  display: flex;
  flex-direction: column;
  min-height: max(calc(100vh - 104px), 765px);
  font-family: "Motiva Sans", sans-serif;
  overflow: hidden;
}

.app_background {
  position: absolute;
  left: 0;
  right: 0;
  top: 0;
  z-index: -2;
  height: 100%;
  background: linear-gradient(to bottom, #242a2f, #1d1e20);
}

div {
  display: block;
  unicode-bidi: isolate;
}

.pagecontent {
  padding-bottom: 128px;
}

.profile_small_header_bg {
  max-width: 1010px;
  height: 215px;
  margin: 0 auto -98px auto;
  background-position: center;
}

.profile_small_header_texture {
  background: rgba(58, 62, 71, 0.5);
  max-width: 978px;
  height: 102px;
  margin: 0 auto;
  position: relative;
}

.profile_small_header_avatar {
  width: 64px;
  height: 64px;
  position: absolute;
  left: 30px;
  top: 18px;
  color: white;
}

.profile_small_header_text {
  position: absolute;
  left: 120px;
  right: 30px;
  bottom: 31px;
  max-height: 65px;
  color: white;
  font-size: 14px;
  overflow: hidden;
  text-overflow: ellipsis;
}

.profile_small_header_name {
  font-size: 26px;
  margin-right: 6px;
}

.profile_small_header_arrow {
  font-size: 16px;
  color: #828282;
  margin-right: 4px;
}

.profile_small_header_additional {
  position: absolute;
  right: 30px;
  top: 0;
  bottom: 0;
}

.grouppage_content_area {
  padding-top: 18px;
}

.maincontent {
  width: 948px;
  margin: 0 auto;
}

.hasContentBoxes {
  display: block;
  float: none;
  width: auto;
  max-width: none;
  position: relative;
}

.profile_ctn {
  display: flex;
  flex-direction: row;
  margin-bottom: 40px;
}

.leftcol {
  min-width: 0;
  width: 200px;
  max-width: 20vw;
  margin: 0 20px 0 0;
}

.button_item {
  display: block;
  line-height: 30px;
  background-image: linear-gradient(to left, transparent, transparent 50%, #3d4450 50%, #3d4450);
  background-position: 100% 0;
  background-size: 200% 100%;
  border-radius: 3px;
  transition-property: background-position,color,background-color;
  transition-duration: .15s;
  transition-timing-function: ease-in;
  color: #999;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  padding: 2px 20px 2px 10px;
  margin-bottom: 5px;

  &:hover {
    background-color: #3d4450;
    background-position: 0 0;
    color: #fff;
  }
}

.button_item_focus {
  display: block;
  line-height: 30px;
  background-image: linear-gradient(to left, transparent, transparent 50%, #3d4450 50%, #3d4450);
  background-size: 200% 100%;
  border-radius: 3px;
  transition-property: background-position,color,background-color;
  transition-duration: .15s;
  transition-timing-function: ease-in;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  padding: 2px 20px 2px 10px;
  margin-bottom: 5px;
  background-color: #3d4450;
  background-position: 0 0;
  color: #fff;
}

.rightcol {
  flex: 1;
  min-width: 0;
}

.DialogHeader {
  display: flex;
  justify-content: flex-start;
  font-size: 24px;
  letter-spacing: 1px;
  margin: 0px 5px 0px 5px;
  font-weight: 700;
  line-height: 28px;
  color: #fff;
  flex-shrink: 0;
  user-select: none;
}

.DialogBodyText {
  margin-left: 5px;
  margin-bottom: 15px;
  padding-bottom: 5px;
  line-height: normal;
  color: #acb2b8;
  font-size: 16px;
  font-weight: 300;
  user-select: none;
}

.Focusable {
  color: #ebebeb;
  text-decoration: none;
  cursor: pointer;
}

.modify_ctn {
  flex: 1;
  display: flex;
  flex-direction: column;
  padding: 0 0 15px 0;
}

.subtitle {
  font-size: 16px;
  line-height: 28px;
  letter-spacing: 0em;
  text-transform: uppercase;
  color: #e4e4e4;
  padding: 0px 0px 6px 10px;
  border-bottom: 2px solid rgba(228,228,228,.1);
}

.modify {
  flex: 1;
  align-items: center;
  padding: 24px 20px;
}

.DialogInputLabelGroup {
  margin-bottom: 22px;
  display: block;
}

.DialogRequirementLabel {
  color: #c15755;
  font-weight: 500;
  font-size: 14px;
}

.DialogLabel {
  font-weight: 300;
  color: #acb2b8;
  text-transform: uppercase;
  line-height: 19px;
  font-size: 13px;
  margin-bottom: 4px;
  user-select: none;
  letter-spacing: initial;
}

.DialogInputRequirementLabel {
  color: #c15755;
  margin-left: 15px;
}

.DialogInput_Wrapper {
  display: flex;
  flex-direction: row;
  position: relative;
  align-items: center;
}

.DialogTextInputBase {
  box-shadow: inset 1px 1px 0px #000a;
  border: none;
  background-color: rgba(0,0,0,.25);
  flex: 1;
  color: #909090;
  border-radius: 3px;
  font-size: 14px;
  font-family: "Motiva Sans",Arial,Helvetica,sans-serif;
  font-weight: 300;
  margin: 0;
}

.DialogTextInputBase:focus {
  box-shadow: inset 1px 1px 0px #000a;
  outline: none;
  border: none;
  background-color: rgba(0,0,0,.25);
}

.DialogInput {
  display: block;
  line-height: 22px;
  padding: 10px;
  margin-bottom: 10px;
}

.save_button_area {
  display: flex;
  flex-direction: row-reverse;
}

.DialogButton {
  z-index: 0;
  background: linear-gradient(to right, #47bfff 0%, #1a44c2 60%);
  background-position: 25%;
  background-size: 330% 100%;
  width: 200px;
  margin-left: 12px;
  box-sizing: content-box;
  border: 0;
  font: inherit;
  overflow: visible;
  padding: 0;
  margin: 2px 0;
  -webkit-user-select: none;
  display: block;
  line-height: 32px;
  color: #dfe3e6;
  font-size: 14px;
  text-align: center;
  border-radius: 2px;
  cursor: pointer;
  transition-property: opacity,background,color,box-shadow;
  transition-duration: .2s;
  transition-timing-function: ease-out;
  position: relative;
}

.DialogButton::before {
  z-index: -1;
  pointer-events: none;
  user-select: none;
  content: " ";
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  border-radius: 2px;
  box-shadow: 0 8px 16px 0 rgba(0,0,0,.3);
  opacity: 0;
  transition: opacity 200ms ease-in-out;
}

.DialogButton:hover::before {
  opacity: 1;
  border-radius: 2px;
  background: linear-gradient(to right, #1bbfff, #3082e0);
}

.DialogBodyAvatar {
  display: flex;
  flex-direction: row;
}

.avatar_display {
  flex: 1;
  display: flex;
  flex-direction: row;
  align-items: flex-end;
  padding: 8px 0px 22px 18px;
}

.avatar_large {
  width: 184px;
  margin-right: 25px;
}

.avatar_medium {
  width: 64px;
  margin-right: 25px;
}

.avatar_small {
  width: 32px;
  margin-right: 25px;
}


.avatar_subtitle {
  padding-top: 15px;
  font-size: 9px;
  line-height: 11px;
  color: #969696;
}

.avatar_modify {
  width: 217px;
  font-size: 14px;
  color: gray;
  margin-bottom: 24px;
}

.DialogButtonAvatar {
  z-index: 0;
  box-sizing: content-box;
  border: 0;
  font: inherit;
  overflow: visible;
  padding: 0;
  margin: 2px 0;
  -webkit-user-select: none;
  display: block;
  line-height: 32px;
  color: #dfe3e6;
  font-size: 14px;
  text-align: center;
  border-radius: 2px;
  cursor: pointer;
  width: 100%;
  transition-property: opacity,background,color,box-shadow;
  transition-duration: .2s;
  transition-timing-function: ease-out;
  background: #3d4450;
  background-position: 99% 1%;
  background-size: 300% 300%;
  position: relative;
}

.DialogButtonAvatar::before {
  z-index: -1;
  pointer-events: none;
  user-select: none;
  content: " ";
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  box-shadow: 0 8px 16px 0 rgba(0,0,0,.3);
  opacity: 0;
  transition: opacity 200ms ease-in-out;
}

.DialogButtonAvatar:hover::before {
  opacity: 1;
  background: #464d58;
}

.custom-upload {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.upload_wrapper {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}
</style>