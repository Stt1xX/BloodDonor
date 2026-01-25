import axios from 'axios'
import router from '@/routes/routes'
import { showAlert } from '@/js/custom-alert'

/**
 * Centralized API client with interceptors for CSRF token and error handling.
 */
const api = axios.create({
  baseURL: '/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// Request interceptor - add CSRF token to all requests
api.interceptors.request.use(
  (config) => {
    const csrfMeta = document.querySelector('meta[name="csrf-token"]')
    if (csrfMeta?.content) {
      config.headers['X-CSRF-TOKEN'] = csrfMeta.content
    }
    return config
  },
  (error) => Promise.reject(error)
)

// Response interceptor - unified error handling
api.interceptors.response.use(
  (response) => response,
  (error) => {
    // Handle cancelled requests silently
    if (axios.isCancel(error)) {
      return Promise.reject(error)
    }

    const status = error.response?.status
    const data = error.response?.data
    const message = data?.message || 'Произошла ошибка'

    switch (status) {
      case 400:
        // Validation errors
        if (data?.fieldErrors) {
          const fieldMessages = data.fieldErrors.map(e => `${e.field}: ${e.message}`).join('\n')
          showAlert(fieldMessages)
        } else {
          showAlert(message)
        }
        break
      case 401:
        // Not authenticated - redirect to login
        router.push('/login')
        break
      case 403:
        showAlert('Нет доступа к данному ресурсу')
        break
      case 404:
        showAlert('Ресурс не найден')
        break
      case 409:
        showAlert(message)
        break
      case 422:
        showAlert(message)
        break
      case 500:
        showAlert('Ошибка сервера. Попробуйте позже')
        break
      default:
        if (message) {
          showAlert(message)
        }
    }

    return Promise.reject(error)
  }
)

export default api
