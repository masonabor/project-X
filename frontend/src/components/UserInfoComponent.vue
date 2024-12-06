<template>
  <main>
    <div class="user-profile-page">
      <div v-if="errorMessage" class="error-message">
        <p>{{ errorMessage }}</p>
      </div>
      <div v-else>
        <h1 v-if="isCoach">Інформація про тренера</h1>
        <h1 v-else>Інформація про користувача</h1>

        <div v-if="user">
          <p><strong>Ім'я:</strong> {{ user.firstName }} {{ user.lastName }}</p>
          <p><strong>Логін:</strong> {{ user.username }}</p>
          <p><strong>Email:</strong> {{ user.email }}</p>
          <p><strong>Телефон:</strong> {{ user.phone }}</p>
          <p><strong>Стать:</strong> {{ user.gender }}</p>
          <p><strong>Дата народження:</strong> {{ formatDate(user.dateOfBirth) }}</p>
          <p v-if="user.accountNumber"><strong>Номер рахунку:</strong> {{ user.accountNumber }}</p>
          <p v-if="user.banned"><strong>Статус:</strong> Заблокований</p>
          <p v-else><strong>Статус:</strong> Активний</p>
        </div>

        <div v-if="isCoach && clients.length">
          <h2>Клієнти тренера</h2>
          <table>
            <thead>
            <tr>
              <th>Ім'я</th>
              <th>Прізвище</th>
              <th>Email</th>
              <th>Телефон</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="client in clients" :key="client.id">
              <td>{{ client.firstName }}</td>
              <td>{{ client.lastName }}</td>
              <td>{{ client.email }}</td>
              <td>{{ client.phone }}</td>
            </tr>
            </tbody>
          </table>
        </div>

        <div v-if="user && user.schedules && user.schedules.length">
          <h2>Розклад</h2>
          <table>
            <thead>
            <tr>
              <th>День тижня</th>
              <th>Час початку</th>
              <th>Час закінчення</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="schedule in user.schedules" :key="schedule.dayOfWeek">
              <td>{{ getDayName(schedule.dayOfWeek) }}</td>
              <td>{{ schedule.startTime }}</td>
              <td>{{ schedule.endTime }}</td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </main>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { useRoute } from "vue-router";

const user = ref({});

const clients = ref([]);
const errorMessage = ref('');
const isCoach = ref(false);

const route = useRoute();
const userId = route.params.id;

function formatDate(date) {
  if (!date) return 'Невідомо';
  return new Date(date).toLocaleDateString('uk-UA');
}

function getDayName(dayOfWeek) {
  const days = {
    MONDAY: "Понеділок",
    TUESDAY: "Вівторок",
    WEDNESDAY: "Середа",
    THURSDAY: "Четвер",
    FRIDAY: "П’ятниця",
    SATURDAY: "Субота",
    SUNDAY: "Неділя"
  };
  return days[dayOfWeek] || dayOfWeek;
}

async function fetchUserInfo() {
  try {
    const response = await axios.get(`/api/users/checkUserInfo/${userId}`, {
      headers: {
        Authorization: `Bearer ${sessionStorage.getItem('token')}`,
      },
    });
    user.value = response.data;
    isCoach.value = response.data.role === 'ROLE_COACH';

    if (isCoach.value) {
      const coachResponse = await axios.get(`/api/coaches/checkCoachInfo/${userId}`, {
        headers: {
          Authorization: `Bearer ${sessionStorage.getItem('token')}`
        }
      });

      user.value = coachResponse.data;
      await fetchCoachClients();
    }
  } catch (error) {
    errorMessage.value = 'Не вдалося завантажити інформацію користувача.';
    console.error(error);
  }
}

async function fetchCoachClients() {
  try {
    const response = await axios.get(`/api/coaches/getClients/${userId}`, {
      headers: {
        Authorization: `Bearer ${sessionStorage.getItem('token')}`,
      },
    });
    clients.value = response.data;
  } catch (error) {
    errorMessage.value = 'Не вдалося завантажити список клієнтів тренера.';
    console.error(error);
  }
}

onMounted(() => {
  fetchUserInfo();
});
</script>

<style scoped>
.user-profile-page {
  max-width: 800px;
  margin: 20px auto;
  font-family: Arial, sans-serif;
}

.error-message {
  color: red;
  text-align: center;
  margin: 20px;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

th, td {
  border: 1px solid #ddd;
  padding: 10px;
  text-align: left;
}

th {
  background-color: #f4f4f4;
}
</style>
