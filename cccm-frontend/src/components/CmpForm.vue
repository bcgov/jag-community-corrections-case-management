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
    </div>

    <section class="pr-4 pl-4">
      <v-tabs v-model="current_tab" fixed-tabs color="deep-purple accent-4">
        <v-tab v-for="item in items" :key="item.id" :href="'#tab-' + item.id" @click="updateTabKey(item)" > 
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
          <FormRenderer v-if="item.id == $CONST_FORMTYPE_CRNA" :key="CRNATabKey" :formType="item.id" :formId="item.formId" :csNumber="clientNum" :relatedClientFormId="item.relatedClientFormId" :readonly="item.readonly"></FormRenderer>
          <FormRenderer v-if="item.id == $CONST_FORMTYPE_SARA" :key="SARATabKey" :formType="item.id" :formId="item.formId" :csNumber="clientNum" :relatedClientFormId="item.relatedClientFormId" :readonly="item.readonly"></FormRenderer>
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
      current_tab: 'tab-CRNA',
      items: [],
      dialog: false,
      formKey: 0,    
      CRNATabKey: 0,  
      SARATabKey: 0,
    }
  },
  mounted(){
    this.formId = this.$route.params.formID;
    this.clientNum = this.$route.params.csNumber;
    //console.log("cmpform mounted: ", this.formId, this.clientNum);
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
          name: "cmpform", 
          params: {
            formID: item.formId,
            csNumber: this.clientNum
          }
        });
        window.location.assign(r.href)
      } else {
        if (item.id == this.$CONST_FORMTYPE_CRNA) {
          this.CRNATabKey++;
        }
        if (item.id == this.$CONST_FORMTYPE_SARA) {
          this.SARATabKey++;
        }
      }
    },
    async getClientFormDetailsAPI(csNum, clientFormId) {
      const [error, response] = await getClientFormDetails(csNum, clientFormId);
      if (error) {
        console.error("Failed getting client form details", error);
      } else {
        //console.log("Form details: ", response);
        this.formType = response.module;
        this.relatedClientFormId = response.relatedClientFormId;
        
        // if formType is 'CRNA', add 'CRNA-CMP' tab, and set the current_tab to 'tab-CRNA'
        if (this.formType === this.$CONST_FORMTYPE_CRNA) {
          this.items.push({ tab: 'CRNA-CMP', id: this.$CONST_FORMTYPE_CRNA, formId: this.formId, relatedClientFormId: this.relatedClientFormId, readonly: response.complete});
          this.current_tab = 'tab-CRNA';
          if (!this.relatedClientFormId) {
            // show the 'add sara' btn if the crna form hasn't linked with sara
            this.items.push({ tab: '', id: 'saraBtn', formId: '', relatedClientFormId: '', readonly: false});
          } else {
            // otherwise, show sara tab
            this.items.push({ tab: 'SARA-CMP', id: this.$CONST_FORMTYPE_SARA, formId: this.relatedClientFormId, relatedClientFormId: this.formId, readonly: false });
          }
        }
        // if formType is 'SARA', add 'SARA-CMP' tab, and set the current_tab to 'tab-SARA'
        if (this.formType === this.$CONST_FORMTYPE_SARA) {
          this.items.push({ tab: 'CRNA-CMP', id: this.$CONST_FORMTYPE_CRNA, formId: this.relatedClientFormId, relatedClientFormId: this.formId, readonly: false });
          this.items.push({ tab: 'SARA-CMP', id: this.$CONST_FORMTYPE_SARA, formId: this.formId, relatedClientFormId: this.relatedClientFormId, readonly: response.complete });
          this.current_tab = 'tab-SARA';
        }
      }
      this.formKey++;
    },
    async createSARAFormAPI() {
      let formData = {};
      // set formData
      formData.clientNumber = this.clientNum;
      formData.linkedClientFormId = this.formId;
      const [error, SARAFormId] = await createSARAForm(formData);
      if (error) {
        console.error("Failed creating SARA form instance", error);
      } else {
        //console.info("SARA form instance created: ", SARAFormId);
        this.items = [];
        this.formId = SARAFormId;
        this.getClientFormDetailsAPI(this.clientNum, SARAFormId);
      }
    },
    handleCreateSARAFormBtnClick() {
      this.dialog = false;
      this.createSARAFormAPI();
    },
    createSARA() {
      let modal = document.getElementById("id_modal_createSARAForm");
      if (modal != null) {
        modal.click();
      }
    }
  }
}
</script>


