<template>
  <VueDatePicker v-model="time" time-picker disable-time-range-validation range placeholder="Select Time" />
</template>

<script setup>
import { ref } from 'vue';
import VueDatePicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css'

const time = ref();
</script>