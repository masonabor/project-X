<template>
  <main>
    <div v-if="isAdmin">
      <div class="admin-dashboard">
        <h1>Адміністративна панель</h1>

        <h2>Користувачі</h2>
        <table>
          <thead>
          <tr>
            <th>Ім'я користувача</th>
            <th>Email</th>
            <th>Телефон</th>
            <th>Дії</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="user in users" :key="user.id">
            <td>{{ user.username }}</td>
            <td>{{ user.email }}</td>
            <td>{{ user.phone }}</td>
            <td>
              <button @click="viewUserDetails(user.id)">Переглянути</button>
              <button v-if="user.banned" @click="unbanUser(user.id)">Розблокувати</button>
              <button v-else @click="banUser(user.id)">Заблокувати</button>
              <button @click="deleteUser(user.id)">Видалити</button>
            </td>
          </tr>
          </tbody>
        </table>

        <h2>Тренери</h2>
        <table>
          <thead>
          <tr>
            <th>Ім'я користувача</th>
            <th>Email</th>
            <th>Телефон</th>
            <th>Дії</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="coach in coaches" :key="coach.username">
            <td>{{ coach.username }}</td>
            <td>{{ coach.email }}</td>
            <td>{{ coach.phone }}</td>
            <td>
              <button @click="viewUserDetails(coach.id)">Переглянути</button>
              <button @click="deleteUser(coach.id)">Видалити</button>
            </td>
          </tr>
          </tbody>
        </table>
        <button @click="createUser">Створити користувача</button>
      </div>
    </div>
    <div  v-else class="not-admin-box">
      Ви не є адміністратором
    </div>
  </main>
</template>

<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";
import router from "@/router/route";

const users = ref([]);
const coaches = ref([]);
const token = ref('')
const isAdmin = ref(false);

const fetchUsers = async () => {
  try {
    const response = await axios.get("/api/users/getAllUsers", {
      headers: {
        Authorization: `Bearer ${token.value}`,
      },
    });
    users.value = response.data;
  } catch (error) {
    console.error("Помилка під час отримання користувачів:", error);
  }
};

const fetchCoaches = async () => {
  try {
    const response = await axios.get("/api/coaches/getCoaches", {
      headers: {
        Authorization: `Bearer ${token.value}`,
      },
    });
    coaches.value = response.data;
  } catch (error) {
    console.error("Помилка під час отримання тренерів:", error);
  }
};

const viewUserDetails = async (userId) => {
  await router.push({ name: 'UserInfoPage', params: {
    id: userId
    }
  })
};

const banUser = async (userId) => {
  try {
    if (confirm("Ви дійсно хочете заблокувати цього користувача?")) {
      await axios.patch(`/api/admins/banUser/${userId}`, {},{
        headers: {
          Authorization: `Bearer ${token.value}`,
        },
      });
      alert("Користувач успішно заблокований!");
       await fetchUsers();
    }
  } catch (error) {
    console.error("Помилка під час блокування користувача:", error);
  }
};

const unbanUser = async (userId) => {
  try {
    if (confirm("Ви дійсно хочете заблокувати цього користувача?")) {
      await axios.patch(`/api/admins/unbanUser/${userId}`, {}, {
        headers: {
          Authorization: `Bearer ${token.value}`,
        },
      });
      alert("Користувач успішно заблокований!");
      await fetchUsers();
    }
  } catch (error) {
    console.error("Помилка під час блокування користувача:", error);
  }
};

const deleteUser = async (userId) => {
  try {
    if (confirm("Ви дійсно хочете видалити цього тренера?")) {
      await axios.delete(`/api/admins/deleteUser/${userId}`, {
        headers: {
          Authorization: `Bearer ${token.value}`,
        },
      });
      alert("Користувач успішно видалений!");
      await fetchCoaches();
    }
  } catch (error) {
    console.error("Помилка під час видалення користувача:", error);
  }
};

const checkAdmin = async () => {
  try {
    const response = await axios.get(`/api/admins/isAdmin`, {
      headers: {
        Authorization: `Bearer ${token.value}`
      }
    })
    if (response && response.status === 200) {
      isAdmin.value = response.data;
    }
  } catch (error) {
    console.error("Помилка під час отримання ролі:", error);
  }
}

const createUser = async () => {
  await router.push('/register');
}

onMounted(() => {
  token.value = sessionStorage.getItem('token')
  checkAdmin()
  fetchUsers()
  fetchCoaches()
})
</script>

<style scoped>

.not-admin-box {
  border: 4px solid #ff4d4d; /* Більш товста рамка */
  border-radius: 16px; /* Згладжені кути */
  padding: 40px; /* Більші відступи всередині рамки */
  background-color: #ffe6e6; /* Легкий червоний фон */
  color: #b30000; /* Темний текст для контрасту */
  font-family: Arial, sans-serif; /* Шрифт */
  font-size: 24px; /* Збільшений розмір тексту */
  text-align: center; /* Центрування тексту */
  max-width: 600px; /* Більша ширина */
  margin: 40px auto; /* Відцентрування по горизонталі з великим відступом */
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2); /* Більш глибока тінь */
}

.admin-dashboard {
  max-width: 900px;
  margin: 0 auto;
  padding: 20px;
}
table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;
}
table,
th,
td {
  border: 1px solid #ccc;
}
th,
td {
  padding: 10px;
  text-align: left;
}
button {
  margin-right: 10px;
  padding: 5px 10px;
  border: none;
  background-color: #e60d2e;
  color: white;
  cursor: pointer;
  border-radius: 3px;
}
button:hover {
  background-color: #ff4b5c;
}
</style>
