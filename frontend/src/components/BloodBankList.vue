<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const bloodBanks = ref([])
const loading = ref(true)
const error = ref('')

// Фильтры
const filters = ref({
  name: '',
  city: '',
  bloodType: ''
})

const loadBloodBanks = async () => {
  try {
    loading.value = true
    const response = await axios.get('/api/blood-banks', {
      params: {
        name: filters.value.name,
        city: filters.value.city,
        bloodType: filters.value.bloodType
      }
    })
    bloodBanks.value = response.data
  } catch (err) {
    error.value = 'Ошибка загрузки данных: ' + (err.response?.data?.message || 'Неизвестная ошибка')
  } finally {
    loading.value = false
  }
}

const applyFilters = () => {
  loadBloodBanks()
}

onMounted(() => {
  loadBloodBanks()
})
</script>

<template>
  <div class="blood-banks">
    <!-- Фильтры -->
    <div class="card mb-4">
      <div class="card-body">
        <h5 class="card-title">Фильтры</h5>
        <div class="row">
          <div class="col-md-3 mb-3">
            <label for="searchName" class="form-label">Название</label>
            <input 
              type="text" 
              class="form-control" 
              id="searchName"
              v-model="filters.name"
            >
          </div>
          <div class="col-md-3 mb-3">
            <label for="searchCity" class="form-label">Город</label>
            <input 
              type="text" 
              class="form-control" 
              id="searchCity"
              v-model="filters.city"
            >
          </div>
          <div class="col-md-3 mb-3">
            <label for="bloodType" class="form-label">Тип крови</label>
            <select 
              class="form-select" 
              id="bloodType"
              v-model="filters.bloodType"
            >
              <option value="">Все</option>
              <option value="A_POSITIVE">A+</option>
              <option value="A_NEGATIVE">A-</option>
              <option value="B_POSITIVE">B+</option>
              <option value="B_NEGATIVE">B-</option>
              <option value="AB_POSITIVE">AB+</option>
              <option value="AB_NEGATIVE">AB-</option>
              <option value="O_POSITIVE">O+</option>
              <option value="O_NEGATIVE">O-</option>
            </select>
          </div>
          <div class="col-md-3 mb-3">
            <label class="form-label">&nbsp;</label>
            <button 
              class="btn btn-primary w-100" 
              @click="applyFilters"
            >
              Применить фильтры
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Список банков крови -->
    <div class="card">
      <div class="card-body">
        <div v-if="error" class="alert alert-danger">
          {{ error }}
        </div>
        <div v-if="loading" class="text-center">
          <div class="spinner-border" role="status">
            <span class="visually-hidden">Загрузка...</span>
          </div>
        </div>
        <div v-else class="table-responsive">
          <table class="table table-hover">
            <thead>
              <tr>
                <th>Название</th>
                <th>Адрес</th>
                <th>Город</th>
                <th>Доступные типы крови</th>
                <th>Действия</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="bank in bloodBanks" :key="bank.id">
                <td>{{ bank.name }}</td>
                <td>{{ bank.address }}</td>
                <td>{{ bank.city }}</td>
                <td>
                  <span 
                    v-for="type in bank.availableBloodTypes" 
                    :key="type"
                    class="badge bg-primary me-1"
                  >
                    {{ type }}
                  </span>
                </td>
                <td>
                  <router-link 
                    :to="'/blood-banks/' + bank.id" 
                    class="btn btn-sm btn-info me-2"
                  >
                    Просмотр
                  </router-link>
                  <button 
                    class="btn btn-sm btn-warning"
                    @click="$emit('edit', bank)"
                  >
                    Редактировать
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.blood-banks {
  margin-bottom: 2rem;
}

.card {
  box-shadow: 0 2px 4px rgba(0,0,0,.1);
}

.badge {
  font-size: 0.8rem;
}
</style> 