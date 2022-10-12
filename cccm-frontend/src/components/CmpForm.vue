<template>
  <div data-app>
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
            <span v-if="linkedSara">
              Are you sure you want to delete?
            </span>
            <span v-else>
              Are you sure you want to delete this form?
            </span>
          </v-card-title>
          
          <v-card-text >
            <span v-if="linkedSara">
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
              <span v-if="linkedSara">
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
        <v-tab v-for="item in items" :key="item.tab" > 
          <span v-if="item.id === 'cp'">{{ item.tab }}</span>
          <div v-if="item.id === 'saraBtn'" class="p-4">
            <v-btn
              v-show=true
              @click.stop="createSARA"
            ><i class="fa fa-plus"></i>&nbsp; Add SARA-CMP Form</v-btn>
          </div>
        </v-tab>
      </v-tabs>
      <v-tabs-items v-model="current_tab">
        <v-tab-item v-for="item in items" :key="item.tab">
          <div v-if="item.id === 'cp'" class="p-4">
            <CmpFormDetail :formType="formType" :formId="formId" :csNumber="clientNum" @cancelFormClicked="handleCancelForm"></CmpFormDetail>
           </div>
        </v-tab-item>
      </v-tabs-items>
    </section>
  </div>
</template>

<script lang="ts">
import Vue from 'vue'
import { Component } from 'vue-property-decorator';
import CmpFormDetail from "@/components/cmp-form/CmpFormDetails.vue";
import { createForm } from "@/components/form.api";

export default {
  name: 'CMPForm',
  components: {
    CmpFormDetail,
  },
  data() {
    return {
      //CONST_FORMTYPE_CRNA: 'CRNA',
      //CONST_FORMTYPE_SARA: 'SARA',
      formId: '',
      clientNum: '',
      linkedSara: null,
      formType: '',
      newCreatedFormId: 0,
      current_tab: 'tab-cp',
      items: [],
      dialog: false,
      deleteDialog: false,
      selectedFormTypeValue: [],
    }
  },
  mounted(){
    this.formId = this.$route.params.formID;
    this.clientNum = this.$route.params.csNumber;
    this.linkedSara = this.$route.params.linkedSara;
    this.formType = this.$route.params.formType;
    // if the crna form doesn't have a sara form linked, show the 'Add SARA-CMP FORM' button
    if (this.formType === 'CRNA') {
      this.items.push({ tab: 'CRNA-CMP', id: 'cp' });
      if (!this.linkedSara) {
        this.items.push({ tab: '', id: 'saraBtn' });
      }
    }
    if (this.formType === 'SARA') {
      this.items.push({ tab: 'SARA-CMP', id: 'cp' });
    }
    this.selectedFormTypeValue.push("sara");
  },
  methods: {
    async createFormAPI() {
      const [error, response] = await createForm(this.formId);
      if (error) {
        console.error(error);
      } 
      newCreatedFormId = response.formID;
    },
    handleCreateSARAFormBtnClick() {
      this.createFormAPI();
      this.$router.push({
        name: "cmpform",
        params: {
          formType: this.$CONST_FORMTYPE_SARA,
          formID: this.newCreatedFormId,
          csNumber: this.clientNum,
          linkedSara: false
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


