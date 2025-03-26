<template>
  <div class="min-h-screen w-screen bg-gradient-to-br from-gray-200 via-gray-100 to-gray-50">
    <router-view class="h-full w-full" />
    <custom-alert/>
  </div>
</template>

<script setup>

import CustomAlert from "@/components/shared/CustomAlert.vue";
import axios from "axios";
import router from "@/routes/routes.js";

axios.interceptors.response.use(
    response => response,
    async error => {
      if (error.response && error.response.status === 401) {
        await router.push("/login");
      }
      return Promise.reject(error);
    }
);


</script>

<style>
html, body, #app {
  margin: 0;
  padding: 0;
  height: 100%;
  width: 100%;
}
</style>