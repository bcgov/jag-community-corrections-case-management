<template>
  <div class="home">
    <Form src="https://hcydsplculhonzk.form.io/demoform1" />
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';

@Component({
  components: {
    Form,
  },
})
export default class Home extends Vue {}
</script>