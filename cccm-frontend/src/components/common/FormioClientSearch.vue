<template>
  <div data-app class="client-search">
    <a id="jumpToResult" href="#id_searchResults"></a>
    <div class="container">
      <section class="paper">
        <div>
          <Form class="formio-container"
              :form="formJSON"
              :submission="initData" 
              v-on:evt_clearAll="handleClearAll"
              v-on:evt_clientSearchEvent_generalInfo="handleClientSearch_byGeneralInfo" 
              v-on:evt_clientSearchEvent_addressInfo="handleClientSearch_byAddressInfo"
              v-on:change="handleChangeEvent"
            />
        </div>
      </section>
    </div>  
    <div id="id_searchResults" class="container">
      <section class="paper">
    <v-card class="p-2">
      <v-card-title>
        Results
        <v-spacer></v-spacer>
        <div class="w-25 p-3">
          <v-text-field
              v-model="search"
              append-icon="mdi-magnify"
              label=""
              single-line
              hide-details
          ></v-text-field>
        </div>
      </v-card-title>
      <v-data-table
        :loading="loading"   
        loading-text="Searching... Please wait"
        :key="key_clientsearchresult"
        :headers="clientHeaders"
        :items="clients"
        :single-expand="singleExpand"
        :expanded.sync="expanded"
        @item-expanded="expandRow"
        item-key="index"
        no-results-text="No clients found"
        :search="search"
        show-expand
        class="elevation-1"
        hide-default-footer
        :page.sync="page"
        :items-per-page="itemsPerPage"
        @page-count="pageCount = $event"
        >
        <!--Customize the Name field, making it clickable-->
        <template v-slot:item.clientName="{ item }">
          <a :href="`${baseURL}${$ROUTER_NAME_CLIENTRECORD}/${item.clientNum}/tab-cp`">{{item.clientName}}</a>
        </template>
        <!--Customize the expanded item to show photo and more-->
        <template v-slot:expanded-item="{ headers, item }">
          <td width="2%"></td>
          <td width="10%" class="innerTableData">
            <figure v-if="item.photoData">
              <img :src="item.photoData" alt="Client photo" />
              <figcaption>Photo Taken: {{item.photoDate}}</figcaption>
            </figure>
            <strong v-else>No photo</strong>
          </td>
          <td width="10%" class="innerTableData">
            <strong>Current Name</strong>
            <br />
            {{ item.currentName }}
          </td>
          <td width="10%" class="innerTableData">
            <strong>Gender</strong>
            <br />
            {{ item.gender }}
          </td>
          <td width="10%" class="innerTableData">
            <strong>Location</strong>
            <br />
            {{ item.communityInformation.communityLocation }}
          </td>
          <td width="10%" class="innerTableData">
            <strong>PCM</strong>
            <br />
            {{ item.communityInformation.caseManager }}
          </td>
          <td width="10%" class="innerTableData">
            <strong>Other Aliases</strong>
            <br />
            {{ item.alias }}
          </td>
          <td :colspan="3" width="40%" class="innerTableData">
            <strong>Other Addresses</strong>
            <br />
            <ul>
              <li v-for="el in item.address" v-if="!el.primary">
                {{ el.fullAddress }}
              </li>
            </ul>
          </td>
        </template>
      </v-data-table>
      <br/>
      <!--Customize the footer-->
      <div v-if="!loading" class="text-center">
        <DatatablePagination :items-per-page.sync="itemsPerPage" :page.sync="page" :page-count="pageCount" />
      </div>
    </v-card>
    </section>
   </div>
   <br/><br/>
  </div>
</template>

<script lang="ts">
import { Form } from '@formio/vue';
import {clientSearchByGeneralInfo, clientSearchByAddressInfo, getClientDetail} from "@/components/form.api";
import template from '@/components/common/templateClientSearch.json';
import updateToken from '@/middleware/update-token';
import 'izitoast/dist/css/iziToast.min.css';
import iZtoast from 'izitoast';
import {useStore} from "@/stores/store";
import {mapStores} from 'pinia';
import DatatablePagination from "@/components/common/DatatablePagination.vue";
import { dateToCCCMDateformat } from '../dateUtils';

export default {
  name: 'FormioClientSearch',
  data() {
    return {
      CONST_DEFAULT_RANGEYEARS: 2,
      CONST_MIN_AGE: 10,
      CONST_MIN_DOB: 1880,
      CONST_CURRENT_YEAR: new Date().getFullYear(),
      key_clientsearchresult: 0,
      formInfoTemplate: template,
      formJSON: {},
      // datatable variables
      page: 1,
      pageCount: 1,
      itemsPerPage: this.$CONST_DATATABLE_ITEMS_PER_PAGE,
      totalClients: 0,
      loading: false,
      search: '',
      expanded: [],
      singleExpand: false,
      clientHeaders: [
        { text: '', align: 'start', value: 'data-table-expand', width: '2%' },
        { text: 'Name', align: 'start', sortable: true, value: 'clientName', width: '10%' },
        { text: 'Current Name?', value: 'currentNameYn', width: '5%' },
        { text: 'Age', value: 'clientAge', width: '5%' },
        { text: 'Date of Birth', value: 'birthDate', width: '10%' },
        { text: 'Address', value: 'fullAddress', width: '28%' },
        { text: 'Address Type', value: 'addressType', width: '10%' },
        { text: 'Expired?', value: 'expired', width: '10%' },
        { text: 'CS#', value: 'clientNum', width: '10%' },
        { text: 'Record Sealed?', value: 'sealed', width: '10%' },
      ],
      baseURL: import.meta.env.BASE_URL,
      initData: {'data': {}},
    }
  },
  components: {
    DatatablePagination,
    Form
  },
  created() {
    this.clients = [];
  },
  mounted() {
    this.buildForm();
    this.setInitData();
  },
  methods: {
    async getLookupCodes() {
      // lookup addressTypeCodes
      const [error, addressTypeCodes] = await this.mainStore.lookupAddressTypeCodes();
      if (error) {
        console.log(error);
      } else {
        this.initData.data.addressTypeCodes = addressTypeCodes;
      }
      // lookup genderCodes
      const [error1, genderCodes] = await this.mainStore.lookupGenderCodes();
      if (error1) {
        console.log(error1);
      } else {
        this.initData.data.genderCodes = genderCodes;
      }
      // lookup idTypeCodes
      const [error2, idTypeCodes] = await this.mainStore.lookupIdTypeCodes();
      if (error2) {
        console.log(error2);
      } else {
        this.initData.data.idTypeCodes = idTypeCodes;
      }
      // lookup cityCodes
      const [error3, cityCodes] = await this.mainStore.lookupCityCodes();
      if (error3) {
        console.log(error3);
      } else {
        this.initData.data.cityCodes = cityCodes;
      }
      // lookup provinceCodes
      const [error4, provinceCodes] = await this.mainStore.getProvinceCodes();
      if (error4) {
        console.log(error4);
      } else {
        this.initData.data.provinceCodes = provinceCodes;
      }
    },
    expandRow ({ item, value }) {
      // When a row is expanded the first time, call getClientDetails to retrive the following info:
      // 1. photo image and photo taken date.
      // 2. alias
      // 3. other addresses 
      if (!item.detailsFetched) {
        this.getClientDetails(item.clientNum);
      }
    },
    async getClientDetails(clientNum) {
      const [error, response] = await getClientDetail(clientNum);
      if (error) {
        console.error(error);
      } else {
        console.log("getClientDetail: ", response);
        //Cache the photoData, alias, addresses into this.clients object
        if (this.clients != null) {
          for (let el of this.clients) {
            if (el.clientNum == clientNum) {
              el.detailsFetched = true;
              if (response.photo != null) {
                el.photoData = "data:image/png;base64, " + response.photo.image;
                const pdate = response.photo.photoTakenDate;
                el.photoDate = dateToCCCMDateformat(pdate);
              }
              el.currentName = response.clientName;
              el.alias = response.alias;
              el.communityInformation = response.communityInformation;
              if (el.address == null) {
                //clone the array
                el.address = [...response.address];
              } else {
                // append both array
                el.address = el.address.concat(response.address);
              }
              // Don't break, the list contains records share the same clientNum, need to update all of them
              //break;
            }
          }
        }
      }
    },
    async buildForm() {
      // make a deep copy of the template
      let tmpJSONStr = JSON.stringify(this.formInfoTemplate);
      tmpJSONStr = tmpJSONStr.replaceAll('${cccm_api_endpoint}', config.VUE_APP_CCCM_API_ENDPOINT);
      tmpJSONStr = tmpJSONStr.replaceAll('${min_dob_year}', this.CONST_MIN_DOB);
      tmpJSONStr = tmpJSONStr.replaceAll('${max_dob_year}', this.CONST_CURRENT_YEAR);
      tmpJSONStr = tmpJSONStr.replaceAll('${min_age}', this.CONST_MIN_AGE);
      tmpJSONStr = tmpJSONStr.replaceAll('${max_age}', this.CONST_CURRENT_YEAR - this.CONST_MIN_DOB);

      // build crnacmpType DDL
      let tmpJSON = JSON.parse(tmpJSONStr);
           
      this.formJSON = tmpJSON;
    },
    async setInitData() {
      this.initData.data.rangeYears = this.CONST_DEFAULT_RANGEYEARS;
      this.getLookupCodes();
    },
    private_getLimitedToCurrentActiveLocation() {
      let limitedToCurrentActiveLocation = false;
      // Commented out on Jan 27, 2023. Will enable it once this option is back.
      // let checkbox = document.getElementsByName("data[limitedToCurrentActiveLocation]");
      // if (checkbox != null &&  checkbox[0] != null) {
      //   limitedToCurrentActiveLocation = checkbox[0].checked;
      // }
      return limitedToCurrentActiveLocation;
    },
    private_processSearchResults(error, response) {
      this.jumpToResult();
      if (error) {
        console.error(error);
        // clear the previous search result
        this.key_clientsearchresult++;
        this.loading = false;
      } else {
        //console.log("client search by general info: ", response);
        this.clients = response;

        // populate primary address info 
        let index = Math.random();
        this.clients = this.clients.filter(el => {
          el.index = index++;
          el.birthDate = dateToCCCMDateformat(el.birthDate);
          el.dueDate = dateToCCCMDateformat(el.dueDate);
          if (el.orderInformation != null) {
            el.orderInformation.dueDate = dateToCCCMDateformat(el.orderInformation.dueDate);
            el.orderInformation.expiryDate = dateToCCCMDateformat(el.orderInformation.expiryDate);
          }
          if (el.generalInformation != null) {
            el.generalInformation.dischargeDate = dateToCCCMDateformat(el.generalInformation.dischargeDate);
            el.generalInformation.paroleDate = dateToCCCMDateformat(el.generalInformation.paroleDate);
          }
          if (el.locationInformation != null) {
            el.locationInformation.warrantExpiryDate = dateToCCCMDateformat(el.locationInformation.warrantExpiryDate);
            el.locationInformation.nextCourtDate = dateToCCCMDateformat(el.locationInformation.nextCourtDate);
          }
          // Map primary address and primary addressType
          if (el.address != null && el.address.length == 1) {
            el.fullAddress = el.address[0].fullAddress;
            el.addressType = el.address[0].primary ? 'Primary' : el.address[0].type;
            el.expired = el.address[0].expired ? 'Y' : 'N';
          } else {
            el.fullAddress = "";
            el.addressType = "";
            el.expired = null;
          }
          el.detailsFetched = false;
          return el;
        });
        this.key_clientsearchresult++;
        this.loading = false;
      }
    },
    jumpToResult() {
      let jumpAnchor = document.getElementById("jumpToResult");
      if (jumpAnchor) {
        jumpAnchor.click();
      }
    },
    handleClearAll() {
      this.initData = {'data': {}};
      this.setInitData();

      this.clients = [];
      this.key_clientsearchresult++;
    },
    async handleClientSearch_byGeneralInfo(evt) {
      if (evt.data != null) {
        if (!(evt.data.idNumber) && !(evt.data.idType ) && !(evt.data.lastName)) {
          iZtoast.warning({
            title: 'Validation',
            message: 'One of Lastname or Identifier is required to search',
            position: 'center'
          });
           return;
        }

        this.jumpToResult();
        this.loading = true;
        //clear the previous search results
        this.clients = [];

        let rangeYears = evt.data.rangeYears;
        if (rangeYears === undefined || rangeYears === null || rangeYears === '') {
          rangeYears = 0;
        }

        let limitedToCurrentActiveLocation = this.private_getLimitedToCurrentActiveLocation();
        const [error, response] = await clientSearchByGeneralInfo(evt.data.age, evt.data.dobYear, evt.data.gender, 
            evt.data.givenName1Or2, evt.data.idNumber, evt.data.idType, evt.data.lastName,
            limitedToCurrentActiveLocation.toString(), rangeYears, evt.data.lastNameSoundex);
        this.private_processSearchResults(error, response);
      }
    },
    async handleClientSearch_byAddressInfo(evt) {
      if (evt.data != null) {
        this.jumpToResult();
        this.loading = true;
        //clear the previous search results
        this.clients = [];

        let limitedToCurrentActiveLocation = this.private_getLimitedToCurrentActiveLocation();
        const [error, response] = await clientSearchByAddressInfo(evt.data.address, evt.data.addressType, evt.data.city, 
            evt.data.includeExpiredAddresses, limitedToCurrentActiveLocation, evt.data.postalCode, evt.data.province);
        this.private_processSearchResults(error, response);
      }
    },
    handleChangeEvent(event) {
      if (   event.changed 
          && ( event.changed.component.key === "age"
            || event.changed.component.key === "dobYear")) {
        //console.log("textfield or textarea changed: ", event);
        // if dobYear is updated, re-calculate and set the age
        if (event.changed.component.key == "dobYear") {
          //console.log("textfield or textarea changed: ", event.data.dobYear);
          let ele = document.getElementsByName("data[age]");
          if (ele != null && ele.length == 1) {
            let calcVal = null;
            if (event.data.dobYear != null) {
              calcVal = this.CONST_CURRENT_YEAR - event.data.dobYear;
            }
            ele[0].value = calcVal;
            event.data.age = calcVal;
          }
        }
        // if age is updated, re-calculate and set the dobYear
        if (event.changed.component.key == "age") {
          let ele = document.getElementsByName("data[dobYear]");
          if (ele != null && ele.length == 1) {
            let calcVal = null;
            if (event.data.age != null) {
              calcVal = this.CONST_CURRENT_YEAR - event.data.age;
            }
            ele[0].value = calcVal;
            event.data.dobYear = calcVal;
          }
        }
      }
    }
  },
  computed: {
    // note we are not passing an array, just one store after the other
    // each store will be accessible as its id + 'Store', i.e., mainStore
    ...mapStores(useStore)
  }
}
</script>

<style>
  .wild-search-text {
    color: #154c79;
    font-size: 0.5em;
  }
  legend[ref="header"]{
    display: flex;
    flex-direction: row-reverse;
    justify-content: center;
    align-items: center;
    gap: 20px;
  }
  .primary {
    background-color: #1867c0 !important;
    border-color: #1867c0 !important;
  }
</style>