<template>
  <div data-app>
    <section class="pr-4 pl-4">
      <v-tabs v-model="current_tab" fixed-tabs color="deep-purple accent-4">
        <v-tab v-for="item in items" :key="item.tab" > 
          <span v-if="item.id === 'cp'">{{ item.tab }}</span>
          <div v-if="item.id === 'saraBtn'" class="p-4">
            <v-btn
              v-show=true
              @click.stop="createSARA"
            ><i class="fa fa-plus"></i>&nbsp; Add SARA-CMP Form</v-btn>
          </div>
        </v-tab>
      </v-tabs>
      <v-tabs-items v-model="current_tab">
        <v-tab-item v-for="item in items" :key="item.tab">
          <div v-if="item.id === 'cp'" class="p-4">
            <CrnaCmpForm :formId="formId"></CrnaCmpForm>
          </div>
        </v-tab-item>
      </v-tabs-items>
    </section>
  </div>
</template>

<script lang="ts">
import Vue from 'vue'
import { Component } from 'vue-property-decorator';
import CrnaCmpForm from "@/components/crna-cmp/crnaCmpForm.vue";
import { createForm } from "@/components/form.api";

export default {
  name: 'CRNACMP',
  data() {
    return {
      formId: '',
      newCreatedFormId: 0,
      clientNum: '',
      current_tab: 'tab-cp',
      items: [
          { tab: 'CRNA-CMP', id: 'cp' },
          { tab: '', id: 'saraBtn' }
        ],
    }
  },
  mounted(){
    this.formId = this.$route.params.formID;
    this.clientNum = this.$route.params.csNumber;
  },
  methods: {
    createSARA() {
      console.log("create sara");
    },
    async createFormAPI() {
      const [error, response] = await createForm(this.formId);
      if (error) {
        console.error(error);
      } 
      newCreatedFormId = response.formID;
    },
    createSARA() {
      this.createFormAPI();
      this.$router.push({
        name: 'saracmp',
        params: {
          formID: this.newCreatedFormId,
          csNumber: this.clientNum
        }
      });
    }
  },
  components: {
    CrnaCmpForm,
  }
}
</script>


