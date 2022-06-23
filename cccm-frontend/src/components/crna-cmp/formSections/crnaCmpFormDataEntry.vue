<template>
    <div>
      <div v-for="(header, indexp) in dataModel.data" 
          :id="`${indexp}`"
          :key="indexp">
        <div v-for="(headerc, indexc) in header.subsections" :key="indexc">
          <div :id="`${indexp}${indexc}`" class="formio_anchor_class">
            <FormioQuestionCombo v-if="headerc.type === 'questionCombo'" :key=Key_questionCombo @dataOnChanged="handleQuestionComboDataOnChanged" :dataModel="headerc" :initData="initData_questionCombo[headerc.key]"/>
            <FormioLabelTextarea v-else-if="headerc.type === 'labelTextarea'" :key=key_labeltextarea[headerc.key] @dataOnChanged="handleLabelTextareaDataOnChanged" :dataModel="headerc" :initData="initData_labeltextarea[headerc.key]"/>
            <FormioRadio v-else-if="headerc.type === 'radio'" :key=key_radio[headerc.key] :dataModel="headerc" :initData="initData_radio[headerc.key]"/>
            <FormioSectionTitle v-else-if="headerc.type === 'sectionTitle'" :dataModel="headerc"/>
            <FormioRadioTextarea v-else-if="headerc.type === 'radioTextarea'" :key=key_radiotextarea[headerc.key] :dataModel="headerc" :initData="initData_radiotextarea[headerc.key]"/>
            <FormioEditDataGridIntervention v-else-if="headerc.type === 'editGridIntervention'" :key=key_editgrid_intervention @dataOnChanged="handleInterventionDataGridOnChanged" :dataTemplate="headerc" :dataModel="dataModel.data" :initData="initData_editgrid_intervention"/>
            <FormioEditDataGridRadioText v-else-if="headerc.type === 'editGridRadioText'" :key=key_editgrid_radiotext[headerc.ref_key_section] :dataTemplate="headerc" :initData="initData_editgrid_radiotext[headerc.ref_key_section]"/>
            <FormioEditDataGridRadioTextList v-else-if="headerc.type === 'editGridRadioTextList'" 
                v-for="(headergc, indexgc) in headerc.editgriditems" 
                :key=key_editgrid_radiotextList[headergc.ref_key_subsection] 
                :dataTemplate="headergc" :dataTemplateP="headerc" 
                :editgridLabel="editgridLabel[headerc.ref_key_section]" 
                :radioValue="radioValue[headergc.ref_key_subsection]" 
                :initData="initData_editgrid_radiotextList[headergc.ref_key_subsection]"/>
            <FormioCheckboxTextareaList v-else-if="headerc.type === 'checkboxTextareaList'" :key=key_checkboxTextareaList[headerc.key] :dataModel="headerc" :initData="initData_checkboxTextareaList[headerc.key]"/>
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
import FormioSectionTitle from "@/components/common/FormioSectionTitle.vue";
import FormioRadioTextarea from "@/components/common/FormioRadioTextarea.vue";
import FormioButton from "@/components/common/FormioButton.vue";
import FormioEditDataGridIntervention from "@/components/common/FormioEditDataGridIntervention.vue";
import FormioEditDataGridRadioTextList from "@/components/common/FormioEditDataGridRadioTextList.vue";
import FormioCheckboxTextareaList from "@/components/common/FormioCheckboxTextareaList.vue";
import FormioEditDataGridRadioText from "@/components/common/FormioEditDataGridRadioText.vue";

export default {
  name: 'CrnaCmpFormDataEntry',
  props: {
    dataModel: {},
  },
  components: {
    FormioQuestionCombo,
    FormioLabelTextarea,
    FormioRadio,
    FormioSectionTitle,
    FormioRadioTextarea,
    FormioButton,
    FormioEditDataGridIntervention,
    FormioEditDataGridRadioTextList,
    FormioCheckboxTextareaList,
    FormioEditDataGridRadioText,
  },
  data() {
    return {
      key_editgrid_intervention: 0,
      initData_editgrid_intervention: {"data": {}},
      Key_questionCombo: 0,
      initData_questionCombo: [],
      questionComboData: {},
      questionComboIndex: 0,
      key_editgrid_radiotextList: [],
      key_editgrid_radiotext: [],
      initData_editgrid_radiotextList: [],
      initData_radiotextarea: [],
      key_radiotextarea: [],
      initData_editgrid_radiotext: [],
      radioValue: [],
      editgridLabel: [],
      initData_checkboxTextareaList: [],
      key_checkboxTextareaList: [],
      initData_labeltextarea: [],
      key_labeltextarea: [],
      initData_radio: [],
      key_radio: [],
    }
  },
  mounted(){
    this.getInitData();
  },
  methods: {
    handleLabelTextareaDataOnChanged(dataValue, key) {
      //Sample dataValue:{"key_textarea": "add"}
      //Update key_editgrid_radiotext and key_editgrid_radiotextList
      //console.log("dataValue: ", dataValue);
      //console.log("key_editgrid_radiotext: ", this.key_editgrid_radiotext);
      //console.log("initData_editgrid_radiotext: ", this.initData_editgrid_radiotext);

      this.initData_editgrid_radiotext[key].data.key_editgrid_radiotext[0].key_textarea = dataValue.key_textarea;
      this.key_editgrid_radiotext[key]++;
    },
    handleRadioTextDataGridOnChanged(dataValue) {

      // force FormioEditDataGridRadioTextList component to reload
      this.key_editgrid_radiotextList[dataValue.hidden_key]++;
    },
    handleInterventionDataGridOnChanged(dataValue) {
      console.log("dataValue: ", dataValue);
      // Changes made to intervention list in 'Case plan', need to repopulate the intervention in 'Needs Assessement'
      //Sample dataValue, should only contain 1 object
      // {
      //   "key_radioButton": "P",
      //   "hidden_key": "quetionCombo_fr",
      //   "key_comments": "",
      //   "key_checkbox": true,
      //   "questionLabel": "Family Relationships",
      //   "key_itv_type": "type1",
      //   "key_itv_description": "some comments for type 1",
      // }
      
      // Update this.initData_questionCombo
      this.initData_questionCombo[dataValue.hidden_key].data.key_comments = dataValue.key_comments;
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
      //   "key_radioButton": "P",
      //   "hidden_key": "quetionCombo_fr",
      //   "key_comments": "",
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
      //   "key_radioButton": "P",
      //   "hidden_key": "quetionCombo_fr",
      //   "key_comments": "",
      //   "key_checkbox": false,
      //   "questionLabel": "Family Relationships"
      // }

      // Sample data for this.questionComboData
      // {
      //   "questionCombo": [
      //     {
      //       "key_radioButton": "P",
      //       "hidden_key": "quetionCombo_fr",
      //       "key_comments": "",
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

      // rebuild initData_editgrid_intervention from questionComboData
      this.initData_editgrid_intervention = {"data": {}};
      let index = 0;
      if (   this.questionComboData != null 
          && this.questionComboData.questionCombo != null 
          && this.questionComboData.questionCombo.length > 0) {
        for (let i = 0; i < this.questionComboData.questionCombo.length; i++) {
          if (   this.questionComboData.questionCombo[i] != null 
              && this.questionComboData.questionCombo[i].key_itvDataGrid != null 
              && this.questionComboData.questionCombo[i].key_itvDataGrid.length > 0) {
            for (let j = 0; j < this.questionComboData.questionCombo[i].key_itvDataGrid.length; j++) {
              if (this.initData_editgrid_intervention.data.key_editgrid_intervention == null) {
                this.initData_editgrid_intervention.data.key_editgrid_intervention = [];
              }
              let questionComboItem = {};
              questionComboItem.key_radioButton = this.questionComboData.questionCombo[i].key_radioButton;
              questionComboItem.hidden_key = this.questionComboData.questionCombo[i].hidden_key;
              questionComboItem.key_comments = this.questionComboData.questionCombo[i].key_comments;
              questionComboItem.key_checkbox = this.questionComboData.questionCombo[i].key_checkbox;
              questionComboItem.key_questionLabel = this.questionComboData.questionCombo[i].key_questionLabel;
              // if key_itv_type value is 'other', use the value of key_itv_other
              questionComboItem.key_itv_type = this.questionComboData.questionCombo[i].key_itvDataGrid[j].key_itv_type;
              if (this.questionComboData.questionCombo[i].key_itvDataGrid[j].key_itv_type === "other") {
                questionComboItem.key_itv_type = this.questionComboData.questionCombo[i].key_itvDataGrid[j].key_itv_other;
              }
              questionComboItem.key_itv_description = this.questionComboData.questionCombo[i].key_itvDataGrid[j].key_itv_description;
              
              this.initData_editgrid_intervention.data.key_editgrid_intervention[index++] = questionComboItem;
            }
          }
        }
      }
      // force FormioEditDataGridIntervention component to reload
      this.key_editgrid_intervention++;
    },
    getInitData() {
      // set this.initData_questionCombo for FormioQuestionCombo component
      // set this.initData_editgrid_intervention for FormioEditDataGridIntervention component
      // set this.initData_editgrid_radiotextList for FormioEditDataGridRadioTextList component
      // set this.initData_editgrid_radiotext for FormioEditDataGridRadioText component
      // set this.initData_labeltextarea for FormioLabelTextarea component
      if (   this.dataModel != null 
          && this.dataModel.data != null) {

        let keyRatio = 10000;
        for (let g = 0; g < this.dataModel.data.length; g++) {
          this.editgridLabel[this.dataModel.data[g].key_section] = this.dataModel.data[g].section;
          if (this.dataModel.data[g].subsections != null) {
            for (let i = 0; i < this.dataModel.data[g].subsections.length; i++) {
              let theKey = this.dataModel.data[g].subsections[i].key;
              // populate initData_questionCombo 
              // populate initData_editgrid_intervention
              if (this.dataModel.data[g].subsections[i].type === 'questionCombo') {
                let tmp = this.dataModel.data[g].subsections[i].initData.data;
                // add to this.initData_editgrid_radiotextList
                if (tmp != null) {
                  //console.log("theKey: ", theKey);
                  //console.log("initData_editgrid_radiotextList: ", this.initData_editgrid_radiotextList);
                  if (this.initData_editgrid_radiotextList[theKey] == null) {
                    this.initData_editgrid_radiotextList[theKey] = {"data": {"key_editgrid_radiotext": {}}};
                  }
                  // force FormioEditDataGridRadioTextList component to reload
                  this.key_editgrid_radiotextList[theKey] = (g + i) * keyRatio;
                  this.initData_editgrid_radiotextList[theKey].data.key_editgrid_radiotext.key_comments = tmp.key_comments;
                  this.initData_editgrid_radiotextList[theKey].data.key_editgrid_radiotext.key_radioButton = tmp.key_radioButton;
                  this.initData_editgrid_radiotextList[theKey].data.key_editgrid_radiotext.hidden_key = tmp.hidden_key;
                  this.initData_editgrid_radiotextList[theKey].data.key_editgrid_radiotext.key_questionLabel = tmp.key_questionLabel;

                  // add to this.initData_questionCombo
                  this.initData_questionCombo[theKey]=this.dataModel.data[g].subsections[i].initData;

                  this.radioValue[theKey]=this.dataModel.data[g].subsections[i].radioGroup.values;

                  // force FormioQuestionCombo component to reload
                  this.Key_questionCombo++;
                }

                // add to this.initData_editgrid_intervention
                if ( tmp != null 
                  && tmp.key_itvDataGrid != null 
                  && tmp.key_itvDataGrid.length > 0) {
                  let index = 0;
                  this.initData_editgrid_intervention = {"data": {}};

                  for (let j = 0; j < tmp.key_itvDataGrid.length; j++) {
                    if (this.initData_editgrid_intervention.data.key_editgrid_intervention == null) {
                      this.initData_editgrid_intervention.data.key_editgrid_intervention = [];
                    }
                    let questionComboItem = {};
                    questionComboItem.key_radioButton = tmp.key_radioButton;
                    questionComboItem.hidden_key = tmp.hidden_key;
                    questionComboItem.key_comments = tmp.key_comments;
                    questionComboItem.key_checkbox = tmp.key_checkbox;
                    questionComboItem.key_questionLabel = tmp.key_questionLabel;
                    // if key_itv_type value is 'other', use the value of key_itv_other
                    questionComboItem.key_itv_type = tmp.key_itvDataGrid[j].key_itv_type;
                    if (tmp.key_itvDataGrid[j].key_itv_type === "other") {
                      questionComboItem.key_itv_type = tmp.key_itvDataGrid[j].key_itv_other;
                    }
                    questionComboItem.key_itv_description = tmp.key_itvDataGrid[j].key_itv_description;
                    
                    this.initData_editgrid_intervention.data.key_editgrid_intervention[index++] = questionComboItem;

                    // force FormioEditDataGridIntervention component to reload
                    this.key_editgrid_intervention++;
                  }
                }
              } 

              // populate this.initData_checkboxTextareaList
              // populate this.initData_editgrid_radiotext
              if (this.dataModel.data[g].subsections[i].type === 'checkboxTextareaList') {
                let curComponent = this.dataModel.data[g].subsections[i];
                this.initData_checkboxTextareaList[theKey] = (g + i) * keyRatio;
                this.initData_checkboxTextareaList[theKey] = curComponent.initData;

                let index = 0;
                if (curComponent.initData != null && curComponent.initData.data != null) {
                  let initVal = curComponent.initData.data;
                  for (let a = 0; a < curComponent.checkboxTextareaItems.length; a++) {
                    if (this.initData_editgrid_radiotext[theKey] == null) {
                      this.initData_editgrid_radiotext[theKey] = {"data": {"key_editgrid_radiotext": []}};
                    }
                    let containerKey = curComponent.checkboxTextareaItems[a].key_container;
                    let questionLabel = curComponent.checkboxTextareaItems[a].label_checkbox;

                    if (initVal[containerKey].key_checkbox) {
                      let item = {};
                      item.key_comments = initVal[containerKey].key_textarea;
                      item.key_questionLabel = questionLabel;
                      item.hidden_key = containerKey;
                      this.initData_editgrid_radiotext[theKey].data.key_editgrid_radiotext[index++] = item;
                    }
                  }
                }
              }

              // populate initData_editgrid_radiotextList
              // populate initData_radio
              if (this.dataModel.data[g].subsections[i].type === 'radio') {
                let curComponent = this.dataModel.data[g].subsections[i];
                if (curComponent.initData != null && curComponent.initData.data != null) {
                  let initData = curComponent.initData.data;

                  // populate initData_editgrid_radiotextList
                  if (this.initData_editgrid_radiotextList[theKey] == null) {
                    this.initData_editgrid_radiotextList[theKey] = {"data": {"key_editgrid_radiotext": {}}};
                  }
                  this.key_editgrid_radiotextList[theKey] = (g + i) * keyRatio;
                  this.initData_editgrid_radiotextList[theKey].data.key_editgrid_radiotext.key_comments = initData.key_comments;
                  this.initData_editgrid_radiotextList[theKey].data.key_editgrid_radiotext.key_radioButton = initData.key_radioButton;
                  this.initData_editgrid_radiotextList[theKey].data.key_editgrid_radiotext.hidden_key = curComponent.key;
                  this.initData_editgrid_radiotextList[theKey].data.key_editgrid_radiotext.key_questionLabel = curComponent.questionLabel;

                  this.radioValue[theKey]=this.dataModel.data[g].subsections[i].values;

                  // populate initData_radio
                  if (this.initData_radio[theKey] == null) {
                    this.initData_radio[theKey] = {"data": {}};
                  }
                  let item = {};
                  item.key_radioButton = initData.key_radioButton;
                  item.key_comments = initData.key_comments;
                  this.initData_radio[theKey].data = item; 
                  this.key_radio[theKey] = (g + i) * keyRatio;
                }
              }

              // populate initData_editgrid_radiotextList
              // populate initData_radiotextarea
              if (this.dataModel.data[g].subsections[i].type === 'radioTextarea') {
                let curComponent = this.dataModel.data[g].subsections[i];
                if (curComponent.initData != null && curComponent.initData.data != null) {
                  let initData = curComponent.initData.data;

                  // populate initData_editgrid_radiotextList
                  if (this.initData_editgrid_radiotextList[theKey] == null) {
                    this.initData_editgrid_radiotextList[theKey] = {"data": {"key_editgrid_radiotext": {}}};
                  }
                  // populate FormioEditDataGridRadioTextList component to reload
                  this.key_editgrid_radiotextList[theKey] = (g + i) * keyRatio;
                  this.initData_editgrid_radiotextList[theKey].data.key_editgrid_radiotext.key_comments = initData.key_comments;
                  this.initData_editgrid_radiotextList[theKey].data.key_editgrid_radiotext.key_radioButton = initData.key_radioButton;
                  this.initData_editgrid_radiotextList[theKey].data.key_editgrid_radiotext.hidden_key = theKey;
                  this.initData_editgrid_radiotextList[theKey].data.key_editgrid_radiotext.key_questionLabel = curComponent.questionLabel;

                  this.radioValue[theKey]=this.dataModel.data[g].subsections[i].values;

                  // populate initData_radiotextarea
                  if (this.initData_radiotextarea[theKey] == null) {
                    this.initData_radiotextarea[theKey] = {"data": {}};
                  }
                  let item = {};
                  item.key_radioButton = initData.key_radioButton;
                  item.key_comments = initData.key_comments;
                  this.initData_radiotextarea[theKey].data = item; 
                  this.key_radiotextarea[theKey] = (g + i) * keyRatio;
                }
              }

              // populate this.initData_editgrid_radiotextList
              // populate this.initData_editgrid_radiotext
              // populate this.initData_labeltextarea
              if (this.dataModel.data[g].subsections[i].type === 'labelTextarea') {
                let curComponent = this.dataModel.data[g].subsections[i];
                if (curComponent.initData != null && curComponent.initData.data != null) {
                  // populate this.initData_labeltextarea
                  if (this.initData_labeltextarea[theKey] == null) {
                    this.initData_labeltextarea[theKey] = {"data": {"key_textarea": ""}};
                  }
                  this.initData_labeltextarea[theKey].data.key_textarea = curComponent.initData.data.key_textarea;
                  this.key_labeltextarea[theKey] = (g + i) * keyRatio;

                  // populate this.initData_editgrid_radiotext 
                  if (this.initData_editgrid_radiotext[theKey] == null) {
                    this.initData_editgrid_radiotext[theKey] = {"data": {"key_editgrid_radiotext": []}};
                  }
                  let item = {};
                  item.key_comments = curComponent.initData.data.key_textarea;
                  item.key_questionLabel = curComponent.label_textarea;
                  item.hidden_key = curComponent.key;
                  this.initData_editgrid_radiotext[theKey].data.key_editgrid_radiotext[0] = item;
                  this.key_editgrid_radiotext[theKey] = (g + i) * keyRatio;

                  // populate this.initData_editgrid_radiotextList
                  if (this.initData_editgrid_radiotextList[theKey] == null) {
                    this.initData_editgrid_radiotextList[theKey] = {"data": {"key_editgrid_radiotext": {}}};
                  }
                  
                  this.key_editgrid_radiotextList[theKey] = (g + i) * keyRatio;
                  this.initData_editgrid_radiotextList[theKey].data.key_editgrid_radiotext.key_comments = curComponent.initData.data.key_textarea;
                  this.initData_editgrid_radiotextList[theKey].data.key_editgrid_radiotext.key_radioButton = "";
                  this.initData_editgrid_radiotextList[theKey].data.key_editgrid_radiotext.hidden_key = curComponent.key;
                  this.initData_editgrid_radiotextList[theKey].data.key_editgrid_radiotext.key_questionLabel = curComponent.label;
                }
              } 
            }
          }
        }
        
      }

      //console.log("this.key_editgrid_radiotextList: ", this.key_editgrid_radiotextList);
      //console.log("this.initData_labeltextarea: ", this.initData_labeltextarea);
      // this.initData_editgrid_radiotextList = {
      //   "data": {
      //     "key_editgrid_radiotext": [
      //       {
      //         "hidden_key": "quetionCombo_fr",
      //         "key_questionLabel": "Family Relationships",
      //         "key_comments": "Difficult family relationships",
      //         "key_radioButton": "P"
      //       }
      //     ]
      //   }
      // };
    }
  }
}
</script>