<template>
  <div class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
    <div class="bg-white p-8 rounded-lg shadow-lg w-[1000px] h-[650px] relative flex flex-col">
      <h2 class="text-3xl font-bold text-red-800 mb-4">Создание запроса</h2>

      <div class="flex-1">
        <div v-if="step === 1">
          <form @submit.prevent="nextStep" class="grid grid-cols-1 gap-6 h-full flex flex-col">
            <div class="space-y-6">
              <div class="col-span-1">
                <div class="flex justify-between">
                  <div class="flex flex-col">
                    <div class="flex items-center">
                      <label class="block text-sm font-medium text-gray-700">Группа крови</label>
                      <span v-if="errors.bloodGroup" class="text-red-500 text-xs ml-2">{{ errors.bloodGroup }}</span>
                    </div>
                    <div class="flex space-x-2 mt-2">
                      <button v-for="group in [1, 2, 3, 4]" :key="group"
                              type="button"
                              @click="bloodRequest.bloodGroup = group"
                              :class="{
                                'bg-red-500 text-white': bloodRequest.bloodGroup === group,
                                'bg-white text-gray-700': bloodRequest.bloodGroup !== group,
                                'hover:bg-red-500 hover:text-white transition-colors': true
                              }"
                              class="px-4 py-2 border rounded-md">
                        {{ group }}
                      </button>
                    </div>
                  </div>
                  <div class="flex flex-col">
                    <div class="flex items-center">
                      <label class="block text-sm font-medium text-gray-700">Резус-фактор</label>
                      <span v-if="errors.rhesusFactor" class="text-red-500 text-xs ml-2">{{ errors.rhesusFactor }}</span>
                    </div>
                    <div class="flex space-x-2 mt-2 justify-end">
                      <button v-for="rhesus in ['+', '-']" :key="rhesus"
                              type="button"
                              @click="bloodRequest.rhesusFactor = rhesus"
                              :class="{
                                'bg-red-500 text-white': bloodRequest.rhesusFactor === rhesus,
                                'bg-white text-gray-700': bloodRequest.rhesusFactor !== rhesus,
                                'hover:bg-red-500 hover:text-white transition-colors': true
                              }"
                              class="px-4 py-2 border rounded-md">
                        {{ rhesus }}
                      </button>
                    </div>
                  </div>
                </div>
              </div>
              <div class="col-span-1 flex items-center space-x-2">
                <input type="checkbox" v-model="bloodRequest.isEmergency" class="h-5 w-5 text-red-600 focus:ring-red-500"/>
                <label class="block text-sm font-medium text-gray-700">Экстренный запрос</label>
              </div>
              <div class="col-span-1">
                <div class="flex items-center">
                  <label class="block text-sm font-medium text-gray-700">Необходимый объем (л.)</label>
                  <span v-if="errors.volumeNeeded" class="text-red-500 text-xs ml-2">{{ errors.volumeNeeded }}</span>
                </div>
                <input type="text" v-model="bloodRequest.volumeNeeded"
                       class="mt-2 w-full border-gray-300 rounded-md shadow-sm focus:ring-red-500 focus:border-red-500"
                       placeholder="Введите объём"/>
              </div>
              <div class="col-span-1">
                <div class="flex items-center">
                  <label class="block text-sm font-medium text-gray-700">Причина запроса</label>
                  <span v-if="errors.description" class="text-red-500 text-xs ml-2">{{ errors.description }}</span>
                </div>
                <textarea v-model="bloodRequest.description"
                          class="mt-2 w-full h-32 border-gray-300 rounded-md shadow-sm focus:ring-red-500 focus:border-red-500 p-2"
                          placeholder="Укажите причину запроса крови"></textarea>
              </div>
            </div>
          </form>
        </div>

        <div v-if="step === 2">
          <input type="text" v-model="searchQuery" placeholder="Поиск по организациям..."
                 @focus="search"
                 @input="searchDebounced"
                 class="mb-4 w-full border-gray-300 rounded-md shadow-sm focus:ring-red-500 focus:border-red-500 px-2 py-1"/>

          <div v-if="banks.length > 0" class="w-full rounded-lg border border-gray-500 overflow-hidden">
            <div class="overflow-y-auto" style="max-height: 400px;">
              <table class="w-full">
                <thead>
                <tr>
                  <th class="p-4 text-left">Наименование</th>
                  <th class="p-4 text-left">Контакты</th>
                  <th class="p-4 text-left">График работы</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="organization in banks"
                    :key="organization.id"
                    class="border-b border-gray-500 last:border-b-0 first:border-t"
                    :class="{
                'bg-red-100': isSelected(organization),
                'hover:bg-gray-50': !isSelected(organization)
              }"
                    @click="toggleSelection(organization)">
                  <td class="p-4">
                    <div class="font-medium">{{ organization.name }}</div>
                    <div class="text-gray-500">{{ organization.address }}</div>
                  </td>
                  <td class="p-4">
                    <div class="text-gray-500">{{ organization.phone }}</div>
                  </td>
                  <td class="p-4">{{ formatWorkingHours(organization.hoursFrom, organization.hoursTo,
                      organization.minutesFrom, organization.minutesTo) }}</td>
                  <td class="p-4 w-[52px] text-center">
                    <svg v-if="isSelected(organization)" xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-red-600 mx-auto" viewBox="0 0 20 20" fill="currentColor">
                      <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd" />
                    </svg>
                  </td>
                </tr>
                </tbody>
              </table>
            </div>
          </div>
          <p v-else class="text-gray-500 text-center mt-20 mb-20">Тут пока тихо...</p>
        </div>
      </div>
      <div class="mt-auto pt-4 border-t">
        <div class="flex justify-between">
          <button v-if="step === 1" type="button" @click="emit('close')"
                  class="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600 transition-colors">
            Отмена
          </button>
          <button v-if="step === 2" type="button" @click="step = 1"
                  class="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600 transition-colors">
            Назад
          </button>

          <button v-if="step === 1" type="submit" @click="nextStep"
                  class="bg-red-600 text-white px-4 py-2 rounded hover:bg-red-700 transition-colors">
            Далее
          </button>
          <button v-if="step === 2" type="button" @click="submitRequest"
                  class="bg-red-600 text-white px-4 py-2 rounded hover:bg-red-700 transition-colors"
                  :disabled="selectedBanks.length === 0"
                  :class="{'opacity-50 cursor-not-allowed': selectedBanks.length === 0}">
            Отправить запрос
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {ref} from 'vue';
import {abstractFetching, formatWorkingHours} from "@/js/utils.js";
import axios from "axios";
import {showAlert} from "@/js/custom-alert.js";
import debounce from "lodash.debounce";

const emit = defineEmits(['close']);
const step = ref(1);
const searchQuery = ref('');
const selectedBanks = ref([]);
const bloodRequest = ref({ bloodGroup: '', rhesusFactor: '', volumeNeeded: '', isEmergency: false, description: '' });
const errors = ref({});
const banks = ref([]);

const getBanksWithResources = async (abortController) => {
  try{
    const encodedPattern = encodeURIComponent(searchQuery.value);
    const url = `/api/organizations/banks_with_resources?pattern=${encodedPattern}`
    const response = await axios.post(url, {
      bloodGroup : bloodRequest.value.bloodGroup,
      rhesusFactor : bloodRequest.value.rhesusFactor,
      volumeNeeded : bloodRequest.value.volumeNeeded.toString().replace(',', '.')
    }, { signal: abortController.signal });
    banks.value = response.data
  } catch (error){
    showAlert(error)
  }
}

const search = async () => {
  await abstractFetching(getBanksWithResources)
}

const searchDebounced = debounce(search, 500);

const isSelected = (bank) => {
  return selectedBanks.value.some(b => b.id === bank.id);
};

const isValidNumber = (value) => {
  return /^[0-9]+([.,][0-9]{1,2})?$/.test(value);
};

const nextStep = async () => {
  errors.value = {};
  if (!bloodRequest.value.bloodGroup) errors.value.bloodGroup = 'Выберите группу крови';
  if (!bloodRequest.value.rhesusFactor) errors.value.rhesusFactor = 'Выберите резус-фактор';
  if (!bloodRequest.value.volumeNeeded) {
    errors.value.volumeNeeded = 'Введите объём';
  } else if (!isValidNumber(bloodRequest.value.volumeNeeded)) {
    errors.value.volumeNeeded = 'Введите корректный объём (например: 0.5, 1.2)';
  }
  if (!bloodRequest.value.description) {
    errors.value.description = 'Укажите причину запроса';
  }
  if (Object.keys(errors.value).length === 0){
    await search()
    step.value = 2;
  }


};

const toggleSelection = (bank) => {
  if (isSelected(bank)) {
    selectedBanks.value = selectedBanks.value.filter(b => b.id !== bank.id);
  } else {
    selectedBanks.value.push(bank);
  }
};

const submitRequest = async () => {
  try{
    const url = '/api/blood_requests'
    const request = bloodRequest.value
    request.bloodBanks = selectedBanks.value.map(bank => bank.id)
    request.volumeNeeded = bloodRequest.value.volumeNeeded.toString().replace(',', '.')
    const response = await axios.post(url, request)
    showAlert(response.data)
    if (response.status === 200)
      emit('close');
  } catch (error){
    showAlert(error.data)
  }

};
</script>

<style scoped>
.overflow-y-auto::-webkit-scrollbar {
  width: 8px;
}

.overflow-y-auto::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

.overflow-y-auto::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 4px;
}

.overflow-y-auto::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style>