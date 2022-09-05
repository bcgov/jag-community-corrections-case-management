<template>
  <div>
    <section class="mb-3">
      <div class="row justify-content-between mb-2">
        <div class="col-sm-6">
          <h1 class="font-weight-bold">Client Record</h1>
        </div>
        <div class="col-sm-3 text-right pr-4">
          <section class="row">
            <div class="col-sm-4" >
              <strong>Name:</strong>
              <p>Smith, John</p>
            </div>
            <div class="col-sm-4" >
              <strong>CS#</strong>
              <p>123456780</p>
            </div>
            <div class="col-sm-4" >
              <strong>Date of Birth</strong>
              <p>1989-02-01</p>
            </div>
          </section>
        </div>
      </div>
    </section>
    <v-tabs
      v-model="tab"
      fixed-tabs
      color="deep-purple accent-4"
      
    >
      <v-tab v-for="item in items" :key="item.tab">
        {{ item.tab }}
      </v-tab>
      
    </v-tabs>
    <v-tabs-items v-model="tab">
      <v-tab-item v-for="item in items" :key="item.tab">
        <FormioClientProfile v-if="item.content === 'cp'" :csNumber="$route.params.csNumber" :clientID="$route.params.clientID"></FormioClientProfile>
        <RNAListView v-if="item.content === 'rl'" :clientNum="$route.params.clientID"></RNAListView>
        <span v-else> {{item.content}}</span>
      </v-tab-item>
    </v-tabs-items>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import FormioClientProfile from "@/components/common/FormioClientProfile.vue";
import RNAListView from '@/views/RNAList.vue'

export default {
  name: "FormioClientRecord",
  data() {
    return {
      theKey: 0,
      tab: null,
      items: [
          { tab: 'Community Profile', content: 'cp' },
          { tab: 'Trend Analysis', content: 'ta' },
          { tab: 'RNA List', content: 'rl' },
          { tab: 'Intervention Summary', content: 'is' },
          
        ],
    }
  },
  components: {
    FormioClientProfile,
    RNAListView,
  },
  mounted() {
    let clientID= this.$route.params.clientID;
    let csNumber= this.$route.params.csNumber;
  }
}
</script>

<style>
</style>