import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  esbuild: {
    loader: 'tsx',
    include: /src\/.*\.[jt]sx?$/,
    exclude: []
  },
  optimizeDeps: {
    esbuildOptions: {
      loader: {
        '.js': 'jsx'
      }
    }
  },
  build: {
    outDir: 'build',
    cssCodeSplit: false,
    rollupOptions: {
      output: {
        // Single bundle output - no code splitting
        // IIFE format for embedding in regular <script> tags
        format: 'iife',
        manualChunks: undefined,
        inlineDynamicImports: true,
        entryFileNames: 'static/js/webtau-report.js',
        assetFileNames: 'static/css/webtau-report.[ext]'
      }
    }
  }
})
