<template>
    <div>
      <div v-for="(header, indexp) in dataModel.data" 
          :id="`${indexp}`"
          :key="indexp">
        <div v-for="(headerc, indexc) in header.subsections" :key="indexc">
          <div :id="`${indexp}${indexc}`" class="formio_anchor_class">
            <FormioQuestionCombo v-if="headerc.type === 'questionCombo'" :dataModel="headerc" />
            <FormioLabelTextarea v-else-if="headerc.type === 'labelTextarea'" :dataModel="headerc" />
            <FormioRadio v-else-if="headerc.type === 'radio'" :dataModel="headerc" />
            <FormioQuestionCetegoryTitle v-else-if="headerc.type === 'sectionTitle'" :dataModel="headerc"/>
            <FormioRadioTextarea v-else-if="headerc.type === 'radioTextarea'" :dataModel="headerc"/>
          </div>
        </div>
      </div>
      <FormioButton v-if="dataModel.saveBtnGroup != null" :dataModel="dataModel.saveBtnGroup" />
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
  }
}
</script>