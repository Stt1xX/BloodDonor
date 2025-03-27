<template>
  <div class="bg-red-600 text-white py-4 px-10 shadow-md">
    <div class="container mx-auto flex justify-between items-center">
      <div class="flex items-center">
        <img src="@/assets/logo.png" alt="Logo" class="w-10 h-10 mr-3" />
        <h1 class="text-2xl font-bold">Blood Link</h1>
      </div>

      <div v-if="user.name && user.surname" class="flex flex-col items-end text-right ml-6">
        <span class="text-lg font-semibold">Добро пожаловать, {{ user.name }} {{ user.surname }}!</span>
        <span class="text-sm text-gray-200">{{ convertUserRole(user.role) }}</span>
      </div>

      <nav class="flex space-x-4">
        <button
            v-for="button in headerButtons[headerGroup]"
            :key="button.text"
            @click="button.action"
            :class="['px-4 py-2 rounded transition-colors duration-300 border border-transparent hover:bg-red-600 hover:text-white hover:border-white',
             isActive(button.route) ? 'bg-[#e3342f] text-white border-white' : 'bg-white text-red-600']"
        >
          {{ button.text }}
        </button>
      </nav>
    </div>
  </div>
</template>

<script setup>
import { defineProps} from 'vue'
import { user } from "@/js/user-info.js"
import axios from "axios";
import router from "@/routes/routes.js";
import { showAlert } from "@/js/custom-alert.js";
import {convertUserRole, HeaderGroups} from "@/js/utils.js";
import { useRoute } from 'vue-router';

const route = useRoute();

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
      showAlert(response.data)
      await router.push('/login')
    }
  } catch (error) {
    showAlert(error.response.data);
  }
}

// Функция для проверки, активна ли кнопка
const isActive = (routePath) => {
  return route.path === routePath;
}

const headerButtons = {
      [HeaderGroups.ADMIN] : [
          { text: 'Запросы', action: () => router.push('/admin/requests'), route: '/admin/requests' },
          { text: 'Организации', action: () => router.push('/admin/organization_settings'), route: '/admin/organization_settings' },
          { text: 'Работники', action: () => router.push('/admin/all_users'), route: '/admin/all_users' },
          { text: 'Запасы', action: () => router.push('/bank_employee/reserves'), route: '/bank_employee/reserves' },
          { text: 'Выйти', action: () => logout() },
        ],
      [HeaderGroups.BANK_EMPLOYEE] : [
          { text: 'Организации', action: () => router.push('/admin/organization_settings'), route: '/admin/organization_settings' },
          { text: 'Запасы', action: () => router.push('/bank_employee/reserves'), route: '/bank_employee/reserves' },
          { text: 'Выйти', action: () => logout() },
      ]
}


</script>

<style scoped>
header {
  background-color: #e3342f;
  color: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
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
