import { createRouter, createWebHistory } from 'vue-router'
import UserRegistrationComponent from "@/components/UserRegistrationComponent.vue"
import LoginComponent from "@/components/LoginComponent.vue";
import DepositComponent from "@/components/DepositComponent.vue";
import TrainingPlanComponent from "@/components/TrainingPlanComponent.vue";
import HomePageComponent from "@/components/HomePageComponent.vue";
import CoachPageComponent from "@/components/CoachPageComponent.vue";
import ViewUserComponent from "@/components/ViewUserComponent.vue";
import AdminPageComponent from "@/components/AdminPageComponent.vue";
import UserInfoComponent from "@/components/UserInfoComponent.vue";

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
        },
        {
            path: "/viewPage",
            name: "ViewUserPage",
            component: ViewUserComponent
        },
        {
            path: "/adminPage",
            name: "AdminPage",
            component: AdminPageComponent
        },
        {
            path: '/userInfo/:id',
            name: "UserInfoPage",
            component: UserInfoComponent
        }]
})




