<template>
  <!--Print view-->
  <div>
      <v-progress-linear v-if="loading" indeterminate height="30" color="primary">Loading print view ...</v-progress-linear>
      <div v-for="(formEle, index) in summaryData" :key="index">
          <h3> {{ getFormTypeDesc[formEle.formType] }}</h3>
          <div class="dashboard-v-card" v-if="formEle.data.length > 0">
              <div v-for="(section, sectionIndex) in formEle.data" :key="sectionIndex"> 
                  <div class="subSectionTitleClass">{{ section.section }}</div>
                  <div v-for="(subSection, ssIndex) in section.subSection" :key="ssIndex"> 
                      <h5>{{ subSection.title }}</h5>
                      <v-data-table v-if="subSection.title == 'Intervention Plan'"
                          no-data-text="" 
                          :items="subSection.answers"
                          :headers="interventionHeaders" item-key="key" 
                          no-results-text="No results found" 
                          hide-default-footer>
                      </v-data-table>
                      <v-data-table v-else
                          no-data-text="No answers for this section" 
                          :items="subSection.answers"
                          :headers="formHeaders" item-key="key" 
                          no-results-text="No results found" 
                          hide-default-footer>
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
        clientFormId: '',
        csNumber: '',
        summaryData: {},
        modalEditVisible: false,
        changeCount: -1,
        loading: false,
        formHeaders: [
            { text: 'Question', value: 'question', width: '20%', align:'center' },
            { text: 'Answer', value: 'value', width: '20%', align:'center' },
            { text: 'Comments', value: 'comment', width: '50%', align:'center' }
        ],
        interventionHeaders: [
            { text: 'Criminogenic Needs', value: 'question', width: '20%', align: 'center' },
            { text: 'Specific Factor', value: '', width: '10%', align: 'center' },
            { text: 'Intervention Type', value: 'interventionType', width: '20%', align: 'center' },
            { text: 'Intervention Description', value: 'interventionComment', width: '40%', align: 'center' }
        ]
      }
  },
  mounted() {
    this.csNumber = this.$route.params.csNumber;
    this.clientFormId = this.$route.params.formID;
    this.getSummaryData();
  },
  methods: {
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
          // set the form title
          this.$FORM_INFO.forEach((entry) => {
              formTypeDesc[entry.formType] = entry.formTitle;
          });
          return formTypeDesc;
      }
  }
}
</script>

