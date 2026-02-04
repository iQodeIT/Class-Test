/** @type {import('tailwindcss').Config} */
module.exports = {
  // NOTE: Update this to include the paths to all of your component files.
  content: ["./App.{js,jsx,ts,tsx}", "./src/**/*.{js,jsx,ts,tsx}"],
  presets: [require("nativewind/preset")],
  theme: {
    extend: {
      colors: {
        sage: {
          50: '#f4f7f4',
          100: '#e5ebe5',
          200: '#cedbcd',
          300: '#abc1aa',
          400: '#83a182',
          500: '#688667',
          600: '#516a50',
          700: '#425541',
          800: '#364435',
          900: '#2d382d',
        },
        clay: {
          50: '#fdf8f5',
          100: '#f9eee6',
          200: '#f2d9c9',
          300: '#e8bc9f',
          400: '#db926c',
          500: '#d2691e', // Clay Orange
          600: '#c25c1a',
          700: '#a14a17',
          800: '#813c18',
          900: '#6a3317',
        },
        taupe: {
          50: '#f7f6f5',
          100: '#eceae6',
          200: '#d8d3cd',
          300: '#bbb1a6',
          400: '#9d8e7f',
          500: '#847364', // Taupe
          600: '#6b5d51',
          700: '#594d44',
          800: '#4b413a',
          900: '#413833',
        },
      },
      fontFamily: {
        playfair: ["PlayfairDisplay_400Regular"],
        inter: ["Inter_400Regular"],
      },
    },
  },
  plugins: [],
}
