<template>
  <div class="weekly-schedule">
    <h2>Створити розклад на тиждень</h2>

    <form @submit.prevent="submitSchedule">
      <div>
        <label for="coach">Оберіть тренера:</label>
        <select v-model="selectedCoach" required>
          <option value="" disabled>Виберіть тренера</option>
          <option v-for="coach in coaches" :key="coach.id" :value="coach.id">
            {{ coach.name }}
          </option>
        </select>
      </div>

      <div v-for="(day, index) in schedule" :key="index" class="schedule-item">
        <div>
          <label>{{ day.dayOfWeek }}</label>
        </div>

        <div>
          <label for="start-time">Час початку:</label>
          <input type="time" v-model="day.startTime" />
        </div>

        <div>
          <label for="end-time">Час закінчення:</label>
          <input type="time" v-model="day.endTime" />
        </div>
      </div>

      <button type="submit" class="submit-btn">
        Зберегти розклад
      </button>
    </form>

    <div v-if="successMessage" class="success-message">
      {{ successMessage }}
    </div>

    <div v-if="errorMessage" class="error-message">
      {{ errorMessage }}
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import axios from 'axios';

export default {
  setup() {
    const schedule = ref([
      { dayOfWeek: 'MONDAY', startTime: '', endTime: '' },
      { dayOfWeek: 'TUESDAY', startTime: '', endTime: '' },
      { dayOfWeek: 'WEDNESDAY', startTime: '', endTime: '' },
      { dayOfWeek: 'THURSDAY', startTime: '', endTime: '' },
      { dayOfWeek: 'FRIDAY', startTime: '', endTime: '' },
      { dayOfWeek: 'SATURDAY', startTime: '', endTime: '' },
      { dayOfWeek: 'SUNDAY', startTime: '', endTime: '' },
    ]);

    const coaches = ref([]);
    const selectedCoach = ref('');
    const successMessage = ref('');
    const errorMessage = ref('');

    const fetchCoaches = async () => {
      try {
        const response = await axios.get('/api/coaches/getCoachesByDTO', {
          headers: {
            Authorization: `Bearer ${sessionStorage.getItem('token')}`,
          },
        });
        coaches.value = response.data;
      } catch (error) {
        errorMessage.value =
            'Помилка при завантаженні списку тренерів: ' +
            (error.response?.data?.message || error.message);
      }
    };

    const submitSchedule = async () => {
      try {
        if (!selectedCoach.value) {
          errorMessage.value = 'Оберіть тренера перед збереженням розкладу.';
          return;
        }

        const validSchedule = schedule.value.filter(
            (item) => item.startTime && item.endTime
        );

        if (validSchedule.length === 0) {
          errorMessage.value = 'Заповніть хоча б один день.';
          return;
        }

        await axios.post(
            `/api/trainingPlans/createTrainingPlan?coachId=${selectedCoach.value}`,
            validSchedule,
            {
              headers: {
                Authorization: `Bearer ${sessionStorage.getItem('token')}`,
              },
            }
        );

        successMessage.value = 'Розклад успішно збережено!';
        errorMessage.value = '';
        resetForm();
      } catch (error) {
        errorMessage.value =
            'Помилка при збереженні розкладу: ' +
            (error.response?.data?.message || error.message);
        successMessage.value = '';
      }
    };

    const resetForm = () => {
      schedule.value.forEach((item) => {
        item.startTime = '';
        item.endTime = '';
      });
      selectedCoach.value = '';
    };

    onMounted(() => {
      fetchCoaches();
    });

    return {
      schedule,
      coaches,
      selectedCoach,
      successMessage,
      errorMessage,
      submitSchedule,
    };
  },
};
</script>

<style scoped>
.weekly-schedule {
  max-width: 600px;
  margin: auto;
}

.schedule-item {
  margin-bottom: 1rem;
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  gap: 1rem;
  align-items: center;
}

label {
  display: block;
  font-weight: bold;
}

input,
select {
  padding: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 5px;
}

button {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.submit-btn {
  background-color: #007bff;
  color: white;
}

.success-message {
  color: green;
  margin-top: 1rem;
}

.error-message {
  color: red;
  margin-top: 1rem;
}
</style>
