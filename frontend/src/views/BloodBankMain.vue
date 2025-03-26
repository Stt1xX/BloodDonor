<template>
  <div class="min-h-screen flex flex-col">
    <Header :header-group="HeaderGroup.ADMIN"/>
    <div class="blood-banks-view">
      <div class="d-flex justify-content-between align-items-center mb-4">
        <h1>Банки крови</h1>
        <button
          class="btn btn-primary"
          @click="$router.push('/blood-banks/new')"
        >
          Добавить банк крови
        </button>
      </div>
      <BloodBankList />
    </div>
  </div>
</template>

<script setup>
import BloodBankList from '@/components/BloodBankList.vue'
import {HeaderGroup} from "@/js/uitls.js";
import Header from "@/components/shared/Header.vue";
</script>

<style scoped>
.blood-banks-view {
  padding: 2rem;
}
</style> 