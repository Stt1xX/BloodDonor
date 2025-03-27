<template>
  <div class="mt-20 mb-52 w-full max-w-[450px] mx-8 p-8 bg-white rounded-2xl shadow-xl border border-red-100">
    <div class="flex flex-col">
      <!-- Logo and Title -->
      <div class="text-center mb-8">
        <div class="mb-4">
          <img src="@/assets/logo.png" alt="Logo" class="w-16 h-16 mx-auto rounded-full cursor-pointer" @click="refreshPage" />
        </div>
        <h2 class="text-3xl font-bold text-red-800 mb-2">Вход</h2>
        <p class="text-gray-600">Банк крови - спасаем жизни вместе</p>
        <div class="mt-2 w-16 h-1 bg-gradient-to-r from-red-500 to-red-600 mx-auto rounded-full"></div>
      </div>

      <!-- Login Form -->
      <form @submit.prevent="handleLogin" class="grid grid-cols-1 gap-6">
        <!-- Email Input -->
        <div class="group col-span-1">
          <label for="email" class="block text-sm font-medium text-gray-700">
            Email
            <span v-if="errors.email" class="text-red-500 text-xs ml-2">{{ errors.email }}</span>
          </label>
          <div class="relative mt-1">
            <span class="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 group-hover:text-red-500 transition-colors">
              <i class="fas fa-envelope"></i>
            </span>
            <input
                type="text"
                id="email"
                v-model="email"
                class="block w-full rounded-lg border-gray-300 shadow-sm focus:ring-red-500 focus:border-red-500 hover:border-red-300 transition-all duration-200"
                placeholder="Введите ваш email"
            />
          </div>
        </div>

        <!-- Password Input -->
        <div class="group col-span-1">
          <label for="password" class="block text-sm font-medium text-gray-700">
            Пароль
            <span v-if="errors.password" class="text-red-500 text-xs ml-2">{{ errors.password }}</span>
          </label>
          <div class="relative mt-1">
            <span class="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 group-hover:text-red-500 transition-colors">
              <i class="fas fa-lock"></i>
            </span>
            <input
                type="password"
                id="password"
                v-model="password"
                class="block w-full rounded-lg border-gray-300 shadow-sm focus:ring-red-500 focus:border-red-500 hover:border-red-300 transition-all duration-200"
                placeholder="Введите ваш пароль"
            />
          </div>
        </div>

        <!-- Submit Button -->
        <div class="col-span-1">
          <button
              type="submit"
              class="w-full py-3 px-4 rounded-lg font-medium text-white bg-gradient-to-r from-red-600 to-red-700 hover:from-red-700 hover:to-red-800 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500 transition-all duration-200 shadow-md"
          >
            Войти
          </button>
        </div>

        <!-- Registration Link -->
        <div class="col-span-1 text-center">
          <router-link
              to="/registration"
              class="text-sm font-medium text-red-600 hover:text-red-500 transition-colors duration-200">
            Зарегистрироваться
          </router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'
import {get_token} from "@/js/csrf-token.js"
import router from "@/routes/routes.js"
import {showAlert} from "@/js/custom-alert.js";

const email = ref('')
const password = ref('')
const errors = ref({})

const validateForm = () => {
  errors.value = {}
  const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  if (!email.value) {
    errors.value.email = 'Email обязателен'
  } else if (!emailPattern.test(email.value)) {
    errors.value.email = 'Некорректный формат email'
  }
  if (!password.value) {
    errors.value.password = 'Пароль обязателен'
  } else if (password.value.length < 5) {
    errors.value.password = 'Пароль должен быть не менее 5 символов'
  }
  return Object.keys(errors.value).length === 0
}

const handleLogin = async () => {
  if (validateForm()) {
    try {
      const response = await axios.post('/api/auth/login', {
        "username": email.value,
        "password": password.value,
      }, {
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded'
        }
      });
      if (response.status === 200) {
        await get_token();
        redirect_user(response.data.role)
        showAlert(response.data.responseText)
      } else {
        showAlert(response)
      }
    } catch (error) {
        showAlert(error.response.data)
    }
  }
}


const redirect_user = (role) => {
  if (role === 'ADMIN') {
    router.push('/admin/organization_settings')
  } else if (role === 'MEDICAL_EMPLOYEE') {
    router.push('/medical_employee/main')
  } else if (role === 'BANK_EMPLOYEE') {
    router.push('/bank_employee/reserves')
  } else {
    router.push('/login')
  }
}

const refreshPage = () => {
  window.location.reload()
}
</script>