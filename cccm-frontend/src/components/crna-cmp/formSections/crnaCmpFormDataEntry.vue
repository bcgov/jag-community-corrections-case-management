<template>
  <div>
    <Form :key="pageKey" :form="dataModel" :submission="initData" 
      v-on:editGridSaveRow="handleEditGridSaveRowEvent"
      v-on:change="handleChangeEvent" 
      v-on:blur="handleBlurEvent" 
      @evt_save="handleSave" 
      @evt_cancel="handleCancelForm"
    />
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import { objectToString } from '@vue/shared';
import {updateForm} from "@/components/form.api";

export default {
  name: 'saraCmpForm',
  props: {
    dataModel: {},
    initData: {},
    dataMap: {},
    saveBtnLabel: '',
    notifySaveDraft: 0,
  },
  components: {
    Form
  },
  data() {
    return {
      CONST_EDITGRID: 'editgrid',
      CONST_DATAGRID: 'datagrid',
      CONST_INTERVENTIONDESCRIPTION: 'interventionDescription',
      CONST_INTERVENTIONTYPE: 'interventionType',
      CONST_INTERVENTIONTYPEOTHER: 'interventionTypeOther',
      CONST_KEY_SAVE_BTN: 'button_save',
      pageKey: 0,
      saveDraft: false,
      latestKey: '',
      latestValue: '',
      latestData: {}
    }
  },
  watch: {
    saveBtnLabel() {
      this.private_updateSaveBtnLabel();
    },
    notifySaveDraft() {
      let btn = this.private_getSaveBtn();
      if (btn != null) { 
        //console.log("Simulate the btn click: ", btn);
        this.saveDraft = true;
        btn.click(); 
      }
    }
  },
  methods: {
    async updateForm(formData) {
      let formId= this.$route.params.formID;
      const [error, response] = await updateForm(formData);
      if (error) {
        console.error("Form update failed: ", error);
      }
    },
    private_getSaveBtn() {
      let theBtn = null;
      let className = '[class*="' + this.CONST_KEY_SAVE_BTN + '"]';
      let thePanel = document.querySelector(className);
      if (thePanel != null) {
        let typeName = '[type=button]';
        theBtn = thePanel.querySelector(typeName);
      }
      return theBtn;
    },
    private_updateSaveBtnLabel() {
      let theBtn = this.private_getSaveBtn();
      if (theBtn != null && theBtn.childNodes != null && theBtn.childNodes[0] != null) {
        theBtn.childNodes[0].nodeValue = this.saveBtnLabel;
      }
    },
    handleEditGridSaveRowEvent(event) {
      if (event != null && event.row != null) {
        console.log("datagrid radio text list event data: ", this.initData.data, 
        event.component.key, event.row,
        event.row.radio, event.row.comments);

        let keys = Object.keys(event.row);
        //console.log("keys: ", keys);

        // look for the data key 
        if (keys != null && keys.length > 0) {
          for (let i = 0; i < keys.length; i++) {
            let dataKey = this.dataMap[event.component.key + "." + keys[i]];
            //console.log("dataKey index: ", event.component.key + "." + keys[i]);
            // found the data key, update the initData
            if (dataKey != null) {
              //console.log("dataKey: ", dataKey);
              this.initData.data[dataKey] = event.row[keys[i]];
            }
          }
        }
      }
      
    },
    handleChangeEvent(event) {
      if (   event.changed 
          && ( event.changed.component.type === "textfield"
            || event.changed.component.type === "textarea")) {
        console.log("textfield or textarea changed: ", event);
        // if data mapping required, keep the latest values
        if (this.private_isDataMappingRequired(event.changed.instance, event.changed.component.key)) {
          //console.log("not part of editgrid");
          this.latestKey = event.changed.component.key;
          this.latestValue = event.changed.value;  
          this.latestData = event.data;  
          this.triggerAutoSave = true;
          //this.private_updateMappedData(event, this.latestKey, this.latestValue, this.latestData);
        }
      }

      // Trigger auto save
      if (   event.changed 
          && ( event.changed.component.type === "radio"
            || event.changed.component.type === "checkbox"
            || event.changed.component.type === "select")
          ) {
        console.log("radio, checkbox or select changed: ", event);
        // if the radio button or checkbox or select is NOT part of an editgrid, call dataMapping function
        if (this.private_isDataMappingRequired(event.changed.instance, event.changed.component.key)) {
          //console.log("radio not part of editgrid");
          this.private_updateMappedData(event.changed.instance, event.changed.component.key, event.changed.value, event.data);
        }
      }
    },
    handleBlurEvent(event) {
      if (this.triggerAutoSave) {
        this.triggerAutoSave = false;
        this.private_updateMappedData(event, this.latestKey, this.latestValue, this.latestData);
        // Refresh the view
        this.pageKey++;
      }
    },
    handleSave(evt) {
      //console.log("save evt: ", evt);
      if (evt != null && evt.data != null) {
        if (this.saveDraft) {
          this.updateForm(evt.data);
          this.saveDraft = false;
        } else {
          // emit an event, saveContinueClicked with setting true to flag "continue to next section", to the parent, so parent knows it's time to save data
          this.$emit('saveContinueClicked', evt.data);
        }
      } 
    },
    handleCancelForm(evt) {
      //console.log("cancel evt: ", evt);
      // emit an event, cancelFormClicked, to the parent, so parent knows it's time to cancel form
      if (evt != null && evt.type === 'evt_cancel') {
        this.$emit('cancelFormClicked');
      }
    },
    private_isDataMappingRequired(theInstance, componentKey) {
      // if it's part of editgrid, return false;
      if (theInstance != null 
          && theInstance.parent != null 
          && theInstance.parent.parent != null 
          && theInstance.parent.parent.type === this.CONST_EDITGRID) {
        return false;
      }
      // If the component key is the dataMap
      if (this.private_isDataMappingExistsHelper(componentKey)) {
        return true;
      }
      // If it's part of the datagrid and dataMap exists for the datagrid, return true;
      let dataGridKey = this.private_isPartOfDatagrid(theInstance);
      //console.log("datagridKey: ", dataGridKey);
      return this.private_isDataMappingExistsHelper(dataGridKey);
    },
    private_isDataMappingExistsHelper(theKey) {
      if (theKey == null) {
        return false;
      }
      let dataMapKeyObj = this.dataMap[theKey];
      if (dataMapKeyObj != null) {
        for (let i = 0; i < dataMapKeyObj.length; i++) {
          let dataKey = dataMapKeyObj[i];
          if (dataKey != null) {
            return true;
          }
        }
      }
      return false;
    },
    private_isPartOfDatagrid(theInstance) {
      let datagridKey = null;
      if (theInstance != null 
          && theInstance.parent != null 
          && theInstance.parent.parent != null 
          && theInstance.parent.parent.type === this.CONST_DATAGRID) {
        datagridKey = theInstance.parent.parent.key;
      } else if ( theInstance.parent.parent.parent != null 
          && theInstance.parent.parent.parent.type === this.CONST_DATAGRID) {
        datagridKey = theInstance.parent.parent.parent.key;
      }
      return datagridKey;
    },
    private_updateMappedData(instance, theKey, newValue, latestData) {
      //console.log("try update mapped data: ", instance, theKey, newValue, latestData);
      console.log("latestData: ", latestData);
      if (instance != null) {
        // it's part of a datagrid, do the updates accordingly
        let datagridKey = this.private_isPartOfDatagrid(instance);
        if (datagridKey != null) {
          console.log("it's part of a datagrid, instance: ,theKey: , newValue: ", instance, theKey, newValue, latestData);
          if (latestData != null) {
            // get the newValue of the dataGrid
            // sample dataGridValue: 
            // [
            //   {
            //       "interventionType": "type1",
            //       "interventionTypeOther": "",
            //       "interventionDescription": "some comments for S0Q0 type 1 sss",
            //       "questionLabel": "Family Relationships"
            //   },
            //   {
            //       "interventionType": "other",
            //       "interventionTypeOther": "S0Q0 type",
            //       "interventionDescription": "some comments for S0Q0 type other",
            //       "questionLabel": "Family Relationships"
            //   }
            // ]
            let dataGridValue = latestData[datagridKey];
            //console.log("dataGridValue: ", dataGridValue, dataGridValue.length);
            // Let's do the data mapping
            if (dataGridValue != null && dataGridValue.length > 0) {
              for (let i = 0; i < dataGridValue.length; i++) {
                let keys = Object.keys(dataGridValue[i]);
                //console.log("dataGridValue[i] keys: ", dataGridValue[i], i, keys);

                // look for the data key 
                if (keys != null && keys.length > 0) {
                  for (let k = 0; k < keys.length; k++) {
                    //console.log("keys[k]: ", keys[k]);

                    // get the mapped key object
                    // sample dataMapKeyObj: ["summary_S3Q00", "S3Q00"]
                    let dataMapKeyObj = this.dataMap[datagridKey];
                    if (dataMapKeyObj != null) {
                      for (let j = 0; j < dataMapKeyObj.length; j++) {
                        //console.log("mapped datakey: ", dataMapKeyObj[j]);
                        if (dataMapKeyObj[j] != null) {
                          // "summary_S3Q00": [
                          //   {
                          //     "questionLabel": "Family Relationships",
                          //     "interventionType": "type 1",
                          //     "comments": "Sample comment for S0Q0",
                          //     "interventionDescription": "some comments for S0Q0 type 1"
                          //   },
                          //   {
                          //     "questionLabel": "Family Relationships",
                          //     "interventionType": "S0Q0 type",
                          //     "comments": "Sample comment for S0Q0",
                          //     "interventionDescription": "some comments for S0Q0 type other"
                          //   }
                          // ]
                          // Overwrite the interventionType value if interventionTypeOther is not empty
                          //console.log("j, i, k, this.initData.data[dataMapKeyObj[j]][i], dataGridValue[i][keys[k]]: ", j, i, k, keys[k], this.initData.data[dataMapKeyObj[j]][i], dataGridValue[i][keys[k]]);
                          if (this.initData.data[dataMapKeyObj[j]][i] == null) {
                            this.initData.data[dataMapKeyObj[j]][i] = {};
                          }
                          if (keys[k] === this.CONST_INTERVENTIONTYPEOTHER && dataGridValue[i][keys[k]] != '') {
                            this.initData.data[dataMapKeyObj[j]][i][this.CONST_INTERVENTIONTYPE]=dataGridValue[i][keys[k]];
                          } else {
                            this.initData.data[dataMapKeyObj[j]][i][keys[k]]=dataGridValue[i][keys[k]];
                          }
                        }
                      }
                    }
                  }
                }
              }
              //console.log("mapped data summary_S3Q00: ", this.initData.data["summary_S3Q000"]);
              //console.log("mapped data S3Q00: ", this.initData.data["S3Q000"]);
            }
          }
        } else {
          let dataMapKeyObj = this.dataMap[theKey];
          // sample dataMapKeyObj: ["summary_S0Q0.radio"]
          if (dataMapKeyObj != null) {
            for (let i = 0; i < dataMapKeyObj.length; i++) {
              let dataKey = dataMapKeyObj[i];
              if (dataKey != null) {
                //console.log("dataKey: ", dataKey);
                // sample dataKey: "summary_S0Q0.radio"
                let dataKeySplit = dataKey.split(".");
                if (dataKeySplit != null && dataKeySplit.length == 2) {
                  let sectionID = dataKeySplit[0];
                  let questionID = dataKeySplit[1];
                  //console.log("dataKey value: ", this.initData.data[sectionID][0][questionID]);
                  this.initData.data[sectionID][0][questionID] = newValue;
                }
              }
            }
          }
        }
      }
    }
  }
}
</script>