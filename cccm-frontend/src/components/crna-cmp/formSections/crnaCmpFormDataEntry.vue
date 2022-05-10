<template>
    <!-- <formio :form= formJSON /> -->
    <div style="width: 100%; padding-left: 2em">
      <div v-for="(header, indexp) in navHeaders.parentchild" :key="indexp">
        <div v-for="(header, indexc) in navHeaders.parentchild[indexp].children" :key="indexc">
          <h2 :id="`${indexp}${indexc}`">{{ navHeaders.parentchild[indexp].children[indexc] }}</h2>
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

<style>
table {
  width: 100%;
  padding-left: 200px;
}
</style>