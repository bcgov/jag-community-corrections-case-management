<template>
    <!-- <Form :form="formJSON"/> -->
    <main>
      <table class="table table-responsive" >
        <tbody v-for="item in navHeaders" :key="item.parent">
          <tr >
            <td>
              <input v-model="item.parent" type="text" />
            </td>
          </tr>
          <tr>
            <td class="table-success"> 
              {{ currentSection }}
              <a v-for="(header, index) in item.children" 
                :key="header" 
                :href="`#${index}`"
                :class="{active: index == currentSection}">

                {{ item.children[index] }}
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
//import onMounted from 'vue';

export default {
  name: 'CrnaCmpFormNavigation',
  // setup() {
  //   const currentSection = ref('');
  //   return { currentSection }
  // },
  props: {
    navHeaders: []
  },
  data() {
    return {
      currentSection : '',
    }
  },
  mounted() {
    const observer = new IntersectionObserver((entries) => {
      entries.forEach((entry) => {
        if (entry.intersectionRatio > 0 ) {
          console.log("currentSection: ");
          console.log(entry.target.getAttribute('id'));
          this.currentSection = entry.target.getAttribute('id')
        }
        })
      })
      document.querySelectorAll('h2').forEach((section) => {
      observer.observe(section)
    })
  },

}
</script>

<style scoped>
main {
  display:flex;
}
article {
  width: 75%;
  margin-bottom: 500px;
}
aside {
  width: 25%
}
aside > div {
  position: sticky;
  top: 20px;
  padding-left: 2em;
}
aside > div > a {
  display: block;
  color: #2c3e50;
  text-decoration: none;
  border-left: 1px solid #ccc;
  padding-left: 2em;
}
aside a.active {
  font-weight: bold;
  border-color: black;
}
</style>