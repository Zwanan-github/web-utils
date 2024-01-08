<script setup>
import {computed, ref} from "vue";
import router from "@/router";
import {useTitle} from "@vueuse/core";

const expireKV = ref({
  1: "1小时",
  6: "6小时",
  24: "1天",
  72: "3天",
  168: "7天",
  672: "30天",
  1344: "60天",
  2016: "90天",
  4032: "180天",
  8064: "365天",
})

const name = computed(() => router.currentRoute.value?.params['name'] ?? '');

useTitle(`剪贴板-${name.value}`)

const pastePlaceholder = ref(
`剪贴板内容
可用拉动右下角来扩大编辑框
编辑框内可滚动
======注意！！！注意！！！注意！！！======
您设置的时间是距离 创建时间 保存的时间
如果您修改后的时间早于当前时间，剪贴板将会自动过期
====================================`
)

const paste = ref({
  name: name.value,
  content: "",
  password: "",
  createTime: "00-00-00 00:00:00",
  updateTime: "00-00-00 00:00:00",
  expireTime: "00-00-00 00:00:00",
  lastBrowse: "00-00-00 00:00:00",
  expire: 72,
  browseTimes: 0
});
const oldPassword = ref('');
// 对错信息
const error = ref('');
const success = ref('');

// 当前的name是否通过
const auth = ref(false)

// 先从数据库查询该剪贴板信息
const getPrivate = async () => {
  const res = await fetch(`/api/paste/private/${name.value}`,{
    method: 'GET'
  })
  if (!res.ok) {
    // 不成功
    throw Error(res.statusText);
  }
  auth.value = await res.json();
  if (auth.value === true) {
      await getPaste();
  }
}

getPrivate()

// 先从数据库查询该剪贴板信息
const getPaste = async () => {
  const res = await fetch(`/api/paste/detail`, {
    method: 'POST',
    body: JSON.stringify(paste.value),
    headers: {
      'Content-Type': 'application/json'
    },
  })
  if (!res.ok) {
    // 不成功
    throw Error(res.statusText);
  }
  const data = await res.json();
  if (data.name === '') {
    return;
  }
  paste.value = data;
  auth.value = true;
  oldPassword.value = data.password;
}

const postPaste = async () => {
  const res = await fetch(`/api/paste?oldPassword=${oldPassword.value}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(paste.value)
  })
  if (!res.ok) {
    error.value = '保存失败';
    // 不成功
    throw Error(res.statusText);
  }
  const data = await res.json();
  if (data.name === '') {
    error.value = '保存失败';
    return;
  }
  success.value = '保存成功';
  paste.value = data;
}

const deletePaste = async () => {
  const res = await fetch(`/api/paste/${name.value}`, {
    method: 'DELETE',
  })
  if (!res.ok) {
    error.value = '删除失败';
    // 不成功
    throw Error(res.statusText);
  }
  const data = await res.json();
  if (data === false) {
    error.value = '删除失败';
    return;
  }
  success.value = '删除成功';
  // 重新加载该页面
  router.go(0);
}

</script>

<template>
  <div v-if="auth === false" class="hero">
    <div class="hero-content text-center">
      <div class="text-center">
        <h1 class="text-2xl font-bold">该剪贴板已加密</h1>
        <!--表单-->
        <input class="input input-bordered join-item" type="text" v-model="paste.password" placeholder="请输入密码"/>
        <button class="btn btn-ghost bg-info join-item" @click="getPaste">查看剪贴板</button>
      </div>
    </div>
  </div>
  <div v-if="auth === true" class="grid min-h-[75vh] place-items-center w-full">
    <div class="py-4 px-10 w-full h-full max-w-[1760px]">
      <div v-if="success !== ''" role="alert" class="alert alert-success">
        <svg xmlns="http://www.w3.org/2000/svg" class="stroke-current shrink-0 h-6 w-6" fill="none" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" /></svg>
        <span>{{ success }}!!</span>
      </div>
      <div v-if="error !== ''" role="alert" class="alert alert-error">
        <svg xmlns="http://www.w3.org/2000/svg" class="stroke-current shrink-0 h-6 w-6" fill="none" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z" /></svg>
        <span>{{ error }}!!</span>
      </div>
      <div class="flex py-2 w-full justify-center flex-col h-full md:flex-row">
        <div class="w-full md:w-3/5 h-full">
          <!--文本框-->
          <textarea class="textarea text-xl min-h-[80vh] w-full textarea-bordered" v-model="paste.content"
                    :placeholder="pastePlaceholder"></textarea>
        </div>
        <div class="divider md:divider-horizontal"></div>
        <div class="h-full w-full md:w-1/5">
          <div class="py-2">
            <span class="font-bold">状态:&nbsp;</span>
            <div v-if="paste.expire === 0 && paste.password === ''" class="badge badge-accent badge-outline">可用</div>
            <div v-if="paste.expire !== 0 && paste.password === ''" class="badge badge-primary badge-outline">公开</div>
            <div v-if="paste.expire !== 0 && paste.password !== ''" class="badge badge-secondary badge-outline">私有</div>
          </div>
          <div class="py-2">
            <span class="font-bold">保存时间:&nbsp;</span>
            <select class="select select-bordered select-sm w-25 max-w-sm" v-model="paste.expire">
              <option v-for="(k, v) in expireKV" :value="v">{{ k }}</option>
            </select>
          </div>
          <div class="py-2">
            <span class="font-bold">设置密码:&nbsp;</span>
            <input class="input input-bordered input-sm w-25 max-w-sm" v-model="paste.password" type="password"
                   placeholder="密码">
          </div>
          <div class="py-2">
            <span class="font-bold">创建时间:&nbsp;</span><br>
            <span class="">{{ paste.createTime }}</span>
          </div>
          <div class="py-2">
            <span class="font-bold">更新时间:&nbsp;</span><br>
            <span class="">{{ paste.updateTime }}</span>
          </div>
          <div class="py-2">
            <span class="font-bold">截止时间:&nbsp;({{ expireKV[paste.expire] }})</span><br>
            <span class="">{{ paste.expireTime }}</span>
          </div>
          <div class="py-2">
            <span class="font-bold">上次查看:&nbsp;</span><br>
            <span class="">{{ paste.lastBrowse }}</span>
          </div>
          <div class="py-2">
            <span class="font-bold">查看次数:&nbsp;</span>
            <span class="">{{ paste.browseTimes }} 次</span>
          </div>
          <div class="py-2">
            <button class="btn btn-primary w-2/5 mx-1" @click="postPaste">保存</button>
            <button class="btn btn-error w-2/5 mx-1" @click="deletePaste">删除</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>