<template>
  <div data-app :key="formKey" >
    <!-- CRNA form instance. Modal dialog section-->
    <div v-if="formType === $CONST_FORMTYPE_CRNA">
      <!--SARA form creation modal dialog-->
      <v-btn
        :id="`${CONST_MODAL_ID_PREFIX}${$CONST_FORMTYPE_SARA}`"
        v-show=false
        @click.stop="dialog = true"
      ></v-btn>
      <v-btn v-if="showSMOForms"
        :id="`${CONST_MODAL_ID_PREFIX}${$CONST_FORMTYPE_STABLE}`"
        v-show=false
        @click.stop="dialog = true"
      ></v-btn>
      <v-dialog
          v-model="dialog"
          persistent
          max-width="550"
        >
        <v-card>
          <v-card-title v-if="formToCreate == $CONST_FORMTYPE_SARA" class="text-h5">
            Are you sure you want to create SARA form?
          </v-card-title>
          <v-card-title v-if="formToCreate == $CONST_FORMTYPE_STABLE" class="text-h5">
            Are you sure you want to create STABLE form? 
            An SMO-OVERALL form will also be created.
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
              @click="handleCreateChildFormBtnClick"
            >
              Yes, continue
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </div>
    <div v-if="showSMOForms && (formType === $CONST_FORMTYPE_STAT99R || formType === $CONST_FORMTYPE_ACUTE)">
      <v-btn
        :id="`${CONST_MODAL_ID_PREFIX}${$CONST_FORMTYPE_SO_OVERALL}`"
        v-show=false
        @click.stop="dialog = true"
      ></v-btn>
      <v-dialog
          v-model="dialog"
          persistent
          max-width="550"
        >
        <v-card>
          <v-card-title v-if="formToCreate == $CONST_FORMTYPE_SO_OVERALL" class="text-h5">
            Are you sure you want to create SMO-OVERALL form?
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
              @click="handleCreateChildFormBtnClick"
            >
              Yes, continue
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </div>
    <section class="pr-4 pl-4">
      <v-tabs v-model="current_tab" fixed-tabs color="deep-purple accent-4">
        <v-tab v-for="item in items" :key="item.id" :href="'#tab-' + item.id" @click="updateTabKey(item)" > 
          <div v-if="item.id === CONST_CREATE_BTN_SARA" class="p-4">
            <v-btn
              v-show=true
              @click.stop="createChildForm($CONST_FORMTYPE_SARA)"
            ><i class="fa fa-plus"></i>&nbsp; Add SARA Form</v-btn>
          </div>
          <div v-else-if="showSMOForms && item.id === CONST_CREATE_BTN_STABLE" class="p-4">
            <v-btn
              v-show=true
              @click.stop="createChildForm($CONST_FORMTYPE_STABLE)"
            ><i class="fa fa-plus"></i>&nbsp; Add STABLE-CMP Form</v-btn>
          </div>
          <div v-else-if="showSMOForms &&  item.id === CONST_CREATE_BTN_OVERALL" class="p-4">
            <v-btn
              v-show=true
              @click.stop="createChildForm($CONST_FORMTYPE_SO_OVERALL)"
            ><i class="fa fa-plus"></i>&nbsp; Add SMO-OVERALL Form</v-btn>
          </div>
          <span v-else>{{ item.tab }}</span>
        </v-tab>
      </v-tabs>
      <v-tabs-items v-model="current_tab">
        <v-tab-item v-for="item in items" :key="item.id" :id="'tab-' + item.id">
          <FormRenderer :key="item.key" :formType="item.id" :formId="item.formId" 
            :csNumber="clientNum" :relatedClientFormId="item.relatedClientFormId" 
            :CRNARating="CRNARating"
            :SARARating="SARARating"
            :readonly="item.readonly" :locked="item.locked" 
            :createdByIdir="item.createdByIdir"
            :canPrint="item.canPrint"></FormRenderer>
        </v-tab-item>
      </v-tabs-items>
    </section>
  </div>
</template>

<script lang="ts">
import Vue from 'vue'
import { Component } from 'vue-property-decorator';
import FormRenderer from "@/components/form/FormRenderer.vue";
import { createForm, getClientFormDetails } from "@/components/form.api";
import {useStore} from "@/stores/store";
import {mapStores} from 'pinia';

export default {
  name: 'CMPForm',
  components: {
    FormRenderer,
  },
  data() {
    return {
      CONST_CREATE_BTN_SARA: 'saraBtn',
      CONST_CREATE_BTN_STABLE: 'stableBtn',
      CONST_CREATE_BTN_OVERALL: 'overallBtn',
      CONST_MODAL_ID_PREFIX: 'id_modal_create',
      formId: '',
      clientNum: '',
      CRNARating: '',
      SARARating: '',
      relatedClientFormId: null,
      formType: '',
      current_tab: 'tab-CRNA',
      items: [],
      dialog: false,
      formKey: 0,    
      isFormReadonly: false,
      formToCreate: ''
    }
  },
  mounted(){
    this.formId = this.$route.params.formID;
    this.clientNum = this.$route.params.csNumber;
    let enCoded = this.$route.params.param;
    // Unpack other params
    if (enCoded) {
        try {
            // base64 decode the string
            let printParamsString = atob(enCoded);
            let printParams = JSON.parse(printParamsString);
            this.CRNARating = printParams.CRNARating;
            this.SARARating = printParams.SARARating;
        } catch (err) {
            console.error("Cannot decode the params: ", err);
        }
    } else {
      this.CRNARating = '';
      this.SARARating = '';
    }
    this.getClientFormDetailsAPI(this.clientNum, this.formId);
  },
  methods: {
    updateTabKey(item) {
      let currentPath = this.$route.path;
      let pathArray = currentPath.split("/");
      let currentPathClientFormId = pathArray[pathArray.length - 1];

      // Reload the whole page if clicking on different tab, 
      // which resolves the issues with the whole DOM object not cleared when reload the same component with different param
      if (currentPathClientFormId != item.formId) {
        let r = this.$router.resolve({
          name: this.$ROUTER_NAME_CMPFORM, 
          params: {
            formID: item.formId,
            csNumber: this.clientNum
          }
        });
        window.location.assign(r.href)
      } else {
        item.key++;
      }
    },
    isReadonly(response) {
      //console.log("readonly check: ", response);
      // When form is locked, readonly is false if login user is sys admin, true otherwise
      if (response.locked) {
        if (this.mainStore.loginUserGroup == this.$USER_GROUP_ADMIN) {
          if (response.complete) {
            return true;
          }
          return false;
        } 
        return true;
      } else {
        // Edge case: if user doesn't have idirId, output a warning, and set readonly to true
        if (response.createdByIdir == null) {
          console.warn("The login user doesn't have idirId, set the access to readonly.");
          return true;
        }

        // When form is unlocked, readonly is true when:
        // 1. the form was created by someone else; OR
        // 2. the form is completed
        let isReadonly = false;
        if (response.createdByIdir != null && response.createdByIdir.toUpperCase() == Vue.$keycloak.tokenParsed.preferred_username.toUpperCase()) {
          isReadonly = response.complete;
        } else {
          isReadonly = true;
          if (this.mainStore.loginUserGroup == this.$USER_GROUP_ADMIN &&
              !response.complete) {
              isReadonly = false;
          }
        }
        return isReadonly;
      }
    },
    async getClientFormDetailsAPI(csNum, clientFormId) {
      const [error, response] = await getClientFormDetails(csNum, clientFormId);
      if (error) {
        console.error("Failed getting client form details", error);
      } else {
        this.private_getTabs(response);
      }
      this.formKey++;
    },
    private_getTabs(response) {
      this.formType = response.module;
      this.relatedClientFormId = response.relatedClientFormId;
      this.isFormReadonly = this.isReadonly(response);

      // if formType is 'CRNA', add 'CRNA-CMP' tab, and set the current_tab to 'tab-CRNA'
      if (this.formType === this.$CONST_FORMTYPE_CRNA) {
        this.items.push({ tab: 'CRNA-CMP', key: 0, id: this.$CONST_FORMTYPE_CRNA, formId: this.formId, relatedClientFormId: this.relatedClientFormId, 
                          readonly: this.isFormReadonly, locked: response.locked, createdByIdir: response.createdByIdir, canPrint: response.formTypeExpiryDate == null ? true : false });
        this.current_tab = 'tab-CRNA';
        if (!this.relatedClientFormId) {
          // show the 'add sara' btn if the crna form hasn't linked with sara
          if (!this.isFormReadonly) {
            this.items.push({ tab: '', key: 0, id: this.CONST_CREATE_BTN_SARA,    formId: '', relatedClientFormId: '', readonly: false, locked: false, createdByIdir: '', canPrint: true });
          }
        } else {
          // otherwise, show sara tab
          this.items.push({ tab: 'SARA', key: 0, id: this.$CONST_FORMTYPE_SARA, formId: this.relatedClientFormId, relatedClientFormId: this.formId, readonly: false, locked: false, createdByIdir: '', canPrint: true });
        }
        // show the 'add STABLE' btn 
        if (this.showSMOForms && !this.isFormReadonly) {
          this.items.push({ tab: '', key: 0, id: this.CONST_CREATE_BTN_STABLE,  formId: '', relatedClientFormId: '', readonly: false, locked: false, createdByIdir: '', canPrint: true });
        }
      } else
      // if formType is 'SARA', add 'SARA' tab, and set the current_tab to 'tab-SARA'
      if (this.formType === this.$CONST_FORMTYPE_SARA) {
        this.items.push({ tab: 'CRNA-CMP', key: 0, id: this.$CONST_FORMTYPE_CRNA, formId: this.relatedClientFormId, relatedClientFormId: this.formId, 
                          readonly: false, locked: false, createdByIdir: '', canPrint: true });
        this.items.push({ tab: 'SARA', key: 0, id: this.$CONST_FORMTYPE_SARA, formId: this.formId, relatedClientFormId: this.relatedClientFormId, 
                          readonly: this.isFormReadonly, locked: response.locked, createdByIdir: response.createdByIdir, canPrint: response.formTypeExpiryDate == null ? true : false });
        this.current_tab = 'tab-SARA';

        // show the 'add STABLE' btn 
        if (this.showSMOForms && !this.isFormReadonly) {
          this.items.push({ tab: '', key: 0, id: this.CONST_CREATE_BTN_STABLE,  formId: '', relatedClientFormId: '', readonly: false, locked: false, createdByIdir: '', canPrint: true });
        }
      } else
      // if formType is 'ACUTE', only show ACUTE tab
      if (this.showSMOForms && this.formType === this.$CONST_FORMTYPE_ACUTE) {
        this.items.push({ tab: 'ACUTE', key: 0, id: this.$CONST_FORMTYPE_ACUTE, formId: this.formId, relatedClientFormId: null, readonly: this.isFormReadonly, locked: response.locked, createdByIdir: response.createdByIdir, canPrint: response.formTypeExpiryDate == null ? true : false });
        this.current_tab = 'tab-ACUTE';
        if (!this.relatedClientFormId) {
          // show the 'add smo-overall' btn if the acute form hasn't linked with smo-overall
          if (!this.isFormReadonly) {
            this.items.push({ tab: '', key: 0, id: this.CONST_CREATE_BTN_OVERALL,  formId: '', relatedClientFormId: '', readonly: false, locked: false, createdByIdir: '', canPrint: true });
          }
        } else {
          // otherwise, show overall tab
          this.items.push({ tab: 'SMO-OVERALL', key: 0, id: this.$CONST_FORMTYPE_SO_OVERALL, formId: this.relatedClientFormId, relatedClientFormId: this.formId, readonly: false, locked: false, createdByIdir: '', canPrint: true });
        }
      } else
      // if formType is 'STAT99R', only show STAT99R tab
      if (this.showSMOForms && this.formType === this.$CONST_FORMTYPE_STAT99R) {
        this.items.push({ tab: 'STAT99R', key: 0, id: this.$CONST_FORMTYPE_STAT99R, formId: this.formId, relatedClientFormId: null, readonly: this.isFormReadonly, locked: response.locked, createdByIdir: response.createdByIdir, canPrint: response.formTypeExpiryDate == null ? true : false });
        this.current_tab = 'tab-STAT99R';
        if (!this.relatedClientFormId) {
          // show the 'add overall' btn if the acute form hasn't linked with overall
          if (!this.isFormReadonly) {
            this.items.push({ tab: '', key: 0, id: this.CONST_CREATE_BTN_OVERALL,  formId: '', relatedClientFormId: '', readonly: false, locked: false, createdByIdir: '', canPrint: true });
          }
        } else {
          // otherwise, show overall tab
          this.items.push({ tab: 'SMO-OVERALL', key: 0, id: this.$CONST_FORMTYPE_SO_OVERALL, formId: this.relatedClientFormId, relatedClientFormId: this.formId, readonly: false, locked: false, createdByIdir: '', canPrint: true });
        }
      } else
      // if formType is 'SMO-OVERALL', only show Overall tab
      if (this.showSMOForms && this.formType === this.$CONST_FORMTYPE_SO_OVERALL) {
        this.items.push({ tab: 'SMO-OVERALL', key: 0, id: this.$CONST_FORMTYPE_SO_OVERALL, formId: this.formId, relatedClientFormId: null, readonly: this.isFormReadonly, locked: response.locked, createdByIdir: response.createdByIdir, canPrint: response.formTypeExpiryDate == null ? true : false });
        this.current_tab = 'tab-OVERALL';
      } else
      // if formType is 'STABLE', only show STABLE tab
      if (this.showSMOForms && this.formType === this.$CONST_FORMTYPE_STABLE) {
        this.items.push({ tab: 'STABLE-CMP', key: 0, id: this.$CONST_FORMTYPE_STABLE, formId: this.formId, relatedClientFormId: null, readonly: this.isFormReadonly, locked: response.locked, createdByIdir: response.createdByIdir, canPrint: response.formTypeExpiryDate == null ? true : false });
        this.current_tab = 'tab-STABLE';
      } else {
        // unsupported formTypes
        this.items.push({ tab: this.formType, key: 0, id: this.formId, formId: this.formId, relatedClientFormId: null, readonly: this.isFormReadonly, locked: response.locked, createdByIdir: response.createdByIdir, canPrint: response.formTypeExpiryDate == null ? true : false });
        this.current_tab = 'tab-' + this.formType;
      }
    },
    async createChildFormAPI() {
      let formData = {};
      // set formData
      formData.clientNumber = this.clientNum;
      formData.linkedClientFormId = this.formId;

      const [error, newFormId] = await createForm(this.formToCreate.toLowerCase(), formData);
      if (error) {
        console.error("Failed creating new form instance: ", this.formToCreate, error);
      } else {
        this.items = [];
        this.formId = newFormId;
        this.getClientFormDetailsAPI(this.clientNum, newFormId);
      }
    },
    handleCreateChildFormBtnClick() {
      this.dialog = false;
      this.createChildFormAPI();
    },
    createChildForm(formType) {
      let modal = document.getElementById(this.CONST_MODAL_ID_PREFIX + formType);
      //console.log("modal: ", modal, formType);
      this.formToCreate = formType;
      if (modal != null) {
        modal.click();
      }
    }
  },
  computed: {
    showSMOForms() {
      return this.mainStore.isShowSMOForms();
    
    },
    // note we are not passing an array, just one store after the other
    // each store will be accessible as its id + 'Store', i.e., mainStore
    ...mapStores(useStore)
  }
}
</script>


