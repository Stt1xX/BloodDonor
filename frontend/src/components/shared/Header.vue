<template>
  <header class="bg-red-600 text-white py-4 shadow-md">
    <div class="container mx-auto flex justify-between items-center">
      <div class="flex items-center">
        <img src="@/assets/logo.png" alt="Logo" class="w-10 h-10 mr-3" />
        <h1 class="text-2xl font-bold">Blood Link</h1>
      </div>
      <nav class="flex space-x-4">

      </nav>
    </div>
  </header>
</template>

<script setup>
</script>

<style scoped>
header {
  background-color: #e3342f;
  color: white;
  padding: 1rem 0;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 1rem;
}

nav a {
  transition: color 0.3s ease;
}

nav a:hover {
  color: #d3d3d3;
}
</style>