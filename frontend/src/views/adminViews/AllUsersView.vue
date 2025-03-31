<template>
  <div class="min-h-screen flex flex-col">
    <Header :header-group="HeaderGroups.ADMIN"/>
    <main class="flex-grow container mx-auto py-32 px-20 flex flex-col items-center">
      <div class="w-full flex justify-between mb-4">
        <input
            type="text"
            v-model="searchQuery"
            @input="searchDebounced"
            @focus="search"
            placeholder="Поиск работников..."
            class="border p-2 rounded w-1/2"/>
      </div>
      <div v-if="managedEntities.length > 0" class="w-full rounded-lg border border-gray-500 overflow-hidden">
        <table class="w-full">
          <thead>
          <tr>
            <th class="p-4 text-left">Пользователь</th>
            <th class="p-4 text-left">Контакты</th>
            <th class="p-4 text-left">Организация</th>
            <th class="p-4 text-left">Действия</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="managedEntity in managedEntities" :key="managedEntity.id" class="border-b border-gray-500 last:border-b-0 first:border-t">
            <td class="p-4">
              <div>{{ managedEntity.name }} {{ managedEntity.surname }}</div>
              <div class="text-gray-500">{{ convertUserRole(managedEntity.role) }}</div>
            </td>
            <td class="p-4">
              <div>{{ managedEntity.email }}</div>
              <div class="text-gray-500">{{ managedEntity.post }}</div>
            </td>
            <td class="p-4 space-x-2">
              {{ managedEntity.organizationName || 'Без организации' }}
            </td>
            <td class="p-4 space-x-2">
              <button @click="remove(managedEntity.id)" class="text-red-600 hover:text-red-800">
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
    </main>
    <Footer />
  </div>
</template>

<script setup>
import {onBeforeUnmount, onMounted, ref} from 'vue'
import Footer from "@/components/shared/Footer.vue";
import Header from "@/components/shared/Header.vue";
import {abstractFetching, convertUserRole, formatTimestamp, HeaderGroups} from "@/js/utils.js";
import debounce from "lodash.debounce";
import axios from "axios";
import {showAlert} from "@/js/custom-alert.js";
import {get_token} from "@/js/csrf-token.js";


const searchQuery = ref('')
const currentPage = ref(0)
const totalPages = ref(0)

const managedEntities = ref([])

const prevPage = () => {
  if (currentPage.value > 1) {
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

let polling;
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
    const url = `/api/user?pattern=${searchQuery.value}&page=${currentPage.value}&size=8&sort=id`
    const response = await axios.get(url,{ signal: abortController.signal })
    totalPages.value = response.data.totalPages;
    managedEntities.value = response.data.content;
  } catch (error) {
    showAlert(error.response.data);
  }
}

const updateManagedEntities = () => {
  getManagedEntities(new AbortController())
}

const remove = async (id) => {
  const url = `/api/user?id=${id}`
  const response = await axios.delete(url)
  showAlert(response.data)
  updateManagedEntities()
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