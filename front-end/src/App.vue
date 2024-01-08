<script setup>
import NavBar from "./components/NavBar.vue";
import Footer from "@/components/Footer.vue";
</script>

<template>
  <NavBar/>
  <div class="min-h-[75vh] h-full bg-base-200">
    <RouterView></RouterView>
  </div>
  <Footer/>
</template>

<style scoped>
</style>
