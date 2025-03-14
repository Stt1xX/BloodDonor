<template>
  <div class="mt-20 mb-52 w-full max-w-[850px] mx-8 p-8 bg-white rounded-2xl shadow-xl border border-red-100">
    <div class="flex flex-col">
      <!-- Logo and Title -->
      <div class="text-center mb-8">
        <h2 class="text-3xl font-bold text-red-800 mb-2">Создание организации</h2>
        <div class="mt-2 w-16 h-1 bg-gradient-to-r from-red-500 to-red-600 mx-auto rounded-full"></div>
      </div>

      <!-- New Organization Form -->
      <form @submit.prevent="handleCreateOrganization" class="grid grid-cols-1 gap-6">
        <!-- Organization Type Selector -->
        <div class="group col-span-1">
          <label for="organizationType" class="block text-sm font-medium text-gray-700">
            Тип организации
            <span v-if="errors.organizationType" class="text-red-500 text-xs ml-2">{{ errors.organizationType }}</span>
          </label>
          <select id="organizationType" v-model="organizationType" class="block w-full rounded-lg border-gray-300 shadow-sm focus:ring-red-500 focus:border-red-500 hover:border-red-300 transition-all duration-200">
            <option value="">Выберите тип</option>
            <option value="BLOOD_BANK">Банк крови</option>
            <option value="MEDICAL_INSTITUTION">Медицинское учреждение</option>
          </select>
        </div>

        <!-- Name Input -->
        <div class="group col-span-1">
          <label for="name" class="block text-sm font-medium text-gray-700">
            Название
            <span v-if="errors.name" class="text-red-500 text-xs ml-2">{{ errors.name }}</span>
          </label>
          <div class="relative mt-1">
            <input
                type="text"
                id="name"
                v-model="name"
                class="block w-full rounded-lg border-gray-300 shadow-sm focus:ring-red-500 focus:border-red-500 hover:border-red-300 transition-all duration-200"
                placeholder="Введите название"
            />
          </div>
        </div>

        <!-- Address Input -->
        <div class="group col-span-1">
          <label for="address" class="block text-sm font-medium text-gray-700">
            Адрес
            <span v-if="errors.address" class="text-red-500 text-xs ml-2">{{ errors.address }}</span>
          </label>
          <div class="relative mt-1">
            <input
                type="text"
                id="address"
                v-model="address"
                class="block w-full rounded-lg border-gray-300 shadow-sm focus:ring-red-500 focus:border-red-500 hover:border-red-300 transition-all duration-200"
                placeholder="Введите адрес"
            />
          </div>
        </div>

        <!-- Phone and Work Time Inputs -->
        <div class="group col-span-1 grid grid-cols-2 gap-6">
          <div>
            <label for="phone" class="block text-sm font-medium text-gray-700">
              Телефон
              <span v-if="errors.phone" class="text-red-500 text-xs ml-2">{{ errors.phone }}</span>
            </label>
            <div class="relative mt-1">
              <input
                  type="text"
                  id="phone"
                  v-model="phone"
                  class="block w-full rounded-lg border-gray-300 shadow-sm focus:ring-red-500 focus:border-red-500 hover:border-red-300 transition-all duration-200"
                  placeholder="Введите телефон"
              />
            </div>
          </div>
          <div>
            <label for="work_time" class="block text-sm font-medium text-gray-700">
              Время работы
              <span v-if="errors.work_time" class="text-red-500 text-xs ml-2">{{ errors.work_time }}</span>
            </label>
            <div class="relative mt-1">
              <vue-date-picker v-model="work_time"
                             time-picker
                             :range="{ disableTimeRangeValidation: true }"
                             placeholder="Select Time"
              />
            </div>
          </div>
        </div>

        <div class="col-span-1">
          <button type="submit"  class="w-full py-3 px-4 rounded-lg font-medium text-white bg-gradient-to-r from-red-600 to-red-700 hover:from-red-700 hover:to-red-800 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500 transition-all duration-200 shadow-md">
            Создать
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'
import router from "@/routes/routes.js"

import VueDatePicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css'

const organizationType = ref('')
const name = ref('')
const address = ref('')
const phone = ref('')
const work_time = ref()
const errors = ref({})

const validateForm = () => {
  errors.value = {}
  if (!organizationType.value) errors.value.organizationType = 'Тип организации обязателен'
  if (!name.value) errors.value.name = 'Название обязательно'
  if (!address.value) errors.value.address = 'Адрес обязателен'
  if (!phone.value) errors.value.phone = 'Телефон обязателен'
  if (!work_time.value) errors.value.work_time = 'Время работы обязательно'
  if (is_bad_time(work_time.value)) {
    errors.value.work_time = 'Время окончания должно быть больше времени начала'
  }
  return Object.keys(errors.value).length === 0
}

const is_bad_time = (time) => {
  if (time !== undefined)
  if (time[0].hours * 60 + time[0].minutes >= time[1].hours * 60 + time[1].minutes) {
    return true
  }
  return false
}


const handleCreateOrganization = async () => {
  if (validateForm()) {
    try {
      const response = await axios.post('/admin/api/add_organization', {
        type: organizationType.value,
        name: name.value,
        address: address.value,
        phone: phone.value,
        hoursFrom: work_time.value[0].hours,
        hoursTo: work_time.value[1].hours,
        minutesFrom: work_time.value[0].minutes,
        minutesTo: work_time.value[1].minutes,
      });
      if (response.status === 200) {
        console.log('Organization created successfully')
      }
    } catch (error) {
      if (error.response && error.response.data && error.response.data.responseText) {
        console.log(error.response.data.responseText);
      }
    }
  }
}

const refreshPage = () => {
  window.location.reload()
}
</script>

<style>

/* Стили для выпадающего меню */
.dp__menu {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); /* Тень */
}

.dp__input_icon {
  display: none;
}

.dp__selection_preview {
  display: none;
}

</style>