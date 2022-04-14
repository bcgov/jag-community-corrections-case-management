<template>
    <div id="app">
        <formio :src="formUrl" v-on:submit="onSubmitMethod" />
    </div>
</template>
<script>
    import { Formio } from 'vue-formio';

    export default {
        data: function() {
            // Load these from vuex or some other state store system.
            return {
                formUrl: "https://hcydsplculhonzk.form.io/crnacmpform"
            }
        },
        components: {
            formio: Formio
        },
        methods: {
            onSubmitMethod: function(submission) {
                console.log(submission);
            }
        }
    };
</script>