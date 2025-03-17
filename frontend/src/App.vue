<template>
  <div class="min-h-screen w-screen bg-gradient-to-br from-gray-200 via-gray-100 to-gray-50">
    <router-view class="h-full w-full" />
    <custom-alert/>
  </div>
</template>

<script setup>

import {onMounted} from "vue";
import {get_token} from "@/js/csrf-token.js";
import CustomAlert from "@/components/shared/CustomAlert.vue";

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