<template>
    <!--Print view-->
    <div class="printview">
        <v-progress-linear v-if="loading" indeterminate height="50" color="primary">Loading print view ...</v-progress-linear>
        <div :key="theKey" v-if="!loading">
            <h3>Case Plan – ({{ getFormTypeDesc }})</h3>
            <div class="dashboard-v-card">
                <v-card>
                    <section class="row justify-content-between align-items-sm-center pr-2 pl-2">
                        <div class='col-sm-4'><Strong>Completed Date: </Strong>{{ formInfoData.completedDate }}</div>
                        <div class="col-sm-4"><Strong>Created By: </Strong>{{ formInfoData.createdBy }}</div>
                        <div class="col-sm-4"><Strong>{{ getFormTypeDesc }} Type: </Strong>{{ formInfoData.clientFormType }}</div>
                    </section>
                    <section class="row justify-content-between align-items-sm-center pr-2 pl-2">
                        <div class='col-sm-4'><Strong>Office Location: </Strong>{{formInfoData.location }}</div>
                        <div class="col-sm-4"><Strong>CRNA Rating: </Strong>{{ CRNARating }}</div>
                        <div class="col-sm-4"><Strong>SARA Rating: </Strong>{{ SARALinked ? SARARating : 'N/A' }}</div>
                    </section>
                </v-card>
                <br>
                <div class="subSectionTitleClass">Client Details</div>
               <v-card>    
                    <section class="row justify-content-between align-items-sm-center pr-2 pl-2">
                        <div class='col-sm-4'><Strong>Name: </Strong>{{ formInfoData.clientData != null ? formInfoData.clientData.clientName : '' }}</div>
                        <div class="col-sm-4"><Strong>CS#: </Strong>{{ formInfoData.clientData != null ? formInfoData.clientData.clientNum : '' }}</div>
                        <div class='col-sm-4'><Strong>Order Exp. Date: </Strong>{{ formInfoData.clientData != null && formInfoData.clientData.orderInformation != null ? formInfoData.clientData.orderInformation.expiryDate : '' }}</div>
                    </section>
                </v-card>
            </div>
            <div v-for="(formEle, index) in formInstanceData" :key="index">
                <div class="dashboard-v-card" v-if="formEle.data.length > 0">
                    <div v-for="(section, sectionIndex) in formEle.data" :key="sectionIndex"> 
                            <div v-if="section.section == 'Case Plan' || section.section == 'Intervention Plan'">
                                <div class="subSectionTitleClass">{{ section.section }}</div>
                                <div v-for="(subSection, ssIndex) in section.subSection" :key="ssIndex"> 
                                    <h5>{{ subSection.title }}</h5>
                                    <v-data-table v-if="subSection.title == 'Intervention Plan'"
                                        no-data-text="" 
                                        :items="subSection.answers"
                                        :headers="interventionHeaders" item-key="key" 
                                        no-results-text="No results found" 
                                        hide-default-footer>
                                        
                                        <!--Customize the Specific Factor field, show the comments or value if comments aren't present -->
                                        <template v-slot:item.comment="{ item }">
                                            <div>{{ item.comment == null ? item.value : item.comment }}</div>
                                        </template>
                                    </v-data-table>
                                    <v-data-table v-else-if="subSection.title == 'Responsivity Factors'"
                                        no-data-text="" 
                                        :items="subSection.answers"
                                        :headers="formHeaders" item-key="key" 
                                        no-results-text="No results found" 
                                        hide-default-footer>
                                    </v-data-table>
                                    <pre class="readonly-field-text" v-else-if="subSection.title == 'Supervision Plan' 
                                                        || subSection.title == 'Assessment Comments'
                                                        || subSection.title == 'Reassessment Comments'"
                                        
                                    >{{ subSection.answers != null && subSection.answers.length == 1 ? subSection.answers[0].comment : '' }}
                                    </pre>
                                    
                                    <br>
                                </div>
                            </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import Vue from 'vue'
import { getDataForSummaryView, getClientFormMetaData, clientProfileSearch } from "@/components/form.api";

export default {
  name: 'FormSummary',
  data() {
      return {
        clientFormId: '',
        csNumber: '',
        formInfoData: {},
        formInstanceData: {},
        CRNARating: '',
        SARARating: '',
        SARALinked: false,
        loading: false,
        theKey: 0,
        formHeaders: [
            { text: 'Responsivity Factor', value: 'question', width: '50%', align:'left', sortable: false },
            { text: 'Comments', value: 'comment', width: '50%', align:'left', sortable: false }
        ],
        interventionHeaders: [
            { text: 'Criminogenic Needs', value: 'question', width: '10%', align: 'left', sortable: false },
            { text: '', value: '', width: '2%', align: 'left', sortable: false },
            { text: 'Specific Factor', value: 'comment', width: '40%', align: 'left', sortable: false },
            { text: '', value: '', width: '2%', align: 'left', sortable: false },
            { text: 'Intervention Type', value: 'interventionType', width: '15%', align: 'left', sortable: false },
            { text: '', value: '', width: '2%', align: 'left', sortable: false },
            { text: 'Intervention Description', value: 'interventionComment', width: '35%', align: 'left', sortable: false }
        ]
      }
  },
  mounted() {
    let enCoded = this.$route.params.param;
    // Process params
    if (enCoded) {
        try {
            // base64 decode the string
            let printParamsString = atob(enCoded);
            let printParams = JSON.parse(printParamsString);
            this.csNumber = printParams.csNumber;
            this.clientFormId = printParams.formID;
            this.CRNARating = printParams.CRNARating;
            this.SARARating = printParams.SARARating;
            this.getPrintData();
        } catch (err) {
            console.error("Cannot render the print view: ", err);
        }
    }  
  },
  methods: {
    async getPrintData() {
        this.loading = true;
        const [error, response] = await getDataForSummaryView(this.csNumber, this.clientFormId, true);
        console.log("formSummary, csNumber: {}, formId: {} ", this.csNumber, this.clientFormId, response);
        if (error) {
            console.error("Get form data failed: ", error);
        } else {
            this.formInstanceData = response;

            const [error1, clientFormMeta] = await getClientFormMetaData(this.csNumber, this.clientFormId);
            //console.log("clientFormMeta: ", clientFormMeta);
            if (error1) {
                console.error("Failed getting client form metadata: ", error1);
            } else {
                this.formInfoData = clientFormMeta;
                this.formInfoData.clientFormType = this.formInfoData.clientFormType == null ? '' : this.formInfoData.clientFormType ? this.$FORM_TYPE_REASSESSMENT : this.$FORM_TYPE_INITIAL;

                // set the form title
                let theForm = this.$FORM_INFO.filter( item => item.formType === this.formType );
                //console.log('form info const:', this.formType, this.$FORM_INFO, theForm);
                if (theForm != null && theForm[0] != null) {
                    this.formInfoData.formTitle = theForm[0].formTitle;
                    this.formInfoData.assessmentStatusRequired = theForm[0].assessmentStatusRequired;
                    this.formInfoData.formTypeLabel = theForm[0].formTypeLabel;
                }

                // Client profile search.
                const [error2, clientProfile] = await clientProfileSearch(this.csNumber);
                if (error2) {
                    console.error("Failed doing client profile search: ", error2);
                } else {
                    this.formInfoData.clientData = clientProfile;
                }
                this.theKey++;
            }
        }
        this.loading = false;
    }
  },
  computed: {
      getFormTypeDesc() {
        // Example print title: Case Plan – (CRNA-CMP)
        let printTitle = '';
        if (this.formInstanceData != null && this.formInstanceData.length > 0) {
            this.formInstanceData.forEach((entry) => {
                printTitle += entry.formType + '-';
                if (entry.formType === this.$CONST_FORMTYPE_SARA) {
                    this.SARALinked = true;
                }
            });
        }
        printTitle += 'CMP';
        return printTitle;
      }
  }
}
</script>

<style scoped>
    .printview:before {
        display: inline-block;
        width: 15px;
        content: url("/src/assets/images/BC_PSSG_H_CMYK_pos.jpg");
    }
    .printview:after {
        display:block;
        width:100%;
        height:0px;
        border-bottom: 1px solid #545454;
        content:""
    }
    .printview>div>h3 {
        display: inline-block;
        width: calc(100% - 50px);
        text-align: right;
        margin: 0;
        padding-left:350px;
        padding-bottom:5px;
        font-size: 25px;
    }

</style>

