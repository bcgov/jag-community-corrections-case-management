<script setup lang="ts">
    import CrnaCmpForm from '@/components/crnaCmpForm.vue'
</script>

<template>
  <div class="about">
    <CrnaCmpForm />
  </div>
</template>

<style>
@media screen and (min-width: 768px) {
  .about {
    min-height: 100vh;
    display: flex;
    align-items: center;
  }
}
</style>