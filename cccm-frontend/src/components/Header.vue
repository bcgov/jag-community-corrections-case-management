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
        <div class="col-sm-11 m-7">
          <strong>Select a new office</strong>
          <v-col
            class="d-flex"
            cols="12"
            sm="12"
          >
            <v-select
                item-text="value"
                item-value="key"
                v-model="selectedLocation"
                :items="locationTypes"
                label=""
                outlined
            >
            </v-select>
          </v-col>
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
    <div class="header-hidden">
      <v-idle
        @idle="onidle"
        @remind="onremind"
        :reminders="[60, 300, 600]" 
        :events="['keypress','mousedown']"
        :duration="900"
        />
    </div>
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
          <a v-if="hasSupervisorDash" :href="`${baseURL}${$ROUTER_NAME_DASHBOARDSUPERVISOR}`" class="headerA">My Team Dashboard</a> <span v-if="hasSupervisorDash"> | </span>
          <a v-if="hasPODash" :href="`${baseURL}${$ROUTER_NAME_DASHBOARDPO}`" class="headerA">My Client Dashboard</a> <span v-if="hasPODash"> | </span>
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
import iZtoast from 'izitoast';

export default {
  name: 'HeaderComponent',
  data() {
    return {
      baseURL: import.meta.env.BASE_URL,
      dialog: false,
      selectedLocation: {key: "", value: ""},
      keyPage: 0
    }
  },
  created() {
    this.locationTypes = [];
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
            
            // fetch the poList for the selected location
            this.mainStore.getPOList(this.mainStore.locationCD);

            // force the page to reload
            location.reload();
            break;
          }
        }
      }
    },
    logout () {
      // clear cached location info
      this.mainStore.clearAll();
      Vue.$keycloak.logout({ redirectUri: window.location.origin + import.meta.env.BASE_URL });
    },
    onidle() {
      iZtoast.error({
          title: 'Session',
          position: 'center',
          message: 'Session has expired'
      });
      this.logout();
    },
    onremind(time) {
      iZtoast.warning({
          title: 'Warning',
          position: 'center',
          message: 'Session is idle: ' + time / 60 + ' minutes remain'
      });
    }
  },
  computed: {
    isUserAuthenticated() {
        return Vue.$keycloak.authenticated;
    },
    hideDashboard() {
      return this.mainStore.isHideDashboard();
    },
    hasSupervisorDash() {
      if (this.mainStore.loginUserGroup == Vue.prototype.$USER_GROUP_SUPERVISOR || 
          this.mainStore.loginUserGroup == Vue.prototype.$USER_GROUP_ADMIN) {
        return true;
      }
      return false;
    },
    hasPODash() {
      if (this.mainStore.loginUserGroup == Vue.prototype.$USER_GROUP_SUPERVISOR || 
          this.mainStore.loginUserGroup == Vue.prototype.$USER_GROUP_ADMIN ||
          this.mainStore.loginUserGroup == Vue.prototype.$USER_GROUP_PO) {
        return true;
      }
      return false;
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


