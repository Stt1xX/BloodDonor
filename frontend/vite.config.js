import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { fileURLToPath, URL } from 'node:url'

export default defineConfig({
  plugins: [vue()],
  build: {
    outDir: '../backend/src/main/resources/static',
    emptyOutDir: true
  },
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
    port: 5353,
    proxy: {
      '/api': {
        target: 'http://127.0.0.1:8080/',
        ws: true,
        changeOrigin: true,
        secure: false
      }
    }
  },
}) 