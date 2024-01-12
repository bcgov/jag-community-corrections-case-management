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
      <v-dialog
          v-model="dialog"
          persistent
          max-width="550"
        >
        <v-card>
          <v-card-title v-if="formToCreate == $CONST_FORMTYPE_SARA" class="text-h5">
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
      // When form is completed, set readonly to true
      if (response.complete) {
        return true;
      }

      // when form is locked and the user if not admin, set readonly to true
      if (response.locked && this.mainStore.loginUserGroup != this.$USER_GROUP_ADMIN) {
        return true;
      }

      // when form is not locked and the user if not admin nor owner, set readonly to true
      if (!response.locked && this.mainStore.loginUserGroup != this.$USER_GROUP_ADMIN && 
           response.createdByIdir != null && 
           response.createdByIdir.toUpperCase() != Vue.$keycloak.tokenParsed.preferred_username.toUpperCase()) {
        return true;
      }

      //If login user is in Admin-comm group, set readonly to true
      if (this.mainStore.loginUserGroup == this.$USER_GROUP_ADMIN_COMM) {
        return true;
      }
      
      return false;
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
    setPrintFlag(formTypeExpiryDate) {
      if (this.mainStore.loginUserGroup == this.$USER_GROUP_ADMIN_COMM) {
        return false;
      }
      if (formTypeExpiryDate == null) {
        return true;
      }
      return false;
    },
    private_getTabs(response) {
      this.formType = response.module;
      this.relatedClientFormId = response.relatedClientFormId;
      this.isFormReadonly = this.isReadonly(response);

      // if formType is 'CRNA', add 'CRNA-CMP' tab, and set the current_tab to 'tab-CRNA'
      if (this.formType === this.$CONST_FORMTYPE_CRNA) {
        this.items.push({ tab: 'CRNA-CMP', key: 0, id: this.$CONST_FORMTYPE_CRNA, formId: this.formId, relatedClientFormId: this.relatedClientFormId, 
                          readonly: this.isFormReadonly, locked: response.locked, createdByIdir: response.createdByIdir, canPrint: this.setPrintFlag(response.formTypeExpiryDate) });
        this.current_tab = 'tab-CRNA';
        if (!this.relatedClientFormId) {
          // show the 'add sara' btn if the crna form hasn't linked with sara
          if (!this.isFormReadonly) {
            this.items.push({ tab: '', key: 0, id: this.CONST_CREATE_BTN_SARA, formId: '', relatedClientFormId: '', readonly: false, locked: false, createdByIdir: '', canPrint: true });
          }
        } else {
          // otherwise, show sara tab
          this.items.push({ tab: 'SARA', key: 0, id: this.$CONST_FORMTYPE_SARA, formId: this.relatedClientFormId, relatedClientFormId: this.formId, readonly: false, locked: false, createdByIdir: '', canPrint: true });
        }
      } else
      // if formType is 'SARA', add 'SARA' tab, and set the current_tab to 'tab-SARA'
      if (this.formType === this.$CONST_FORMTYPE_SARA) {
        this.items.push({ tab: 'CRNA-CMP', key: 0, id: this.$CONST_FORMTYPE_CRNA, formId: this.relatedClientFormId, relatedClientFormId: this.formId, 
                          readonly: false, locked: false, createdByIdir: '', canPrint: true });
        this.items.push({ tab: 'SARA', key: 0, id: this.$CONST_FORMTYPE_SARA, formId: this.formId, relatedClientFormId: this.relatedClientFormId, 
                          readonly: this.isFormReadonly, locked: response.locked, createdByIdir: response.createdByIdir, canPrint: this.setPrintFlag(response.formTypeExpiryDate) });
        this.current_tab = 'tab-SARA';
      } else
      // if formType is 'ACUTE', only show ACUTE tab
      if (this.formType === this.$CONST_FORMTYPE_ACUTE) {
        this.items.push({ tab: 'Acute', key: 0, id: this.$CONST_FORMTYPE_ACUTE, formId: this.formId, relatedClientFormId: null, readonly: this.isFormReadonly, locked: response.locked, createdByIdir: response.createdByIdir, canPrint: this.setPrintFlag(response.formTypeExpiryDate) });
        this.current_tab = 'tab-ACUTE';
      } else
      // if formType is 'STAT99R', only show STAT99R tab
      if (this.formType === this.$CONST_FORMTYPE_STAT99R) {
        this.items.push({ tab: 'Static-99R', key: 0, id: this.$CONST_FORMTYPE_STAT99R, formId: this.formId, relatedClientFormId: null, readonly: this.isFormReadonly, locked: response.locked, createdByIdir: response.createdByIdir, canPrint: this.setPrintFlag(response.formTypeExpiryDate) });
        this.current_tab = 'tab-STAT99R';
      } else
      // if formType is 'SMO-OVERALL', only show Overall tab
      if (this.formType === this.$CONST_FORMTYPE_SO_OVERALL) {
        this.items.push({ tab: 'SMO-Overall-CMP', key: 0, id: this.$CONST_FORMTYPE_SO_OVERALL, formId: this.formId, relatedClientFormId: null, readonly: this.isFormReadonly, locked: response.locked, createdByIdir: response.createdByIdir, canPrint: this.setPrintFlag(response.formTypeExpiryDate) });
        this.current_tab = 'tab-OVERALL';
      } else
      // if formType is 'STABLE', only show STABLE tab
      if (this.formType === this.$CONST_FORMTYPE_STABLE) {
        this.items.push({ tab: 'Stable', key: 0, id: this.$CONST_FORMTYPE_STABLE, formId: this.formId, relatedClientFormId: null, readonly: this.isFormReadonly, locked: response.locked, createdByIdir: response.createdByIdir, canPrint: this.setPrintFlag(response.formTypeExpiryDate) });
        this.current_tab = 'tab-Stable';
      } else {
        // unsupported formTypes
        this.items.push({ tab: this.formType, key: 0, id: this.formId, formId: this.formId, relatedClientFormId: null, readonly: this.isFormReadonly, locked: response.locked, createdByIdir: response.createdByIdir, canPrint: this.setPrintFlag(response.formTypeExpiryDate) });
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
      this.formToCreate = formType;
      if (modal != null) {
        modal.click();
      }
    }
  },
  computed: {
    // note we are not passing an array, just one store after the other
    // each store will be accessible as its id + 'Store', i.e., mainStore
    ...mapStores(useStore)
  }
}
</script>


