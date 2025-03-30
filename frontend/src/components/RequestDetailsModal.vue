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
              <p class="font-medium">rh{{ request.rhFactor }}</p>
            </div>
            <div>
              <p class="text-sm text-gray-500">Объем</p>
              <p class="font-medium">{{ request.volume }} л.</p>
            </div>
            <div>
              <p class="text-sm text-gray-500">Статус</p>
              <p>
                <span :class="statusClasses" class="px-3 py-1 rounded-full text-sm">
                  {{ request.requestStatus }}
                </span>
              </p>
            </div>
          </div>

          <div v-if="request.author" class="pt-2">
            <p class="text-sm text-gray-500">Автор подачи</p>
            <p class="font-medium">{{ request.author }}</p>
          </div>

          <div v-if="request.processedBy && request.requestStatus !== 'В ожидании'" class="pt-2">
            <p class="text-sm text-gray-500">Обработано</p>
            <p class="font-medium">{{ request.processedBy }}</p>
            <p class="text-sm text-gray-500">{{ formatDate(request.processedAt) }}</p>
          </div>

          <div v-if="request.rejectionReason && request.requestStatus === 'Отклонено'" class="pt-2">
            <p class="text-sm text-gray-500">Причина отклонения</p>
            <p class="font-medium text-red-600">{{ request.rejectionReason }}</p>
          </div>

          <div class="pt-2">
            <p class="text-sm text-gray-500">Описание</p>
            <p class="font-medium whitespace-pre-line">{{ request.details }}</p>
          </div>

          <div class="pt-4 border-t">
            <p class="text-sm text-gray-500">Дата создания</p>
            <p class="font-medium">{{ formatDate(request.createdAt) }}</p>
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
  switch(props.request.requestStatus) {
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