<template>
  <div data-app>
    <v-btn
      id="id_modal_deleteForm"
      v-show=false
      @click.stop="dialog = true"
    ></v-btn>
    <v-dialog
        v-model="dialog"
        persistent
        max-width="550"
      >
      <v-card>
        <v-card-title class="text-h5">
          Delete Form?
        </v-card-title>
        <v-card-text>
          All information you have entered will be deleted. <br><br>
          You will be returned back to the Client Record's RNA list
        </v-card-text>
        <v-card-actions>
            <v-btn
              color="#f81e41"
              dark
              @click="handleDeleteFormBtnClick"
            >
              Yes, delete this form
            </v-btn>
            <v-spacer></v-spacer>
            <v-btn
              @click="dialog = false"
            >
              No, I want to continue
            </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <div class="main">
      <div class="wrap">
        <div class="mainRow">
          <div class="column L">
            <div class="menu-Sticky">
              <div class="menuR1">
                <Form :key="componentKey" :form="data_formInfo" />
              </div>
              <div class="menuR2">
                <SaraCmpFormNavigation :key="componentKey" :dataModel="formJSONFormData" @parentNavClicked="handleNavChildCallback" :parentNavMoveToNext="parentNavMoveToNext"/>
              </div>
            </div>
            <div class="mainContent">
              <SaraCmpFormDataEntry :key="componentKey" :dataModel="data_formEntries" :initData="formJSONFormData.initData" 
                  :dataMap="formJSONFormData.dataMap" :saveBtnLabel="btnSaveContinueText" :notifySaveDraft="notifySaveDraft"
                  @saveContinueClicked="handleSaveContinue" @cancelFormClicked="handleCancelForm"/>   
            </div>
          </div>
          <div class="column R">
            <div class="R-Sticky">
              <br/>
              <saraCmpFormRightPanel :key="componentKey" :dataModel="data_rightPanel" @saveDraftClicked="handleSaveDraft" @printFormClicked="handlePrintForm"/>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import Vue from 'vue'
import { Component } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import {getFormDetails, updateForm, deleteForm} from "@/components/form.api";

import SaraCmpFormDataEntry from "@/components/sara-cmp/formSections/saraCmpFormDataEntry.vue";
import SaraCmpFormNavigation from "@/components/sara-cmp/formSections/saraCmpFormNavigation.vue";
import SaraCmpFormRightPanel from "@/components/sara-cmp/formSections/saraCmpFormRightPanel.vue";
import sampleFormData from './sampleData/saraSampleData.json';

export default {
  name: 'crnaForm',
  components: {
    Form,
    SaraCmpFormDataEntry,
    SaraCmpFormNavigation,
    SaraCmpFormRightPanel,
  },
  data() {
    return {
      notifySaveDraft: 0,
      parentNavMoveToNext: 1,
      totalNumParentNav: 1,
      parentNavCurLocation: '0',
      btnSaveContinueText: "Save and Continue",
      formJSONFormData: sampleFormData,
      componentKey: 0,
      data_rightPanel: {"display": "form"},
      data_formEntries: {"display": "form"},
      data_formInfo: {"display": "form"},
      data_buttonGroup: {},
      initData: {},
      dataMap: {},
      dialog: false,
      baseURL: import.meta.env.BASE_URL,
      clientNum: '',
      formId: '',
    }
  },
  mounted(){
    this.clientNum = this.$route.params.csNumber;
    this.formId= this.$route.params.formID;
    console.log("clientNum, formId, baseURL: ", this.clientNum, this.formId, this.baseURL);
    this.getFormData();
  },
  methods: {
    async getFormData() {
      const [error, response] = await getFormDetails(this.formId);
      if (error) {
        console.error("Get formDetail failed: ", error);
      } else {
        this.private_process_formData(response);
      }

      // To be removed
      this.private_process_formData(response);
    },
    async updateForm(formData) {
      const [error, response] = await updateForm(formData);
      if (error) {
        console.error("Form update failed: ", error);
      }
    },
    async deleteForm() {
      const [error, response] = await deleteForm(this.formId);
      if (error) {
        console.error("Form delete failed: ", error);
      }
    },
    private_process_formData(response) {
      //console.log("Form payload: ", response);
      //this.formJSONFormData = response;
      const formInfo = this.formJSONFormData.components.filter(obj => {
        return obj.key === 'section_formInfo';
      });
      this.data_formInfo = formInfo[0];

      const formdata = this.formJSONFormData.components.filter(obj => {
        return obj.key === 'section_data';
      });
      this.data_formEntries = formdata[0];
      //console.log("this.data_formEntries: ", this.data_formEntries);

      this.data_rightPanel.components = this.formJSONFormData.components.filter(obj => {
        return obj.key === 'section_rightpanel';
      });
      //console.log("this.data_rightPanel: ", this.data_rightPanel);

      this.totalNumParentNav = this.data_formEntries.components.length - 1;
      //console.log("totalNumParentNav: ", this.totalNumParentNav);
      this.componentKey++;
    },
    handleSaveContinue(formData) {
      //console.log("formData: ", formData);
      // if not reaching the last section, increment this.parentNavCurLocation to navigate to the next section
      if (this.parentNavCurLocation < this.totalNumParentNav - 1) {
        this.parentNavMoveToNext++;
      }
      if (formData != null) {
        this.updateForm(formData);
      }
    },
    handleSaveDraft() {
      console.log("handleSaveDraft: ");
      this.notifySaveDraft++;
    },
    handlePrintForm() {
      console.log('Print Form.')
    },
    handleCancelForm() {
      console.log("Cancel Form");
      let modal = document.getElementById("id_modal_deleteForm");
      if (modal != null) {
        modal.click();
      }
    },
    handleDeleteFormBtnClick() {
      this.dialog = false;
      //this.deleteForm();

      //Redirect User back to clientRecord.RNAList
      this.$router.push({
        name: 'clientrecord',
        params: {
          clientNum: this.clientNum,
          tabIndex: 'tab-rl'
        }
      });
    },
    handleNavChildCallback(parentNavCurLocationFromChild) {
      this.parentNavCurLocation = parentNavCurLocationFromChild;
      if (parentNavCurLocationFromChild == this.totalNumParentNav - 1) {
        this.btnSaveContinueText = "Submit Form"; 
      } else {
        this.btnSaveContinueText = "Save and Continue"; 
      }
    }
  }  
}
</script>

<style>
.sectionTitleClass {
  font-size: 35px;
}

.subSectionTitleClass {
  font-size: 20px;
  font-weight: bold;
  color: #fcba19;
  text-decoration: underline;
  -webkit-text-decoration-color: rgb(255, 208, 0); /* Safari */  
  text-decoration-color: rgb(255, 208, 0);
}

.subSectionChildTitleClass {
  font-size: 15px;
  font-weight: bold;
  
}

.crna-subSectionTitleClass {
  font-size: 20px;
  font-weight: bold;
  padding-bottom: 1px!important;
}

.crna-subSectionTitleClass:nth-child(1):after {
  content: "";
  height: 0px;
  width: 50px;
  display: block;
  border: 4px solid #FCBA19;
  margin-bottom:20px;
}


/* 
 * Need to add the following definition, 
 * vuetify/dist/vuetify.min.css override it's original definition
 */
ol {
    display: block;
    margin-block-start: 1em;
    margin-block-end: 1em;
    margin-inline-start: 0px;
    margin-inline-end: 0px;
    padding-inline-start: 40px;
}

ul {
    display: block;
    list-style-type: disc;
    margin-block-start: 1em;
    margin-block-end: 1em;
    margin-inline-start: 0px;
    margin-inline-end: 0px;
    padding-inline-start: 40px;
}

li {
    display: list-item;
    text-align: -webkit-match-parent;
}

.hide {
	display: none;
}
.show {
	display: block;
}

.red {
  font-size: 15px;
  background-color: rgb(255, 0, 0);
  color: hsl(0, 0%, 1%)
}

.yellow {
  font-size: 15px;
  background-color: rgb(236, 216, 103);
  color: hsl(0, 0%, 1%);
  line-height: 1.6;
}

.green {
  font-size: 15px;
  background-color: rgb(103, 236, 147);
  color: hsl(0, 0%, 1%);
  line-height: 1.6;
}
</style>