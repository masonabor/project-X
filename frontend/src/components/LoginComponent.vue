<template>
  <main>
    <form @submit.prevent="login">
      <div class="">
        <label for="usernameOrEmail">Ім'я користувача</label>
        <input type="text" id="usernameOrEmail" v-model="usernameOrEmail" placeholder="Ввведіть тут">
        <label for="password">Пароль</label>
        <input type="password" id="password" v-model="password" placeholder="Введіть тут">
        <button class="btn" type="submit">Вхід</button>
      </div>
      <p>{{ message }}</p>
    </form>
  </main>
</template>

<script setup>
import {ref} from "vue";
import axios from "axios";
import router from "@/router/route"


const usernameOrEmail = ref("")
const password = ref("")
const message = ref()
const token = ref("")

async function login() {

  if (usernameOrEmail.value === "" || password.value === "") {
    message.value = "Заповніть всі поля"
  }

  const loginData = {
    username: usernameOrEmail.value,
    password: password.value,
  }

  try {
    const response = await axios.post("/api/auth/login", loginData)
    if (response.status === 200) {
      token.value = response.data.token;
      sessionStorage.setItem("token", token.value)
    }
    if (!token.value) {
      message.value = "Ви не є зареєстровані"
    }
    await router.push("/viewPage")
  } catch (e) {
    message.value = 'Користувача не знайдено'
  }
}
</script>
<style scoped>
.form-container {
  width: 100%;
  max-width: 400px;
  margin: 50px auto;
  padding: 2rem;
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  text-align: center;
}

.form-content {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.form-title {
  font-size: 1.8rem;
  font-weight: bold;
  margin-bottom: 1.5rem;
  color: #333;
}

label {
  font-weight: bold;
  display: block;
  margin-bottom: 0.5rem;
  color: #333;
  text-align: left;
  width: 100%;
}

input[type="text"],
input[type="password"] {
  width: 100%;
  padding: 0.8rem;
  border: 1px solid #ccc;
  border-radius: 5px;
  background: #f9f9f9;
  font-size: 1rem;
  color: #333;
  margin-bottom: 1rem;
}

button {
  width: 100%;
  padding: 1rem;
  border: none;
  border-radius: 10px;
  background: #c41717;
  color: #fff;
  font-size: 1.2rem;
  cursor: pointer;
  text-transform: uppercase;
  font-weight: bold;
  margin-top: 1rem;
  transition: background-color 0.3s ease;
}

button:hover {
  background-color: #a10f0f;
}

.form-message {
  margin-top: 1rem;
  color: red;
  font-weight: bold;
}
</style>
<style src="@/public/css/authorizationAndRegistrationStyles.css"></style>
<style src="@/public/css/registration.css"></style>