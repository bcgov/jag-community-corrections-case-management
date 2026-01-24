<template>
  <v-row>
    <v-col cols="1" class="pl-3 pr-2">
      <v-select
          solo
          :items="pageFilterList"
          v-model="pageItems"
          dense
          item-color="primary"
          @input="onPageItemsUpdate"
      ></v-select>
    </v-col>
    <v-col cols="11">
      <v-pagination
          v-model="currentPage"
          :total-visible="7"
          :length="pageCount"
          @input="$emit('update:page', currentPage);"
      ></v-pagination>
    </v-col>
  </v-row>
</template>

<script lang="ts">
import { APP_GLOBALS } from '@/constants/appGlobals';

export default {
  name: "DatatablePagination",
  props: {
    page: {
      type: Number,
      default: 1
    },
    pageCount: {
      type: Number,
      default: 1
    },
    itemsPerPage: {
      type: Number,
      default: APP_GLOBALS.$CONST_DATATABLE_ITEMS_PER_PAGE
    }
  },
  data() {
    return {
      currentPage: this.page,
      pageItems: this.itemsPerPage,
      pageFilterList: APP_GLOBALS.$CONST_DATATABLE_PAGE_FILTERLIST
    };
  },
  methods: {
    onPageItemsUpdate(event: string) {
      this.pageItems = parseInt(event, 10);
      this.$emit('update:itemsPerPage', this.pageItems);
    }
  }
};
</script>
