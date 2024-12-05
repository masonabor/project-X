import { createRouter, createWebHistory } from 'vue-router'
import UserPageComponent from "@/components/UserPageComponent.vue"
import UserRegistrationComponent from "@/components/UserRegistrationComponent.vue"
import LoginComponent from "@/components/LoginComponent.vue";
import DepositComponent from "@/components/DepositComponent.vue";
import TrainingPlan from "@/components/TrainingPlan.vue";
import TablePage from '@/components/TablePage.vue';

import AddUser from '@/components/AddUser.vue';
import EditUser from '@/components/EditUser.vue';


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
        },
        // {
        //     path: "/table",
        //     name: "TablePage",
        //     component: TablePage
        // },
        { path: '/users', name: 'UsersTable', component: TablePage },
        { path: '/addUser', name: 'AddUser', component: AddUser },
        { path: '/editUser/:id', name: 'EditUser', component: EditUser },
    ]

})




