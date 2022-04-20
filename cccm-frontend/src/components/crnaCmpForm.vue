<template>
    <div class="container">
        <Form :src="formUrl"/>
    </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';

@Component({
    components: {
        Form,
    },
    data: () => {
        return {
            formUrl: "https://hcydsplculhonzk.form.io/demoform1",
        };
    },
})
export default class Home extends Vue {}

</script>