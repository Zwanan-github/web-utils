<script setup>

import {onMounted, ref} from "vue";

const links = ref([
  {
    name: "剪贴板",
    href: '/paste'
  },
  {
    name: '敲代码',
    href: '/code'
  }
])

const theme = ref(localStorage.getItem("theme") !== "night" ? "light" : "night");

onMounted(() => {
  localStorage.setItem("theme", theme.value);
  window.document.documentElement.setAttribute("data-theme", theme.value);
})


function changeTheme(change) {
  localStorage.setItem("theme", change);
  window.document.documentElement.setAttribute("data-theme", change);
  theme.value = change;
}

</script>

<template>
  <div class="navbar bg-base-100">
    <div class="navbar-start">
      <div class="dropdown">
        <div tabindex="0" role="button" class="btn btn-ghost lg:hidden">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h8m-8 6h16"/>
          </svg>
        </div>
        <ul tabindex="0" class="menu menu-sm dropdown-content mt-3 z-[1] p-2 shadow bg-base-100 rounded-box w-52">
          <li v-for="(link) in links">
            <RouterLink :to="link.href">{{ link.name }}</RouterLink>
          </li>
        </ul>
      </div>
      <RouterLink to="/" class="btn btn-ghost text-xl">Zwanan's Utils</RouterLink>
    </div>
    <div class="navbar-center hidden lg:flex">
      <ul class="menu menu-horizontal px-1">
        <li v-for="(link) in links">
          <RouterLink :to="link.href">{{ link.name }}</RouterLink>
        </li>
      </ul>
    </div>
    <div class="navbar-end">
      <div class="dropdown">
        <div tabindex="0" role="button" class="btn m-1">
          Theme
          <svg width="12px" height="12px" class="h-2 w-2 fill-current opacity-60 inline-block"
               xmlns="http://www.w3.org/2000/svg" viewBox="0 0 2048 2048">
            <path d="M1799 349l242 241-1017 1017L7 590l242-241 775 775 775-775z"></path>
          </svg>
        </div>
        <ul tabindex="0" class="dropdown-content z-[1] p-2 shadow-2xl bg-base-300 rounded-box w-20">
          <li><input type="radio" @click="changeTheme('light')" name="theme-dropdown"
                     class="theme-controller btn btn-sm btn-block btn-ghost justify-start" aria-label="Light"
                     value="light"/></li>
          <li><input type="radio" @click="changeTheme('night')" name="theme-dropdown"
                     class="theme-controller btn btn-sm btn-block btn-ghost justify-start" aria-label="Night"
                     value="night"/></li>
        </ul>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>