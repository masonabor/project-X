<template>
  <main>
    <div class="replenish-account">
      <h2>Поповнення рахунку</h2>
      <form @submit.prevent="submitForm">
        <div class="input-group">
          <label for="amount">Сума поповнення (грн)</label>
          <input
              type="number"
              id="amount"
              v-model.number="amount"
              placeholder="Введіть суму"
              min="1"
              step="0.01"
          />
          <span v-if="amountError" class="error-message">{{ amountError }}</span>
        </div>
        <button type="submit" class="btn" :disabled="isLoading">
          {{ isLoading ? "Поповнення..." : "Поповнити" }}
        </button>
      </form>

      <div v-if="successMessage" class="success-message">{{ successMessage }}</div>
      <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
    </div>
  </main>
</template>

<script setup>
import { ref } from "vue";
import axios from "axios";

const amount = ref(0);
const amountError = ref("");
const successMessage = ref("");
const errorMessage = ref("");
const isLoading = ref(false);

function validateAmount() {
  amountError.value = "";
  if (!amount.value || amount.value <= 0) {
    amountError.value = "Введіть правильну суму (більше 0)";
    return false;
  }
  return true;
}

async function submitForm() {
  if (!validateAmount()) {
    return;
  }

  isLoading.value = true;
  successMessage.value = "";
  errorMessage.value = "";

  try {
    const response = await axios.patch(
        "/api/account/deposit",
        {
          amount: amount.value
        },
        {
          headers: {
            Authorization: `Bearer ${sessionStorage.getItem("token")}`,
          },
        }
    );
    successMessage.value = response.data;
    amount.value = 0;
  } catch (error) {
    errorMessage.value =
        error.response?.data || "Помилка при поповненні рахунку. Спробуйте ще раз.";
    console.error(error);
  } finally {
    isLoading.value = false;
  }
}
</script>

<style scoped>
.time-input-form {
  max-width: 400px;
  margin: auto;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  font-family: Arial, sans-serif;
  background: #f9f9f9;
}

.input-group {
  margin-bottom: 20px;
}

label {
  display: block;
  margin-bottom: 8px;
  font-weight: bold;
}

input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 16px;
  box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.1);
}

input:focus {
  border-color: #007bff;
  outline: none;
  box-shadow: 0 0 5px rgba(0, 123, 255, 0.5);
}

.btn {
  display: block;
  width: 100%;
  padding: 10px 15px;
  background-color: #c41717; /* Червоний колір */
  color: white; /* Білий текст */
  border: none;
  border-radius: 20px; /* Заокруглені краї */
  cursor: pointer;
  font-size: 16px;
  font-weight: bold; /* Жирний текст */
  text-align: center;
  transition: background-color 0.3s ease; /* Анімація зміни кольору */
}

.btn:disabled {
  background-color: #f19999; /* Світло-червоний для неактивної кнопки */
  cursor: not-allowed;
}

.btn:hover {
  background-color: #a10f0f; /* Темніший червоний при наведенні */
}

.success-message {
  margin-top: 20px;
  color: green;
}

.error-message {
  margin-top: 10px;
  color: red;
  font-size: 14px;
}
</style>
