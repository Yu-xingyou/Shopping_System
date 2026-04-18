import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LoginView from '../views/LoginView.vue'
import RegisterView from '../views/RegisterView.vue'
import ForgotPasswordView from '../views/ForgotPasswordView.vue'
import ResetPasswordView from '../views/ResetPasswordView.vue'
import ProductsView from '../views/ProductsView.vue'
import UsersView from '../views/UsersView.vue'
import StaffsView from '../views/StaffsView.vue'
import OrdersView from '../views/OrdersView.vue'
import AdminDashboard from '../views/AdminDashboard.vue'
import UserOrderView from '../views/UserOrderView.vue'
import ProfileView from '../views/ProfileView.vue'
import FavoritesView from '../views/FavoritesView.vue'
import RevenueView from '../views/RevenueView.vue'
import HotProductsView from '../views/HotProductsView.vue'
import AdminProfileView from '../views/AdminProfileView.vue'
import StaffProfileView from '../views/StaffProfileView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView,
    },
    {
      path: '/register',
      name: 'register',
      component: RegisterView,
    },
    {
      path: '/forgot-password',
      name: 'forgot-password',
      component: ForgotPasswordView,
    },
    {
      path: '/reset-password',
      name: 'reset-password',
      component: ResetPasswordView,
    },
    {
      path: '/products',
      name: 'products',
      component: ProductsView,
    },
    {
      path: '/users',
      name: 'users',
      component: UsersView,
    },
    {
      path: '/staffs',
      name: 'staffs',
      component: StaffsView,
    },
    {
      path: '/orders',
      name: 'orders',
      component: OrdersView,
      meta: { requiresAuth: true }
    },
    {
      path: '/user-orders',
      name: 'user-orders',
      component: UserOrderView,
      meta: { requiresAuth: true }
    },
    {
      path: '/profile',
      name: 'profile',
      component: ProfileView,
      meta: { requiresAuth: true }
    },
    {
      path: '/admin/profile',
      name: 'admin-profile',
      component: AdminProfileView,
      meta: { requiresAuth: true, role: 'admin' }
    },
    {
      path: '/staff/profile',
      name: 'staff-profile',
      component: StaffProfileView,
      meta: { requiresAuth: true, role: 'staff' }
    },
    {
      path: '/admin/dashboard',
      name: 'AdminDashboard',
      redirect: '/products',
      meta: { requiresAuth: true, role: 'admin' }
    },
    {
      path: '/favorites',
      name: 'favorites',
      component: FavoritesView,
      meta: { requiresAuth: true, role: 'user' }
    },
    {
      path: '/admin/revenue',
      name: 'admin-revenue',
      component: RevenueView,
      meta: { requiresAuth: true, role: 'admin' }
    },
    {
      path: '/admin/hot-products',
      name: 'admin-hot-products',
      component: HotProductsView,
      meta: { requiresAuth: true, role: 'admin' }
    },
  ],
})

export default router

