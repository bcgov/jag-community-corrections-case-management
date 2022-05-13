<template>
  <div class="divTable">
    <div class="divTableBody">
      <div class="divTableRowL1 divTableRowNav">
        <div class="divTableCell">
	  <span>
            <a v-for="(header, index) in navHeaders.data" 
              :key="header.section" 
              :href="`#${index}${indexZero}`"
              :class="[index == currentSectionParent ? 'active' : '', 'navHeaderA-L1']"
              @click="setCurrentSectionParentChild"
              ref="parentAnchor">
              {{ header.section }}
            </a>
          </span>
        </div>
      </div>
      <div class="divTableRowL2 divTableRowNav">
        <div v-for="(header, indexp) in navHeaders.data" :key="indexp"
            :class="[currentSectionParent == indexp ? 'divTableCell' : 'hide', '']">
           
            <!-- {{ currentSectionParent }} {{ currentSectionChild }} -->
            <span>
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

.divTableRowNav {
	padding: 15px 20px 25px 20px;
	background-color: #fff;
	box-shadow:0 5px 10px 0px #ddd;
}

.divTableRowL1 {
	padding: 15px 20px 25px 20px;
	border-left:1px solid #ddd;
	border-right:1px solid #ddd;
	border-top:1px solid #ddd;
	border-bottom:0px solid #fff;
}

.divTableRowL2 {
	padding: 0px 6px 12px 6px;
	border-left:1px solid #ddd;
	border-right:1px solid #ddd;
	border-bottom:1px solid #ddd;
	border-top:0px solid #fff;
  	line-height: 3.1;
}
.divTableHeading {
	background-color: #EEE;
	display: table-header-group;
}
.divTableCell, .divTableHead {
	border: 0px solid #999999;
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
a.navHeaderA-L1.active {
	color:#003366;
	background-repeat:no-repeat;
	background-position: center bottom;
	background-image:url(/src/assets/css/navArrow.jpg);
}

a.navHeaderA-L1 {
	font-weight: 600;
	color:#000;
	margin:0px 20px;
	padding:5px 25px 20px 25px;
	background-repeat:no-repeat;
	background-position: center bottom;
	background-image:url(/src/assets/css/navArrowNotSelected.jpg);	
}

a.navHeaderA-L1:hover {
	color:#003366;
	text-decoration:none;
	background-repeat:no-repeat;
	background-position: center bottom;
	background-image:url(/src/assets/css/navArrow.jpg);
}

a.navHeaderA-L2.active {
	font-size: .8rem;
	background-color:#BFD4E9;
	color: #436492;
	font-weight: 600;
	padding: 15px 8px;
	border: 1px solid #BFD4E9;	

}

a.navHeaderA-L2 {
	font-size: .8rem;
	color:#436492;
	font-weight: 600;
	padding: 15px 8px;
	background-color:#DBE2E9;
	border: 1px solid #DBE2E9;	
}

a.navHeaderA-L2:hover {
	color:#000;
	border: 1px solid #003366;
	text-decoration:none;
	-o-transition:.8s;
	-ms-transition:.8s;
	-moz-transition:.8s;
	-webkit-transition:.8s;
	/* ...and now for the proper property */
	transition:.8s;		
}
</style>