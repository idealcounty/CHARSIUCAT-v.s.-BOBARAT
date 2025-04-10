<script setup lang="ts">
import {ref, computed} from 'vue'
import {router} from '../../router'

const productId = ref('')
const productName = ref('')
const productAmount = ref(0)
const productPrice = ref(0)
const productDescription = ref('')
const productLogoURL = ref('')
const productImageURL = ref([])

</script>

<template>
  <div class="app">
    <div class="administrator">
      <div class="administrator_title">
        管理员界面
      </div>

    </div>
  </div>
</template>

<style scoped>

</style>