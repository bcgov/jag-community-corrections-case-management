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
    <v-card class="p-2">
      <v-card-title>
        Results
        <v-spacer></v-spacer>
        <div class="w-25">
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
        :key="key_clientsearchresult"
        :headers="clientHeaders"
        :items="clients"
        :single-expand="singleExpand"
        :expanded.sync="expanded"
        @item-expanded="expandRow"
        item-key="clientID"
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
        <template v-slot:item.fullName="{ item }">
          <a :href="`${baseURL}clientprofile/${item.clientID}/${item.csNumber}`" >{{item.fullName}}</a>
        </template>
        <!--Customize the expanded item to show photo and more-->
        <template v-slot:expanded-item="{ headers, item }">
          <td :colspan="1"></td>
          <td :colspan="1">
            <figure>
              <img :src="item.photoData" alt="Client photo" width="132" height="132" />
              <figcaption>Photo Taken: {{item.datePhotoTaken}}</figcaption>
            </figure>
          </td>
          <td :colspan="1">
            <strong>Current Name</strong>
            <br />
            {{ item.currentName }}
          </td>
          <td :colspan="1">
            <strong>Gender</strong>
            <br />
            {{ item.gender }}
          </td>
          <td :colspan="1">
            <strong>Location</strong>
            <br />
            {{ item.location }}
          </td>
          <td :colspan="1">
            <strong>PCM</strong>
            <br />
            {{ item.pcm }}
          </td>
        </template>
      </v-data-table>
      <br/>
      <!--Customize the footer-->
      <div v-if="!loading" class="text-center pt-2">
        <v-row class="flex justify-content-between">
          <v-col cols="1" sm="1">
            <v-select
              solo
              :items="items"
              value=5
              dense
              item-color="primary"
              @input="itemsPerPage = parseInt($event, 10)"
            ></v-select>
          </v-col>
          <v-col cols="5" sm="5" class="text-right">
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
import {Component, Vue} from 'vue-property-decorator';
import {Form} from 'vue-formio';
import {clientSearch, photoSearch} from "@/components/form.api";
import template from '@/components/common/templateClientSearch.json';

export default {
  name: 'FormioClientSearch',
  data() {
    return {
      key_clientsearchresult: 0,
      formInfoTemplate: template,
      formJSON: {},
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
        { text: 'Name', align: 'start', sortable: true, value: 'fullName' },
        { text: 'Current Name?', value: 'currentNameIndicator' },
        { text: 'Age', value: 'clientAge' },
        { text: 'Date of Birth', value: 'birthDate' },
        { text: 'Address', value: 'fullAddress' },
        { text: 'Address Type', value: 'addressType' },
        { text: 'Expired?', value: 'expired' },
        { text: 'CS#', value: 'csNumber' },
        { text: 'Record Sealed?', value: 'recordSealed' },
      ],
      clients: [],
      baseURL: import.meta.env.BASE_URL,
    }
  },
  components: {
    Form
  },
  mounted() {
    this.buildForm()
  },
  methods: {
    expandRow ({ item, value }) {
      // call searchPhotoAPI only when the photo hasn't loaded.
      if (item.photoData == null) {
        this.handlePhotoSearch(item.clientID);
      }
    },
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
      //         "clientID": "1",
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
        let address = evt.data.address;
        let age = evt.data.age;
        let birthYear = evt.data.dobYear;
        let clientNum = evt.data.idNumber;
        let gender = evt.data.gender;
        let location = evt.data.city;
        let name = evt.data.lastName;
        let officer = "";
        let soundex = evt.data.lastNameSoundex;
        
        const [error, response] = await clientSearch(address, age, birthYear, clientNum,
            gender, location, name, officer, soundex);
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
                "clientID": 10010101,
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
                "clientID": 20010101,
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
                "clientID": 20010103,
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
                "clientID": 100153101,
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
                "clientID": 10048392,
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
                "clientID": 28398322,
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
                "clientID": 38440221,
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
                "clientID": 43502022,
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
                "clientID": 430493242,
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
                "clientID": 1324233555,
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

        // Might need to be removed if backend returns the currentNameIndicator
        // populate currentNameIndicator fields
        this.clients = this.clients.filter(el => {
          let currentNameIndicator = "Yes";
          // console.log("fullName: ", el.fullName);
          // console.log("currentName: ", el.currentName);
          // let fnArray = el.fullName.trim().split(",");
          // let cnArray = el.currentName.trim().split(" ");
          // if ((fnArray == null || fnArray.length !== 2) && 
          //     (cnArray == null || cnArray.length !== 2)) {
          //   if (fnArray[0].trim() == cnArray[1].trim() && fnArray[1].trim() == cnArray[0].trim()) {
          //     currentNameIndicator = "Yes";
          //   }
          // };
          
          el.currentNameIndicator = currentNameIndicator;
          return el;
        });
          
        if (error) {
          console.error(error);
        }
      }
    },
    async handlePhotoSearch(clientID) {
      console.log("Photo search for clientID: ", clientID);
      const [error, response] = await photoSearch(clientID);

      // To be deleted, sample data
      let photoArary = ["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"];
      //build sample photo data
      let sd = "";
      for (let el1 of photoArary) {
        sd += el1;
      }

      //Cache the photoData into this.clients object
      if (this.clients != null) {
        for (let el of this.clients) {
          if (el.clientID == clientID) {
            el.photoData = "data:image/png;base64, " + sd;
            el.csNumber = " " + el.csNumber; // force the expanded row to refresh
            break;
          }
        }
      }
      if (error) {
        console.error(error);
      } 
    }
  }
}
</script>

<style>
.wild-search-text {
  display: flex;
  flex-direction: row-reverse;
  justify-content: center;
  align-items: center;
  gap: 20px;
  color: #154c79;
  font-size: 0.5em;

}
.primary {
  background-color: #1867c0 !important;
  border-color: #1867c0 !important;
}
</style>