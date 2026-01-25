import api from './index'

/**
 * API module for authentication operations.
 */
export const authApi = {
  /**
   * Login with credentials.
   * @param {Object} credentials - { username, password }
   */
  login(credentials) {
    const formData = new FormData()
    formData.append('username', credentials.username)
    formData.append('password', credentials.password)
    
    return api.post('/login', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },

  /**
   * Logout the current user.
   */
  logout() {
    return api.post('/logout')
  },

  /**
   * Get CSRF token from server.
   */
  getCsrfToken() {
    return api.get('/csrf')
  }
}

export default authApi
