<template>
  <div class="min-h-screen flex flex-col bg-gray-100">
    <Header :header-group="HeaderGroups.MEDICAL_EMPLOYEE" />

    <main class="flex-grow container mx-auto py-16 px-8 flex flex-col items-center">
      <div class="w-full flex flex-col md:flex-row justify-between items-center mb-6 space-y-4 md:space-y-0">
        <div class="flex items-center flex-wrap gap-4">
          <div class="flex items-center space-x-2">
            <span class="text-gray-700 font-medium">Статус:</span>
            <button
                v-for="status in ['В ожидании', 'Отклонено', 'Принято']"
                :key="status"
                @click="toggleStatus(status)"
                :class="[
                  status === requestStatus ? 'bg-red-500 text-white' : 'bg-white',
                  'hover:bg-red-500 hover:text-white transition-colors border px-4 py-2 rounded-md shadow-sm'
                ]"
            >
              {{ status }}
            </button>
          </div>
        </div>
        <button
            @click="showForm = true"
            class="bg-red-600 text-white px-4 py-2 rounded-md shadow-md hover:bg-red-700 transition-colors"
        >
          Добавить кровь
        </button>
      </div>

      <div v-if="managedEntities.length > 0" class="w-full rounded-lg border border-gray-500 overflow-hidden">
        <table class="w-full">
          <thead>
          <tr class="bg-gray-100">
            <th class="p-4 text-center">Группа крови</th>
            <th class="p-4 text-center">Резус-фактор</th>
            <th class="p-4 text-center">Объем</th>
            <th class="p-4 text-center">Статус</th>
            <th class="p-4 text-center">Дата создания</th>
            <th class="p-4 text-center">Действия</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="managedEntity in managedEntities" :key="managedEntity.id" class="border-b border-gray-500 last:border-b-0 hover:bg-gray-50">
            <td class="p-4 text-center">
              <div>гр. {{ managedEntity.bloodGroup }}</div>
            </td>
            <td class="p-4 text-center">
              <div>rh{{ managedEntity.rhFactor }}</div>
            </td>
            <td class="p-4 text-center">
              <div>{{ managedEntity.volume }} л.</div>
            </td>
            <td class="p-4 text-center">
              <span :class="getStatusClass(managedEntity.requestStatus)" class="px-3 py-1 rounded-full text-sm">
                {{ managedEntity.requestStatus }}
              </span>
            </td>
            <td class="p-4 text-center">
              {{ formatDate(managedEntity.createdAt) }}
            </td>
            <td class="p-4 text-center space-x-2">
              <button
                  @click.stop="showDetails(managedEntity)"
                  class="text-blue-600 hover:text-blue-800"
                  title="Подробнее"
              >
                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
              </button>
              <button
                  v-if="managedEntity.requestStatus === 'В ожидании'"
                  @click.stop="deleteManagedEntity(managedEntity.id)"
                  class="text-red-600 hover:text-red-800"
                  title="Удалить"
              >
                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
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
      <CreateBloodReqWindow v-if="showForm" @close="closeForm"/>
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
import { HeaderGroups } from "@/js/utils.js";
import Header from "@/components/shared/Header.vue";
import {showAlert} from "@/js/custom-alert.js";
import axios from "axios";
import {get_token} from "@/js/csrf-token.js";
import Footer from "@/components/shared/Footer.vue";
import CreateBloodReqWindow from "@/components/CreateBloodReqWindow.vue";
import RequestDetailsModal from "@/components/RequestDetailsModal.vue";

// Тестовые данные
const managedEntities = ref([
  {
    id: 1,
    bloodGroup: 1,
    rhFactor: '+',
    volume: 1.5,
    requestStatus: 'В ожидании',
    createdAt: '2023-05-15T10:30:00',
    author: 'Иванов И.И.',
    details: 'Необходимо для операции на сердце',
    processedBy: null,
    rejectionReason: null
  },
  {
    id: 2,
    bloodGroup: 2,
    rhFactor: '-',
    volume: 2.0,
    requestStatus: 'Принято',
    createdAt: '2023-05-14T14:45:00',
    author: 'Петров П.П.',
    details: 'Запланированная операция',
    processedBy: 'Сидорова С.С.',
    processedAt: '2023-05-14T15:30:00',
    rejectionReason: null
  },
  {
    id: 3,
    bloodGroup: 3,
    rhFactor: '+',
    volume: 1.0,
    requestStatus: 'Отклонено',
    createdAt: '2023-05-13T09:15:00',
    author: 'Смирнов С.С.',
    details: 'Недостаточно доноров',
    processedBy: 'Кузнецова К.К.',
    processedAt: '2023-05-13T10:20:00',
    rejectionReason: 'Недостаточно доноров с данной группой крови'
  }
]);

const selectedRequest = ref(null);


const showForm = ref(false);
const requestStatus = ref(null);
const currentPage = ref(0);
const totalPages = ref(1);

const formatDate = (dateString) => {
  const date = new Date(dateString);
  return date.toLocaleDateString() + ' ' + date.toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'});
};

const getStatusClass = (status) => {
  switch(status) {
    case 'В ожидании': return 'bg-yellow-100 text-yellow-800';
    case 'Принято': return 'bg-green-100 text-green-800';
    case 'Отклонено': return 'bg-red-100 text-red-800';
    default: return 'bg-gray-100 text-gray-800';
  }
};

const toggleStatus = (status) => {
  requestStatus.value = requestStatus.value === status ? null : status;
  updateManagedEntities();
};

const showDetails = (request) => {
  selectedRequest.value = request;
};

const deleteManagedEntity = async (id) => {
  try {
    // В реальном приложении здесь будет вызов API
    managedEntities.value = managedEntities.value.filter(item => item.id !== id);
    showAlert('Заявка успешно удалена');
  } catch (error) {
    showAlert(error.response?.data || 'Ошибка при удалении');
  }
};

const prevPage = () => {
  if (currentPage.value >= 1) {
    currentPage.value--;
    updateManagedEntities();
  }
};

const nextPage = () => {
  if (currentPage.value < totalPages.value) {
    currentPage.value++;
    updateManagedEntities();
  }
};

const closeForm = () => {
  showForm.value = false;
  updateManagedEntities();
};

const updateManagedEntities = () => {
  // В реальном приложении здесь будет вызов API
  // getManagedEntities(new AbortController());
};

onMounted(() => {
  get_token();
});

onBeforeUnmount(() => {
  // Очистка интервалов, если есть
});
</script>