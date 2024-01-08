<script setup>

import {ref} from "vue";
import {useTitle} from "@vueuse/core";

const name = ref('')
useTitle("网络剪贴板")

</script>

<template>
  <div class="hero">
    <div class="hero-content text-center">
      <div class="max-w-md">
        <div class="join">
          <input class="input input-bordered join-item" type="text" v-model="name" placeholder="剪贴板名"/>
          <RouterLink class="btn btn-ghost bg-info join-item" :to="`/paste/${name}`">查看剪贴板</RouterLink>
        </div>
        <h1 class="pt-10 text-3xl font-bold">开始使用</h1>
        <div class="text-left max-w-xl">
          <p class="pt-6">
            网络剪贴板URL构成如下：
          </p>
          <p>
            https://utils.zwanan.top/paste/剪贴板名字
          </p>
          <p>
            如您的网络剪贴板名字为 demo，则网络剪贴板链接URL为 utils.zwanan.top/paste/demo, 直接在浏览器输入链接即可快速访问和编辑。
          </p>
        </div>
      </div>
    </div>

  </div>
</template>

<style scoped>

</style>