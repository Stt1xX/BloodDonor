<template>
  <div class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
    <div class="bg-white p-8 rounded-lg shadow-lg w-1/2 relative">
      <h2 class="text-3xl font-bold text-red-800 mb-4">{{ props.title }}</h2>
      <form @submit.prevent="handleCreateOrganization" class="grid grid-cols-1 gap-6">
        <!-- Organization Type Selector -->
        <div class="group col-span-1">
          <label for="organizationType" class="block text-sm font-medium text-gray-700">
            Тип организации
            <span v-if="errors.organizationType" class="text-red-500 text-xs ml-2">{{ errors.organizationType }}</span>
          </label>
          <select id="organizationType" v-model="organization.organizationType" class="block w-full rounded-lg border-gray-300 shadow-sm focus:ring-red-500 focus:border-red-500 hover:border-red-300 transition-all duration-200" :disabled="isEdit">
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
                v-model="organization.name"
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
                v-model="organization.address"
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
                  v-model="organization.phone"
                  class="block w-full rounded-lg border-gray-300 shadow-sm focus:ring-red-500 focus:border-red-500 hover:border-red-300 transition-all duration-200"
                  placeholder="Введите телефон"
              />
            </div>
          </div>
          <div>
            <label for="workingHours" class="block text-sm font-medium text-gray-700">
              Время работы
              <span v-if="errors.workingHours" class="text-red-500 text-xs ml-2">{{ errors.workingHours }}</span>
            </label>
            <div class="relative mt-1">
              <vue-date-picker v-model="organization.workingHours"
                               time-picker
                               :range="{ disableTimeRangeValidation: true }"
                               placeholder="Select Time"
              />
            </div>
          </div>
        </div>

        <div class="col-span-1 flex justify-end mt-4">
          <button type="button" @click="$emit('close')" class="bg-gray-500 text-white px-4 py-2 rounded mr-2 hover:bg-gray-600 transition-colors">Отмена</button>
          <button type="submit" class="bg-red-600 text-white px-4 py-2 rounded hover:bg-red-700 transition-colors">Создать</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue'
import axios from 'axios'

import VueDatePicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css'
import {showAlert} from "@/js/custom-alert.js";

const emit = defineEmits(['close'])

const props = defineProps(
    {
      title : {
        type : String,
        required : true
      },
      organization : {
        type : Object,
        required : true,
        default : (() => {
          return {
            organizationType : '',
            name : '',
            address : '',
            phone : '',
            workingHours : ''
          }
        })
      },
      isEdit : {
        type : Boolean,
        required : true
      }
    }
)

const organization = ref(props.organization)

const errors = ref({})

const validateForm = () => {
  errors.value = {}
  if (!organization.value.organizationType) errors.value.organizationType = 'Тип организации обязателен'
  if (!organization.value.name) errors.value.name = 'Название обязательно'
  if (!organization.value.address) errors.value.address = 'Адрес обязателен'
  if (!organization.value.phone) errors.value.phone = 'Телефон обязателен'
  if (!organization.value.workingHours) errors.value.workingHours = 'Время работы обязательно'
  if (is_bad_time(organization.value.workingHours)) {
    errors.value.workingHours = 'Время окончания должно быть больше времени начала'
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

const createOrganization = async () => {
  try {
    const response = await axios.post('/api/organizations',
{
      type : organization.value.organizationType,
      name : organization.value.name,
      address : organization.value.address,
      phone : organization.value.phone,
      hoursFrom : organization.value.workingHours[0].hours,
      hoursTo : organization.value.workingHours[1].hours,
      minutesFrom : organization.value.workingHours[0].minutes,
      minutesTo : organization.value.workingHours[1].minutes
    })
    showAlert(response.data)
    emit('close')
  } catch (error) {
    console.log(error)
    showAlert(error.response.data)
  }
}

const editOrganization = async (id) => {
  try {
    const response = await axios.put('/api/organizations',
        {
          id : organization.value.id,
          type : organization.value.organizationType,
          name : organization.value.name,
          address : organization.value.address,
          phone : organization.value.phone,
          hoursFrom : organization.value.workingHours[0].hours,
          hoursTo : organization.value.workingHours[1].hours,
          minutesFrom : organization.value.workingHours[0].minutes,
          minutesTo : organization.value.workingHours[1].minutes
        })
    showAlert(response.data)
    emit('close')
  } catch (error) {
    console.log(error)
    showAlert(error.response.data)
  }
}

const handleCreateOrganization = async () => {
  if (validateForm()) {
    if (props.isEdit) {
      await editOrganization(props.organization.id)
    } else {
      await createOrganization()
    }

  }
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