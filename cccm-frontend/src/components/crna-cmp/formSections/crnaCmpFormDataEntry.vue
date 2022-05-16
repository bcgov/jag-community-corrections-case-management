<template>
    <div>
      <div v-for="(header, indexp) in dataModel.data" 
          :key="indexp"
          :class="[currentSectionParent == indexp ? 'show' : 'hide', '']">
        <div v-for="(headerc, indexc) in header.subsections" :key="indexc">
          <div :id="`${indexp}${indexc}`" class="formio_anchor_class">
            <FormioQuestionCombo :dataModel="headerc" />
          </div>
        </div>
        <CrnaCmpFormSaveBtnGroup />
      </div>
    </div>
</template>

<script>
import { Component, Vue } from 'vue-property-decorator';
import { ref, reactive } from '@vue/composition-api';
import FormioQuestionCombo from "@/components/common/FormioQuestionCombo.vue";
import CrnaCmpFormSaveBtnGroup from "@/components/common/FormioDraftBtnGroup.vue";


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
    CrnaCmpFormSaveBtnGroup
  }
}
</script>