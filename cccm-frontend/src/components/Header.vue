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
        max-width="450"
    >
      <v-card>
        <div class="col-sm-6 m-7">
          <strong>Select a new office</strong>
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
            @click="dialog = false"
          >
            Cancel
          </v-btn>
          <v-spacer></v-spacer>
          <v-btn
            color="primary"
            dark
            @click="setCurrentActiveLocation"
          >
            Select
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
        <span v-if="mainStore.loginUserGroup != null" class="headerText textShadow">BC Corrections - <a href="#" @click="handleShowModal">{{ mainStore.locationDescription }}</a></span>
        <span v-if="isUserAuthenticated" class="header-section usernameText col-sm-4 float-right">
          <i class="fa fa-user"></i>  &nbsp;<strong>{{getUserName}}</strong> &nbsp;|&nbsp;
          <a @click="logout">
            <span>Logout</span>
          </a>
        </span>
      </div>
      <div v-if="mainStore.loginUserGroup != null" class="headerNavContainer">
        <span class="headerNav">
          <a v-if="!hideDashboard" :href="getDashboardURL" class="headerA">My Dashboard</a> <span v-if="!hideDashboard"> | </span>
          <a :href="`${baseURL}${$ROUTER_NAME_CLIENTSEARCH}`" class="headerA">Client Search</a> 
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
      keyPage: 0
    }
  },
  mounted() {
    this.getLocationInfo();
  },
  methods: {
    async getLocationInfo() {
      const [error, locations] = await this.mainStore.getUserLocations();
      if (error) {
        console.log(error);
      } else {
        this.locationTypes = this.mainStore.locations;
        const [error1, defaultLocation] = await this.mainStore.getUserDefaultLocation();
        if (error1) {
          console.error(error1);
        } else {
          this.selectedLocation.key = this.mainStore.locationCD;
          this.selectedLocation.value = this.mainStore.locationDescription;
          this.locationTypes = this.mainStore.locations;
          this.keyPage++;
        }
      }
    },
    handleShowModal() {
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
      this.mainStore.clearAll();
      Vue.$keycloak.logout({ redirectUri: window.location.origin + import.meta.env.BASE_URL });
    }
  },
  computed: {
    isUserAuthenticated() {
        return Vue.$keycloak.authenticated;
    },
    hideDashboard() {
      return this.mainStore.isHideDashboard();
    },
    getDashboardURL() {
      if (this.mainStore.loginUserGroup == Vue.prototype.$USER_GROUP_SUPERVISOR || 
          this.mainStore.loginUserGroup == Vue.prototype.$USER_GROUP_ADMIN) {
        return this.baseURL + Vue.prototype.$ROUTER_NAME_DASHBOARDSUPERVISOR;
      } else if (this.mainStore.loginUserGroup == Vue.prototype.$USER_GROUP_PO) {
        return this.baseURL + Vue.prototype.$ROUTER_NAME_DASHBOARDPO;
      } else {
        return this.baseURL;
      }
      
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


