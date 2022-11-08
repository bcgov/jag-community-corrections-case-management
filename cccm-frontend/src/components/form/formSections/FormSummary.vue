<template>
    <!--Summary section-->
    <div>
        <h1>Summary</h1>
        <v-progress-linear v-if="loading" indeterminate height="30" color="primary">Loading summary</v-progress-linear>
        <div v-for="(formEle, index) in summaryData" :key="index">
            <h2>{{ formEle.formType }}</h2>
            <div class="dashboard-v-card" v-if="formEle.data.length > 0">
                <div v-for="(section, sectionIndex) in formEle.data" :key="sectionIndex"> 
                    <h3 class="heading">{{ section.section }}</h3>
                    <div v-for="(subSection, ssIndex) in section.subSection" :key="ssIndex"> 
                        <h4 class="heading">{{ subSection.title }}</h4>
                        <v-data-table class="summary-table elevation-10" 
                            no-data-text="No answers for this section" 
                            :items="subSection.answers"
                            :headers="formHeaders" item-key="key" 
                            no-results-text="No results found" 
                            hide-default-footer>
                            <!--Customize the action field, making it clickable-->
                            <template v-slot:item.editKey="{item}">
                                <a href="#" @click="editFormItem(item.editKey)">
                                    <v-icon>fas fa-eye</v-icon>
                                </a>
                            </template>
                        </v-data-table>
                    </div>
                    
                </div>
            </div>
        </div>
    </div>
</template>
  
<script lang="ts">
import Vue from 'vue'
import { getDataForSummaryView } from "@/components/form.api";

export default {
    name: 'FormSummary',
    data() {
        return {
            summaryData: {},
            modalEditVisible: false,
            changeCount: -1,
            loading: false,
            formHeaders: [
                { text: 'Question', value: 'question', width: '20%',align:'center' , class: 'summary-head'},
                { text: 'Answer', value: 'value', width: '20%' ,align:'center' },
                { text: 'Comments', value: 'comment', width: '50%' ,align:'center' },
                { text: 'Action', value: 'editKey', width: '10%' ,align:'center' },
            ],
        }
    },
    props: {
        dataChangeCount: {
            type: Number
        },
        clientFormId: 0,
        csNumber: ''
    },
    mounted() {
        this.getSummaryData();
    },
    methods: {
        formValuesUpdated() {
            this.getSummaryData();
        },
        editFormItem(editKey) {
            console.log("Clicked %o", editKey);
            // entries are 1-based but tab indexes are zero based (ugh)
            let section = Number.parseInt(editKey.substr(1, 2)) - 1; 
            let question = Number.parseInt(editKey.substr(4, 6));
            console.log("section: , question: ", section, question);
            this.$emit('viewSectionQuestion', section, question);
        },
        async getSummaryData() {
            this.loading = true;
            const [error, response] = await getDataForSummaryView(this.csNumber, this.clientFormId, true);
            //console.log("formSummary, csNumber: {}, formId: {} ", this.csNumber, this.clientFormId, response);
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
  
  
  