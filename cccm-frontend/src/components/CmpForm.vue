<template>
  <div data-app :key="formKey" >
    <!-- CRNA form instance. Modal dialog section-->
    <div v-if="formType === $CONST_FORMTYPE_CRNA">
      <!--SARA form creation modal dialog-->
      <v-btn
        id="id_modal_createSARAForm"
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
            Are you sure you want to create SARA form?
          </v-card-title>
          <v-card-text>
            <br><br>
          </v-card-text>
          <v-card-actions>
            <v-btn
              @click="dialog = false"
            >
            No, I don't want to create
            </v-btn>
            <v-spacer></v-spacer>
            <v-btn
              color="primary"
              dark
              @click="handleCreateSARAFormBtnClick"
            >
              Yes, continue
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>

      <!-- Form delete modal dialog-->
      <v-btn
        id="id_modal_deleteForm"
        v-show=false
        @click.stop="deleteDialog = true"
      ></v-btn>
      <v-dialog
          v-model="deleteDialog"
          persistent
          max-width="550"
        >
        <v-card>
          <v-card-title class="text-h5">
            <span v-if="relatedClientFormId">
              Are you sure you want to delete?
            </span>
            <span v-else>
              Are you sure you want to delete this form?
            </span>
          </v-card-title>
          
          <v-card-text >
            <span v-if="relatedClientFormId">
              The CRNA-CMP and the SARA-CMP forms and all the information you have entered will be deleted. You will be directed to the client's RNA list.
            </span>
            <span v-else>
              This form and all the information you have entered will be deleted and you will be directed to the client's RNA list. 
            </span>
          </v-card-text>
          <v-card-actions>
            <v-btn
              @click="deleteDialog = false"
            >
            No, I don't want to delete
            </v-btn>
            <v-spacer></v-spacer>
            <v-btn
              color="#f81e41"
              dark
              @click="handleDeleteFormBtnClick"
            >
              <span v-if="relatedClientFormId">
                Yes, delete form(s)
              </span>
              <span v-else>
                Yes, delete this form
              </span>
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </div>

    <!-- SARA form instance. Modal dialog section-->
    <div v-if="formType === $CONST_FORMTYPE_SARA">
      <!--Form delete modal dialog -->
      <v-btn
        id="id_modal_deleteForm"
        v-show=false
        @click.stop="deleteDialog = true"
      ></v-btn>
      <v-dialog
          v-model="deleteDialog"
          persistent
          max-width="550"
        >
        <v-card>
          <div class="col-sm-12 m-7">
            <v-card-title >
              Select what you want to delete:
            </v-card-title>
            <v-checkbox
              v-model="selectedFormTypeValue"
              label="CRNA-CMP"
              value="crna"
            ></v-checkbox>
            <v-checkbox
              v-model="selectedFormTypeValue"
              :readonly=true
              label="SARA-CMP"
              value="sara"
            ></v-checkbox>
            <v-card-title>
            Are you sure you want to delete?
          </v-card-title>
          <v-card-text>
            The form(s) and all the information you have entered will be deleted and you will be directed to the client's RNA list. 
          </v-card-text>
          </div>
          
          <v-card-actions>
            <v-btn
              @click="deleteDialog = false"
            >
            No, I don't want to delete
            </v-btn>
            <v-spacer></v-spacer>
            <v-btn
              color="#f81e41"
              dark
              @click="handleDeleteFormBtnClick"
            >
              Yes, delete form(s)
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </div>
    <section class="pr-4 pl-4">
      <v-tabs v-model="current_tab" fixed-tabs color="deep-purple accent-4">
        <v-tab v-for="item in items" :key="item.id" :href="'#tab-' + item.id"> 
          <span v-if="item.id === $CONST_FORMTYPE_CRNA">{{ item.tab }}</span>
          <div v-if="item.id === 'saraBtn'" class="p-4">
            <v-btn
              v-show=true
              @click.stop="createSARA"
            ><i class="fa fa-plus"></i>&nbsp; Add SARA-CMP Form</v-btn>
          </div>
          <span v-if="item.id === $CONST_FORMTYPE_SARA" class="p-4">
            {{ item.tab }}
          </span>
        </v-tab>
      </v-tabs>
      <v-tabs-items v-model="current_tab">
        <v-tab-item v-for="item in items" :key="item.id" :id="'tab-' + item.id">
          <FormRenderer :formType="item.id" :formId="item.formId" :csNumber="clientNum" @cancelFormClicked="handleCancelForm"></FormRenderer>
        </v-tab-item>
      </v-tabs-items>
    </section>
  </div>
</template>

<script lang="ts">
import Vue from 'vue'
import { Component } from 'vue-property-decorator';
import FormRenderer from "@/components/form/FormRenderer.vue";
import { createSARAForm, getClientFormDetails } from "@/components/form.api";

export default {
  name: 'CMPForm',
  components: {
    FormRenderer,
  },
  data() {
    return {
      formId: '',
      clientNum: '',
      relatedClientFormId: null,
      formType: '',
      newCreatedFormId: 0,
      current_tab: 'tab-CRNA',
      items: [],
      dialog: false,
      deleteDialog: false,
      selectedFormTypeValue: [],
      formKey: 0,      
    }
  },
  mounted(){
    this.formId = this.$route.params.formID;
    this.clientNum = this.$route.params.csNumber;
    this.getClientFormDetailsAPI();
  },
  methods: {
    async getClientFormDetailsAPI() {
      const [error, response] = await getClientFormDetails(this.clientNum, this.formId);
      if (error) {
        console.error("Failed getting client form details", error);
      } else {
        //console.log("Form details: ", response);
        this.formType = response.module;
        this.relatedClientFormId = response.relatedClientFormId;
        
        // if formType is 'CRNA', add 'CRNA-CMP' tab, and set the current_tab to 'tab-CRNA'
        if (this.formType === this.$CONST_FORMTYPE_CRNA) {
          this.items.push({ tab: 'CRNA-CMP', id: this.$CONST_FORMTYPE_CRNA, formId: this.formId });
          this.current_tab = 'tab-CRNA';
          if (!this.relatedClientFormId) {
            // show the 'add sara' btn if the crna form hasn't linked with sara
            this.items.push({ tab: '', id: 'saraBtn', formId: '' });
          } else {
            // otherwise, show sara tab
            this.items.push({ tab: 'SARA-CMP', id: this.$CONST_FORMTYPE_SARA, formId: this.relatedClientFormId });
          }
        }
        // if formType is 'SARA', add 'SARA-CMP' tab, and set the current_tab to 'tab-SARA'
        if (this.formType === this.$CONST_FORMTYPE_SARA) {
          this.items.push({ tab: 'CRNA-CMP', id: this.$CONST_FORMTYPE_CRNA, formId: this.relatedClientFormId });
          this.items.push({ tab: 'SARA-CMP', id: this.$CONST_FORMTYPE_SARA, formId: this.formId });
          this.current_tab = 'tab-SARA';
        }
        this.selectedFormTypeValue.push("sara");
      }
      //this.formKey++;
    },
    async createSARAFormAPI() {
      let formData = {};
      // set formData
      formData.clientNumber = "00142091";
      //formData.clientNumber = this.clientNum;
      formData.linkedClientFormId = this.formId;
      const [error, SARAFormId] = await createSARAForm(formData);
      if (error) {
        console.error("Failed creating SARA form instance", error);
      } else {
        this.newCreatedFormId = SARAFormId;
      }
    },
    handleCreateSARAFormBtnClick() {
      this.createSARAFormAPI();
      this.$router.push({
        name: "cmpform",
        params: {
          formID: this.newCreatedFormId,
          csNumber: this.clientNum
        }
      });
    },
    createSARA() {
      console.log("Create SARA Form");
      let modal = document.getElementById("id_modal_createSARAForm");
      if (modal != null) {
        modal.click();
      }
    },
    handleCancelForm() {
      console.log("Cancel Form");
      let modal = document.getElementById("id_modal_deleteForm");
      if (modal != null) {
        modal.click();
      }
    },
    handleDeleteFormBtnClick() {
      this.deleteDialog = false;
      if (this.formType === this.$CONST_FORMTYPE_CRNA) {
        //this.deleteForm();
      }
      if (this.formType === this.$CONST_FORMTYPE_SARA) {
        console.log("selectedFormTypeValue: ", this.selectedFormTypeValue);
        //this.deleteForm();
      }
      //Redirect User back to clientRecord.RNAList
      this.$router.push({
        name: 'clientrecord',
        params: {
          clientNum: this.clientNum,
          tabIndex: 'tab-rl'
        }
      });
    },
  }
}
</script>


