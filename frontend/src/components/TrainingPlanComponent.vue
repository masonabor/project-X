<template>
  <main>
    <div class="weekly-schedule">
      <div v-if="hasCoach">
        <h2>Редагувати розклад тренувань</h2>
      </div>
      <div v-else>
        <h2>Створити розклад на тиждень</h2>
      </div>

      <form @submit.prevent="submitSchedule">
        <div v-if="!hasCoach">
          <label for="coach">Оберіть тренера:</label>
          <select v-model="selectedCoach" required>
            <option value="" disabled>Виберіть тренера</option>
            <option v-for="coach in coaches" :key="coach.id" :value="coach.id">
              {{ coach.lastName }} {{ coach.firstName}}
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
  </main>
</template>

<script>
import { ref, onMounted } from 'vue';
import axios from 'axios';

export default {
  computed: {
  },
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
    const trainingPlan = ref(null);
    const hasCoach = ref(false);

    const fetchCoaches = async () => {
      try {
        const response = await axios.get('/api/coaches/getCoaches', {
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

        const validSchedule = schedule.value
            .filter((item) => item.startTime && item.endTime)
            .map((item) => ({
              dayOfWeek: item.dayOfWeek,
              startTime: `${item.startTime}`,
              endTime: `${item.endTime}`,
            }));

        if (validSchedule.length === 0) {
          errorMessage.value = 'Заповніть хоча б один день.';
          return;
        }

        if (trainingPlan.value.coachId !== 0) {
          selectedCoach.value = trainingPlan.value.coachId;
        }

        await axios.post(
            `/api/trainingPlans/createTrainingPlan`,
            {
              coachId: selectedCoach.value,
              schedules: validSchedule,
            },
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

    const getTrainingPlan = async () => {
      try {
        const response = await axios.get('/api/trainingPlans/getTrainingPlan', {
          headers: {
            Authorization: `Bearer ${sessionStorage.getItem('token')}`,
          },
        });

        trainingPlan.value = response.data;

        if (trainingPlan.value.coachId !== 0) {
          selectedCoach.value = trainingPlan.value.coachId;
          hasCoach.value = true;
        }

      } catch (error) {
        errorMessage.value =
            'Помилка при завантаженні тренувального плану: ' +
            (error.response?.data?.message || error.message);
        trainingPlan.value = { value: null };
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
      getTrainingPlan();
    });

    return {
      schedule,
      coaches,
      selectedCoach,
      successMessage,
      errorMessage,
      submitSchedule,
      trainingPlan,
      hasCoach
    };
  },
};
</script>

<style scoped>


.weekly-schedule {
  max-width: 2400px; /* Збільшена ширина */
  margin: 2rem auto;
  background: #f5f5f5;
  border-radius: 15px; /* Закруглення країв */
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
  overflow: hidden;
  font-family: Arial, sans-serif;
}

h2 {
  background: #333;
  color: #fff;
  margin: 0;
  padding: 1rem;
  text-align: center;
  font-size: 1.8rem;
  border-radius: 15px 15px 0 0; /* Закруглення тільки верхніх країв */
  margin-bottom: 1rem; /* Відступ між заголовком і таблицею */
}

form {
  padding: 1.5rem;
}

form > div {
  margin-bottom: 1rem;
}

label {
  font-weight: bold;
  display: block;
  margin-bottom: 0.5rem;
  color: #333;
}

select,
input[type="time"] {
  width: 100%;
  padding: 0.8rem;
  border: 1px solid #ccc;
  border-radius: 5px;
  background: #fff;
  font-size: 1rem;
  color: #333;
}

.schedule-item {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  gap: 1rem;
  align-items: center;
  padding: 0.5rem;
}

.schedule-item:nth-child(even) {
  background: #e0e0e0; /* Світло-сірий фон для парних рядків */
}

.schedule-item:nth-child(odd) {
  background: #fff; /* Білий фон для непарних рядків */
}

button {
  display: block;
  width: 100%;
  padding: 1rem;
  border: none;
  border-radius: 10px;
  background: #c41717;
  color: #fff;
  font-size: 1.2rem;
  cursor: pointer;
  margin-top: 1rem;
  text-transform: uppercase;
  font-weight: bold;
}

button:hover {
  background-color: #a10f0f;
}


.success-message,
.error-message {
  margin-top: 1rem;
  padding: 0.8rem;
  border-radius: 5px;
  font-weight: bold;
  text-align: center;
}

.success-message {
  background-color: #4caf50;
  color: #fff;
}

.error-message {
  background-color: #f44336;
  color: #fff;
}



</style>



