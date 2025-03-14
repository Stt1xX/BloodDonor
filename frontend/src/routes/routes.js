import { createRouter, createWebHistory } from 'vue-router'

import LoginView from '@/views/shared/LoginView.vue'
import RegistrationView from '@/views/shared/RegistrationView.vue'
import AdminMainView from '@/views/AdminMainView.vue'
import BloodBankMain from '@/views/BloodBankMain.vue'
import MedicalInstitutionMain from '@/views/MedicalInstitutionMain.vue'

const routes = [
    {
      path: '/login',
      component: LoginView,
      meta: { requiresAuth: false }
    },
    {
      path: '/registration',
      component: RegistrationView,
      meta: { requiresAuth: false }
    },
    {
      path: '/admin/main',
      component: AdminMainView,
      meta: { requiresAuth: true }
    },
    {
      path: '/bank_employee/main',
      component: BloodBankMain,
      meta: { requiresAuth: true }
    },
    {
      path: '/medical_employee/main',
      component: MedicalInstitutionMain,
      meta: { requiresAuth: true }
    },
    {
        path: '/',
        redirect: '/login'
    },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router;