<template>
    <div v-if="user">
      <p>Привіт, {{user.lastName}} {{user.firstName}} {{user.middleName}}</p>
    </div>
</template>
<script>

export default {

  computed: {

  }
}

</script>
