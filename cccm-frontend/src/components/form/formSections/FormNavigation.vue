<template>
  <div class="divTable">
    <div class="divTableBody">
      <div class="divTableRowL1 divTableRowNav">
        <div class="divTableCell">
	        <span v-for="(header, index) in dataModel.components" :key="index">
            <a v-if="index < dataModel.components.length - 1" :key="index" 
              :href="`#${index}${indexZero}`"
              :class="[index == currentSectionParent ? 'active' : '', 'navHeaderA-L1']"
              @click="setCurrentSectionParentChild">
              {{ header.label }}
            </a>
          </span>
        </div>
      </div>
      <div class="divTableRowL2 divTableRowNav">
        <span v-for="(header, indexp) in dataModel.components" :key="indexp">
          <!-- To skip the button components-->
          <div v-if="indexp < dataModel.components.length - 1" :key="indexp"
              :class="[currentSectionParent == indexp ? 'divTableCell' : 'hide', '']">
              <!-- {{ currentSectionParent }} {{ currentSectionChild }} -->
              <span v-if="header.custom_subNavOn != null && header.custom_subNavOn">
                <span v-for="(headerc, indexc) in header.components" :key="headerc.key"> 
                  <a  v-if="indexc > 0"
                    :key="headerc.key" 
                    :href="`#${indexp}${indexc}`"
                    :class="[indexc == currentSectionChild ? 'active' : '', 'navHeaderA-L2']"
                    @click="setCurrentSectionParentChild">
                    {{ headerc.navText }}
                  </a>
                </span>
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
  name: 'FormNavigation',
  props: {
    dataModel: {},
    // param passed from parent to indicate move to the next parentNav
    parentNavMoveToNext: {
      type: Number,
      default: 1,
    },
    parentNavJumpToPointed: {
      type: String,
      default: '0',
    }
  },
  data() {
    return {
      //const
      CUSTOM_QUESTION_PREFIX: 'question_panel_',
      ENTRY_TARGET_TIMEOUT: 100,
      observer : null,
      currentSectionChild : '1',
      currentSectionParent : '0',
      indexZero: '1',
      timeoutDelay: 2000,
      initLoad: true,

      data_rightPanel: {},
   }
  },
  watch: {
    parentNavJumpToPointed() {
      //console.log("nav watch: ", this.parentNavJumpToPointed );
      //this.parentNavJumpToPointed sample value: 1Q3_0
      // Need to get the value before _
      let navSection = this.parentNavJumpToPointed.split("_");
      if (navSection != null && navSection.length == 2){
        let nav = navSection[0].split("Q");
        if (nav != null && nav.length == 2){
          this.private_moveNav((parseInt(nav[0])).toString(), (parseInt(nav[1])).toString());
        }
      }
    },
    parentNavMoveToNext() {
      this.private_moveNav((parseInt(this.currentSectionParent) + 1).toString(), '1');
    }
  },
  mounted() {
    setTimeout(() => {
      // Keep track of when the entry transitioned to a visible state.
      let visibleSince = 0;
      this.observer = new IntersectionObserver((entries) => {
        entries.forEach((entry) => {
          //console.log("entry: ", entry, entry.target.className, entry.isIntersecting, entry.isVisible, entry.time);
          if (typeof entry.isVisible === 'undefined') {
            // The browser doesn't support Intersection Observer v2, falling back to v1 behavior.
            entry.isVisible = true;
          };
          if (entry.isIntersecting && entry.isVisible) {
            visibleSince = entry.time;
            //console.log("Entry: ", entry);
            // Sample value for entry.target.className: question_panel_S02Q01
            let tmpID_index = entry.target.className.indexOf(this.CUSTOM_QUESTION_PREFIX) + this.CUSTOM_QUESTION_PREFIX.length;
            let tmpID = entry.target.className.substring(tmpID_index, tmpID_index + 6);
            //console.log("tmpID on mounted: ", entry.target.className, tmpID);
            if (tmpID) {
              let theArray = [];
              let idArray = [];
              theArray = tmpID.split('S');
              if (theArray && theArray.length == 2) {
                idArray = theArray[1].split('Q');
                if (theArray && theArray.length == 2) {
                  //console.log("show value: ", idArray[0] - 1, idArray[1] - 1);
                  // the question_panel_S02Q01 is 1 base, need to convert it back to 0 based
                  this.showHideWrapper(parseInt(idArray[0]) - 1, parseInt(idArray[1]), true);
                }
              }
            }
          } else {
            visibleSince = 0;
          }
        })
      }, {
          threshold: 1,
          // Track the actual visibility of the element
          trackVisibility: true,
          // Set a minimum delay between notifications
          delay: 250
      });

      let className = '[class*="' + this.CUSTOM_QUESTION_PREFIX + '"]';
      //let className = '[class=wrap]';
      //this.observer.observe(document.querySelector(className));
      //console.log("all sections: ", document.querySelector(className));
      //this.observer.observe(document.querySelectorAll(className));
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
    private_moveNav(parentNavPos, childNavPos) {
      //console.log("parentNavPos, childNavPos: ", parentNavPos, childNavPos);
      this.showHideWrapper(parseInt(parentNavPos), parseInt(childNavPos), false);

      // Move the position to the top by simulating an anchor click
      let hrefVal = '#' + parentNavPos + childNavPos;
      let selector = 'a[href="' + hrefVal + '"]'
      let theAnchor = document.querySelector(selector);
      //console.log("selector: , theAnchor: ", selector, theAnchor);
      if (theAnchor != null) {
        theAnchor.click();
      }
    },
    // method corresponds to clicking on parent nav link, it always sets the currentSectionChild to '0'
    setCurrentSectionParentChild(e) {
      if (e.target && e.target.hash) {
        // a sample of hash value: #00
        //console.log(e.target.hash);
        //console.log("e: ", e.target.hash.substr(1, 1), e.target.hash.substr(2, 2));
        this.showHideWrapper(parseInt(e.target.hash.substr(1, 1)), parseInt(e.target.hash.substr(2, 2)), false);
      }
    },
    showHideWrapper(posParentNav, posChildNav, autoScroll) {
      this.currentSectionParent = posParentNav; 
      this.currentSectionChild = posChildNav;

      // emit an event, parentNavClicked, to the parent, so parent knows the currentSectionParent
      this.$emit('parentNavClicked', this.currentSectionParent);
                    
      // show/hide sideCards panels
      //this.showHideRightsidePanels();

      // show/hide questions
      this.showHideSections(autoScroll);
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
      if (this.dataModel.components != null && this.dataModel.components.length > 0) {
        let sideCardsPanelHiddenList = this.dataModel.components[this.currentSectionParent].sideCardPanelHiddenList;
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
    showHideSections(autoScroll) {
      //console.log("this.currentSectionParent, this.currentSectionChild: ", this.currentSectionParent, this.currentSectionChild);
      // show questions
      if (this.dataModel.components != null && this.dataModel.components.length >= 1) {
        //console.log("panel is not null: ",  this.dataModel.components.length);
        for (let i = 0; i <= this.dataModel.components.length - 2; i++) {
          // Section ID is 1 based, not 0 based.
          let panelIDIndex = (i + 1).toString().length < 2 ? "0" + (i + 1).toString() : (i + 1).toString();
          let panelID = "S" + panelIDIndex;
          //console.log("this.currentSectionParent, panelID: ", this.currentSectionParent, this.currentSectionParent.toString().length, "S" + "0" + this.currentSectionParent.toString(), panelID);
          let thePanel = document.getElementById(panelID);
          //console.log("ShowHideSections", this.currentSectionParent, this.currentSectionChild, panelIDIndex, panelID, thePanel);

          if (thePanel != null) {
            //console.log(this.currentSectionParent, i, thePanel);
            if (this.currentSectionParent == i) {
              thePanel.setAttribute('style', 'display:block');
              if (!autoScroll) {
                if (this.currentSectionChild >= 1) {
                  let questionIDIndex = (this.currentSectionChild).toString().length < 2 ? ("0" + (this.currentSectionChild).toString()): (this.currentSectionChild).toString();
                  let questionClassName = '[class*="' + this.CUSTOM_QUESTION_PREFIX + panelID + "Q" + questionIDIndex + '"]';
                  //console.log("questionClassName: ", questionClassName);
                  let theQuestionPanel = document.querySelector(questionClassName);
                  //console.log("theQuestionPanel: ", theQuestionPanel);
                  if (theQuestionPanel != null) {
                    theQuestionPanel.scrollIntoView(false);
                  }
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
	border: 2px solid #BFD4E9;	

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