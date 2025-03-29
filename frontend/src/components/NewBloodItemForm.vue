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
                        @click="bloodReserve.bloodGroup = group"
                        :class="{
                          'bg-red-500 text-white':  bloodReserve.bloodGroup === group,
                          'bg-white text-gray-700': bloodReserve.bloodGroup !== group,
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
                        @click="bloodReserve.rhesusFactor = rhesus"
                        :class="{
                          'bg-red-500 text-white': bloodReserve.rhesusFactor === rhesus,
                          'bg-white text-gray-700': bloodReserve.rhesusFactor !== rhesus,
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
            Срок годности
            <span v-if="errors.createdAt" class="text-red-500 text-xs ml-2">{{ errors.createdAt }}</span>
            <span v-if="errors.expirationDate" class="text-red-500 text-xs ml-2">{{ errors.expirationDate }}</span>
          </label>
          <div class="mt-2 flex space-x-2">
            <vue-date-picker v-model="bloodReserve.createdAt"
                             :enable-time-picker="false"
                             placeholder="Дата поставки"
                             class="w-full border-gray-300 rounded-md shadow-sm focus:ring-red-500 focus:border-red-500"/>
            <vue-date-picker v-model="bloodReserve.expirationDate"
                             :enable-time-picker="false"
                             placeholder="Дата просрочки"
                             class="w-full border-gray-300 rounded-md shadow-sm focus:ring-red-500 focus:border-red-500"/>
          </div>
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
import {onMounted, ref} from 'vue';
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
            createdAt: '',
            expirationDate: '',
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
  return /^[0-9]+([.,][0-9]{1,2})?$/.test(value);
};

const validateForm = () => {
  errors.value = {};
  const today = new Date();
  const tomorrow = new Date(today)
  tomorrow.setDate(today.getDate() + 1)
  today.setHours(0, 0, 0, 0);
  tomorrow.setHours(0, 0, 0, 0);

  if (!bloodReserve.value.bloodGroup) {
    errors.value.bloodGroup = 'Выберите группу крови';
  }
  if (!bloodReserve.value.rhesusFactor) {
    errors.value.rhesusFactor = 'Выберите резус-фактор';
  }
  if (!bloodReserve.value.createdAt) {
    errors.value.createdAt = 'Выберите дату поставки';
  } else if (new Date(bloodReserve.value.createdAt).setHours(0, 0, 0, 0) > today) {
    errors.value.createdAt = 'Дата поставки не может быть в будущем';
  }
  if (!bloodReserve.value.expirationDate) {
    errors.value.expirationDate = 'Выберите дату просрочки';
  }
  if (bloodReserve.value.createdAt && bloodReserve.value.expirationDate) {
    const createdAt = new Date(bloodReserve.value.createdAt).setHours(0, 0, 0, 0);
    const expirationDate = new Date(bloodReserve.value.expirationDate).setHours(0, 0, 0, 0);

    if (createdAt >= expirationDate) {
      errors.value.expirationDate = 'Дата просрочки должна быть позже даты поставки';
    }
    if (expirationDate < tomorrow) {
      errors.value.expirationDate = 'Дата просрочки не может быть раньше завтрашнего дня';
    }
  }
  if (!bloodReserve.value.volume) {
    errors.value.volume = 'Введите объём';
  } else if (!isValidNumber(bloodReserve.value.volume)) {
    errors.value.volume = 'Введите корректный объём (например: 0.5, 1.2)';
  }

  return Object.keys(errors.value).length === 0;
};

const createBloodReserve = async () => {
  try {
    const response = await axios.post('/api/blood_units', {
      bloodGroup: bloodReserve.value.bloodGroup,
      rhesusFactor: bloodReserve.value.rhesusFactor,
      expirationDate: bloodReserve.value.expirationDate.toISOString().split('T')[0],
      createdAt: bloodReserve.value.createdAt.toISOString().split('T')[0],
      volume: parseFloat(bloodReserve.value.volume.toString().replace(',', '.'))
    });
    showAlert(response.data);
    emit('close');
  } catch (error) {
    showAlert(error.response.data);
  }
};

const editBloodReserve = async () => {
  try {
    const response = await axios.put('/api/blood_units', {
      id: bloodReserve.value.id,
      bloodGroup: bloodReserve.value.bloodGroup,
      rhesusFactor: bloodReserve.value.rhesusFactor,
      expirationDate: bloodReserve.value.expirationDate.toISOString().split('T')[0],
      createdAt: bloodReserve.value.createdAt.toISOString().split('T')[0],
      volume: parseFloat(bloodReserve.value.volume.toString().replace(',', '.'))
    });
    showAlert(response.data);
    emit('close');
  } catch (error) {
    console.log(error)
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
