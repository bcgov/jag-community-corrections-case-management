<template>
  <div data-app class="client-search">
    <a id="jumpToResult" href="#id_searchResults"></a>
    <div class="container">
      <section class="paper">
        <div>
          <Form class="formio-container"
              :form="formJSON"
              :submission="initData" 
              @formReady="handleFormReady"
              @change="handleChangeEvent"
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
        v-model:expanded="expanded"
        @update:expanded="expandRow"
        item-value="index"
        no-results-text="No clients found"
        :search="search"
        show-expand
        class="elevation-1"
        hide-default-footer
        v-model:page="page"
        v-model:items-per-page="itemsPerPage"
        >
        <!--Customize the Name field, making it clickable-->
        <template v-slot:item.clientName="{ item }">
          <a :href="`${baseURL}${$ROUTER_NAME_CLIENTRECORD}/${item.clientNum}/tab-cp`">{{item.clientName}}</a>
        </template>
        <!--Customize the expanded item to show photo and more-->
        <template v-slot:expanded-row="{ headers, item }">
          <tr>
          <td width="5%"></td>
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
              <template v-for="(el, index) in item.address" :key="index">
                <li v-if="el && !el.primary">
                  {{ el.fullAddress }}
                </li>
              </template>
            </ul>
          </td>
          </tr>
        </template>
      </v-data-table>
      <br/>
      <!--Customize the footer-->
      <div v-if="!loading" class="text-center">
        <DatatablePagination v-model:items-per-page="itemsPerPage" v-model:page="page" :page-count="pageCount" />
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
      singleExpand: true,
      clients: [],
      clientHeaders: [
        { title: '', align: 'start', key: 'data-table-expand', width: '2%' },
        { title: 'Name', align: 'start', sortable: true, key: 'clientName', width: '10%' },
        { title: 'Current Name?', key: 'currentNameYn', width: '5%' },
        { title: 'Age', key: 'clientAge', width: '5%' },
        { title: 'Date of Birth', key: 'birthDate', width: '10%' },
        { title: 'Address', key: 'fullAddress', width: '28%' },
        { title: 'Address Type', key: 'addressType', width: '10%' },
        { title: 'Expired?', key: 'expired', width: '10%' },
        { title: 'CS#', key: 'clientNum', width: '10%' },
        { title: 'Record Sealed?', key: 'sealed', width: '10%' },
      ],
      baseURL: import.meta.env.BASE_URL,
      initData: {'data': {}},
      formioInstance: null,
      formioEventsBound: false,
      lookupCodesLoaded: false,
    }
  },
  components: {
    DatatablePagination,
    Form
  },
  async mounted() {
    this.buildForm();
    await this.setInitData();
  },  
  methods: {
    updatePageCount() {
      const term = (this.search || '').toString().trim().toLowerCase();
      const totalItems = term
        ? this.clients.filter(item => {
            try {
              return JSON.stringify(item).toLowerCase().includes(term);
            } catch {
              return false;
            }
          }).length
        : this.clients.length;
      this.pageCount = Math.max(1, Math.ceil(totalItems / this.itemsPerPage));
      if (this.page > this.pageCount) {
        this.page = this.pageCount;
      }
    },
    async getLookupCodes() {
      // lookup addressTypeCodes
      const [error, addressTypeCodes] = await this.mainStore.lookupAddressTypeCodes();
      if (error) {
        console.error(error);
      } else {
        this.initData.data.addressTypeCodes = addressTypeCodes;
      }
      // lookup genderCodes
      const [error1, genderCodes] = await this.mainStore.lookupGenderCodes();
      if (error1) {
        console.error(error1);
      } else {
        this.initData.data.genderCodes = genderCodes;
      }
      // lookup idTypeCodes
      const [error2, idTypeCodes] = await this.mainStore.lookupIdTypeCodes();
      if (error2) {
        console.error(error2);
      } else {
        this.initData.data.idTypeCodes = idTypeCodes;
      }
      // lookup cityCodes
      const [error3, cityCodes] = await this.mainStore.lookupCityCodes();
      if (error3) {
        console.error(error3);
      } else {
        this.initData.data.cityCodes = cityCodes;
      }
      // lookup provinceCodes
      const [error4, provinceCodes] = await this.mainStore.getProvinceCodes();
      if (error4) {
        console.error(error4);
      } else {
        this.initData.data.provinceCodes = provinceCodes;
      }
    },
    expandRow (expandedItems) {
      // When a row is expanded the first time, call getClientDetails to retrive the following info:
      // 1. photo image and photo taken date.
      // 2. alias
      // 3. other addresses 
      if (expandedItems && expandedItems.length > 0) {
        // If more than one row is expanded, keep only the most recent one
        //expanded.value = expandedItems.length > 0 ? [expandedItems[expandedItems.length - 1]] : []
        const index = expandedItems[expandedItems.length - 1];
        // fetch the row based on the index value only if index exists and details have not been fetched yet
        if (index !== undefined) {
          const item = this.clients.find(el => el.index === index);
          if (item && !item.detailsFetched) {
            this.getClientDetails(item.clientNum);
          }
        }
      }
    },
    async getClientDetails(clientNum) {
      const [error, response] = await getClientDetail(clientNum);
      if (error) {
        console.error(error);
      } else {
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
      await this.getLookupCodes();
      this.lookupCodesLoaded = true;
      this.refreshFormSubmission();
    },
    refreshFormSubmission() {
      if (!this.formioInstance || !this.lookupCodesLoaded) {
        return;
      }
      if (this.formioInstance.data) {
        this.formioInstance.data = { ...this.initData.data };
      }
      if (this.formioInstance.setSubmission) {
        this.formioInstance.setSubmission(
          { data: { ...this.initData.data } },
          { noValidate: true, sanitize: true }
        );
      } else {
        this.formioInstance.submission = { data: { ...this.initData.data } };
      }
      if (this.formioInstance.redraw) {
        this.formioInstance.redraw();
      }
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
        this.updatePageCount();
      }
    },
    jumpToResult() {
      let jumpAnchor = document.getElementById("jumpToResult");
      if (jumpAnchor) {
        jumpAnchor.click();
      }
    },
    async handleClearAll() {
      this.initData = {'data': {}};
      if (this.formioInstance?.resetValue) {
        this.formioInstance.resetValue();
      }
      await this.setInitData();

      this.clients = [];
      this.key_clientsearchresult++;
    },
    handleFormReady(formInstance) {
      if (this.formioInstance && this.formioInstance !== formInstance && this.formioInstance?.off) {
        // Unbind previous event listeners
        this.formioInstance.off('change', this.handleChangeEvent);
        this.formioInstance.off('evt_clearAll', this.handleClearAll);
        this.formioInstance.off('evt_clientSearchEvent_generalInfo', this.handleClientSearch_byGeneralInfo);
        this.formioInstance.off('evt_clientSearchEvent_addressInfo', this.handleClientSearch_byAddressInfo);

        this.formioEventsBound = false;
      }

      this.formioInstance = formInstance;
      if (formInstance?.on && !this.formioEventsBound) {
        // Bind the change event directly on the formio instance
        formInstance.on('change', this.handleChangeEvent);
        formInstance.on('evt_clearAll', this.handleClearAll);
        formInstance.on('evt_clientSearchEvent_generalInfo', this.handleClientSearch_byGeneralInfo);
        formInstance.on('evt_clientSearchEvent_addressInfo', this.handleClientSearch_byAddressInfo);

        this.formioEventsBound = true;
        
        // Add custom Enter key handler
        this.$nextTick(() => {
          const formElement = formInstance.element;
          if (formElement) {
            formElement.addEventListener('keydown', this.handleEnterKey);
          }
        });
      }
      if (formInstance?.once) {
        formInstance.once('render', () => this.refreshFormSubmission());
      }
      this.refreshFormSubmission();
    },
    handleEnterKey(event) {
      if (event.key === 'Enter' && !event.shiftKey) {
        const target = event.target;
        // Only handle Enter in input fields, not in buttons or textareas
        if (target.tagName === 'INPUT' || (target.tagName === 'TEXTAREA' && event.shiftKey)) {
          let isInGeneralInfo = false;
          let isInAddressInfo = false;
          if (target.name) {
            if (target.name.includes('generalInfo') || 
                target.name.includes('lastName') || 
                target.name.includes('givenName') || 
                target.name.includes('gender') || 
                target.name.includes('dob') || 
                target.name.includes('age') || 
                target.name.includes('idType') || 
                target.name.includes('idNumber')) {
              isInGeneralInfo = true;
            } else if (target.name.includes('addressInfo') || 
                       target.name.includes('address') || 
                       target.name.includes('city') || 
                       target.name.includes('province')) {
              isInAddressInfo = true;
            }
          }       
          if (isInGeneralInfo) {
            event.preventDefault();
            this.handleClientSearch_byGeneralInfo(this.formioInstance.data);
          } else if (isInAddressInfo) {
            event.preventDefault();
            this.handleClientSearch_byAddressInfo(this.formioInstance.data);
          }
        }
      }
    },
    async handleClientSearch_byGeneralInfo(evt) {
      const data = evt?.data ?? evt;
      if (data != null) {
        if (!(data.idNumber) && !(data.idType ) && !(data.lastName)) {
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

        let rangeYears = data.rangeYears;
        if (rangeYears === undefined || rangeYears === null || rangeYears === '') {
          rangeYears = 0;
        }

        let limitedToCurrentActiveLocation = this.private_getLimitedToCurrentActiveLocation();
        const [error, response] = await clientSearchByGeneralInfo(data.age, data.dobYear, data.gender, 
            data.givenName1Or2, data.idNumber, data.idType, data.lastName,
            limitedToCurrentActiveLocation.toString(), rangeYears, data.lastNameSoundex);
        this.private_processSearchResults(error, response);
      }
    },
    async handleClientSearch_byAddressInfo(evt) {      
      const data = evt?.addressInfo ?? evt;
      if (data != null) {
        this.jumpToResult();
        this.loading = true;
        //clear the previous search results
        this.clients = [];

        let limitedToCurrentActiveLocation = this.private_getLimitedToCurrentActiveLocation();
        const [error, response] = await clientSearchByAddressInfo(data.address, data.addressType, data.city, 
            data.includeExpiredAddresses, limitedToCurrentActiveLocation, data.postalCode, data.province);
        this.private_processSearchResults(error, response);
      }
    },
    handleChangeEvent(event) {      
      // Defensive check for event structure
      if (!event) {
        console.warn("Change event is null or undefined");
        return;
      }

      if (   event.changed 
          && event.changed.component
          && ( event.changed.component.key === "age"
            || event.changed.component.key === "dobYear")) {        
        // if dobYear is updated, re-calculate and set the age
        if (event.changed.component.key == "dobYear") {
          let ele = document.getElementsByName("data[age]");
          if (ele != null && ele.length == 1) {
            let calcVal = null;
            if (event.data && event.data.dobYear != null) {
              calcVal = this.CONST_CURRENT_YEAR - event.data.dobYear;
            }
            ele[0].value = calcVal;
            if (event.data) {
              event.data.age = calcVal;
            }
          }
        }
        // if age is updated, re-calculate and set the dobYear
        if (event.changed.component.key == "age") {
          let ele = document.getElementsByName("data[dobYear]");
          if (ele != null && ele.length == 1) {
            let calcVal = null;
            if (event.data && event.data.age != null) {
              calcVal = this.CONST_CURRENT_YEAR - event.data.age;
            }
            ele[0].value = calcVal;
            if (event.data) {
              event.data.dobYear = calcVal;
            }
          }
        }
      }
    }
  },
  computed: {
    // note we are not passing an array, just one store after the other
    // each store will be accessible as its id + 'Store', i.e., mainStore
    ...mapStores(useStore)
  },
  watch: {
    itemsPerPage() {
      this.updatePageCount();
    },
    search() {
      this.updatePageCount();
    },
    clients: {
      deep: true,
      handler() {
        this.updatePageCount();
      }
    }
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