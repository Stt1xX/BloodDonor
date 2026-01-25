import { createRouter, createWebHistory } from 'vue-router'
import { getUserInfo } from '@/js/user-info.js'

// LoginView is eagerly loaded as it's the entry point
import LoginView from '@/views/shared/LoginView.vue'

// Lazy-loaded views - these will be code-split into separate chunks
const RegistrationView = () => import('@/views/shared/RegistrationView.vue')
const NotFoundView = () => import('@/views/shared/NotFoundView.vue')

// Admin views
const OrganizationSettingsView = () => import('@/views/adminViews/OrganizationSettingsView.vue')
const EmployeeRequestsView = () => import('@/views/adminViews/EmployeeRequestsView.vue')
const AllUsersView = () => import('@/views/adminViews/AllUsersView.vue')

// Bank employee views
const BankReserveView = () => import('@/views/bankViews/BankReserveView.vue')
const BankRequestsView = () => import('@/views/bankViews/BankRequestsView.vue')

// Medical employee views
const MedicalRequestsView = () => import('@/views/medicalViews/MedicalRequestsView.vue')

const routes = [
    {
        path: '/login',
        name: 'Login',
        component: LoginView,
        meta: { requiresAuth: false }
    },
    {
        path: '/registration',
        name: 'Registration',
        component: RegistrationView,
        meta: { requiresAuth: false }
    },
    {
        path: '/admin/organization_settings',
        name: 'OrganizationSettings',
        component: OrganizationSettingsView,
        meta: { requiresAuth: true, role: 'ADMIN' }
    },
    {
        path: '/admin/requests',
        name: 'EmployeeRequests',
        component: EmployeeRequestsView,
        meta: { requiresAuth: true, role: 'ADMIN' }
    },
    {
        path: '/admin/all_users',
        name: 'AllUsers',
        component: AllUsersView,
        meta: { requiresAuth: true, role: 'ADMIN' }
    },
    {
        path: '/bank_employee/reserves',
        name: 'BankReserves',
        component: BankReserveView,
        meta: { requiresAuth: true, role: 'BANK_EMPLOYEE' }
    },
    {
        path: '/bank_employee/requests',
        name: 'BankRequests',
        component: BankRequestsView,
        meta: { requiresAuth: true, role: 'BANK_EMPLOYEE' }
    },
    {
        path: '/medical_employee/requests',
        name: 'MedicalRequests',
        component: MedicalRequestsView,
        meta: { requiresAuth: true, role: 'MEDICAL_EMPLOYEE' }
    },
    {
        path: '/',
        redirect: '/login'
    },
    {
        path: '/:pathMatch(.*)*',
        name: 'NotFound',
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
