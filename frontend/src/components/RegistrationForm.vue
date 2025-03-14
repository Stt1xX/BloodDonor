<template>
  <div class="mt-20 mb-52 w-full max-w-[970px] mx-8 p-8 bg-white rounded-2xl shadow-xl border border-red-100">
    <div class="flex flex-col">
      <!-- Logo and Title -->
      <div class="text-center mb-8">
        <div class="mb-4">
          <img src="@/assets/logo.png" alt="Logo" class="w-16 h-16 mx-auto rounded-full cursor-pointer" @click="refreshPage" />
        </div>
        <h2 class="text-3xl font-bold text-red-800 mb-2">Регистрация</h2>
        <p class="text-gray-600">Банк крови - спасаем жизни вместе</p>
        <div class="mt-2 w-16 h-1 bg-gradient-to-r from-red-500 to-red-600 mx-auto rounded-full"></div>
      </div>

      <!-- Registration Form -->
      <form @submit.prevent="handleRegistration" class="grid grid-cols-1 md:grid-cols-3 gap-6">
        <!-- Name Input -->
        <div class="group col-span-1">
          <label for="name" class="block text-sm font-medium text-gray-700">
            Имя
            <span v-if="errors.name" class="text-red-500 text-xs ml-2">{{ errors.name }}</span>
          </label>
          <div class="relative mt-1">
            <span class="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 group-hover:text-red-500 transition-colors">
              <i class="fas fa-user"></i>
            </span>
            <input
                type="text"
                id="name"
                v-model="name"
                class="block w-full rounded-lg border-gray-300 shadow-sm focus:ring-red-500 focus:border-red-500 hover:border-red-300 transition-all duration-200"
                placeholder="Введите ваше имя"
            />
          </div>
        </div>

        <!-- Last Name Input -->
        <div class="group col-span-1">
          <label for="lastName" class="block text-sm font-medium text-gray-700">
            Фамилия
            <span v-if="errors.lastName" class="text-red-500 text-xs ml-2">{{ errors.lastName }}</span>
          </label>
          <div class="relative mt-1">
            <span class="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 group-hover:text-red-500 transition-colors">
              <i class="fas fa-user"></i>
            </span>
            <input
                type="text"
                id="lastName"
                v-model="lastName"
                class="block w-full rounded-lg border-gray-300 shadow-sm focus:ring-red-500 focus:border-red-500 hover:border-red-300 transition-all duration-200"
                placeholder="Введите вашу фамилию"
            />
          </div>
        </div>

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

        <!-- Role Selector -->
        <div class="group col-span-1">
          <label for="role" class="block text-sm font-medium text-gray-700">
            Роль
            <span v-if="errors.role" class="text-red-500 text-xs ml-2">{{ errors.role }}</span>
          </label>
          <CustomSelect
              id="role"
              :options="roleOptions"
              v-model="role"
              placeholder="Выберите роль"
          />
        </div>

        <!-- Position Input -->
        <div class="group col-span-1">
          <label for="position" class="block text-sm font-medium text-gray-700">
            Должность
            <span v-if="errors.position" class="text-red-500 text-xs ml-2">{{ errors.position }}</span>
          </label>
          <div class="relative mt-1">
            <input
                type="text"
                id="position"
                v-model="position"
                class="block w-full rounded-lg border-gray-300 shadow-sm focus:ring-red-500 focus:border-red-500 hover:border-red-300 transition-all duration-200"
                placeholder="Введите вашу должность"
            />
          </div>
        </div>

        <!-- Institution Input -->
        <div class="group col-span-3">
          <label for="institution" class="block text-sm font-medium text-gray-700">
            Заведение
            <span v-if="errors.institution" class="text-red-500 text-xs ml-2">{{ errors.institution }}</span>
          </label>
          <div class="relative mt-1">
            <input
                type="text"
                id="institution"
                v-model="institution"
                class="block w-full rounded-lg border-gray-300 shadow-sm focus:ring-red-500 focus:border-red-500 hover:border-red-300 transition-all duration-200"
                placeholder="Введите название заведения"
            />
          </div>
        </div>

        <!-- Submit Button -->
        <div class="col-span-3">
          <button
              type="submit"
              class="w-full py-3 px-4 rounded-lg font-medium text-white bg-gradient-to-r from-red-600 to-red-700 hover:from-red-700 hover:to-red-800 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500 transition-all duration-200 shadow-md"
          >
            Зарегистрироваться
          </button>
        </div>

        <!-- Login Link -->
        <div class="col-span-3 text-center">
          <router-link
              to="/login"
              class="text-sm font-medium text-red-600 hover:text-red-500 transition-colors duration-200"
          >
            Войти
          </router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import CustomSelect from '@/components/shared/CustomSelect.vue'

const router = useRouter()
const name = ref('')
const lastName = ref('')
const email = ref('')
const password = ref('')
const role = ref('')
const position = ref('')
const institution = ref('')
const errors = ref({})

const roleOptions = [
  { value: 'admin', text: 'Админ' },
  { value: 'bank_employee', text: 'Сотрудник банка' },
  { value: 'medical_worker', text: 'Медицинский работник' }
]

const validateForm = () => {
  errors.value = {}
  const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  if (!name.value) errors.value.name = 'Имя обязательно'
  if (!lastName.value) errors.value.lastName = 'Фамилия обязательна'
  if (!email.value) {
    errors.value.email = 'Email обязателен'
  } else if (!emailPattern.test(email.value)) {
    errors.value.email = 'Некорректный формат email'
  }
  if (!password.value) {
    errors.value.password = 'Пароль обязателен'
  } else if (password.value.length < 5) {
    errors.value.password = 'Должен быть более 4 символов'
  }
  if (!role.value) errors.value.role = 'Роль обязательна'
  if (!position.value) errors.value.position = 'Должность обязательна'
  if (!institution.value) errors.value.institution = 'Заведение обязательно'
  return Object.keys(errors.value).length === 0
}

const handleRegistration = async () => {
  if (validateForm()) {
    try {
      console.log('Registration attempt:', { name: name.value, lastName: lastName.value, email: email.value, password: password.value, role: role.value, position: position.value, institution: institution.value })
      router.push('/')
    } catch (error) {
      console.error('Registration error:', error)
    }
  }
}

const refreshPage = () => {
  window.location.reload()
}
</script>