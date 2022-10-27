<template>
    <!--Case plan section-->
    <div>
        <v-progress-linear v-if="loading" indeterminate height="30" color="primary">Loading case plan</v-progress-linear>
        <Form v-if="!loading" 
          :key="keyCaseplan" 
          :form="dataModel" 
          :submission="initData" 
          v-on:change="handleChangeEvent" 
          v-on:blur="handleBlurEvent" />
    </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import {updateForm, getCasePlanIntervention} from "@/components/form.api";

export default {
  name: 'FormCasePlan',
  props: {
    dataModel: {},
    initData: {},
    clientFormId: 0,
    csNumber: ''
  },
  components: {
    Form
  },
  data() {
    return {
        CONST_CONTAINER: 'container',
        keyCaseplan: 0,
        loading: false,
        autoSaveData: {},
        autoSaveDataCandidate: {},
    }
  },
  mounted() {
    console.log("caseplan mounted");
    // reset the indicator
    this.savingSuccess = false;
    this.saving = false;

    // fetch interventions 
    this.getCasePlanInterventionAPI();
  },
  methods: {
    async autoSave() {
      //console.log("autosave");
      //only start saving if previous saving is done
      //console.log("this.autoSaveDataCandidate: ", this.autoSaveDataCandidate);
      if (!this.saving && Object.keys(this.autoSaveDataCandidate).length > 0) {
        this.autoSaveData = JSON.parse(JSON.stringify(this.autoSaveDataCandidate));
        console.log("autosave data: ", this.autoSaveData);
        //clear this.autoSaveDataCandidate, so we are not repeatedly saving it
        this.autoSaveDataCandidate = {};
        
        // Repeat the saving till it succeeds
       //while(!this.savingSuccess) {
          try {
            this.saving = true;
            this.savingSuccess = true;
            this.saving = false;
            const [error, response] = await updateForm(this.csNumber, this.clientFormId, this.autoSaveData);
            if (error) {
              console.error(error);
            } else {
              this.savingSuccess = true;
              this.saving = false;
            }
          } catch (err) {
            console.error("Saving case plan data failed %o", err);
          } 
       // }
      }
    },
    async getCasePlanInterventionAPI() {
        this.loading = true;
        const [error, interventionData] = await getCasePlanIntervention(this.csNumber, this.clientFormId, true);
        if (error) {
            console.error(error);
        } else {
            this.loading = false;
            // add Intervention data to the initData; refresh the page to show it
            this.initData.data.interventions = interventionData;
            //console.log("interventionData; ", this.initData.data.interventions);
            this.keyCaseplan++;
        }
    },
    handleChangeEvent(event) {
      if (   event.changed 
          && ( event.changed.component.type === "textfield"
            || event.changed.component.type === "textarea")) {
        //console.log("textfield or textarea changed: ", event, event.data, event.changed.component.key, event.changed.value);
        // don't trigger the autosave on every key stroke, keep the latest values for now.
        // Trigger the autosave when blur event occurs.
        console.log("textarea changed: ", event.data);
        let containerKey = this.private_isPartOfContainer(event.changed.instance);
        let theKey = event.changed.component.key;
        if (containerKey != null) {
          //console.log("part of a containter: ", containerKey);
          theKey = containerKey;
        } 
        this.private_addToAutoSaveDataCandidate(theKey, event.data);
      }

      // Trigger autosave
      if (   event.changed 
          && ( event.changed.component.type === "radio"
            || event.changed.component.type === "checkbox"
            || event.changed.component.type === "select")
          ) {
        // if the radio button or checkbox or select is part of an datagrid, map the datagrid data to this.autoSaveDataCandidate
        let containerKey = this.private_isPartOfContainer(event.changed.instance);
        let theKey = event.changed.component.key;
        if (containerKey != null) {
          theKey = containerKey;
        } 
        this.private_addToAutoSaveDataCandidate(theKey, event.data);
      }
    },
    handleBlurEvent(event) {
      //console.log("From blur event, this.autoSaveDataCandidate: ", this.autoSaveDataCandidate);
      this.autoSave();
    },
    private_addToAutoSaveDataCandidate(key, eventData) {
      this.autoSaveDataCandidate[key] = eventData[key];
      console.log("candidate after: ", this.autoSaveDataCandidate);
    },
    private_isPartOfContainer(theInstance) {
      //console.log("check partof dataGrid: ", theInstance);
      let containerKey = null;
      if (theInstance != null 
          && theInstance.parent != null
          && theInstance.parent.type === this.CONST_CONTAINER) {
        containerKey = theInstance.parent.key;
      } 
      return containerKey;
    }
  }
}
</script>