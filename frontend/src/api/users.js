import api from './index'

/**
 * API module for user operations.
 */
export const usersApi = {
  /**
   * Get current authenticated user info.
   */
  getCurrentUser() {
    return api.get('/users/me')
  },

  /**
   * Get all users with search and pagination.
   * @param {Object} params - Query parameters (pattern, page, size, sort)
   */
  getAll(params = {}) {
    return api.get('/users', { params })
  },

  /**
   * Delete a user (soft delete).
   * @param {number} id - User ID
   */
  delete(id) {
    return api.delete(`/users/${id}`)
  }
}

export default usersApi
