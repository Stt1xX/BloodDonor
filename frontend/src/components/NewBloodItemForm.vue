<template>
  <div class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
    <div class="bg-white p-8 rounded-lg shadow-lg w-1/2 relative">
      <h2 class="text-3xl font-bold text-red-800 mb-4">{{ title }}</h2>

      <form @submit.prevent="handleSubmit" class="grid grid-cols-1 gap-6">

        <div class="col-span-1">
          <div class="flex justify-between">
            <div class="flex flex-col">
              <div class="flex items-center">
                <label class="block text-sm font-medium text-gray-700">
                  Группа крови
                </label>
                <span v-if="errors.bloodGroup" class="text-red-500 text-xs ml-2">{{ errors.bloodGroup }}</span>
              </div>
              <div class="flex space-x-2 mt-2">
                <button v-for="group in [1, 2, 3, 4]" :key="group"
                        type="button"
                        @click="bloodReserve.group = group"
                        :class="{
                          'bg-red-500 text-white':  bloodReserve.group === group,
                          'bg-white text-gray-700': bloodReserve.group !== group,
                          'hover:bg-red-500 hover:text-white transition-colors': true
                        }"
                        class="px-4 py-2 border rounded-md">
                  {{ group }}
                </button>
              </div>
            </div>
            <div class="flex flex-col">
              <div class="flex items-center">
                <label class="block text-sm font-medium text-gray-700">
                  Резус-фактор
                </label>
                <span v-if="errors.rhesusFactor" class="text-red-500 text-xs ml-2">{{ errors.rhesusFactor }}</span>
              </div>
              <div class="flex space-x-2 mt-2 justify-end">
                <button v-for="rhesus in ['+', '-']" :key="rhesus"
                        type="button"
                        @click="bloodReserve.rhesus = rhesus"
                        :class="{
                          'bg-red-500 text-white': bloodReserve.rhesus === rhesus,
                          'bg-white text-gray-700': bloodReserve.rhesus !== rhesus,
                          'hover:bg-red-500 hover:text-white transition-colors': true
                        }"
                        class="px-4 py-2 border rounded-md">
                  {{ rhesus }}
                </button>
              </div>
            </div>

          </div>
        </div>

        <div class="col-span-1">
          <label class="block text-sm font-medium text-gray-700">
            Дата просрочки
            <span v-if="errors.expiryDate" class="text-red-500 text-xs ml-2">{{ errors.expiryDate }}</span>
          </label>
          <vue-date-picker v-model="bloodReserve.expiryDate"
                           :enable-time-picker="false"
                           placeholder="Выберите дату"
                           class="mt-2 w-full border-gray-300 rounded-md shadow-sm focus:ring-red-500 focus:border-red-500"/>
        </div>

        <div class="col-span-1">
          <label for="volume" class="block text-sm font-medium text-gray-700">
            Объём (л.)
            <span v-if="errors.volume" class="text-red-500 text-xs ml-2">{{ errors.volume }}</span>
          </label>
          <input type="text" id="volume" v-model="bloodReserve.volume"
                 class="mt-2 w-full border-gray-300 rounded-md shadow-sm focus:ring-red-500 focus:border-red-500"
                 placeholder="Введите объём"/>
        </div>

        <div class="col-span-1 flex justify-end mt-4">
          <button type="button" @click="$emit('close')"
                  class="bg-gray-500 text-white px-4 py-2 rounded mr-2 hover:bg-gray-600 transition-colors">
            Отмена
          </button>
          <button type="submit"
                  class="bg-red-600 text-white px-4 py-2 rounded hover:bg-red-700 transition-colors">
            {{ isEdit ? 'Сохранить' : 'Добавить' }}
          </button>
        </div>

      </form>
    </div>
  </div>
</template>


<script setup>
import {ref} from 'vue';
import axios from 'axios';
import VueDatePicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css';
import { showAlert } from '@/js/custom-alert.js';

const emit = defineEmits(['close']);

const props = defineProps(
{
        title: {
          type: String,
          required: true
        },
        bloodReserve: {
          type: Object,
          required: true,
          default: () => ({
            bloodGroup: '',
            rhesusFactor: '',
            expiryDate: '',
            volume: ''
          })
        },
        isEdit: {
          type: Boolean,
          required: true
        }
      }
);

const bloodReserve = ref({ ...props.bloodReserve });
const errors = ref({});
const isSubmitted = ref(false);

const isValidNumber = (value) => {
  return /^[0-9]+([.,][0-9]+)?$/.test(value);
};

const validateForm = () => {
  errors.value = {};

  if (!bloodReserve.value.group) {
    errors.value.bloodGroup = 'Выберите группу крови';
  }
  if (!bloodReserve.value.rhesus) {
    errors.value.rhesusFactor = 'Выберите резус-фактор';
  }
  if (!bloodReserve.value.expiryDate) {
    errors.value.expiryDate = 'Выберите дату просрочки';
  }
  if (!bloodReserve.value.volume) {
    errors.value.volume = 'Введите объём';
  } else if (!isValidNumber(bloodReserve.value.volume)) {
    errors.value.volume = 'Введите корректный объём (например: 0.5, 1,2)';
  }

  return Object.keys(errors.value).length === 0;
};

// Функция создания записи
const createBloodReserve = async () => {
  try {
    const response = await axios.post('/api/blood-reserve', {
      bloodGroup: bloodReserve.value.bloodGroup,
      rhesusFactor: bloodReserve.value.rhesusFactor,
      expiryDate: bloodReserve.value.expiryDate.toISOString().split('T')[0],
      volume: parseFloat(bloodReserve.value.volume.toString().replace(',', '.'))
    });
    showAlert(response.data);
    emit('close');
  } catch (error) {
    showAlert(error.response.data);
  }
};

// Функция редактирования записи
const editBloodReserve = async () => {
  try {
    const response = await axios.put('/api/blood-reserve', {
      id: bloodReserve.value.id,
      bloodGroup: bloodReserve.value.bloodGroup,
      rhesusFactor: bloodReserve.value.rhesusFactor,
      expiryDate: bloodReserve.value.expiryDate.toISOString().split('T')[0],
      volume: parseFloat(bloodReserve.value.volume.toString().replace(',', '.'))
    });
    showAlert(response.data);
    emit('close');
  } catch (error) {
    showAlert(error.response.data);
  }
};

const handleSubmit = async () => {
  isSubmitted.value = true;
  if (validateForm()) {
    if (props.isEdit) {
      await editBloodReserve();
    } else {
      await createBloodReserve();
    }
  }
};
</script>

<style scoped>

</style>
