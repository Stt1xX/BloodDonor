<template>
  <div class="bg-red-600 text-white py-4 px-10 shadow-md">
    <div class="container mx-auto flex justify-between items-center">
      <div class="flex items-center">
        <img src="@/assets/logo.png" alt="Logo" class="w-10 h-10 mr-3" />
        <h1 class="text-2xl font-bold">Blood Link</h1>
      </div>

      <div v-if="user.name && user.surname" class="flex flex-col items-end text-right ml-6">
        <span class="text-lg font-semibold">Добро пожаловать, {{ user.name }} {{ user.surname }}!</span>
        <span class="text-sm text-gray-200">{{ convertUserRole(user.role) }}</span>
      </div>

      <div class="flex items-center space-x-4">
        <!-- Кнопка уведомлений -->
        <div class="relative">
          <button
              @click="toggleNotifications"
              class="p-2 rounded-full hover:bg-red-700 transition-colors relative"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9" />
            </svg>
            <span
                v-if="unreadCount > 0"
                class="absolute top-0 right-0 bg-white text-red-600 rounded-full w-5 h-5 flex items-center justify-center text-xs font-bold"
            >
              {{ unreadCount }}
            </span>
          </button>

          <!-- Выпадающее меню уведомлений -->
          <div
              v-if="showNotifications"
              class="absolute right-0 mt-2 w-72 bg-white rounded-md shadow-lg overflow-hidden z-50"
          >
            <div class="py-1">
              <div class="px-4 py-2 bg-gray-100 border-b">
                <p class="text-sm font-medium text-gray-700">Уведомления</p>
              </div>

              <div v-if="notifications.length > 0">
                <div
                    v-for="notification in notifications"
                    :key="notification.id"
                    @click="markAsRead(notification)"
                    class="px-4 py-3 hover:bg-gray-50 cursor-pointer border-b"
                    :class="{ 'bg-gray-50': !notification.read }"
                >
                  <p class="text-sm font-medium text-gray-800">{{ notification.title }}</p>
                  <p class="text-xs text-gray-500 mt-1">{{ notification.message }}</p>
                  <p class="text-xs text-gray-400 mt-1">{{ formatDate(notification.date) }}</p>
                </div>
              </div>
              <div v-else class="px-4 py-3 text-center text-sm text-gray-500">
                Нет новых уведомлений
              </div>

              <div class="px-4 py-2 bg-gray-100 border-t">
                <button
                    @click="markAllAsRead"
                    class="text-xs text-red-600 hover:text-red-800"
                    :disabled="unreadCount === 0"
                >
                  Пометить все как прочитанные
                </button>
              </div>
            </div>
          </div>
        </div>

        <!-- Основные кнопки навигации -->
        <nav class="flex space-x-4">
          <button
              v-for="button in headerButtons[headerGroup]"
              :key="button.text"
              @click="button.action"
              :class="['px-4 py-2 rounded transition-colors duration-300 border border-transparent hover:bg-red-600 hover:text-white hover:border-white',
               isActive(button.route) ? 'bg-[#e3342f] text-white border-white' : 'bg-white text-red-600']"
          >
            {{ button.text }}
          </button>
        </nav>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, ref, onMounted } from 'vue'
import { user } from "@/js/user-info.js"
import axios from "axios";
import router from "@/routes/routes.js";
import { showAlert } from "@/js/custom-alert.js";
import {convertUserRole, HeaderGroups} from "@/js/utils.js";
import { useRoute } from 'vue-router';

const route = useRoute();
const showNotifications = ref(false);
const notifications = ref([]);
const unreadCount = ref(0);

// Загрузка уведомлений
const loadNotifications = async () => {
  try {
    const response = await axios.get('/api/notifications');
    notifications.value = response.data.notifications;
    unreadCount.value = response.data.unreadCount;
  } catch (error) {
    console.error('Ошибка загрузки уведомлений:', error);
  }
};

// Пометить как прочитанное
const markAsRead = async (notification) => {
  try {
    await axios.post(`/api/notifications/${notification.id}/read`);
    notification.read = true;
    unreadCount.value = Math.max(0, unreadCount.value - 1);
  } catch (error) {
    console.error('Ошибка пометки уведомления:', error);
  }
};

// Пометить все как прочитанные
const markAllAsRead = async () => {
  try {
    await axios.post('/api/notifications/mark-all-read');
    notifications.value.forEach(n => n.read = true);
    unreadCount.value = 0;
  } catch (error) {
    console.error('Ошибка пометки всех уведомлений:', error);
  }
};

// Форматирование даты
const formatDate = (dateString) => {
  const date = new Date(dateString);
  return date.toLocaleString('ru-RU');
};

// Переключение видимости уведомлений
const toggleNotifications = () => {
  showNotifications.value = !showNotifications.value;
  if (showNotifications.value) {
    loadNotifications();
  }
};

// Закрытие при клике вне области
const handleClickOutside = (event) => {
  const notificationArea = document.querySelector('.relative');
  if (notificationArea && !notificationArea.contains(event.target)) {
    showNotifications.value = false;
  }
};

onMounted(() => {
  // Загрузка уведомлений при монтировании
  loadNotifications();

  // Установка обработчика клика вне области
  document.addEventListener('click', handleClickOutside);

  // Очистка при демонтировании
  return () => {
    document.removeEventListener('click', handleClickOutside);
  };
});

// Остальной код без изменений
defineProps({
  headerGroup: {
    type: Number,
    required: true
  }
})

const logout = async () => {
  try {
    const response = await axios.post('/api/logout')
    if (response.status === 200) {
      showAlert(response.data)
      await router.push('/login')
    }
  } catch (error) {
    showAlert(error.response.data);
  }
}

const isActive = (routePath) => {
  return route.path === routePath;
}

const headerButtons = {
  [HeaderGroups.ADMIN] : [
    { text: 'Заявки', action: () => router.push('/admin/requests'), route: '/admin/requests'},
    { text: 'Организации', action: () => router.push('/admin/organization_settings'), route: '/admin/organization_settings' },
    { text: 'Работники', action: () => router.push('/admin/all_users'), route: '/admin/all_users' },
    { text: 'Выйти', action: () => logout() },
  ],
  [HeaderGroups.BANK_EMPLOYEE] : [
    { text: 'Запасы', action: () => router.push('/bank_employee/reserves'), route: '/bank_employee/reserves' },
    { text: 'Запросы', action: () => router.push('/bank_employee/requests'), route: '/bank_employee/requests'},
    { text: 'Выйти', action: () => logout() },
  ],
  [HeaderGroups.MEDICAL_EMPLOYEE] : [
    { text: 'Запросы', action: () => router.push('/medical_employee/requests'), route: '/medical_employee/requests'},
    { text: 'Выйти', action: () => logout() }
  ]
}
</script>

<style scoped>
header {
  background-color: #e3342f;
  color: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

nav button {
  transition: color 0.3s ease, background-color 0.3s ease, border-color 0.3s ease;
}

nav button:hover {
  color: white;
  background-color: #e3342f;
  border-color: white;
}

.notification-badge {
  position: absolute;
  top: -5px;
  right: -5px;
  background-color: white;
  color: #e3342f;
  border-radius: 50%;
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.75rem;
  font-weight: bold;
}
</style>