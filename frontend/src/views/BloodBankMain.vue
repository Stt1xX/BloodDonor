<script setup>
import BloodBankList from '@/components/BloodBankList.vue'
</script>

<template>
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
</template>

<style scoped>
.blood-banks-view {
  padding: 2rem;
}
</style> 