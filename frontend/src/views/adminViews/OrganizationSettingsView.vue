<template>
  <div class="min-h-screen flex flex-col">
    <Header :header-group="HeaderGroups.ADMIN"/>
    <main class="flex-grow container mx-auto py-32 px-20 flex flex-col items-center">
      <div class="w-full flex justify-between mb-4">
        <input type="text"
               v-model="searchQuery"
               placeholder="Поиск организаций..."
               class="border p-2 rounded w-1/2"
               @focus="search"
               @input="searchDebounced"
        />
        <button @click="showForm = true; formTitle = 'Создание организации';" class="bg-red-600 text-white px-4 py-2 rounded hover:bg-red-700 transition-colors">Создать организацию</button>
      </div>
        <div v-if="organizations.length > 0" class="w-full  rounded-lg border border-gray-500 overflow-hidden">
        <table class="w-full">
          <thead>
          <tr>
            <th class="p-4 text-left">Наименование</th>
            <th class="p-4 text-left">Контакты</th>
            <th class="p-4 text-left">Тип</th>
            <th class="p-4 text-left">График работы</th>
            <th class="p-4 text-left">Действия</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="organization in organizations" :key="organization.id" class="border-b border-gray-500 last:border-b-0 first:border-t">
            <td class="p-4">
              <div>{{ organization.name }}</div>
              <div class="text-gray-500">{{ organization.address }}</div>
            </td>
            <td class="p-4">
              <div class="text-gray-500">{{ organization.phone }}</div>
            </td>
            <td class="p-4">{{convertOrganizationType(organization.organizationType) }}</td>
            <td class="p-4">{{ formatWorkingHours(organization.hoursFrom, organization.hoursTo,
                organization.minutesFrom, organization.minutesTo) }}</td>
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
      <NewOrganizationForm v-if="showForm" @close="closeForm" :title="formTitle" v-bind:organization="isEdit ? {...formOrganization} : undefined" :is-edit="isEdit"/>
    </main>
    <Footer />
  </div>
</template>

<script setup>
import {onBeforeUnmount, onMounted, ref} from 'vue'
import NewOrganizationForm from '@/components/NewOrganizationForm.vue'
import Footer from "@/components/shared/Footer.vue";
import Header from "@/components/shared/Header.vue";
import {
  abstractFetching,
  convertOrganizationType,
  formatWorkingHours,
  formatWorkingHoursArray,
  HeaderGroups
} from "@/js/utils.js";
import axios from "axios";
import {showAlert} from "@/js/custom-alert.js";
import debounce from "lodash.debounce";
import {get_token} from "@/js/csrf-token.js";

const showForm = ref(false)
const formTitle = ref('Создание организации')
const formOrganization = ref({})
const isEdit = ref(false)

const searchQuery = ref('')
const currentPage = ref(0)
const totalPages = ref(1)

const organizations = ref([])

const editOrganization = (organization) => {
  formTitle.value = 'Редактирование организации'
  formOrganization.value = organization
  formOrganization.value.workingHours = formatWorkingHoursArray(organization.hoursFrom, organization.hoursTo,
      organization.minutesFrom, organization.minutesTo)
  isEdit.value = true
  showForm.value = true
}

const deleteOrganization = async (id) => {
  try {
    const url = `/api/organizations?id=${id}`
    const response = await axios.delete(url)
    showAlert(response.data)
  } catch (error) {
    showAlert(error.response.data);
  }
  updateManagedEntities()
}

const prevPage = () => {
  if (currentPage.value >= 1) {
    currentPage.value--;
    updateManagedEntities()
  }
};

const nextPage = () => {
  if (currentPage.value < totalPages.value) {
    currentPage.value++;
    updateManagedEntities()
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
  updateManagedEntities()
}


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
    const url = `/api/organizations?type=&pattern=${encodeURIComponent(searchQuery.value)}&page=${currentPage.value}&size=8&sort=id`
    const response = await axios.get(url,{ signal: abortController.signal })
    totalPages.value = response.data.totalPages;
    organizations.value = response.data.content;
  } catch (error) {
    showAlert(error.response.data);
  }
}

const updateManagedEntities = () => {
  getManagedEntities(new AbortController())
}

const search = async () => {
  await abstractFetching(getManagedEntities)
}

const searchDebounced = debounce(search, 300);

</script>

<style scoped>

table td {
  border-right: none;
}
</style>