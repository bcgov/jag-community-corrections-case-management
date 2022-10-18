<template>
    <!--Case plan section-->
    <div>
        <v-progress-linear v-if="loading" indeterminate height="30" color="primary">Loading case plan</v-progress-linear>
        <Form v-if="!loading" :key="keyCaseplan" :form="dataModel" :submission="initData" />
    </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Form } from 'vue-formio';
import {updateForm, getCasePlanIntervention} from "@/components/form.api";

export default {
  name: 'FormCasePlan',
  props: {
    dataModel: {},
    initData: {},
    clientFormId: 0,
    csNumber: ''
  },
  components: {
    Form
  },
  data() {
    return {
        keyCaseplan: 0,
        loading: false,
    }
  },
  mounted() {
    console.log("dataModel: ", this.dataModel);
    this.getCasePlanInterventionAPI();

    console.log("After: ", this.initData.data.interventions);
    if (this.initData.data.interventions != null) {
        for (let i = 0; i < this.initData.data.interventions.length; i++) {
            let itv = this.initData.data.interventions[i];
            console.log("itv: ", itv);
            if (itv != null && itv.navKey != null) {
                console.log("itv.navKey: ", itv.navKey);
                let editLink = document.getElementById("editLink_" + itv.navKey);
                console.log("editLink: ", editLink);
                if (editLink != null) {
                    console.log("register editLink click event: ", editLink);
                    editLink.addEventListener("click", this.editMe);
                }
            }
        }
    }
  },
  methods: {
    editMe() {
        console.log("edit me");
    },
    async getCasePlanInterventionAPI() {
        this.loading = true;
        const [error, interventionData] = await getCasePlanIntervention(this.csNumber, this.clientFormId, true);
        if (error) {
            console.error(error);
        } else {
            this.loading = false;
            // add Intervention data to the initData; refresh the page to show it
            this.initData.data.interventions = interventionData;
            console.log("interventionData; ", this.initData.data.interventions);
            this.keyCaseplan++;
        }
    }
  }
}
</script>