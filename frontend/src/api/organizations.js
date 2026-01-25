import api from './index'

/**
 * API module for organization operations.
 */
export const organizationsApi = {
  /**
   * Get all organizations.
   */
  getAll() {
    return api.get('/organizations')
  },

  /**
   * Get organization by ID.
   * @param {number} id - Organization ID
   */
  getById(id) {
    return api.get(`/organizations/${id}`)
  },

  /**
   * Create a new organization.
   * @param {Object} data - Organization data
   */
  create(data) {
    return api.post('/organizations', data)
  },

  /**
   * Update an organization.
   * @param {number} id - Organization ID
   * @param {Object} data - Updated organization data
   */
  update(id, data) {
    return api.patch(`/organizations/${id}`, data)
  },

  /**
   * Delete an organization.
   * @param {number} id - Organization ID
   */
  delete(id) {
    return api.delete(`/organizations/${id}`)
  },

  /**
   * Get all blood banks with their resources.
   */
  getBanksWithResources() {
    return api.get('/organizations/banks-resources')
  }
}

export default organizationsApi
