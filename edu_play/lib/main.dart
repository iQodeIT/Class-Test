import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';

void main() {
  runApp(const SoulsticeApp());
}

class SoulsticeApp extends StatelessWidget {
  const SoulsticeApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Soulstice',
      theme: ThemeData(
        useMaterial3: true,
        colorScheme: ColorScheme.fromSeed(
          seedColor: const Color(0xFF8A9A5B), // Sage Green
          primary: const Color(0xFF8A9A5B),
          secondary: const Color(0xFFC07A50), // Clay Orange
          tertiary: const Color(0xFF483C32),  // Taupe
          surface: const Color(0xFFFDFBF7),   // Neutral/Cream
        ),
        textTheme: GoogleFonts.interTextTheme(
          Theme.of(context).textTheme,
        ).copyWith(
          displayLarge: GoogleFonts.playfairDisplay(
            textStyle: Theme.of(context).textTheme.displayLarge,
            fontWeight: FontWeight.bold,
          ),
          displayMedium: GoogleFonts.playfairDisplay(
            textStyle: Theme.of(context).textTheme.displayMedium,
            fontWeight: FontWeight.bold,
          ),
          displaySmall: GoogleFonts.playfairDisplay(
            textStyle: Theme.of(context).textTheme.displaySmall,
            fontWeight: FontWeight.bold,
          ),
          headlineLarge: GoogleFonts.playfairDisplay(
            textStyle: Theme.of(context).textTheme.headlineLarge,
            fontWeight: FontWeight.bold,
          ),
          headlineMedium: GoogleFonts.playfairDisplay(
            textStyle: Theme.of(context).textTheme.headlineMedium,
            fontWeight: FontWeight.bold,
          ),
          headlineSmall: GoogleFonts.playfairDisplay(
            textStyle: Theme.of(context).textTheme.headlineSmall,
            fontWeight: FontWeight.bold,
          ),
        ),
      ),
      home: const PlaceholderHomeScreen(),
      debugShowCheckedModeBanner: false,
    );
  }
}

class PlaceholderHomeScreen extends StatelessWidget {
  const PlaceholderHomeScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(
          'Soulstice',
          style: Theme.of(context).textTheme.headlineMedium,
        ),
        centerTitle: true,
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text(
              'Welcome to Soulstice',
              style: Theme.of(context).textTheme.headlineLarge,
            ),
            const SizedBox(height: 20),
            const Text('Core Data Architecture Foundation is being built...'),
          ],
        ),
      ),
    );
  }
}
