<template>
  <div class="min-h-screen flex flex-col">
    <Header :header-group="HeaderGroups.ADMIN"/>
    <div class="blood-requests-view">
      <div class="d-flex justify-content-between align-items-center mb-4">
        <h1>Заявки на кровь</h1>
        <button
          class="btn btn-primary"
          @click="$router.push('/blood-requests/new')"
        >
          Создать заявку
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import {HeaderGroups} from "@/js/utils.js";
import Header from "@/components/shared/Header.vue";
</script>

<style scoped>
.blood-requests-view {
  padding: 2rem;
}
</style> 