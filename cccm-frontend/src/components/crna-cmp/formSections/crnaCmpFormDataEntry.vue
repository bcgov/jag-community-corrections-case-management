<template>
    <div>
      <div v-for="(header, indexp) in dataModel.data" 
          :id="`${indexp}`"
          :key="indexp">
        <div v-for="(headerc, indexc) in header.subsections" :key="indexc">
          <div :id="`${indexp}${indexc}`" class="formio_anchor_class">
            <FormioQuestionCombo v-if="headerc.type === 'questionCombo'" @dataOnChanged="handleDataOnChanged" :dataModel="headerc" :initData="initData"/>
            <FormioLabelTextarea v-else-if="headerc.type === 'labelTextarea'" :dataModel="headerc" />
            <FormioRadio v-else-if="headerc.type === 'radio'" :dataModel="headerc" />
            <FormioQuestionCetegoryTitle v-else-if="headerc.type === 'sectionTitle'" :dataModel="headerc"/>
            <FormioRadioTextarea v-else-if="headerc.type === 'radioTextarea'" :dataModel="headerc"/>
            <FormioEditDataGrid v-else-if="headerc.type === 'editGrid'" :dataTemplate="headerc" :key=key_intervention :dataModel="dataModel.data" :initData="initData_intervention"/>
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
    initData: {}
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
      questionComboData: {},
      questionComboIndex: 0,
      key_intervention: 0,
      initData_intervention: {"data": {}},
    }
  },
  methods: {
    handleDataOnChanged(dataValue, dataType) {
      // Sample dataValue when checkbox is checked
      //     {
      //       "radioButton": "P",
      //       "hidden_key": "quetionCombo_fr",
      //       "comments": "",
      //       "key_checkbox": true,
      //       "questionLabel": "Family Relationships"
      //       "key_itvDataGrid": [
      //         {
      //           "key_itv_type": "",
      //           "key_itv_other": "",
      //           "key_itv_description": ""
      //         }
      //       ]
      //     }
      // Sample dataValue when checkbox is unchecked
      //     {
      //       "radioButton": "P",
      //       "hidden_key": "quetionCombo_fr",
      //       "comments": "",
      //       "key_checkbox": false,
      //       "questionLabel": "Family Relationships"
      //     }

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
     

      // return 
      //   {
      //     "data": {
      //       "questionCombo": [
      //         {
      //           "radioButton": "P",
      //           "hidden_key": "quetionCombo_fr",
      //           "comments": "",
      //           "key_checkbox": true,
      //           "key_itv_type": "type1",
      //           "key_itv_other": "",
      //           "key_itv_description": "type 1 desc",
      //           "questionLabel": "Family Relationships",
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
            //       ]
      //         },
      //         {
      //           "radioButton": "P",
      //           "hidden_key": "quetionCombo_fr",
      //           "comments": "",
      //           "key_checkbox": true,
      //           "key_itv_type": "other",
      //           "key_itv_description": "abc desc",
      //           "key_itv_other": "abc",
      //           "questionLabel": "Family Relationships",
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
            //       ]
      //         }
      //       ]
      //     }
      //   };
      if (dataType === 'questionCombo') {
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
        //console.log("questionComboData: ", JSON.stringify(this.questionComboData));

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
                //questionComboItem = this.questionComboData.questionCombo[i];
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
        //console.log("initData_intervention: ", JSON.stringify(this.initData_intervention));
        this.key_intervention++;
      }
    }
  }
}
</script>