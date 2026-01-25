import api from './index'

/**
 * API module for blood units operations.
 */
export const bloodUnitsApi = {
  /**
   * Get blood units with filtering and pagination.
   * @param {Object} params - Query parameters (bloodGroup, rhesusFactor, reverse, page, size, sort)
   */
  getAll(params = {}) {
    return api.get('/blood-units', { params })
  },

  /**
   * Create a new blood unit.
   * @param {Object} data - Blood unit data
   */
  create(data) {
    return api.post('/blood-units', data)
  },

  /**
   * Update an existing blood unit.
   * @param {number} id - Blood unit ID
   * @param {Object} data - Updated blood unit data
   */
  update(id, data) {
    return api.put(`/blood-units/${id}`, data)
  },

  /**
   * Delete a blood unit.
   * @param {number} id - Blood unit ID
   */
  delete(id) {
    return api.delete(`/blood-units/${id}`)
  }
}

export default bloodUnitsApi
