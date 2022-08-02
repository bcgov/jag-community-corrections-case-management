<template>
  <div data-app>
    <div class="container">
      <section class="paper">
        <div class="pt-5">
          <Form class="formio-container" :form="formJSON" v-on:evt_clientSearchEvent="handleClientSearch"/>
        </div>
      </section>
    </div>

    <div class="container">
      <section class="paper">
        <v-card>
      <v-card-title>
        Results
        <v-spacer></v-spacer>
        <v-text-field
          v-model="search"
          append-icon="mdi-magnify"
          label=""
          single-line
          hide-details
        ></v-text-field>
      </v-card-title>
      <v-data-table
        :key="key_clientsearchresult"
        :headers="clientHeaders"
        :items="clients"
        :single-expand="singleExpand"
        :expanded.sync="expanded"
        item-key="id"
        no-results-text="No clients found"
        :search="search"
        hide-default-header
        show-expand
        class="elevation-1"
        hide-default-footer
        :page.sync="page"
        :items-per-page="itemsPerPage"
        @page-count="pageCount = $event"
        >
        <!--Customize the header style-->
        <template v-slot:header="{ props: { headers } }">
          <thead>
            <tr class="datatable-header-bg-style">
              <th v-for="h in headers" :class="h.class" :key="h.id">
                <span>{{h.text}}</span>
              </th>
            </tr>
          </thead>
        </template>
        <!--Customize the Name field, making it clickable-->
        <template v-slot:item.fullName="{ item }">
          <a :href="`/clientprofile/${item.id}`" target="_blank">{{item.fullName}}</a>
        </template>
        <!--Customize the expanded item to show photo and more-->
        <template v-slot:expanded-item="{ headers, item }">
          <td :colspan="1"></td>
          <td :colspan="1">
            <img :src="item.photoURL" alt="Client photo" width="132" height="132" />
          </td>
          <td :colspan="1">
            <strong>Current Name</strong>
            <br />
            {{ item.currentName}}
          </td>
          <td :colspan="1">
            <strong>Gender</strong>
            <br />
            {{ item.gender}}
          </td>
          <td :colspan="1">
            <strong>Location</strong>
            <br />
            {{ item.location}}
          </td>
          <td :colspan="1">
            <strong>PCM</strong>
            <br />
            {{ item.pcm}}
          </td>
        </template>
      </v-data-table>
      <br/>
      <!--Customize the footer-->
      <div v-if="!loading" class="text-center pt-2">
        <v-row>
          <v-col cols="2" sm="2">
            <v-select
              solo
              :items="items"
              value=5
              dense
              item-color="primary"
              @input="itemsPerPage = parseInt($event, 10)"
            ></v-select>
          </v-col>
          <v-col cols="10" sm="10">
            <v-pagination v-model="page" :total-visible="7" :length="pageCount"></v-pagination>
          </v-col>
        </v-row>
      </div>
    </v-card>
      </section>
    </div>

  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import {clientSearch} from "@/components/form.api";
import template from '@/components/common/templateClientSearch.json';

export default {
  name: 'FormioClientSearch',
  data() {
    return {
      key_clientsearchresult: 0,
      formInfoTemplate : template,
      formJSON : {},
      // datatable variables
      items: ['1', '2', '5', '10', '15'],
      page: 1,
      pageCount: 1,
      itemsPerPage: 5,
      totalClients: 0,
      loading: true,
      search: '',
      expanded: [],
      singleExpand: false,
      clientHeaders: [
        {
          text: '',
          align: 'start',
          value: 'data-table-expand'
        },
        {
          text: 'Name',
          align: 'start',
          sortable: true,
          value: 'fullName',
          class: 'datatable-header-text-style'
        },
        { text: 'Age', value: 'clientAge', class: 'datatable-header-text-style' },
        { text: 'Date of Bith', value: 'birthDate', class: 'datatable-header-text-style' },
        { text: 'Address', value: 'fullAddress', class: 'datatable-header-text-style' },
        { text: 'Address Type', value: 'addressType', class: 'datatable-header-text-style' },
        { text: 'Expired?', value: 'expired', class: 'datatable-header-text-style' },
        { text: 'CS#', value: 'csNumber', class: 'datatable-header-text-style' },
        { text: 'Record Sealed?', value: 'recordSealed', class: 'datatable-header-text-style' },
      ],
      clients: [],
    }
  },
  components: {
    Form
  },
  mounted(){
    this.buildForm()
  },
  methods: {
    buildForm() {
      // make a deep copy of the template
      let tmpJSONStr = JSON.stringify(this.formInfoTemplate);

      // build crnacmpType DDL
      let tmpJSON = JSON.parse(tmpJSONStr);
           
      this.formJSON = tmpJSON;
    },
    async handleClientSearch(evt) {
      // Sample client search results:
      //   [
      //     {
      //         "id": "1",
      //         "firstName": "Bob",
      //         "LastName": "Ross",
      //         "gender": "Male",
      //         "clientAge": 44,
      //         "birthDate": "1975-02-03",
      //         "addressType": "Work",
      //         "expired": "No",
      //         "csNumber": "123456789",
      //         "recordSealed": "Yes",
      //         "currentName": "Bob Ross",
      //         "location": "VICTORIA",
      //         "pcm": "Gillis, Mike",
      //         "photoURL": "", 
      //         "datePhotoTaken": "2022-03-02",
      //         "address": [
      //             {
      //                 "street": "123 Hello St",
      //                 "city": "Victoria",
      //                 "postalCode": "123 abc"
      //             }
      //         ]
      //     }
      // ]
      if (evt.data != null) {
        //console.log("payload: ", evt.data);
        // Sample payload:
        // {
        //     "limitedToCurrentActiveLocation": false,
        //     "lastName": "",
        //     "lastNameSoundex": false,
        //     "givenName1Or2": "",
        //     "gender": "",
        //     "dobYear": "",
        //     "age": "",
        //     "rangeYears": "",
        //     "addressType": "all",
        //     "address": "",
        //     "includeExpiredAddresses": false,
        //     "city": "",
        //     "province": "",
        //     "postalCode": "",
        //     "idType": "cn",
        //     "idNumber": ""
        // }
        const [error, response] = await clientSearch(evt.data);
        // this.totalClients = response.length;
        // this.pageCount = Math.floor(this.totalClients / this.itemsPerPage);
        // if (this.totalClients % this.itemsPerPage != 0) {
        //   this.pageCount++;
        // };
        this.key_clientsearchresult++;
        this.loading = false;
        this.clients =   
          [
            {
                "id": "1",
                "fullName": "Ross, Bob",
                "clientAge": 44,
                "birthDate": "1979-12-03",
                "fullAddress": "123 Hello St, Victoria BC", 
                "addressType": "Work",
                "expired": "No",
                "csNumber": "123456780",
                "recordSealed": "Yes",
                "gender": "Male",
                "currentName": "Bob Ross",
                "location": "VICTORIA",
                "pcm": "Gillis, Mike",
                "photoURL": "https://www.w3schools.com/images/lamp.jpg", 
                "datePhotoTaken": "2022-03-02",
                "address": [
                    {
                        "street": "123 Hello St",
                        "city": "Victoria",
                        "postalCode": "123 abc"
                    }
                ]
            },
            {
                "id": "2",
                "fullName": "Smith, Sam",
                "clientAge": 40,
                "birthDate": "1983-02-03",
                "fullAddress": "123 Hello St, Victoria BC", 
                "addressType": "Home",
                "expired": "No",
                "csNumber": "123456781",
                "recordSealed": "Yes",
                "gender": "Male",
                "currentName": "Sam Smith",
                "location": "VICTORIA",
                "pcm": "Gillis, Mike",
                "photoURL": "https://www.w3schools.com/images/stickman.gif", 
                "datePhotoTaken": "2022-03-02",
                "address": [
                    {
                        "street": "123 Hello St",
                        "city": "Victoria",
                        "postalCode": "123 abc"
                    }
                ]
            },
            {
                "id": "3",
                "fullName": "Ross, Bob",
                "clientAge": 44,
                "birthDate": "1979-12-03",
                "fullAddress": "123 Hello St, Victoria BC", 
                "addressType": "Work",
                "expired": "No",
                "csNumber": "123456782",
                "recordSealed": "Yes",
                "gender": "Male",
                "currentName": "Bob Ross",
                "location": "VICTORIA",
                "pcm": "Gillis, Mike",
                "photoURL": "https://www.w3schools.com/images/lamp.jpg", 
                "datePhotoTaken": "2022-03-02",
                "address": [
                    {
                        "street": "123 Hello St",
                        "city": "Victoria",
                        "postalCode": "123 abc"
                    }
                ]
            },
            {
                "id": "4",
                "fullName": "Smith, Sam",
                "clientAge": 40,
                "birthDate": "1983-02-03",
                "fullAddress": "123 Hello St, Victoria BC", 
                "addressType": "Home",
                "expired": "No",
                "csNumber": "123456783",
                "recordSealed": "Yes",
                "gender": "Male",
                "currentName": "Sam Smith",
                "location": "VICTORIA",
                "pcm": "Gillis, Mike",
                "photoURL": "https://www.w3schools.com/images/stickman.gif", 
                "datePhotoTaken": "2022-03-02",
                "address": [
                    {
                        "street": "123 Hello St",
                        "city": "Victoria",
                        "postalCode": "123 abc"
                    }
                ]
            },
            {
                "id": "5",
                "fullName": "Ross, Bob",
                "clientAge": 44,
                "birthDate": "1979-12-03",
                "fullAddress": "123 Hello St, Victoria BC", 
                "addressType": "Work",
                "expired": "No",
                "csNumber": "123456784",
                "recordSealed": "Yes",
                "gender": "Male",
                "currentName": "Bob Ross",
                "location": "VICTORIA",
                "pcm": "Gillis, Mike",
                "photoURL": "https://www.w3schools.com/images/lamp.jpg", 
                "datePhotoTaken": "2022-03-02",
                "address": [
                    {
                        "street": "123 Hello St",
                        "city": "Victoria",
                        "postalCode": "123 abc"
                    }
                ]
            },
            {
                "id": "6",
                "fullName": "Smith, Sam",
                "clientAge": 40,
                "birthDate": "1983-02-03",
                "fullAddress": "123 Hello St, Victoria BC", 
                "addressType": "Home",
                "expired": "No",
                "csNumber": "123456785",
                "recordSealed": "Yes",
                "gender": "Male",
                "currentName": "Sam Smith",
                "location": "VICTORIA",
                "pcm": "Gillis, Mike",
                "photoURL": "https://www.w3schools.com/images/stickman.gif", 
                "datePhotoTaken": "2022-03-02",
                "address": [
                    {
                        "street": "123 Hello St",
                        "city": "Victoria",
                        "postalCode": "123 abc"
                    }
                ]
            },
            {
                "id": "7",
                "fullName": "Ross, Bob",
                "clientAge": 44,
                "birthDate": "1979-12-03",
                "fullAddress": "123 Hello St, Victoria BC", 
                "addressType": "Work",
                "expired": "No",
                "csNumber": "123456786",
                "recordSealed": "Yes",
                "gender": "Male",
                "currentName": "Bob Ross",
                "location": "VICTORIA",
                "pcm": "Gillis, Mike",
                "photoURL": "https://www.w3schools.com/images/lamp.jpg", 
                "datePhotoTaken": "2022-03-02",
                "address": [
                    {
                        "street": "123 Hello St",
                        "city": "Victoria",
                        "postalCode": "123 abc"
                    }
                ]
            },
            {
                "id": "8",
                "fullName": "Smith, Sam",
                "clientAge": 40,
                "birthDate": "1983-02-03",
                "fullAddress": "123 Hello St, Victoria BC", 
                "addressType": "Home",
                "expired": "No",
                "csNumber": "123456787",
                "recordSealed": "Yes",
                "gender": "Male",
                "currentName": "Sam Smith",
                "location": "VICTORIA",
                "pcm": "Gillis, Mike",
                "photoURL": "https://www.w3schools.com/images/stickman.gif", 
                "datePhotoTaken": "2022-03-02",
                "address": [
                    {
                        "street": "123 Hello St",
                        "city": "Victoria",
                        "postalCode": "123 abc"
                    }
                ]
            },
            {
                "id": "9",
                "fullName": "Ross, Bob",
                "clientAge": 44,
                "birthDate": "1979-12-03",
                "fullAddress": "123 Hello St, Victoria BC", 
                "addressType": "Work",
                "expired": "No",
                "csNumber": "123456788",
                "recordSealed": "Yes",
                "gender": "Male",
                "currentName": "Bob Ross",
                "location": "VICTORIA",
                "pcm": "Gillis, Mike",
                "photoURL": "https://www.w3schools.com/images/lamp.jpg", 
                "datePhotoTaken": "2022-03-02",
                "address": [
                    {
                        "street": "123 Hello St",
                        "city": "Victoria",
                        "postalCode": "123 abc"
                    }
                ]
            },
            {
                "id": "10",
                "fullName": "Smith, Sam",
                "clientAge": 40,
                "birthDate": "1983-02-03",
                "fullAddress": "123 Hello St, Victoria BC", 
                "addressType": "Home",
                "expired": "No",
                "csNumber": "123456789",
                "recordSealed": "Yes",
                "gender": "Male",
                "currentName": "Sam Smith",
                "location": "VICTORIA",
                "pcm": "Gillis, Mike",
                "photoURL": "https://www.w3schools.com/images/stickman.gif", 
                "datePhotoTaken": "2022-03-02",
                "address": [
                    {
                        "street": "123 Hello St",
                        "city": "Victoria",
                        "postalCode": "123 abc"
                    }
                ]
            }
          ];
        if (error) {
          console.error(error);
        }
      }
       
    }
  },
}
</script>

<style >
.datatable-header-bg-style {
  background: #154c79;
}
.datatable-header-text-style {
  color: #ffffff !important;
}
.wild-search-text {
  color: #154c79;
  font-size: 0.5em;
  text-align: right;
}
.primary {
  background-color: #1867c0 !important;
  border-color: #1867c0 !important;
}
</style>