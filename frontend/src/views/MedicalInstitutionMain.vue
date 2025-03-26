<template>
  <div class="min-h-screen flex flex-col">
    <Header :header-group="HeaderGroup.ADMIN"/>
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
      <BloodRequestList />
    </div>
  </div>
</template>

<script setup>
import BloodRequestList from '@/components/BloodRequestList.vue'
import {HeaderGroup} from "@/js/uitls.js";
import Header from "@/components/shared/Header.vue";
</script>

<style scoped>
.blood-requests-view {
  padding: 2rem;
}
</style> 