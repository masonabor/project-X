import { createRouter, createWebHistory } from 'vue-router'
// import UserPageComponent from "@/components/UserPageComponent.vue"
import UserRegistrationComponent from "@/components/UserRegistrationComponent.vue"
import LoginComponent from "@/components/LoginComponent.vue";
import DepositComponent from "@/components/DepositComponent.vue";
import TrainingPlanComponent from "@/components/TrainingPlanComponent.vue";
import HomePageComponent from "@/components/HomePageComponent.vue";
import CoachPageComponent from "@/components/CoachPageComponent.vue";
import ViewUserPageComponent from "@/components/ViewUserPageComponent.vue";

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
            component: ViewUserPageComponent
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
            component: TrainingPlanComponent
        },
        {
            path: "/coachPage",
            name: "CoachPage",
            component: CoachPageComponent
        }]
})




