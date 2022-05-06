<template>
  <div class="header">
        <header>
          <div class="banner">
            <a display:block href="https://gov.bc.ca">
              <img src="/src/assets/images/logo-banner.svg" alt="Go to the Government of British Columbia website" height="50px"/>
            </a>
            <div id="id_location">BC Corrections {{ locationInfo }}</div>
          </div>
          <div class="other">
            <a class="nav-btn">
              <i class="fas fa-bars" id="menu"></i>
            </a>
          </div>
        </header>
        <nav class="navigation-main" id="navbar">
          <div class="container">
            <ul>
              <li><a href="/" class="active">Dashboard</a></li>
              <li><a href="/crnacmp">CRNA-CMP</a></li>
            </ul>
          </div>
        </nav>
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


