import { fileURLToPath } from 'url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vuetify from 'vite-plugin-vuetify'

// https://vitejs.dev/config/
export default defineConfig({
  base: '/cccm',
  server: {
    port: 3000
  },
  plugins: [
    vue(),
    vuetify({ autoImport: true })
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  build: {
    target: 'es2020',
    commonjsOptions: {
      include: [/keycloak-js/, /node_modules/]
    }
  },
  optimizeDeps: {
    include: ['keycloak-js']
  }
})
