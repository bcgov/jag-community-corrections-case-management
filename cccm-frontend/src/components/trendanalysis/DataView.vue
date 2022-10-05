<template>
  <div class="container justify-content-start  m-3 ">
    <div class="row w-75">
      <div class="btn-group" role="group" aria-label="Basic example">

        <button type="button" @click="activate('graph')" :class="getActiveClass('graph')" class="btn">
          <font-awesome-icon icon="fa-solid fa-chart-bar" class="button-icon"/>
          <span class="ms-3 flex-0  ml-3">Graph</span></button>
        <button type="button" @click="activate('comments')"  :class="getActiveClass('comments')" class="btn">
          <font-awesome-icon icon="fa-solid fa-comments" class="button-icon"/>
          <span class="ms-3  ml-3">Comments ({{ commentCount }})</span></button>
        <button type="button" @click="activate('interventions')"  :class="getActiveClass('interventions')" class="btn block">
          <font-awesome-icon icon="fa-solid fa-warning pr-4" class="button-icon"/>
          <span class="ms-3 ml-3">Interventions ({{ interventionCount }})</span></button>
      </div>
    </div>

  </div>
</template>

<script>

import {mapState, mapStores} from "pinia";

export default {
  name: "DataView",
  data() {
    return {
      active: '',
      interventionCount: 5,
      commentCount: 10
    }
  },
  // computed: mapState(['factors','commentCount','interventionCount','advancedFilter', 'filterStartDate','filterEndDate']),
  watch: {
    commentCount(newValue) {
      console.log("Count CHANGE!! %o",newValue);
    },
    filters(newValue, oldValue) {
      console.log("FILTER CHANGE!! %o %o", newValue, oldValue);
    }
  },
  props: {
    selectedView: {
      type: String,
      required: true
    },
  },

  methods: {
    getActiveClass(button) {
      return ( button === this.active) ? 'btn-primary' : 'btn-outline-secondary';
    },
    activate(button) {
      this.active = button;
      console.log("Button %o", button);
      this.$emit('changeView',this.active);
    }
  },
  mounted() {
    this.active = this.selectedView;
  },
  updated() {
    console.log("Data view updated");
  }
}
</script>

<style scoped>
.button-icon {
  position: absolute;
  left: 8px;
  top: 8px;
}
</style>