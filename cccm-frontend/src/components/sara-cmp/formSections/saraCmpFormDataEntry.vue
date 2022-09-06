<template>
  <div>
    <Form :key="pageKey" :form="dataModel" :submission="initData" 
      v-on:editGridSaveRow="handleEditGridSaveRowEvent"
      v-on:change="handleChangeEvent" 
      v-on:blur="handleBlurEvent" 
    />
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import { objectToString } from '@vue/shared';

export default {
  name: 'saraCmpForm',
  props: {
    dataModel: {},
    initData: {},
    dataMap: {}
  },
  components: {
    Form
  },
  data() {
    return {
      pageKey: 0,
    }
  },
  methods: {
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
        
        // Refresh the view
        this.pageKey++;
      }
      
    },
    handleChangeEvent(event) {
      if (   event.changed 
          && ( event.changed.component.type === "textfield"
            || event.changed.component.type === "textarea")) {
        console.log("handleChangeEvent: ", event, event.changed.component.key);      
        this.triggerAutoSave = true;
      }

      // Trigger auto save
      if (   event.changed 
          && ( event.changed.component.type === "radio"
            || event.changed.component.type === "checkbox"
            || event.changed.component.type === "select")
          ) {
            console.log("AutoSave triggered: ", event, event.changed.component.key);
            
      }
    },
    handleBlurEvent(event) {
      if (this.triggerAutoSave) {
        console.log("AutoSave blur triggered: ", event);
        this.triggerAutoSave = false;
        
      }
    }
  }
}
</script>