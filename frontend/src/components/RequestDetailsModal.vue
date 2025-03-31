<template>
  <div class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
    <div class="bg-white rounded-lg shadow-xl w-full max-w-2xl">
      <div class="p-6">
        <div class="flex justify-between items-start">
          <h3 class="text-2xl font-bold text-gray-800">Детали заявки #{{ request.id }}</h3>
          <button @click="$emit('close')" class="text-gray-500 hover:text-gray-700">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>

        <div class="mt-6 space-y-4">
          <div class="grid grid-cols-2 gap-4">
            <div>
              <p class="text-sm text-gray-500">Группа крови</p>
              <p class="font-medium">гр. {{ request.bloodGroup }}</p>
            </div>
            <div>
              <p class="text-sm text-gray-500">Резус-фактор</p>
              <p class="font-medium">rh{{ request.rhesusFactor }}</p>
            </div>
            <div>
              <p class="text-sm text-gray-500">Объем</p>
              <p class="font-medium">{{ request.volumeNeeded }} л.</p>
            </div>
            <div>
              <p class="text-sm text-gray-500">Статус</p>
              <p>
                <span :class="statusClasses" class="px-3 py-1 rounded-full text-sm">
                  {{ request.status }}
                </span>
              </p>
            </div>
            <div class="pt-4">
              <p class="text-sm text-gray-500">Дата создания</p>
              <p class="font-medium">{{ formatDate(request.createdAt) }}</p>
            </div>
            <div v-if="request.status !== 'В ожидании'" class="pt-4">
              <p class="text-sm text-gray-500">Дата обработки</p>
              <p class="font-medium">{{ formatDate(request.updatedAt) }}</p>
            </div>
            <div v-if="request.creator" class="pt-2">
              <p class="text-sm text-gray-500">Отправитель</p>
              <p class="font-medium">{{ request.creator.name }} {{request.creator.surname}}</p>
              <p class="text-sm flex items-center">
                <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"/>
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"/>
                </svg>
                {{ request.creator.organizationName }}
              </p>
            </div>
            <div v-if="request.status !== 'В ожидании'">
              <div class="pt-2">
                <p class="text-sm text-gray-500">Обработчик</p>
                <p class="font-medium">{{ request.banker.name }} {{request.banker.surname}}</p>
                <p class="text-sm flex items-center">
                  <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"/>
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"/>
                  </svg>
                  {{ request.banker.organizationName }}
                </p>
              </div>
            </div>
            <div class="pt-4">
              <p class="text-sm text-gray-500">Приоритет</p>
              <p class="font-medium">{{ request.isEmergency ? 'Срочно!' : 'Не срочно' }}</p>
            </div>
          </div>
          <div class="pt-2">
            <p class="text-sm text-gray-500">Описание</p>
            <p class="font-medium whitespace-pre-line">{{ request.description }}</p>
          </div>
          <div v-if="request.status === 'Отклонено'">
            <div class="pt-2">
              <p class="text-sm text-gray-500">Причина отклонения</p>
              <p class="font-medium text-red-600">{{ request.rejectionReason }}</p>
            </div>
          </div>
        </div>

        <div class="mt-6 flex justify-end">
          <button
              @click="$emit('close')"
              class="bg-red-600 text-white px-4 py-2 rounded-md hover:bg-red-700 transition-colors"
          >
            Закрыть
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue';

const props = defineProps({
  request: {
    type: Object,
    required: true
  }
});

const emit = defineEmits(['close']);

const statusClasses = computed(() => {
  switch(props.request.status) {
    case 'В ожидании': return 'bg-yellow-100 text-yellow-800';
    case 'Принято': return 'bg-green-100 text-green-800';
    case 'Отклонено': return 'bg-red-100 text-red-800';
    default: return 'bg-gray-100 text-gray-800';
  }
});

const formatDate = (dateString) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  return date.toLocaleDateString() + ' ' + date.toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'});
};
</script>