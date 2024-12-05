import { createRouter, createWebHistory } from 'vue-router'
import UserPageComponent from "@/components/UserPageComponent.vue"
import UserRegistrationComponent from "@/components/UserRegistrationComponent.vue"
import LoginComponent from "@/components/LoginComponent.vue";
import DepositComponent from "@/components/DepositComponent.vue";
import TrainingPlan from "@/components/TrainingPlan.vue";
import HomePageComponent from "@/components/HomePageComponent.vue";

export default createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: "/",
            name: "Home",
            component: HomePageComponent,
        },
        {
            path: "/register",
            name: "RegisterPage",
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
        },
        {
            path: "/deposit",
            name: "DepositPage",
            component: DepositComponent
        },
        {
            path: "/trainingPlan",
            name: "TrainingPlanPage",
            component: TrainingPlan
        }]
})




