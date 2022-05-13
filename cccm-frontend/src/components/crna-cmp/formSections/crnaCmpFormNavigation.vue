<template>
    <table >
      <tbody>
        <tr >
          <td>
            <a v-for="(header, index) in navHeaders.data" 
              :key="header.section" 
              :href="`#${index}${indexZero}`"
              :class="{active: index == currentSectionParent}"
              @click="setCurrentSectionParentChild"
              ref="parentAnchor">
              {{ header.section }}
            </a>
          </td>
        </tr>
        <tr v-for="(header, indexp) in navHeaders.data" :key="indexp"
            :class="[currentSectionParent == indexp ? 'show' : 'hide', 'table-success']">
          <td> 
            <!-- {{ currentSectionParent }} {{ currentSectionChild }} -->
            <a v-for="(headerc, indexc) in header.subsections" 
              :key="headerc.questionLabel" 
              :href="`#${indexp}${indexc}`"
              :class="{active: indexc == currentSectionChild}">
              {{ headerc.questionLabel }}
            </a>
          </td>
        </tr>
      </tbody>
    </table>
</template>

<script>

import { Component, Vue } from 'vue-property-decorator';
import { ref, reactive } from '@vue/composition-api';

export default {
  name: 'CrnaCmpFormNavigation',
  props: {
    navHeaders: {}
  },
  data() {
    return {
      currentSectionChild : '00',
      currentSectionParent : '0',
      indexZero: '0'
    }
  },
  mounted() {
    const observer = new IntersectionObserver((entries) => {
      entries.forEach((entry) => {
        if (entry.intersectionRatio > 0 ) {
          let tmpID = entry.target.getAttribute('id');
          if (tmpID) {
            this.currentSectionChild = tmpID.substr(1, 1);
            this.currentSectionParent = tmpID.substr(0, 1);
          }
          // console.log("currentSectionChild: ");
          // console.log(this.currentSectionChild);
          // console.log("currentSectionParent: ");
          // console.log(this.currentSectionParent);
        }
      })
    });
    document.querySelectorAll('div.formio_anchor_class').forEach((section) => {
      observer.observe(section)
    })
  },
  methods: {
    setCurrentSectionParentChild() {
      //let hashVal = this.$refs.parentAnchor.a.hash;
      //this.currentSectionParent = hashVal.substr(0, 1);
      this.currentSectionParent = '1';
      this.currentSectionChild = '0';
      // console.log("currentSectionParent set to: ");
      // console.log(this.currentSectionParent);
      // console.log("currentSectionChild set to: ");
      // console.log(this.currentSectionChild);
    }
  }
}
</script>

<style scoped>
div > a {
	display: block;
	color: #2c3e50;
	text-decoration: none;
	border-left: 1px solid #ccc;
	padding-left: 0em;
}
a.active {
	font-weight: bold;
	border-color: black;
}
.hide {
	display: none;
}
.show {
	display: block;
}
</style>