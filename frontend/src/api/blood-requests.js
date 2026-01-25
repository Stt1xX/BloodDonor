import api from './index'

/**
 * API module for blood requests operations.
 */
export const bloodRequestsApi = {
  /**
   * Get blood requests for bank employee view.
   * @param {Object} params - Query parameters (bloodGroup, rhesusFactor, reverse, page, size, sort)
   */
  getForBank(params = {}) {
    return api.get('/blood-requests/bank', { params })
  },

  /**
   * Get blood requests for medical institution view.
   * @param {Object} params - Query parameters (status, page, size, sort)
   */
  getForMedical(params = {}) {
    return api.get('/blood-requests/medical', { params })
  },

  /**
   * Create a new blood request.
   * @param {Object} data - Blood request data
   */
  create(data) {
    return api.post('/blood-requests', data)
  },

  /**
   * Accept a blood request (bank employee action).
   * @param {number} id - Request ID
   */
  accept(id) {
    return api.patch(`/blood-requests/${id}/accept`)
  },

  /**
   * Reject a blood request with reason.
   * @param {number} id - Request ID
   * @param {string} reason - Rejection reason
   */
  reject(id, reason) {
    return api.patch(`/blood-requests/${id}/reject`, { reason })
  }
}

export default bloodRequestsApi
