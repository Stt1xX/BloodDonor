import { createRouter, createWebHistory } from 'vue-router'

import LoginView from '@/views/shared/LoginView.vue'
import RegistrationView from '@/views/shared/RegistrationView.vue'
import OrganizationSettingsView from '@/views/adminViews/OrganizationSettingsView.vue'
import BankReserveView from '@/views/bankViews/BankReserveView.vue'
import MedicalRequestsView from '@/views/medicalViews/MedicalRequestsView.vue'
import EmployeeRequestsView from "@/views/adminViews/EmployeeRequestsView.vue";
import {getUserInfo} from "@/js/user-info.js";
import AllUsersView from "@/views/adminViews/AllUsersView.vue";
import NotFoundView from '@/views/shared/NotFoundView.vue'
import BankRequestsView from "@/views/bankViews/BankRequestsView.vue";
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
        path: '/admin/all_users',
        component: AllUsersView,
        meta: {requiresAuth: true }
    },
    {
        path: '/bank_employee/reserves',
        component: BankReserveView,
        meta: { requiresAuth: true }
    },
    {
        path: "/bank_employee/requests",
        component: BankRequestsView,
        meta: { requiresAuth: true }
    },
    {
        path: '/medical_employee/requests',
        component: MedicalRequestsView,
        meta: { requiresAuth: true }
    },
    {
        path: '/',
        redirect: '/login'
    },
    {
        path: '/:pathMatch(.*)*',
        component: NotFoundView
    }
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
