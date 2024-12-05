<template>
  <form @submit.prevent="login">
    <div class="">
      <label for="usernameOrEmail">Ім'я користувача або електронна пошта</label>
      <input type="text" id="usernameOrEmail" v-model="usernameOrEmail" placeholder="Ввведіть тут">
      <label for="password">Пароль</label>
      <input type="password" id="password" v-model="password" placeholder="Введіть тут">
      <button type="submit">Вхід</button>
    </div>
    <p>{{ message }}</p>
  </form>
</template>

<script setup>
import {ref} from "vue";
import axios, {HttpStatusCode} from "axios";
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
    throw HttpStatusCode.Unauthorized
  }
}
</script>