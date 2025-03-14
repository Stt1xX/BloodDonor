<template>
  <div class="min-h-screen w-screen bg-gray-50">
    <router-view class="h-full w-full" />
  </div>
</template>

<script setup>

import {onMounted} from "vue";
import {get_token} from "@/js/csrf-token.js";

onMounted( async () => {
  await get_token()
})

</script>

<style>
html, body, #app {
  margin: 0;
  padding: 0;
  height: 100%;
  width: 100%;
}

.app {
  height: 100%;
  width: 100%;
  font-family: Arial, sans-serif;
}
</style>