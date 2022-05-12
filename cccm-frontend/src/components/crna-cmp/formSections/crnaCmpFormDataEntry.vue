<template>
    <!-- <formio :form= formJSON /> -->
    <div style="width: 100%; ">
      <div v-for="(header, indexp) in navHeaders.parentchild" 
          :key="indexp"
          :class="[currentSectionParent == indexp ? 'show' : 'hide', 'table-success']">
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
import { ref, reactive } from '@vue/composition-api';
import { Form } from 'vue-formio';

export default {
  name: 'CrnaCmpFormDataEntry',
  props: {
    //formJSON: {},
    navHeaders: [],
    // currentSectionChild: '0',
    //currentSectionParent: '0'
  },
  data() {
    return {
      //currentSectionChild : '',
      currentSectionParent : '',
      //indexZero: '0'
    }
  },
  // mounted() {
  //   const observer = new IntersectionObserver((entries) => {
  //     entries.forEach((entry) => {
  //       if (entry.intersectionRatio > 0 ) {
  //         console.log("currentSectionChild: ");
  //         console.log(entry.target.getAttribute('id'));
  //         this.currentSectionChild = entry.target.getAttribute('id');
  //         this.currentSectionParent = entry.target.getAttribute('id').substr(0,1);
  //         }
  //       })
  //     })
  //     document.querySelectorAll('div').forEach((section) => {
  //     observer.observe(section)
  //   })
  // },
  components: {
    formio : Form
  }
}
</script>
