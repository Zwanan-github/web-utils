import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { fileURLToPath, URL } from 'node:url'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
    extensions: [
      '.js',
      '.json',
      '.mjs',
      '.ts',
      '.vue',
    ],
  },
  server: {
    host: true,
    port: 80,
    proxy: {
      // api请求转发至后端
      '/api/': {
        target: 'http://localhost:8080/',
        // target: 'https://zwanan.top:8080/',
        changeOrigin: true,
        followRedirects: true,
        rewrite: (path) => path.replace(/^\/api/, ''),
      },
    },
  }
})
