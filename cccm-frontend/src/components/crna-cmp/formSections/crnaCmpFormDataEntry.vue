<template>
    <div>
      <div v-for="(header, indexp) in navHeaders.data" 
          :key="indexp"
          :class="[currentSectionParent == indexp ? 'show' : 'hide', 'table-success']">
        <div v-for="(headerc, indexc) in header.subsections" :key="indexc">
          <div :id="`${indexp}${indexc}`" class="formio_anchor_class">
            <FormioQuestionCombo :questionDataModel="headerc" />
          </div>
        </div>
      </div>
    </div>
</template>

<script>
import { Component, Vue } from 'vue-property-decorator';
import { ref, reactive } from '@vue/composition-api';
import FormioQuestionCombo from "@/components/common/FormioQuestionCombo.vue";


export default {
  name: 'CrnaCmpFormDataEntry',
  props: {
    navHeaders: {},
  },
  data() {
    return {
      currentSectionParent : '',
    }
  },
  components: {
    FormioQuestionCombo,
  }
}
</script>