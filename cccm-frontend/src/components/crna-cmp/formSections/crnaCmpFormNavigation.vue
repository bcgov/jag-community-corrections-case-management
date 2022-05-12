<template>
    <!-- <Form :form="formJSON"/> -->
    <main style="width: 100%; padding-left: 2em;">
      <table >
        <tbody>
          <tr >
            <td>
              <a v-for="(header, index) in navHeaders.parents" 
                :key="header" 
                :href="`#${index}${indexZero}`"
                :class="{active: index == currentSectionParent}"
                @click="setCurrentSectionParentChild"
                ref="parentAnchor">
                {{ header }}
              </a>
            </td>
          </tr>
          <tr v-for="(header, indexp) in navHeaders.parentchild" 
              :key="indexp" 
              :class="[currentSectionParent == indexp ? 'show' : 'hide', 'table-success']">
            <td> 
              {{ currentSectionParent }} {{ currentSectionChild }}
              <a v-for="(headerc, indexc) in navHeaders.parentchild[indexp].children" 
                :key="headerc" 
                :href="`#${indexp}${indexc}`"
                :class="{active: indexc == currentSectionChild}">

                {{ headerc }}
              </a>
            </td>
          </tr>
        </tbody>
      </table>
    </main>
</template>

<script>

import { Component, Vue } from 'vue-property-decorator';
import { ref, reactive } from '@vue/composition-api';

export default {
  name: 'CrnaCmpFormNavigation',
  props: {
    navHeaders: []
  },
  data() {
    return {
      currentSectionChild : '0',
      currentSectionParent : '0',
      indexZero: '0'
    }
  },
  mounted() {
    const observer = new IntersectionObserver((entries) => {
      entries.forEach((entry) => {
        if (entry.intersectionRatio > 0 ) {
          console.log("currentSectionChild: ");
          console.log(entry.target.getAttribute('id'));
          this.currentSectionChild = entry.target.getAttribute('id');
          this.currentSectionParent = entry.target.getAttribute('id').substr(0, 1);
          }
        })
      })
      document.querySelectorAll('h2').forEach((section) => {
      observer.observe(section)
    })
  },
  methods: {
    setCurrentSectionParentChild() {
      //let hashVal = this.$refs.parentAnchor.a.hash;
      //this.currentSectionParent = hashVal.substr(0, 1);
      this.currentSectionParent = '1';
      this.currentSectionChild = '0';
      //console.log("currentSectionParent set to: ");
      //console.log(this.currentSectionParent);
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