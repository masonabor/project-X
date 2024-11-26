import { createRouter, createWebHistory } from 'vue-router'
import UserPageComponent from "@/components/UserPageComponent.vue"
import UserRegistrationComponent from "@/components/UserRegistrationComponent.vue"
import LoginComponent from "@/components/LoginComponent.vue";

export default createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: "/",
            name: "Home",
            component: UserRegistrationComponent
        },
        {
            path: "/userPage",
            name: "UserPage",
            component: UserPageComponent
        },
        {
            path: "/login",
            name: "LoginPage",
            component: LoginComponent
        }]
})




