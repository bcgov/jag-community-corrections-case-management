<template>
  <div data-app>
    <Form :form="formJSON" v-on:clientSearchEvent="handleClientSearch"/>
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
        >
        <template v-slot:header="{ props: { headers } }">
          <thead>
            <tr class="some-other-style">
              <th v-for="h in headers" :class="h.class">
                <span>{{h.text}}</span>
              </th>
            </tr>
          </thead>
        </template>
        <!-- <template v-slot:top>
          <v-toolbar flat>
            <v-toolbar-title>Results</v-toolbar-title>
          </v-toolbar>
        </template> -->
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
    </v-card>
    <br/><br/><br/><br/><br/>
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
      totalDesserts: 0,
      loading: true,
      options: {
          page: 1,
          itemsPerPage: 2
        },
      search: '',
      expanded: [],
      singleExpand: false,
      clientHeaders: [
        {
          text: 'Name',
          align: 'start',
          sortable: true,
          value: 'fullName',
          class: 'my-header-style'
        },
        { text: 'Age', value: 'clientAge', class: 'my-header-style' },
        { text: 'Date of Bith', value: 'birthDate', class: 'my-header-style' },
        { text: 'Address', value: 'fullAddress', class: 'my-header-style' },
        { text: 'Address Type', value: 'addressType', class: 'my-header-style' },
        { text: 'Expired?', value: 'expired', class: 'my-header-style' },
        { text: 'CS#', value: 'csNumber', class: 'my-header-style' },
        { text: 'Record Sealed?', value: 'recordSealed', class: 'my-header-style' },
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
    async handleClientSearch(payload) {
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
      console.log("handleClientSearch", payload);
      this.loading = true
      const [error, response] = await clientSearch(payload);
      this.totalDesserts = response.length;
      this.loading = false;
      this.key_clientsearchresult++;
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
              "csNumber": "123456789",
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
          },
          {
              "id": "3",
              "fullName": "Ross, Bob",
              "clientAge": 44,
              "birthDate": "1979-12-03",
              "fullAddress": "123 Hello St, Victoria BC", 
              "addressType": "Work",
              "expired": "No",
              "csNumber": "123456789",
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
          },
          {
              "id": "5",
              "fullName": "Ross, Bob",
              "clientAge": 44,
              "birthDate": "1979-12-03",
              "fullAddress": "123 Hello St, Victoria BC", 
              "addressType": "Work",
              "expired": "No",
              "csNumber": "123456789",
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
          },
          {
              "id": "7",
              "fullName": "Ross, Bob",
              "clientAge": 44,
              "birthDate": "1979-12-03",
              "fullAddress": "123 Hello St, Victoria BC", 
              "addressType": "Work",
              "expired": "No",
              "csNumber": "123456789",
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
          },
          {
              "id": "9",
              "fullName": "Ross, Bob",
              "clientAge": 44,
              "birthDate": "1979-12-03",
              "fullAddress": "123 Hello St, Victoria BC", 
              "addressType": "Work",
              "expired": "No",
              "csNumber": "123456789",
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
  },
}
</script>

<style scoped>
.some-other-style {
  background: #154c79;
}
.my-header-style {
  color: #ffffff !important;
}

</style>