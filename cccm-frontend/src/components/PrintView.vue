<template>
    <!--Print view-->
    <div class="printview">
        <v-progress-linear v-if="loading" indeterminate height="50" color="primary">Loading print view ...</v-progress-linear>
        <div :key="theKey" v-if="!loading">
            <h3>Case Plan – ({{ getFormTypeDesc }})</h3>
            <div class="p-4">
                <v-card>
                    <section class="row justify-content-between align-items-sm-center pr-2 pl-2">
                        <div class='col-sm-4'><Strong>Completed Date: </Strong>{{ formInfoData.completedDate }}</div>
                        <div class="col-sm-4"><Strong>Created By: </Strong>{{ formInfoData.createdBy }}</div>
                        <div class="col-sm-4"><Strong>{{ getFormTypeDesc }} Type: </Strong>{{ formInfoData.clientFormType }}</div>
                    </section>
                    <section class="row justify-content-between align-items-sm-center pr-2 pl-2">
                        <div class='col-sm-4'><Strong>Office Location: </Strong>{{formInfoData.location }}</div>
                        <div class="col-sm-4"><Strong>CRNA Rating: </Strong>{{ CRNARating }}</div>
                        <div class="col-sm-4"><Strong>SARA Rating: </Strong>{{ formInfoData.module == $CONST_FORMTYPE_SO_OVERALL ? SARARating: SARALinked ? SARARating : 'N/A' }}</div>
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
                <div class="p-4" v-if="formEle.data.length > 0">
                    <div v-for="(section, sectionIndex) in formEle.data" :key="sectionIndex + 'printsection'">
                        <div v-if="section.section == 'Case Plan' || section.section == 'Intervention Plan'">
                            <div class="subSectionTitleClass">{{ section.section }}</div>
                            <div v-for="(subSection, ssIndex) in section.subSection" :key="ssIndex"> 
                                <h5>{{ subSection.title }}</h5>
                                <v-data-table v-if="subSection.type == $CONST_SUBSECTIONTYPE_INTERVENTION"
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
                                <v-data-table v-else-if="subSection.type == $CONST_SUBSECTIONTYPE_RESPONSIVITY"
                                    no-data-text="" 
                                    :items="subSection.answers"
                                    :headers="subSection.noComments ? formNoCommentsHeaders : formHeaders"
                                    item-key="key"
                                    no-results-text="No results found" 
                                    hide-default-footer>
                                </v-data-table>
                                <pre class="readonly-field-text" v-else-if="subSection.type == $CONST_SUBSECTIONTYPE_SUPERVISIONPLANCOMMENT
                                                    || subSection.type == $CONST_SUBSECTIONTYPE_FORMCOMMENT
                                                    || subSection.title == 'Reassessment Comments'"
                                    
                                >{{ subSection.answers?.length == 1 ? subSection.answers[0].value : '' }}
                                </pre>
                                <br/>
                            </div>
                        </div>
                        <div v-if="section.includeInPrint">
                            <div class="subSectionTitleClass">{{ section.section }}</div>
                            <div v-for="(subSection, ssIndex) in section.subSection" :key="ssIndex + 'printsection'"> 
                                <h5>{{ subSection.title }}</h5>
                                <v-data-table
                                    no-data-text="" 
                                    :items="subSection.answers"
                                    :headers="subSection.noComments ? formNoCommentsHeaders : formHeaders"
                                    item-key="key"
                                    no-results-text="No results found" 
                                    hide-default-footer>
                                </v-data-table>                                
                                <br>
                            </div>
                        </div>
                    </div>
                    <!-- Render SMO-Overall BP section in it's original format (i.e., the table format) and place it at the end-->
                    <div v-if="formEle.formType == $CONST_FORMTYPE_SO_OVERALL">
                        <div class="subSectionTitleClass">{{ $CONST_LABEL_SMO_OVERALL_BP_SECTION }}</div>
                        <FormSMOOverallBPSection
                            :csNumber="csNumber"
                            :clientFormId="clientFormId">
                        </FormSMOOverallBPSection>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { getDataForSummaryView, getClientFormMetaData, clientProfileSearch, loadFormData } from "@/components/form.api";
import FormSMOOverallBPSection from "@/components/form/formSections/FormSMOOverallBPSection.vue";
import { dateToCCCMDateformat } from "./dateUtils";

export default {
  name: 'FormSummary',
  components: {
    FormSMOOverallBPSection
  },
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
            { text: 'Question', value: 'question', width: '20%', sortable: false },
            { text: 'Answer', value: 'value', width: '40%', sortable: false, cellClass: 'readonly-field-text' },
            { text: 'Comments', value: 'comment', width: '40%', cellClass: 'readonly-field-text' }
        ],
        formNoCommentsHeaders: [
          { text: 'Question', value: 'question', width: '20%', sortable: false },
          { text: 'Answer', value: 'value', width: '80%', sortable: false, cellClass: 'readonly-field-text' }
        ],
        interventionHeaders: [
            { text: 'Criminogenic Needs', value: 'question', width: '10%', sortable: false },
            { text: 'Specific Factor', value: 'comment', width: '40%', sortable: false, cellClass: 'readonly-field-text' },
            { text: 'Intervention Type', value: 'interventionType', width: '15%', sortable: false },
            { text: 'Intervention Description', value: 'interventionComment', width: '35%', sortable: false, cellClass: 'readonly-field-text' }
        ],
        showSMOOverallBPSection: false,
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
        //console.log("formSummary, csNumber: {}, formId: {} ", this.csNumber, this.clientFormId, response);
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
                //console.log("this.formInfoData: ", this.formInfoData);
                this.formInfoData.clientFormType = clientFormMeta.clientFormType == null ? '' : clientFormMeta.clientFormType ? this.$FORM_TYPE_REASSESSMENT : this.$FORM_TYPE_INITIAL;

                // convert date
                this.formInfoData.completedDate = dateToCCCMDateformat(this.formInfoData.completedDate);

                // set the form title
                let theForm = this.$FORM_INFO.filter( item => item.formType === this.formInfoData.module );
                //console.log('form info const:', clientFormMeta, this.formType, this.$FORM_INFO, theForm);
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

                    // convert date
                    if (this.formInfoData.clientData.orderInformation != null) {
                        this.formInfoData.clientData.orderInformation.expiryDate = dateToCCCMDateformat(this.formInfoData.clientData.orderInformation.expiryDate);
                    }
                    
                }

                // If it's SMO-Overall form, get the CRNA and SARA rating
                if (this.formInfoData.module == this.$CONST_FORMTYPE_SO_OVERALL) {
                    // Load SMO_Overall form data
                    const [error, clientFormData] = await loadFormData(this.csNumber, this.clientFormId);
                    //console.log("client form data: ", clientFormData)
                    if (error) {
                        console.error(error);
                    } else {
                        this.formData = clientFormData;
                    }
                }
                this.theKey++;
            }
        }
        this.loading = false;
    },
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

