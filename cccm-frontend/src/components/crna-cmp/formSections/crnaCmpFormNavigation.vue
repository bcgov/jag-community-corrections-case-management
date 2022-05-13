<template>
  <div class="divTable">
		<div class="divTableBody">
			<div class="divTableRow">
				<div class="divTableCell">
            <!-- Need to get two css classes for the a href.  1 for selected, the other for not selected. -->
					<span class="navHeaderA navHeaderA-L1">
            <a v-for="(header, index) in navHeaders.data" 
              :key="header.section" 
              :href="`#${index}${indexZero}`"
              :class="[index == currentSectionParent ? 'active' : '', 'navHeaderA-L2']"
              @click="setCurrentSectionParentChild"
              ref="parentAnchor">
              {{ header.section }}
            </a>
          </span>
        </div>
      </div>
      <div class="divTableRow">
        <div v-for="(header, indexp) in navHeaders.data" :key="indexp"
            :class="[currentSectionParent == indexp ? 'divTableCell' : 'hide', '']">
           
            <!-- {{ currentSectionParent }} {{ currentSectionChild }} -->
            <span class="navHeaderA navHeaderA-L1">
              <a v-for="(headerc, indexc) in header.subsections" 
                :key="headerc.questionLabel" 
                :href="`#${indexp}${indexc}`"
                :class="[indexc == currentSectionChild ? 'active' : '', 'navHeaderA-L2']">
                {{ headerc.questionLabel }}
              </a>
            </span>
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
    navHeaders: {}
  },
  data() {
    return {
      observer : null,
      currentSectionChild : '00',
      currentSectionParent : '0',
      indexZero: '0',
    }
  },
  mounted() {
    setTimeout(() => {
      this.observer = new IntersectionObserver((entries) => {
        entries.forEach((entry) => {
          //console.log("Intersection ratio: ", entry.intersectionRatio);
          if (entry &&
              entry.isIntersecting &&
              entry.intersectionRatio > 0) {
            let tmpID = entry.target.getAttribute('id');
            //console.log("subsectionID: ", tmpID);
            if (tmpID) {
              this.currentSectionChild = tmpID.substr(1, 1);
              this.currentSectionParent = tmpID.substr(0, 1);
            }
          }
        })
      }
      );
      document.querySelectorAll('div.formio_anchor_class').forEach((section) => {
        this.observer.observe(section);
      });
    }, 1000);
  },
  destroyed(){
    console.log("Destroyed, disconnect observer");
    this.observer.disconnect();
  },
  methods: {
    // method corresponds to clicking on parent nav link, it alwasy set the currentSectionChild to '0'
    setCurrentSectionParentChild(e) {
      if (e.target && e.target.hash) {
        this.currentSectionParent = e.target.hash.substr(1, 1);
        this.currentSectionChild = '0';
      }
      console.log(`parent: ${this.currentSectionParent}; child: ${this.currentSectionChild}`);
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