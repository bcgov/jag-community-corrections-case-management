<template>
  <div class="dashboard-table-header-ul float-right pr-3 mr-3">
    <ul>
      <li>Due today or overdue</li>
      <li>Due within 1 to 14 days</li>
      <li>Due in over 14 days</li>
    </ul>
  </div>
</template>

<script lang="ts">
import * as DateFns from 'date-fns';

export default {
  name: "DashboardDueDateLegend",
  methods: {
    getColor(dueDateStr: Date) {
      if (dueDateStr == null) {
        return "";
      }
      const dueDate = new Date(dueDateStr);
      if (DateFns.isValid(dueDate) === false) {
        console.error("invalid dueDateStr");
        return "";
      }      
      const diffTime = dueDate.getTime() - new Date().getTime();
      const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
      if (diffDays <= 0) {
        return "dashboard-background-color-red";
      }
      if (diffDays >= 1 && diffDays <= 14) {
        return "dashboard-background-color-yellow";
      }
      if (diffDays > 14) {
        return "dashboard-background-color-green";
      }
      return "";
    },
  }
}
</script>

<style scoped>
  .dashboard-table-header-ul ul {
    list-style:  none;
  }

  .dashboard-table-header-ul ul>li {
    padding:0;
    margin:0;
  }
  .dashboard-table-header-ul ul>li::before {
    content: "\2022";
    color: black;
    font-weight: bold;
    display: inline-block;
    position:relative;
    width: 1em;
    font-size:2em;
    top: 5px;
    margin-left: -.5em;
    margin-top: -.25em
  }

  .dashboard-table-header-ul ul>li:nth-child(1)::before {
    color: #FFB9B9;
  }

  .dashboard-table-header-ul ul>li:nth-child(2)::before {
    color: #FFE5BD;
  }

  .dashboard-table-header-ul ul>li:nth-child(3)::before {
    color: #C6E8C5;
  }
</style>
