<template>
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

    <div
        v-if="showNotifications"
        class="absolute right-0 mt-2 w-80 bg-white rounded-md shadow-lg overflow-hidden z-50 border border-gray-200"
    >
      <div class="py-1">
        <div class="px-4 py-2 bg-gray-100 border-b flex justify-between items-center">
          <p class="text-sm font-medium text-gray-700">Уведомления</p>
          <button
              @click.stop="markAllAsRead"
              class="text-xs text-red-600 hover:text-red-800 font-medium"
              :disabled="unreadCount === 0"
              :class="{ 'opacity-50 cursor-not-allowed': unreadCount === 0 }"
          >
            Очистить все
          </button>
        </div>

        <div v-if="notifications.length > 0" class="max-h-96 overflow-y-auto">
          <div
              v-for="notification in notifications"
              :key="notification.id"
              @click="markAsRead(notification)"
              class="px-4 py-3 cursor-pointer border-b border-gray-100"
              :class="{
                'bg-red-50 border-l-4 border-l-red-500': !notification.read,
                'hover:bg-gray-50': notification.read
              }"
          >
            <div class="flex items-start">
              <div class="flex-shrink-0 pt-0.5">
              </div>
              <div class="ml-3 flex-1">
                <p class="text-sm font-medium text-gray-800" :class="{ 'font-semibold': !notification.read }">
                  {{ notification.title }}
                </p>
                <p class="text-xs text-gray-600 mt-1">{{ notification.notification }}</p>
                <p class="text-xs text-gray-400 mt-1">{{ formatTimestamp(notification.createdAt) }}</p>
              </div>
            </div>
          </div>
        </div>
        <div v-else class="px-4 py-6 text-center text-sm text-gray-500">
          Нет новых уведомлений
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {ref, onMounted, onBeforeUnmount} from 'vue';
import axios from "axios";
import {formatTimestamp} from "@/js/utils.js";
import {showAlert} from "@/js/custom-alert.js";

const showNotifications = ref(false);
const notifications = ref([]);

const unreadCount = ref(0)

const loadNotifications = async () => {
  try {
    const response = await axios.get('/api/notifications?page=0&size=8&sort=id');
    notifications.value = response.data.content;
    unreadCount.value = response.data.totalElements;
  } catch (error) {
    showAlert(error.response.data);
  }
};

const markAsRead = async (notification) => {
  if (notification.read) return;

  try {
    await axios.post(`/api/notifications/read?id=${notification.id}`);
    notifications.value = notifications.value.filter(n => n.id !== notification.id)
  } catch (error) {
    showAlert(error.response.data);
  }
};

const markAllAsRead = async () => {
  if (unreadCount.value === 0) return;

  try {
    const unreadIds = notifications.value
        .filter(n => !n.read)
        .map(n => n.id);
    await axios.post('/api/notifications/read_all', unreadIds);
    notifications.value = []
  } catch (error) {
    showAlert(error.response.data);
  }
};

const toggleNotifications = () => {
  showNotifications.value = !showNotifications.value;
  if (showNotifications.value) {
    loadNotifications();
  }
};

const handleClickOutside = (event) => {
  const notificationArea = document.querySelector('.relative');
  if (notificationArea && !notificationArea.contains(event.target)) {
    showNotifications.value = false;
  }
};

let polling;
onMounted(() => {
  loadNotifications();
  polling = setInterval(loadNotifications, 7000);
  document.addEventListener('click', handleClickOutside);
});

onBeforeUnmount(() => {
  clearInterval(polling)
  document.removeEventListener('click', handleClickOutside);
});
</script>

<style scoped>

</style>