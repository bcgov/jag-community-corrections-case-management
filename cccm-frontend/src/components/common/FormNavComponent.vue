<template>
    <div class="divTable">
      <div class="divTableBody">
        <div class="divTableRowL1 divTableRowNav">
       
          <div class="divTableCell">
                <span>
                          <a v-for="(values, name, sectionIndex) in sectionQuestionMap"
                            :key="sectionIndex"
                            :href="getSectionKey(sectionIndex)"
                            :class="[sectionIndex == currentSectionParent ? 'active' : '', 'navHeaderA-L1']"
                            @click="setCurrentSectionParentChild">
                              {{ name }} 
                          </a>
                        </span>
          </div>
        </div>
  
        <div class="divTableRowL2 divTableRowNav">
          <div v-for="(values, name, sectionIndex) in sectionQuestionMap" :key="sectionIndex"
              :class="[currentSectionParent == sectionIndex ? 'divTableCell' : 'hide', '']">
              <span>
                <a v-for="(value, questionIndex) in values.questions" 
                  :key="questionIndex" 
                  :href="getSectionQuestionKey(sectionIndex,questionIndex)"
                  :class="[questionIndex == currentSectionChild ? 'active' : '', 'navHeaderA-L2']">
                  {{ value }} 
                </a>
              </span>
  
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script lang="ts">
  import { Component, Vue } from 'vue-property-decorator';
  import { ref, reactive } from '@vue/composition-api';
  
  export default {
    name: 'FormNavComponent',
    props: {
      dataModel: {},
  
      sectionQuestionMap: {
          type: Object
      },
      // param passed from parent to indicate move to the next parentNav
      parentNavMoveToNext: {
        type: Number,
        default: 1,
      }
    },
    data() {
      return {
        observer : null,
        currentSectionChild : 0,
        currentSectionParent : 0,
        indexZero: '0',
      }
    },
    watch: {
      sectionQuestionMap() {
          alert("section map change");
      },
      parentNavMoveToNext() {
        console.log("Moving nav...%d",this.currentSectionParent);
        let parentNavPos = this.currentSectionParent + 1;
        this.currentSectionParent++;
        let childNavPos = 0;
    //    // this.showHideWrapper("S0" + parentNavPos + "Q01");
        
    //     // Move the position to the top by simulating an anchor click
    //     let hrefVal = '#S0' + parentNavPos + "Q01";
    //     let selector = 'a[href="' + hrefVal + '"]'
    //     let theAnchor = document.querySelector(selector);
    //     if (theAnchor != null) {
    //       theAnchor.click();
    //     }
      }
    },
    mounted() {
      setTimeout(() => {
        this.observer = new IntersectionObserver((entries) => {
          entries.forEach((entry) => {
            console.log("Intersection ratio: ", entry.intersectionRatio);
            if (entry &&
                entry.isIntersecting &&
                entry.intersectionRatio > 0) {
              let tmpID = entry.target.getAttribute('id');
              if (tmpID) {
                this.currentSectionParent = tmpID.substr(0, 1);
                this.currentSectionChild = tmpID.substr(1);
  
                // show/hide sideCards panels
                this.showHideRightsidePanels();
  
                // show/hide questions
                this.showHideQuestions();
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
      if (this.observer != null) {
        console.log("Destroyed, disconnect observer");
        this.observer.disconnect();
      }
    },
    methods: {
        // get section key - summary is a special case
        getSectionKey(sectionIndex: number) : string {
      return "#S" + String(sectionIndex + 1).padStart(2, '0') + "Q01";
    },
      // get section/question key ( numbers should be 1-based not zero)
      getSectionQuestionKey(sectionIndex: number, questionIndex: number) : string {
        return "#S" + String(sectionIndex + 1).padStart(2, '0') + "Q" + String(questionIndex + 1).padStart(2, '0');
      },
      // method corresponds to clicking on parent nav link, it always sets the currentSectionChild to 0
      setCurrentSectionParentChild(e : any) {
        if (e.target && e.target.hash) {
          // a sample of hash value: #00
          this.showHideWrapper(e.target.hash);
        }
      },
      showHideWrapper(sectionQuestionKey: string) {
      
        console.log("Nav to %s", sectionQuestionKey);
  
        
        this.currentSectionParent = Number.parseInt(sectionQuestionKey.substring(2,4))-1;
        this.currentSectionChild = Number.parseInt(sectionQuestionKey.substring(5,7))-1;
  
        console.log("Section %d %d", this.currentSectionParent, this.currentSectionChild);
  
        //console.log("Local click:", this.currentSectionParent, this.currentSectionChild);
  
        // emit an event, parentNavClicked, to the parent, so parent knows the currentSectionParent
        this.$emit('parentNavClicked', this.currentSectionParent);
                      
        // show/hide sideCards panels
        this.showHideRightsidePanels();
  
        // show/hide questions
        this.showHideQuestions();
      },
      showHideRightsidePanels() {
        // show all panels
        if (this.dataModel.rightPanel != null) {
          for (let i = 0; i < this.dataModel.rightPanel.length; i++) {
            //console.log(this.dataModel.rightPanel[i].panelKey);
            let className = '[class*="' + this.dataModel.rightPanel[i].panelKey + '"]';
            let thePanel = document.querySelector(className);
            if (thePanel != null && thePanel.parentNode != null && thePanel.parentNode.parentNode) {
              thePanel.parentNode.parentNode.setAttribute('style', 'display:block');
            }
  
          }
        }
        
        // hide the panel
        if (this.dataModel.data != null && this.dataModel.data.length > 0) {
          let sideCardsPanelHiddenList = this.dataModel.data[this.currentSectionParent].sideCardPanelHiddenList;
          if (sideCardsPanelHiddenList != null) {
            for (let i = 0; i < sideCardsPanelHiddenList.length; i++) {
              let className = '[class*="' + sideCardsPanelHiddenList[i] + '"]';
              let thePanel = document.querySelector(className);
              if (thePanel != null && thePanel.parentNode != null && thePanel.parentNode.parentNode) {
                thePanel.parentNode.parentNode.setAttribute('style', 'display:none');
              }
            }
          }
        }
      },
      showHideQuestions() {
        // show questions
        if (this.dataModel.data != null) {
          for (let i = 0; i < this.dataModel.data.length; i++) {
            let thePanel = document.getElementById(i);
            if (thePanel != null) {
              //console.log(this.currentSectionParent, i);
              if (this.currentSectionParent == i) {
                thePanel.setAttribute('style', 'display:block');
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