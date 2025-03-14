<script setup>
import BloodRequestList from '@/components/BloodRequestList.vue'
</script>

<template>
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
</template>

<style scoped>
.blood-requests-view {
  padding: 2rem;
}
</style> 