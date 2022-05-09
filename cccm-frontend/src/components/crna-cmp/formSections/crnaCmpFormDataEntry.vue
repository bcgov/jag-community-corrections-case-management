<template>
    <!-- <formio :form= formJSON /> -->
    <div>
      <div v-for="item in navHeaders" :key="item.parent">
        <div v-for="(header, index) in item.children" :key="index">
          <h2 :id="index">{{ header }}</h2>
          <p>
            Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
          </p>
        </div>
      </div>
    </div>
</template>

<script>
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';

export default {
  name: 'CrnaCmpFormDataEntry',
  props: {
    //formJSON: {},
    navHeaders: []
  },
  components: {
    formio : Form
  }
}
</script>