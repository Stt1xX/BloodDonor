<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const requests = ref([])
const loading = ref(true)
const error = ref('')

// Фильтры
const filters = ref({
  status: '',
  bloodType: '',
  dateFrom: '',
  dateTo: ''
})

const loadRequests = async () => {
  try {
    loading.value = true
    const response = await axios.get('/api/blood-requests', {
      params: {
        status: filters.value.status,
        bloodType: filters.value.bloodType,
        dateFrom: filters.value.dateFrom,
        dateTo: filters.value.dateTo
      }
    })
    requests.value = response.data
  } catch (err) {
    error.value = 'Ошибка загрузки данных: ' + (err.response?.data?.message || 'Неизвестная ошибка')
  } finally {
    loading.value = false
  }
}

const applyFilters = () => {
  loadRequests()
}

onMounted(() => {
  loadRequests()
})
</script>

<template>
  <div class="blood-requests">
    <!-- Фильтры -->
    <div class="card mb-4">
      <div class="card-body">
        <h5 class="card-title">Фильтры</h5>
        <div class="row">
          <div class="col-md-3 mb-3">
            <label for="status" class="form-label">Статус</label>
            <select 
              class="form-select" 
              id="status"
              v-model="filters.status"
            >
              <option value="">Все</option>
              <option value="NEW">Новая</option>
              <option value="IN_PROGRESS">В обработке</option>
              <option value="COMPLETED">Выполнена</option>
              <option value="CANCELLED">Отменена</option>
            </select>
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
            <label for="dateFrom" class="form-label">Дата с</label>
            <input 
              type="date" 
              class="form-control" 
              id="dateFrom"
              v-model="filters.dateFrom"
            >
          </div>
          <div class="col-md-3 mb-3">
            <label for="dateTo" class="form-label">Дата по</label>
            <input 
              type="date" 
              class="form-control" 
              id="dateTo"
              v-model="filters.dateTo"
            >
          </div>
        </div>
        <div class="row">
          <div class="col-12">
            <button 
              class="btn btn-primary" 
              @click="applyFilters"
            >
              Применить фильтры
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Список заявок -->
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
                <th>Дата</th>
                <th>Тип крови</th>
                <th>Объем (мл)</th>
                <th>Статус</th>
                <th>Медучреждение</th>
                <th>Действия</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="request in requests" :key="request.id">
                <td>{{ new Date(request.date).toLocaleDateString() }}</td>
                <td>{{ request.bloodType }}</td>
                <td>{{ request.volume }}</td>
                <td>
                  <span 
                    class="badge"
                    :class="{
                      'bg-primary': request.status === 'NEW',
                      'bg-warning': request.status === 'IN_PROGRESS',
                      'bg-success': request.status === 'COMPLETED',
                      'bg-danger': request.status === 'CANCELLED'
                    }"
                  >
                    {{ request.status }}
                  </span>
                </td>
                <td>{{ request.medicalInstitution.name }}</td>
                <td>
                  <router-link 
                    :to="'/blood-requests/' + request.id" 
                    class="btn btn-sm btn-info me-2"
                  >
                    Просмотр
                  </router-link>
                  <button 
                    v-if="request.status === 'NEW'"
                    class="btn btn-sm btn-warning"
                    @click="$emit('process', request)"
                  >
                    Обработать
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
.blood-requests {
  margin-bottom: 2rem;
}

.card {
  box-shadow: 0 2px 4px rgba(0,0,0,.1);
}

.badge {
  font-size: 0.8rem;
}
</style> 