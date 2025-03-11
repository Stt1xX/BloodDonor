<template>
  <div class="container">
    <h1>Управление донорами крови</h1>
    <div class="add-donor">
      <input v-model="newDonor.fullName" placeholder="ФИО донора">
      <select v-model="newDonor.bloodType">
        <option value="">Выберите группу крови</option>
        <option value="A+">A+</option>
        <option value="A-">A-</option>
        <option value="B+">B+</option>
        <option value="B-">B-</option>
        <option value="AB+">AB+</option>
        <option value="AB-">AB-</option>
        <option value="O+">O+</option>
        <option value="O-">O-</option>
      </select>
      <input v-model="newDonor.phoneNumber" placeholder="Номер телефона">
      <button @click="addDonor" :disabled="!isValidDonor">Добавить донора</button>
    </div>

    <div class="filters">
      <select v-model="selectedBloodType" @change="filterDonors">
        <option value="">Все группы крови</option>
        <option value="A+">A+</option>
        <option value="A-">A-</option>
        <option value="B+">B+</option>
        <option value="B-">B-</option>
        <option value="AB+">AB+</option>
        <option value="AB-">AB-</option>
        <option value="O+">O+</option>
        <option value="O-">O-</option>
      </select>
      <label>
        <input type="checkbox" v-model="showOnlyAvailable" @change="filterDonors">
        Только доступные доноры
      </label>
    </div>

    <ul class="donor-list">
      <li v-for="donor in filteredDonors" :key="donor.id" :class="{ unavailable: !donor.isAvailable }">
        <div class="donor-info">
          <h3>{{ donor.fullName }}</h3>
          <p>Группа крови: {{ donor.bloodType }}</p>
          <p>Телефон: {{ donor.phoneNumber }}</p>
          <p>Последняя донация: {{ donor.lastDonationDate || 'Нет данных' }}</p>
        </div>
        <div class="donor-actions">
          <label>
            <input type="checkbox" v-model="donor.isAvailable" @change="updateDonor(donor)">
            Доступен
          </label>
          <button @click="deleteDonor(donor.id)" class="delete">Удалить</button>
        </div>
      </li>
    </ul>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'App',
  data() {
    return {
      donors: [],
      newDonor: {
        fullName: '',
        bloodType: '',
        phoneNumber: '',
        isAvailable: true
      },
      selectedBloodType: '',
      showOnlyAvailable: false
    }
  },
  computed: {
    isValidDonor() {
      return this.newDonor.fullName.trim() && this.newDonor.bloodType && this.newDonor.phoneNumber.trim();
    },
    filteredDonors() {
      let filtered = [...this.donors];
      if (this.selectedBloodType) {
        filtered = filtered.filter(d => d.bloodType === this.selectedBloodType);
      }
      if (this.showOnlyAvailable) {
        filtered = filtered.filter(d => d.isAvailable);
      }
      return filtered;
    }
  },
  created() {
    this.fetchDonors();
  },
  methods: {
    async fetchDonors() {
      try {
        const response = await axios.get('/api/donors');
        this.donors = response.data;
      } catch (error) {
        console.error('Error fetching donors:', error);
      }
    },
    async addDonor() {
      if (!this.isValidDonor) return;
      try {
        const response = await axios.post('/api/donors', this.newDonor);
        this.donors.push(response.data);
        this.newDonor = {
          fullName: '',
          bloodType: '',
          phoneNumber: '',
          isAvailable: true
        };
      } catch (error) {
        console.error('Error adding donor:', error);
      }
    },
    async updateDonor(donor) {
      try {
        await axios.put(`/api/donors/${donor.id}`, donor);
      } catch (error) {
        console.error('Error updating donor:', error);
      }
    },
    async deleteDonor(id) {
      try {
        await axios.delete(`/api/donors/${id}`);
        this.donors = this.donors.filter(donor => donor.id !== id);
      } catch (error) {
        console.error('Error deleting donor:', error);
      }
    },
    filterDonors() {
      this.fetchDonors();
    }
  }
}
</script>

<style>
.container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

h1 {
  text-align: center;
  color: #2c3e50;
  margin-bottom: 30px;
}

.add-donor {
  display: grid;
  grid-template-columns: 2fr 1fr 1fr auto;
  gap: 10px;
  margin-bottom: 30px;
}

.add-donor input,
.add-donor select {
  padding: 10px;
  font-size: 16px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.filters {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
  align-items: center;
}

.filters select {
  padding: 8px;
  border-radius: 4px;
  border: 1px solid #ddd;
}

button {
  padding: 10px 20px;
  background-color: #42b983;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:disabled {
  background-color: #a8d5c2;
  cursor: not-allowed;
}

button:hover:not(:disabled) {
  background-color: #3aa876;
}

.donor-list {
  list-style: none;
  padding: 0;
}

.donor-list li {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  background-color: #f8f9fa;
  margin-bottom: 10px;
  border-radius: 4px;
  border-left: 4px solid #42b983;
}

.donor-list li.unavailable {
  border-left-color: #dc3545;
  opacity: 0.7;
}

.donor-info h3 {
  margin: 0 0 10px 0;
  color: #2c3e50;
}

.donor-info p {
  margin: 5px 0;
  color: #6c757d;
}

.donor-actions {
  display: flex;
  align-items: center;
  gap: 15px;
}

.delete {
  background-color: #dc3545;
}

.delete:hover {
  background-color: #c82333;
}

label {
  display: flex;
  align-items: center;
  gap: 5px;
  cursor: pointer;
}

input[type="checkbox"] {
  width: 16px;
  height: 16px;
}
</style> 