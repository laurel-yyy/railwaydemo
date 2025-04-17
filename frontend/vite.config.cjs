const { defineConfig } = require('vite');
const react = require('@vitejs/plugin-react');
const tailwindcss = require("tailwindcss");
const path = require('path');

module.exports = defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src')
    }
  },

  css: {
    postcss: {
      plugins: [tailwindcss()],
    }
  }
})