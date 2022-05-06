import Vue from 'vue'
import VueCompositionAPI, { createApp, h } from '@vue/composition-api'

import App from './App.vue'
import router from './router'

Vue.use(VueCompositionAPI)

const app = createApp({
  router,
  data() {
    return {
      locationVal: ""
    }
  },
  created () {
    this.getLocation();
    // dynamically render the location info once page is rendered
    let varLocation = document.getElementById('id_location');
    varLocation.textContent = "BC Corrections " + this.locationVal;
  },
  methods: {
    getLocation() {
      this.locationVal = "Victoria Probation Office";
    }
  },
  render: () => h(App)
})

app.mount('#app')
