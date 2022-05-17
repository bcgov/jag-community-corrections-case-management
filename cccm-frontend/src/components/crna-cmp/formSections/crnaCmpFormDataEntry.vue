<template>
    <div>
      <div v-for="(header, indexp) in dataModel.data" 
          :key="indexp"
          :class="[currentSectionParent == indexp ? 'show' : 'hide', '']">
        <div v-for="(headerc, indexc) in header.subsections" :key="indexc">
          <div :id="`${indexp}${indexc}`" class="formio_anchor_class">
            <FormioQuestionCombo v-if="headerc.type === 'questionCombo'" :dataModel="headerc" />
            <FormioLabelTextarea v-else-if="headerc.type === 'labelTextarea'" :dataModel="headerc" />
            <FormioRadio v-else-if="headerc.type === 'radio'" :dataModel="headerc" />
            <!-- <div v-else>
              Not A/B/C
            </div> -->

          </div>
        </div>
      </div>
      <CrnaCmpFormSaveBtnGroup />
    </div>
</template>

<script>
import { Component, Vue } from 'vue-property-decorator';
import { ref, reactive } from '@vue/composition-api';
import FormioQuestionCombo from "@/components/common/FormioQuestionCombo.vue";
import CrnaCmpFormSaveBtnGroup from "@/components/common/FormioDraftBtnGroup.vue";
import FormioLabelTextarea from "@/components/common/FormioLabelTextArea.vue";
import FormioRadio from "@/components/common/FormioRadio.vue";

export default {
  name: 'CrnaCmpFormDataEntry',
  props: {
    dataModel: {},
  },
  data() {
    return {
      currentSectionParent : '',
    }
  },
  components: {
    FormioQuestionCombo,
    FormioLabelTextarea,
    FormioRadio,
    CrnaCmpFormSaveBtnGroup
  }
}
</script>