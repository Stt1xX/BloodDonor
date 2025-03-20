<template>
  <header class="bg-red-600 text-white py-4 shadow-md">
    <div class="container mx-auto flex justify-between items-center">
      <div class="flex items-center">
        <img src="@/assets/logo.png" alt="Logo" class="w-10 h-10 mr-3" />
        <h1 class="text-2xl font-bold">Blood Link</h1>
      </div>
      <nav class="flex space-x-4">
        <button
            v-for="button in headerButtons[headerGroup]"
            :key="button.text"
            @click="button.action"
            class="bg-white text-red-600 px-4 py-2 rounded transition-colors duration-300 border border-transparent hover:bg-red-600 hover:text-white hover:border-white"
        >
          {{ button.text }}
        </button>
      </nav>
    </div>
  </header>
</template>

<script setup>
import {defineProps, ref} from 'vue'
import axios from "axios";
import router from "@/routes/routes.js";
import {showAlert} from "@/js/custom-alert.js";

defineProps({
  headerGroup: {
    type: Number,
    required: true
  }
})

const logout = async () => {
  try {
    const response = await axios.post('/api/logout')
    if (response.status === 200) {
      await router.push('/login')
    }
  } catch (error) {
    showAlert(error.response.data)
  }
}

const headerButtons = [
  [{ text: 'Работники', action: () => router.push('/admin/requests') },
  { text: 'Организации', action: () => router.push('/admin/organization_settings') },
  { text: 'Выйти', action: () => logout() }],
]

</script>

<style scoped>
header {
  background-color: #e3342f;
  color: white;
  padding: 1rem 0;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 1rem;
}

nav button {
  transition: color 0.3s ease, background-color 0.3s ease, border-color 0.3s ease;
}

nav button:hover {
  color: white;
  background-color: #e3342f;
  border-color: white;
}
</style>