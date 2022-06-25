<template>
  <!--Header section-->
  <div class="header">
    <a href="https://gov.bc.ca"><img src="@/assets/gov_bc_logo.svg" width="150px" border="0" /></a>
    <span class="headerText textShadow">BC Corrections - {{ locationInfo }}
      <span v-if="isUserAuthenticated" class="col-sm-2 text-right">
        <i class="fa fa-user"></i>  &nbsp;<strong>{{getUserName}}</strong> &nbsp;|&nbsp; 
        <a @click="logout">
          <span>Logout</span>
        </a>				
      </span>
    </span>
    <span class="headerNav textShadow">
      <a href="/" class="headerA">Dashboard</a> | 
      <a href="/crnacmp" class="headerA">CRNA/CMP Form</a>
    </span>
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


