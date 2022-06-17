<template>
    <div>
      <div v-for="(header, indexp) in dataModel.data" 
          :id="`${indexp}`"
          :key="indexp">
        <div v-for="(headerc, indexc) in header.subsections" :key="indexc">
          <div :id="`${indexp}${indexc}`" class="formio_anchor_class">
            <FormioQuestionCombo v-if="headerc.type === 'questionCombo'" :key=Key_questionCombo @dataOnChanged="handleQuestionComboDataOnChanged" :dataModel="headerc" :initData="initData_questionCombo[headerc.key]"/>
            <FormioLabelTextarea v-else-if="headerc.type === 'labelTextarea'" :dataModel="headerc" />
            <FormioRadio v-else-if="headerc.type === 'radio'" :dataModel="headerc" />
            <FormioQuestionCetegoryTitle v-else-if="headerc.type === 'sectionTitle'" :dataModel="headerc"/>
            <FormioRadioTextarea v-else-if="headerc.type === 'radioTextarea'" :dataModel="headerc"/>
            <FormioEditDataGrid v-else-if="headerc.type === 'editGrid'" @dataOnChanged="handleInterventionDataGridOnChanged" :dataTemplate="headerc" :key=key_intervention :dataModel="dataModel.data" :initData="initData_intervention"/>
          </div>
        </div>
      </div>
    </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { ref, reactive } from '@vue/composition-api';
import FormioQuestionCombo from "@/components/common/FormioQuestionCombo.vue";
import FormioLabelTextarea from "@/components/common/FormioLabelTextArea.vue";
import FormioRadio from "@/components/common/FormioRadio.vue";
import FormioQuestionCetegoryTitle from "@/components/common/FormioQuestionCategoryTitle.vue";
import FormioRadioTextarea from "@/components/common/FormioRadioTextarea.vue";
import FormioButton from "@/components/common/FormioButton.vue";
import FormioEditDataGrid from "@/components/common/FormioEditDataGrid.vue";

export default {
  name: 'CrnaCmpFormDataEntry',
  props: {
    dataModel: {},
  },
  components: {
    FormioQuestionCombo,
    FormioLabelTextarea,
    FormioRadio,
    FormioQuestionCetegoryTitle,
    FormioRadioTextarea,
    FormioButton,
    FormioEditDataGrid,
  },
  data() {
    return {
      key_intervention: 0,
      initData_intervention: {"data": {}},
      Key_questionCombo: 0,
      initData_questionCombo: [],
      questionComboData: {},
      questionComboIndex: 0,
    }
  },
  mounted(){
    this.getInitData();
  },
  methods: {
    handleInterventionDataGridOnChanged(dataValue) {
      // Changes made to intervention list in 'Case plan', need to repopulate the intervention in 'Needs Assessement'
      //Sample dataValue, should only contain 1 object
      // {
      //   "radioButton": "P",
      //   "hidden_key": "quetionCombo_fr",
      //   "comments": "",
      //   "key_checkbox": true,
      //   "questionLabel": "Family Relationships",
      //   "key_itv_type": "type1",
      //   "key_itv_description": "some comments for type 1",
      // }
      
      // Update this.initData_questionCombo
      this.initData_questionCombo[dataValue.hidden_key].data.comments = dataValue.comments;
      for (let k = 0; k < this.initData_questionCombo[dataValue.hidden_key].data.key_itvDataGrid.length; k++) {
        if (this.initData_questionCombo[dataValue.hidden_key].data.key_itvDataGrid[k].key_itv_type === dataValue.key_itv_type 
          || this.initData_questionCombo[dataValue.hidden_key].data.key_itvDataGrid[k].key_itv_other === dataValue.key_itv_type) {
          // replace the 'key_itv_description' value
          this.initData_questionCombo[dataValue.hidden_key].data.key_itvDataGrid[k].key_itv_description = dataValue.key_itv_description;
        }
      }
      // force FormioQuestionCombo component to reload
      this.Key_questionCombo++;
    },
    handleQuestionComboDataOnChanged(dataValue) {
      // Sample dataValue when checkbox is checked
      // {
      //   "radioButton": "P",
      //   "hidden_key": "quetionCombo_fr",
      //   "comments": "",
      //   "key_checkbox": true,
      //   "questionLabel": "Family Relationships"
      //   "key_itvDataGrid": [
      //     {
      //       "key_itv_type": "",
      //       "key_itv_other": "",
      //       "key_itv_description": ""
      //     }
      //   ]
      // }
      // Sample dataValue when checkbox is unchecked
      // {
      //   "radioButton": "P",
      //   "hidden_key": "quetionCombo_fr",
      //   "comments": "",
      //   "key_checkbox": false,
      //   "questionLabel": "Family Relationships"
      // }

      // Sample data for this.questionComboData
      // {
      //   "questionCombo": [
      //     {
      //       "radioButton": "P",
      //       "hidden_key": "quetionCombo_fr",
      //       "comments": "",
      //       "key_checkbox": true,
      //       "key_itvDataGrid": [
      //         {
      //           "key_itv_type": "type1",
      //           "key_itv_other": "",
      //           "key_itv_description": "type 1 desc"
      //         },
      //         {
      //           "key_itv_type": "other",
      //           "key_itv_description": "abc desc",
      //           "key_itv_other": "abc"
      //         }
      //       ],
      //       "questionLabel": "Family Relationships"
      //     }
      //   ]
      // }
      if (dataValue != null) {
        if (this.questionComboData.questionCombo == null) {
          this.questionComboData.questionCombo = [];
        } 
        
        let found = false;
        for (let i = 0; i < this.questionComboData.questionCombo.length; i++) {
          if (this.questionComboData.questionCombo[i].hidden_key === dataValue.hidden_key) {
            found = true;
            // if key_checkbox is checked, replace the existing value with the new one
            if (dataValue.key_checkbox) {
              this.questionComboData.questionCombo[i] = dataValue;
            } else {
              // if key_checkbox is unchecked, remove the item from the array
              this.questionComboData.questionCombo.splice(i, 1);
              this.questionComboIndex--;
            }
            break;
          }
        }
        if (!found && dataValue.key_checkbox) {
          this.questionComboData.questionCombo[this.questionComboIndex++] = dataValue;
        }
      }

      // rebuild initData_intervention from questionComboData
      this.initData_intervention = {"data": {}};
      let index = 0;
      if (   this.questionComboData != null 
          && this.questionComboData.questionCombo != null 
          && this.questionComboData.questionCombo.length > 0) {
        for (let i = 0; i < this.questionComboData.questionCombo.length; i++) {
          if (   this.questionComboData.questionCombo[i] != null 
              && this.questionComboData.questionCombo[i].key_itvDataGrid != null 
              && this.questionComboData.questionCombo[i].key_itvDataGrid.length > 0) {
            for (let j = 0; j < this.questionComboData.questionCombo[i].key_itvDataGrid.length; j++) {
              if (this.initData_intervention.data.questionCombo == null) {
                this.initData_intervention.data.questionCombo = [];
              }
              let questionComboItem = {};
              questionComboItem.radioButton = this.questionComboData.questionCombo[i].radioButton;
              questionComboItem.hidden_key = this.questionComboData.questionCombo[i].hidden_key;
              questionComboItem.comments = this.questionComboData.questionCombo[i].comments;
              questionComboItem.key_checkbox = this.questionComboData.questionCombo[i].key_checkbox;
              questionComboItem.questionLabel = this.questionComboData.questionCombo[i].questionLabel;
              // if key_itv_type value is 'other', use the value of key_itv_other
              questionComboItem.key_itv_type = this.questionComboData.questionCombo[i].key_itvDataGrid[j].key_itv_type;
              if (this.questionComboData.questionCombo[i].key_itvDataGrid[j].key_itv_type === "other") {
                questionComboItem.key_itv_type = this.questionComboData.questionCombo[i].key_itvDataGrid[j].key_itv_other;
              }
              questionComboItem.key_itv_description = this.questionComboData.questionCombo[i].key_itvDataGrid[j].key_itv_description;
              
              this.initData_intervention.data.questionCombo[index++] = questionComboItem;
            }
          }
        }
      }
      // force FormioEditDataGrid component to reload
      this.key_intervention++;
    },
    getInitData() {
      // get the initial array of questionComboData to pre-populate the question combo component
      if (   this.dataModel != null 
          && this.dataModel.data != null) {
        for (let g = 0; g < this.dataModel.data.length; g++) {
          this.initData_intervention = {"data": {}};
          let index = 0;
          if (this.dataModel.data[g].subsections != null) {
            for (let i = 0; i < this.dataModel.data[g].subsections.length; i++) {
              if (this.dataModel.data[g].subsections[i].type === 'questionCombo') {
                let tmp = this.dataModel.data[g].subsections[i].interventionNeeded.initData.data;
                // add to this.initData_questionCombo
                this.initData_questionCombo[this.dataModel.data[g].subsections[i].key]=this.dataModel.data[g].subsections[i].interventionNeeded.initData;
              
                // add to this.initData_intervention
                if ( tmp != null 
                  && tmp.key_itvDataGrid != null 
                  && tmp.key_itvDataGrid.length > 0) {
                  for (let j = 0; j < tmp.key_itvDataGrid.length; j++) {
                    if (this.initData_intervention.data.questionCombo == null) {
                      this.initData_intervention.data.questionCombo = [];
                    }
                    let questionComboItem = {};
                    questionComboItem.radioButton = tmp.radioButton;
                    questionComboItem.hidden_key = tmp.hidden_key;
                    questionComboItem.comments = tmp.comments;
                    questionComboItem.key_checkbox = tmp.key_checkbox;
                    questionComboItem.questionLabel = tmp.questionLabel;
                    // if key_itv_type value is 'other', use the value of key_itv_other
                    questionComboItem.key_itv_type = tmp.key_itvDataGrid[j].key_itv_type;
                    if (tmp.key_itvDataGrid[j].key_itv_type === "other") {
                      questionComboItem.key_itv_type = tmp.key_itvDataGrid[j].key_itv_other;
                    }
                    questionComboItem.key_itv_description = tmp.key_itvDataGrid[j].key_itv_description;
                    
                    this.initData_intervention.data.questionCombo[index++] = questionComboItem;
                  }
                }
              } 
            }
          }
        }
      }
      // force FormioQuestionCombo component to reload
      this.Key_questionCombo++;
      // force FormioEditDataGrid component to reload
      this.key_intervention++;
    }
  }
}
</script>