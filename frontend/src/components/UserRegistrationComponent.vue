<template>
    <main>
      <form @submit.prevent="submitForm" class="inputs" novalidate>

        <div class="input">
          <label for="username">Username</label>
          <input type="text" v-model="username" @input="validateUsername">
          <span class="error-message" v-if="usernameError">{{ usernameError }}</span>
        </div>
        <div class="input">
          <label for="email">Email</label>
          <input type="email" v-model="email" @input="validateEmail">
          <span class="error-message" v-if="emailError">{{ emailError }}</span>
        </div>

        <div class="input">
          <label for="password">Пароль</label>
          <input type="password" minlength="5" v-model="password" @input="validatePassword">
          <span class="error-message" v-if="passwordError">{{ passwordError }}</span>
        </div>

        <div class="input">
          <label for="lastname">Прізвище</label>
          <input type="text" minlength="3" v-model="lastName" @input="validateLastname">
          <span class="error-message" v-if="lastnameError">{{ lastnameError }}</span>
        </div>

        <div class="input">
          <label for="firstname">Ім'я</label>
          <input type="text" minlength="3" v-model="firstName" @input="validateFirstname">
          <span class="error-message" v-if="firstnameError">{{ firstnameError }}</span>
        </div>

        <div class="input">
          <label for="middlename">По батькові</label>
          <input type="text" v-model="middleName" @input="validateMiddlename">
          <span class="error-message" v-if="middlenameError">{{ middlenameError }}</span>
        </div>

        <div class="input">
          <label for="dateOfBirth">Дата народження</label>
          <input type="date" id="dateOfBirth" v-model="dateOfBirth" @input="validateDateOfBirth">
          <span class="error-message" v-if="dateOfBirthError">{{ dateOfBirthError }}</span>
        </div>

        <div class="input">
          <label for="gender">Стать</label>
          <select v-model="gender">
            <option v-for="(option, index) in genders" :key="index" :value="option">
              {{ option }}
            </option>
          </select>
          <span class="error-message" v-if="genderError">{{ genderError }}</span>
        </div>

        <div class="input">
          <label for="phone">Телефон</label>
          <input type="text" id="phone" v-model="phone" placeholder="+380########" @input="validatePhone">
          <span class="error-message" v-if="phoneError">{{ phoneError }}</span>
        </div>

        <div class="input" v-if="isAdmin">
          <label for="role">Роль користувача (для адміністраторів)</label>
          <select v-model="role">
            <option v-for="(role, index) in roles" :key="index" :value="role">
              {{ role }}
            </option>
          </select>
        </div>

        <div class="submit-container">
          <button class="btn" type="submit">Зареєструватися</button>
          <button class="btn" @click="toLoginPage">Увійти</button>
        </div>

      </form>
    </main>
</template>

<script setup>
import {onMounted, ref} from "vue"
import axios from "axios"
import router from "@/router/route"

const roles = ref(["Користувач", "Адміністратор", "Тренер"])
const genders = ref(["Чоловік", "Жінка", "Інше"])

const username = ref("")
const email = ref("")
const password = ref("")
const lastName = ref("")
const firstName = ref("")
const middleName = ref("")
const dateOfBirth = ref("")
const gender = ref("")
const phone = ref("")
const role = ref("");

const usernameError = ref('')
const emailError = ref("")
const passwordError = ref("")
const lastnameError = ref("")
const firstnameError = ref("")
const middlenameError = ref("")
const dateOfBirthError = ref("")
const genderError = ref("")
const phoneError = ref("")

const hasErrors = ref(false)
const textRegex = /[^a-zA-ZА-Яа-яЁёЇїІіЄєҐґ]/

const token = ref("")
const isAdmin = ref(false)

onMounted(async () => {
  token.value = await getToken()
  isAdmin.value = await isUserAdmin(token.value)
})

function validateEmail() {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  emailError.value = ""
  if (!email.value) {
    emailError.value = "Введіть email"
    hasErrors.value = true
  } else if (!emailRegex.test(email.value)) {
    emailError.value = "Ви не правильно ввели пошту"
    hasErrors.value = true
  }
}

function validatePassword() {
  passwordError.value = ""
  if (!password.value || password.value.length < 5) {
    passwordError.value = "Пароль повинен містити принаймні 5 символів";
    hasErrors.value = true
  }
}

function validateUsername() {
  usernameError.value = ""
  if (!username.value || username.value.length < 5) {
    usernameError.value = "Введіть юзернейм"
    hasErrors.value = true
  }
}

function validateLastname() {
  lastnameError.value = ""
  if (!lastName.value || lastName.value.length < 2) {
    lastnameError.value = "Введіть прізвище"
    hasErrors.value = true
  } else if (textRegex.test(lastName.value)) {
    lastnameError.value = "Присутні числа та нетекстові символи"
    hasErrors.value = true
  }
}

function validateFirstname() {
  firstnameError.value = ""
  if (!firstName.value || firstName.value.length < 2) {
    firstnameError.value = "Введіть ім'я"
    hasErrors.value = true
  } else if (textRegex.test(firstName.value)) {
    firstnameError.value = "Присутні числа та нетекстові символи"
    hasErrors.value = true
  }
}

function validateMiddlename() {
  middlenameError.value = ""
  if (!middleName.value || middleName.value.length < 2) {
    middlenameError.value = "Введіть по-батькові";
    hasErrors.value = true
  } else if (textRegex.test(middleName.value)) {
    middlenameError.value = "Присутні числа та нетекстові символи"
    hasErrors.value = true
  }
}

function validateDateOfBirth() {
  dateOfBirthError.value = ""
  const date = new Date(dateOfBirth.value)
  const controlDate = new Date('1950-01-01')
  if (!dateOfBirth.value) {
    dateOfBirthError.value = "Введіть дату народження"
    hasErrors.value = true
  } else if (date < controlDate) {
    dateOfBirthError.value = "Менше 1950 року"
    hasErrors.value = true
  }
}

function validateGender() {
  genderError.value = ""
  const validGenders = ["Чоловік", "Жінка", "Інше"]
  if (!validGenders.includes(gender.value)) {
    genderError.value = "Оберіть стать"
    hasErrors.value = true
  }
}

function validatePhone() {
  phoneError.value = ""
  const phoneRegex = /^(?:\+380|0)\d{9}$/
  if (!phone.value) {
    phoneError.value = "Введіть номер телефону"
    hasErrors.value = true
  } else if (!phoneRegex.test(phone.value)) {
    phoneError.value = "Неправильний формат"
    hasErrors.value = true
  }
}

async function submitForm() {

  emailError.value = passwordError.value = lastnameError.value = firstnameError.value = middlenameError.value = dateOfBirthError.value = genderError.value = phoneError.value = "";
  hasErrors.value = false

  validateEmail()
  validatePassword()
  validateLastname()
  validateFirstname()
  validateMiddlename()
  validateDateOfBirth()
  validateGender()
  validatePhone()
  validateUsername()

  if (hasErrors.value) return

  const user = {
    username: username.value,
    email: email.value,
    password: password.value,
    lastName: lastName.value,
    firstName: firstName.value,
    middleName: middleName.value,
    dateOfBirth: dateOfBirth.value,
    gender: gender.value,
    phone: phone.value
  }

  const headers = {
    headers: {
      Authorization: `Bearer ${token.value}`
    }
  }

  try {
    if (!hasErrors.value) {
      const response = await axios.post("/api/auth/register", user)
      sessionStorage.setItem("token", response.data.token)
      alert("Успішна реєстрація!");
      await router.push("/login")
    }
    if (isAdmin.value && role.value === roles.value[1] && !hasErrors.value) {
      await axios.post("/api/auth/registerAdmin", user, headers)
    }
    if (isAdmin.value && role.value === roles.value[2] && !hasErrors.value) {
      await axios.post("/api/auth/registerCoach", user, headers)
    }

    email.value = password.value = lastName.value = firstName.value = middleName.value = dateOfBirth.value = gender.value = phone.value = "";
  } catch (error) {
    alert("Помилка при реєстрації");
  }
}

async function getToken() {
  return sessionStorage.getItem("token")
}

async function isUserAdmin(token) {

  if (!token) {
    return false;
  }

  try {
    const response = await axios.get(`/api/admin/isAdmin`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })
    if (response.status === 200) {
      return response.data
    }
  } catch (e) {
    console.log("Ви не є адміністратором", e)
    return false;
  }
}

function toLoginPage() {
  router.push("/login")
}

</script>


