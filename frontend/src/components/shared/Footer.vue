<template>
  <div class="w-full bg-gradient-to-r from-red-600 to-red-700 text-white">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
        <div>
          <h3 class="text-sm font-semibold tracking-wider uppercase">О проекте</h3>
          <p class="mt-4 text-base">
            BloodLink - система управления банком крови, помогающая спасать жизни
          </p>
        </div>

        <div>
          <h3 class="text-sm font-semibold tracking-wider uppercase">Контакты</h3>
          <ul class="mt-4 space-y-2">
            <li>
              <p class="text-base">
                8 (960) 760-62-06
              </p>
            </li>
            <li>
              <p class="text-base">
                ramtim1@mail.ru
              </p>
            </li>
          </ul>
        </div>

        <div>
          <h3 class="text-sm font-semibold tracking-wider uppercase">Правовая информация</h3>
          <ul class="mt-4 space-y-2">
            <li>
              <a href="#" class="text-base hover:text-gray-200">
                Политика конфиденциальности
              </a>
            </li>
            <li>
              <a href="#" class="text-base hover:text-gray-200">
                Условия использования
              </a>
            </li>
          </ul>
        </div>
      </div>

      <div class="mt-8 border-t border-red-500 pt-8 flex justify-between items-center">
        <p class="text-base">
          © {{ new Date().getFullYear() }} BloodLink. Все права защищены.
        </p>
        <div class="flex space-x-6">
          <a href="#" class="hover:text-gray-200">
            <span class="sr-only">VK</span>
            <svg class="h-6 w-6" fill="currentColor" viewBox="0 0 24 24">
            </svg>
          </a>
          <a href="https://t.me/Stt0xX" class="hover:text-gray-200">
            <span class="sr-only">Telegram</span>
            <svg class="h-6 w-6" fill="currentColor" viewBox="0 0 24 24">
              <path d="M12 0C5.373 0 0 5.373 0 12s5.373 12 12 12 12-5.373 12-12S18.627 0 12 0zm5.894 8.221l-1.97 9.28c-.145.658-.537.818-1.084.508l-3-2.21-1.446 1.394c-.14.18-.357.223-.548.223l.18-2.935 5.36-4.82c.23-.21-.054-.33-.354-.12l-6.62 4.17-2.86-.89c-.62-.194-.63-.62.13-.92l11.17-4.29c.52-.19.98.13.8.72z"/>
            </svg>
          </a>
        </div>
      </div>
  </div>
</div>
</template>

<script setup>
// No additional logic needed
</script>