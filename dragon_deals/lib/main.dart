import 'package:dragon_deals/screens/onboarding_screen.dart';
import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Dragon Deals',
      theme: ThemeData(
        // Define the default brightness and colors.
        colorScheme: ColorScheme.fromSeed(
          seedColor: const Color(0xFFFF3B30), // Primary: Red
          primary: const Color(0xFFFF3B30),
          secondary: const Color(0xFF2B2D42), // Secondary: Dark Navy
          error: const Color(0xFFF87171),
          // Other colors can be defined here as needed
        ),

        // Define the default font family.
        textTheme: GoogleFonts.poppinsTextTheme(
          Theme.of(context).textTheme,
        ),

        // Use Material 3 design.
        useMaterial3: true,
      ),
      home: const OnboardingScreen(),
      debugShowCheckedModeBanner: false,
    );
  }
}