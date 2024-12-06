<template>
  <div v-if="error">
    <p>{{ error }}</p>
  </div>
  <div v-else-if="isUser">
    <UserPageComponent></UserPageComponent>
  </div>
  <div v-else-if="isCoach">
    <CoachPageComponent></CoachPageComponent>
  </div>
  <div v-else-if="isAdmin">
    <p>Сторінка адміністратора у розробці.</p>
  </div>
</template>

<script setup>
import {onMounted, ref} from "vue";
import axios from "axios";
import UserPageComponent from "@/components/UserPageComponent.vue";
import CoachPageComponent from "@/components/CoachPageComponent.vue";


const isUser = ref(false);
const isCoach = ref(false);
const isAdmin = ref(false);
const error = ref("");

onMounted(async () => {
  const token = sessionStorage.getItem("token");

  try {
    const response = await axios.get("/api/users/getRole", {
      headers: { Authorization: `Bearer ${token}` },
    });
    const role = response.data;
    if (role === "ROLE_USER") isUser.value = true;
    else if (role === "ROLE_COACH") isCoach.value = true;
    else if (role === "ROLE_ADMIN") isAdmin.value = true;
  } catch (e) {
    error.value = "Не вдалося завантажити роль користувача.";
  }
});
</script>
