<template>
  <div class="divTable">
    <div class="divTableBody">
      <div class="divTableRowL1 divTableRowNav">
        <div class="divTableCell">
	        <span v-for="(header, index) in data_formEntries.components" :key="index">
            <a v-if="index < data_formEntries.components.length - 1"
              :key="index" 
              :href="`#${index}${indexZero}`"
              :class="[index == currentSectionParent ? 'active' : '', 'navHeaderA-L1']"
              @click="setCurrentSectionParentChild">
              {{ header.label }}
            </a>
          </span>
        </div>
      </div>
      <div class="divTableRowL2 divTableRowNav">
        <span v-for="(header, indexp) in data_formEntries.components" :key="indexp">
          <!-- To skip the button components-->
          <div v-if="indexp < data_formEntries.components.length - 1" 
              :key="indexp"
              :class="[currentSectionParent == indexp ? 'divTableCell' : 'hide', '']">
              <!-- {{ currentSectionParent }} {{ currentSectionChild }} -->
              <span v-if="header.custom_subNavOnOff != null && header.custom_subNavOnOff == 'on'">
                <a v-for="(headerc, indexc) in header.components" 
                  :key="headerc.key" 
                  :href="`#${indexp}${indexc}`"
                  :class="[indexc == currentSectionChild ? 'active' : '', 'navHeaderA-L2']"
                  @click="setCurrentSectionParentChild">
                  {{ headerc.label }}
                </a>
              </span>
          </div>
        </span>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { ref, reactive } from '@vue/composition-api';

export default {
  name: 'CrnaCmpFormNavigation',
  props: {
    dataModel: {},
    // param passed from parent to indicate move to the next parentNav
    parentNavMoveToNext: {
      type: Number,
      default: 1,
    }
  },
  data() {
    return {
      //const
      CUSTOM_SECTION_PREFIX: 'custom_section_',
      CUSTOM_QUESTION_PREFIX: 'custom_question_',
      observer : null,
      currentSectionChild : '0',
      currentSectionParent : '0',
      indexZero: '0',
      timeoutDelay: 1000,
      initLoad: true,

      data_formEntries: {"display": "form"},
      data_rightPanel: {},
   }
  },
  watch: {
    parentNavMoveToNext() {
      let parentNavPos = (parseInt(this.currentSectionParent) + 1).toString();
      let childNavPos = '0';
      this.showHideWrapper(parentNavPos, childNavPos);

      // Move the position to the top by simulating an anchor click
      let hrefVal = '#' + parentNavPos + childNavPos;
      let selector = 'a[href="' + hrefVal + '"]'
      let theAnchor = document.querySelector(selector);
      if (theAnchor != null) {
        theAnchor.click();
      }
    }
  },
  mounted() {
    this.private_loadData();

    setTimeout(() => {
      this.observer = new IntersectionObserver((entries) => {
        entries.forEach((entry) => {
          //console.log("Intersection ratio: ", entry.intersectionRatio);
          if (this.initLoad) {
            this.timeoutDelay = 2500;
            this.showHideWrapper(0, 0);
            this.initLoad = false;
          } else {
            if (entry &&
                entry.isIntersecting &&
                entry.intersectionRatio > 0) {
              //console.log("Entry: ", entry);
              let tmpID_index = entry.target.className.indexOf(this.CUSTOM_QUESTION_PREFIX) + this.CUSTOM_QUESTION_PREFIX.length;
              let tmpID = entry.target.className.substring(tmpID_index, tmpID_index + 3);
              //console.log("tmpID on mounted: ", tmpID);
              if (tmpID) {
                this.currentSectionParent = tmpID.substr(0, 1);
                this.currentSectionChild = tmpID.substr(1);
                
                this.showHideWrapper(this.currentSectionParent, this.currentSectionChild);
              }
            }
          }
        })
      });
      let className = '[class*="' + this.CUSTOM_QUESTION_PREFIX + '"]';
      document.querySelectorAll(className).forEach((section) => {
        this.observer.observe(section);
      });
    }, this.timeoutDelay);
  },
  destroyed(){
    if (this.observer != null) {
      console.log("Destroyed, disconnect observer");
      this.observer.disconnect();
    }
  },
  methods: {
    private_loadData() {
      const formdata = this.dataModel.components.filter(obj => {
        return obj.key === 'section_data';
      });
      this.data_formEntries = formdata[0];
      //console.log("this.data_formEntries: ", this.data_formEntries);

      const rightPanelData = this.dataModel.components.filter(obj => {
        return obj.key === 'section_rightpanel';
      });
      this.data_rightPanel = rightPanelData[0];
      //console.log("this.data_rightPanel: ", this.data_rightPanel);
    },
    // method corresponds to clicking on parent nav link, it always sets the currentSectionChild to '0'
    setCurrentSectionParentChild(e) {
      if (e.target && e.target.hash) {
        // a sample of hash value: #00
        //console.log("e: ", e.target.hash.substr(1, 1), e.target.hash.substr(2, 1));
        this.showHideWrapper(e.target.hash.substr(1, 1), e.target.hash.substr(2, 2));
      }
    },
    showHideWrapper(posParentNav, posChildNav) {
      this.currentSectionParent = posParentNav; 
      this.currentSectionChild = posChildNav;

      //console.log("Local click:", this.currentSectionParent, this.currentSectionChild);

      // emit an event, parentNavClicked, to the parent, so parent knows the currentSectionParent
      this.$emit('parentNavClicked', this.currentSectionParent);
                    
      // show/hide sideCards panels
      this.showHideRightsidePanels();

      // show/hide questions
      this.showHideSections();
    },
    showHideRightsidePanels() {
      // show all panels
      if (this.data_rightPanel != null && this.data_rightPanel.components != null) {
        for (let i = 0; i < this.data_rightPanel.components.length; i++) {
          //console.log(this.dataModel.rightPanel[i].panelKey);
          let className = '[class*="' + this.data_rightPanel.components[i].key + '"]';
          let thePanel = document.querySelector(className);
          //console.log("className: ", className, thePanel);
          if (thePanel != null) {
            thePanel.setAttribute('style', 'display:block');
          }
        }
      }
      
      // hide the panel
      if (this.data_formEntries.components != null && this.data_formEntries.components.length > 0) {
        let sideCardsPanelHiddenList = this.data_formEntries.components[this.currentSectionParent].sideCardPanelHiddenList;
        //console.log("sideCardsPanelHiddenList: ", sideCardsPanelHiddenList);
        if (sideCardsPanelHiddenList != null) {
          for (let i = 0; i < sideCardsPanelHiddenList.length; i++) {
            //console.log('formio-component-' + sideCardsPanelHiddenList[i]);
            let className = '[class*="' + sideCardsPanelHiddenList[i] + '"]';
            let thePanel = document.querySelector(className);
            if (thePanel != null) {
              thePanel.setAttribute('style', 'display:none');
            }
          }
        }
      }
    },
    showHideSections() {
      //console.log("this.currentSectionParent, this.currentSectionChild: ", this.currentSectionParent, this.currentSectionChild);
      // show questions
      if (this.data_formEntries.components != null && this.data_formEntries.components.length >= 1) {
        //console.log("panel is not null: ",  this.data_formEntries.components.length);
        for (let i = 0; i < this.data_formEntries.components.length - 1; i++) {
          let className = '[class*="' + this.CUSTOM_SECTION_PREFIX + i + '0"]';
          let thePanel = document.querySelector(className);

          if (thePanel != null) {
            // console.log(this.currentSectionParent, i, thePanel);
            if (this.currentSectionParent == i) {
              thePanel.setAttribute('style', 'display:block');
              if (this.currentSectionChild != 0) {
                let questionClassName = '[class*="' + this.CUSTOM_QUESTION_PREFIX + i + this.currentSectionChild + '"]';
                let theQuestionPanel = document.querySelector(questionClassName);
                //console.log("theQuestionPanel: ", theQuestionPanel);
                if (theQuestionPanel != null) {
                  //theQuestionPanel.scrollIntoView({ behavior: 'smooth', block: 'center' });
                  //window.scrollTo(0, theQuestionPanel.offsetTop);
                  //theQuestionPanel.scrollTop(5);
                  theQuestionPanel.scrollIntoView(false);
                }
              }
            } else {
              thePanel.setAttribute('style', 'display:none');
            }
          } 
        }
      } 
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