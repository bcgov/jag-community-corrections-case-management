<template>
  <!--Header section-->
  <div class="header">
    <div class="header-section">
      <div class="header-img">
        <a href="https://gov.bc.ca"><img src="@/assets/gov_bc_logo.svg" width="187px" border="0" /></a>
      </div>
      <span class="headerText textShadow">BC Corrections - {{ mainStore.locationDescription }}
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
        <a :href="`${baseURL}clientsearch`" class="headerA">Client Search</a> |
        <a :href="`${baseURL}clientrecord/clientID/csnum`" class="headerA">Client Record</a> |
        <a :href="`${baseURL}crnacmp/1`" class="headerA">CRNA CMP Form</a> |
        <a :href="`${baseURL}saracmp/1`" class="headerA">SARA CMP Form</a> |
        <a :href="`${baseURL}rnalist`" class="headerA">RNA List</a> |
      </span>
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
    }
  },
  methods: {
    logout () {
      // clear cached location info
      this.mainStore.clearCachedLocation();
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


