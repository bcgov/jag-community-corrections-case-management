<template>
  <div class="container justify-content-start  m-3 ">
    <div class="row w-75">
      <div class="btn-group" role="group" aria-label="Basic example">
        <v-btn-toggle
          v-model="toggle_exclusive"
          mandatory
        >
          <v-btn @click="activate('graph')" :class="getActiveClass('graph')" >
            <i class="fas fa-chart-bar labelclass"> </i> 
            <span class="ms-3 flex-0 ml-3 labelclass">Graph</span>
          </v-btn>
          <v-btn  @click="activate('combined')"  :class="getActiveClass('combined')">
            <i class="fas fa-comments labelclass"></i> 
            <span class="ms-3 ml-3 labelclass">{{ getTabLabel }}</span>
          </v-btn>
        </v-btn-toggle>
      </div>
    </div>

  </div>
</template>

<script>

import {mapState, mapStores} from "pinia";
import { trendStore } from '@/stores/trendstore';

export default {
  name: "DataView",
  data() {
    return {
      active: '',
      toggle_exclusive: ''
    }
  },

  computed: {
    getTabLabel() {
      let label = "Comments (" + this.commentCount + ")";
      let theForm = this.$FORM_INFO.filter( item => item.formType === this.formType );
      if (theForm != null && theForm[0] != null && theForm[0].cmp) {
        label += " & Interventions (" + this.interventionCount + ")";
      }
      return label;
    },
    ...mapState(trendStore, ['factors', 'startDate', 'endDate','commentCount','interventionCount', 'formType'])
  },
  props: {
    selectedView: {
      type: String,
      required: true
    },
  },

  methods: {
    getActiveClass(button) {
      return ( button === this.active) ? 'primary' : 'black';
    },
    activate(button) {
      this.active = button;
      this.$emit('changeView',this.active);
    }
  },
  mounted() {
    this.active = this.selectedView;
  },

}
</script>

<style scoped>
.button-icon {
  position: absolute;
  left: 8px;
  top: 8px;
}

.labelclass {
  color: white;
}

.primary {
    background: #38598a!important;
}

.black {
    background: grey!important;
}
</style>