<template>
  <div class="coach-page">
    <h1>Особистий кабінет тренера</h1>

    <div v-if="coachData" class="coach-info">
      <h2>Інформація про тренера</h2>
      <p><strong>Ім'я:</strong> {{ coachData.firstName }} {{ coachData.middleName }} {{ coachData.lastName }}</p>
      <p><strong>Електронна пошта:</strong> {{ coachData.email }}</p>
      <p><strong>Телефон:</strong> {{ coachData.phone }}</p>
      <p><strong>Стать:</strong> {{ coachData.gender }}</p>
    </div>

    <div v-if="clients.length" class="clients-table">
      <h2>Список клієнтів</h2>
      <table>
        <thead>
        <tr>
          <th>Ім'я</th>
          <th>Прізвище</th>
          <th>Телефон</th>
          <th>Електронна пошта</th>
          <th>Дії</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="client in clients" :key="client.id">
          <td>{{ client.firstName }}</td>
          <td>{{ client.lastName }}</td>
          <td>{{ client.phone }}</td>
          <td>{{ client.email }}</td>
          <td>
            <button @click="viewSchedule(client.id)" class="btn">Переглянути розклад</button>
            <button @click="acceptTraining(client.id)" class="btn">Погодити</button>
            <button @click="deleteTraining(client.id)" class="btn danger">Видалити</button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>

    <div v-if="errorMessage" class="error">
      <p>{{ errorMessage }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";
import router from "@/router/route";

const coachData = ref(null);
const clients = ref([]);
const errorMessage = ref("");

async function fetchCoachData() {
  try {
    const response = await axios.get("/api/coach/profile", {
      headers: {
        Authorization: `Bearer ${sessionStorage.getItem("token")}`,
      },
    });

    coachData.value = response.data;

    const clientsResponse = await axios.get("/api/coach/clients", {
      headers: {
        Authorization: `Bearer ${sessionStorage.getItem("token")}`,
      },
    });

    clients.value = clientsResponse.data;
  } catch (error) {
    errorMessage.value = "Не вдалося завантажити дані тренера.";
    console.error(error);
  }
}

function viewSchedule(clientId) {
  router.push({ name: "ViewSchedule", params: { clientId } });
}

async function acceptTraining(clientId) {
  try {
    await axios.patch(
        `/api/coaches/acceptTrainingPlan?userId=${clientId}`,
        {},
        {
          headers: {
            Authorization: `Bearer ${sessionStorage.getItem("token")}`,
          },
        }
    );
    alert("Тренування погоджено.");
  } catch (error) {
    alert("Помилка під час погодження тренування.");
    console.error(error);
  }
}

async function deleteTraining(clientId) {
  try {
    await axios.delete(`/api/trainingPlans/deleteTrainingPlan?userId=${clientId}`, {
      headers: {
        Authorization: `Bearer ${sessionStorage.getItem("token")}`,
      },
    });
    clients.value = clients.value.filter((client) => client.id !== clientId);
    alert("Тренування видалено.");
  } catch (error) {
    alert("Помилка під час видалення тренування.");
    console.error(error);
  }
}

onMounted(() => {
  fetchCoachData();
});
</script>

<style scoped>
.coach-page {
  max-width: 800px;
  margin: auto;
  padding: 20px;
  font-family: Arial, sans-serif;
}

.coach-info {
  background: #f9f9f9;
  padding: 15px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.clients-table {
  margin-top: 20px;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;
}

th, td {
  border: 1px solid #ddd;
  padding: 10px;
  text-align: left;
}

th {
  background-color: #f4f4f4;
}

.btn {
  padding: 5px 10px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.btn.danger {
  background-color: #dc3545;
}

.btn:hover {
  background-color: #0056b3;
}

.btn.danger:hover {
  background-color: #c82333;
}

.error {
  color: red;
  margin-top: 20px;
}
</style>
