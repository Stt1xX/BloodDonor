<template>
  <div class="min-h-screen flex flex-col">
    <Header :header-group="HeaderGroup.ADMIN"/>
    <main class="flex-grow container mx-auto py-20 px-20 flex flex-col items-center">
      <div class="w-full flex justify-between mb-4">
        <input type="text" v-model="searchQuery" placeholder="Поиск организаций..." class="border p-2 rounded w-1/2"/>
        <button @click="showForm = true; formTitle = 'Создание организации';" class="bg-red-600 text-white px-4 py-2 rounded hover:bg-red-700 transition-colors">Создать организацию</button>
      </div>
      <div class="w-full rounded-lg border border-gray-500 overflow-hidden">
        <table class="w-full">
          <tbody>
          <tr v-for="organization in filteredOrganizations" :key="organization.id" class="border-b border-gray-500 last:border-b-0">
            <td class="p-4">
              <div>{{ organization.name }}</div>
              <div class="text-gray-500">{{ organization.address }}</div>
            </td>
            <td class="p-4">
              <div>{{ organization.email }}</div>
              <div class="text-gray-500">{{ organization.phone }}</div>
            </td>
            <td class="p-4">{{convertOrganizationType(organization.organizationType) }}</td>
            <td class="p-4">{{ formatWorkingHours(organization.workingHours) }}</td>
            <td class="p-4 space-x-2">
              <button @click="editOrganization(organization)" class="text-blue-600 hover:text-blue-800">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16.5 3.5l4 4-12 12-4 1 1-4 12-12z" />
                </svg>
              </button>
              <button @click="deleteOrganization(organization.id)" class="text-red-600 hover:text-red-800">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                </svg>
              </button>
            </td>
          </tr>
          </tbody>
        </table>
      </div>

      <div class="mt-4 flex items-center">
        <button
            :disabled="currentPage === 1"
            class="bg-gray-300 text-gray-700 px-4 py-2 rounded mr-2 hover:bg-gray-400 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
        >
          Предыдущая
        </button>

        <span class="mx-2">{{ currentPage }}</span>

        <button
            @click="nextPage"
            :disabled="currentPage === totalPages"
            class="bg-gray-300 text-gray-700 px-4 py-2 rounded ml-2 hover:bg-gray-400 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
        >
          Следующая
        </button>
      </div>
      <NewOrganizationForm v-if="showForm" @close="closeForm" :title="formTitle" v-bind:organization="isEdit ? {...formOrganization} : undefined" :is-edit="isEdit"/>
    </main>
    <Footer />
  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue'
import NewOrganizationForm from '@/components/NewOrganizationForm.vue'
import Footer from "@/components/shared/Footer.vue";
import Header from "@/components/shared/Header.vue";
import {convertOrganizationType, formatWorkingHours, HeaderGroup} from "@/js/uitls.js";

const showForm = ref(false)
const formTitle = ref('Создание организации')
const formOrganization = ref({})
const isEdit = ref(false)

// DIMA
const searchQuery = ref('')
const currentPage = ref(1)
const totalPages = ref(1)

const organizations = ref([
    // DIMA // Пример данных
  { id: 1, name: 'Organization 1', address: 'Address 1', email: 'email1@example.com', organizationType: 'BLOOD_BANK', workingHours: [{hours : 12, minutes : 0}, {hours : 20, minutes: 0}], phone : '123456789'},
  { id: 2, name: 'Organization 2', address: 'Address 2', email: 'email2@example.com', organizationType: 'MEDICAL_INSTITUTION', workingHours: [{hours : 8, minutes : 0}, {hours : 18, minutes: 0}], phone : '123456789' },
])

const filteredOrganizations = organizations.value

const editOrganization = (organization) => {
  formTitle.value = 'Редактирование организации'
  formOrganization.value = organization
  isEdit.value = true
  showForm.value = true
}

// DIMA
const deleteOrganization = (id) => {
  // Логика удаления организации
}

const prevPage = () => {
  if (currentPage.value > 1) {
    currentPage.value--;
    // DIMA
    getOrganizations()
  }
};

const nextPage = () => {
  if (currentPage.value < totalPages.value) {
    currentPage.value++;
    // DIMA
    getOrganizations()
  }
};

const closeForm = () => {
  isEdit.value = false
  showForm.value = false
  formOrganization.value = {
    organizationType: '',
    name: '',
    address: '',
    phone: '',
    work_time: '',
  }
}

// DIMA
const getOrganizations = async () => {
  // Запрос на получение организаций // searhQuery.value,
}


onMounted(() => {
  getOrganizations()
})

</script>

<style scoped>

table td {
  border-right: none;
}
</style>