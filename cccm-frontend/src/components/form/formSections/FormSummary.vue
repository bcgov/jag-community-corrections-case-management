<template>
    <!--Summary section-->
    <div>
        <v-progress-linear v-if="loading" indeterminate height="30" color="primary">Loading summary</v-progress-linear>
        <div v-for="(formEle, index) in summaryData" :key="index">
            <h3>{{ getFormTypeDesc[formEle.formType] }}</h3>
            <div class="dashboard-v-card" v-if="formEle.data.length > 0">
                <div v-for="(section, sectionIndex) in formEle.data" :key="sectionIndex"> 
                    <div class="subSectionTitleClass">{{ section.section }}</div>
                    <div v-for="(subSection, ssIndex) in section.subSection" :key="ssIndex"> 
                        <h5>{{ subSection.title }}</h5>
                        <v-data-table 
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
    props: {
        clientFormId: 0,
        csNumber: '',
        printRequested: false
    },
    data() {
        return {
            summaryData: {},
            modalEditVisible: false,
            changeCount: -1,
            loading: false,
            formHeaders: [
                { text: 'Question', value: 'question', width: '20%', align:'center' },
                { text: 'Answer', value: 'value', width: '20%', align:'center' },
                { text: 'Comments', value: 'comment', width: '50%', align:'center' },
                { text: 'Action', value: 'editKey', width: '10%', align:'center' },
            ],
        }
    },
    mounted() {
        this.getSummaryData();
        //console.log("this.printRequested: ", this.printRequested);
        if (this.printRequested) { 
            this.$emit('cancelPrintFlag');
            setTimeout(() => {
                window.print();
            }, 1000);
        }
    },
    methods: {
        formValuesUpdated() {
            this.getSummaryData();
        },
        editFormItem(editKey) {
            // entries are 1-based but tab indexes are zero based (ugh)
            let section = Number.parseInt(editKey.substr(1, 2)) - 1; 
            let question = Number.parseInt(editKey.substr(4, 6));
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
    computed: {
        getFormTypeDesc() {
            let formTypeDesc = [];
            formTypeDesc[this.$CONST_FORMTYPE_CRNA] = 'Community Risk Needs Assessment Form';
            formTypeDesc[this.$CONST_FORMTYPE_SARA] = 'SARA';

            return formTypeDesc
        }
    }
}
</script>
  
  
  