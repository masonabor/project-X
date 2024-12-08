<template>
  <main>
  <div class="user-page">
    <h1>Особистий кабінет</h1>

    <div v-if="userData" class="user-info">
      <h2>Інформація про користувача</h2>
      <p><strong>Ім'я:</strong> {{ userData.firstName }} {{ userData.middleName }} {{ userData.lastName }}</p>
      <p><strong>Електронна пошта:</strong> {{ userData.email }}</p>
      <p><strong>Телефон:</strong> {{ userData.phone }}</p>
      <p><strong>Стать:</strong> {{ userData.gender }}</p>
      <p><strong>Дата народження:</strong> {{ formatDate(userData.dateOfBirth) }}</p>
      <p><strong>Роль:</strong> {{ userData.role.role }}</p>
      <p><strong>Номер рахунку:</strong> {{ userData.account.accountNumber }}</p>

      <div v-if="trainingPlan" class="training-plan">
        <h2>Розклад</h2>
        <ul>
          <li v-for="(schedule, index) in trainingPlan.schedules" :key="index">
            {{ schedule.dayOfWeek }}: {{ schedule.startTime }} - {{ schedule.endTime }}
          </li>
        </ul>
        <p v-if="!trainingPlan.acceptedByCoach"><strong>Тренер:</strong> {{ "Не призначено" }}</p>
        <div v-if="trainingPlan.acceptedByCoach">
          <p><strong>Тренер:</strong> {{ trainingPlan.coachName }}</p>
          <p><strong>Пошта тренера:</strong> {{ trainingPlan.coachEmail}}</p>
        </div>
      </div>

      <div class="actions">
        <button @click="deposit" class="btn">Поповнити рахунок</button>
        <button @click="createSchedule" class="btn" v-if="!trainingPlan">Створити розклад</button>
        <button @click="createSchedule" class="btn" v-if="trainingPlan">Редагувати розклад</button>
      </div>
    </div>

    <div v-if="errorMessage" class="error">
      <p>{{ errorMessage }}</p>
    </div>
  </div>
  </main>
</template>

<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";
import router from "@/router/route";

const userData = ref(null);
const trainingPlan = ref(null);
const errorMessage = ref("");

async function fetchUserData() {
  try {
    const response = await axios.get("/api/users/profile", {
      headers: {
        Authorization: `Bearer ${sessionStorage.getItem("token")}`,
      },
    });

    userData.value = {
      ...response.data,
      account: {
        ...response.data.account,
        accountNumber: response.data.account.accountNumber,
      }
    }

    const trainingResponse = await axios.get("/api/trainingPlans/getTrainingPlan", {
      headers: {
        Authorization: `Bearer ${sessionStorage.getItem("token")}`,
      },
    });

    trainingPlan.value = {
      ...trainingResponse.data,
      coachEmail: trainingResponse.data.coachEmail,
      coachName: trainingResponse.data.coachName,
      acceptedByCoach: trainingResponse.data.acceptedByCoach,
    }
  } catch (error) {
    errorMessage.value = "Не вдалося завантажити дані користувача.";
    console.error(error);
  }
}

function formatDate(date) {
  if (!date) return "Не вказано";
  return new Date(date).toLocaleDateString("uk-UA");
}

async function deposit() {
  await router.push("/deposit");
}

function createSchedule() {
  router.push("/trainingPlan");
}

onMounted(() => {
  fetchUserData();
});
</script>

<style scoped>
.user-page {
  max-width: 600px;
  margin: auto;
  padding: 20px;
  font-family: Arial, sans-serif;
}

.user-info {
  background: #f9f9f9;
  padding: 15px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.training-plan {
  margin-top: 20px;
}

.actions {
  margin-top: 20px;
}

.actions .btn {
  display: inline-block;
  margin-right: 10px;
  padding: 10px 15px;
  background-color: #e60d2e;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.actions .btn:hover {
  background-color: #ff4b5c;
}

.error {
  margin-top: 20px;
  color: red;
}
</style>
