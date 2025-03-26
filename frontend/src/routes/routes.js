import { createRouter, createWebHistory } from 'vue-router'

import LoginView from '@/views/shared/LoginView.vue'
import RegistrationView from '@/views/shared/RegistrationView.vue'
import OrganizationSettingsView from '@/views/adminViews/OrganizationSettingsView.vue'
import BloodBankMain from '@/views/BloodBankMain.vue'
import MedicalInstitutionMain from '@/views/MedicalInstitutionMain.vue'
import EmployeeRequestsView from "@/views/adminViews/EmployeeRequestsView.vue";
import {getUserInfo} from "@/js/user-info.js";

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
        path: '/admin/requests',
        component: EmployeeRequestsView,
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


router.beforeEach(async (to, from, next) => {
    if (to.path !== "/login" && to.path !== "/registration") {
        await getUserInfo()
    }
    next();
});

export default router;