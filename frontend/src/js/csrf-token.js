/**
 * Получает CSRF токен из meta-тега страницы
 * @returns {string|null} CSRF токен или null
 */
export function getCsrfToken() {
  const metaTag = document.querySelector('meta[name="csrf-token"]')
  return metaTag ? metaTag.content : null
}

/**
 * Добавляет CSRF токен в заголовки запроса
 * @param {Object} headers - Объект заголовков
 * @returns {Object} Заголовки с CSRF токеном
 */
export function addCsrfHeader(headers = {}) {
  const token = getCsrfToken()
  if (token) {
    headers['X-CSRF-TOKEN'] = token
  }
  return headers
}