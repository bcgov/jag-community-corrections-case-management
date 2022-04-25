<template>
    <div class="container">
        <Form :src="formJSON"/>
    </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import sampleFormData from './sample.json';

@Component({
    components: {
        Form,
    },
    data: () => {
        return {
            formJSON: sampleFormData,
            formUrl: "https://hcydsplculhonzk.form.io/demoform1",
        };
    },
})
export default class Home extends Vue {}

</script>