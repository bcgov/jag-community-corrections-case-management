<template>
  <!--Header section-->
  <div class="header">
    <a href="https://gov.bc.ca"><img src="@/assets/gov_bc_logo.svg" width="150px" border="0" /></a>
    <span class="headerText textShadow">BC Corrections - {{ locationInfo }}</span>
    <span class="headerNav textShadow"><a href="/" class="headerA">Dashboard</a> | <a href="/crnacmp" class="headerA">CRNA/CMP Form</a></span>
  </div>
</template>

<script lang="ts">

export default {
  name: 'HeaderComponent',
  props: {
    locationInfo: ""
  },
}
</script>


