<script setup>

import {useTitle} from "@vueuse/core";

useTitle("Zwanan的在线工具")

</script>

<template>
  <div class="hero py-28">
    <div class="hero-content text-center">
      <div class="max-w-md">
        <label class="swap swap-flip text-9xl">
          <!-- this hidden checkbox controls the state -->
          <input type="checkbox" />
          <div class="swap-on">😈</div>
          <div class="swap-off">😇</div>
        </label>
        <h1 class="mt-3 text-5xl font-bold">Zwanan的在线工具</h1>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>