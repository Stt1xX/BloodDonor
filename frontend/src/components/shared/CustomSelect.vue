<template>
  <div class="relative">
    <label :for="id" class="block text-sm font-medium text-gray-700">{{ label }}</label>
    <div class="relative mt-1">
      <select
          :id="id"
          v-model="selectedValue"
          class="block w-full rounded-lg border-gray-300 shadow-sm focus:ring-red-500 focus:border-red-500 hover:border-red-300 transition-all duration-200 appearance-none"
          @change="updateValue"
      >
        <option value="" disabled>{{ placeholder }}</option>
        <option v-for="option in options" :key="option.value" :value="option.value">
          {{ option.text }}
        </option>
      </select>
      <span class="absolute right-3 top-1/2 transform -translate-y-1/2 text-gray-400 pointer-events-none">
        <i class="fas fa-chevron-down"></i>
      </span>
    </div>
  </div>
</template>

<script setup>
import { ref, defineProps, defineEmits } from 'vue'

const props = defineProps({
  id: {
    type: String,
    required: true
  },
  label: {
    type: String,
    required: true
  },
  options: {
    type: Array,
    required: true
  },
  placeholder: {
    type: String,
    default: 'Select an option'
  },
  modelValue: {
    type: String,
    default: ''
  }
})

const emits = defineEmits(['update:modelValue'])

const selectedValue = ref(props.modelValue)

const updateValue = () => {
  emits('update:modelValue', selectedValue.value)
}
</script>

<style scoped>
</style>