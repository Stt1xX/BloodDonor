<template>
  <div class="min-h-screen flex flex-col">
    <Header :header-group="HeaderGroup.ADMIN"/>
    <main class="flex-grow container mx-auto py-8 px-4 flex flex-col items-center">
      <div class="w-full flex justify-between mb-4">
        <input type="text" v-model="searchQuery" placeholder="Поиск организаций..." class="border p-2 rounded w-1/2"/>
        <button @click="showForm = true" class="bg-red-600 text-white px-4 py-2 rounded hover:bg-red-700 transition-colors">Создать организацию</button>
      </div>
      <table class="w-full border-collapse">
        <thead>
        <tr>
          <th class="border p-2">Имя организации</th>
          <th class="border p-2">Адрес</th>
          <th class="border p-2">Действия</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="organization in filteredOrganizations" :key="organization.id">
          <td class="border p-2">{{ organization.name }}</td>
          <td class="border p-2">{{ organization.address }}</td>
          <td class="border p-2 flex justify-end space-x-2">
            <button @click="editOrganization(organization)" class="text-blue-600 hover:text-blue-800">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
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
      <NewOrganizationForm v-if="showForm" @close="showForm = false"/>
    </main>
    <Footer />
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import NewOrganizationForm from '@/components/NewOrganizationForm.vue'
import Footer from "@/components/shared/Footer.vue";
import Header from "@/components/shared/Header.vue";
import { HeaderGroup } from "@/js/uitls.js";

const showForm = ref(false)
const searchQuery = ref('')
const organizations = ref([
  // Пример данных
  { id: 1, name: 'Organization 1', address: 'Address 1' },
  { id: 2, name: 'Organization 2', address: 'Address 2' },
  // Добавьте больше организаций здесь
])

const filteredOrganizations = computed(() => {
  return organizations.value.filter(org =>
      org.name.toLowerCase().includes(searchQuery.value.toLowerCase())
  )
})

const editOrganization = (organization) => {
  // Логика редактирования организации
}

const deleteOrganization = (id) => {
  // Логика удаления организации
}
</script>

<style scoped>
main {
  padding-top: 2rem;
  padding-left: 1rem;
  padding-right: 1rem;
}

table th, table td {
  text-align: left;
}
</style>