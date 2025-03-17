import { createRouter, createWebHistory } from 'vue-router'

import LoginView from '@/views/shared/LoginView.vue'
import RegistrationView from '@/views/shared/RegistrationView.vue'
import OrganizationSettingsView from '@/views/adminViews/OrganizationSettingsView.vue'
import BloodBankMain from '@/views/BloodBankMain.vue'
import MedicalInstitutionMain from '@/views/MedicalInstitutionMain.vue'
import AdminRequestsView from "@/views/adminViews/AdminRequestsView.vue";
import BankEmpRequestsView from "@/views/adminViews/BankEmpRequestsView.vue";
import MedicalEmpRequetsView from "@/views/adminViews/MedicalEmpRequetsView.vue";

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
      path: '/admin/organization_settings',
      component: OrganizationSettingsView,
      meta: { requiresAuth: true }
    },
    {
        path: '/admin/admin_requests',
        component: AdminRequestsView,
        meta: { requiresAuth: true }
    },
    {
        path: '/admin/bank_requests',
        component: BankEmpRequestsView,
        meta: { requiresAuth: true }
    },
    {
        path: '/admin/medical_requests',
        component: MedicalEmpRequetsView,
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