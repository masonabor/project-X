<template>
    <div v-if="user">
      <p>Привіт, {{user.lastName}} {{user.firstName}} {{user.middleName}}</p>
    </div>
</template>
<script>
import { getUserSession} from "@/sessionHelper";

export default {

  computed: {
    user() {
      return getUserSession()
    }
  }
}

</script>
