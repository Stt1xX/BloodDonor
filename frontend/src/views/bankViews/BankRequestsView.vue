<template>
  <div class="min-h-screen flex flex-col bg-gray-100">
    <Header :header-group="HeaderGroups.BANK_EMPLOYEE" />

    <main class="flex-grow container mx-auto py-16 px-8 flex flex-col items-center">
      <div class="w-full flex flex-col md:flex-row justify-between items-center mb-6 space-y-4 md:space-y-0">
        <div class="flex items-center flex-wrap gap-4">
          <div class="flex items-center space-x-2">
            <span class="text-gray-700 font-medium">Группа:</span>
            <button
                v-for="group in [1, 2, 3, 4]"
                :key="group"
                @click="toggleBloodGroup(group)"
                :class="[
                bloodGroup === group ? 'bg-red-500 text-white' : 'bg-white',
                'hover:bg-red-500 hover:text-white transition-colors border px-4 py-2 rounded-md shadow-sm'
                ]"
            >
              {{ group }}
            </button>
          </div>
          <div class="flex items-center space-x-2">
            <span class="text-gray-700 font-medium">Резус:</span>
            <button
                v-for="rhesus in ['+', '-']"
                :key="rhesus"
                @click="toggleRhesus(rhesus)"
                :class="[
                rhesusFactor === rhesus ? 'bg-red-500 text-white' : 'bg-white',
                'hover:bg-red-500 hover:text-white transition-colors border px-4 py-2 rounded-md shadow-sm'
                ]"
                class="px-4 py-2 border rounded-md shadow-sm"
            >
              {{ rhesus }}
            </button>
          </div>
          <div>
            <select @change="updateManagedEntities()" v-model="sortBy" class="px-4 py-2 border rounded-md bg-white shadow-sm">
              <option value="request.volumeNeeded">По объему</option>
              <option value="request.createdAt">По дате поставки</option>
              <option value="request.isEmergency">По приоритету</option>
            </select>
          </div>
        </div>
        <div class="flex items-center space-x-2">
          <span class="text-gray-700 font-medium">Инвертировать поиск:</span>
          <input @change="updateManagedEntities()" type="checkbox" v-model="reverseSearch" class="form-checkbox h-5 w-5 text-red-600" />
        </div>
        <p>
          Всего запросов: {{ RequestNumberByGroupAndRhesus }}
        </p>
        <button
            class="invisible bg-red-600 text-white px-4 py-2 rounded-md shadow-md hover:bg-red-700 transition-colors"
        >
          Добавить кровь
        </button>
      </div>

      <div v-if="managedEntities.length > 0" class="w-full rounded-lg border border-gray-500 overflow-hidden">
        <table class="w-full">
          <thead>
          <tr class="bg-gray-100">
            <th class="p-4 text-center">Отправитель</th>
            <th class="p-4 text-center">Группа крови</th>
            <th class="p-4 text-center">Резус-фактор</th>
            <th class="p-4 text-center">Объем</th>
            <th class="p-4 text-center">Приоритет</th>
            <th class="p-4 text-center">Дата создания</th>
            <th class="p-4 text-center">Действия</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="managedEntity in managedEntities" :key="managedEntity.id" class="border-b border-gray-500 last:border-b-0">
            <td class="p-4 text-center">
              <div>{{ managedEntity.institution.name }}</div>
            </td>
            <td class="p-4 text-center">
              <div>{{ managedEntity.bloodGroup }}г.</div>
            </td>
            <td class="p-4 text-center">
              <div>rh{{ managedEntity.rhesusFactor }}</div>
            </td>
            <td class="p-4 text-center">
              <div>{{ managedEntity.volumeNeeded }}л.</div>
            </td>
            <td class="p-4 text-center text-red-500">
              <div v-if="managedEntity.isEmergency">Срочно!</div>
            </td>
            <td class="p-4 text-center">
              {{ formatTimestamp(managedEntity.createdAt) }}
            </td>
            <td class="p-4 text-center space-x-2">
              <button @click="accept(managedEntity.id)" class="text-green-600 hover:text-green-800">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
                </svg>
              </button>
              <button @click="reject(managedEntity)" class="text-red-600 hover:text-red-800">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                </svg>
              </button>
              <button
                  @click.stop="showDetails(managedEntity)"
                  class="text-blue-600 hover:text-blue-800"
                  title="Подробнее"
              >
                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
              </button>
            </td>
          </tr>
          </tbody>
        </table>
      </div>

      <p v-else class="text-gray-500 text-center mt-20 mb-20">Тут пока тихо...</p>

      <div class="mt-4 flex items-center">
        <button
            @click="prevPage"
            :disabled="currentPage === 0"
            class="bg-gray-300 text-gray-700 px-4 py-2 rounded mr-2 hover:bg-gray-400 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
        >
          Предыдущая
        </button>

        <span class="mx-2">{{ currentPage + 1 }}</span>

        <button
            @click="nextPage"
            :disabled="currentPage === totalPages || currentPage === totalPages - 1"
            class="bg-gray-300 text-gray-700 px-4 py-2 rounded ml-2 hover:bg-gray-400 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
        >
          Следующая
        </button>
      </div>
      <RejectBloodReqWindow v-if="showForm" @close="closeForm" @confirm="handleRejectSubmit" :request-data="formManagedEntity"/>
      <RequestDetailsModal
          v-if="selectedRequest"
          :request="selectedRequest"
          @close="selectedRequest = null"
      />
    </main>
    <Footer />
  </div>
</template>

<script setup>
import {onBeforeUnmount, onMounted, ref} from "vue";
import {formatTimestamp, HeaderGroups} from "@/js/utils.js";
import Header from "@/components/shared/Header.vue";
import {showAlert} from "@/js/custom-alert.js";
import axios from "axios";
import {get_token} from "@/js/csrf-token.js";
import Footer from "@/components/shared/Footer.vue";
import RejectBloodReqWindow from "@/components/RejectBloodReqWindow.vue";
import RequestDetailsModal from "@/components/RequestDetailsModal.vue";

const managedEntities = ref([]);

const toggleBloodGroup = (group) => {
  bloodGroup.value = bloodGroup.value === group ? null : group;
  updateManagedEntities()
};

const toggleRhesus = (rhesus) => {
  rhesusFactor.value = rhesusFactor.value === rhesus ? null : rhesus;
  updateManagedEntities()
};

const showForm = ref(false)
const selectedRequest = ref(null);

const reverseSearch = ref(false);
const bloodGroup = ref(null);
const rhesusFactor = ref(null);
const formManagedEntity = ref({})
const RequestNumberByGroupAndRhesus = ref(0);
const sortBy = ref("request.volumeNeeded");

const currentPage = ref(0)
const totalPages = ref(1)

const closeForm = () => {
  showForm.value = false
  updateManagedEntities()
}

const prevPage = () => {
  if (currentPage.value > 1) {
    currentPage.value--;
    getManagedEntities()
  }
};

const nextPage = () => {
  if (currentPage.value < totalPages.value) {
    currentPage.value++;
    getManagedEntities()
  }
};



let polling
onMounted(() => {
  updateManagedEntities()
  polling = setInterval(updateManagedEntities, 7000);
  get_token()
})

onBeforeUnmount(() => {
  clearInterval(polling)
})

const getManagedEntities = async (abortController) => {
  try {
    const url = `/api/blood_requests/bank?rhesusFactor=${rhesusFactor.value}&bloodGroup=${bloodGroup.value}&reverse=${reverseSearch.value}&page=${currentPage.value}&size=8&sort=${sortBy.value}`
    const response = await axios.get(url,{ signal: abortController.signal })
    totalPages.value = response.data.totalPages;
    managedEntities.value = response.data.content;
    RequestNumberByGroupAndRhesus.value =  response.data.totalElements;
  } catch (error) {
    showAlert(error.response.data);
  }
}

const updateManagedEntities = () => {
  getManagedEntities(new AbortController())
}

const reject = (managedEntity) => {
  formManagedEntity.value = { ...managedEntity };
  showForm.value = true;
};

const accept = async (id) => {
  try{
    const url = `/api/blood_requests/accept/${id}`
    const response = await axios.post(url)
    showAlert(response.data)
    updateManagedEntities()
  } catch (error){
    showAlert(error)
  }
}

const handleRejectSubmit = (data) => {
  console.log("Отказ оформлен:", data);
  closeForm();
};

const showDetails = (request) => {
  selectedRequest.value = request;
};

</script>