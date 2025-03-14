<template>
  <div class="min-h-screen flex flex-col">
    <Header />
    <main class="flex-grow container mx-auto py-8 flex flex-col items-center">
      <NewOrganizationForm />
    </main>
    <Footer />
  </div>
</template>

<script setup>
import NewOrganizationForm from '@/components/NewOrganizationForm.vue'
import Footer from "@/components/shared/Footer.vue";
import Header from "@/components/shared/Header.vue";
</script>

<style scoped>
main {
  background: linear-gradient(to right, #f8f9fa, #e9ecef);
  padding: 2rem;
  border-radius: 1rem;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}
</style>