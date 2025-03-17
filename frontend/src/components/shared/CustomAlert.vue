<template>
  <div v-if="visible" class="bg-red-600 text-white p-4 rounded-lg flex items-center justify-between fixed bottom-4 right-4 h-16 w-96 shadow-lg z-50">
    <div class="flex items-center">
      <button @click="closeAlert" class="mr-4 p-2 rounded-full hover:bg-red-700 transition-colors">
        <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
        </svg>
      </button>
      <span class="text-sm">{{ message }}</span>
    </div>
    <div class="absolute bottom-0 left-0 h-1 bg-white animate-progress"></div>
  </div>
</template>

<script setup>
import { closeAlert, message, visible } from "@/js/custom-alert.js";
</script>

<style scoped>

@keyframes progress {
  from {
    width: 100%;
  }
  to {
    width: 0;
  }
}

.animate-progress {
  animation: progress 5s linear forwards;
}

.bg-red-600 {
  background-color: #dc2626;
}

.bg-red-700 {
  background-color: #b91c1c;
}

.hover\:bg-red-700:hover {
  background-color: #b91c1c;
}
</style>