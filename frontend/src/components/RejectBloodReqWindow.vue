<template>
  <div class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
    <div class="bg-white p-8 rounded-lg shadow-lg w-1/2 relative">
      <h2 class="text-3xl font-bold text-red-800 mb-4">Отклонение запроса</h2>

      <form class="grid grid-cols-1 gap-6">

        <div class="col-span-1">
          <div class="flex justify-between">
            <div class="flex flex-col">
              <div class="flex items-center">
                <label class="block text-sm font-medium text-gray-700">
                  Группа крови
                </label>
              </div>
              <div class="flex space-x-2 mt-2">
                <button v-for="group in [1, 2, 3, 4]" :key="group"
                        type="button"
                        :disabled="true"
                        :class="{
                          'bg-red-500 text-white':  parseInt(requestData.bloodGroup) === group,
                          'bg-white text-gray-700': parseInt(requestData.bloodGroup) !== group,
                          'transition-colors': true
                        }"
                        class="px-4 py-2 border rounded-md">
                  {{ group }}
                </button>
              </div>
            </div>
            <div class="flex flex-col">
              <div class="flex items-center">
                <label class="block text-sm font-medium text-gray-700">
                  Резус-фактор
                </label>
              </div>
              <div class="flex space-x-2 mt-2 justify-end">
                <button v-for="rhesus in ['+', '-']" :key="rhesus"
                        type="button"
                        :disabled="true"
                        :class="{
                          'bg-red-500 text-white': requestData.rhesusFactor === rhesus,
                          'bg-white text-gray-700': requestData.rhesusFactor !== rhesus,
                          'transition-colors': true
                        }"
                        class="px-4 py-2 border rounded-md">
                  {{ rhesus }}
                </button>
              </div>
            </div>

          </div>
        </div>

        <div class="col-span-1">
          <label class="block text-sm font-medium text-gray-700">
            Дата создания
          </label>
          <input type="text" id="volume" v-model="requestData.createdAt" disabled
                 class="mt-2 w-full border-gray-300 rounded-md shadow-sm"
                 placeholder="Введите дату создания"/>
        </div>

        <div class="col-span-1">
          <label for="volume" class="block text-sm font-medium text-gray-700">
            Объём (л.)
          </label>
          <input type="text" id="volume" v-model="requestData.volumeNeeded" disabled
                 class="mt-2 w-full border-gray-300 rounded-md shadow-sm focus:ring-red-500 focus:border-red-500"
                 placeholder="Введите объём"/>
        </div>
        <div class="mb-4">
          <label for="rejectionReason" class="text-sm font-medium text-gray-700 flex-shrink-0 mr-2">Причина отказа:</label>
          <span v-if="errorMessage" class="text-red-600 text-sm">{{ errorMessage }}</span>
        <textarea id="rejectionReason" v-model="rejectionReason"
                  class="block w-full rounded-lg border-gray-300 shadow-sm focus:ring-red-500 focus:border-red-500 h-24 p-2"
                  placeholder="Введите причину отказа..."></textarea>
        </div>

        <div class="flex justify-end mt-4">
          <button @click="emit('close')" type="button" class="bg-gray-500 text-white px-4 py-2 rounded mr-2 hover:bg-gray-600 transition-colors">
            Отмена
          </button>
          <button @click="confirmRejection" type="button" class="bg-red-600 text-white px-4 py-2 rounded hover:bg-red-700 transition-colors">
            Подтвердить
          </button>
        </div>

      </form>
    </div>
  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue';
const emit = defineEmits(['close', 'confirm']);

const props = defineProps({
  requestData: {
    type: Object,
    required: true,
  },
});

const rejectionReason = ref('');
const errorMessage = ref('');

const confirmRejection = () => {
  if (rejectionReason.value.trim() === '') {
    errorMessage.value = 'Введите причину отказа';
    return;
  }
  errorMessage.value = '';
  emit('confirm', {
    id: props.requestData.id,
    reason: rejectionReason.value
  });
};
</script>
