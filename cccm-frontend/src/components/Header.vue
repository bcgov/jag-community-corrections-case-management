<template>
  <!--Header section-->
  <div class="header">
    <div class="header-section">
      <div class="header-img">
        <a href="https://gov.bc.ca"><img src="@/assets/gov_bc_logo.svg" width="187px" border="0" /></a>
      </div>
      <span class="headerText textShadow">BC Corrections - {{ mainStore.locationDescription }}
      <span v-if="isUserAuthenticated" class="col-sm-2 text-right">
        <i class="fa fa-user"></i>  &nbsp;<strong>{{getUserName}}</strong> &nbsp;|&nbsp;
        <a @click="logout">
          <span>Logout</span>
        </a>
      </span>
    </span>
    </div>
    <div class="headerNavContainer">
      <span class="headerNav textShadow">
        <a :href="`${baseURL}`" class="headerA">My Dashboard</a> |
        <a :href="`${baseURL}clientsearch`" class="headerA">Client Search</a> |
        <!-- <a href="/clientprofile" class="headerA">Client Profile</a> | -->
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
      Vue.$keycloak.logout({ redirectUri: window.location.origin });
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


