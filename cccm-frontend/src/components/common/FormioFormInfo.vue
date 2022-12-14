<template>
  <Form :form="formJSON" :submission="dataModel" @evt_editForm="handleUnlockForm"/>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import templateFormInfo from '@/components/common/templateFormInfo.json';

export default {
  name: 'FormioFormInfo',
  props: {
    dataModel: {},
  },
  data() {
    return {
      formInfoTemplate : templateFormInfo,
      formJSON : {},
    }
  },
  components: {
    Form
  },
  mounted(){
    this.buildFormInfoDataEntry()
  },
  methods: {
    buildFormInfoDataEntry() {
      // make a deep copy of the template
      let tmpJSONStr = JSON.stringify(this.formInfoTemplate);
      let tmpJSON = JSON.parse(tmpJSONStr);
      this.formJSON = tmpJSON;

      if (this.dataModel != null && this.dataModel.data != null) {
        let formTypeLocation = "";
        if (this.dataModel.data.assessmentStatusRequired) {
          formTypeLocation = "<div class='mb-3 mt-3'><Strong>{{data.formTypeLabel}}: </Strong>{{data.clientFormType}}</div><div><Strong>Location: </Strong>{{data.location}}</div>";
        } else {
          formTypeLocation = "<div class='mb-3 mt-3'><Strong>Location: </Strong>{{data.location}}</div>"
        }
        this.dataModel.data.formTypeLocation = formTypeLocation;
      }
    },
    handleUnlockForm(evt) {
      if (evt != null && evt.type === 'evt_editForm' ) {
        this.$emit('unlockForm');
      }
    }
  },
}
</script>

<style>
.custom_wrap {
  color: #FCBA19;
  text-align:center;
  margin: 10px;
  padding: 10px;
  background-color:#FDF4DF;
  font-size: 25px;
}
</style>