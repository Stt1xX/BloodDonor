import { ref } from 'vue'
import { usersApi } from '@/api/users'

// Реактивное состояние пользователя
export const currentUser = ref(null)
export const isLoading = ref(false)

// Обратная совместимость - алиас для currentUser
export const user = currentUser

/**
 * Загружает информацию о текущем пользователе
 * @returns {Promise<Object|null>} Данные пользователя или null
 */
export async function fetchCurrentUser() {
  isLoading.value = true
  try {
    const { data } = await usersApi.getCurrentUser()
    currentUser.value = data
    return data
  } catch (error) {
    currentUser.value = null
    return null
  } finally {
    isLoading.value = false
  }
}

// Обратная совместимость - алиас для fetchCurrentUser
export const getUserInfo = fetchCurrentUser

/**
 * Проверяет, авторизован ли пользователь
 * @returns {boolean}
 */
export function isAuthenticated() {
  return currentUser.value !== null
}

/**
 * Проверяет роль пользователя
 * @param {string} role - Требуемая роль
 * @returns {boolean}
 */
export function hasRole(role) {
  return currentUser.value?.role === role
}

/**
 * Очищает данные пользователя (при logout)
 */
export function clearUser() {
  currentUser.value = null
}