<template>
  <div class="container justify-content-start  m-3 ">
    <div class="row w-75">
      <div class="btn-group" role="group" aria-label="Basic example">
        <v-btn-toggle
          v-model="toggle_exclusive"
          mandatory
        ><v-btn @click="activate('graph')" :class="getActiveClass('graph')" >
          <i class="fas fa-chart-bar"> </i> <span class="ms-3 flex-0  ml-3">Graph</span>
          </v-btn>
          <!-- <v-btn @click="activate('comments')"  :class="getActiveClass('comments')">
            <i class="fas fa-comments"></i><span class="ms-3  ml-3">Comments ({{ commentCount }})</span>
          </v-btn>
          <v-btn  @click="activate('interventions')"  :class="getActiveClass('interventions')">
            <i class="fas fa-warning"></i> <span class="ms-3 ml-3">Interventions ({{ interventionCount }})</span>
          </v-btn> -->
          <v-btn  @click="activate('combined')"  :class="getActiveClass('combined')">
            <i class="fas fa-comments"></i> <span class="ms-3 ml-3">Comments ({{ commentCount }}) & interventions ({{ interventionCount }})</span>
          </v-btn>
        </v-btn-toggle>
      </div>
    </div>

  </div>
</template>

<script>

import {mapState, mapStores} from "pinia/dist/pinia";
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
    ...mapState(trendStore, ['factors', 'startDate', 'endDate','commentCount','interventionCount'])
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
</style>