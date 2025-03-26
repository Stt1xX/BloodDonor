<template>
  <div class="not-found">
    <h1>404 - Страница не найдена</h1>
    <p>К сожалению, запрашиваемая страница не существует.</p>
    <router-link to="/">Вернуться на главную</router-link>
  </div>
</template>

<style scoped>
.not-found {
  text-align: center;
}
h1 {
  font-size: 2rem;
  color: red;
}
p {
  font-size: 1.2rem;
}
</style>
