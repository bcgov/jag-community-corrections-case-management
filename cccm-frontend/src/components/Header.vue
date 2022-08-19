<template>
  <!--Header section-->
  <div class="header">
    <div class="header-section">
      <div class="header-img">
        <a href="https://gov.bc.ca"><img src="@/assets/gov_bc_logo.svg" width="187px" border="0" /></a>
      </div>
      <span class="headerText textShadow">BC Corrections - {{ locationInfo }}
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
        <a href="/" class="headerA">My Dashboard</a> |
        <a href="/clientsearch" class="headerA">Client Search</a> |
        <!-- <a href="/clientprofile" class="headerA">Client Profile</a> | -->
        <a href="/crnacmp" class="headerA">CRNA/CMP Form</a>
      </span>
    </div>
  </div>
</template>

<script lang="ts">
import Vue from 'vue'

export default {
  name: 'HeaderComponent',
  props: {
    locationInfo: "",
  },
  methods: {
    logout () {
      Vue.$keycloak.logout({ redirectUri: window.location.origin })
    }
  },
  computed: {
    isUserAuthenticated() {
        return Vue.$keycloak.authenticated;
    },
    getUserName() {
        return Vue.$keycloak.tokenParsed.preferred_username;
    }
  },
}
</script>


