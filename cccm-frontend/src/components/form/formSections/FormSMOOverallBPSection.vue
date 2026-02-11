<template>
    <!--SMOOverall BP section-->
    <Form :key="formKey" 
        :form="SMOOverallBPSectionFormJSON" 
        :submission="formData" 
        :options="SMOOverallBPSectionOptions"
    ></Form>
</template>
  
<script lang="ts">
import { Form } from '@formio/vue';
import { loadFormData, getFormioTemplate } from "@/components/form.api";
import { useAutosaveStore } from "@/stores/autoSaveStore";
import { mapStores } from 'pinia';

export default {
    name: 'FormSMOOverallBPSection',
    props: {
        clientFormId: 0,
        csNumber: ''
    },
    components: {
        Form
    },
    data() {
        return {
            SMOOverallBPSectionFormJSON : {"display": "form", "components": []},
            SMOOverallBPSectionOptions: {},
            formData: {},
            formKey: 0
        }
    },
    mounted() {
        if (this.autosaveStore.isSavingInProgress()) { 
            // Delay loading summary view data
            console.log("Delay data loading: ");
            setTimeout(() => {
                this.loadPage();
            }, this.timeoutDelay)
        } else {
            console.log("Prompt data loading: ");
            this.loadPage();
        }
    },
    methods: {
        async loadPage() {
            this.SMOOverallBPSectionOptions.readOnly = true;
            const [error, formioTemplate] = await getFormioTemplate(this.csNumber, this.clientFormId);
            if (error) {
                console.error(error);
            } else {
                let BPSection = formioTemplate.components.filter(item => item.label == this.$CONST_LABEL_SMO_OVERALL_BP_SECTION);
                //console.log("BPSEction: ", BPSection);
                if (BPSection != null && BPSection[0] != null && BPSection[0].components.length == 2) {
                    // Exclude the section title which is defined in components[0]
                    const clone = JSON.parse(JSON.stringify(BPSection[0].components[1]));
                    this.SMOOverallBPSectionFormJSON.components[0] = clone;
                    
                    // Load SMO-Overall form data which is in {"data": {"S02Q01": "D", ...}} format
                    this.loadSMOOverallFormData();
                    this.formKey++;
                }
            }
        },
        async loadSMOOverallFormData() {
            // Load SMO_Overall form data
            const [error, clientFormData] = await loadFormData(this.csNumber, this.clientFormId);
            //console.log("client form data: ", clientFormData)
            if (error) {
                console.error(error);
            } else {
                this.formData = clientFormData;
            }
        }
    },
    computed: {
        // note we are not passing an array, just one store after the other
        // each store will be accessible as its id + 'Store', i.e., mainStore
        ...mapStores(useAutosaveStore)
    }
}
</script>