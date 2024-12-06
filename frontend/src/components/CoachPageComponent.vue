<template>
  <div v-if="!isCoach">Ви не є тренером</div>
  <div v-if="isCoach">
    <div class="coach-page">
      <h1>Особистий кабінет тренера</h1>

      <div v-if="coachData" class="coach-info">
        <h2>Інформація про тренера</h2>
        <p><strong>Ім'я:</strong> {{ coachData.firstName }} {{ coachData.middleName }} {{ coachData.lastName }}</p>
        <p><strong>Електронна пошта:</strong> {{ coachData.email }}</p>
        <p><strong>Телефон:</strong> {{ coachData.phone }}</p>
        <p><strong>Стать:</strong> {{ coachData.gender }}</p>
      </div>

      <div v-if="acceptedClients.length" class="clients-table">
        <h2>Клієнти, які займаються</h2>
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
          <tr v-for="client in acceptedClients" :key="client.id">
            <td>{{ client.firstName }}</td>
            <td>{{ client.lastName }}</td>
            <td>{{ client.phone }}</td>
            <td>{{ client.email }}</td>
            <td>
              <button @click="viewClientInfo(client.id)" class="btn">Переглянути інформацію</button>
              <button @click="acceptClient(client.id, false)" class="btn">Припинити співпрацю</button>
            </td>
          </tr>
          </tbody>
        </table>
      </div>

      <div v-if="notApprovedClients.length" class="clients-table">
        <h2>Клієнти, які очікують підтвердження</h2>
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
          <tr v-for="client in notApprovedClients" :key="client.id">
            <td>{{ client.firstName }}</td>
            <td>{{ client.lastName }}</td>
            <td>{{ client.phone }}</td>
            <td>{{ client.email }}</td>
            <td>
              <button @click="viewClientInfo(client.id)" class="btn">Переглянути інформацію</button>
              <button @click="acceptClient(client.id, true)" class="btn success">Підтвердити</button>
            </td>
          </tr>
          </tbody>
        </table>
      </div>

      <div v-if="errorMessage" class="error">
        <p>{{ errorMessage }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";
import router from "@/router/route";

const coachData = ref(null);
const acceptedClients = ref([]);
const notApprovedClients = ref([]);
const errorMessage = ref("");
const isCoach = ref(false);

async function fetchClients(accepted) {
  try {
    const response = await axios.get(`/api/coaches/getAllClients/${accepted}`, {
      headers: {
        Authorization: `Bearer ${sessionStorage.getItem("token")}`,
      },
    });
    return response.data;
  } catch (error) {
    console.error(error);
    throw new Error("Не вдалося завантажити дані клієнтів.");
  }
}

async function fetchCoachData() {
  try {
    const response = await axios.get("/api/coaches/profile", {
      headers: {
        Authorization: `Bearer ${sessionStorage.getItem("token")}`,
      },
    });
    coachData.value = response.data;

    if (response.data.role === "ROLE_COACH") {
      isCoach.value = true
    }

    acceptedClients.value = await fetchClients(true);
    notApprovedClients.value = await fetchClients(false);
  } catch (error) {
    errorMessage.value = "Не вдалося завантажити дані тренера.";
  }
}

function viewClientInfo(clientId) {
  router.push({ name: "UserInfoPage", params: {
    id: clientId
    } });
}

async function acceptClient(clientId, accept) {
  try {
    await axios.patch(
        `/api/coaches/acceptTrainingPlan/${clientId}/${accept}`,
        {},
        {
          headers: {
            Authorization: `Bearer ${sessionStorage.getItem("token")}`,
          },
        }
    );
    const client = notApprovedClients.value.find((c) => c.id === clientId);
    if (client) {
      notApprovedClients.value = notApprovedClients.value.filter((c) => c.id !== clientId);
      acceptedClients.value.push(client);
    }
    alert("Клієнт успішно підтверджений.");
  } catch (error) {
    alert("Помилка під час підтвердження клієнта.");
    console.error(error);
  }
}

onMounted(() => {
  fetchCoachData();
});
</script>

