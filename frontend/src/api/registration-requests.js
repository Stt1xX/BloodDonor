import api from './index'

/**
 * API module for registration request operations.
 */
export const registrationRequestsApi = {
  /**
   * Get all registration requests with pagination.
   * @param {Object} params - Query parameters (page, size)
   */
  getAll(params = {}) {
    return api.get('/registration-requests', { params })
  },

  /**
   * Create a new registration request.
   * @param {Object} data - Registration data (name, surname, patronymic, email, password, organization)
   */
  create(data) {
    return api.post('/registration-requests', data)
  },

  /**
   * Approve a registration request.
   * @param {number} id - Request ID
   */
  approve(id) {
    return api.post(`/registration-requests/${id}/approve`)
  },

  /**
   * Reject a registration request.
   * @param {number} id - Request ID
   */
  reject(id) {
    return api.post(`/registration-requests/${id}/reject`)
  }
}

export default registrationRequestsApi
