import api from './index'

/**
 * API module for notifications operations.
 */
export const notificationsApi = {
  /**
   * Get notifications with pagination.
   * @param {Object} params - Query parameters (page, size, sort)
   */
  getAll(params = {}) {
    return api.get('/notifications', { params })
  },

  /**
   * Mark a single notification as read.
   * @param {number} id - Notification ID
   */
  markAsRead(id) {
    return api.patch(`/notifications/${id}/read`)
  },

  /**
   * Mark multiple notifications as read.
   * @param {number[]} ids - Array of notification IDs
   */
  markAllAsRead(ids) {
    return api.patch('/notifications/read-all', ids)
  }
}

export default notificationsApi
