<template>
    <!-- <Form :form="formJSON"/> -->
    
	<div class="divTable">
		<div class="divTableBody">
			<div class="divTableRow">
				<div class="divTableCell">
					<!-- Need to get two css classes for the a href.  1 for selected, the other for not selected. -->
					<span class="navHeaderA"><a v-for="(header, index) in navHeaders.parents" 
					:key="header" 
					:href="`#${index}${indexZero}`"
					:class="{active: index == currentSectionParent}"
					@click="setCurrentSectionParentChild"
					ref="parentAnchor">
					{{ header }}
					</a></span>
            			</div>
          		</div>
          	
          		<div class="divTableRow">
          			<div v-for="(header, indexp) in navHeaders.parentchild" 
              			:key="indexp" 
              			:class="[currentSectionParent == indexp ? 'divTableCell' : 'hide', '']">

			      {{ currentSectionParent }} {{ currentSectionChild }}
				      <!-- Same as above - Need to get two (different) css classes for the a href.  1 for selected, the other for not selected. -->
				      <span class="navHeaderA"><a v-for="(headerc, indexc) in navHeaders.parentchild[indexp].children" 
					:key="headerc" 
					:href="`#${indexp}${indexc}`"
					:class="{active: indexc == currentSectionChild}">

					{{ headerc }}
				      </a></span>
            			</div>
          		</div>
          	</div>
	</div>
    
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
          console.log("currentSectionChild: ");
          console.log(tmpID);

          if (tmpID) {
            this.currentSectionChild = tmpID;
            this.currentSectionParent = tmpID.substr(0, 1);
          }
        }
      })
    });
    document.querySelectorAll('h2').forEach((section) => {
      observer.observe(section)
    })
  },
  methods: {
    setCurrentSectionParentChild() {
      //let hashVal = this.$refs.parentAnchor.a.hash;
      //this.currentSectionParent = hashVal.substr(0, 1);
      this.currentSectionParent = '1';
      this.currentSectionChild = '10';
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
.divTable {
	display: table;
	width: 100%;
}
.divTableRow {
	display: table-row;
}
.divTableHeading {
	background-color: #EEE;
	display: table-header-group;
}
.divTableCell, .divTableHead {
	border: 1px solid #999999;
	display: table-cell;
	padding: 3px 10px;
}
.divTableHeading {
	background-color: #EEE;
	display: table-header-group;
	font-weight: bold;
}
.divTableFoot {
	background-color: #EEE;
	display: table-footer-group;
	font-weight: bold;
}
.divTableBody {
	display: table-row-group;
}
.navHeaderA {

}
</style>