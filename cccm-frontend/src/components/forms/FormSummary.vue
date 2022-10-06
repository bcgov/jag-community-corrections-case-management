<template>
    <!--Summary section-->
    <div>
        <h1>Summary</h1>
        <v-progress-linear v-if="loading" indeterminate height="30" color="primary">Loading summary</v-progress-linear>

        <div v-for="data in summaryData">
            <div class="dashboard-v-card" v-if="data.answers.length > 0">
                <h3 class="heading">{{ data.section }}</h3>
                <v-data-table :items="data.answers" no-data-text="No answers for this section" class="summary-table elevation-10"
                    :headers="headers" item-key="answer.key" no-results-text="No results found" hide-default-footer>
                    <template v-slot:item.key="key">
                        <a href="#" @click="editFormItem(key)">
                            <v-icon>fas fa-eye</v-icon>
                        </a>
                    </template>
                </v-data-table>
            </div>
        </div>
    </div>
</template>
  
<script lang="ts">
import Vue from 'vue'
import { getFormSummary } from "@/components/form.api";

export default {
    name: 'FormSummary',
    data() {
        return {
            summaryData: {},
            modalEditVisible: false,
            changeCount: -1,
            loading: false,
            headers: [
                { text: 'Question', value: 'question', width: '20%',align:'center' , class: 'summary-head'},
                { text: 'Answer', value: 'value', width: '20%' ,align:'center' },
                { text: 'Comments', value: 'comment', width: '50%' ,align:'center' },
                { text: 'Action', value: 'key', width: '10%' ,align:'center' },
            ],
        }
    },
    props: {

        dataChangeCount: {
            type: Number
        },
        showSummaryCounter: {
            type: Number
        }
    },
    mounted() {
        this.getSummaryData();
    },

    methods: {

        formValuesUpdated() {
            this.getSummaryData();
        },
        editFormItem(item) {
            console.log("Clicked %o", item);
            let section = Number.parseInt(item.value.substr(1, 2)) - 1; // entries are 1-based but tab indexes are zero based (ugh)
            let question = Number.parseInt(item.value.substr(4, 6)) - 1;
            this.$emit('viewSectionQuestion', section, question);

        },
        async getSummaryData() {
            this.loading = true;
            let formId = this.$route.params.formID;
            let csNumber = this.$route.params.csNumber;
            const [error, response] = await getFormSummary(csNumber, formId);
            if (error) {
                console.error("Get summary failed: ", error);
            } else {
                this.summaryData = response;
            }
            this.loading = false;

        }
    },

}
</script>
<style scoped>


h3.heading {
    margin-top: 20px;
}

h3.heading::after {

    content: "";
    height: 0px;
    width: 50px;
    display: block;
    border-bottom: 8px solid #FCBA19;
    margin-bottom: 20px;
}

.summary-table > th > td {
    font-size: 16px;
}



</style>
  
  
  