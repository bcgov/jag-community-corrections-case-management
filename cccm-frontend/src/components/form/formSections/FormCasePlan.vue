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
      CONST_MAX_RETRY: 5,
      CONST_CONTAINER: 'container',
      keyCaseplan: 0,
      loading: false,
      autoSaveData: {},
      autoSaveDataCandidate: {},
      saving: false,
    }
  },
  mounted() {
    // reset the indicator
    this.saving = false;

    // fetch interventions 
    this.getCasePlanInterventionAPI();
  },
  methods: {
    async autoSave() {
      //only start saving if previous saving is done
      if (!this.saving && Object.keys(this.autoSaveDataCandidate).length > 0) {
        // deep copy of autoSaveDataCandidate, and assign it to autoSaveData 
        this.autoSaveData = JSON.parse(JSON.stringify(this.autoSaveDataCandidate));

        //clear this.autoSaveDataCandidate, so we are not repeatedly saving it
        this.autoSaveDataCandidate = {};
        
        // Repeat the saving till it succeeds
        let savingCnt = 0;
        while(savingCnt < this.CONST_MAX_RETRY) {
          try {
            this.saving = true;
            const [error, response] = await updateForm(this.csNumber, this.clientFormId, this.autoSaveData);
            if (error) {
              console.error(error);
            } else {
              this.saving = false;
              break;
            }
          } catch (err) {
            console.error("Saving case plan data failed %o", err);
          } 
        }
        // Saving failed after CONST_MAX_RETRY where this.saving flag didn't set to true, do the following:
        // 1. Add payload back to the autoSaveDataCandidate, so we don't miss out data saving.
        // 2. Set the this.saving flag to false so autosave can continue
        if (this.saving) {
          this.private_mergePayload(this.autoSaveData);
          this.saving = false;
          console.error("Auto save failed after " + this.CONST_MAX_RETRY + "times");
        }
      }
    },
    private_mergePayload(source) {
      for (let i = 0; i < Object.keys(source); i++) {
        let key = Object.keys(source)[i];
        if (this.autoSaveDataCandidate[key] == null) {
          this.autoSaveDataCandidate[key] = source[key];
        }
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
        let containerKey = this.private_isPartOfContainer(event.changed.instance);
        let theKey = event.changed.component.key;
        if (containerKey != null) {
          theKey = containerKey;
        } 
        this.private_addToAutoSaveDataCandidate(theKey, event.data, false);
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
        this.private_addToAutoSaveDataCandidate(theKey, event.data, true);
      }
    },
    handleBlurEvent(event) {
      //console.log("From blur event, this.autoSaveDataCandidate: ", this.autoSaveDataCandidate);
      this.autoSave();
    },
    private_addToAutoSaveDataCandidate(key, eventData, autoSaveNow) {
      this.autoSaveDataCandidate[key] = eventData[key];
      if (autoSaveNow) {
        this.autoSave();
      }
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