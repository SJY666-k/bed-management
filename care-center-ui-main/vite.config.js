import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  // 打包后静态资源使用相对路径
  base: './', 
  server: {
    port: 5173,
    // 监听所有网卡，手机/其他电脑可用「本机局域网 IP:5173」访问（默认只听 127.0.0.1）
    host: true,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
})