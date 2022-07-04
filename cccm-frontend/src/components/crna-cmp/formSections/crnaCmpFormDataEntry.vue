<template>
    <div>
      <div v-for="(header, indexp) in dataModel.data" 
          :id="`${indexp}`"
          :key="indexp">
        <div v-for="(headerc, indexc) in header.subsections" :key="indexc">
          <div :id="`${indexp}${indexc}`" class="formio_anchor_class">
            <FormioQuestionCombo v-if="headerc.type === 'questionCombo'" :key="`${indexc}${Key_questionCombo}`" @dataOnChanged="handleQuestionComboDataOnChanged" :dataModel="headerc" :initData="initData_questionCombo[headerc.key]"/>
            <FormioLabelTextarea v-else-if="headerc.type === 'labelTextarea'" :key="`${indexc}${key_labeltextarea}`" @dataOnChanged="handleLabelTextareaDataOnChanged" :dataModel="headerc" :initData="initData_labeltextarea[headerc.key]"/>
            <FormioRadio v-else-if="headerc.type === 'radio'" :key=key_radio[headerc.key] @dataOnChanged="handleRadioDataOnChanged" :dataModel="headerc" :initData="initData_radio[headerc.key]"/>
            <FormioSectionTitle v-else-if="headerc.type === 'sectionTitle'" :dataModel="headerc"/>
            <FormioRadioTextarea v-else-if="headerc.type === 'radioTextarea'" :key=key_radiotextarea[headerc.key] @dataOnChanged="handleRadioTextareaDataOnChanged" :dataModel="headerc" :initData="initData_radiotextarea[headerc.key]"/>
            <FormioEditDataGridIntervention v-else-if="headerc.type === 'editGridIntervention'" :key="`${indexc}${key_editgrid_intervention}`" @dataOnChanged="handleInterventionDataGridOnChanged" :dataTemplate="headerc" :dataModel="dataModel.data" :initData="initData_editgrid_intervention"/>
            <FormioEditDataGridRadioText v-else-if="headerc.type === 'editGridRadioText'" :key="`${indexc}${key_editgrid_radiotext}`" @dataOnChanged="handleRadioTextDataGridDataOnChanged" :dataTemplate="headerc" :initData="initData_editgrid_radiotext[headerc.ref_key_section]"/>
            <FormioCheckboxTextareaList v-else-if="headerc.type === 'checkboxTextareaList'" :key="`${indexc}${key_checkboxTextareaList}`" @dataOnChanged="handleCheckboxTextareaListDataOnChanged" :dataModel="headerc" :initData="initData_checkboxTextareaList[headerc.key]"/>
            <FormioEditDataGridRadioTextList v-else-if="headerc.type === 'editGridRadioTextList'" 
              v-for="(headergc, indexgc) in headerc.editgriditems" 
              :key="`${indexc}${indexgc}${key_editgrid_radiotextList}`"
              @dataOnChanged="handleGridRadioTextListDataGridDataOnChanged"
              :dataTemplate="headergc" :dataTemplateP="headerc" 
              :editgridLabel="editgridLabel[headerc.ref_key_section]" 
              :radioValue="radioValue[headergc.ref_key_subsection]" 
              :initData="initData_editgrid_radiotextList[headergc.ref_key_subsection]"/>
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
      key_editgrid_radiotextList: 0,
      key_editgrid_radiotext: 0,
      initData_editgrid_radiotextList: [],
      initData_radiotextarea: [],
      key_radiotextarea: [],
      initData_editgrid_radiotext: [],
      radioValue: [],
      editgridLabel: [],
      initData_checkboxTextareaList: [],
      key_checkboxTextareaList: 0,
      initData_labeltextarea: [],
      key_labeltextarea: 0,
      initData_radio: [],
      key_radio: [],
    }
  },
  mounted(){
    this.getInitData();
  },
  methods: {
    handleCheckboxTextareaListDataOnChanged(dataValue, parentKey, containerKey, questionLabel) {
      if (dataValue != null) {
        let initVal = dataValue;
        if (this.initData_editgrid_radiotext[parentKey] == null) {
          this.initData_editgrid_radiotext[parentKey] = {"data": {"key_editgrid_radiotext": []}};
        }
        let found = false;
        let theIndex = 0;
        for (let i = 0; i < this.initData_editgrid_radiotext[parentKey].data.key_editgrid_radiotext.length; i++) {
          let curItem = this.initData_editgrid_radiotext[parentKey].data.key_editgrid_radiotext[i];
          if (curItem.hidden_key === containerKey) {
            found = true;
            theIndex = i;
            break;
          }
        }
        // The updated checkboxTextarea is already in the list, update it
        if (found) {
          // the checkbox is still checked, update the comments
          if (initVal[containerKey].key_checkbox) {
            let item = {};
            item.key_comments = initVal[containerKey].key_textarea;
            item.key_questionLabel = questionLabel;
            item.hidden_key = containerKey;
            item.hidden_parent_key = parentKey;
            item.source_type = "checkboxTextareaList";
            this.initData_editgrid_radiotext[parentKey].data.key_editgrid_radiotext[theIndex] = item;
          } else {
            // the checkbox is unchecked, remove the item from the list
            this.initData_editgrid_radiotext[parentKey].data.key_editgrid_radiotext.splice(theIndex, 1);
          }
        // The updated checkboxTextarea is not in the list, add to it.
        } else {
          let arrayLength = this.initData_editgrid_radiotext[parentKey].data.key_editgrid_radiotext.length;
          let item = {};
          item.key_comments = initVal[containerKey].key_textarea;
          item.key_questionLabel = questionLabel;
          item.hidden_key = containerKey;
          item.hidden_parent_key = parentKey;
          item.source_type = "checkboxTextareaList";
          this.initData_editgrid_radiotext[parentKey].data.key_editgrid_radiotext[arrayLength] = item;
        }
        // force the FormioEditDataGridRadioText component to refresh
        this.key_editgrid_radiotext++;
      }
    },
    handleGridRadioTextListDataGridDataOnChanged(dataValue) {
      //console.log("RadioTextListDataGridData data on change: ", dataValue);
    },
    handleRadioTextDataGridDataOnChanged(dataValue) {
      //console.log("RadioTextDataGridData data on change: ", dataValue);
      // Sample dataValue
      // {
      //   "key_comments": "sample comments aaa",
      //   "key_questionLabel": "Indigenous considerations",
      //   "hidden_key": "key_container_ic",
      //   "hidden_parent_key": "key_responsivityFactors",
      //   "source_type": "checkboxTextareaList"
      // }
      if (dataValue != null) {
        let theKey = dataValue.hidden_parent_key;
        if (dataValue.source_type === "checkboxTextareaList") {
          this.initData_checkboxTextareaList[theKey].data[dataValue.hidden_key].key_textarea = dataValue.key_comments;
          //force FormioCheckboxTextareaList component to refresh
          this.key_checkboxTextareaList++;
        } else if (dataValue.source_type === "labelTextarea") {
          // update this.initData_labeltextarea
          if (this.initData_labeltextarea[theKey] == null) {
            this.initData_labeltextarea[theKey] = {"data": {"key_textarea": ""}};
          }
          this.initData_labeltextarea[theKey].data.key_textarea = dataValue.key_comments;
          //force FormioLabelTextarea component to refresh
          this.key_labeltextarea++;
        }
      }
    },
    handleRadioTextareaDataOnChanged(dataValue) {
      //console.log("RadioTextarea data on change: ", dataValue);
      // {
      //   "key_radioButton": "n",
      //   "key_comments": "Sample comments"
      // }
      if (dataValue != null) {
        let theKey = dataValue.hidden_key;
        if (this.initData_editgrid_radiotextList[theKey] != null) {
          this.initData_editgrid_radiotextList[theKey].data.key_editgrid_radiotext[0].key_comments = dataValue.key_comments;
          this.initData_editgrid_radiotextList[theKey].data.key_editgrid_radiotext[0].key_radioButton = dataValue.key_radioButton;
          this.initData_editgrid_radiotextList[theKey].data.key_editgrid_radiotext[0].hidden_key = theKey;
          
          // force FormioEditDataGridRadioTextList component refresh
          this.key_editgrid_radiotextList++;
        }
      } 
    },
    handleRadioDataOnChanged(dataValue, theKey) {
      //console.log("Radio data on change: ", dataValue, theKey);
      if (dataValue != null && this.initData_editgrid_radiotextList[theKey] != null) {
        this.initData_editgrid_radiotextList[theKey].data.key_editgrid_radiotext[0].key_comments = dataValue.key_comments;
        this.initData_editgrid_radiotextList[theKey].data.key_editgrid_radiotext[0].key_radioButton = dataValue.key_radioButton;
        this.initData_editgrid_radiotextList[theKey].data.key_editgrid_radiotext[0].hidden_key = theKey;

        // force FormioEditDataGridRadioTextList component refresh
        this.key_editgrid_radiotextList++;
      }
    },
    handleLabelTextareaDataOnChanged(dataValue, key) {
      //console.log("dataValue: ", dataValue, key);
      if (this.initData_editgrid_radiotext[key] != null) {
        let item = this.initData_editgrid_radiotext[key].data.key_editgrid_radiotext[0];
        item.key_comments = dataValue.key_textarea;
        item.hidden_parent_key = key;
        item.hidden_key = key;
        item.source_type = "labelTextarea";
        this.key_editgrid_radiotext++;
      }
      
      if (this.initData_editgrid_radiotextList[key] != null) {
        let item = this.initData_editgrid_radiotextList[key].data.key_editgrid_radiotext[0];
        item.key_comments = dataValue.key_textarea;
        item.hidden_key = key;
        this.key_editgrid_radiotextList++;
      }
    },
    handleRadioTextDataGridOnChanged(dataValue) {
      // force FormioEditDataGridRadioTextList component to reload
      this.key_editgrid_radiotextList++;
    },
    handleInterventionDataGridOnChanged(dataValue) {
      // Changes made to intervention list in 'Case plan', need to repopulate the intervention in 'Needs Assessement'
      // Sample dataValue, should only contain 1 object
      // {
      //   "key_radioButton": "P",
      //   "hidden_key": "questionCombo_fr",
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
      //console.log("QuestionComboDataOnChanged: ", dataValue);
      // Sample dataValue when checkbox is checked
      // {
      //   "key_radioButton": "P",
      //   "hidden_key": "questionCombo_fr",
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
      //   "hidden_key": "questionCombo_fr",
      //   "key_comments": "",
      //   "key_checkbox": false,
      //   "questionLabel": "Family Relationships"
      // }
      this.private_update_initData_editgrid_intervention(dataValue);
      this.private_update_initData_editgrid_radiotextList(dataValue);
    },
    private_update_initData_editgrid_intervention(dataValue) {
      // Sample data for this.questionComboData
      // {
      //   "questionCombo": [
      //     {
      //       "key_radioButton": "P",
      //       "hidden_key": "questionCombo_fr",
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
    private_update_initData_editgrid_radiotextList(dataValue) {
      if (dataValue != null) {
        let theKey = dataValue.hidden_key;
        if (this.initData_editgrid_radiotextList[theKey] != null) {
          this.initData_editgrid_radiotextList[theKey].data.key_editgrid_radiotext[0].key_comments = dataValue.key_comments;
          this.initData_editgrid_radiotextList[theKey].data.key_editgrid_radiotext[0].key_radioButton = dataValue.key_radioButton;
          this.initData_editgrid_radiotextList[theKey].data.key_editgrid_radiotext[0].hidden_key = theKey;

          // force FormioEditDataGridRadioTextList component refresh
          this.key_editgrid_radiotextList++;
        }
      }
    },
    getInitData() {
      // set this.initData_questionCombo for FormioQuestionCombo component
      // set this.initData_editgrid_intervention for FormioEditDataGridIntervention component
      // set this.initData_editgrid_radiotextList for FormioEditDataGridRadioTextList component
      // set this.initData_editgrid_radiotext for FormioEditDataGridRadioText component
      // set this.initData_labeltextarea for FormioLabelTextarea component
      if (   this.dataModel != null 
          && this.dataModel.data != null) {

        for (let g = 0; g < this.dataModel.data.length; g++) {
          this.editgridLabel[this.dataModel.data[g].key_section] = this.dataModel.data[g].section;
          if (this.dataModel.data[g].subsections != null) {
            for (let i = 0; i < this.dataModel.data[g].subsections.length; i++) {
              let theKey = this.dataModel.data[g].subsections[i].key;
              // populate initData_questionCombo 
              // populate initData_editgrid_intervention
              let curComponent = this.dataModel.data[g].subsections[i];
              if (this.dataModel.data[g].subsections[i].type === 'questionCombo') {
                let initData = curComponent.initData.data;
                // add to this.initData_editgrid_radiotextList
                if (initData != null) {
                  if (this.initData_editgrid_radiotextList[theKey] == null) {
                    this.initData_editgrid_radiotextList[theKey] = {"data": {"key_editgrid_radiotext": []}};
                  }
                  let item = {};
                  item.key_comments = initData.key_comments;
                  item.key_radioButton = initData.key_radioButton;
                  item.hidden_key = theKey;
                  item.key_questionLabel = curComponent.questionLabel;
                  this.initData_editgrid_radiotextList[theKey].data.key_editgrid_radiotext[0] = item;
                  // force FormioEditDataGridRadioTextList component to reload
                  this.key_editgrid_radiotextList++;
                  
                  // add to this.initData_questionCombo
                  this.initData_questionCombo[theKey]=this.dataModel.data[g].subsections[i].initData;

                  this.radioValue[theKey]=this.dataModel.data[g].subsections[i].radioGroup.values;

                  // force FormioQuestionCombo component to reload
                  this.Key_questionCombo++;
                }

                // add to this.initData_editgrid_intervention
                if ( initData != null 
                  && initData.key_itvDataGrid != null 
                  && initData.key_itvDataGrid.length > 0) {
                  let index = 0;
                  this.initData_editgrid_intervention = {"data": {}};

                  for (let j = 0; j < initData.key_itvDataGrid.length; j++) {
                    if (this.initData_editgrid_intervention.data.key_editgrid_intervention == null) {
                      this.initData_editgrid_intervention.data.key_editgrid_intervention = [];
                    }
                    let questionComboItem = {};
                    questionComboItem.key_radioButton = initData.key_radioButton;
                    questionComboItem.hidden_key = theKey;
                    questionComboItem.key_comments = initData.key_comments;
                    questionComboItem.key_checkbox = initData.key_checkbox;
                    questionComboItem.key_questionLabel = curComponent.questionLabel;
                    // if key_itv_type value is 'other', use the value of key_itv_other
                    questionComboItem.key_itv_type = initData.key_itvDataGrid[j].key_itv_type;
                    if (initData.key_itvDataGrid[j].key_itv_type === "other") {
                      questionComboItem.key_itv_type = initData.key_itvDataGrid[j].key_itv_other;
                    }
                    questionComboItem.key_itv_description = initData.key_itvDataGrid[j].key_itv_description;
                    
                    this.initData_editgrid_intervention.data.key_editgrid_intervention[index++] = questionComboItem;

                    // force FormioEditDataGridIntervention component to reload
                    this.key_editgrid_intervention++;
                  }
                }
              } 

              // populate this.initData_checkboxTextareaList
              // populate this.initData_editgrid_radiotext
              if (this.dataModel.data[g].subsections[i].type === 'checkboxTextareaList') {
                if (this.initData_checkboxTextareaList[theKey] == null) {
                  this.initData_checkboxTextareaList[theKey] = this.genRandomNumber();
                }
                
                this.initData_checkboxTextareaList[theKey] = curComponent.initData;
                this.key_checkboxTextareaList++;

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
                      item.hidden_parent_key = theKey;
                      item.source_type = "checkboxTextareaList";
                      this.initData_editgrid_radiotext[theKey].data.key_editgrid_radiotext[index++] = item;
                    }
                  }
                }
                this.key_editgrid_radiotext++;
              }

              // populate initData_editgrid_radiotextList
              // populate initData_radio
              if (this.dataModel.data[g].subsections[i].type === 'radio') {
                if (curComponent.initData != null && curComponent.initData.data != null) {
                  let initData = curComponent.initData.data;

                  // populate initData_editgrid_radiotextList
                  if (this.initData_editgrid_radiotextList[theKey] == null) {
                    this.initData_editgrid_radiotextList[theKey] = {"data": {"key_editgrid_radiotext": []}};
                  }
                  this.key_editgrid_radiotextList++;
                 
                  let item = {};
                  item.key_comments = initData.key_comments;
                  item.key_radioButton = initData.key_radioButton;
                  item.hidden_key = curComponent.key;
                  item.key_questionLabel = curComponent.questionLabel;
                  this.initData_editgrid_radiotextList[theKey].data.key_editgrid_radiotext[0] = item;

                  this.radioValue[theKey]=this.dataModel.data[g].subsections[i].values;

                  // populate initData_radio
                  if (this.initData_radio[theKey] == null) {
                    this.initData_radio[theKey] = {"data": {}};
                  }
                  item = {};
                  item.key_radioButton = initData.key_radioButton;
                  item.key_comments = initData.key_comments;
                  this.initData_radio[theKey].data = item; 
                  if (this.key_radio[theKey] == null) {
                    this.key_radio[theKey] = this.genRandomNumber();
                  }
                }
              }

              // populate initData_editgrid_radiotextList
              // populate initData_radiotextarea
              if (this.dataModel.data[g].subsections[i].type === 'radioTextarea') {
                if (curComponent.initData != null && curComponent.initData.data != null) {
                  let initData = curComponent.initData.data;

                  // populate initData_editgrid_radiotextList
                  if (this.initData_editgrid_radiotextList[theKey] == null) {
                    this.initData_editgrid_radiotextList[theKey] = {"data": {"key_editgrid_radiotext": []}};
                  }
                  let item = {};
                  item.key_comments = initData.key_comments;
                  item.key_radioButton = initData.key_radioButton;
                  item.hidden_key = theKey;
                  item.key_questionLabel = curComponent.questionLabel;
                  this.initData_editgrid_radiotextList[theKey].data.key_editgrid_radiotext[0] = item;

                  // force FormioEditDataGridRadioTextList component to reload
                  this.key_editgrid_radiotextList++;
                  
                  this.radioValue[theKey]=this.dataModel.data[g].subsections[i].values;

                  // populate initData_radiotextarea
                  if (this.initData_radiotextarea[theKey] == null) {
                    this.initData_radiotextarea[theKey] = {"data": {}};
                  }
                  item = {};
                  item.key_radioButton = initData.key_radioButton;
                  item.key_comments = initData.key_comments;
                  this.initData_radiotextarea[theKey].data = item; 
                  if (this.key_radiotextarea[theKey] == null) {
                    this.key_radiotextarea[theKey] = this.genRandomNumber();
                  }
                }
              }

              // populate this.initData_editgrid_radiotextList
              // populate this.initData_editgrid_radiotext
              // populate this.initData_labeltextarea
              if (this.dataModel.data[g].subsections[i].type === 'labelTextarea') {
                if (curComponent.initData != null && curComponent.initData.data != null) {
                  // populate this.initData_labeltextarea
                  if (this.initData_labeltextarea[theKey] == null) {
                    this.initData_labeltextarea[theKey] = {"data": {"key_textarea": ""}};
                  }
                  this.initData_labeltextarea[theKey].data.key_textarea = curComponent.initData.data.key_textarea;
                  this.key_labeltextarea++;

                  // populate this.initData_editgrid_radiotext 
                  if (this.initData_editgrid_radiotext[theKey] == null) {
                    this.initData_editgrid_radiotext[theKey] = {"data": {"key_editgrid_radiotext": []}};
                  }
                  let item = {};
                  item.key_comments = curComponent.initData.data.key_textarea;
                  item.key_questionLabel = curComponent.label_textarea;
                  item.hidden_key = theKey;
                  item.hidden_parent_key = theKey;
                  item.source_type = "labelTextarea";
                  this.initData_editgrid_radiotext[theKey].data.key_editgrid_radiotext[0] = item;
                  this.key_editgrid_radiotext++;

                  // populate this.initData_editgrid_radiotextList
                  if (this.initData_editgrid_radiotextList[theKey] == null) {
                    this.initData_editgrid_radiotextList[theKey] = {"data": {"key_editgrid_radiotext": []}};
                  }
                  
                  item = {};
                  item.key_comments = curComponent.initData.data.key_textarea;
                  item.key_radioButton = "";
                  item.hidden_key = theKey;
                  item.key_questionLabel = curComponent.label;
                  this.initData_editgrid_radiotextList[theKey].data.key_editgrid_radiotext[0] = item;
                  this.key_editgrid_radiotextList++;
                }
              } 
            }
          }
        }
        
      }

      //console.log("ssssssssssssthis.key_editgrid_radiotextList: ", this.key_editgrid_radiotextList);
      //console.log("ssssssssssssssssthis.initData_editgrid_intervention: ", this.initData_editgrid_intervention);
      // this.initData_editgrid_radiotextList = {
      //   "data": {
      //     "key_editgrid_radiotext": [
      //       {
      //         "hidden_key": "questionCombo_fr",
      //         "key_questionLabel": "Family Relationships",
      //         "key_comments": "Difficult family relationships",
      //         "key_radioButton": "P"
      //       }
      //     ]
      //   }
      // };
    },
    genRandomNumber() {
      return Math.floor(Math.random() * 100000);
    }
  }
}
</script>