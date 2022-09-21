<template>
  <div data-app>
    <!--Model section for user select a new CAL (current active location)-->
    <v-btn
      id="id_modal"
      v-show=false
      @click.stop="dialog = true"
    ></v-btn>
    <v-dialog
        v-model="dialog"
        persistent
        max-width="550"
    >
      <v-card>
        <div class="col-sm-6 m-7">
          <strong>Select a new location</strong>
          <v-select
              item-text="value"
              item-value="key"
              v-model="selectedLocation"
              :items="locationTypes"
              label=""
              outlined
          >
          </v-select>
        </div>
        <v-card-actions>
          <v-btn
            color="primary"
            dark
            @click="setCurrentActiveLocation"
          >
            Select
          </v-btn>
          <v-spacer></v-spacer>
          <v-btn
            class="ma-2"
            @click="dialog = false"
          >
            Cancel
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!--Header section-->
    <div class="header">
      <div class="header-section">
        <div class="header-img">
          <a href="https://gov.bc.ca"><img src="@/assets/gov_bc_logo.svg" width="187px" border="0" /></a>
        </div>
        <span class="headerText textShadow">BC Corrections - <a href="#" @click="handleShowModal">{{ mainStore.locationDescription }}</a>
      </span>
        <span v-if="isUserAuthenticated" class="header-section usernameText col-sm-4 float-right">
          <i class="fa fa-user"></i>  &nbsp;<strong>{{getUserName}}</strong> &nbsp;|&nbsp;
          <a @click="logout">
            <span>Logout</span>
          </a>
        </span>
      </div>
      <div class="headerNavContainer">
        <span class="headerNav textShadow">
          <a :href="`${baseURL}`" class="headerA">My Dashboard</a> |
          <a :href="`${baseURL}clientsearch`" class="headerA">Client Search</a> 
        </span>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import Vue from 'vue'
import {useStore} from "@/stores/store";
import {mapStores} from 'pinia';

export default {
  name: 'HeaderComponent',
  data() {
    return {
      baseURL: import.meta.env.BASE_URL,
      dialog: false,
      selectedLocation: {key: "", value: ""},
      locationTypes: [],
    }
  },
  mounted() {
    this.selectedLocation.key = this.mainStore.locationCD;
    this.selectedLocation.value = this.mainStore.locationDescription;
    this.locationTypes = this.mainStore.locations;
  },
  methods: {
    handleShowModal() {
      console.log("Show modal");
      let modal = document.getElementById("id_modal");
      if (modal != null) {
        modal.click();
      }
    },
    setCurrentActiveLocation() {
      // Stop the dialog
      this.dialog = false;

      // Push the newly selected location into store
      if (this.locationTypes != null) {
        for (let i = 0; i < this.locationTypes.length; i++) {
          if (this.locationTypes[i].key == this.selectedLocation) {
            // Store the newly selected location into store
            this.mainStore.locationCD = this.locationTypes[i].key;
            this.mainStore.locationDescription = this.locationTypes[i].value;
            break;
          }
        }
      }
    },
    logout () {
      // clear cached location info
      this.mainStore.clearCachedLocation();
      this.mainStore.clearCachedUserLocations();
      Vue.$keycloak.logout({ redirectUri: window.location.origin + import.meta.env.BASE_URL });
    }
  },
  computed: {
    isUserAuthenticated() {
        return Vue.$keycloak.authenticated;
    },
    getUserName() {
        return Vue.$keycloak.tokenParsed.preferred_username;
    },
    // note we are not passing an array, just one store after the other
    // each store will be accessible as its id + 'Store', i.e., mainStore
    ...mapStores(useStore)
  },
}
</script>


