/** @type {import('tailwindcss').Config} */
module.exports = {
  // NOTE: Update this to include the paths to all of your component files.
  content: ["./app/**/*.{js,jsx,ts,tsx}", "./components/**/*.{js,jsx,ts,tsx}"],
  presets: [require("nativewind/preset")],
  theme: {
    extend: {
      colors: {
        background: "#0D0D1A",
        cardBg: "#1A1A2E",
        surface: "#16213E",
        accentGreen: "#00F5A0",
        accentOrange: "#FF6B35",
        textPrimary: "#FFFFFF",
        textSecondary: "#A0A0B0",
        border: "#2A2A4A",
        danger: "#FF4757",
      },
    },
  },
  plugins: [],
};
