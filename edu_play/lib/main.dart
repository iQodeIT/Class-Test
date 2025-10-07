import 'package:edu_play/screens/onboarding_screen.dart';
import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'EduPlay',
      theme: ThemeData(
        // Define the default brightness and colors.
        colorScheme: ColorScheme.fromSeed(
          seedColor: const Color(0xFF3B82F6), // Primary: Sky Blue
          primary: const Color(0xFF3B82F6),
          secondary: const Color(0xFFFACC15), // Accent: Warm Yellow
          error: const Color(0xFFF87171), // Secondary: Coral Red
          // Other colors can be defined here as needed
        ),

        // Define the default font family.
        textTheme: GoogleFonts.nunitoTextTheme(
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